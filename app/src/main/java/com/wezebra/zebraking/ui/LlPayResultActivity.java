package com.wezebra.zebraking.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wezebra.zebraking.R;
import com.wezebra.zebraking.ui.installment.SIAStepThreeResultActivity;
import com.wezebra.zebraking.ui.myaccount.RepaymentActivity;
import com.wezebra.zebraking.ui.myaccount.SelectPayWayActivity;
import com.wezebra.zebraking.util.PreferenceUtils;

import utils.Constants;

public class LlPayResultActivity extends ActionBarActivity implements View.OnClickListener{

    private static final String TAG = "WXPayEntryActivity";
    private LinearLayout layoutSuccess;
    private LinearLayout layoutFail;
    private TextView tvQuestion;
    private TextView tvPayAmount;
    private TextView tvPayOrderCode;
    private String amount;
    private long orderCode;
    private int payType;
    private boolean isPaySuccess = false;
    private TextView btnSuccess;
    private TextView btnFail;
    private Intent intent;
    private String resultPay;
    private String ret_Code;
    private String retMsg;
    private TextView tv_pay_fail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wxpay_entry);

        initView();
        initData();
        initListener();
    }

    private void initListener() {
        btnSuccess.setOnClickListener(this);
        btnFail.setOnClickListener(this);
        tvQuestion.setOnClickListener(this);
    }

    private void initData() {
        intent=getIntent();
        resultPay=intent.getStringExtra("resultPay");
        ret_Code=intent.getStringExtra("ret_Code");
        retMsg=intent.getStringExtra("retMsg");

        if (Constants.RET_CODE_SUCCESS.equals(ret_Code)) {//交易成功
            //得到支付结果,如果支付成功
            if (Constants.RESULT_PAY_SUCCESS //支付成功
                    .equalsIgnoreCase(resultPay)) {
                //设置支付成功可见
                layoutSuccess.setVisibility(View.VISIBLE);
                layoutFail.setVisibility(View.INVISIBLE);
                tvQuestion.setVisibility(View.INVISIBLE);

                payType = PreferenceUtils.getPayType();
                amount = PreferenceUtils.getPayAmount();
                orderCode = PreferenceUtils.getPayOrderCode();
                tvPayAmount.setText("￥" + amount);
                tvPayOrderCode.setText(orderCode + "");
            }
        }else{
            tv_pay_fail.setText(retMsg +"状态码："+ret_Code);
            //设置支付成功不可见
            layoutFail.setVisibility(View.VISIBLE);
            layoutSuccess.setVisibility(View.INVISIBLE);
            tvQuestion.setVisibility(View.VISIBLE);
        }
    }

    private void initView() {
        layoutSuccess = (LinearLayout) findViewById(R.id.success_layout);
        layoutFail = (LinearLayout) findViewById(R.id.fail_layout);
        tvQuestion = (TextView) findViewById(R.id.pay_question);
        tvPayAmount = (TextView) findViewById(R.id.pay_amount);
        tvPayOrderCode = (TextView) findViewById(R.id.pay_order_code);
        btnSuccess = (TextView) findViewById(R.id.btn_pay_success);
        btnFail = (TextView) findViewById(R.id.btn_pay_fail);
        tv_pay_fail=(TextView)findViewById(R.id.pay_fail);
    }

    public void onSuccessClick()
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

        intent.setClass(this,clazz);
        //就是singleTop
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    //如果支付失败，则执行这个方法
    public void onFailClick(){
        Intent intent = new Intent(this,SelectPayWayActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
       // Log.e("羊驼","草泥马！");
        startActivity(intent);
     // finish();
    }
    private void showServiceTelDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("联系客服").setMessage("您希望致电客服400-833-6069吗?")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        Intent intent = new Intent("android.intent.action.CALL", Uri.parse("tel:4008336069"));
                        startActivity(intent);
                    }
                }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {

            }
        }).create().show();
    }


    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btn_pay_success:
                onSuccessClick();
                break;
            case R.id.btn_pay_fail:
               //Log.e("error","草泥马！");
                onFailClick();
                break;
            case R.id.pay_question:
                showServiceTelDialog();
                break;
        }
    }



}
