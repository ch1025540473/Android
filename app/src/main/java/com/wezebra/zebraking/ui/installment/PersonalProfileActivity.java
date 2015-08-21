package com.wezebra.zebraking.ui.installment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.wezebra.zebraking.App;
import com.wezebra.zebraking.R;
import com.wezebra.zebraking.behavior.HttpAsyncTask;
import com.wezebra.zebraking.ui.BaseActivity;
import com.wezebra.zebraking.util.Constants;
import com.wezebra.zebraking.util.L;
import com.wezebra.zebraking.util.PreferenceUtils;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;
//个人信息完善界面
public class PersonalProfileActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout baseInfoLayout,idInfoLayout,jobInfoLayout,contactsInfoLayout,eduInfoLayout,incomeInfoLayout,socialSecurityAuthLayout,pafAuthLayout,phoneAuthLayout,email_info,sesameAuth;
    public ImageView baseAuthImg,idAuthImg,jobAuthImg,contactsAuthImg,eduAuthImg,incomeAuthImg,ssAuthImg,pafAuthImg,phoneAuthImg,job_auth_img,email_info_img;

    private SharedPreferences sharedPreferences,entrance_flag;
    private SharedPreferences.Editor editor,entrance_flag_editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_profile);
        L.i("testss........",PreferenceUtils.getUserName());
        initView();
    }

    private void initView() {

        baseInfoLayout = (LinearLayout)findViewById(R.id.base_info_layout);
        idInfoLayout = (LinearLayout)findViewById(R.id.id_info_layout);
        jobInfoLayout = (LinearLayout)findViewById(R.id.job_info_layout);
        contactsInfoLayout = (LinearLayout)findViewById(R.id.contacts_info_layout);
        eduInfoLayout = (LinearLayout)findViewById(R.id.edu_info_layout);
        incomeInfoLayout = (LinearLayout)findViewById(R.id.income_info_layout);
        socialSecurityAuthLayout = (LinearLayout)findViewById(R.id.social_security_auth_layout);
        pafAuthLayout = (LinearLayout)findViewById(R.id.paf_auth_layout);
        phoneAuthLayout = (LinearLayout)findViewById(R.id.phone_auth_layout);
        email_info = (LinearLayout) findViewById(R.id.email_info);
        sesameAuth = (LinearLayout) findViewById(R.id.sesame_autu);

        baseInfoLayout.setOnClickListener(this);
        idInfoLayout.setOnClickListener(this);
        jobInfoLayout.setOnClickListener(this);
        contactsInfoLayout.setOnClickListener(this);
        eduInfoLayout.setOnClickListener(this);
        incomeInfoLayout.setOnClickListener(this);
        socialSecurityAuthLayout.setOnClickListener(this);
        pafAuthLayout.setOnClickListener(this);
        phoneAuthLayout.setOnClickListener(this);
        email_info.setOnClickListener(this);
        sesameAuth.setOnClickListener(this);

        baseAuthImg = (ImageView)findViewById(R.id.base_auth_img);
        idAuthImg = (ImageView)findViewById(R.id.id_auth_img);
        jobAuthImg = (ImageView)findViewById(R.id.job_auth_img);
        contactsAuthImg = (ImageView)findViewById(R.id.contacts_auth_img);
        eduAuthImg = (ImageView)findViewById(R.id.edu_auth_img);
        incomeAuthImg = (ImageView)findViewById(R.id.income_auth_img);
        ssAuthImg = (ImageView)findViewById(R.id.ss_auth_img);
        pafAuthImg = (ImageView)findViewById(R.id.paf_auth_img);
        phoneAuthImg = (ImageView)findViewById(R.id.phone_auth_img);
        job_auth_img = (ImageView)findViewById(R.id.job_auth_img);
        email_info_img = (ImageView)findViewById(R.id.email_info_img);

    }

    @Override
    protected void onResume() {
        super.onResume();

        pullStatus();

    }

    private void pullStatus() {

        List<NameValuePair> pullParameters = new ArrayList<>();
//        pullParameters.add(new BasicNameValuePair("api", "getUserLogin"));
        pullParameters.add(new BasicNameValuePair("api", "queryUserInfoAuthStatusApi"));
        HttpAsyncTask asyncTask = HttpAsyncTask.getInstance(this, PreferenceUtils.getUserId()+"",pullParameters);
        asyncTask.pullUserLogin();

    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_personal_profile, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    public void onClick(View v) {

        Intent intent = new Intent();
        entrance_flag = getSharedPreferences(Constants.ENTRANCE_FLAG,MODE_PRIVATE);
        entrance_flag_editor = entrance_flag.edit();
        switch (v.getId()) {
            case R.id.base_info_layout:
                if (App.userLogin.getBasicStatusInt() == Constants.AUDITING) {
                    Toast.makeText(this, getString(R.string.status_auditing_tips), Toast.LENGTH_LONG).show();
                    return;
                }
                entrance_flag_editor.putInt(Constants.ENTRANCE_FLAG, Constants.IS_FROM_PROFILE);
                entrance_flag_editor.commit();
                //如果状态已经通过
                if (App.userLogin.getBasicStatusInt() == Constants.PASSED) {
                    intent.setClass(this,ReFillActivity.class);
                    intent.putExtra("title", getString(R.string.title_activity_base_info));
                    intent.putExtra("redirect",Constants.BASIC_INFO);
                } else {
                    intent.setClass(this, BaseInfoStepInitialActivity.class);
                }
                pullBaseInfoData(intent);
                break;
            case R.id.id_info_layout:
                if (App.userLogin.getIdentityStatusInt() == Constants.AUDITING) {
                    Toast.makeText(this,getString(R.string.status_auditing_tips),Toast.LENGTH_LONG).show();
                    return;
                }
                entrance_flag_editor.putInt(Constants.ENTRANCE_FLAG, Constants.IS_FROM_PROFILE);
                entrance_flag_editor.commit();
                if (App.userLogin.getIdentityStatusInt() == Constants.PASSED) {
                    intent.setClass(this,ReFillActivity.class);
                    intent.putExtra("title", getString(R.string.title_activity_identification));
                    intent.putExtra("redirect",Constants.IDENTITY_INFO);
                } else {
                    intent.setClass(this,IdentificationActivity.class);
                }
                startActivity(intent);
                break;
            case R.id.job_info_layout:
                L.i("job_info_layout",App.userLogin.getOnJobStatusInt()+"");
                if (App.userLogin.getOnJobStatusInt() == Constants.AUDITING) {
                    Toast.makeText(this,getString(R.string.status_auditing_tips),Toast.LENGTH_LONG).show();
                    return;
                }
                if (App.userLogin.getOnJobStatusInt() == Constants.PASSED) {
                    intent.setClass(this,ReFillActivity.class);
                    intent.putExtra("title", getString(R.string.title_activity_service_info));
                    intent.putExtra("redirect", Constants.JOB_INFO);
                } else {
                    intent.setClass(this, JobInfoActivity.class);
                }
                entrance_flag_editor.putInt(Constants.ENTRANCE_FLAG,Constants.IS_FROM_PROFILE);
                entrance_flag_editor.commit();
                startActivity(intent);
                break;
            case R.id.contacts_info_layout:
                if (App.userLogin.getContactStatusInt() == Constants.AUDITING) {
                    Toast.makeText(this,getString(R.string.status_auditing_tips),Toast.LENGTH_LONG).show();
                    return;
                }
                entrance_flag_editor.putInt(Constants.ENTRANCE_FLAG,Constants.IS_FROM_PROFILE);
                entrance_flag_editor.commit();
                if (App.userLogin.getContactStatusInt() == Constants.PASSED) {
                    intent.setClass(this,ReFillActivity.class);
                    intent.putExtra("title", getString(R.string.title_activity_contacts_auth));
                    intent.putExtra("redirect", Constants.CONTACTS_INFO);
                } else {
                    intent.setClass(this,ContactsAuthActivity.class);
                }
                startActivity(intent);
                break;
            case R.id.edu_info_layout:
                if (App.userLogin.getEducationStatusInt() == Constants.AUDITING) {
                    Toast.makeText(this,getString(R.string.status_auditing_tips),Toast.LENGTH_LONG).show();
                    return;
                }
                entrance_flag_editor.putInt(Constants.ENTRANCE_FLAG, Constants.IS_FROM_PROFILE);
                entrance_flag_editor.commit();
                if (App.userLogin.getEducationStatusInt() == Constants.PASSED) {
                    intent.setClass(this,ReFillActivity.class);
                    intent.putExtra("title", getString(R.string.title_activity_education_auth));
                    intent.putExtra("redirect", Constants.EDUCATION_INFO);
                } else {
                    intent.setClass(this,EducationAuthActivity.class);
                }
                startActivity(intent);
                break;
            case R.id.income_info_layout:
                if (App.userLogin.getIncomeStatusInt() == Constants.AUDITING) {
                    Toast.makeText(this,getString(R.string.status_auditing_tips),Toast.LENGTH_LONG).show();
                    return;
                }
                entrance_flag_editor.putInt(Constants.ENTRANCE_FLAG, Constants.IS_FROM_PROFILE);
                entrance_flag_editor.commit();
                if (App.userLogin.getIncomeStatusInt() == Constants.PASSED) {
                    intent.setClass(this,ReFillActivity.class);
                    intent.putExtra("title", getString(R.string.title_activity_income_auth));
                    intent.putExtra("redirect", Constants.INCOME_INFO);
                } else {
                    intent.setClass(this,IncomeAuthActivity.class);
                }
                startActivity(intent);
                break;
            case R.id.social_security_auth_layout:
                if (App.userLogin.getSocialSecurityStatusInt() == Constants.AUDITING) {
                    Toast.makeText(this,getString(R.string.status_auditing_tips),Toast.LENGTH_LONG).show();
                    return;
                }
                entrance_flag_editor.putInt(Constants.ENTRANCE_FLAG, Constants.IS_FROM_PROFILE);
                entrance_flag_editor.commit();
                if (App.userLogin.getSocialSecurityStatusInt() == Constants.PASSED) {
                    intent.setClass(this,ReFillActivity.class);
                    intent.putExtra("title", getString(R.string.title_activity_social_security_auth));
                    intent.putExtra("redirect", Constants.SS_INFO);
                } else {
                    intent.setClass(this,SocialSecurityAuthActivity.class);
                }
                startActivity(intent);
                break;
            case R.id.paf_auth_layout:
                if (App.userLogin.getReserveStatusInt() == Constants.AUDITING) {
                    Toast.makeText(this,getString(R.string.status_auditing_tips),Toast.LENGTH_LONG).show();
                    return;
                }
                entrance_flag_editor.putInt(Constants.ENTRANCE_FLAG, Constants.IS_FROM_PROFILE);
                entrance_flag_editor.commit();
                if (App.userLogin.getReserveStatusInt() == Constants.PASSED) {
                    intent.setClass(this,ReFillActivity.class);
                    intent.putExtra("title", getString(R.string.title_activity_public_acc_funds_auth));
                    intent.putExtra("redirect", Constants.PAF_INFO);
                } else {
                    intent.setClass(this,PublicAccFundsAuthActivity.class);
                }
                startActivity(intent);
                break;
            case R.id.phone_auth_layout:
                if (App.userLogin.getMobileStatusInt() == Constants.AUDITING) {
                    Toast.makeText(this,getString(R.string.status_auditing_tips),Toast.LENGTH_LONG).show();
                    return;
                }
                entrance_flag_editor.putInt(Constants.ENTRANCE_FLAG, Constants.IS_FROM_PROFILE);
                entrance_flag_editor.commit();
                if (App.userLogin.getMobileStatusInt() == Constants.PASSED) {
                    intent.setClass(this,ReFillActivity.class);
                    intent.putExtra("title", getString(R.string.title_activity_phone_auth));
                    intent.putExtra("redirect", Constants.PHONE_INFO);
                } else {
                    intent.setClass(this,PhoneAuthStepOneActivity.class);
                }
                startActivity(intent);
                break;
            case R.id.email_info:
                L.i("email_info",App.userLogin.getCompanyEamilInt()+"");
                if (App.userLogin.getCompanyEamilInt() == Constants.AUDITING) {
                    Toast.makeText(this,getString(R.string.status_auditing_tips),Toast.LENGTH_LONG).show();
                    return;
                }
                entrance_flag_editor.putInt(Constants.ENTRANCE_FLAG, Constants.IS_FROM_PROFILE);
                entrance_flag_editor.commit();
                if (App.userLogin.getCompanyEamilInt() == Constants.PASSED) {
                    intent.setClass(this,ReFillActivity.class);
                    intent.putExtra("title", getString(R.string.title_activity_company_email));
                    intent.putExtra("redirect", Constants.EMAIL_INFO);
                } else {
                    intent.setClass(this,CompanyEmailAuth.class);
                }
                startActivity(intent);
                break;
            case R.id.sesame_autu:
//                if (App.userLogin.getCompanyEamilInt() == Constants.AUDITING) {
//                    Toast.makeText(this,getString(R.string.status_auditing_tips),Toast.LENGTH_LONG).show();
//                    return;
//                }
//                entrance_flag_editor.putInt(Constants.ENTRANCE_FLAG, Constants.IS_FROM_PROFILE);
//                entrance_flag_editor.commit();
//                if (App.userLogin.getCompanyEamilInt() == Constants.PASSED) {
//                    intent.setClass(this,ReFillActivity.class);
//                    intent.putExtra("title", getString(R.string.title_activity_company_email));
//                    intent.putExtra("redirect", Constants.EMAIL_INFO);
//                } else {
//                    intent.setClass(this,CompanyEmailAuth.class);
//                }
                startActivity(new Intent(this,SesameCreditActivity.class));
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
