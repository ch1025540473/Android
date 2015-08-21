package com.wezebra.zebraking.http;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.wezebra.zebraking.App;
import com.wezebra.zebraking.ui.login.PhoneNumberActivity;
import com.wezebra.zebraking.util.CommonUtils;
import com.wezebra.zebraking.util.PreferenceUtils;
import com.wezebra.zebraking.widget.LoadingBar;

import java.util.Map;

/**
 * Created by 俊杰 on 2015/6/24.
 */
public class ZebraTask
{
    private static RequestQueue mQueue;
    private Context context;
    private Map<String, String> params;
    private Class[] classes;
    private CusSuccessListener successListener;
    private CusFailListener failListener;
    private DefaultErrorListener.CusErrorListener cusErrorListener;
    private CusResponseListener responseListener;

    /**
     * 默认显示 ProgressDialog
     */
    private boolean showProgress = true;
    private ProgressDialog dialog;


    private ZebraTask(Builder builder)
    {
        context = builder.mContext;
        params = builder.mParams;
        classes = builder.classes;
        successListener = builder.mSuccessListener;
        failListener = builder.mFailListener;
        cusErrorListener = builder.mCusErrorListener;
        showProgress = builder.mShowProgress;
        responseListener = builder.mResponseListener;

        if (showProgress)
        {
            dialog = new LoadingBar(context);
        }
    }


    public static final class Builder
    {
        private Context mContext;
        private Map<String, String> mParams;
        private Class[] classes;
        private CusSuccessListener mSuccessListener;
        private CusFailListener mFailListener;
        private DefaultErrorListener.CusErrorListener mCusErrorListener;
        private CusResponseListener mResponseListener;
        private boolean mShowProgress = true;

        public Builder(@NonNull Context context,
                       @NonNull Map<String, String> params,
                       @NonNull CusSuccessListener successListener,
                       Class... clazz)
        {
            mContext = context;
            mParams = params;
            mSuccessListener = successListener;
            classes = clazz;
        }

        public Builder setCusFailListener(CusFailListener failListener)
        {
            this.mFailListener = failListener;
            return this;
        }

        public Builder setCusErrorListener(DefaultErrorListener.CusErrorListener errorListener)
        {
            this.mCusErrorListener = errorListener;
            return this;
        }

        public Builder setShowProgress(boolean showProgress)
        {
            this.mShowProgress = showProgress;
            return this;
        }

        public Builder setResponseListener(CusResponseListener responseListener)
        {
            this.mResponseListener = responseListener;
            return this;
        }

        public ZebraTask build()
        {
            return new ZebraTask(this);
        }
    }

    public RequestQueue getRequestQueue()
    {
        if (null == mQueue)
        {
            mQueue = Volley.newRequestQueue(App.getAppContext());
        }

        return mQueue;
    }

    public void execute()
    {
        Response.Listener<TaskResponse> listener = new Response.Listener<TaskResponse>()
        {
            @Override
            public void onResponse(TaskResponse response)
            {
                if (dialog != null && dialog.isShowing())
                {
                    dialog.dismiss();
                    dialog = null;
                }

                if (responseListener != null)
                {
                    responseListener.onResponse(response);
                }

                int code = response.getCode();

                if (2000 == code)
                {
                    successListener.onSuccess(response);
                } else if (failListener != null)
                {
                    failListener.onFail(response);
                } else
                {
                    handleFailResponse(context, response);
                }
            }
        };

        Response.ErrorListener errorListener = new DefaultErrorListener(context, dialog, cusErrorListener);

        if (showProgress)
        {
            dialog.show();
        }

        addRequest(new ZebraRequest(listener, errorListener, params, classes), context);
    }

    public void addRequest(Request request, Context context)
    {
        // 检测网络
        if (!CommonUtils.isOpenNetwork(context))
        {
            Toast.makeText(context, "当前网络未连接，请连接网络...", Toast.LENGTH_SHORT).show();
            request.deliverError(new VolleyError());
            return;
        }

        getRequestQueue().add(request);
    }

    public static void handleFailResponse(Context context, TaskResponse response)
    {
        int code = response.getCode();
        Log.i("CODE",code+"");
        if (1003 == code || 1005 == code || 2001 == code)
        {
            // 清除用户数据
            PreferenceUtils.deleteAll();
            PreferenceUtils.setIsFirstOpen(false);
            App.userDetailData = null;

            Intent intent = new Intent(context, PhoneNumberActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            context.startActivity(intent);
            Toast.makeText(context, response.getDesc(), Toast.LENGTH_SHORT).show();
        }
//        else if (2001 == code)
//        {
//            // 清除用户数据
//            PreferenceUtils.deleteAll();
//            PreferenceUtils.setIsFirstOpen(false);
//            App.userDetailData = null;
//
//            Intent intent = new Intent(context, PhoneNumberActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            context.startActivity(intent);
////            Toast.makeText(context, response.getDesc(), Toast.LENGTH_SHORT).show();
//        }
        else if (2002 == code || 2003 == code || 2004 == code || 2005 == code || 2006 == code || 2007 == code || 2008 == code || 2009 == code
                || 2014 == code || 2024 == code || 2025 == code || 2023 == code || 2026 == code || 2999 == code || 2013 == code)
        {
            Toast.makeText(context, response.getDesc(), Toast.LENGTH_SHORT).show();
        } else if (2030 == code)
        {

        } else if (2040 == code){
            Toast.makeText(context, "邮箱输入有误,请重新输入", Toast.LENGTH_SHORT).show();
        } else
        {
            Toast.makeText(context, "系统发生了些小错误，请稍后重试", Toast.LENGTH_SHORT).show();
        }
    }
}
