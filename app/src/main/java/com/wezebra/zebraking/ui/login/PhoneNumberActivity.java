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

import com.wezebra.zebraking.R;
import com.wezebra.zebraking.http.CusSuccessListener;
import com.wezebra.zebraking.http.TaskResponse;
import com.wezebra.zebraking.http.ZebraTask;
import com.wezebra.zebraking.http.data.LoginData;
import com.wezebra.zebraking.ui.BaseActivity;
import com.wezebra.zebraking.util.CommonUtils;
import com.wezebra.zebraking.util.Constants;
import com.wezebra.zebraking.util.PreferenceUtils;

import java.util.Map;
import java.util.TreeMap;

public class PhoneNumberActivity extends BaseActivity implements View.OnClickListener
{
    /**
     * 用户已存在，未设置密码
     */
    public static final int EXISTED_UNSET = 201;
    /**
     * 用户已存在，并且已经设置过密码
     */
    public static final int EXISTED_SET = 202;
    /**
     * 用户不存在
     */
    public static final int UNEXISTED = 203;
    private EditText phoneNumber;
    private TextView submit;
    private ImageView leftIcon;
    private TextView midTitle;

    private TextView.OnEditorActionListener editorActionListener = new TextView.OnEditorActionListener()
    {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
        {
            if (actionId == EditorInfo.IME_ACTION_DONE)
            {
                preLogin();
                return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        setContentView(R.layout.activity_phone_number);

        /**
         * 第一次登陆，则跳转到引导页面{@link GuideActivity}
         */
        if (PreferenceUtils.getIsFirstOpen())
        {
            Intent intent = new Intent(this, GuideActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        initView();
    }

    private void initView()
    {
        phoneNumber = (EditText) findViewById(R.id.phone_number);
        phoneNumber.setOnEditorActionListener(editorActionListener);
        submit = (TextView) findViewById(R.id.submit);
        submit.setOnClickListener(this);
        leftIcon = (ImageView)findViewById(R.id.title_bar_left_icon);
        midTitle = (TextView)findViewById(R.id.title_bar_mid_text);
        leftIcon.setVisibility(View.GONE);
        midTitle.setText("登录");
    }

    @Override
    protected void baseInit()
    {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    /**
     * 登录函数
     *
     * @param number 手机号码
     */
    private void login(final String number)
    {
        Map<String, String> params = new TreeMap<>();
        params.put("api", Constants.API_LOGIN);
        params.put("mobile", number);

        new ZebraTask.Builder(this, params, new CusSuccessListener()
        {
            @Override
            public void onSuccess(TaskResponse response)
            {
                LoginData data = (LoginData) response.getData();
                    int state = data.getState();
                Intent intent = null;

                switch (state)
                {
                    case EXISTED_UNSET:
                        intent = new Intent(PhoneNumberActivity.this, PasswordResetActivity.class);
                        intent.putExtra(PasswordResetActivity.STATE, state);
                        break;
                    case EXISTED_SET:
                        intent = new Intent(PhoneNumberActivity.this, PasswordActivity.class);
                        break;
                    case UNEXISTED:
                        intent = new Intent(PhoneNumberActivity.this, PasswordResetActivity.class);
                        intent.putExtra(PasswordResetActivity.STATE, state);
                        break;
                }

                // 返回成功则保存手机号码
                PreferenceUtils.setPhoneNum(number);
                startActivity(intent);
            }
        }, LoginData.class).build().execute();
    }

    @Override
    public void onClick(View v)
    {
        preLogin();
    }

    private void preLogin()
    {
        String number = phoneNumber.getText().toString().trim();

        if (TextUtils.isEmpty(number))
        {
            phoneNumber.setError("号码不能为空");
            return;
        }

        if (!CommonUtils.isMobileNO(number))
        {
            phoneNumber.setError("请输入正确的手机号码");
            return;
        }

        CommonUtils.hideSoftKeyboard(PhoneNumberActivity.this);
        login(number);
    }
}
