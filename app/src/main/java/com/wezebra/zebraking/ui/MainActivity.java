package com.wezebra.zebraking.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.telephony.TelephonyManager;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.haibison.android.lockpattern.LockPatternActivity;
import com.umeng.message.PushAgent;
import com.umeng.update.UmengUpdateAgent;
import com.wezebra.zebraking.App;
import com.wezebra.zebraking.R;
import com.wezebra.zebraking.http.CusSuccessListener;
import com.wezebra.zebraking.http.HttpUtils;
import com.wezebra.zebraking.http.TaskResponse;
import com.wezebra.zebraking.http.ZebraTask;
import com.wezebra.zebraking.ui.fragment.AccountFragment;
import com.wezebra.zebraking.ui.fragment.InstallmentFragment;
import com.wezebra.zebraking.ui.myaccount.ApplyActivity;
import com.wezebra.zebraking.util.CommonUtils;
import com.wezebra.zebraking.util.Constants;
import com.wezebra.zebraking.util.L;
import com.wezebra.zebraking.util.PreferenceUtils;
import com.wezebra.zebraking.util.ThreadPoolUtil;
import com.wezebra.zebraking.util.ZEBRA_UMENG_ALIAS;
import com.wezebra.zebraking.widget.CustomViewPager;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


public class MainActivity extends BaseActivity
{
    public static final String TAG = "MainActivity";
    private static final int REQ_CREATE_PATTERN = 1;
    private static final int REQ_ENTER_PATTERN = 2;
    private RadioGroup mGroup;
    private CustomViewPager mPager;
    private InstallmentFragment installmentFragment;
    private AccountFragment accountFragment;
    private PushAgent mPushAgent;
    private String type;
    private String code;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        L.d(TAG, "onCreate");
        setContentView(R.layout.activity_main);

        // 显示手势密码
//        showPattern();

        type = getIntent().getStringExtra("type");
        code = getIntent().getStringExtra("code");

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        mGroup = (RadioGroup) findViewById(R.id.tabs);
        mGroup.setOnCheckedChangeListener(onCheckedChangeListener);
        mPager = (CustomViewPager) findViewById(R.id.view_pager);
        mPager.setAdapter(new MainPagerAdapter(getSupportFragmentManager()));
        mPager.setOnPageChangeListener(onPageChangeListener);
        mPager.setChildId(R.id.banner);

        initSharedPreferences();

        mPushAgent = PushAgent.getInstance(this);

        if (PreferenceUtils.getIsPushOpen())
        {
            if (!mPushAgent.isEnabled())
            {
                mPushAgent.onAppStart();
                mPushAgent.enable();
            }
        }


        ThreadPoolUtil.submit(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    mPushAgent.addAlias(PreferenceUtils.getUserId() + "", ZEBRA_UMENG_ALIAS.ZEBRA_USER);
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });

        if (PreferenceUtils.getDeviceUpdateTime() < System.currentTimeMillis())
        {
            // 上传设备信息
            updateDeviceData();
        }
    }

    @Override
    protected void onNewIntent(Intent intent)
    {
//        L.d(TAG, "onNewIntent");
        super.onNewIntent(intent);

        type = intent.getStringExtra("type");
        code = intent.getStringExtra("code");
//        boolean lockShowing = intent.getBooleanExtra("lock", false);
//        L.d(TAG, "type: " + type);
//        L.d(TAG, "code: " + code);
//        L.d(TAG, "lock: " + lockShowing);

//        if (!lockShowing)
//        {
            handlePushMessage();
//        } else
//        {
//            showPattern();
//        }
    }

    @Override
    protected void onResume()
    {
        super.onResume();
//        L.d(TAG, "onResume");
        UmengUpdateAgent.forceUpdate(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        PreferenceUtils.setIsLockResume(false);
        switch (requestCode)
        {
            case REQ_CREATE_PATTERN:
            {
                if (resultCode == Activity.RESULT_OK)
                {
                    char[] pattern = data.getCharArrayExtra(
                            LockPatternActivity.EXTRA_PATTERN);

                    String p = new String(pattern);
                    PreferenceUtils.setPattern(p);
                    if (PreferenceUtils.getDeviceUpdateTime() < System.currentTimeMillis())
                    {
                        // 上传设备信息
                        updateDeviceData();
                    }

                    handlePushMessage();
                } else if (resultCode == Activity.RESULT_CANCELED)
                {
                    L.d(TAG, "RESULT_CANCELED");
                    if (data != null)
                    {
                        finish();
                    }
                }

                break;
            }

            case REQ_ENTER_PATTERN:
            {
                Intent intent = null;
                switch (resultCode)
                {
                    case Activity.RESULT_OK:
                        L.d(TAG, "RESULT_OK");
                        if (PreferenceUtils.getDeviceUpdateTime() < System.currentTimeMillis())
                        {
                            // 上传设备信息
                            updateDeviceData();
                        }

                        handlePushMessage();

                        break;
                    case Activity.RESULT_CANCELED:
                        L.d(TAG, "RESULT_CANCELED");

                        if (data != null)
                        {
                            finish();
                        }
                        break;
                    case LockPatternActivity.RESULT_FAILED:
                        L.d(TAG, "RESULT_FAILED");

                        App.signOut(this,2);
                        finish();
                        Toast.makeText(this, "输入错误次数过多,请重新登录", Toast.LENGTH_SHORT).show();
                        break;
                    case LockPatternActivity.RESULT_FORGOT_PATTERN:
                        L.d(TAG, "RESULT_FORGOT_PATTERN");

                        App.signOut(this, 2);
                        finish();
                        Toast.makeText(this, "请重新登录", Toast.LENGTH_SHORT).show();

                        break;
                    case LockPatternActivity.RESULT_OTHER_ACCOUNT_PATTERN:
                        L.d(TAG, "RESULT_OTHER_PATTERN");

                        App.signOut(this,1);
                        finish();
                        break;
                }

                break;
            }
        }
    }

    private void handlePushMessage()
    {
        Intent intent;
        if (type != null)
        {
            if (App.INSTANCE.getActiveApplyData() == null)
            {
                getActiveApplay();
                return;
            }

            switch (type)
            {
                case "B":
                    intent = new Intent(this, ApplyActivity.class);
                    startActivity(intent);
                    break;

                case "D":
                case "E":
                case "F":
                case "G":
                    intent = new Intent(this, ApplyActivity.class);
                    intent.putExtra("code", Long.parseLong(code));
                    startActivity(intent);
                    break;
            }

        }
    }

    private void getActiveApplay()
    {
//        Map<String, String> params = new TreeMap<>();
//        params.put("api", Constants.API_ACTIVE_APPLY);
//
//        new ZebraTask.Builder(this, params, new CusSuccessListener()
//        {
//            @Override
//            public void onSuccess(TaskResponse response)
//            {
//                ActiveApplyData data = (ActiveApplyData) response.getData();
//                App.activeApplyData = data;
//                App.activeApplyData.setServerAccessed(2000);
//                handlePushMessage();
//            }
//        }, ActiveApplyData.class).build().execute();

        HttpUtils.getActiveApply(this, true, new CusSuccessListener()
        {
            @Override
            public void onSuccess(TaskResponse response)
            {
                handlePushMessage();
            }
        });
    }

    private void showPattern()
    {
        String pattern = PreferenceUtils.getPattern();

        if (pattern.length() > 0)
        {
            char[] savedPattern = pattern.toCharArray();

            Intent intent = new Intent(LockPatternActivity.ACTION_COMPARE_PATTERN, null,
                    this, LockPatternActivity.class);
            intent.putExtra(LockPatternActivity.EXTRA_PATTERN, savedPattern);
            startActivityForResult(intent, REQ_ENTER_PATTERN);
        } else
        {
            Intent intent = new Intent(LockPatternActivity.ACTION_CREATE_PATTERN, null,
                    this, LockPatternActivity.class);
            startActivityForResult(intent, REQ_CREATE_PATTERN);
        }

        PreferenceUtils.setIsLockResume(true);
    }

    public void setViewPagerPos(int pos)
    {
        mPager.setCurrentItem(pos, false);
    }

    private void initSharedPreferences()
    {
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.INFO_AUTH_STATE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();

        SharedPreferences entrance_flag = getSharedPreferences(Constants.ENTRANCE_FLAG, MODE_PRIVATE);
        SharedPreferences.Editor entrance = entrance_flag.edit();
        entrance.clear();
        entrance.commit();
    }

    private RadioGroup.OnCheckedChangeListener onCheckedChangeListener = new RadioGroup.OnCheckedChangeListener()
    {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId)
        {
            switch (checkedId)
            {
                case R.id.tab1:
                    mPager.setCurrentItem(0, false);
                    break;
                case R.id.tab2:
                    mPager.setCurrentItem(1, false);
                    break;
            }
        }
    };

    private ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener()
    {
        @Override
        public void onPageScrolled(int i, float v, int i2)
        {

        }

        @Override
        public void onPageSelected(int i)
        {
            switch (i)
            {
                case 0:
                    mGroup.check(R.id.tab1);
                    break;
                case 1:
                    mGroup.check(R.id.tab2);
            }
        }

        @Override
        public void onPageScrollStateChanged(int i)
        {
        }
    };

    class MainPagerAdapter extends FragmentPagerAdapter
    {
        public MainPagerAdapter(FragmentManager fm)
        {
            super(fm);
        }

        @Override
        public Fragment getItem(int i)
        {
            switch (i)
            {
                case 0:
                    installmentFragment = new InstallmentFragment();
                    return installmentFragment;
                case 1:
                    accountFragment = new AccountFragment();
                    return accountFragment;
                default:
                    return null;
            }
        }

        @Override
        public int getCount()
        {
            return 2;
        }
    }

    private void updateDeviceData()
    {
        new AsyncTask<Void, Void, Map<String, String>>()
        {
            @Override
            protected Map<String, String> doInBackground(Void... params)
            {
                Map<String, String> map = new TreeMap<>();

                TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

                int mWidth = getResources().getDisplayMetrics().widthPixels;
                int mHeight = getResources().getDisplayMetrics().heightPixels;
                String res = App.getAppStringMetaData("UMENG_CHANNEL");

                map.put("deviceId", tm.getDeviceId());
                map.put("mobileType", Build.MODEL);
                map.put("resolution", mWidth + "×" + mHeight);
                map.put("sysVersion", Build.VERSION.RELEASE);
                map.put("os", "Android");
                map.put("res", res);
                map.put("appInfo", getAppInfo());

                double[] position = CommonUtils.getCoarsePosition(MainActivity.this);
                if (position != null)
                {
                    map.put("lng", position[1] + "");
                    map.put("lat", position[0] + "");
                }

                return map;
            }

            @Override
            protected void onPostExecute(Map<String, String> stringStringMap)
            {
                deviceUpdate(stringStringMap);
            }
        }.execute();
    }

    /**
     * 设备信息保存 / 更新
     */
    public void deviceUpdate(final Map<String, String> deviceInfo)
    {
        Map<String, String> params = new TreeMap<>();
        params.put("api", Constants.API_DEVICE_UPDATE);
        params.putAll(deviceInfo);

        new ZebraTask.Builder(this, params, new CusSuccessListener()
        {
            @Override
            public void onSuccess(TaskResponse response)
            {
                // 15天以后才能再次上传
                PreferenceUtils.setDeviceUpdateTime(System.currentTimeMillis() + (15 * 24 * 60 * 60 * 1000));
            }
        }).setShowProgress(false).build().execute();
    }

    private String getAppInfo()
    {
        PackageManager manager = getPackageManager();
        List<ApplicationInfo> applicationInfos = manager.getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);
        Collections.sort(applicationInfos, new ApplicationInfo.DisplayNameComparator(manager));

        StringBuilder builder = new StringBuilder();

        for (ApplicationInfo app : applicationInfos)
        {
            //非系统程序
            if ((app.flags & ApplicationInfo.FLAG_SYSTEM) <= 0)
            {
                builder.append(app.loadLabel(manager));
                builder.append(",");
            }

            //本来是系统程序，被用户手动更新后，该系统程序也成为第三方应用程序了
            else if ((app.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0)
            {
                builder.append(app.loadLabel(manager));
                builder.append(",");
            }
        }

        if (builder.length() > 0)
        {
            builder.deleteCharAt(builder.length() - 1);
        }

        return builder.toString();
    }

}
