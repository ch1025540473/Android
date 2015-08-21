package com.wezebra.zebraking.ui.installment;

import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.wezebra.zebraking.R;
import com.wezebra.zebraking.behavior.HttpAsyncTask;
import com.wezebra.zebraking.ui.BaseActivity;
import com.wezebra.zebraking.util.GenericUtils;
import com.wezebra.zebraking.util.PreferenceUtils;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class PreCalculationActivity extends BaseActivity implements View.OnClickListener {

    private static final double SERVICE_FEE_RATE = 0.02;

    private ImageView cosmos,hexagon_min,line_01,line_02,line_03,line_04;
    private TextView text_01,text_02,text_03,text_04;
    private Animation anim_cosmos,anim_hexagon_min, anim_line,anim_text;
    private FrameLayout anim_frameLayout;
    private EditText amount;
    private Spinner terms;
    private TextView calculate;
    public int amount_int,terms_int;
    public int service_fee,payment_per_month;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_calculation);

        initView();
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_pre_calculation, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    public void initView() {

        cosmos = (ImageView)findViewById(R.id.cosmos);
        hexagon_min = (ImageView)findViewById(R.id.hexagon_min);
        line_01 = (ImageView)findViewById(R.id.line_01);
        line_02 = (ImageView)findViewById(R.id.line_02);
        line_03 = (ImageView)findViewById(R.id.line_03);
        line_04 = (ImageView)findViewById(R.id.line_04);
        text_01 = (TextView)findViewById(R.id.text_01);
        text_02 = (TextView)findViewById(R.id.text_02);
        text_03 = (TextView)findViewById(R.id.text_03);
        text_04 = (TextView)findViewById(R.id.text_04);
        anim_frameLayout = (FrameLayout)findViewById(R.id.animation_layout);
        amount = (EditText)findViewById(R.id.amount);
        terms = (Spinner)findViewById(R.id.terms);
        calculate = (TextView)findViewById(R.id.calculate);

        calculate.setOnClickListener(this);
        anim_cosmos = AnimationUtils.loadAnimation(this, R.anim.animation_cosmos);
        anim_hexagon_min = AnimationUtils.loadAnimation(this,R.anim.animation_hexagon_min);
        anim_line = AnimationUtils.loadAnimation(this,R.anim.animation_line);
        anim_text = AnimationUtils.loadAnimation(this,R.anim.animation_text);

        terms.setOnTouchListener(this);
        calculate.setOnTouchListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.calculate:
                if (input_check() != true) {
                    break;
                }

                get_result();

                List<NameValuePair> postParameters = new ArrayList<>();
                postParameters.add(new BasicNameValuePair("money",payment_per_month + ""));
                postParameters.add(new BasicNameValuePair("staging",(terms_int+1)+""));
                postParameters.add(new BasicNameValuePair("api", "costTrial"));
                HttpAsyncTask httpAsyncTask = HttpAsyncTask.getInstance(PreCalculationActivity.this, PreferenceUtils.getUserId()+"", postParameters);
                httpAsyncTask.preCal();

//                get_result();
//                set_result();
//                start_animation();
                break;
        }

    }

    public boolean input_check() {

        if (!GenericUtils.checkDigit(amount.getText().toString())) {
            amount.requestFocus();
            amount.setError("请输入准确的月租金额度");
            return false;
        }

        switch (terms.getSelectedItemPosition()) {
            case 1:
                terms_int = 2;
                break;
            case 2:
                terms_int = 5;
                break;
        }
        if (terms_int == 0) {
            Toast.makeText(this,"请选择交租方式",Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    public void get_result() {
//        amount_int = Integer.parseInt(amount.getText().toString()) * terms_int;
//        service_fee = (int)(amount_int * SERVICE_FEE_RATE * terms_int);
//        payment_per_month = amount_int / terms_int;
        payment_per_month = Integer.parseInt(amount.getText().toString());
    }

    public void set_result() {

        String s1 = new String("分期金额：￥" + amount_int);
        SpannableStringBuilder builder_01 = new SpannableStringBuilder(s1);
        ForegroundColorSpan textPink1 = new ForegroundColorSpan(getResources().getColor(R.color.text_pink));
        builder_01.setSpan(textPink1,5,s1.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        text_01.setText(builder_01);

        String s2 = new String("服务费：￥" + service_fee);
        SpannableStringBuilder builder_02 = new SpannableStringBuilder(s2);
        ForegroundColorSpan textPink2 = new ForegroundColorSpan(getResources().getColor(R.color.text_pink));
        builder_02.setSpan(textPink2,4,s2.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        text_02.setText(builder_02);

        String s3 = new String("分期数：" + terms_int);
        SpannableStringBuilder builder_03 = new SpannableStringBuilder(s3);
        ForegroundColorSpan textPink3 = new ForegroundColorSpan(getResources().getColor(R.color.text_pink));
        builder_03.setSpan(textPink3,4,s3.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        text_03.setText(builder_03);

        String s4 = new String("每月还款：￥" + payment_per_month);
        SpannableStringBuilder builder_04 = new SpannableStringBuilder(s4);
        ForegroundColorSpan textPink4 = new ForegroundColorSpan(getResources().getColor(R.color.text_pink));
        builder_04.setSpan(textPink4,5,s4.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        text_04.setText(builder_04);

    }

    public void start_animation() {

        anim_frameLayout.setVisibility(View.VISIBLE);
        cosmos.startAnimation(anim_cosmos);
        hexagon_min.startAnimation(anim_hexagon_min);
        line_01.startAnimation(anim_line);
        line_02.startAnimation(anim_line);
        line_03.startAnimation(anim_line);
        line_04.startAnimation(anim_line);
        text_01.startAnimation(anim_text);
        text_02.startAnimation(anim_text);
        text_03.startAnimation(anim_text);
        text_04.startAnimation(anim_text);

    }

}
