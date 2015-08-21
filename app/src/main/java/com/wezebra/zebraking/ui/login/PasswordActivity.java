package com.wezebra.zebraking.ui.login;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wezebra.zebraking.R;
import com.wezebra.zebraking.http.CusFailListener;
import com.wezebra.zebraking.http.CusSuccessListener;
import com.wezebra.zebraking.http.TaskResponse;
import com.wezebra.zebraking.http.ZebraTask;
import com.wezebra.zebraking.http.data.SignInData;
import com.wezebra.zebraking.ui.BaseActivity;
import com.wezebra.zebraking.ui.MainActivity;
import com.wezebra.zebraking.util.Base64Digest;
import com.wezebra.zebraking.util.CommonUtils;
import com.wezebra.zebraking.util.Constants;
import com.wezebra.zebraking.util.PreferenceUtils;

import java.util.Map;
import java.util.TreeMap;

public class PasswordActivity extends BaseActivity implements View.OnClickListener
{
    private EditText etPassword;
    private TextView submit;
    private TextView forgetPassword;
    private ImageView leftIcon;
    private TextView midTitle;

    private TextView.OnEditorActionListener editorActionListener = new TextView.OnEditorActionListener()
    {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
        {
            if (actionId == EditorInfo.IME_ACTION_DONE)
            {
                preSignIn();
                return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);
        initView();
    }

    private void initView()
    {
        etPassword = (EditText) findViewById(R.id.password);
        etPassword.setOnEditorActionListener(editorActionListener);
        submit = (TextView) findViewById(R.id.submit);
        forgetPassword = (TextView) findViewById(R.id.forget_password);
        submit.setOnClickListener(this);
        forgetPassword.setOnClickListener(this);
        leftIcon = (ImageView) findViewById(R.id.title_bar_left_icon);
        midTitle = (TextView) findViewById(R.id.title_bar_mid_text);
        midTitle.setText("登录");
        leftIcon.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                onBackPressed();
            }
        });
    }

    @Override
    protected void baseInit()
    {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    private boolean validatePassword(String password)
    {
        if (TextUtils.isEmpty(password) || password.length() < 6 || !CommonUtils.isNumberOrLetter(password))
        {
            etPassword.setError("请输入6位以上的数字和字母");
            return false;
        }

        return true;
    }

    private void signIn(String password)
    {
        Map<String, String> params = new TreeMap<>();
        params.put("api", Constants.API_SIGN_IN);
        params.put("mobile", PreferenceUtils.getPhoneNum());
        params.put("password", password);

        new ZebraTask.Builder(this, params, new CusSuccessListener()
        {
            @Override
            public void onSuccess(TaskResponse response)
            {
                SignInData data = (SignInData) response.getData();

                PreferenceUtils.setUserId(data.getUid());
                PreferenceUtils.setKey(data.getKey());
                PreferenceUtils.setRole(data.getRole());

                Intent intent = new Intent(PasswordActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        }, SignInData.class).setCusFailListener(new CusFailListener()
        {
            @Override
            public void onFail(TaskResponse response)
            {
                if (response.getCode() == 2004)
                {
                    SignInData data = (SignInData) response.getData();
                    int times = data.getTimes();
                    if (times != 0)
                    {
                        Toast.makeText(PasswordActivity.this, "密码错误，今日还有" + data.getTimes() + "次机会!", Toast.LENGTH_SHORT).show();
                    } else
                    {
                        Toast.makeText(PasswordActivity.this, response.getDesc(), Toast.LENGTH_SHORT).show();
                    }

                } else
                {
                    ZebraTask.handleFailResponse(PasswordActivity.this, response);
                }
            }
        }).build().execute();
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.forget_password:
                Intent intent = new Intent(this, PasswordResetActivity.class);
                intent.putExtra(PasswordResetActivity.RESET, true);
                startActivity(intent);
                break;
            case R.id.submit:
                preSignIn();
                break;
        }
    }

    private void preSignIn()
    {
        String password = etPassword.getText().toString().trim();
        if (validatePassword(password))
        {
            CommonUtils.hideSoftKeyboard(PasswordActivity.this);
            signIn(Base64Digest.encode(password));
        }
    }
}
