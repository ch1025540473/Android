package com.wezebra.zebraking.ui.installment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.wezebra.zebraking.R;
import com.wezebra.zebraking.http.CusSuccessListener;
import com.wezebra.zebraking.http.TaskResponse;
import com.wezebra.zebraking.http.ZebraTask;
import com.wezebra.zebraking.http.data.CompanyEmailData;
import com.wezebra.zebraking.http.data.FourAuthData;
import com.wezebra.zebraking.ui.BaseActivity;
import com.wezebra.zebraking.ui.fragment.CompanyEmailCompleteFragment;
import com.wezebra.zebraking.ui.fragment.CompanyEmailInputFragment;
import com.wezebra.zebraking.util.Constants;
import com.wezebra.zebraking.util.L;
import com.wezebra.zebraking.util.LocationUtil;

import org.apache.http.message.BasicNameValuePair;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by admin on 2015/7/24.
 */
public class CompanyEmailAuth extends BaseActivity implements View.OnClickListener,CompanyEmailInputFragment.MyLister{

    private FragmentManager fragmentManager;
    private Fragment inputFragment,completeFragment;
    private FragmentTransaction fragmentTransaction;
    private static final String TAG = "CompanyEmailAuth";
    public String emailaddr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_auth);
        fragmentManager = getFragmentManager();
        changeFragment(0); // 显示输入邮箱的fragment
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
//            case R.id.submit:
//                String emailAddrstr = emailAddr.getText().toString();
//                if (!checkInput(emailAddrstr)){
//                    Toast.makeText(CompanyEmailAuth.this, "请输入正确的邮箱格式!", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                ArrayList<BasicNameValuePair> lacation= LocationUtil.getLocation(this);
//                lacation.get(0).getValue();
//                lacation.get(1).getValue();
//                Map<String,String> params = new TreeMap<>();
//                params.put("api","authCompanyEmailApi");
//                params.put("email",emailAddrstr);
//                params.put("channel","android");
//                params.put("lng",lacation.get(0).getValue());
//                params.put("lat",lacation.get(1).getValue());
//                new ZebraTask.Builder(this, params, new CusSuccessListener()
//                {
//                    @Override
//                    public void onSuccess(TaskResponse response)
//                    {
//                        L.i(TAG, response.getDesc());
//                        if (response.getCode()== Constants.HTTP_REQUEST_SUCCESS){
//                            FragmentTransaction fragmentTransaction =fragmentManager.beginTransaction();
//                            if (complete==null){
//                                complete = new CompanyEmailCompleteFragment();
//                                fragmentTransaction.replace(R.id.email_input, complete);
//                            }
//                            fragmentTransaction.show(complete);
//                            fragmentTransaction.commit();
//                        }
//                    }
//                }, null).build().execute();
//                break;
//        }
    }
    }

    private void changeFragment(int index) {
        fragmentTransaction  = fragmentManager.beginTransaction();
        switch (index){
            case 0:
                if (inputFragment == null){
                    inputFragment = new CompanyEmailInputFragment();
                }
                fragmentTransaction.add(R.id.email_framelayout,inputFragment);
                fragmentTransaction.show(inputFragment);
                break;
            case 1:
                fragmentTransaction = fragmentManager.beginTransaction();
                L.i(TAG, "completevoliy");
                if (completeFragment==null){
                    completeFragment = new CompanyEmailCompleteFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("emailaddr",emailaddr);
                    completeFragment.setArguments(bundle);
                }
                fragmentTransaction.replace(R.id.email_framelayout, completeFragment);
                fragmentTransaction.show(completeFragment);
                break;
        }
        fragmentTransaction.commit();
    }

    @Override
    public void turnCompletEmail() {

        changeFragment(1);
    }

    @Override
    public void setEmailAddr(String addr) {
        emailaddr = addr;
    }


}
