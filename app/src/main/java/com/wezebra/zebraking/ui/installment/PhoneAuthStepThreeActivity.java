package com.wezebra.zebraking.ui.installment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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

public class PhoneAuthStepThreeActivity extends BaseActivity implements View.OnClickListener {

    private EditText verifyCode;
    public ImageView verifyCodeImg;
    private TextView submit;
    public String verifyCode_s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_auth_step_three);

        initView();
    }

    private void initView() {

        verifyCode = (EditText)findViewById(R.id.verify_code);
        verifyCodeImg = (ImageView)findViewById(R.id.verify_code_img);
        submit = (TextView)findViewById(R.id.submit);
        submit.setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();

        pullData();
    }

    private void pullData() {

        List<NameValuePair> pullParameters = new ArrayList<>();
        pullParameters.add(new BasicNameValuePair("mobile",App.mobilePhoneAuth.getMobilePhoneNumber()));
        App.mobilePhoneAuth.setIndex(App.mobilePhoneAuth.getIndex() + 1);
        pullParameters.add(new BasicNameValuePair("index", App.mobilePhoneAuth.getIndex() + ""));
        pullParameters.add(new BasicNameValuePair("api", "authPhoneStep3"));
        HttpAsyncTask.getInstance(this, PreferenceUtils.getUserId()+"",pullParameters).pullPhoneAuthStepThree();

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

        verifyCode_s = verifyCode.getText().toString();
        if (verifyCode_s.equals("")) {
            verifyCode.requestFocus();
            verifyCode.setError(getString(R.string.verify_code_empty_errors));
            return;
        }

        Intent intent = new Intent();
        intent.setClass(this,PhoneAuthStepFourActivity.class);
        intent.putExtra("verifyCode",verifyCode_s);
        startActivity(intent);

    }
}
