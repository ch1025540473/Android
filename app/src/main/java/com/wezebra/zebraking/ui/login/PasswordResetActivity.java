package com.wezebra.zebraking.ui.login;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wezebra.zebraking.R;
import com.wezebra.zebraking.http.CusSuccessListener;
import com.wezebra.zebraking.http.TaskResponse;
import com.wezebra.zebraking.http.ZebraTask;
import com.wezebra.zebraking.http.data.SignInData;
import com.wezebra.zebraking.ui.BaseActivity;
import com.wezebra.zebraking.ui.MainActivity;
import com.wezebra.zebraking.ui.WebViewActivity;
import com.wezebra.zebraking.util.Base64Digest;
import com.wezebra.zebraking.util.CommonUtils;
import com.wezebra.zebraking.util.Constants;
import com.wezebra.zebraking.util.L;
import com.wezebra.zebraking.util.PreferenceUtils;
import com.wezebra.zebraking.widget.LinkTouchMovementMethod;
import com.wezebra.zebraking.widget.TouchableSpan;

import java.util.Map;
import java.util.TreeMap;

public class PasswordResetActivity extends BaseActivity implements View.OnClickListener
{
    public static final String TAG = "PasswordResetActivity";
    public static final String STATE = "state";
    public static final String RESET = "reset";
    private EditText etVerify;
    private EditText etPassword;
    private TextView btnVerify;
    private TextView submit;
    private ImageView checkBox;
    private TextView agreement;
    private LinearLayout agreementLayout;
    private TextView tip;
    private Animation showTipAnimation;
    private boolean isTipShowing = false;
    private boolean isChecked = true;
    private int showMargin;
    private String verifyCode;
    private String password;
    private int countTime = -1;
    private boolean reset;
    private ImageView leftIcon;
    private TextView midTitle;
    private Handler timerHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case 1:
                    btnVerify.setText(countTime + "秒");
                    countTime--;
                    break;
                case 2:
                    countTime--;
                    btnVerify.setText("重新获取");
                    btnVerify.setEnabled(true);
                    break;
            }
            super.handleMessage(msg);
        }
    };

    private Runnable timeRunnable = new Runnable()
    {
        @Override
        public void run()
        {
            if (countTime >= 0)
            {
                Message message = timerHandler.obtainMessage();
                if (countTime != 0)
                {
                    message.what = 1;
                } else
                {
                    message.what = 2;
                }
                message.sendToTarget();
                timerHandler.postDelayed(this, 1000);
            }
        }
    };

    private TextView.OnEditorActionListener editorActionListener = new TextView.OnEditorActionListener()
    {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
        {
            if (actionId == EditorInfo.IME_ACTION_DONE)
            {
                preResetPassword();
                return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_reset);
        initView();
        initValue();
    }

    @Override
    protected void baseInit()
    {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    private void initView()
    {
        leftIcon = (ImageView) findViewById(R.id.title_bar_left_icon);
        midTitle = (TextView) findViewById(R.id.title_bar_mid_text);
        midTitle.setText("重设密码");
        leftIcon.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                onBackPressed();
            }
        });

        etVerify = (EditText) findViewById(R.id.et_verify);
        etPassword = (EditText) findViewById(R.id.et_new_password);
        etPassword.setOnEditorActionListener(editorActionListener);
        btnVerify = (TextView) findViewById(R.id.btn_verify);
        submit = (TextView) findViewById(R.id.submit);
        checkBox = (ImageView) findViewById(R.id.checkbox);
        agreement = (TextView) findViewById(R.id.agreement);
        agreementLayout = (LinearLayout) findViewById(R.id.agreement_layout);
        tip = (TextView) findViewById(R.id.tip);

        btnVerify.setOnClickListener(this);
        submit.setOnClickListener(this);
//        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
//        {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
//            {
//                checkBox.setBackgroundResource(R.drawable.uncheckbox);
//                PasswordResetActivity.this.isChecked = isChecked;
//            }
//        });
        checkBox.setEnabled(true);
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                L.i("imageview----------","1111111111111");
//                checkBox.setBackgroundResource(R.drawable.uncheckbox);
                if (PasswordResetActivity.this.isChecked == true){
                    checkBox.setImageResource(R.drawable.uncheckbox);
                    PasswordResetActivity.this.isChecked = false;
                }else {
                    checkBox.setImageResource(R.drawable.checkbox);
                    PasswordResetActivity.this.isChecked = true;
                }

            }
        });

        // 初始化tip显示动画
        showMargin = -(int) (31 * getResources().getDisplayMetrics().density);
        showTipAnimation = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime,
                                               Transformation t)
            {
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tip.getLayoutParams();
                params.topMargin = (int) (showMargin - showMargin
                        * interpolatedTime);
                tip.setLayoutParams(params);
            }
        };
        showTipAnimation.setDuration(300);


        // 金融服务协议
        SpannableString ss = new SpannableString(getString(R.string.agreement));
        ss.setSpan(new TouchableSpan(getResources().getColor(R.color.text_green)
                , getResources().getColor(R.color.text_light)
                , Color.TRANSPARENT)
        {
            @Override
            public void onClick(View widget)
            {

                WebViewActivity.redirectToWebViewActivity(PasswordResetActivity.this
                        , Constants.URL_AGREEMENT, "斑马王国服务与隐私协议");
            }
        }, 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        agreement.setText(ss);
        agreement.setMovementMethod(new LinkTouchMovementMethod());
    }

    private void initValue()
    {
        int state = getIntent().getIntExtra(STATE, 201);
        reset = getIntent().getBooleanExtra(RESET, false);

        if (!reset)
        {
//            getSupportActionBar().setTitle("设置密码");
            midTitle.setText("设置密码");

            // 尚未设置密码时，显示“设置密码”。默认为“重设密码”
            etPassword.setHint(getResources().getString(R.string.hint_input_set_password));
            if (state == 203)
            {
                // 若是新用户，则显示协议
                agreementLayout.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * 获取验证码
     */
    private void getVerify()
    {
        Map<String, String> params = new TreeMap<>();
        params.put("api", Constants.API_GET_SMS_CODE);
        params.put("mobile", PreferenceUtils.getPhoneNum());

        new ZebraTask.Builder(this, params, new CusSuccessListener()
        {
            @Override
            public void onSuccess(TaskResponse response)
            {
                btnVerify.setEnabled(false);
                countTime = 59;
                btnVerify.setText("60秒");
                timerHandler.postDelayed(timeRunnable, 1000);
//                showTip();
                Toast.makeText(PasswordResetActivity.this, "已向" +
                                PreferenceUtils.getPhoneNum() + "发送了验证码",
                        Toast.LENGTH_SHORT).show();
            }
        }).build().execute();
    }

    /**
     * 重置密码
     */
    private void resetPassword()
    {
        Map<String, String> params = new TreeMap<>();
        params.put("api", Constants.API_UPDATE_PWD);
        params.put("mobile", PreferenceUtils.getPhoneNum());

        // 请求类型：type=1,表示修改密码，type=2,表示设置密码
        params.put("type", "" + (reset ? 1 : 2));
        params.put("smsCode", verifyCode);
        params.put("password", Base64Digest.encode(password));

        new ZebraTask.Builder(this, params, new CusSuccessListener()
        {
            @Override
            public void onSuccess(TaskResponse response)
            {
                SignInData data = (SignInData) response.getData();

                PreferenceUtils.setPattern("");
                PreferenceUtils.setUserId(data.getUid());
                PreferenceUtils.setKey(data.getKey());
                PreferenceUtils.setRole(data.getRole());

                Intent intent = new Intent(PasswordResetActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        }, SignInData.class).build().execute();
    }

    private void showTip()
    {
        if (!isTipShowing)
        {
            SpannableString ss = new SpannableString("已向" + PreferenceUtils.getPhoneNum() + "发送了验证码");
            ss.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.text_blue))
                    , 2, 13, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            tip.setText(ss);
            tip.startAnimation(showTipAnimation);
            isTipShowing = true;
        }
    }

    private boolean validateParams()
    {
        verifyCode = etVerify.getText().toString().trim();
        password = etPassword.getText().toString().trim();

        if (verifyCode.length() != 6 || !CommonUtils.isNumber(verifyCode))
        {
            etVerify.setError("请输入正确的6位验证码");
            return false;
        }

        if (TextUtils.isEmpty(password) || password.length() < 6 || !CommonUtils.isNumberOrLetter(password))
        {
            etPassword.setError("请输入6位以上的数字和字母");
            return false;
        }

        if (!isChecked)
        {
            Toast.makeText(this, "您需要同意《斑马金融服务与隐私协议》", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btn_verify:

                getVerify();
                break;
            case R.id.submit:
                preResetPassword();
                break;
        }
    }

    private void preResetPassword()
    {
        if (validateParams())
        {
            CommonUtils.hideSoftKeyboard(PasswordResetActivity.this);
            resetPassword();
        }
    }
}
