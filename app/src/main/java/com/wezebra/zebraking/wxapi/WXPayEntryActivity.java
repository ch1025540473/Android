package com.wezebra.zebraking.wxapi;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.wezebra.zebraking.R;
import com.wezebra.zebraking.http.CusFailListener;
import com.wezebra.zebraking.http.CusSuccessListener;
import com.wezebra.zebraking.http.DefaultErrorListener;
import com.wezebra.zebraking.http.TaskResponse;
import com.wezebra.zebraking.http.ZebraTask;
import com.wezebra.zebraking.http.data.PayStatusData;
import com.wezebra.zebraking.ui.BaseActivity;
import com.wezebra.zebraking.ui.installment.SIAStepThreeResultActivity;
import com.wezebra.zebraking.ui.myaccount.RepaymentActivity;
import com.wezebra.zebraking.ui.myaccount.SelectPayWayActivity;
import com.wezebra.zebraking.util.Constants;
import com.wezebra.zebraking.util.L;
import com.wezebra.zebraking.util.PreferenceUtils;

import java.util.Map;
import java.util.TreeMap;
//根据微信支付结果显示相对应的页面
public class WXPayEntryActivity extends BaseActivity implements IWXAPIEventHandler, View.OnClickListener
{
    private static final String TAG = "WXPayEntryActivity";
    private IWXAPI api;
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

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wxpay_entry);
        api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);
        api.handleIntent(getIntent(), this);

        layoutSuccess = (LinearLayout) findViewById(R.id.success_layout);
        layoutFail = (LinearLayout) findViewById(R.id.fail_layout);
        tvQuestion = (TextView) findViewById(R.id.pay_question);
        tvPayAmount = (TextView) findViewById(R.id.pay_amount);
        tvPayOrderCode = (TextView) findViewById(R.id.pay_order_code);
        btnSuccess = (TextView) findViewById(R.id.btn_pay_success);
        btnFail = (TextView) findViewById(R.id.btn_pay_fail);
        btnSuccess.setOnClickListener(this);
        btnFail.setOnClickListener(this);
        tvQuestion.setOnClickListener(this);

        payType = PreferenceUtils.getPayType();
        amount = PreferenceUtils.getPayAmount();
        orderCode = PreferenceUtils.getPayOrderCode();

        tvPayAmount.setText("￥" + amount);
        tvPayOrderCode.setText(orderCode + "");
    }

    private void showSuccess()
    {
        layoutSuccess.setVisibility(View.VISIBLE);
        layoutFail.setVisibility(View.GONE);
        tvQuestion.setVisibility(View.GONE);
    }

    private void showFail()
    {
        layoutSuccess.setVisibility(View.GONE);
        layoutFail.setVisibility(View.VISIBLE);
        tvQuestion.setVisibility(View.VISIBLE);
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
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed()
    {
        if (isPaySuccess)
        {
            onSuccessClick();
        } else
        {
            super.onBackPressed();
        }
    }

    @Override
    protected void onNewIntent(Intent intent)
    {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq baseReq)
    {
        L.d(TAG, "onReq: "
                        + "\nopenId: " + baseReq.openId
                        + "\ntransaction: " + baseReq.transaction
                        + "\ncheckArgs: " + baseReq.checkArgs()
                        + "\ntype: " + baseReq.getType()
        );
    }

    @Override
    public void onResp(BaseResp baseResp)
    {
        L.d(TAG, "onResp: " + "\nerrStr: " + baseResp.errStr
                + "\nopenId: " + baseResp.openId
                + "\ntransaction: " + baseResp.transaction
                + "\ncheckArgs: " + baseResp.checkArgs()
                + "\ntype: " + baseResp.getType()
                + "\nerrCode: " + baseResp.errCode);

        if (baseResp.errCode == 0)
        {
            payStatus();
        } else
        {
            new Handler().postDelayed(new Runnable()
            {
                @Override
                public void run()
                {
                    showFail();
//                    Toast.makeText(WXPayEntryActivity.this, "未成功支付", Toast.LENGTH_LONG).show();
                }
            }, 100);
        }
    }

    private void payStatus()
    {
        Map<String, String> params = new TreeMap<>();
        params.put("api", "payStatus");
        params.put("code", PreferenceUtils.getPayOrderCode() + "");
        params.put("outTradeNo", PreferenceUtils.getOutTradeNo());
        new ZebraTask.Builder(this, params, new CusSuccessListener()
        {
            @Override
            public void onSuccess(TaskResponse response)
            {
                showSuccess();
                isPaySuccess = true;
            }
        }, PayStatusData.class).setCusFailListener(new CusFailListener()
        {
            @Override
            public void onFail(TaskResponse response)
            {
                showFail();
            }
        }).setCusErrorListener(new DefaultErrorListener.CusErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                showFail();
            }
        }).build().execute();
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

    //如果支付失败，则执行这个方法
    public void onFailClick(){
        Intent intent = new Intent(this,SelectPayWayActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Log.e("羊驼", "草泥马！");
        startActivity(intent);
        // finish();
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
               onFailClick();
                break;
            case R.id.pay_question:
                showServiceTelDialog();
                break;
        }
    }


}
