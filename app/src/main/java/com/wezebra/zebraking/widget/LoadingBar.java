package com.wezebra.zebraking.widget;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.wezebra.zebraking.R;

public class LoadingBar extends ProgressDialog
{
    private View view;
    private TextView tvMsg;
    private LayoutInflater mInflater;

    public LoadingBar(Context context)
    {
        this(context, R.style.CustomDialogTheme);
    }

    public LoadingBar(Context context, int theme)
    {
        super(context, theme);
        init(context);
    }

    private void init(Context context)
    {
        mInflater = LayoutInflater.from(context);
        view = mInflater.inflate(R.layout.loading_bar, null);
        tvMsg = (TextView) view.findViewById(R.id.progress_msg);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(view);
    }

    @Override
    public void setMessage(CharSequence message)
    {
        super.setMessage(message);
        if (!TextUtils.isEmpty(message))
        {
            tvMsg.setVisibility(View.VISIBLE);
            tvMsg.setText(message);
        }

    }

    public static LoadingBar show(Context ctx)
    {
        LoadingBar d = new LoadingBar(ctx);
        d.show();
        return d;
    }
}
