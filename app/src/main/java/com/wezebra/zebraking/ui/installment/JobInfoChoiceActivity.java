package com.wezebra.zebraking.ui.installment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.wezebra.zebraking.R;
import com.wezebra.zebraking.http.CusSuccessListener;
import com.wezebra.zebraking.http.TaskResponse;
import com.wezebra.zebraking.http.ZebraTask;
import com.wezebra.zebraking.http.data.FourAuthData;
import com.wezebra.zebraking.http.data.LoginData;
import com.wezebra.zebraking.ui.BaseActivity;
import com.wezebra.zebraking.ui.login.PasswordActivity;
import com.wezebra.zebraking.ui.login.PasswordResetActivity;
import com.wezebra.zebraking.util.Constants;
import com.wezebra.zebraking.util.L;
import com.wezebra.zebraking.util.PreferenceUtils;

import java.util.Map;
import java.util.TreeMap;

/**
 * Created by admin on 2015/7/24.
 */
public class JobInfoChoiceActivity extends BaseActivity implements View.OnClickListener{

    private static final String TAG = "JobInfoChoiceActivity";
    private RelativeLayout service_info;
    private RelativeLayout companyEmail;
    private RelativeLayout security_auth;
    private RelativeLayout public_acc_funds_auth;
    private ImageView item_title_icon_service;
    private ImageView item_title_icon_email;
    private ImageView item_title_icon_security_auth;
    private ImageView item_title_icon_public;
    int job,email,social,reserve;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jobinfo_choice);
        intiView();
        preStatus();
    }

    /**
     *
     * 初始化状态
     */
    private void preStatus() {
        Map<String,String> params = new TreeMap<>();
        params.put("api", "queryFourAuthStatusApi");
        params.put("orderCode", PreferenceUtils.getPayOrderCode()+"");
        new ZebraTask.Builder(this, params, new CusSuccessListener()
        {
            @Override
            public void onSuccess(TaskResponse response)
            {
                FourAuthData data = (FourAuthData) response.getData();
                int jobStatus = data.getOnJobStatusInt();
                L.i(TAG,data.toString());
                inintStatus(data.getOnJobStatusInt(),data.getCompanyEamilInt(),data.getSocialSecurityStatusInt(),data.getReserveStatusInt());
            }
        }, FourAuthData.class).build().execute();
    }

    private void inintStatus(int jobStatus,int emailStatus,int socialStatus,int reserveStatus){
        job = jobStatus;
        email = emailStatus;
        social = socialStatus;
        reserve = reserveStatus;
        if (Constants.AUDITING == jobStatus || Constants.PASSED == jobStatus){
            changeImageStatus(item_title_icon_service,R.drawable.job_info_green);
        }
        if (Constants.AUDITING == emailStatus || Constants.PASSED == emailStatus){
            changeImageStatus(item_title_icon_email,R.drawable.email_green);
        }
        if (Constants.AUDITING == socialStatus || Constants.PASSED == socialStatus){
            changeImageStatus(item_title_icon_security_auth,R.drawable.social_security_green);
        }
        if (Constants.AUDITING == reserveStatus || Constants.PASSED == reserveStatus){
            changeImageStatus(item_title_icon_public,R.drawable.paf_green);
        }

    }

    /**
     *
     * @param imageView
     * @param resId
     */
    private void changeImageStatus(ImageView imageView,int resId) {
        imageView.setImageResource(resId);
    }


    private void intiView() {
        service_info = (RelativeLayout) findViewById(R.id.service_info);
        companyEmail = (RelativeLayout) findViewById(R.id.company_email);
        security_auth = (RelativeLayout) findViewById(R.id.security_auth);
        public_acc_funds_auth = (RelativeLayout) findViewById(R.id.public_acc_funds_auth);
        service_info.setOnClickListener(this);
        companyEmail.setOnClickListener(this);
        security_auth.setOnClickListener(this);
        public_acc_funds_auth.setOnClickListener(this);
        item_title_icon_service = (ImageView) findViewById(R.id.item_title_icon_service);
        item_title_icon_email = (ImageView) findViewById(R.id.item_title_icon_email);
        item_title_icon_security_auth = (ImageView) findViewById(R.id.item_title_icon_security_auth);
        item_title_icon_public = (ImageView) findViewById(R.id.item_title_icon_public);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        Class clazz =null;
        Intent intent = new Intent();
        switch (id){
            case R.id.service_info:
                if (job == Constants.AUDITING){
                    Toast.makeText(this, getString(R.string.status_auditing_tips), Toast.LENGTH_LONG).show();
                    return;
                }else if (job==Constants.PASSED){
                    intent.setClass(this,ReFillActivity.class);
                    intent.putExtra("title", getString(R.string.title_activity_service_info));
                    intent.putExtra("redirect",Constants.JOB_INFO);
                } else {
                    intent.setClass(this,JobInfoActivity.class);
                }
                break;
            case R.id.company_email:
                L.i(TAG,email+"");
                if (email == Constants.AUDITING){
                    Toast.makeText(this, getString(R.string.status_auditing_tips), Toast.LENGTH_LONG).show();
                    return;
                }else if (email==Constants.PASSED){
                    intent.setClass(this,ReFillActivity.class);
                    intent.putExtra("title", getString(R.string.title_activity_company_email));
                    intent.putExtra("redirect",Constants.EMAIL_INFO);
                } else {
                    intent.setClass(this,CompanyEmailAuth.class);
                }

                break;
            case R.id.security_auth:
                if (social == Constants.AUDITING){
                    Toast.makeText(this, getString(R.string.status_auditing_tips), Toast.LENGTH_LONG).show();
                    return;
                }else if (social==Constants.PASSED){
                    intent.setClass(this,ReFillActivity.class);
                    intent.putExtra("title", getString(R.string.title_activity_social_security_auth));
                    intent.putExtra("redirect",Constants.SS_INFO);
                } else {
                    intent.setClass(this,SocialSecurityAuthActivity.class);
                }

//                clazz = SocialSecurityAuthActivity.class;
                break;
            case R.id.public_acc_funds_auth:
                if (reserve == Constants.AUDITING){
                    Toast.makeText(this, getString(R.string.status_auditing_tips), Toast.LENGTH_LONG).show();
                    return;
                }else if (reserve==Constants.PASSED){
                    intent.setClass(this,ReFillActivity.class);
                    intent.putExtra("title", getString(R.string.title_activity_public_acc_funds_auth));
                    intent.putExtra("redirect",Constants.PAF_INFO);
                } else {
                    intent.setClass(this,PublicAccFundsAuthActivity.class);
                }
//                clazz  = PublicAccFundsAuthActivity.class;
                break;
        }
        startActivity(intent);
    }

}
