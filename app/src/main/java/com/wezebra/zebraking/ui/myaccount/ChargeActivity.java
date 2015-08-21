package com.wezebra.zebraking.ui.myaccount;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.wezebra.zebraking.R;
import com.wezebra.zebraking.http.HttpUtils;
import com.wezebra.zebraking.ui.BaseActivity;
import com.wezebra.zebraking.ui.WebViewActivity;
import com.wezebra.zebraking.util.CommonUtils;
import com.wezebra.zebraking.util.Constants;
import com.wezebra.zebraking.widget.LinkTouchMovementMethod;
import com.wezebra.zebraking.widget.TouchableSpan;

import java.util.Map;
import java.util.TreeMap;

public class ChargeActivity extends BaseActivity implements View.OnClickListener
{
    public static final String TAG = "RechargeActivity";
    private TextView submit;
    private TextView tvChargeTip;
    private EditText etCharge;
    private String fromActivity;

    public static void redirectToChargeActivity(Context context, String fromActivity)
    {
        Intent intent = new Intent(context, ChargeActivity.class);
        intent.putExtra(WebViewActivity.FROM_ACTIVITY, fromActivity);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charge);
        tvChargeTip = (TextView) findViewById(R.id.tv_charge_tip);
        etCharge = (EditText) findViewById(R.id.et_charge);
        submit = (TextView) findViewById(R.id.submit);
        submit.setOnClickListener(this);
        fromActivity = getIntent().getStringExtra(WebViewActivity.FROM_ACTIVITY);
        CommonUtils.setPricePoint(etCharge);

        String chargeTip = getString(R.string.charge_tip);
        SpannableString ss = new SpannableString(chargeTip);
        ss.setSpan(new TouchableSpan(getResources().getColor(R.color.text_green),
                getResources().getColor(R.color.text_light), Color.TRANSPARENT)
        {
            @Override
            public void onClick(View widget)
            {

                WebViewActivity.redirectToWebViewActivity(ChargeActivity.this,
                        "http://www.dtcash.com/Content/html/payinfo.html", "支付说明");
            }
        }, chargeTip.indexOf("支付说明"), chargeTip.indexOf("支付说明") + 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        tvChargeTip.setText(ss);
        tvChargeTip.setMovementMethod(new LinkTouchMovementMethod());
    }

    @Override
    public void onClick(View v)
    {
        charge();
    }

    private void charge()
    {
        String count = etCharge.getText().toString().trim();
        Map<String, String> params = new TreeMap<>();
        params.put("api", Constants.API_CHARGE);
        params.put("transAmt", count);
        HttpUtils.addHFRequest(this, params, "充值", fromActivity);
    }
}
