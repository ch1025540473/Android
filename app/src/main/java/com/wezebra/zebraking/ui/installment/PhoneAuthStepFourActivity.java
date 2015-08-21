package com.wezebra.zebraking.ui.installment;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wezebra.zebraking.App;
import com.wezebra.zebraking.R;
import com.wezebra.zebraking.behavior.HttpAsyncTask;
import com.wezebra.zebraking.ui.BaseActivity;
import com.wezebra.zebraking.util.PreferenceUtils;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class PhoneAuthStepFourActivity extends BaseActivity implements View.OnClickListener {

    private TextView submit,tips;
    private EditText smsCode;

    public LinearLayout smsTipsLayout;
    public String verifyCode_s;
    public String smsCode_s;

    private Handler handler;
    private Runnable runnable;
    private int seconds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_auth_step_four);

        initView();

        if ( getIntent().getExtras() != null) {
            if ( getIntent().getExtras().getString("verifyCode") != null ) {
                verifyCode_s = getIntent().getExtras().getString("verifyCode");
            } else {
                verifyCode_s = "";
            }
        } else {
            verifyCode_s = "";
        }


        pullData();

        seconds = 0;
        smsTipsLayout.setVisibility(View.VISIBLE);
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                seconds++;
                tips.setText(seconds+"");
                handler.postDelayed(runnable,1000);
            }
        };
        handler.postDelayed(runnable,1000);

    }

    private void initView() {

        submit = (TextView)findViewById(R.id.submit);
        submit.setOnClickListener(this);
        tips = (TextView)findViewById(R.id.sms_code_tips);
        smsCode = (EditText)findViewById(R.id.sms_code);
        smsTipsLayout = (LinearLayout)findViewById(R.id.sms_tips_layout);

    }

    private void pullData() {

        List<NameValuePair> pullParameters = new ArrayList<>();
        pullParameters.add(new BasicNameValuePair("mobile", App.mobilePhoneAuth.getMobilePhoneNumber()));
        if ( !verifyCode_s.equals("") ) {
            pullParameters.add(new BasicNameValuePair("authCode",verifyCode_s));
        }
        pullParameters.add(new BasicNameValuePair("api", "authPhoneStep4"));
        HttpAsyncTask.getInstance(this, PreferenceUtils.getUserId() + "", pullParameters).pullPhoneAuthStepFour();

    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_phone_auth_step_three, menu);
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

                smsCode_s = smsCode.getText().toString();
                if (smsCode_s.equals("")) {
                    smsCode.requestFocus();
                    smsCode.setError(getString(R.string.sms_code_empty_errors));
                    return;
                }

                List<NameValuePair> postParameters = new ArrayList<>();
                postParameters.add(new BasicNameValuePair("mobile",App.mobilePhoneAuth.getMobilePhoneNumber()));
                postParameters.add(new BasicNameValuePair("smsCode",smsCode_s));
                if ( !verifyCode_s.equals("") ) {
                    postParameters.add(new BasicNameValuePair("authCode", verifyCode_s));
                }
                postParameters.add(new BasicNameValuePair("source",App.mobilePhoneAuth.getSource()));
                postParameters.add(new BasicNameValuePair("api", "authPhoneStep5"));
                HttpAsyncTask.getInstance(this,PreferenceUtils.getUserId()+"",postParameters).postPhoneAuthStepFour();

                break;
        }

    }
}
