package utils;
/*   作者：LJW on 2015/7/27 15:15
 *   邮箱：eric.lian@wezebra.com
 *   版权：斑马网信科技有限公司
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.wezebra.zebraking.http.CusSuccessListener;
import com.wezebra.zebraking.http.TaskResponse;
import com.wezebra.zebraking.http.ZebraTask;
import com.wezebra.zebraking.model.Bill;
import com.wezebra.zebraking.model.PayOrder;
import com.wezebra.zebraking.ui.LlPayResultActivity;
import com.wezebra.zebraking.util.CommonUtils;
import com.wezebra.zebraking.util.PreferenceUtils;
import com.wezebra.zebraking.wxapi.WxPayActivity;

import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.TreeMap;

public class LlPayHelper {


    private static  LlPayHelper instance = new LlPayHelper();

    private WeakReference<Activity> activityWeakReference;

    private LlPayHelper(){};

    public static  LlPayHelper getInstance(){
        return instance;
    }

    private Bill bill;

    private Intent intent;
    /**
     * @param currentBill    付款的订单对象
     * @param context 上下文 ，
     * @param info   当时第一次使用连连支付的时候需要把个人信息传递＜姓名和身份证号码＞ 1姓名 2身份证号码
     */
    public void pay(Bill currentBill,Activity context,String... info){

        if(activityWeakReference == null || activityWeakReference.get() == null ){
            activityWeakReference= new WeakReference<Activity>(context);
        }
        if(intent == null){
         intent = new Intent(this.activityWeakReference.get(), LlPayResultActivity.class);
        }

        this.bill = currentBill;
        //连连支付所需参数
        Map<String, String> params = new TreeMap<String,String>();
        params.put("api","lianlianPay");
        params.put("orderCode",bill.getOrderCode()+"");
        if (bill.getType() == WxPayActivity.PAY_FIRST)
        {
            params.put("payfor", "servFee");
        } else
        {   //支付还款余业务，还款业务才有billcode
            params.put("payfor", "repay");
            params.put("billCode",bill.getBillCode()+ "");
        }
        params.put("channel","android");
        //仅仅是第一次使用连连支付的时候才会传递身份证和姓名
        Log.e("info",info.toString());
        if(info.length>0){
            params.put("name",info[0]);
            params.put("idNo",info[1]);
        }


        new ZebraTask.Builder(context, params, new CusSuccessListener() {
            @Override
            public void onSuccess(TaskResponse response) {
                //把订单的数据存起来
                PayOrder payOrder=(PayOrder)response.getData();
                Field[] fields =PayOrder.class.getFields();
                JSONObject object=new JSONObject();
                for(Field f:fields){
                    try {
                        object.put(f.getName(),f.get(payOrder));
                        }catch (Exception e){

                        }

                };
                //二次支付转跳到支付页面
                MobileSecurePayer msp = new MobileSecurePayer();
                //进行支付
                boolean bRet = msp.pay(object.toString(), mHandler,
                Constants.RQF_PAY,LlPayHelper.this.activityWeakReference.get(), false);
            }
        }, PayOrder.class).build().execute();
    }




    private Handler mHandler = createHandler();


    private Handler createHandler() {
        return new Handler() {
            public void handleMessage(Message msg) {
                String strRet = (String) msg.obj;
                switch (msg.what) {
                    case Constants.RQF_PAY: {//如果是当前的订单号
                        JSONObject objContent = BaseHelper.string2JSON(strRet);
                        //得到回复信息
                        String retCode = objContent.optString("ret_code");
                        String retMsg = objContent.optString("ret_msg");
                        String resultPay = objContent.optString("result_pay");
                        //  先判断状态码，状态码为 成功或处理中 的需要 验签
                       /* if (Constants.RET_CODE_SUCCESS.equals(retCode)) {//交易成功
                            //得到支付结果
                            String resulPay = objContent.optString("result_pay");
                            if (Constants.RESULT_PAY_SUCCESS //支付成功
                                    .equalsIgnoreCase(resulPay)) {*/
                                /*BaseHelper.showDialog(LlPayHelper.this.activityWeakReference.get(), "提示",
                                        "支付成功，交易状态码：" + retCode,
                                        android.R.drawable.ic_dialog_alert);
                                        TODO 支付成功后续处理
                                */
                                PreferenceUtils.setPayType(bill.getType());
                                PreferenceUtils.setPayOrderCode(bill.getOrderCode());
                                PreferenceUtils.setPayAmount(CommonUtils.formatDouble(bill.getOverCount()));
                                intent.putExtra("resultPay",resultPay);
                                intent.putExtra("ret_Code",retCode);
                                intent.putExtra("retMsg",retMsg);
                                LlPayHelper.this.activityWeakReference.get().startActivity(intent);
                                //交易成功
                            //} else {
                               /* BaseHelper.showDialog(LlPayHelper.this.activityWeakReference.get(), "提示",
                                        retMsg + "，交易状态码:" + retCode,
                                        android.R.drawable.ic_dialog_alert);*/
                                //交易失败
                            }
/*
                        } else if (Constants.RET_CODE_PROCESS.equals(retCode)) {//处理中
                            String resulPay = objContent.optString("result_pay");
                            if (Constants.RESULT_PAY_PROCESSING//正在处理过程中
                                    .equalsIgnoreCase(resulPay)) {
                         */   /*
                                BaseHelper.showDialog(LlPayHelper.this.activityWeakReference.get(), "提示",
                                        objContent.optString("ret_msg") + "交易状态码："
                                                + retCode,
                                        android.R.drawable.ic_dialog_alert);
                            */
                            }

                       // } else {//否则就是其他支付状态
                            /*BaseHelper.showDialog(LlPayHelper.this.activityWeakReference.get(), "提示", retMsg
                                            + "，交易状态码:" + retCode,
                                    android.R.drawable.ic_dialog_alert);
                            */

                   /*     }
                    }
                    break;
                }
*/                super.handleMessage(msg);
            }
        };

    }

}
