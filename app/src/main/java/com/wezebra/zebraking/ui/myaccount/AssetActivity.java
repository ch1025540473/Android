package com.wezebra.zebraking.ui.myaccount;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.wezebra.zebraking.App;
import com.wezebra.zebraking.R;
import com.wezebra.zebraking.http.CusSuccessListener;
import com.wezebra.zebraking.http.HttpUtils;
import com.wezebra.zebraking.http.TaskResponse;
import com.wezebra.zebraking.ui.BaseActivity;
import com.wezebra.zebraking.ui.WebViewActivity;
import com.wezebra.zebraking.util.Constants;
import com.wezebra.zebraking.util.Utils;

import java.util.Map;
import java.util.TreeMap;

public class AssetActivity extends BaseActivity implements View.OnClickListener
{
    private TextView tip;
    private Button cash;
    private Button recharge;
    private TextView tvTotal;
    private TextView tvAvailable;
    private TextView tvFreeze;
    public static boolean shouldRefresh = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asset);
        initView();
        initValue();
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        if (shouldRefresh)
        {
            HttpUtils.addGetUserDetailRequest(this, new CusSuccessListener()
            {
                @Override
                public void onSuccess(TaskResponse response)
                {
                    initValue();
                    shouldRefresh = false;
                }
            });
        }
    }

    private void initView()
    {
        tip = (TextView) findViewById(R.id.asset_tip);
        cash = (Button) findViewById(R.id.asset_cash);
        recharge = (Button) findViewById(R.id.asset_recharge);
        tvTotal = (TextView) findViewById(R.id.asset_total);
        tvAvailable = (TextView) findViewById(R.id.asset_available);
        tvFreeze = (TextView) findViewById(R.id.asset_freeze);
        tip.setOnClickListener(this);
        cash.setOnClickListener(this);
        recharge.setOnClickListener(this);
    }

    private void initValue()
    {
        if (null != App.userDetailData)
        {
            tvTotal.setText(App.userDetailData.getUseMoney().getTotalAmtStr());
            tvAvailable.setText(App.userDetailData.getUseMoney().getAvailableAmtStr());
            tvFreeze.setText(App.userDetailData.getUseMoney().getFreezeAmtStr());
        }
    }

    private void openHFAccount()
    {
        Map<String, String> params = new TreeMap<>();
        params.put("api", Constants.API_OPEN_ACCOUNT);

        HttpUtils.addHFRequest(this, params, "开户");
    }

    @Override
    public void onClick(View v)
    {
        Intent intent;
        switch (v.getId())
        {
            case R.id.asset_tip:
                Dialog dialog = Utils.getDialog(this, "什么是冻结金额？"
                        , "冻结金额表示您的资金暂时性因为某项业务处理而不能使用，当业务处理完毕后资金将自动解冻。\n");
                dialog.show();
                break;
            case R.id.asset_cash:
                if (null != App.userDetailData)
                {
                    if (TextUtils.isEmpty(App.userDetailData.getBankCard().getFormatAcctId()))
                    {
                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setMessage("您还未绑定银行卡，请先充值绑卡").setPositiveButton("充值绑卡", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                ChargeActivity.redirectToChargeActivity(AssetActivity.this, WebViewActivity.FROM_ASSET);
                            }
                        }).setNegativeButton(android.R.string.cancel, null);
                        builder.create().show();
                        return;
                    }
                }
                CashActivity.redirectToCashActivity(this, WebViewActivity.FROM_ASSET);
                break;
            case R.id.asset_recharge:
                ChargeActivity.redirectToChargeActivity(this, WebViewActivity.FROM_ASSET);
                break;
        }
    }
}
