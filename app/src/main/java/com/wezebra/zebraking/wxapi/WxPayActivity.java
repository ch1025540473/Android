package com.wezebra.zebraking.wxapi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.wezebra.zebraking.R;
import com.wezebra.zebraking.http.CusFailListener;
import com.wezebra.zebraking.http.CusSuccessListener;
import com.wezebra.zebraking.http.TaskResponse;
import com.wezebra.zebraking.http.ZebraTask;
import com.wezebra.zebraking.http.data.WxPayData;
import com.wezebra.zebraking.ui.BaseActivity;
import com.wezebra.zebraking.ui.installment.SIAStepThreeResultActivity;
import com.wezebra.zebraking.ui.myaccount.RepaymentActivity;
import com.wezebra.zebraking.util.Constants;
import com.wezebra.zebraking.util.PreferenceUtils;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

public class WxPayActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "WxPayActivity";
    public static final int PAY_FIRST = 1;
    public static final int PAY_REPAYMENT = 2;
    public static final int FAIL_RESULT_CODE = 300;
    private PayReq req;
    private final IWXAPI msgApi = WXAPIFactory.createWXAPI(this, null);

    private TextView tvPayAmount;
    private CheckBox mCheck;
    private int payType;
    private long orderCode;
    private long billCode;
    private String amount;
    private TextView btnPay;
    private TextView tvAgreement;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_wx_pay);
        //new 微信支付的接口
        req = new PayReq();
        //注册api
        msgApi.registerApp(Constants.APP_ID);


        //支付金额的文本
        tvPayAmount = (TextView) findViewById(R.id.pay_amount);
        mCheck = (CheckBox) findViewById(R.id.checkbox);
        btnPay = (TextView) findViewById(R.id.submit);
        //协议
        tvAgreement = (TextView) findViewById(R.id.agreement);

        //支付按钮
        btnPay.setOnClickListener(this);
        //协议监听
        tvAgreement.setOnClickListener(this);
        //得到传递参数
        //支付的类型，是支付服务费还是还款业务

        payType = getIntent().getIntExtra("type", 0);
        orderCode = getIntent().getLongExtra("order", 0);
        amount = getIntent().getStringExtra("amount");
        billCode = getIntent().getLongExtra("bill", 0);
        //设置显示金额
        tvPayAmount.setText("￥" + amount);
    }
    //进行支付
    private void pay()
    {
        Map<String, String> params = new TreeMap<>();
        //设置微信支付参数
        params.put("api", "wxPay");
        //支付的类型，PAY_DIRST 是支付服务费
        if (payType == PAY_FIRST)
        {
            params.put("payfor", "servFee");
        } else
        {   //支付还款余业务
            params.put("payfor", "repay");
            params.put("billCode", billCode + "");
        }
        //订单号，属于那一期的业务
        params.put("orderCode", orderCode + "");
        //发送一个异步请求
        new ZebraTask.Builder(this, params, new CusSuccessListener()
        {
            @Override
            public void onSuccess(TaskResponse response)
            {
                WxPayData data = (WxPayData) response.getData();

                PreferenceUtils.setOutTradeNo(data.getOutTradeNo());
                PreferenceUtils.setPayType(payType);
                PreferenceUtils.setPayOrderCode(orderCode);
                PreferenceUtils.setPayAmount(amount);
                //配置参数
                genPayReq(data.getPrepayid());
                //发送请求
                sendPayReq();
            }
        }, WxPayData.class).setCusFailListener(new CusFailListener()
        {
            @Override
            public void onFail(TaskResponse response)
            {

                if (response.getCode() == 2823)
                {
                    WxPayData data = (WxPayData) response.getData();
                    Toast.makeText(WxPayActivity.this,
                            "errCode: " + data.getErr_code() + "\nerrCodeDes: " + data.getErr_code_des(),
                            Toast.LENGTH_LONG).show();
                } else if (response.getCode() == 2824)
                {
                    Class clazz;
                    Intent intent = new Intent();
                    if (payType == 1)
                    {
                        clazz = SIAStepThreeResultActivity.class;
                    } else
                    {
                        clazz = RepaymentActivity.class;
                        intent.putExtra("code", orderCode);
                    }

                    intent.setClass(WxPayActivity.this, clazz);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    Toast.makeText(WxPayActivity.this, response.getDesc(), Toast.LENGTH_SHORT).show();
                    finish();
                } else if (2825 == response.getCode())
                {
//                    Intent intent = new Intent(WxPayActivity.this, ApplyActivity.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    startActivity(intent);
                    Toast.makeText(WxPayActivity.this, response.getDesc(), Toast.LENGTH_SHORT).show();
                    setResult(FAIL_RESULT_CODE);
                    finish();
                } else
                {
                    ZebraTask.handleFailResponse(WxPayActivity.this, response);
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


    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {   //当点击提交的时候
            case R.id.submit:
//                if (mCheck.isChecked())
//                {
                pay();
//                } else
//                {
//                    Toast.makeText(this, "您需要同意《斑马王国理财服务协议》", Toast.LENGTH_LONG).show();
//                }
                break;
            case R.id.agreement:

                break;
        }
    }
}
