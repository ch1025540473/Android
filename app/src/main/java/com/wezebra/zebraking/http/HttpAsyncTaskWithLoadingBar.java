package com.wezebra.zebraking.http;

import android.app.Activity;
import android.os.AsyncTask;

import com.wezebra.zebraking.widget.LoadingBar;

/**
 * Created by HQDev on 2015/6/17.
 */
public abstract class HttpAsyncTaskWithLoadingBar extends AsyncTask<String,Integer,String> {

    private LoadingBar loadingBar;

    protected abstract Activity getContextActivity();

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        if (loadingBar == null)
        {
            loadingBar = new LoadingBar( getContextActivity() );
        }
        loadingBar.setCancelable(false);
        loadingBar.show();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        if (loadingBar != null && loadingBar.isShowing()) {
            loadingBar.dismiss();
        }

    }

}
