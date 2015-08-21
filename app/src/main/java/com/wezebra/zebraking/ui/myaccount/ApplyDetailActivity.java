package com.wezebra.zebraking.ui.myaccount;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.wezebra.zebraking.App;
import com.wezebra.zebraking.R;
import com.wezebra.zebraking.model.OrderLog;
import com.wezebra.zebraking.ui.BaseActivity;
import com.wezebra.zebraking.ui.WebViewActivity;
import com.wezebra.zebraking.ui.adapter.OrderLogListAdapter;
import com.wezebra.zebraking.util.CommonUtils;
import com.wezebra.zebraking.util.Constants;
import com.wezebra.zebraking.util.PreferenceUtils;
import com.wezebra.zebraking.util.Utils;
import com.wezebra.zebraking.widget.ExpandableHeightListView;

import java.util.List;

public class ApplyDetailActivity extends BaseActivity
{
    public static final String TAG = "ApplyDetailActivity";
    private TextView tvName;
    private TextView tvIndentity;
    private TextView tvPhone;
    private TextView tvBankCardCode;
    private TextView tvAddress;
    private TextView tvAmount;
    private TextView tvRate;
    private TextView tvServiceFee;
    private TextView agreement;
    private List<OrderLog> logList;
    private OrderLogListAdapter adapter;
    private ExpandableHeightListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_detail);
        initView();
        initValue();
    }

    private void initView()
    {
        tvName = (TextView) findViewById(R.id.name);
        tvIndentity = (TextView) findViewById(R.id.identity);
        tvPhone = (TextView) findViewById(R.id.phone);
        tvBankCardCode = (TextView) findViewById(R.id.bank_card_code);
        tvAddress = (TextView) findViewById(R.id.address);
        tvAmount = (TextView) findViewById(R.id.amount);
        tvRate = (TextView) findViewById(R.id.rate);
        tvServiceFee = (TextView) findViewById(R.id.service_fee);
        listView = (ExpandableHeightListView) findViewById(R.id.list);
        listView.setExpanded(true);
        agreement = (TextView) findViewById(R.id.agreement);
    }

    private void initValue()
    {
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String identity = intent.getStringExtra("identity");
        String phone = intent.getStringExtra("phone");
        String code = intent.getStringExtra("code");
        String address = intent.getStringExtra("address");
        double amount = intent.getDoubleExtra("amount", 0);
        int stage = intent.getIntExtra("stage", 0);
        float rate = intent.getFloatExtra("rate", 0);
        double servFee = intent.getDoubleExtra("servFee", 0);
        logList = intent.getParcelableArrayListExtra("list");

        tvName.setText(Utils.formatName(name));
        tvIndentity.setText(Utils.formatID(identity));
        tvPhone.setText(Utils.formatMobile(phone));
        tvBankCardCode.setText(code);
        tvAddress.setText(address);
        tvAmount.setText(CommonUtils.formatDouble(amount) + "×" + stage);
        tvRate.setText(CommonUtils.formatFloat(rate * 100) + "%");
        tvServiceFee.setText(CommonUtils.formatDouble(servFee) + "");

        adapter = new OrderLogListAdapter(this, logList);
        listView.setAdapter(adapter);

        agreement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebViewActivity.redirectToWebViewActivity(ApplyDetailActivity.this, Constants.URL_LOAN_AGREEMENT + "?uid="
                        + PreferenceUtils.getUserId() + "&orderCode=" + App.INSTANCE.getActiveApplyData().getOrderCode(), "借款与还款协议");
            }
        });
    }

}
