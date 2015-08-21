package com.wezebra.zebraking.ui.installment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import com.wezebra.zebraking.App;
import com.wezebra.zebraking.R;
import com.wezebra.zebraking.http.CusSuccessListener;
import com.wezebra.zebraking.http.HttpUtils;
import com.wezebra.zebraking.http.TaskResponse;
import com.wezebra.zebraking.ui.BaseActivity;
import com.wezebra.zebraking.ui.MainActivity;
import com.wezebra.zebraking.ui.myaccount.ApplyActivity;
import com.wezebra.zebraking.util.Constants;

public class SIAStepThreeResultActivity extends BaseActivity
{

    private TextView submit;

    private Handler handler;
    private Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_siastep_three_result);

        initView();
    }

    private void initView()
    {

        submit = (TextView) findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), PersonalProfileActivity.class);
                startActivity(intent);
            }
        });

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
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        handler.removeCallbacks(runnable);
        super.onBackPressed();
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_siastep_three_result, menu);
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
//                    if ((App.activeApplyData.getStatus() == Constants.ORDER_SUCCESS) || (App.activeApplyData.getStatus() == Constants.ORDER_WAITING_REPAY) || (App.activeApplyData.getStatus() == Constants.ORDER_CLOSED))
//                    {
//                        Intent intent = new Intent();
//                        intent.setClass(SIAStepThreeResultActivity.this, ApplyActivity.class);
//                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        startActivity(intent);
//                        SIAStepThreeResultActivity.this.finish();
//                    } else
//                    {
//                        handler.postDelayed(runnable, Constants.REFRESH_PULL_TIME);
//                    }
//                } else
//                {
//                    App.activeApplyData = new ActiveApplyData();
//                    HttpUtils.handleFailResponse(SIAStepThreeResultActivity.this, response);
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
                if ((status == Constants.ORDER_SUCCESS) || (status == Constants.ORDER_WAITING_REPAY) || (status == Constants.ORDER_CLOSED))
                {
                    Intent intent = new Intent();
                    intent.setClass(SIAStepThreeResultActivity.this, ApplyActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    SIAStepThreeResultActivity.this.finish();
                } else
                {
                    handler.postDelayed(runnable, Constants.REFRESH_PULL_TIME);
                }
            }
        });
    }

}
