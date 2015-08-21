package com.wezebra.zebraking.ui.installment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wezebra.zebraking.App;
import com.wezebra.zebraking.R;
import com.wezebra.zebraking.behavior.HttpAsyncTask;
import com.wezebra.zebraking.behavior.StartSubmitAnimation;
import com.wezebra.zebraking.model.ColorHolder;
import com.wezebra.zebraking.ui.BaseActivity;
import com.wezebra.zebraking.ui.WebViewActivity;
import com.wezebra.zebraking.util.Constants;
import com.wezebra.zebraking.util.PreferenceUtils;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class SubmitInstallmentApplicationStepTwoActivity extends BaseActivity implements View.OnClickListener
{

    private LinearLayout landlordAccountLayout, houseDataLayout;
    private TextView agreement;
    private TextView submit;
    private ImageView landlordAccount, houseData;
    private ImageView checkOne, checkTwo;

    private SharedPreferences sharedPreferences, entrance_flag;
    private SharedPreferences.Editor editor, entrance_flag_editor;
    private int info_submit, landlord_info_state, house_data_info_state;
    private ColorHolder colorHolder;

    public TextView tips;
    public int orderStatus, step;
    public String formatMemo;
    public int payeeStatus, materrialStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_installment_application_step_two);
        initView();
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        Bundle bundle = getIntent().getBundleExtra(Constants.INFO_SUBMIT);
        if (bundle != null)
        {
            info_submit = bundle.getInt(Constants.INFO_SUBMIT);
        } else
        {
            info_submit = Constants.NO_INFO_SUBMIT;
        }
        getIntent().removeExtra(Constants.INFO_SUBMIT);

        List<NameValuePair> postParameters = new ArrayList<>();
        postParameters.add(new BasicNameValuePair("orderCode", App.INSTANCE.getActiveApplyData().getOrderCode() + ""));
        postParameters.add(new BasicNameValuePair("api", "queryPayeeInfoAndAudit"));
        HttpAsyncTask.getInstance(this, PreferenceUtils.getUserId() + "", postParameters).pullSubmitTwoInfo();

    }

    private void initView()
    {

        landlordAccountLayout = (LinearLayout) findViewById(R.id.landlord_account_layout);
        houseDataLayout = (LinearLayout) findViewById(R.id.house_data_layout);
        agreement = (TextView) findViewById(R.id.agreement);
        submit = (TextView) findViewById(R.id.submit);
        landlordAccount = (ImageView) findViewById(R.id.landlord_account);
        houseData = (ImageView) findViewById(R.id.house_data);
        checkOne = (ImageView) findViewById(R.id.check_one);
        checkTwo = (ImageView) findViewById(R.id.check_two);
        tips = (TextView) findViewById(R.id.tips);

//        // button 闪亮动画辅助类
//        colorHolder = new ColorHolder(submit, Color.parseColor("#2bb8aa"),
//                Color.parseColor("#34d8c9"));

        agreement.setOnClickListener(this);
        submit.setOnClickListener(this);
        landlordAccountLayout.setOnClickListener(this);
        houseDataLayout.setOnClickListener(this);

//        getState();
//
//        //判断是否闪烁提交按钮
//        if ( (landlord_info_state == Constants.INFO_FILLED)&&(house_data_info_state == Constants.INFO_FILLED) ) {
//            CountDownTimer timer = new CountDownTimer(1200,100) {
//                @Override
//                public void onTick(long millisUntilFinished) {
//
//                }
//
//                @Override
//                public void onFinish() {
//                    startAnim();
//                }
//            };
//            timer.start();
//
//        }
//
//        initIcon();

    }

    public void iconAnim()
    {

        getState();
        editor = sharedPreferences.edit();
        switch (info_submit)
        {
            case Constants.LANDLORD_INFO_SUBMIT:
                landlordAccount.setBackgroundResource(R.drawable.landlord_auth_green);
                StartSubmitAnimation.run(this, checkOne);
                editor.putInt(Constants.LANDLORD_INFO_STATE, Constants.INFO_FILLED);
                editor.commit();
                break;
            case Constants.HOUSE_DATA_SUBMIT:
                houseData.setBackgroundResource(R.drawable.house_data_green);
                StartSubmitAnimation.run(this, checkTwo);
                editor.putInt(Constants.HOUSE_DATA_INFO_STATE, Constants.INFO_FILLED);
                editor.commit();
                break;
        }

    }

    public void refreshView()
    {

        if ((orderStatus == Constants.ORDER_WAITING_MODIFY))
        {

            tips.setText(formatMemo);
            tips.setTextColor(getResources().getColor(R.color.text_red));
            tips.setGravity(Gravity.LEFT);

        } else
        {

            tips.setText(getString(R.string.submit_step_two_default_tips));
            tips.setTextColor(getResources().getColor(R.color.text_dark));
            tips.setGravity(Gravity.CENTER);

        }

        switch (payeeStatus)
        {
            case Constants.WAIT_SUBMIT:
            case Constants.UNPASS:
                checkOne.setVisibility(View.INVISIBLE);
                landlordAccount.setBackgroundResource(R.drawable.landlord_auth_grey);
                break;
            case Constants.AUDITING:
            case Constants.PASSED:
                checkOne.setVisibility(View.VISIBLE);
                landlordAccount.setBackgroundResource(R.drawable.landlord_auth_green);
                break;
        }
        switch (materrialStatus)
        {
            case Constants.WAIT_SUBMIT:
            case Constants.UNPASS:
                checkTwo.setVisibility(View.INVISIBLE);
                houseData.setBackgroundResource(R.drawable.house_data_grey);
                break;
            case Constants.AUDITING:
            case Constants.PASSED:
                checkTwo.setVisibility(View.VISIBLE);
                houseData.setBackgroundResource(R.drawable.house_data_green);
                break;
        }

    }

    private void initIcon()
    {

        sharedPreferences = getSharedPreferences(Constants.INFO_AUTH_STATE, MODE_PRIVATE);
        if (landlord_info_state == Constants.INFO_FILLED)
        {
            checkOne.setVisibility(View.VISIBLE);
            landlordAccount.setBackgroundResource(R.drawable.landlord_auth_green);
        } else
        {
            landlordAccount.setBackgroundResource(R.drawable.landlord_auth_grey);
        }

        if (house_data_info_state == Constants.INFO_FILLED)
        {
            checkTwo.setVisibility(View.VISIBLE);
            houseData.setBackgroundResource(R.drawable.house_data_green);
        } else
        {
            houseData.setBackgroundResource(R.drawable.house_data_grey);
        }

    }

    public void getState()
    {

        sharedPreferences = getSharedPreferences(Constants.INFO_AUTH_STATE, MODE_PRIVATE);
        landlord_info_state = sharedPreferences.getInt(Constants.LANDLORD_INFO_STATE, Constants.DEFAULT_INFO_STATE);
        house_data_info_state = sharedPreferences.getInt(Constants.HOUSE_DATA_INFO_STATE, Constants.DEFAULT_INFO_STATE);

    }

    // 开始 button 闪亮动画
    private void startAnim()
    {
        ValueAnimator animator1 = ObjectAnimator.ofFloat(colorHolder, "ratio", 1, 0).setDuration(250);
        ValueAnimator animator2 = ObjectAnimator.ofFloat(colorHolder, "ratio", 0, 1).setDuration(250);
        final AnimatorSet set = new AnimatorSet();
        set.play(animator1).before(animator2);
        set.addListener(new AnimatorListenerAdapter()
        {
            @Override
            public void onAnimationEnd(Animator animation)
            {
                set.start();
            }
        });
        set.start();

//        AnimatorSet set2 = new AnimatorSet();
//        set2.play(animator1).before(animator2);
//        AnimatorSet set3 = new AnimatorSet();
//        set3.play(set).before(set2);
//
//        set3.addListener(new AnimatorListenerAdapter()
//        {
//            @Override
//            public void onAnimationEnd(Animator animation)
//            {
//                submit.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_green_bg));
//            }
//        });
//        set3.start();
    }

    @Override
    public void onClick(View v)
    {

        switch (v.getId())
        {
            case R.id.agreement:

                WebViewActivity.redirectToWebViewActivity(this, Constants.URL_LOAN_AGREEMENT + "?uid="
                        + PreferenceUtils.getUserId() + "&orderCode=" + App.INSTANCE.getActiveApplyData().getOrderCode(), "借款与还款协议");
                break;
            case R.id.submit:

                if (payeeStatus == Constants.WAIT_SUBMIT)
                {

                    new AlertDialog.Builder(this)
                            .setMessage("请先填写房东账号信息")
                            .setPositiveButton(getString(R.string.OK),
                                    new DialogInterface.OnClickListener()
                                    {
                                        public void onClick(DialogInterface dialoginterface, int i)
                                        {
                                            //按钮事件
                                            dialoginterface.dismiss();
                                            entrance_flag = getSharedPreferences(Constants.ENTRANCE_FLAG, MODE_PRIVATE);
                                            entrance_flag_editor = entrance_flag.edit();
                                            entrance_flag_editor.putInt(Constants.ENTRANCE_FLAG, Constants.IS_FROM_STEP_TWO);
                                            entrance_flag_editor.commit();
                                            Intent intent1 = new Intent();
                                            intent1.setClass(SubmitInstallmentApplicationStepTwoActivity.this, LandlordAuthActivity.class);
                                            startActivity(intent1);
                                        }
                                    })
                            .show();
                    return;

                } else if (payeeStatus == Constants.UNPASS)
                {

                    new AlertDialog.Builder(this)
                            .setMessage("请先修改房东账号信息")
                            .setPositiveButton(getString(R.string.OK),
                                    new DialogInterface.OnClickListener()
                                    {
                                        public void onClick(DialogInterface dialoginterface, int i)
                                        {
                                            //按钮事件
                                            dialoginterface.dismiss();
                                            entrance_flag = getSharedPreferences(Constants.ENTRANCE_FLAG, MODE_PRIVATE);
                                            entrance_flag_editor = entrance_flag.edit();
                                            entrance_flag_editor.putInt(Constants.ENTRANCE_FLAG, Constants.IS_FROM_STEP_TWO);
                                            entrance_flag_editor.commit();
                                            Intent intent1 = new Intent();
                                            intent1.setClass(SubmitInstallmentApplicationStepTwoActivity.this, LandlordAuthActivity.class);
                                            startActivity(intent1);
                                        }
                                    })
                            .show();
                    return;

                }

                if (materrialStatus == Constants.WAIT_SUBMIT)
                {

                    new AlertDialog.Builder(this)
                            .setMessage("请先填写租房资料信息")
                            .setPositiveButton(getString(R.string.OK),
                                    new DialogInterface.OnClickListener()
                                    {
                                        public void onClick(DialogInterface dialoginterface, int i)
                                        {
                                            //按钮事件
                                            dialoginterface.dismiss();
                                            entrance_flag = getSharedPreferences(Constants.ENTRANCE_FLAG, MODE_PRIVATE);
                                            entrance_flag_editor = entrance_flag.edit();
                                            entrance_flag_editor.putInt(Constants.ENTRANCE_FLAG, Constants.IS_FROM_STEP_TWO);
                                            entrance_flag_editor.commit();
                                            Intent intent1 = new Intent();
                                            intent1.setClass(SubmitInstallmentApplicationStepTwoActivity.this, HouseDataAuthActivity.class);
                                            startActivity(intent1);
                                        }
                                    })
                            .show();
                    return;

                } else if (materrialStatus == Constants.UNPASS)
                {

                    new AlertDialog.Builder(this)
                            .setMessage("请先修改租房资料信息")
                            .setPositiveButton(getString(R.string.OK),
                                    new DialogInterface.OnClickListener()
                                    {
                                        public void onClick(DialogInterface dialoginterface, int i)
                                        {
                                            //按钮事件
                                            dialoginterface.dismiss();
                                            entrance_flag = getSharedPreferences(Constants.ENTRANCE_FLAG, MODE_PRIVATE);
                                            entrance_flag_editor = entrance_flag.edit();
                                            entrance_flag_editor.putInt(Constants.ENTRANCE_FLAG, Constants.IS_FROM_STEP_TWO);
                                            entrance_flag_editor.commit();
                                            Intent intent1 = new Intent();
                                            intent1.setClass(SubmitInstallmentApplicationStepTwoActivity.this, HouseDataAuthActivity.class);
                                            startActivity(intent1);
                                        }
                                    })
                            .show();
                    return;

                }

                Intent intent = new Intent();
                intent.setClass(this, SIAStepTwoResultActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("doFinish", true);
                List<NameValuePair> postParameters = new ArrayList<>();
                postParameters.add(new BasicNameValuePair("orderCode", App.INSTANCE.getActiveApplyData().getOrderCode() + ""));
                postParameters.add(new BasicNameValuePair("step", 3 + ""));
                postParameters.add(new BasicNameValuePair("api", "examinaApi"));
                HttpAsyncTask.getInstance(this, PreferenceUtils.getUserId() + "", postParameters).post(intent);

                break;
            case R.id.landlord_account_layout:
                entrance_flag = getSharedPreferences(Constants.ENTRANCE_FLAG, MODE_PRIVATE);
                entrance_flag_editor = entrance_flag.edit();
                entrance_flag_editor.putInt(Constants.ENTRANCE_FLAG, Constants.IS_FROM_STEP_TWO);
                entrance_flag_editor.commit();
                intent = new Intent();
                intent.setClass(this, LandlordAuthActivity.class);
                startActivity(intent);
                break;
            case R.id.house_data_layout:
                entrance_flag = getSharedPreferences(Constants.ENTRANCE_FLAG, MODE_PRIVATE);
                entrance_flag_editor = entrance_flag.edit();
                entrance_flag_editor.putInt(Constants.ENTRANCE_FLAG, Constants.IS_FROM_STEP_TWO);
                entrance_flag_editor.commit();
                intent = new Intent();
                intent.setClass(this, HouseDataAuthActivity.class);
                startActivity(intent);
                break;
        }

    }
}
