package com.wezebra.zebraking.ui.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.wezebra.zebraking.R;
import com.wezebra.zebraking.http.CusSuccessListener;
import com.wezebra.zebraking.http.TaskResponse;
import com.wezebra.zebraking.http.ZebraTask;
import com.wezebra.zebraking.util.ChangeBindListener;
import com.wezebra.zebraking.util.Constants;
import com.wezebra.zebraking.util.L;

import java.util.Map;
import java.util.TreeMap;

/**
 * Created by admin on 2015/7/27.
 */
public class NewBindPhoneFragment extends Fragment implements View.OnClickListener{

    private EditText mobile_edit,mobile_volicode;
    private TextView count_second,submit_security_Code;
    private int number = 60;
    public ChangeBindListener changeBindListener;

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {

            if (msg.what == 0){

                count_second.setText(number+"秒");
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
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        changeBindListener = (ChangeBindListener)activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_change_bind_mobile,null);
//        view.findViewById(R.id.);
        mobile_edit = (EditText) view.findViewById(R.id.mobile_edit);
        mobile_volicode = (EditText) view.findViewById(R.id.mobile_volicode);
        count_second = (TextView) view.findViewById(R.id.count_second);
        submit_security_Code = (TextView) view.findViewById(R.id.submit_security_Code);
        count_second.setOnClickListener(this);
        submit_security_Code.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        Map<String,String> params = new TreeMap<>();
        String newPhone = mobile_edit.getText().toString().trim();
        switch (v.getId()){
            case R.id.count_second:

                params.put("api", "getSmsCode");
                params.put("mobile", newPhone);
                new ZebraTask.Builder(getActivity(), params, new CusSuccessListener()
                {
                    @Override
                    public void onSuccess(TaskResponse response)
                    {
                        if (Constants.HTTP_REQUEST_SUCCESS == response.getCode()){
                            number=59;
//                            changeBindListener.countSecond();
                            startCount();
                        }
                    }
                }).build().execute();
            break;
            case R.id.submit_security_Code:
                params.put("api", "updateMobile");
                params.put("mobile",newPhone);
                params.put("smsCode",mobile_volicode.getText().toString().trim());
                params.put("type",2+"");

                new ZebraTask.Builder(getActivity(), params, new CusSuccessListener()
                {
                    @Override
                    public void onSuccess(TaskResponse response)
                    {
                        int code = response.getCode();
                        String desc = response.getDesc();
                        if (Constants.HTTP_REQUEST_SUCCESS == code){
                            L.i("code", "sssssssssssssss");
                            changeBindListener.turn2Next(1);
                        }else{
                            L.i("code", code+"");
//                            Toast.makeText(getActivity(),desc,Toast.LENGTH_SHORT).show();
                        }
                    }
                }).build().execute();
                break;
        }
    }

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
        mHandler.postDelayed(countRun, 1000);
    }
}
