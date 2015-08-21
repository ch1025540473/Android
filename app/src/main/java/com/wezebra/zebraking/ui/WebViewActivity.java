package com.wezebra.zebraking.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.wezebra.zebraking.R;
import com.wezebra.zebraking.ui.fragment.AccountFragment;
import com.wezebra.zebraking.ui.installment.SubmitInstallmentApplicationStepThreeActivity;
import com.wezebra.zebraking.ui.myaccount.AssetActivity;
import com.wezebra.zebraking.ui.myaccount.PersonalInfoActivity;
import com.wezebra.zebraking.ui.myaccount.RepaymentActivity;
import com.wezebra.zebraking.ui.myaccount.TenderActivity;
import com.wezebra.zebraking.util.L;

public class WebViewActivity extends BaseActivity
{
    public static final String TAG = "WebViewActivity";
    private WebView webView;
    private ProgressBar progressBar;
    public static final String FROM_ACTIVITY = "from_activity";
    public static final String TITLE = "title";
    public static final String FROM_PERSONAL = "from_personal";
    public static final String FROM_ASSET = "from_asset";
    public static final String FROM_REPAYMENT = "from_repayment";
    public static final String FROM_PAY_FIRST = "from_PAY_FIRST";
    public static final String FROM_TENDER = "from_tender";
    public static final String FROM_GRANT = "from_grant";
    public static final int HF_SUCCESS = 200;

    private int type;
    private String title;
    public boolean callBackReturned = false;
    private String fromActivity;

    public static void redirectToWebViewActivity(Context fromActivity, String url, String title)
    {
        Intent intent = new Intent(fromActivity, WebViewActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("url", url);
        fromActivity.startActivity(intent);
    }

    public static void redirectToWebViewActivity(Activity fromActivity, String url, String title, String fromActivityName)
    {
        Intent intent = new Intent(fromActivity, WebViewActivity.class);
        intent.putExtra(FROM_ACTIVITY, fromActivityName);
        intent.putExtra("url", url);
        intent.putExtra("title", title);

        if (fromActivityName != null && (fromActivityName.equals(FROM_PAY_FIRST) || fromActivityName.equals(FROM_GRANT)))
        {
            fromActivity.startActivityForResult(intent, 100);
        } else
        {
            fromActivity.startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        title = getIntent().getStringExtra("title");
        fromActivity = getIntent().getStringExtra(FROM_ACTIVITY);

        if (!TextUtils.isEmpty(title))
        {
            getSupportActionBar().setTitle(title);
        }

        progressBar = (ProgressBar) findViewById(R.id.frame_loading);
        webView = (WebView) findViewById(R.id.web_view);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new WebViewInterface(this), "Android");

        webView.setWebChromeClient(new WebChromeClient()
        {
            @Override
            public void onProgressChanged(WebView view, int newProgress)
            {
                if (newProgress == 100)
                {
                    progressBar.setVisibility(View.GONE);
                }
                super.onProgressChanged(view, newProgress);
            }
        });

        webView.setWebViewClient(new WebViewClient()
        {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url)
            {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon)
            {
                if (url.indexOf("callback.sync") >= 0 || url.indexOf("hfresult") >= 0)
                {
                    callBackReturned = true;
                    getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                } else
                {
                    callBackReturned = false;
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                }

                L.d(TAG, "callBack showing: " + callBackReturned + ", current url: " + url);

                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error)
            {
                handler.proceed();
            }
        });
        webView.getSettings().setJavaScriptEnabled(true);

        String url = getIntent().getStringExtra("url");

        L.d(TAG, "url: " + url);
        if (!TextUtils.isEmpty(url))
        {
            webView.loadUrl(url);
        }
    }

    private void finishActivity()
    {
        Intent intent = new Intent();
        Class clazz;
        switch (fromActivity)
        {
            case FROM_PERSONAL:
                PersonalInfoActivity.shouldRefresh = true;
                clazz = PersonalInfoActivity.class;
                break;
            case FROM_ASSET:
                AssetActivity.shouldRefresh = true;
                clazz = AssetActivity.class;
                break;
            case FROM_PAY_FIRST:
                clazz = SubmitInstallmentApplicationStepThreeActivity.class;
                break;
            case FROM_REPAYMENT:
                clazz = RepaymentActivity.class;
                break;
            case FROM_TENDER:
                clazz = TenderActivity.class;
                break;
            default:
                clazz = MainActivity.class;
        }

        intent.setClass(this, clazz);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed()
    {
        if (!callBackReturned)
        {
            super.onBackPressed();
        }

    }

    public class WebViewInterface
    {
        Context mContext;

        public WebViewInterface(Context context)
        {
            mContext = context;

        }

        @JavascriptInterface
        public void openAccountSuccess()
        {
            L.d(TAG, "JavascriptInterface: " + "openAccountSuccess");
            if (fromActivity != null && fromActivity.equals(FROM_PAY_FIRST))
            {
                SubmitInstallmentApplicationStepThreeActivity.shouldRefresh = true;
            } else
            {
                AccountFragment.shouldRefresh = true;
            }
            finish();
        }

        @JavascriptInterface
        public void openAccountFail()
        {
            L.d(TAG, "JavascriptInterface: " + "openAccountFail");
            finish();
        }

        @JavascriptInterface
        public void chargeSuccess()
        {
            L.d(TAG, "JavascriptInterface: " + "chargeSuccess");
            AccountFragment.shouldRefresh = true;
            if (fromActivity.equals(FROM_REPAYMENT) || fromActivity.equals(FROM_PAY_FIRST) || fromActivity.equals(FROM_TENDER))
            {
                finish();
            } else
            {
                finishActivity();
            }
        }

        @JavascriptInterface
        public void chargeFail()
        {
            L.d(TAG, "JavascriptInterface: " + "chargeFail");
            finish();
        }

        @JavascriptInterface
        public void cashSuccess()
        {
            L.d(TAG, "JavascriptInterface: " + "cashSuccess");
            finishActivity();
        }

        @JavascriptInterface
        public void cashFail()
        {
            L.d(TAG, "JavascriptInterface: " + "cashFail");
            finish();
        }

        @JavascriptInterface
        public void payFirstSuccess()
        {
            L.d(TAG, "JavascriptInterface: " + "payFirstSuccess");
            AccountFragment.shouldRefresh = true;
            setResult(HF_SUCCESS);
            finish();
        }

        @JavascriptInterface
        public void payFirstFail()
        {
            L.d(TAG, "JavascriptInterface: " + "payFirstFail");
            finish();
        }

        @JavascriptInterface
        public void grantSuccess()
        {
            L.d(TAG, "JavascriptInterface: " + "grantSuccess");
            setResult(HF_SUCCESS);
            finish();
        }

        @JavascriptInterface
        public void grantFail()
        {
            L.d(TAG, "JavascriptInterface: " + "grantFail");
            finish();
        }

        @JavascriptInterface
        public void investTenderApiSuccess()
        {
            L.d(TAG, "JavascriptInterface: " + "investTenderApiSuccess");
            AccountFragment.shouldRefresh = true;
            finishActivity();
        }

        @JavascriptInterface
        public void investTenderApiFail()
        {
            L.d(TAG, "JavascriptInterface: " + "investTenderApiFail");
            finish();
        }

        @JavascriptInterface
        public void payMoneySuccess(String code)
        {
            L.d(TAG, "JavascriptInterface: " + "payMoneySuccess");
            L.d(TAG, "code: " + code);
            AccountFragment.shouldRefresh = true;
            Intent intent = new Intent(mContext, RepaymentActivity.class);
            intent.putExtra("code", Long.parseLong(code));
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }

        @JavascriptInterface
        public void payMoneyFail()
        {
            L.d(TAG, "JavascriptInterface: " + "payMoneyFail");
            finish();
        }
    }
}
