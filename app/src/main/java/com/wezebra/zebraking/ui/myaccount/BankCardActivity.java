package com.wezebra.zebraking.ui.myaccount;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.wezebra.zebraking.App;
import com.wezebra.zebraking.R;
import com.wezebra.zebraking.ui.BaseActivity;

public class BankCardActivity extends BaseActivity
{
    public static final String TAG = "BankCardActivity";
    private TextView tvBankName;
    private TextView tvCardNum;
    private ImageView icon;

    @Override

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_card);
        initView();
    }

    private void initView()
    {
        tvBankName = (TextView) findViewById(R.id.bank_name);
        tvCardNum = (TextView) findViewById(R.id.card_num);
        icon = (ImageView) findViewById(R.id.bank_icon);

        if (null != App.userDetailData)
        {
            tvBankName.setText(App.userDetailData.getBankCard().getBankName());
            tvCardNum.setText(App.userDetailData.getBankCard().getFormatAcctId());
            String code = App.userDetailData.getBankCard().getCode();
            code = code.toLowerCase();

            try
            {
                int drawableId = getResources().getIdentifier(code, "drawable", getPackageName());
                icon.setImageDrawable(getResources().getDrawable(drawableId));
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}
