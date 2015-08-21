package com.wezebra.zebraking.ui.fragment;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wezebra.zebraking.R;
import com.wezebra.zebraking.http.CusSuccessListener;
import com.wezebra.zebraking.http.TaskResponse;
import com.wezebra.zebraking.http.ZebraTask;
import com.wezebra.zebraking.util.ChangeBindListener;
import com.wezebra.zebraking.util.Constants;
import com.wezebra.zebraking.util.L;
import com.wezebra.zebraking.util.PreferenceUtils;

import java.util.Map;
import java.util.TreeMap;

/**
 * Created by admin on 2015/7/27.
 */
public class OldBindPhoneFragment extends Fragment implements View.OnClickListener{

    EditText mobile_edit;
    TextView count_second,submit_old_code,phone_number;
    ChangeBindListener changeBindListener;
    boolean isSend = true;
    private int number = 60;
    LinearLayout title_phone;

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {

            if (msg.what == 0){
                count_second.setText(number+"秒");
                if (number==56){
//                    title_phone.setVisibility(View.GONE);

                    phone_number.setText(changePhoneNum(PreferenceUtils.getPhoneNum()));
                    int y = title_phone.getHeight();
                    ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(title_phone,"translationY",0,-y);
                    objectAnimator.setDuration(3000);
                    objectAnimator.start();
                }
                number--;
            }
            if (msg.what == 1){
                mHandler.removeCallbacks(countRun);
                count_second.setEnabled(true);
                count_second.setText("重新获取");
            }
            super.handleMessage(msg);
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_change_bind_code,null);
        mobile_edit = (EditText) view.findViewById(R.id.mobile_edit);
        count_second = (TextView) view.findViewById(R.id.count_second);
        phone_number = (TextView) view.findViewById(R.id.phone_number);
        title_phone = (LinearLayout) view.findViewById(R.id.titile_phone);
        count_second.setOnClickListener(this);
        submit_old_code = (TextView) view.findViewById(R.id.submit_old_code);
        submit_old_code.setOnClickListener(this);
        return view;
    }


    private String changePhoneNum(String str){
        StringBuilder builder = new StringBuilder();
        builder.append(str.substring(0,3));
        builder.append("****");
        builder.append(str.substring(7));
        return builder.toString();

    }

    @Override
    public void onClick(View v) {
        Map<String,String> params = new TreeMap<>();
        switch (v.getId()){
            case R.id.count_second:

                params.put("api", "getSmsCode");
                params.put("mobile",PreferenceUtils.getPhoneNum());
                new ZebraTask.Builder(getActivity(), params, new CusSuccessListener()
                {
                    @Override
                    public void onSuccess(TaskResponse response)
                    {
                       if (Constants.HTTP_REQUEST_SUCCESS == response.getCode()){
                           number=59;
                           title_phone.setVisibility(View.VISIBLE);
                           phone_number.setText(changePhoneNum(PreferenceUtils.getPhoneNum()));
                           int y = title_phone.getHeight();
                           ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(title_phone,"translationY",-y,0);
                           objectAnimator.setDuration(3000);
                           objectAnimator.start();
                           startCount();
                        }
                    }
                }).build().execute();
                break;
            case R.id.submit_old_code:
                params.put("api", "updateMobile");
                params.put("mobile",PreferenceUtils.getPhoneNum());
                params.put("smsCode",mobile_edit.getText().toString().trim());
                params.put("type",1+"");

                new ZebraTask.Builder(getActivity(), params, new CusSuccessListener()
                {
                    @Override
                    public void onSuccess(TaskResponse response)
                    {
                        if (Constants.HTTP_REQUEST_SUCCESS == response.getCode()){
                            changeBindListener.turn2Next(0);
                        }
                    }
                }).build().execute();
                break;
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try{
            changeBindListener = (ChangeBindListener)activity;
        }catch (ClassCastException e){
            new ClassCastException("must implements ChangeBindListener");
        }

    }

//    private Runnable timeRunnable = new Runnable()
//    {
//        @Override
//        public void run()
//        {
//            if (number >= 0)
//            {
//                Message message = timerHandler.obtainMessage();
//                if (number != 0)
//                {
//                    message.what = 1;
//                } else
//                {
//                    message.what = 2;
//                }
//                message.sendToTarget();
//                timerHandler.postDelayed(this, 1000);
//            }
//        }
//    };

//    Runnable myRunnable = new Runnable() {
//        @Override
//        public void run() {
//
//        }
//    };

    Runnable countRun = new Runnable() {
        @Override
        public void run() {
            Message message = mHandler.obtainMessage();
            if (number > 0) {
                message.what = 0;
            }else {
                message.what = 1;
            }
            message.sendToTarget();
            mHandler.postDelayed(this, 1000);
        }
    };

    public void startCount(){
        count_second.setEnabled(false);
//        mHandler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Message message = mHandler.obtainMessage();
//                if (number > 0) {
//                    message.what = 0;
//                }else {
//                    message.what = 1;
//                }
//                message.sendToTarget();
//                mHandler.postDelayed(this, 1000);
//            }
//        }, 1000);
        mHandler.postDelayed(countRun,1000);
    }

//    private boolean validateParams()
//    {
//        verifyCode = etVerify.getText().toString().trim();
//        password = etPassword.getText().toString().trim();
//
//        if (verifyCode.length() != 6 || !CommonUtils.isNumber(verifyCode))
//        {
//            etVerify.setError("请输入正确的6位验证码");
//            return false;
//        }
//
//        if (TextUtils.isEmpty(password) || password.length() < 6 || !CommonUtils.isNumberOrLetter(password))
//        {
//            etPassword.setError("请输入6位以上的数字和字母");
//            return false;
//        }
//
//        if (!isChecked)
//        {
//            Toast.makeText(this, "您需要同意《斑马金融服务与隐私协议》", Toast.LENGTH_SHORT).show();
//            return false;
//        }
//
//        return true;
//    }

}
