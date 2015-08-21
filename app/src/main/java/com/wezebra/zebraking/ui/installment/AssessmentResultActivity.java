package com.wezebra.zebraking.ui.installment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.TextView;

import com.wezebra.zebraking.R;
import com.wezebra.zebraking.http.HttpUtils;

public class AssessmentResultActivity extends ActionBarActivity implements View.OnClickListener {

    private TextView submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_result);

        initView();
    }

    private void initView() {

        submit = (TextView)findViewById(R.id.submit);
        submit.setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();

        //获取订单号
        getActiveApplay();
    }

    /**
     * 获取当前订单
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
//                } else
//                {
//                    HttpUtils.handleFailResponse(AssessmentResultActivity.this, response);
//                }
//            }
//        }, new DefaultErrorListener(this, dialog), params, ActiveApplyData.class);
//
//        dialog.show();
//        HttpUtils.addRequest(request, this);

        HttpUtils.getActiveApply(this,true);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_assessment_result, menu);
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
    public void onClick(View v) {
        Intent intent = new Intent();
        intent.setClass(this,SubmitInstallmentApplicationStepOneActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        return;
    }
}
