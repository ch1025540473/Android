package com.wezebra.zebraking.wxapi;/*   作者：LJW on 2015/7/28 20:03
 *   邮箱：eric.lian@wezebra.com
 *   版权：斑马网信科技有限公司
 */

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.wezebra.zebraking.http.CusFailListener;
import com.wezebra.zebraking.http.CusSuccessListener;
import com.wezebra.zebraking.http.TaskResponse;
import com.wezebra.zebraking.http.ZebraTask;
import com.wezebra.zebraking.http.data.WxPayData;
import com.wezebra.zebraking.model.Bill;
import com.wezebra.zebraking.ui.installment.SIAStepThreeResultActivity;
import com.wezebra.zebraking.ui.myaccount.RepaymentActivity;
import com.wezebra.zebraking.util.CommonUtils;
import com.wezebra.zebraking.util.Constants;
import com.wezebra.zebraking.util.PreferenceUtils;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.lang.ref.WeakReference;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

public class WxPayHelper {


    private static WxPayHelper instance = new WxPayHelper();

    private WeakReference<Activity> activityWeakReference;

    public static  WxPayHelper getInstance(){
        return instance;
    }

    private static final String TAG = "WxPayActivity";

    public static final int PAY_FIRST = 1;

    public static final int PAY_REPAYMENT = 2;

    public static final int FAIL_RESULT_CODE = 300;

    private PayReq req;

    private IWXAPI msgApi ;



    private WxPayHelper(){};


    public  void pay(Activity activity, final Bill bill)
    {

       int payType = bill.getType();

       long orderCode = bill.getOrderCode();

        String amount = CommonUtils.formatDouble(bill.getOverCount());

        if(activityWeakReference == null || activityWeakReference.get() == null ){
            activityWeakReference= new WeakReference<Activity>(activity);
        }

        msgApi = WXAPIFactory.createWXAPI(WxPayHelper.this.activityWeakReference.get()
                , null);
        //new 微信支付的接口
        req = new PayReq();
        //注册api
        msgApi.registerApp(Constants.APP_ID);


        Map<String, String> params = new TreeMap<>();
        //设置微信支付参数
        params.put("api", "wxPay");
        //支付的类型，PAY_DIRST 是支付服务费
        if (payType == PAY_FIRST)
        {
            params.put("payfor", "servFee");
        } else
        {   //支付还款余业务，还款业务才有billcode
            params.put("payfor", "repay");
            params.put("billCode",bill.getBillCode()+"");
        }

        Log.e("wx支付",bill.getBillCode()+"");
        //订单号，属于那一期的业务
        params.put("orderCode", orderCode + "");
        //发送一个异步请求
        new ZebraTask.Builder(WxPayHelper.this.activityWeakReference.get(), params, new CusSuccessListener()
        {
            @Override
            public void onSuccess(TaskResponse response)
            {
                WxPayData data = (WxPayData) response.getData();

                PreferenceUtils.setOutTradeNo(data.getOutTradeNo());
                PreferenceUtils.setPayType(bill.getType());
                PreferenceUtils.setPayOrderCode(bill.getOrderCode());
                PreferenceUtils.setPayAmount(CommonUtils.formatDouble(bill.getOverCount()));
                //配置参数
                genPayReq(data.getPrepayid());
                Log.e("配置参数",data.toString());
                //发送请求
                sendPayReq();
            }
    }, WxPayData.class).setCusFailListener(new CusFailListener() {
            @Override
            public void onFail(TaskResponse response) {

                if (response.getCode() == 2823) {
                    WxPayData data = (WxPayData) response.getData();
                    Toast.makeText(WxPayHelper.this.activityWeakReference.get(),
                            "errCode: " + data.getErr_code() + "\nerrCodeDes: " + data.getErr_code_des(),
                            Toast.LENGTH_LONG).show();
                } else if (response.getCode() == 2824) {
                    Class clazz;
                    Intent intent = new Intent();
                    if (PreferenceUtils.getPayType() == 1) {
                        clazz = SIAStepThreeResultActivity.class;
                    } else {
                        clazz = RepaymentActivity.class;
                        intent.putExtra("code", PreferenceUtils.getPayOrderCode());
                    }

                    intent.setClass(WxPayHelper.this.activityWeakReference.get(), clazz);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    WxPayHelper.this.activityWeakReference.get().startActivity(intent);
                    Toast.makeText(WxPayHelper.this.activityWeakReference.get(), response.getDesc(), Toast.LENGTH_SHORT).show();
                    //finish();
                } else if (2825 == response.getCode()) {
//                    Intent intent = new Intent(WxPayActivity.this, ApplyActivity.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    startActivity(intent);
                    Toast.makeText(WxPayHelper.this.activityWeakReference.get(), response.getDesc(), Toast.LENGTH_SHORT).show();
                    //context.setResult(FAIL_RESULT_CODE);
                    //finish();
                } else {
                    ZebraTask.handleFailResponse(WxPayHelper.this.activityWeakReference.get(), response);
                }
            }
        }).build().execute();
    }


    //配置支付参数
    private void genPayReq(String prepayId)
    {
        req.appId = Constants.APP_ID;
        req.partnerId = Constants.MCH_ID;
        req.prepayId = prepayId;
        req.packageValue = "Sign=WXPay";
        req.nonceStr = genNonceStr();
        req.timeStamp = String.valueOf(genTimeStamp());

        List<NameValuePair> signParams = new LinkedList<NameValuePair>();
        signParams.add(new BasicNameValuePair("appid", req.appId));
        signParams.add(new BasicNameValuePair("noncestr", req.nonceStr));
        signParams.add(new BasicNameValuePair("package", req.packageValue));
        signParams.add(new BasicNameValuePair("partnerid", req.partnerId));
        signParams.add(new BasicNameValuePair("prepayid", req.prepayId));
        signParams.add(new BasicNameValuePair("timestamp", req.timeStamp));

        req.sign = genAppSign(signParams);
    }

    private String genNonceStr()
    {
        Random random = new Random();
        return MD5.getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
    }
    //得到时间戳  秒数
    private long genTimeStamp()
    {
        return System.currentTimeMillis() / 1000;
    }
    //对订单进行签名
    private String genAppSign(List<NameValuePair> params)
    {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < params.size(); i++)
        {
            sb.append(params.get(i).getName());
            sb.append('=');
            sb.append(params.get(i).getValue());
            sb.append('&');
        }
        sb.append("key=");
        sb.append(Constants.API_KEY);
        //进行MD5加密
        String appSign = MD5.getMessageDigest(sb.toString().getBytes()).toUpperCase();
        return appSign;
    }
    //发送支付请求
    private void sendPayReq()
    {
        msgApi.registerApp(Constants.APP_ID);
        msgApi.sendReq(req);
    }

}
