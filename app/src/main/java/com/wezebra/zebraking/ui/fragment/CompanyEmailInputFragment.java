package com.wezebra.zebraking.ui.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.wezebra.zebraking.R;
import com.wezebra.zebraking.http.CusSuccessListener;
import com.wezebra.zebraking.http.TaskResponse;
import com.wezebra.zebraking.http.ZebraTask;
import com.wezebra.zebraking.util.Constants;
import com.wezebra.zebraking.util.LocationUtil;

import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by admin on 2015/7/25.
 */
public class CompanyEmailInputFragment extends Fragment implements View.OnClickListener{

    private TextView submit_button;
    private EditText emailAddr;
    Fragment complete;
    MyLister myLister;

    private static final String TAG = "CompanyEmailAuth";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_company_email,null);
        submit_button = (TextView)view.findViewById(R.id.submit);
        submit_button.setOnClickListener(this);
        emailAddr = (EditText)view.findViewById(R.id.email_addr);
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            myLister = (MyLister) activity;
        }catch (ClassCastException e){
            new ClassCastException("must implent MyLister");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.submit:
                final String emailAddrstr = emailAddr.getText().toString();
                if (!checkInput(emailAddrstr)){
                    Toast.makeText(getActivity(), "请输入正确的邮箱!", Toast.LENGTH_SHORT).show();
                    return;
                }
                ArrayList<BasicNameValuePair> lacation= LocationUtil.getLocation((ActionBarActivity)getActivity());
                lacation.get(0).getValue();
                lacation.get(1).getValue();
                Map<String,String> params = new TreeMap<>();
                params.put("api","authCompanyEmailApi");
                params.put("email",emailAddrstr);
                params.put("channel","android");
                params.put("lng",lacation.get(0).getValue());
                params.put("lat",lacation.get(1).getValue());
                new ZebraTask.Builder(getActivity(), params, new CusSuccessListener()
                {
                    @Override
                    public void onSuccess(TaskResponse response)
                    {
                        if (response.getCode()== Constants.HTTP_REQUEST_ERROR){
                            Toast.makeText(getActivity(), "邮箱有误,请输入正确的邮箱!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        myLister.setEmailAddr(emailAddrstr);
                        myLister.turnCompletEmail();
                    }
                }).build().execute();
                break;
        }
    }

    /**
     * 邮箱验证
     * @param emailAddrstr
     * @return
     */
    private boolean checkInput(String emailAddrstr) {
        Pattern pattern = Pattern.compile("^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");
        Matcher matcher = pattern.matcher(emailAddrstr);
        if (matcher.matches()){
            return true;
        }
        return false;
    }

    /**
     * 回调接口，用于与activity进行通信
     */
    public interface MyLister{
        public void turnCompletEmail();
        public void setEmailAddr(String addr);
    }
}
