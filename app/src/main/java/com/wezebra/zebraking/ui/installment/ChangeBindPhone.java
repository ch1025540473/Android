package com.wezebra.zebraking.ui.installment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.wezebra.zebraking.R;
import com.wezebra.zebraking.http.CusSuccessListener;
import com.wezebra.zebraking.http.TaskResponse;
import com.wezebra.zebraking.http.ZebraTask;
import com.wezebra.zebraking.http.data.FourAuthData;
import com.wezebra.zebraking.ui.BaseActivity;
import com.wezebra.zebraking.ui.fragment.BindPhoneComplete;
import com.wezebra.zebraking.ui.fragment.CompanyEmailCompleteFragment;
import com.wezebra.zebraking.ui.fragment.CompanyEmailInputFragment;
import com.wezebra.zebraking.ui.fragment.NewBindPhoneFragment;
import com.wezebra.zebraking.ui.fragment.OldBindPhoneFragment;
import com.wezebra.zebraking.util.ChangeBindListener;
import com.wezebra.zebraking.util.L;
import com.wezebra.zebraking.util.PreferenceUtils;
import com.wezebra.zebraking.util.ThreadPoolUtil;
import com.wezebra.zebraking.util.ZEBRA_UMENG_ALIAS;

import java.util.Map;
import java.util.TreeMap;

/**
 * Created by admin on 2015/7/27.
 */
public class ChangeBindPhone extends BaseActivity implements View.OnClickListener,ChangeBindListener{

    EditText mobile_edit,mobile_volicode;
    TextView count_second,submit_security_Code;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    Fragment oldBindPhone,newBindPhone,bindcomplete;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_bind_phone);
//        init();
        fragmentManager = getFragmentManager();
        changeFragment(0);
    }

//    private void init() {
//        mobile_edit = (EditText) findViewById(R.id.mobile_edit);
//        mobile_volicode = (EditText) findViewById(R.id.mobile_volicode);
//        count_second = (TextView) findViewById(R.id.count_second);
//        submit_security_Code = (TextView) findViewById(R.id.submit_security_Code);
//        count_second.setOnClickListener(this);
//        submit_security_Code.setOnClickListener(this);
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.count_second:
                Map<String,String> params = new TreeMap<>();
                params.put("api", "queryFourAuthStatusApi");
                params.put("orderCode", PreferenceUtils.getPayOrderCode()+"");
                new ZebraTask.Builder(this, params, new CusSuccessListener()
                {
                    @Override
                    public void onSuccess(TaskResponse response)
                    {
                        FourAuthData data = (FourAuthData) response.getData();
                        int jobStatus = data.getOnJobStatusInt();
                        L.i("data", data.toString());
                    }
                }, FourAuthData.class).build().execute();
            break;
        }
    }

    private void changeFragment(int index) {
        fragmentTransaction  = fragmentManager.beginTransaction();
        switch (index){
            case 0:
                if (oldBindPhone == null){
                    oldBindPhone = new OldBindPhoneFragment();
                }
                fragmentTransaction.replace(R.id.change_bind_phone_frame, oldBindPhone);
                fragmentTransaction.show(oldBindPhone);
                break;
            case 1:
                if (newBindPhone==null){
                    newBindPhone = new NewBindPhoneFragment();
                }
                fragmentTransaction.replace(R.id.change_bind_phone_frame, newBindPhone);
                fragmentTransaction.show(newBindPhone);
                break;
            case 2:
                if (bindcomplete==null){
                    bindcomplete = new BindPhoneComplete();
                }
                fragmentTransaction.replace(R.id.change_bind_phone_frame, bindcomplete);
                fragmentTransaction.show(bindcomplete);
                break;
        }
        fragmentTransaction.commit();
    }

    @Override
    public void countSecond() {
    }

    @Override
    public void turn2Next(int index) {
        if (index == 0){
            changeFragment(1);
        }else {
            changeFragment(2);
        }

    }


}
