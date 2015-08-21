package com.wezebra.zebraking.http;

import android.app.Dialog;
import android.content.Context;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.umeng.analytics.MobclickAgent;
import com.wezebra.zebraking.util.L;

/**
 * Created by 俊杰 on 2015/5/16.
 */
public class DefaultErrorListener implements Response.ErrorListener
{
    public static final String TAG = "DefaultErrorListener";
    private Dialog dialog;
    private Context context;
    private CusErrorListener listener;

    public DefaultErrorListener(Context context, Dialog dialog)
    {
        this(context, dialog, null);
    }

    public DefaultErrorListener(Context context, Dialog dialog, CusErrorListener listener)
    {
        if (dialog != null)
        {
            dialog.setCancelable(false);
        }
        this.dialog = dialog;
        this.context = context;
        this.listener = listener;
    }

    @Override
    public void onErrorResponse(VolleyError error)
    {
        L.e(TAG, error.getMessage());
        MobclickAgent.reportError(context, error);
        if (dialog != null && dialog.isShowing())
        {
            dialog.dismiss();
            dialog = null;
        }

        if (error instanceof TimeoutError)
        {
            Toast.makeText(context, "连接超时", Toast.LENGTH_SHORT).show();
            return;
        }

        NetworkResponse response = error.networkResponse;
        if (response != null)
        {
            int statusCode = response.statusCode;
            Toast.makeText(context, "statusCode: " + statusCode, Toast.LENGTH_SHORT).show();
        }

        if (listener != null)
        {
            listener.onErrorResponse(error);
        }
    }

    public interface CusErrorListener
    {
        public void onErrorResponse(VolleyError error);
    }
}
