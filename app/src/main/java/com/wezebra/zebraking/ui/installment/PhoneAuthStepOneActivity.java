package com.wezebra.zebraking.ui.installment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.wezebra.zebraking.App;
import com.wezebra.zebraking.R;
import com.wezebra.zebraking.behavior.HttpAsyncTask;
import com.wezebra.zebraking.model.MobilePhoneAuth;
import com.wezebra.zebraking.ui.BaseActivity;
import com.wezebra.zebraking.util.GenericUtils;
import com.wezebra.zebraking.util.PreferenceUtils;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class PhoneAuthStepOneActivity extends BaseActivity implements View.OnClickListener {

    private EditText mobilePhoneNumber;
    private TextView submit;
    private String mobilePhoneNumber_s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_auth_step_one);

        initView();
        ((App)getApplication()).mobilePhoneAuth = new MobilePhoneAuth();
    }

    private void initView() {

        submit = (TextView)findViewById(R.id.submit);
        submit.setOnClickListener(this);
        mobilePhoneNumber = (EditText)findViewById(R.id.mobile_phone_number);

    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_phone_auth, menu);
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

        switch (v.getId()) {
            case R.id.submit:

                mobilePhoneNumber_s = mobilePhoneNumber.getText().toString();
                if ( !GenericUtils.checkMobile(mobilePhoneNumber_s) ) {
                    mobilePhoneNumber.requestFocus();
                    mobilePhoneNumber.setError(getString(R.string.mobile_phone_format_errors));
                    return;
                } else {
                    List<NameValuePair> postParameters = new ArrayList<>();
                    postParameters.add(new BasicNameValuePair("mobile", mobilePhoneNumber_s));
                    ((App)getApplication()).mobilePhoneAuth.setMobilePhoneNumber(mobilePhoneNumber_s);
                    postParameters.add(new BasicNameValuePair("index","1"));
                    ((App)getApplication()).mobilePhoneAuth.setIndex(1);
                    postParameters.add(new BasicNameValuePair("api", "authPhoneStep1"));
                    HttpAsyncTask httpAsyncTask = HttpAsyncTask.getInstance(PhoneAuthStepOneActivity.this, PreferenceUtils.getUserId()+"", postParameters);

                    Intent intent = new Intent();
                    intent.setClass(this,PhoneAuthStepTwoActivity.class);
                    intent.putExtra("doFinish",false);
                    httpAsyncTask.mobilePhoneAuthPostStepOne(intent);
                }

                break;
        }

    }
}
