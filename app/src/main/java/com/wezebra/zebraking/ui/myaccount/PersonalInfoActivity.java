package com.wezebra.zebraking.ui.myaccount;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wezebra.zebraking.App;
import com.wezebra.zebraking.R;
import com.wezebra.zebraking.http.CusSuccessListener;
import com.wezebra.zebraking.http.HttpUtils;
import com.wezebra.zebraking.http.TaskResponse;
import com.wezebra.zebraking.http.ZebraTask;
import com.wezebra.zebraking.ui.BaseActivity;
import com.wezebra.zebraking.ui.WebViewActivity;
import com.wezebra.zebraking.ui.installment.ChangeBindPhone;
import com.wezebra.zebraking.ui.installment.PersonalProfileActivity;
import com.wezebra.zebraking.ui.login.PasswordResetActivity;
import com.wezebra.zebraking.util.Constants;

import java.util.Map;
import java.util.TreeMap;

public class PersonalInfoActivity extends BaseActivity implements View.OnClickListener
{
    private RelativeLayout personalData;
    private RelativeLayout thirdParty;
    private RelativeLayout bankCard;
    private RelativeLayout passwordReset;
    private RelativeLayout changebindphone;
    private TextView signOut;
    private TextView haveCard;
    public static boolean shouldRefresh = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);
        initView();
        initValue();
    }

    private void initView()
    {
        personalData = (RelativeLayout) findViewById(R.id.personal_data);
        thirdParty = (RelativeLayout) findViewById(R.id.personal_third_party);
        bankCard = (RelativeLayout) findViewById(R.id.personal_bank_card);
        passwordReset = (RelativeLayout) findViewById(R.id.personal_password_reset);
        changebindphone = (RelativeLayout) findViewById(R.id.change_bind_phone);
        signOut = (TextView) findViewById(R.id.personal_sign_out);
        haveCard = (TextView) findViewById(R.id.personal_have_card);
        personalData.setOnClickListener(this);
        thirdParty.setOnClickListener(this);
        bankCard.setOnClickListener(this);
        passwordReset.setOnClickListener(this);
        signOut.setOnClickListener(this);
        changebindphone.setOnClickListener(this);
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

    private void initValue()
    {

        if (null != App.userDetailData)
        {
            if (!TextUtils.isEmpty(App.userDetailData.getBankCard().getFormatAcctId()))
            {
                haveCard.setText(getString(R.string.bank_attached));
            }
        }
    }

    private void signOut()
    {
        Map<String, String> params = new TreeMap<>();
        params.put("api", Constants.API_SIGN_OUT);

        new ZebraTask.Builder(this, params, new CusSuccessListener()
        {
            @Override
            public void onSuccess(TaskResponse response)
            {

                App.signOut(PersonalInfoActivity.this,1);

                finish();
            }
        }).build().execute();
    }

    @Override
    public void onClick(View v)
    {
        Intent intent;
        switch (v.getId())
        {
            case R.id.personal_data:
                intent = new Intent(this, PersonalProfileActivity.class);
                startActivity(intent);
                break;
            case R.id.personal_third_party:

                break;
            case R.id.personal_bank_card:
                if (null != App.userDetailData)
                {
                    if (TextUtils.isEmpty(App.userDetailData.getBankCard().getFormatAcctId()))
                    {
                        ChargeActivity.redirectToChargeActivity(this, WebViewActivity.FROM_PERSONAL);
                        return;
                    }
                }

                intent = new Intent(this, BankCardActivity.class);
                startActivity(intent);
                break;
            case R.id.personal_password_reset:
                intent = new Intent(this, PasswordResetActivity.class);
                intent.putExtra(PasswordResetActivity.RESET, true);
                startActivity(intent);
                break;
            case R.id.personal_sign_out:
                signOut();
                break;
            case R.id.change_bind_phone:
                startActivity(new Intent(this, ChangeBindPhone.class));
                break;
        }
    }
}
