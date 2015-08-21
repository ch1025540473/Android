package com.wezebra.zebraking.ui.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.wezebra.zebraking.R;
import com.wezebra.zebraking.http.CusSuccessListener;
import com.wezebra.zebraking.http.TaskResponse;
import com.wezebra.zebraking.http.ZebraTask;
import com.wezebra.zebraking.http.data.CompanyEmailData;
import com.wezebra.zebraking.ui.installment.JobInfoChoiceActivity;
import com.wezebra.zebraking.ui.installment.SubmitInstallmentApplicationStepOneActivity;
import com.wezebra.zebraking.util.Constants;
import com.wezebra.zebraking.util.L;
import com.wezebra.zebraking.util.PreferenceUtils;

import java.util.Map;
import java.util.TreeMap;

/**
 * Created by admin on 2015/7/24.
 */
public class CompanyEmailCompleteFragment extends Fragment implements View.OnClickListener{

    TextView complete_voliy,email_agin,email_text;
    String emailaddr;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        emailaddr = getArguments().getString("emailaddr");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_company_email_complete, container,false);
        init(view);
        return view;
    }

    private void init(View view) {
        complete_voliy = (TextView) view.findViewById(R.id.complete_voliy);
        email_agin = (TextView) view.findViewById(R.id.email_agin);
        email_text = (TextView) view.findViewById(R.id.email_text);
        complete_voliy.setOnClickListener(this);
        email_agin.setOnClickListener(this);
        // 以下内容为改变邮箱颜色
        StringBuilder builder = new StringBuilder("斑马王国已向您的邮箱");
        String newemail =getCodeEmail(emailaddr);
        L.i("newemail",newemail);
        builder.append(newemail);
        builder.append("发送了验证邮件，请登录您的邮箱按邮件提示要求进行验证，并输入邮件中得安全码");
        SpannableStringBuilder ssb = new SpannableStringBuilder(builder);
        int emailcolor = Color.parseColor("#F4b50b");
        ssb.setSpan(new ForegroundColorSpan(emailcolor),10,10+newemail.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        email_text.setText(ssb);
    }

    public String getCodeEmail(String email){
        String[] emails = email.split("@");
        StringBuilder sb = new StringBuilder();
        sb.append(email.substring(0,3));
        sb.append("****");
        sb.append(emails[1]);
        return sb.toString();
    }
    @Override
    public void onClick(View v) {
        Map<String,String> params = new TreeMap<>();
        params.put("api","authCompanyEmailApi");
        switch (v.getId()){
            case R.id.complete_voliy:
                params.put("query",1+"");
                new ZebraTask.Builder(getActivity(), params, new CusSuccessListener()
                {
                    @Override
                    public void onSuccess(TaskResponse response)
                    {
                        L.i("email",response.toString());
                        CompanyEmailData data = (CompanyEmailData) response.getData();
                        int status = data.getStatus();
                        L.i("emailstatus",status+"");
                        if (status==Constants.PASSED || status==Constants.AUDITING){
                            startActivity(new Intent(getActivity(), SubmitInstallmentApplicationStepOneActivity.class));
                            PreferenceUtils.setFourStatus(1);
                        }else {
                            Toast.makeText(getActivity(),"验证失败",Toast.LENGTH_SHORT).show();
                        }
                    }
                }, CompanyEmailData.class).build().execute();
            break;
            case R.id.email_agin:
                params.put("again",1+"");
                new ZebraTask.Builder(getActivity(), params, new CusSuccessListener()
                {
                    @Override
                    public void onSuccess(TaskResponse response)
                    {
                        if (response.getCode()==Constants.HTTP_REQUEST_SUCCESS){
                            CompanyEmailData data = (CompanyEmailData) response.getData();
                            int status = data.getStatus();
                            L.i("emailstatus", status + "");
                            if (status!=Constants.WAIT_SUBMIT){
                                Toast.makeText(getActivity(),"请勿重复提交!",Toast.LENGTH_SHORT).show();
                                return;
                            }
                            Toast.makeText(getActivity(),"验证邮件发送成功",Toast.LENGTH_SHORT).show();
                        }
                    }
                },CompanyEmailData.class).build().execute();
                break;
        }
    }
}
