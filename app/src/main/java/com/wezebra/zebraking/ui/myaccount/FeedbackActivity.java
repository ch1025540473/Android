package com.wezebra.zebraking.ui.myaccount;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.wezebra.zebraking.R;
import com.wezebra.zebraking.http.CusFailListener;
import com.wezebra.zebraking.http.CusSuccessListener;
import com.wezebra.zebraking.http.DefaultErrorListener;
import com.wezebra.zebraking.http.TaskResponse;
import com.wezebra.zebraking.http.ZebraTask;
import com.wezebra.zebraking.ui.BaseActivity;
import com.wezebra.zebraking.util.Constants;

import java.util.Map;
import java.util.TreeMap;

public class FeedbackActivity extends BaseActivity implements View.OnClickListener
{
    public static final String TAG = "FeedbackActivity";
    private TextView submit;
    private EditText etFeedBack;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        initView();
    }

    private void initView()
    {
        submit = (TextView) findViewById(R.id.submit);
        submit.setOnClickListener(this);
        etFeedBack = (EditText) findViewById(R.id.et_feedback);
    }

    @Override
    public void onClick(View v)
    {
        String feed = etFeedBack.getText().toString().trim();

        if (feed.length() < 5)
        {
            Toast.makeText(this, "最少输入5个字哦", Toast.LENGTH_SHORT).show();
            return;
        }

        submit.setEnabled(false);
        feedBack(feed);
    }

    private void feedBack(String feed)
    {
        Map<String, String> params = new TreeMap<>();
        params.put("api", Constants.API_FEED_BACK);
        params.put("feedBack", feed);

        new ZebraTask.Builder(this, params, new CusSuccessListener()
        {
            @Override
            public void onSuccess(TaskResponse response)
            {
                Toast.makeText(FeedbackActivity.this, "亲，您的意见我们已收到，非常感谢", Toast.LENGTH_LONG).show();
                finish();
            }
        }).setCusFailListener(new CusFailListener()
        {
            @Override
            public void onFail(TaskResponse response)
            {
                submit.setEnabled(true);
                ZebraTask.handleFailResponse(FeedbackActivity.this, response);
            }
        }).setCusErrorListener(new DefaultErrorListener.CusErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                submit.setEnabled(true);
            }
        }).build().execute();
    }
}
