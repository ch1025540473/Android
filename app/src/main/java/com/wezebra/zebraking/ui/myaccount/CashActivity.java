package com.wezebra.zebraking.ui.myaccount;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.wezebra.zebraking.App;
import com.wezebra.zebraking.R;
import com.wezebra.zebraking.http.HttpUtils;
import com.wezebra.zebraking.ui.BaseActivity;
import com.wezebra.zebraking.ui.WebViewActivity;
import com.wezebra.zebraking.util.CommonUtils;
import com.wezebra.zebraking.util.Constants;

import java.util.Map;
import java.util.TreeMap;

public class CashActivity extends BaseActivity implements View.OnClickListener
{
    public static final String TAG = "CashActivity";
    private TextView submit;
    private TextView cardCode;
    private TextView bankName;
    private EditText etCash;
    private String fromActivity;

    public static void redirectToCashActivity(Context context, String fromActivity)
    {
        Intent intent = new Intent(context, CashActivity.class);
        intent.putExtra(WebViewActivity.FROM_ACTIVITY, fromActivity);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash);
        initView();
        initValue();
    }

    private void initView()
    {
        etCash = (EditText) findViewById(R.id.et_cash);
        cardCode = (TextView) findViewById(R.id.bank_card_code);
        bankName = (TextView) findViewById(R.id.bank_name);
        submit = (TextView) findViewById(R.id.submit);
        CommonUtils.setPricePoint(etCash);
        submit.setOnClickListener(this);
        fromActivity = getIntent().getStringExtra(WebViewActivity.FROM_ACTIVITY);
    }

    private void initValue()
    {
        if (App.userDetailData != null)
        {
            cardCode.setText(App.userDetailData.getBankCard().getFormatAcctId());
            bankName.setText(App.userDetailData.getBankCard().getBankName());
            etCash.setHint("最多可提现金额 " + App.userDetailData.getUseMoney().getAvailableAmtStr() + " 元");
        }
    }

    @Override
    public void onClick(View v)
    {
        String amount = etCash.getText().toString().trim();
        cash(amount);
    }

    private void cash(String amount)
    {
        Map<String, String> params = new TreeMap<>();
        params.put("api", Constants.API_CASH);
        params.put("transAmt", amount);
        HttpUtils.addHFRequest(this, params, "取现", fromActivity);
    }
}
