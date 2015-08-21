package com.wezebra.zebraking.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;

import com.android.volley.VolleyError;
import com.wezebra.zebraking.App;
import com.wezebra.zebraking.R;
import com.wezebra.zebraking.http.CusResponseListener;
import com.wezebra.zebraking.http.CusSuccessListener;
import com.wezebra.zebraking.http.DefaultErrorListener;
import com.wezebra.zebraking.http.TaskResponse;
import com.wezebra.zebraking.http.ZebraTask;
import com.wezebra.zebraking.http.data.ActiveApplyData;
import com.wezebra.zebraking.ui.login.PhoneNumberActivity;
import com.wezebra.zebraking.util.Constants;
import com.wezebra.zebraking.util.PreferenceUtils;

import java.util.Map;
import java.util.TreeMap;

public class LoadingActivity extends ActionBarActivity
{
    public static final int MIN_LOADING_DURATION = 1000;
    public static final int MAX_LOADING_DURATION = 3000;
    public static final String TAG = "LoadingActivity";
    private long startTime;

//    /**
//     * {@link #getUserDetail()} 是否返回
//     */
//    private boolean detailReturned = false;

    /**
     * {@link #getActiveApplay()} 是否返回
     */
    private boolean activeApplayReturned = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //setContentView(/*R.layout.activity_loading*/);
        setContentView(R.layout.activity_loading);
        startTime = System.currentTimeMillis();
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                if (hasLogIn())
                {
                    while (true)
                    {
                        // 请求都返回或 loading 时间大于3000ms时跳转
                        if (( activeApplayReturned) || (System.currentTimeMillis() - startTime) > MAX_LOADING_DURATION)
                        {
                            Intent intent = new Intent(LoadingActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                            break;
                        }
                    }
                } else
                {
                    Intent intent = new Intent(LoadingActivity.this, PhoneNumberActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, MIN_LOADING_DURATION);

        // 获取初始化信息
        if (hasLogIn())
        {
            getActiveApplay();
//            getUserDetail();
        }
    }

    /**
     * 检测登录状态
     *
     * preference 中没有保存 userId 即认为未登录
     *
     * @return false 未登录
     * true  已登录
     */
    private boolean hasLogIn()
    {
        if (PreferenceUtils.getUserId() == 0)
        {
            return false;
        }
        return true;
    }

    /**
     * 获取当前订单信息
     */
    private void getActiveApplay()
    {
        Map<String, String> params = new TreeMap<>();
        params.put("api", Constants.API_ACTIVE_APPLY);

        new ZebraTask.Builder(this, params, new CusSuccessListener()
        {
            @Override
            public void onSuccess(TaskResponse response)
            {
                ActiveApplyData data = (ActiveApplyData) response.getData();
                App.INSTANCE.setActiveApplyData(data);
            }
        }, ActiveApplyData.class).setCusErrorListener(new DefaultErrorListener.CusErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                activeApplayReturned = true;
            }
        }).setResponseListener(new CusResponseListener()
        {
            @Override
            public void onResponse(TaskResponse response)
            {
                activeApplayReturned = true;
            }
        }).setShowProgress(false).build().execute();
    }

    /**
     * 获取账户初始信息
     */
//    public void getUserDetail()
//    {
//        Map<String, String> params = new TreeMap<>();
//        params.put("api", Constants.API_GET_USER_DETAIL);
//
//        new ZebraTask.Builder(this, params, new CusSuccessListener()
//        {
//            @Override
//            public void onSuccess(TaskResponse response)
//            {
//                UserDetailData data = (UserDetailData) response.getData();
//                App.userDetailData = data;
//            }
//        }, UserDetailData.class).setCusErrorListener(new DefaultErrorListener.CusErrorListener()
//        {
//            @Override
//            public void onErrorResponse(VolleyError error)
//            {
//                detailReturned = true;
//            }
//        }).setResponseListener(new CusResponseListener()
//        {
//            @Override
//            public void onResponse(TaskResponse response)
//            {
//                detailReturned = true;
//            }
//        }).setShowProgress(false).build().execute();
//    }
}
