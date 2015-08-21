package com.wezebra.zebraking.http;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.wezebra.zebraking.App;
import com.wezebra.zebraking.http.data.ActiveApplyData;
import com.wezebra.zebraking.http.data.HFData;
import com.wezebra.zebraking.http.data.UserDetailData;
import com.wezebra.zebraking.ui.WebViewActivity;
import com.wezebra.zebraking.ui.myaccount.ChargeActivity;
import com.wezebra.zebraking.util.CommonUtils;
import com.wezebra.zebraking.util.Constants;
import com.wezebra.zebraking.util.L;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by 俊杰 on 2015/5/12.
 */
public class HttpUtils
{
    public static void addHFRequest(final Activity context,
                                    final Map<String, String> p,
                                    final String title)
    {
        addHFRequest(context, p, title, null);
    }

    public static void addHFRequest(final Activity context,
                                    final Map<String, String> p,
                                    final String title,
                                    final String fromActivity)
    {
        final Map<String, String> params = new TreeMap<>();
        params.putAll(p);
        params.put("channel", "android");

        double[] position = CommonUtils.getCoarsePosition(context);
        params.put("lat", position == null ? "" : position[0] + "");
        params.put("lng", position == null ? "" : position[1] + "");

        new ZebraTask.Builder(context, params, new CusSuccessListener()
        {
            @Override
            public void onSuccess(TaskResponse response)
            {
                HFData data = (HFData) response.getData();
                String redirectUrl = null;
                try
                {
                    redirectUrl = URLDecoder.decode(data.getRedirectUrl(), "UTF-8");
                } catch (UnsupportedEncodingException e)
                {
                    e.printStackTrace();
                }

                String url = Constants._HOST + redirectUrl;
                L.d("HF API --> " + params.get("api"), "redirectUrl: " + redirectUrl);
                L.d("HF API --> " + params.get("api"), "Url: " + url);

                // fromActivity 用于确定汇付操作成功后如何跳转
                WebViewActivity.redirectToWebViewActivity(context, url, title, fromActivity);
            }
        }, HFData.class).setCusFailListener(new CusFailListener()
        {
            @Override
            public void onFail(final TaskResponse response)
            {
                if (2026 == response.getCode())
                {
                    // 如果是取现金额不足，则只toast
                    if (params.get("api").equals(Constants.API_CASH))
                    {
                        ZebraTask.handleFailResponse(context, response);
                        return;
                    }

                    // 提示去充值
                    // 如果是支付首付款、还款、投标时余额不足，则直接以差额进行充值。否则，跳转到充值界面
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("账户余额不足，请先充值后再支付").setPositiveButton("立即充值",
                            new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    if (null != fromActivity &&
                                            (fromActivity.equals(WebViewActivity.FROM_PAY_FIRST)
                                                    || fromActivity.equals(WebViewActivity.FROM_REPAYMENT)
                                                    || fromActivity.equals(WebViewActivity.FROM_TENDER)))
                                    {
                                        Map<String, String> params = new TreeMap<>();
                                        params.put("api", Constants.API_CHARGE);
                                        // 使用服务端返回的差额进行充值
                                        params.put("transAmt", ((HFData) response.getData()).getChargeAmt());
                                        addHFRequest(context, params, "充值", fromActivity);
                                        return;
                                    }

                                    ChargeActivity.redirectToChargeActivity(context, fromActivity);

                                }
                            }).setNegativeButton(android.R.string.cancel, null);
                    builder.create().show();
                } else
                {
                    ZebraTask.handleFailResponse(context, response);
                }
            }
        }).build().execute();
    }

    public static void addGetUserDetailRequest(final Context context, final CusSuccessListener listener)
    {
        Map<String, String> params = new TreeMap<>();
        params.put("api", Constants.API_GET_USER_DETAIL);

        new ZebraTask.Builder(context, params, new CusSuccessListener()
        {
            @Override
            public void onSuccess(TaskResponse response)
            {
                UserDetailData data = (UserDetailData) response.getData();
                App.userDetailData = data;
                if (listener != null)
                {
                    listener.onSuccess(response);
                }
            }
        }, UserDetailData.class).build().execute();
    }

    public static void getActiveApply(Context context)
    {
        getActiveApply(context, false, null);
    }

    public static void getActiveApply(Context context, boolean showProgress)
    {
        getActiveApply(context, showProgress, null);
    }

    public static void getActiveApply(Context context, final CusSuccessListener listener)
    {
        getActiveApply(context, false, listener);
    }

    public static void getActiveApply(Context context, boolean showProgress, final CusSuccessListener listener)
    {
        Map<String, String> params = new TreeMap<>();
        params.put("api", Constants.API_ACTIVE_APPLY);

        new ZebraTask.Builder(context, params, new CusSuccessListener()
        {
            @Override
            public void onSuccess(TaskResponse response)
            {
                ActiveApplyData data = (ActiveApplyData) response.getData();
                App.INSTANCE.setActiveApplyData(data);

                if (listener != null)
                {
                    listener.onSuccess(response);
                }
            }
        }, ActiveApplyData.class).setShowProgress(showProgress).build().execute();
    }
}
