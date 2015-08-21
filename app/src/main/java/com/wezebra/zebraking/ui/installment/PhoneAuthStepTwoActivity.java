package com.wezebra.zebraking.ui.installment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.wezebra.zebraking.App;
import com.wezebra.zebraking.R;
import com.wezebra.zebraking.behavior.HttpAsyncTask;
import com.wezebra.zebraking.ui.BaseActivity;
import com.wezebra.zebraking.util.Base64Digest;
import com.wezebra.zebraking.util.PreferenceUtils;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class PhoneAuthStepTwoActivity extends BaseActivity implements View.OnClickListener {

    private TextView submit;
    public EditText password,verifyCode;
    public ImageView verifyCodeImg;
    public LinearLayout verifyCodeLayout;

    private String password_s,verifyCode_s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_auth_step_two);

        initView();
    }

    private void initView() {

        submit = (TextView)findViewById(R.id.submit);
        submit.setOnClickListener(this);

        verifyCodeLayout = (LinearLayout)findViewById(R.id.verify_code_layout);
        verifyCodeImg = (ImageView)findViewById(R.id.verify_code_img);
        if ( App.mobilePhoneAuth.getIsNeed() == true ) {
            verifyCodeLayout.setVisibility(View.VISIBLE);
            ImageLoader imageLoader = ImageLoader.getInstance();
            imageLoader.displayImage( App.mobilePhoneAuth.getUrl(),verifyCodeImg );
        } else {
            verifyCodeLayout.setVisibility(View.INVISIBLE);
        }

        password = (EditText)findViewById(R.id.password);
        verifyCode = (EditText)findViewById(R.id.verify_code);

    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_phone_auth_step_two, menu);
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
                password_s = password.getText().toString();
                if (password_s.equals("")) {
                    password.requestFocus();
                    password.setError(getString(R.string.password_empty_errors));
                    return;
                }
                if ( App.mobilePhoneAuth.getIsNeed() == true ) {
                    verifyCode_s = verifyCode.getText().toString();
                    if (verifyCode_s.equals("")) {
                        verifyCode.requestFocus();
                        verifyCode.setError(getString(R.string.verify_code_empty_errors));
                        return;
                    }
                }

                List<NameValuePair> postParameters = new ArrayList<>();
                postParameters.add(new BasicNameValuePair("mobile", App.mobilePhoneAuth.getMobilePhoneNumber()));
                postParameters.add(new BasicNameValuePair("password", Base64Digest.encode(password_s)));
                if ( App.mobilePhoneAuth.getIsNeed() == true ) {
                   postParameters.add(new BasicNameValuePair("authCode",verifyCode_s));
                }
                postParameters.add(new BasicNameValuePair("source",App.mobilePhoneAuth.getSource()));
                postParameters.add(new BasicNameValuePair("api", "authPhoneStep2"));
                HttpAsyncTask httpAsyncTask = HttpAsyncTask.getInstance(PhoneAuthStepTwoActivity.this, PreferenceUtils.getUserId()+"", postParameters);

                Intent intent = new Intent();
                intent.setClass(this,PhoneAuthStepFourActivity.class);
                intent.putExtra("doFinish",false);
                httpAsyncTask.mobilePhoneAuthPostStepTwo(intent);

                break;
        }

    }
}
