package com.wezebra.zebraking.ui.myaccount;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wezebra.zebraking.R;
import com.wezebra.zebraking.http.HttpUtils;
import com.wezebra.zebraking.ui.BaseActivity;
import com.wezebra.zebraking.ui.WebViewActivity;
import com.wezebra.zebraking.util.CommonUtils;
import com.wezebra.zebraking.util.Constants;

import java.util.Map;
import java.util.TreeMap;

public class RepaymentDetailActivity extends BaseActivity implements View.OnClickListener
{
    public static final String OVERCOUNT = "overCount";
    public static final String REPAYAMOUNT = "repayAmount";
    public static final String OVERDUEFEE = "overdueFee";
    private double overCount;
    private double repayAmount;
    private double overdueFee;
    private TextView total;
    private TextView repayment;
    private TextView overdue;
    private TextView submit;
    private TextView tvAliPay;
    private TextView tvBank;
    private long orderCode;
    private long billCode;
    private LinearLayout offLineLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repayment_detail);
        initView();
        initValue();
    }

    private void initView()
    {   //待还本息
        total = (TextView) findViewById(R.id.bill_total);
        //待还本金
        repayment = (TextView) findViewById(R.id.bill_repayment);
        //逾期罚款
        overdue = (TextView) findViewById(R.id.bill_overdue_fee);
        //
        offLineLayout = (LinearLayout) findViewById(R.id.off_line_layout);
        //这些控件一般都是线下支付的时候显示
        //支付宝 ，各种提示
        tvAliPay = (TextView) findViewById(R.id.alipay_tips);
        tvBank = (TextView) findViewById(R.id.bank_tips);
        submit = (TextView) findViewById(R.id.submit);
        //支付按钮
        submit.setOnClickListener(this);
    }

    private void initValue()
    {
        Intent intent = getIntent();
        boolean isOnLine = intent.getBooleanExtra("isOnline", false);

        overCount = intent.getDoubleExtra(OVERCOUNT, 0);
        repayAmount = intent.getDoubleExtra(REPAYAMOUNT, 0);
        overdueFee = intent.getDoubleExtra(OVERDUEFEE, 0);

        billCode = intent.getLongExtra("billCode", 0);
        orderCode = intent.getLongExtra("orderCode", 0);

        if (overCount > 0)
        {
            total.setText(CommonUtils.formatDouble(overCount));
        }

        if (repayAmount > 0)
        {
            repayment.setText(CommonUtils.formatDouble(repayAmount));
        }

        if (overdueFee > 0)
        {
            overdue.setText(CommonUtils.formatDouble(overdueFee));
        }

        if (!isOnLine)
        {
            submit.setVisibility(View.GONE);
            offLineLayout.setVisibility(View.VISIBLE);
            String overCount = CommonUtils.formatDouble(this.overCount);
            String aliTip = "转账￥" + overCount + "元至以下支付宝账号，并备注借款人的姓名和手机号（例如：张三 15828641962）";
            String bankTip = "转账￥" + overCount + "元至以下银行账号，并备注借款人的姓名和手机号（例如：张三 15828641962）";

            SpannableStringBuilder builder = new SpannableStringBuilder(aliTip);

            builder.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.text_light)), 0, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            builder.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.text_pink)), 2, 3 + overCount.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            builder.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.text_light)), 3 + overCount.length(), aliTip.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            tvAliPay.setText(builder);

            builder.clear();
            builder = new SpannableStringBuilder(bankTip);
            builder.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.text_light)), 0, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            builder.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.text_pink)), 2, 3 + overCount.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            builder.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.text_light)), 3 + overCount.length(), bankTip.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            tvBank.setText(builder);
        }
    }

    @Override
    public void onClick(View v)
    {
        payMoney();
    }

    private void payMoney()
    {
        Map<String, String> params = new TreeMap<>();
        params.put("api", Constants.API_PAYMONEY);
        params.put("billCode", billCode + "");
        params.put("orderCode", orderCode + "");
        HttpUtils.addHFRequest(this, params, "还款", WebViewActivity.FROM_REPAYMENT);
    }
}
