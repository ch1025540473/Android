package com.wezebra.zebraking.ui.myaccount;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;

import com.umeng.message.PushAgent;
import com.wezebra.zebraking.R;
import com.wezebra.zebraking.ui.BaseActivity;
import com.wezebra.zebraking.ui.WebViewActivity;
import com.wezebra.zebraking.util.Constants;
import com.wezebra.zebraking.util.PreferenceUtils;

public class AboutActivity extends BaseActivity implements View.OnClickListener
{
    public static final String TAG = "AboutActivity";
    private RelativeLayout feedback;
    private RelativeLayout aboutZebra;
    private RelativeLayout customerService;
    private RelativeLayout evaluate;
    private SwitchCompat pushSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        initView();
    }

    private void initView()
    {
        feedback = (RelativeLayout) findViewById(R.id.feedback);
        aboutZebra = (RelativeLayout) findViewById(R.id.about_zebra);
        customerService = (RelativeLayout) findViewById(R.id.service_tel);
        evaluate = (RelativeLayout) findViewById(R.id.go_evaluate);
        pushSwitch = (SwitchCompat) findViewById(R.id.push_switch);
        feedback.setOnClickListener(this);
        aboutZebra.setOnClickListener(this);
        customerService.setOnClickListener(this);
        evaluate.setOnClickListener(this);
        pushSwitch.setChecked(PreferenceUtils.getIsPushOpen());
        pushSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                PreferenceUtils.setIsPushOpen(isChecked);
                PushAgent mPushAgent = PushAgent.getInstance(getApplicationContext());
                if (isChecked && !mPushAgent.isEnabled())
                {
                    mPushAgent.enable();
                } else if (!isChecked && mPushAgent.isEnabled())
                {
                    mPushAgent.disable();
                }
            }
        });
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
        Intent intent;
        switch (v.getId())
        {
            case R.id.about_zebra:
                WebViewActivity.redirectToWebViewActivity(this, Constants.URL_ABOUT, "关于斑马");
                break;
            case R.id.feedback:
                intent = new Intent(this, FeedbackActivity.class);
                startActivity(intent);
                break;
            case R.id.service_tel:
                showServiceTelDialog();
                break;
            case R.id.go_evaluate:

                Uri uri = Uri.parse("market://details?id=com.wezebra.zebraking");
                intent = new Intent(Intent.ACTION_VIEW, uri);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                if ((intent.resolveActivity(getPackageManager()) != null))
                {
                    startActivity(intent);
                }

                break;
        }
    }
}
