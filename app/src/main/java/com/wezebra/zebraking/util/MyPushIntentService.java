package com.wezebra.zebraking.util;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.umeng.common.message.Log;
import com.umeng.message.UmengBaseIntentService;
import com.umeng.message.entity.UMessage;

import org.android.agoo.client.BaseConstants;
import org.json.JSONObject;

/**
 * Developer defined push intent service. 
 * Remember to call {@link com.umeng.message.PushAgent#setPushIntentServiceClass(Class)}.
 * @author lucas
 *
 */
public class MyPushIntentService extends UmengBaseIntentService
{
	private static final String TAG = MyPushIntentService.class.getName();

	@Override
	protected void onMessage(Context context, Intent intent) {
		super.onMessage(context, intent);
		try {
			String message = intent.getStringExtra(BaseConstants.MESSAGE_BODY);
			UMessage msg = new UMessage(new JSONObject(message));
			Log.d(TAG, "message=" + message);
			Log.d(TAG, "custom=" + msg.custom);

			Toast.makeText(context,"message: "+message,Toast.LENGTH_LONG).show();
			// code  to handle message here
			// ...
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
		}
	}
}
