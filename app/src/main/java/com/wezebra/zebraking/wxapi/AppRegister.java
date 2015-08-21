package com.wezebra.zebraking.wxapi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.wezebra.zebraking.util.Constants;
import com.wezebra.zebraking.util.L;

public class AppRegister extends BroadcastReceiver
{

    @Override
    public void onReceive(Context context, Intent intent)
    {
        final IWXAPI api = WXAPIFactory.createWXAPI(context, null);

        L.d("Register", "onReceive");
        // 将该app注册到微信
        api.registerApp(Constants.APP_ID);
    }
}
