package com.wezebra.zebraking.ui.installment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wezebra.zebraking.App;
import com.wezebra.zebraking.R;
import com.wezebra.zebraking.behavior.HttpAsyncTask;
import com.wezebra.zebraking.http.CusSuccessListener;
import com.wezebra.zebraking.http.HttpUtils;
import com.wezebra.zebraking.http.TaskResponse;
import com.wezebra.zebraking.ui.BaseActivity;
import com.wezebra.zebraking.ui.myaccount.ApplyActivity;
import com.wezebra.zebraking.util.Constants;
import com.wezebra.zebraking.util.PreferenceUtils;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class SIAStepOneResultActivity extends BaseActivity implements View.OnClickListener
{

    private TextView submit;
    public LinearLayout authOne, authTwo;
    public ImageView authOneImg, authTwoImg;

    private Handler handler;
    private Runnable runnable;

    private SharedPreferences entrance_flag;
    private SharedPreferences.Editor entrance_flag_editor;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_siastep_one_result);

        initView();
    }

    private void initView()
    {

        submit = (TextView) findViewById(R.id.submit);
        submit.setOnClickListener(this);
        authOne = (LinearLayout) findViewById(R.id.auth_one);
        authTwo = (LinearLayout) findViewById(R.id.auth_two);
        authOneImg = (ImageView) findViewById(R.id.auth_one_img);
        authTwoImg = (ImageView) findViewById(R.id.auth_two_img);

    }

    @Override
    protected void onResume()
    {
        super.onResume();

        handler = new Handler();
        runnable = new Runnable()
        {
            @Override
            public void run()
            {

                getActiveApplay();

            }
        };
        handler.postDelayed(runnable, Constants.REFRESH_PULL_TIME);

        //拉取认证信息
        List<NameValuePair> pullParameters = new ArrayList<>();
        pullParameters.add(new BasicNameValuePair("api", "getUserLogin"));
        HttpAsyncTask asyncTask = HttpAsyncTask.getInstance(this, PreferenceUtils.getUserId() + "", pullParameters);
        asyncTask.pullUserLoginSIA();

    }

    @Override
    protected void onPause()
    {
        super.onPause();

        handler.removeCallbacks(runnable);
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();

        handler.removeCallbacks(runnable);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_siastep_one_result, menu);
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
    public void onClick(View v)
    {

        entrance_flag = getSharedPreferences(Constants.ENTRANCE_FLAG, MODE_PRIVATE);
        entrance_flag_editor = entrance_flag.edit();

        switch (v.getId())
        {
            case R.id.submit:
                Intent intent = new Intent();
                intent.setClass(this, PersonalProfileActivity.class);
                startActivity(intent);
                break;
            case R.id.auth_one:

                if (App.userLogin != null)
                {

                    entrance_flag_editor.putInt(Constants.ENTRANCE_FLAG, Constants.IS_FROM_SIA_ONE);
                    entrance_flag_editor.commit();
                    intent = new Intent();

                    switch (App.userLogin.getIncomeStatusInt())
                    {
                        case Constants.WAIT_SUBMIT:
                        case Constants.UNPASS:
                            intent.setClass(this, IncomeAuthActivity.class);
                            startActivity(intent);
                            break;
                        case Constants.AUDITING:
                            Toast.makeText(this, getString(R.string.status_auditing_tips), Toast.LENGTH_LONG).show();
                            return;
                        case Constants.PASSED:
                            intent.setClass(this, ReFillActivity.class);
                            intent.putExtra("title", getString(R.string.title_activity_siastep_one_result));
                            intent.putExtra("redirect", Constants.INCOME_INFO);
                            startActivity(intent);
                            break;
                    }
                }
                break;
            case R.id.auth_two:

                if (App.userLogin != null)
                {

                    entrance_flag_editor.putInt(Constants.ENTRANCE_FLAG, Constants.IS_FROM_SIA_ONE);
                    entrance_flag_editor.commit();
                    intent = new Intent();

                    switch (App.userLogin.getMobileStatusInt())
                    {
                        case Constants.WAIT_SUBMIT:
                        case Constants.UNPASS:
                            intent.setClass(this, PhoneAuthStepOneActivity.class);
                            startActivity(intent);
                            break;
                        case Constants.AUDITING:
                            Toast.makeText(this, getString(R.string.status_auditing_tips), Toast.LENGTH_LONG).show();
                            return;
                        case Constants.PASSED:
                            intent.setClass(this, ReFillActivity.class);
                            intent.putExtra("title", getString(R.string.title_activity_phone_auth));
                            intent.putExtra("redirect", Constants.PHONE_INFO);
                            startActivity(intent);
                            break;
                    }
                }
                break;
        }

    }

    /**
     * 获取当前订单信息
     */
    private void getActiveApplay()
    {
//        Map<String, String> params = new TreeMap<>();
//        params.put("api", Constants.API_ACTIVE_APPLY);
//
//        final Dialog dialog = new LoadingBar(this);
//        ZebraRequest request = new ZebraRequest(new Response.Listener<TaskResponse>()
//        {
//            @Override
//            public void onResponse(TaskResponse response)
//            {
//                if (dialog != null && dialog.isShowing())
//                {
//                    dialog.dismiss();
//                }
//                int code = response.getCode();
//
//                if (code == 2000)
//                {
//                    ActiveApplyData data = (ActiveApplyData) response.getData();
//                    App.activeApplyData = data;
//                    App.activeApplyData.setServerAccessed(2000);
//
//                    if (App.activeApplyData.getStatus() == Constants.ORDER_WAITING_ADDED)
//                    {
//                        Intent intent = new Intent();
//                        intent.setClass(SIAStepOneResultActivity.this, SubmitInstallmentApplicationStepTwoActivity.class);
//                        startActivity(intent);
//                        SIAStepOneResultActivity.this.finish();
//                    } else if (App.activeApplyData.getStatus() == Constants.ORDER_WAITING_MODIFY)
//                    {
//                        Intent intent = new Intent();
//                        intent.setClass(SIAStepOneResultActivity.this, SubmitInstallmentApplicationStepOneActivity.class);
//                        startActivity(intent);
//                        SIAStepOneResultActivity.this.finish();
//                    } else if (App.activeApplyData.getStatus() == Constants.ORDER_AUDITING_BASIC)
//                    {
//                        handler.postDelayed(runnable, Constants.REFRESH_PULL_TIME);
//                    } else
//                    {
//                        Intent intent = new Intent();
//                        intent.setClass(SIAStepOneResultActivity.this, ApplyActivity.class);
//                        startActivity(intent);
//                        SIAStepOneResultActivity.this.finish();
//                    }
//                } else
//                {
//                    App.activeApplyData = new ActiveApplyData();
//                    HttpUtils.handleFailResponse(SIAStepOneResultActivity.this, response);
//                }
//            }
//        }, new DefaultErrorListener(this, dialog), params, ActiveApplyData.class);
//
//        //dialog.show();
//        HttpUtils.addRequest(request, this);

        HttpUtils.getActiveApply(this, new CusSuccessListener()
        {
            @Override
            public void onSuccess(TaskResponse response)
            {
                int status = App.INSTANCE.getActiveApplyData().getStatus();

                if (status == Constants.ORDER_WAITING_ADDED)
                {
                    Intent intent = new Intent();
                    intent.setClass(SIAStepOneResultActivity.this, SubmitInstallmentApplicationStepTwoActivity.class);
                    startActivity(intent);
                    SIAStepOneResultActivity.this.finish();
                } else if (status == Constants.ORDER_WAITING_MODIFY)
                {
                    Intent intent = new Intent();
                    intent.setClass(SIAStepOneResultActivity.this, SubmitInstallmentApplicationStepOneActivity.class);
                    startActivity(intent);
                    SIAStepOneResultActivity.this.finish();
                } else if (status == Constants.ORDER_AUDITING_BASIC)
                {
                    handler.postDelayed(runnable, Constants.REFRESH_PULL_TIME);
                } else
                {
                    Intent intent = new Intent();
                    intent.setClass(SIAStepOneResultActivity.this, ApplyActivity.class);
                    startActivity(intent);
                    SIAStepOneResultActivity.this.finish();
                }
            }
        });
    }
}
