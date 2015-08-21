package com.wezebra.zebraking.ui.myaccount;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.wezebra.zebraking.R;
import com.wezebra.zebraking.http.HttpUtils;
import com.wezebra.zebraking.ui.BaseActivity;
import com.wezebra.zebraking.ui.WebViewActivity;
import com.wezebra.zebraking.util.Constants;

import java.util.Map;
import java.util.TreeMap;

public class InvestActivity extends BaseActivity implements View.OnClickListener
{
    public static final String TAG = "InvestActivity";
    private TextView tvProId;
    private EditText etInvest;
    private TextView submit;
    private String proId;
    private long limit;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invest);
        initView();
        initValue();
    }

    private void initView()
    {
        tvProId = (TextView) findViewById(R.id.invest_proId);
        etInvest = (EditText) findViewById(R.id.et_invest);
        submit = (TextView) findViewById(R.id.submit);
        submit.setOnClickListener(this);
    }

    private void initValue()
    {
        proId = getIntent().getStringExtra("proId");
        limit = getIntent().getLongExtra("limit", 0);
        tvProId.setText(proId);
        etInvest.setHint("最多可投资金额 " + (limit / 100) + "元");
    }

    @Override
    public void onClick(View v)
    {
        invest();
    }

    private void invest()
    {
        String amount = etInvest.getText().toString().trim();
        Map<String, String> params = new TreeMap<>();
        params.put("api", Constants.API_INVESTTENDER);
        params.put("transAmt", amount);
        params.put("proId", proId);
        HttpUtils.addHFRequest(this, params, "投资", WebViewActivity.FROM_TENDER);
    }
}
