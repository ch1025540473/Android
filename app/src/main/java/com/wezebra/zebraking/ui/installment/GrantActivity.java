package com.wezebra.zebraking.ui.installment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.wezebra.zebraking.App;
import com.wezebra.zebraking.R;
import com.wezebra.zebraking.http.HttpUtils;
import com.wezebra.zebraking.ui.BaseActivity;
import com.wezebra.zebraking.ui.WebViewActivity;
import com.wezebra.zebraking.util.Constants;
import com.wezebra.zebraking.util.L;

import java.util.Map;
import java.util.TreeMap;

public class GrantActivity extends BaseActivity implements View.OnClickListener
{
    public static final String TAG = "GrantActivity";
    private TextView submit;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grant);
        submit = (TextView) findViewById(R.id.submit);
        submit.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        L.d(TAG, "onActivityResult");
        L.d(TAG, "resultCode: " + resultCode);
        if (resultCode == WebViewActivity.HF_SUCCESS)
        {
            startActivity(new Intent(this, SIAStepThreeResultActivity.class));
            finish();
        }
    }

    private void grant()
    {
        Map<String, String> params = new TreeMap<>();
        params.put("api", Constants.API_GRANT);
        params.put("orderCode", App.INSTANCE.getActiveApplyData().getOrderCode() + "");
        HttpUtils.addHFRequest(this, params, "授权", WebViewActivity.FROM_GRANT);
    }

    @Override
    public void onClick(View v)
    {
        grant();
    }
}
