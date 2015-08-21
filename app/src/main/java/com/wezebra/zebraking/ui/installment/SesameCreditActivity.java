package com.wezebra.zebraking.ui.installment;

import android.app.Fragment;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.wezebra.zebraking.R;
import com.wezebra.zebraking.ui.BaseActivity;
import com.wezebra.zebraking.ui.fragment.AuthDialogFragment;

public class SesameCreditActivity extends BaseActivity implements View.OnClickListener{

    TextView turn_up,sesame_des;
    AuthDialogFragment authDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sesame_credit);
        init();
    }

    private void init() {
        turn_up = (TextView) findViewById(R.id.turn_up);
        sesame_des = (TextView) findViewById(R.id.sesame_des);
        // 修改认证描述中字体颜色
        changeTextColor(sesame_des,9,12);
        turn_up.setOnClickListener(this);
        authDialog = new AuthDialogFragment();
    }

    private void changeTextColor(TextView textView,int start,int end) {
        SpannableStringBuilder builder = new SpannableStringBuilder(textView.getText().toString());
        builder.setSpan(new ForegroundColorSpan(Color.parseColor("#f9b30b")),start,end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(builder);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.turn_up:
                authDialog.show(getFragmentManager(),"sesamecredit");
                break;
        }
    }
}
