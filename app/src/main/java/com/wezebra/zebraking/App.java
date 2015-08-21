package com.wezebra.zebraking;

import android.app.Application;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.support.v4.app.NotificationCompat;
import android.telephony.TelephonyManager;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;
import com.wezebra.zebraking.http.data.ActiveApplyData;
import com.wezebra.zebraking.http.data.UserDetailData;
import com.wezebra.zebraking.model.MobilePhoneAuth;
import com.wezebra.zebraking.model.Payee;
import com.wezebra.zebraking.model.PhotoUploadComponent;
import com.wezebra.zebraking.model.User;
import com.wezebra.zebraking.model.UserLogin;
import com.wezebra.zebraking.orm.DBManager;
import com.wezebra.zebraking.ui.MainActivity;
import com.wezebra.zebraking.ui.login.PasswordActivity;
import com.wezebra.zebraking.ui.login.PhoneNumberActivity;
import com.wezebra.zebraking.util.Base64Digest;
import com.wezebra.zebraking.util.FileUtils;
import com.wezebra.zebraking.util.L;
import com.wezebra.zebraking.util.PreferenceUtils;
import com.wezebra.zebraking.util.ThreadPoolUtil;
import com.wezebra.zebraking.util.WezebraImageLoader;
import com.wezebra.zebraking.util.ZEBRA_UMENG_ALIAS;

import org.acra.ACRA;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;
import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by 俊杰 on 2015/4/6.
 */
@ReportsCrashes(formUri = "https://collector.tracepot.com/97989078"
        , mode = ReportingInteractionMode.TOAST, resToastText = R.string.crash_toast)
public class App extends Application
{
    public static final String TAG = "App";
    public static App INSTANCE;
    public PhotoUploadComponent photoUploadComponent;
    public HashMap<String, List<NameValuePair>> postParameters;
    public static UserDetailData userDetailData;
    public static User user;
    public static Payee payee;
    private ActiveApplyData activeApplyData;
    public static MobilePhoneAuth mobilePhoneAuth;
    public static UserLogin userLogin;

    // 友盟推送
    private PushAgent mPushAgent;

    @Override
    public void onCreate()
    {
        super.onCreate();
        INSTANCE = this;
        ACRA.init(this);
        PreferenceUtils.create(this);
        DBManager.create(this);
        FileUtils.initExternalDir(true);

        //初始化Universal Image Loader
        DisplayImageOptions defaultDisplayImageOptions = new DisplayImageOptions.Builder().imageScaleType(ImageScaleType.EXACTLY_STRETCHED).build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .imageDownloader(new WezebraImageLoader(this))
                .defaultDisplayImageOptions(defaultDisplayImageOptions)
                .build();
        ImageLoader.getInstance().init(config);

        MobclickAgent.openActivityDurationTrack(false);

        // 更新友盟统计数据发送策略
        MobclickAgent.updateOnlineConfig(this);
        initUmengPush();

        L.i(TAG, "onCreate");
    }

    public static Context getAppContext()
    {
        return INSTANCE.getApplicationContext();
    }

    public ActiveApplyData getActiveApplyData()
    {
//        if (null == activeApplyData)
//        {
            activeApplyData = PreferenceUtils.getActiveAppayData();
//        }

        return activeApplyData;
    }

    public void setActiveApplyData(ActiveApplyData activeApplyData)
    {
        if (activeApplyData != null && activeApplyData.getOrderCode() > 0)
        {
            this.activeApplyData = activeApplyData;
            PreferenceUtils.setActiveApplyData(activeApplyData);
        }
    }

    public static String getAppStringMetaData(String name)
    {
        String value = "";
        try
        {
            ApplicationInfo appInfo = INSTANCE.getPackageManager().
                    getApplicationInfo(INSTANCE.getPackageName(), PackageManager.GET_META_DATA);

            value = appInfo.metaData.getString(name);
        } catch (PackageManager.NameNotFoundException e)
        {
            e.printStackTrace();
        }

        return value;
    }

    private void initUmengPush()
    {
        mPushAgent = PushAgent.getInstance(this);
        mPushAgent.setDebugMode(true);

        /**
         * 该Handler是在IntentService中被调用，故
         * 1. 如果需启动Activity，需添加Intent.FLAG_ACTIVITY_NEW_TASK
         * 2. IntentService里的onHandleIntent方法是并不处于主线程中，因此，如果需调用到主线程，需如下所示;
         * 	      或者可以直接启动Service
         * */
        UmengMessageHandler messageHandler = new UmengMessageHandler()
        {

            @Override
            public Notification getNotification(Context context, UMessage msg)
            {
                switch (msg.builder_id)
                {
                    case 0:
                        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
                        RemoteViews myNotificationView = new RemoteViews(context.getPackageName(), R.layout.notification_view);
                        myNotificationView.setTextViewText(R.id.notification_title, msg.title);
                        myNotificationView.setTextViewText(R.id.notification_text, msg.text);

                        long time = System.currentTimeMillis();
                        SimpleDateFormat format = new SimpleDateFormat("HH:mm");

                        myNotificationView.setTextViewText(R.id.notification_time, format.format(new Date(time)));
                        myNotificationView.setImageViewBitmap(R.id.notification_large_icon, getLargeIcon(context, msg));
//                        myNotificationView.setImageViewResource(R.id.notification_small_icon, getSmallIconId(context, msg));
                        builder.setContent(myNotificationView);
                        builder.setAutoCancel(true);
                        Notification mNotification = builder.build();
                        //由于Android v4包的bug，在2.3及以下系统，Builder创建出来的Notification，并没有设置RemoteView，故需要添加此代码
                        mNotification.contentView = myNotificationView;
                        return mNotification;
                    default:
                        //默认为0，若填写的builder_id并不存在，也使用默认。
                        return super.getNotification(context, msg);
                }
            }
        };
        mPushAgent.setMessageHandler(messageHandler);

        /**
         * 该Handler是在BroadcastReceiver中被调用，故
         * 如果需启动Activity，需添加Intent.FLAG_ACTIVITY_NEW_TASK
         * */
        UmengNotificationClickHandler notificationClickHandler = new UmengNotificationClickHandler()
        {
            @Override
            public void dealWithCustomAction(Context context, UMessage msg)
            {
                SharedPreferences preferences = context.getSharedPreferences("zebra", Context.MODE_MULTI_PROCESS);
                long uid = preferences.getLong("user_id", 0);
//                boolean isLockShowing = preferences.getBoolean("is_lock_resume", false);

                // 未登录的用户跳转到登录界面
                if (0 == uid)
                {
                    Intent intent = new Intent(context, PhoneNumberActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    context.startActivity(intent);

                    return;
                }

                String custom = Base64Digest.decode(msg.custom);

                try
                {
                    JSONObject object = new JSONObject(custom);
                    String type = object.optString("type");
                    long code = object.optLong("orderCode");

                    Intent intent = new Intent(context, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("type", type);
                    intent.putExtra("code", code + "");


//                    if (isLockShowing)
//                    {
//                        intent.putExtra("lock", isLockShowing);
//                    }
                    context.startActivity(intent);
                } catch (JSONException e)
                {
                    e.printStackTrace();
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        };
        mPushAgent.setNotificationClickHandler(notificationClickHandler);
    }

    public static String getDeviceId()
    {
        TelephonyManager tm = (TelephonyManager) INSTANCE.getSystemService(Context.TELEPHONY_SERVICE);
        String device_id = tm.getDeviceId();
        if (device_id == null)
        {
            device_id = "device-id grab failed";
        }
        return device_id;
    }

    public static void signOut(final Context context, int tag)
    {
        final long userId = PreferenceUtils.getUserId();
//        INSTANCE.activeApplyData = null;
        ThreadPoolUtil.submit(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    PushAgent.getInstance(context).removeAlias(userId + "", ZEBRA_UMENG_ALIAS.ZEBRA_USER);
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });


        String phoneNum = PreferenceUtils.getPhoneNum();
        // 清除用户数据
        PreferenceUtils.deleteAll();
        PreferenceUtils.setIsFirstOpen(false);
        App.userDetailData = null;

        Intent intent;
        if (tag == 1)
        {
            intent = new Intent(context, PhoneNumberActivity.class);
        } else
        {
            PreferenceUtils.setPhoneNum(phoneNum);
            intent = new Intent(context, PasswordActivity.class);
        }

        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

        context.startActivity(intent);
    }
}