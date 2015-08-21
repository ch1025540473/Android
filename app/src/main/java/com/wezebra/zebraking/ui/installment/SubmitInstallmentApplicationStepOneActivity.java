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
import android.widget.Toast;

import com.wezebra.zebraking.App;
import com.wezebra.zebraking.R;
import com.wezebra.zebraking.behavior.HttpAsyncTask;
import com.wezebra.zebraking.behavior.StartSubmitAnimation;
import com.wezebra.zebraking.model.ColorHolder;
import com.wezebra.zebraking.ui.BaseActivity;
import com.wezebra.zebraking.util.Constants;
import com.wezebra.zebraking.util.PreferenceUtils;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class SubmitInstallmentApplicationStepOneActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout baseInfoLayout,idInfoLayout,jobOrEduInfoLayout,contactsInfoLayout;
    private ImageView baseInfo,idInfo,jobOrEduInfo,contactsInfo;
    private ImageView checkOne,checkTwo,checkThree,checkFour;
    private int info_submit;
    private TextView submit,job_or_edu_info_text,tips;

    private SharedPreferences sharedPreferences,entrance_flag;
    private SharedPreferences.Editor editor,entrance_flag_editor;
    private ColorHolder colorHolder;

    private int base_info_state,id_info_state,job_or_edu_info_state,contacts_info_state,job_or_edu;

    public int academicStatus,orderStatus,step,basicStatus,identityStatus,educationStatus,workStatus,contactStatus;
    public String formatMemo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_installment_application_step_one);
        initView();

    }

    @Override
    protected void onResume() {
        super.onResume();

        Bundle bundle = getIntent().getBundleExtra(Constants.INFO_SUBMIT);
        if (bundle != null) {
            info_submit = bundle.getInt(Constants.INFO_SUBMIT);
        } else {
            info_submit = Constants.NO_INFO_SUBMIT;
        }
        getIntent().removeExtra(Constants.INFO_SUBMIT);

        long orderCode = 0;
        if (App.INSTANCE.getActiveApplyData() != null) {
            orderCode = App.INSTANCE.getActiveApplyData().getOrderCode();
        } else {
            orderCode = getIntent().getLongExtra("code",0);
        }
        List<NameValuePair> postParameters = new ArrayList<>();
        postParameters.add(new BasicNameValuePair("orderCode",orderCode+""));
        postParameters.add(new BasicNameValuePair("api", "getUserLoginAndAudit"));
        HttpAsyncTask.getInstance(this,PreferenceUtils.getUserId()+"",postParameters).pullSubmitOneInfo();

    }

    public void initView() {

        tips = (TextView)findViewById(R.id.tips);
        baseInfoLayout = (LinearLayout)findViewById(R.id.base_info_layout);
        idInfoLayout = (LinearLayout)findViewById(R.id.id_info_layout);
        jobOrEduInfoLayout = (LinearLayout)findViewById(R.id.job_or_edu_info_layout);
        contactsInfoLayout = (LinearLayout)findViewById(R.id.contacts_info_layout);

        baseInfo = (ImageView)findViewById(R.id.base_info_img);
        idInfo= (ImageView)findViewById(R.id.id_info_img);
        jobOrEduInfo = (ImageView)findViewById(R.id.job_or_edu_info_img);
        contactsInfo = (ImageView)findViewById(R.id.contacts_info_img);
        checkOne = (ImageView)findViewById(R.id.check_one);
        checkTwo = (ImageView)findViewById(R.id.check_two);
        checkThree = (ImageView)findViewById(R.id.check_three);
        checkFour = (ImageView)findViewById(R.id.check_four);
        submit = (TextView)findViewById(R.id.submit);
        job_or_edu_info_text = (TextView)findViewById(R.id.job_or_edu_info_txt);

//        // button 闪亮动画辅助类
//        colorHolder = new ColorHolder(submit, Color.parseColor("#2bb8aa"),
//                Color.parseColor("#34d8c9"));

        baseInfoLayout.setOnClickListener(this);
        idInfoLayout.setOnClickListener(this);
        jobOrEduInfoLayout.setOnClickListener(this);
        contactsInfoLayout.setOnClickListener(this);
        submit.setOnClickListener(this);

//        getState();
//
//        //判断是否闪烁提交按钮
//        if ( (base_info_state == Constants.INFO_FILLED)&&(id_info_state == Constants.INFO_FILLED)&&(job_or_edu_info_state == Constants.INFO_FILLED)&&(contacts_info_state == Constants.INFO_FILLED) ) {
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

        //initIcon();

    }

    public void submitAnim() {

        getState();
        editor = sharedPreferences.edit();
        switch (info_submit) {
            case Constants.BASE_INFO_SUBMIT:
                baseInfo.setBackgroundResource(R.drawable.base_info_green);
                StartSubmitAnimation.run(this, checkOne);
                editor.putInt(Constants.BASE_INFO_STATE,Constants.INFO_FILLED);
                editor.commit();
                break;
            case Constants.ID_INFO_SUBMIT:
                idInfo.setBackgroundResource(R.drawable.id_info_green);
                StartSubmitAnimation.run(this,checkTwo);
                editor.putInt(Constants.ID_INFO_STATE,Constants.INFO_FILLED);
                editor.commit();
                break;
            case Constants.JOB_OR_EDUCATION_INFO_SUBMIT:
//                if (job_or_edu == Constants.IS_JOB_AUTH) {
//                    jobOrEduInfo.setBackgroundResource(R.drawable.job_info_green);
//                } else {
//                    jobOrEduInfo.setBackgroundResource(R.drawable.edu_info_green);
//                }
                if (academicStatus == 1) {
                    jobOrEduInfo.setBackgroundResource(R.drawable.edu_info_green);
                } else {
                    jobOrEduInfo.setBackgroundResource(R.drawable.job_info_green);
                }
                StartSubmitAnimation.run(this,checkThree);
                editor.putInt(Constants.JOB_OR_EDUCATION_INFO_STATE,Constants.INFO_FILLED);
                editor.commit();
                break;
            case Constants.CONTACTS_INFO_SUBMIT:
                contactsInfo.setBackgroundResource(R.drawable.contacts_info_green);
                StartSubmitAnimation.run(this,checkFour);
                editor.putInt(Constants.CONTACTS_INFO_STATE,Constants.INFO_FILLED);
                editor.commit();
                break;
        }

    }

    public void refreshView() {
        if (PreferenceUtils.getFourStatus()==1){
            workStatus=1; // 四项中满足一项，icon即为绿色
        }
        if ( (orderStatus == Constants.ORDER_WAITING_MODIFY) ) {

            tips.setText(formatMemo);
            tips.setTextColor(getResources().getColor(R.color.text_red));
            tips.setGravity(Gravity.LEFT);

        } else {

            tips.setText(getString(R.string.submit_step_one_default_tips));
            tips.setTextColor(getResources().getColor(R.color.text_dark));
            tips.setGravity(Gravity.CENTER);

        }

        switch ( basicStatus ) {
            case Constants.WAIT_SUBMIT:
            case Constants.UNPASS:
                checkOne.setVisibility(View.INVISIBLE);
                baseInfo.setBackgroundResource(R.drawable.base_info_grey);
                break;
            case Constants.AUDITING:
            case Constants.PASSED:
                checkOne.setVisibility(View.VISIBLE);
                baseInfo.setBackgroundResource(R.drawable.base_info_green);
                break;
        }
        switch ( identityStatus ) {
            case Constants.WAIT_SUBMIT:
            case Constants.UNPASS:
                checkTwo.setVisibility(View.INVISIBLE);
                idInfo.setBackgroundResource(R.drawable.id_info_grey);
                break;
            case Constants.AUDITING:
            case Constants.PASSED:
                checkTwo.setVisibility(View.VISIBLE);
                idInfo.setBackgroundResource(R.drawable.id_info_green);
                break;
        }
        switch ( academicStatus ) {
            case 1:
                switch ( educationStatus ) {
                    case Constants.WAIT_SUBMIT:
                    case Constants.UNPASS:
                        jobOrEduInfo.setBackgroundResource(R.drawable.edu_info_grey);
                        job_or_edu_info_text.setText(Constants.EDU_AUTH);
                        break;
                    case Constants.AUDITING:
                    case Constants.PASSED:
                        checkThree.setVisibility(View.VISIBLE);
                        jobOrEduInfo.setBackgroundResource(R.drawable.edu_info_green);
                        job_or_edu_info_text.setText(Constants.EDU_AUTH);
                        break;
                }
                break;
            case 2:
                switch ( workStatus ) {
                    case Constants.WAIT_SUBMIT:
                    case Constants.UNPASS:
                        jobOrEduInfo.setBackgroundResource(R.drawable.job_info_grey);
                        job_or_edu_info_text.setText(Constants.JOB_AUTH);
                        break;
                    case Constants.AUDITING:
                    case Constants.PASSED:
                        checkThree.setVisibility(View.VISIBLE);
                        jobOrEduInfo.setBackgroundResource(R.drawable.job_info_green);
                        job_or_edu_info_text.setText(Constants.JOB_AUTH);
                        break;
                }
                break;
        }
        switch ( contactStatus ) {
            case Constants.WAIT_SUBMIT:
            case Constants.UNPASS:
                checkFour.setVisibility(View.INVISIBLE);
                contactsInfo.setBackgroundResource(R.drawable.contacts_info_grey);
                break;
            case Constants.AUDITING:
            case Constants.PASSED:
                checkFour.setVisibility(View.VISIBLE);
                contactsInfo.setBackgroundResource(R.drawable.contacts_info_green);
                break;
        }

    }

    // 开始 button 闪亮动画
    private void startAnim()
    {
        ValueAnimator animator1 = ObjectAnimator.ofFloat(colorHolder, "ratio", 1, 0).setDuration(250);
        ValueAnimator animator2 = ObjectAnimator.ofFloat(colorHolder,"ratio",0,1).setDuration(250);
        final AnimatorSet set = new AnimatorSet();
        set.play(animator1).before(animator2);
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
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
//                //submit.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_green_bg));
//            }
//        });
//        set3.start();
    }

    public void getState() {

        sharedPreferences = getSharedPreferences(Constants.INFO_AUTH_STATE,MODE_PRIVATE);
        base_info_state = sharedPreferences.getInt(Constants.BASE_INFO_STATE, Constants.DEFAULT_INFO_STATE);
        id_info_state = sharedPreferences.getInt(Constants.ID_INFO_STATE,Constants.DEFAULT_INFO_STATE);
        job_or_edu = sharedPreferences.getInt(Constants.JOB_OR_EDUCATION,Constants.DEFAULT_AUTH_ABOUT_JOB＿AND＿EDU);
        job_or_edu_info_state = sharedPreferences.getInt(Constants.JOB_OR_EDUCATION_INFO_STATE,Constants.DEFAULT_INFO_STATE);
        contacts_info_state = sharedPreferences.getInt(Constants.CONTACTS_INFO_STATE,Constants.DEFAULT_INFO_STATE);

    }

    private void initIcon() {

        sharedPreferences = getSharedPreferences(Constants.INFO_AUTH_STATE,MODE_PRIVATE);
        if (base_info_state == Constants.INFO_FILLED) {
            checkOne.setVisibility(View.VISIBLE);
            baseInfo.setBackgroundResource(R.drawable.base_info_green);
        } else {
            baseInfo.setBackgroundResource(R.drawable.base_info_grey);
        }

        if (id_info_state == Constants.INFO_FILLED) {
            checkTwo.setVisibility(View.VISIBLE);
            idInfo.setBackgroundResource(R.drawable.id_info_green);
        } else {
            idInfo.setBackgroundResource(R.drawable.id_info_grey);
        }

        if (job_or_edu_info_state == Constants.INFO_FILLED) {
            checkThree.setVisibility(View.VISIBLE);
            if (job_or_edu == Constants.IS_JOB_AUTH) {
                jobOrEduInfo.setBackgroundResource(R.drawable.job_info_green);
            } else {
                jobOrEduInfo.setBackgroundResource(R.drawable.edu_info_green);
            }
        } else {
            if (job_or_edu == Constants.IS_JOB_AUTH) {
                jobOrEduInfo.setBackgroundResource(R.drawable.job_info_grey);
            } else {
                jobOrEduInfo.setBackgroundResource(R.drawable.edu_info_grey);
            }
        }

        if (contacts_info_state == Constants.INFO_FILLED) {
            checkFour.setVisibility(View.VISIBLE);
            contactsInfo.setBackgroundResource(R.drawable.contacts_info_green);
        } else {
            contactsInfo.setBackgroundResource(R.drawable.contacts_info_grey);
        }

        if (job_or_edu == Constants.IS_EDUCATION_AUTH)
            job_or_edu_info_text.setText(Constants.EDU_AUTH);
        else
            job_or_edu_info_text.setText(Constants.JOB_AUTH);

    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        entrance_flag = getSharedPreferences(Constants.ENTRANCE_FLAG,MODE_PRIVATE);
        entrance_flag_editor = entrance_flag.edit();
        switch (v.getId()) {
            case R.id.base_info_layout:
                if (basicStatus == Constants.AUDITING) {
                    Toast.makeText(this,getString(R.string.status_auditing_tips),Toast.LENGTH_LONG).show();
                    return;
                }
                entrance_flag_editor.putInt(Constants.ENTRANCE_FLAG, Constants.IS_FROM_APPLICATION);
                entrance_flag_editor.commit();
                if (basicStatus == Constants.PASSED) {
                    intent.setClass(this,ReFillActivity.class);
                    intent.putExtra("title", getString(R.string.title_activity_base_info));
                    intent.putExtra("redirect",Constants.BASIC_INFO);
                } else {
                    intent.setClass(this, BaseInfoStepInitialActivity.class);
                }
                pullBaseInfoData(intent);
                break;
            case R.id.id_info_layout:
                if (identityStatus == Constants.AUDITING) {
                    Toast.makeText(this,getString(R.string.status_auditing_tips),Toast.LENGTH_LONG).show();
                    return;
                }
                entrance_flag_editor.putInt(Constants.ENTRANCE_FLAG,Constants.IS_FROM_APPLICATION);
                entrance_flag_editor.commit();
                if (identityStatus == Constants.PASSED) {
                    intent.setClass(this,ReFillActivity.class);
                    intent.putExtra("title", getString(R.string.title_activity_identification));
                    intent.putExtra("redirect",Constants.IDENTITY_INFO);
                } else {
                    intent.setClass(this,IdentificationActivity.class);
                }
                startActivity(intent);
                break;
            case R.id.job_or_edu_info_layout:
                entrance_flag_editor.putInt(Constants.ENTRANCE_FLAG,Constants.IS_FROM_APPLICATION);
                entrance_flag_editor.commit();
//                sharedPreferences = getSharedPreferences(Constants.INFO_AUTH_STATE,MODE_PRIVATE);
//                int n = sharedPreferences.getInt(Constants.JOB_OR_EDUCATION,Constants.DEFAULT_AUTH_ABOUT_JOB＿AND＿EDU);
//                if (n == Constants.IS_JOB_AUTH)
//                    intent.setClass(this,JobInfoActivity.class);
//                else if (n == Constants.IS_EDUCATION_AUTH)
//                    intent.setClass(this,EducationAuthActivity.class);
                switch (academicStatus) {
                    case 1:
                        if (educationStatus == Constants.AUDITING) {
                            Toast.makeText(this,getString(R.string.status_auditing_tips),Toast.LENGTH_LONG).show();
                            return;
                        }
                        if (educationStatus == Constants.PASSED) {
                            intent.setClass(this,ReFillActivity.class);
                            intent.putExtra("title", getString(R.string.title_activity_education_auth));
                            intent.putExtra("redirect", Constants.EDUCATION_INFO);
                        } else {
                            intent.setClass(this,EducationAuthActivity.class);
                        }
                        break;
                    case 2:
//                        if (workStatus == Constants.AUDITING) {
//                            Toast.makeText(this,getString(R.string.status_auditing_tips),Toast.LENGTH_LONG).show();
//                            return;
//                        }
//                        if (workStatus == Constants.PASSED) {
//                            intent.setClass(this,ReFillActivity.class);
//                            intent.putExtra("title", getString(R.string.title_activity_job_info));
//                            intent.putExtra("redirect", Constants.JOB_INFO);
//                        } else {
                            intent.setClass(this,JobInfoChoiceActivity.class);
//                        }
                        break;
                }
                startActivity(intent);
                break;
            case R.id.contacts_info_layout:
                if (contactStatus == Constants.AUDITING) {
                    Toast.makeText(this,getString(R.string.status_auditing_tips),Toast.LENGTH_LONG).show();
                    return;
                }
                entrance_flag_editor.putInt(Constants.ENTRANCE_FLAG,Constants.IS_FROM_APPLICATION);
                entrance_flag_editor.commit();
                if (contactStatus == Constants.PASSED) {
                    intent.setClass(this,ReFillActivity.class);
                    intent.putExtra("title", getString(R.string.title_activity_contacts_auth));
                    intent.putExtra("redirect", Constants.CONTACTS_INFO);
                } else {
                    intent.setClass(this,ContactsAuthActivity.class);
                }
                startActivity(intent);
                break;
            case R.id.submit:

                if ( basicStatus == Constants.WAIT_SUBMIT ) {

                    new AlertDialog.Builder(this)
                            .setMessage("请先填写基础信息")
                            .setPositiveButton(getString(R.string.OK),
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialoginterface, int i) {
                                            //按钮事件
                                            dialoginterface.dismiss();
                                            entrance_flag_editor.putInt(Constants.ENTRANCE_FLAG, Constants.IS_FROM_APPLICATION);
                                            entrance_flag_editor.commit();
                                            Intent intent1 = new Intent();
                                            intent1.setClass(SubmitInstallmentApplicationStepOneActivity.this, BaseInfoStepInitialActivity.class);
                                            pullBaseInfoData(intent1);
                                        }
                                    })
                            .show();
                    return;

                } else if ( basicStatus == Constants.UNPASS ) {

                    new AlertDialog.Builder(this)
                            .setMessage("请先修改基础信息")
                            .setPositiveButton(getString(R.string.OK),
                                    new DialogInterface.OnClickListener(){
                                        public void onClick(DialogInterface dialoginterface, int i){
                                            //按钮事件
                                            dialoginterface.dismiss();
                                            entrance_flag_editor.putInt(Constants.ENTRANCE_FLAG, Constants.IS_FROM_APPLICATION);
                                            entrance_flag_editor.commit();
                                            Intent intent1 = new Intent();
                                            intent1.setClass(SubmitInstallmentApplicationStepOneActivity.this, BaseInfoStepInitialActivity.class);
                                            pullBaseInfoData(intent1);
                                        }
                                    })
                            .show();
                    return;

                }

                if ( identityStatus == Constants.WAIT_SUBMIT ) {

                    new AlertDialog.Builder(this)
                            .setMessage("请先填写身份信息")
                            .setPositiveButton(getString(R.string.OK),
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialoginterface, int i) {
                                            //按钮事件
                                            dialoginterface.dismiss();
                                            entrance_flag_editor.putInt(Constants.ENTRANCE_FLAG, Constants.IS_FROM_APPLICATION);
                                            entrance_flag_editor.commit();
                                            Intent intent1 = new Intent();
                                            intent1.setClass(SubmitInstallmentApplicationStepOneActivity.this, IdentificationActivity.class);
                                            startActivity(intent1);
                                        }
                                    })
                            .show();
                    return;

                } else if ( identityStatus == Constants.UNPASS ) {

                    new AlertDialog.Builder(this)
                            .setMessage("请先修改身份信息")
                            .setPositiveButton(getString(R.string.OK),
                                    new DialogInterface.OnClickListener(){
                                        public void onClick(DialogInterface dialoginterface, int i){
                                            //按钮事件
                                            dialoginterface.dismiss();
                                            entrance_flag_editor.putInt(Constants.ENTRANCE_FLAG, Constants.IS_FROM_APPLICATION);
                                            entrance_flag_editor.commit();
                                            Intent intent1 = new Intent();
                                            intent1.setClass(SubmitInstallmentApplicationStepOneActivity.this, IdentificationActivity.class);
                                            startActivity(intent1);
                                        }
                                    })
                            .show();
                    return;

                }

                switch ( academicStatus ) {

                    case 1:
                        //学历认证
                        if ( educationStatus == Constants.WAIT_SUBMIT ) {

                            new AlertDialog.Builder(this)
                                    .setMessage("请先填写学历信息")
                                    .setPositiveButton(getString(R.string.OK),
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialoginterface, int i) {
                                                    //按钮事件
                                                    dialoginterface.dismiss();
                                                    entrance_flag_editor.putInt(Constants.ENTRANCE_FLAG, Constants.IS_FROM_APPLICATION);
                                                    entrance_flag_editor.commit();
                                                    Intent intent1 = new Intent();
                                                    intent1.setClass(SubmitInstallmentApplicationStepOneActivity.this, EducationAuthActivity.class);
                                                    startActivity(intent1);
                                                }
                                            })
                                    .show();
                            return;

                        } else if ( educationStatus == Constants.UNPASS ) {

                            new AlertDialog.Builder(this)
                                    .setMessage("请先修改学历信息")
                                    .setPositiveButton(getString(R.string.OK),
                                            new DialogInterface.OnClickListener(){
                                                public void onClick(DialogInterface dialoginterface, int i){
                                                    //按钮事件
                                                    dialoginterface.dismiss();
                                                    entrance_flag_editor.putInt(Constants.ENTRANCE_FLAG, Constants.IS_FROM_APPLICATION);
                                                    entrance_flag_editor.commit();
                                                    Intent intent1 = new Intent();
                                                    intent1.setClass(SubmitInstallmentApplicationStepOneActivity.this, EducationAuthActivity.class);
                                                    startActivity(intent1);
                                                }
                                            })
                                    .show();
                            return;

                        }
                        break;
                    case 2:
                        //工作认证
                        if ( workStatus == Constants.WAIT_SUBMIT ) {

                            new AlertDialog.Builder(this)
                                    .setMessage("请先填写工作信息")
                                    .setPositiveButton(getString(R.string.OK),
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialoginterface, int i) {
                                                    //按钮事件
                                                    dialoginterface.dismiss();
                                                    entrance_flag_editor.putInt(Constants.ENTRANCE_FLAG, Constants.IS_FROM_APPLICATION);
                                                    entrance_flag_editor.commit();
                                                    Intent intent1 = new Intent();
                                                    intent1.setClass(SubmitInstallmentApplicationStepOneActivity.this, JobInfoActivity.class);
                                                    startActivity(intent1);
                                                }
                                            })
                                    .show();
                            return;

                        } else if ( workStatus == Constants.UNPASS ) {

                            new AlertDialog.Builder(this)
                                    .setMessage("请先修改工作信息")
                                    .setPositiveButton(getString(R.string.OK),
                                            new DialogInterface.OnClickListener(){
                                                public void onClick(DialogInterface dialoginterface, int i){
                                                    //按钮事件
                                                    dialoginterface.dismiss();
                                                    entrance_flag_editor.putInt(Constants.ENTRANCE_FLAG, Constants.IS_FROM_APPLICATION);
                                                    entrance_flag_editor.commit();
                                                    Intent intent1 = new Intent();
                                                    intent1.setClass(SubmitInstallmentApplicationStepOneActivity.this, JobInfoActivity.class);
                                                    startActivity(intent1);
                                                }
                                            })
                                    .show();
                            return;

                        }
                        break;

                }

                if ( contactStatus == Constants.WAIT_SUBMIT ) {

                    new AlertDialog.Builder(this)
                            .setMessage("请先填写联系人信息")
                            .setPositiveButton(getString(R.string.OK),
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialoginterface, int i) {
                                            //按钮事件
                                            dialoginterface.dismiss();
                                            entrance_flag_editor.putInt(Constants.ENTRANCE_FLAG, Constants.IS_FROM_APPLICATION);
                                            entrance_flag_editor.commit();
                                            Intent intent1 = new Intent();
                                            intent1.setClass(SubmitInstallmentApplicationStepOneActivity.this, ContactsAuthActivity.class);
                                            startActivity(intent1);
                                        }
                                    })
                            .show();
                    return;

                } else if ( contactStatus == Constants.UNPASS ) {

                    new AlertDialog.Builder(this)
                            .setMessage("请先修改联系人信息")
                            .setPositiveButton(getString(R.string.OK),
                                    new DialogInterface.OnClickListener(){
                                        public void onClick(DialogInterface dialoginterface, int i){
                                            //按钮事件
                                            dialoginterface.dismiss();
                                            entrance_flag_editor.putInt(Constants.ENTRANCE_FLAG, Constants.IS_FROM_APPLICATION);
                                            entrance_flag_editor.commit();
                                            Intent intent1 = new Intent();
                                            intent1.setClass(SubmitInstallmentApplicationStepOneActivity.this, ContactsAuthActivity.class);
                                            startActivity(intent1);
                                        }
                                    })
                            .show();
                    return;

                }

                intent.setClass(this, SIAStepOneResultActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("doFinish", true);
                final List<NameValuePair> postParameters = new ArrayList<>();
                postParameters.add(new BasicNameValuePair("orderCode",App.INSTANCE.getActiveApplyData().getOrderCode()+""));
                postParameters.add(new BasicNameValuePair("step",1+""));
                postParameters.add(new BasicNameValuePair("api", "examinaApi"));

//                ArrayList<BasicNameValuePair> location = LocationUtil.getLocation(this);
//                Log.i("lng", location.get(0).getValue());
//                Log.i("lat", location.get(1).getValue());
//                postParameters.add(new BasicNameValuePair("channel", "android"));
//                postParameters.add(new BasicNameValuePair("lng",location.get(0).getValue()));
//                postParameters.add(new BasicNameValuePair("lat",location.get(1).getValue()));
//
//                HttpAsyncTaskWithLoadingBar httpAsyncTaskWithLoadingBar = new HttpAsyncTaskWithLoadingBar() {
//
//                    @Override
//                    protected Activity getContextActivity() {
//                        return SubmitInstallmentApplicationStepOneActivity.this;
//                    }
//
//                    @Override
//                    protected String doInBackground(String... params) {
//
//                        String s = HttpClient2.getInstance().post(postParameters);
//                        Log.i("HttpResponse",s);
//
//                        return s;
//                    }
//
//                };
//                httpAsyncTaskWithLoadingBar.execute();

                HttpAsyncTask.getInstance(this,PreferenceUtils.getUserId()+"",postParameters).post(intent);
                break;
        }
    }

    private void pullBaseInfoData(Intent intent) {

        List<NameValuePair> pullParameters = new ArrayList<>();
        pullParameters.add(new BasicNameValuePair("api", "queryUserBasicInfo"));
        HttpAsyncTask asyncTask = HttpAsyncTask.getInstance(this, PreferenceUtils.getUserId()+"",pullParameters);
        asyncTask.pullBaseInfo(intent);

    }

}