package com.wezebra.zebraking.ui.installment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.j256.ormlite.field.types.IntegerObjectType;
import com.wezebra.zebraking.App;
import com.wezebra.zebraking.R;
import com.wezebra.zebraking.behavior.HttpAsyncTask;
import com.wezebra.zebraking.http.CusSuccessListener;
import com.wezebra.zebraking.http.HttpUtils;
import com.wezebra.zebraking.http.TaskResponse;
import com.wezebra.zebraking.http.ZebraTask;
import com.wezebra.zebraking.model.Bill;
import com.wezebra.zebraking.ui.BaseActivity;
import com.wezebra.zebraking.ui.WebViewActivity;
import com.wezebra.zebraking.ui.myaccount.ApplyActivity;
import com.wezebra.zebraking.ui.myaccount.RepaymentActivity;
import com.wezebra.zebraking.ui.myaccount.SelectPayWayActivity;
import com.wezebra.zebraking.util.CommonUtils;
import com.wezebra.zebraking.util.Constants;
import com.wezebra.zebraking.util.PreferenceUtils;
import com.wezebra.zebraking.wxapi.WxPayActivity;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
//立即支付页面，所有的信息完成，等待支付服务费
public class SubmitInstallmentApplicationStepThreeActivity extends BaseActivity implements View.OnClickListener
{
    public static final String TAG = "SubmitInstallmentApplicationStepThreeActivity";
    private TextView submit;
    private TextView tips, tips2, alipayTips, bankTips;
    private ScrollView scrollView;
    private LinearLayout offLine;

    public String serviceFee, advanceFunding;

    public static boolean shouldRefresh = false;

    private Handler handler;
    private Runnable runnable;
    //立即支付页面
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_installment_application_step_three);
        initView();

        List<NameValuePair> pullParameters = new ArrayList<>();
        pullParameters.add(new BasicNameValuePair("api", "getOrderFee"));
        pullParameters.add(new BasicNameValuePair("orderCode", App.INSTANCE.getActiveApplyData().getOrderCode() + ""));
        HttpAsyncTask asyncTask = HttpAsyncTask.getInstance(this, PreferenceUtils.getUserId() + "", pullParameters);
        asyncTask.pullOrderFee();

//        serviceFee = "100";
//        advanceFunding = "7000";
//        initView();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        if (shouldRefresh)
        {
            HttpUtils.addGetUserDetailRequest(this, new CusSuccessListener()
            {
                @Override
                public void onSuccess(TaskResponse response)
                {
                    shouldRefresh = false;
                }
            });
        }

        handler = new Handler();
        runnable = new Runnable()
        {
            @Override
            public void run()
            {

                getActiveApplay();

            }
        };
        handler.postDelayed(runnable, Constants.REFRESH_PULL_TIME);
    }

    @Override
    protected void onPause()
    {
        super.onPause();

        handler.removeCallbacks(runnable);
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();

        handler.removeCallbacks(runnable);
    }

    private void initView()
    {
        submit = (TextView) findViewById(R.id.submit);
        submit.setOnClickListener(this);

        tips = (TextView) findViewById(R.id.tips);
        tips2 = (TextView) findViewById(R.id.tips2);
        alipayTips = (TextView) findViewById(R.id.alipay_tips);
        bankTips = (TextView) findViewById(R.id.bank_tips);
        scrollView = (ScrollView) findViewById(R.id.scroll_view);
        scrollView.setVerticalScrollBarEnabled(false);
        offLine = (LinearLayout) findViewById(R.id.off_line);
    }

    public void refreshView()
    {

        boolean isOnLine;
//        isOnLine = getIntent().getBooleanExtra("isOnLine", false);
        if (App.INSTANCE.getActiveApplyData() == null)
        {
            return;
        }

        if (App.INSTANCE.getActiveApplyData().getIsOnLine().equals("1"))
        {
            isOnLine = true;
        } else
        {
            isOnLine = false;
        }
        if (isOnLine == true)
        {
            submit.setVisibility(View.VISIBLE);
            offLine.setVisibility(View.GONE);
        } else
        {
            submit.setVisibility(View.GONE);
            offLine.setVisibility(View.VISIBLE);
        }

        String s = toDBC("请支付服务费，支付成功后，斑马王国随即垫付￥" + advanceFunding + "到房东账户。请于7日之内完成支付，否则申请会自动关闭");
        SpannableStringBuilder builder = new SpannableStringBuilder(s);
        ForegroundColorSpan textLight1 = new ForegroundColorSpan(getResources().getColor(R.color.text_light));
        ForegroundColorSpan textPink = new ForegroundColorSpan(getResources().getColor(R.color.text_pink));
        ForegroundColorSpan textLight2 = new ForegroundColorSpan(getResources().getColor(R.color.text_light));
        builder.setSpan(textLight1, 0, 21, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        String t = advanceFunding;
        int length = t.length();
        Log.i("advanceFunding.length()", length + "");
        Log.i("TotalLength", s.length() + "");
        builder.setSpan(textPink, 21, 22 + length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.setSpan(textLight2, 22 + length, 22 + length + 26, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tips.setText(builder);

        String s2 = toDBC("您需要支付服务费：￥" + serviceFee);
        SpannableStringBuilder builder2 = new SpannableStringBuilder(s2);
        ForegroundColorSpan s2Light = new ForegroundColorSpan(getResources().getColor(R.color.text_light));
        ForegroundColorSpan s2Pink = new ForegroundColorSpan(getResources().getColor(R.color.text_pink));
        builder2.setSpan(s2Light, 0, 9, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        String t2 = serviceFee;
        int length2 = t2.length();
        Log.i("serviceFee.length()", length2 + "");
        Log.i("TotalLength", s2.length() + "");
        builder2.setSpan(s2Pink, 9, 10 + length2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tips2.setText(builder2);

        String s3 = toDBC("转账￥" + serviceFee + "元至以下支付宝账号，并备注借款人的姓名和手机号（例如：张三 15828641962）");
        SpannableStringBuilder builder3 = new SpannableStringBuilder(s3);
        ForegroundColorSpan s3Light1 = new ForegroundColorSpan(getResources().getColor(R.color.text_light));
        ForegroundColorSpan s3Pink = new ForegroundColorSpan(getResources().getColor(R.color.text_pink));
        ForegroundColorSpan s3Light2 = new ForegroundColorSpan(getResources().getColor(R.color.text_light));
        builder3.setSpan(s3Light1, 0, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        Log.i("serviceFee.length()", length2 + "");
        Log.i("TotalLength", s3.length() + "");
        builder3.setSpan(s3Pink, 2, 3 + length2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder3.setSpan(s3Light2, 3 + length2, 3 + length2 + 42, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        alipayTips.setText(builder3);

        String s4 = toDBC("转账￥" + serviceFee + "元至以下银行账号，并备注借款人的姓名和手机号（例如：张三 15828641962）");
        SpannableStringBuilder builder4 = new SpannableStringBuilder(s4);
        ForegroundColorSpan s4Light1 = new ForegroundColorSpan(getResources().getColor(R.color.text_light));
        ForegroundColorSpan s4Pink = new ForegroundColorSpan(getResources().getColor(R.color.text_pink));
        ForegroundColorSpan s4Light2 = new ForegroundColorSpan(getResources().getColor(R.color.text_light));
        builder4.setSpan(s4Light1, 0, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        Log.i("serviceFee.length()", length2 + "");
        Log.i("TotalLength", s4.length() + "");
        builder4.setSpan(s4Pink, 2, 3 + length2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder4.setSpan(s4Light2, 3 + length2, 3 + length2 + 41, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        bankTips.setText(builder4);

    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.submit:
//                Intent intent = new Intent();
////                intent.setClass(this,PersonalProfileActivity.class);
//                intent.setClass(this, SIAStepThreeResultActivity.class);
//                startActivity(intent);

//                if (null != App.userDetailData)
//                {
//                    if (TextUtils.isEmpty(App.userDetailData.getHfAccount().getUsrCustId()))
//                    {
//                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//                        builder.setMessage("为保证您的资金安全，支付前需要您先开通汇付账号哦").setPositiveButton("立即开通", new DialogInterface.OnClickListener()
//                        {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which)
//                            {
//                                openHFAccount();
//                            }
//                        }).setNegativeButton(android.R.string.cancel, null);
//                        builder.create().show();
//                        return;
//                    }
//
//                }
                payFirst();
                break;
        }
    }

    private void payFirst(){

//        Map<String, String> params = new TreeMap<>();
//        params.put("api", Constants.API_PAY_FIRST);
//        params.put("orderCode", App.INSTANCE.getActiveApplyData().getOrderCode() + "");
//        HttpUtils.addHFRequest(this, params, "首期款", WebViewActivity.FROM_PAY_FIRST);
        //这个也应该转调到选择支付方式页面
 //       Intent intent = new Intent(this, WxPayActivity.class);


//---------------------------------------------------先去请求上一次支付情况

        final  Intent intent2 = new Intent(this,SelectPayWayActivity.class);
        Bill bill=new Bill();
        bill.setType(WxPayActivity.PAY_FIRST);
        bill.setOrderCode(App.INSTANCE.getActiveApplyData().getOrderCode());
        bill.setOverCount(Double.parseDouble(serviceFee));
        intent2.putExtra("bill",bill);
        Map<String, String> params = new TreeMap<String,String>();

        params.put("api","lastPay");

       // Log.e("走到这个地方，说明没有错","爱上一个人我们都没有错");

        new ZebraTask.Builder(this, params, new CusSuccessListener()
        {
            @Override
            public void onSuccess(TaskResponse response)
            {
                LinkedHashMap<String ,Object> lastPay= (LinkedHashMap<String,Object>)response.getData();

                if(!TextUtils.isEmpty((String) lastPay.get("payBy"))){

                    //支付方式
                    intent2.putExtra("payBy",(String)lastPay.get("payBy"));
                    //是否使用过连连支付
                    intent2.putExtra("hasUsedll",(boolean)lastPay.get("hasUsedll"));

                    startActivity(intent2);
                }else {
                    //发生错误的时候会走到这里
                    throw new  RuntimeException(RepaymentActivity.class.toString()+"157 line error");
                }
            }
        }).setShowProgress(false).build().execute();
       // startActivity(intent2);
    }

    private void openHFAccount()
    {
        Map<String, String> params = new TreeMap<>();
        params.put("api", Constants.API_OPEN_ACCOUNT);
        HttpUtils.addHFRequest(this, params, "开户", WebViewActivity.FROM_PAY_FIRST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
//        if (resultCode == WebViewActivity.HF_SUCCESS)
//        {
//            startActivity(new Intent(this, GrantActivity.class));
//            finish();
//        }

        if (resultCode == WxPayActivity.FAIL_RESULT_CODE)
        {
            Intent intent = new Intent(this, ApplyActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }

    }

    public static String toDBC(String input)
    {
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++)
        {
            if (c[i] == 12288)
            {
                c[i] = (char) 32;
                continue;
            }
            if (c[i] > 65280 && c[i] < 65375)
                c[i] = (char) (c[i] - 65248);
        }
        return new String(c);
    }

    /**
     * 获取当前订单信息
     */
    private void getActiveApplay()
    {
//        Map<String, String> params = new TreeMap<>();
//        params.put("api", Constants.API_ACTIVE_APPLY);
//
//        final Dialog dialog = new LoadingBar(this);
//        ZebraRequest request = new ZebraRequest(new Response.Listener<TaskResponse>()
//        {
//            @Override
//            public void onResponse(TaskResponse response)
//            {
//                if (dialog != null && dialog.isShowing())
//                {
//                    dialog.dismiss();
//                }
//                int code = response.getCode();
//
//                if (code == 2000)
//                {
//                    ActiveApplyData data = (ActiveApplyData) response.getData();
//                    App.activeApplyData = data;
//                    App.activeApplyData.setServerAccessed(2000);
//
//                    if ( (App.activeApplyData.getStatus() == Constants.ORDER_CLOSED) ) {
//                        Intent intent = new Intent();
//                        intent.setClass(SubmitInstallmentApplicationStepThreeActivity.this,ApplyActivity.class);
//                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        startActivity(intent);
//                        SubmitInstallmentApplicationStepThreeActivity.this.finish();
//                    } else if ( (App.activeApplyData.getStatus() == Constants.ORDER_AUDITING_LOAN) ) {
//                        Intent intent = new Intent();
//                        intent.setClass(SubmitInstallmentApplicationStepThreeActivity.this,SIAStepThreeResultActivity.class);
//                        startActivity(intent);
//                        SubmitInstallmentApplicationStepThreeActivity.this.finish();
//                    } else {
//                        handler.postDelayed(runnable,Constants.REFRESH_PULL_TIME);
//                    }
//                } else
//                {
//                    App.activeApplyData = new ActiveApplyData();
//                    HttpUtils.handleFailResponse(SubmitInstallmentApplicationStepThreeActivity.this, response);
//                }
//            }
//        }, new DefaultErrorListener(this, dialog), params, ActiveApplyData.class);
//
//        //dialog.show();
//        HttpUtils.addRequest(request, this);

        HttpUtils.getActiveApply(this, new CusSuccessListener()
        {
            @Override
            public void onSuccess(TaskResponse response)
            {
                int status = App.INSTANCE.getActiveApplyData().getStatus();
                if ((status == Constants.ORDER_CLOSED))
                {
                    Intent intent = new Intent();
                    intent.setClass(SubmitInstallmentApplicationStepThreeActivity.this, ApplyActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    SubmitInstallmentApplicationStepThreeActivity.this.finish();
                } else if ((status == Constants.ORDER_AUDITING_LOAN))
                {
                    Intent intent = new Intent();
                    intent.setClass(SubmitInstallmentApplicationStepThreeActivity.this, SIAStepThreeResultActivity.class);
                    startActivity(intent);
                    SubmitInstallmentApplicationStepThreeActivity.this.finish();
                } else
                {
                    handler.postDelayed(runnable, Constants.REFRESH_PULL_TIME);
                }
            }
        });
    }

}
