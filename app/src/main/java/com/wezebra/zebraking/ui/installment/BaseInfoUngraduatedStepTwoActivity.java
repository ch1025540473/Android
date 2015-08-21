package com.wezebra.zebraking.ui.installment;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.wezebra.zebraking.App;
import com.wezebra.zebraking.R;
import com.wezebra.zebraking.behavior.HttpAsyncTask;
import com.wezebra.zebraking.behavior.SpinnerUtils;
import com.wezebra.zebraking.model.ProgressBarHolder;
import com.wezebra.zebraking.model.User;
import com.wezebra.zebraking.ui.BaseActivity;
import com.wezebra.zebraking.util.Constants;
import com.wezebra.zebraking.util.PreferenceUtils;
import com.wezebra.zebraking.util.UmengUtils;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class BaseInfoUngraduatedStepTwoActivity extends BaseActivity implements View.OnClickListener {

    private TextView submit,tips;
    private SharedPreferences entrance_flag,sharedPreferences;
    private SharedPreferences.Editor editor;
    private int entrance;

    private RadioGroup creditCard,overdue;
    private EditText creditAmount;
    private LinearLayout amountLayout,overdueLayout;

    private Spinner expend,marriage;

    private ProgressBarHolder progressBarHolder;

    private int expend_i,marriage_i,creditCard_i,overdue_i;
    private String creditAmount_s;
    private List<NameValuePair> postParameters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_info_ungraduated_step_two);

        initView();
        echoHistory();
    }

    @Override
    protected void onResume() {
        super.onResume();

        postParameters = new ArrayList<>();
        for (NameValuePair i:((App)getApplication()).postParameters.get("StepOneUG")) {
            postParameters.add(i);
        }
        Log.i("postParameters2-3", postParameters.toString());
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        showProgressbar();
    }

    private void showProgressbar() {

        final TextView progress = (TextView)findViewById(R.id.progress_text);
        final ImageView progressbar = (ImageView)findViewById(R.id.progressbar);
        progressbar.setVisibility(View.VISIBLE);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.anim_base_info_progressbar_ug_two);
        animation.setFillAfter(true);
        progressbar.startAnimation(animation);

        final ViewTreeObserver observer = progress.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                ViewTreeObserver obs = progress.getViewTreeObserver();
                //textViewHolder = new TextViewHolder(tv,tv.getWidth(),seekBar.getWidth()-tv.getWidth()-tv.getWidth());
                progressBarHolder = new ProgressBarHolder(progress, progressbar, progress.getWidth(), progressbar.getWidth() - (int) (progress.getWidth() * 1.7));

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    obs.removeOnGlobalLayoutListener(this);
                } else {
                    obs.removeGlobalOnLayoutListener(this);
                }

                ValueAnimator animator = new ObjectAnimator().ofInt(progressBarHolder, "progress", 66, 100).setDuration(840);
                animator.setInterpolator(new LinearInterpolator());
                animator.start();

            }
        });

    }

    private void initView() {

        ScrollView scrollView = (ScrollView)findViewById(R.id.scroll_view);
        scrollView.setVerticalScrollBarEnabled(Constants.SCROLL_VIEW_VERTICAL_SCROLL_BAR_ENANBLE);

        submit = (TextView)findViewById(R.id.submit);
        submit.setOnClickListener(this);

        creditCard = (RadioGroup)findViewById(R.id.credit_card);
        amountLayout = (LinearLayout)findViewById(R.id.amount_layout);
        overdueLayout = (LinearLayout)findViewById(R.id.overdue_layout);
        creditCard.check(R.id.with_credit_card);
        creditCard.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.with_credit_card:
                        amountLayout.setVisibility(View.VISIBLE);
                        overdueLayout.setVisibility(View.VISIBLE);
                        break;
                    case R.id.without_credit_card:
                        amountLayout.setVisibility(View.INVISIBLE);
                        overdueLayout.setVisibility(View.INVISIBLE);
                        break;
                }
            }
        });

        overdue = (RadioGroup)findViewById(R.id.overdue);
        overdue.check(R.id.overdued);

        //Init Spinner
        expend = (Spinner)findViewById(R.id.expend);
        marriage = (Spinner)findViewById(R.id.marriage);
        SpinnerUtils.initExpend(this, expend);
        SpinnerUtils.initMarriage(this, marriage);

        creditAmount = (EditText)findViewById(R.id.credit_amount);
        tips = (TextView)findViewById(R.id.tips);

    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_base_info_ungraduated_step_two, menu);
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

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.submit:
                if (checkInput()) {

                    UmengUtils.baseInfoSubmit(this,null,null,null,null,marriage.getSelectedItem().toString());

                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("提示");
                    builder.setMessage("请确认所填信息正确无误，完成后将暂时无法更改哦");
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();

                            Intent intent = new Intent();
                            Bundle bundle = new Bundle();
                            bundle.putInt(Constants.INFO_SUBMIT,Constants.BASE_INFO_SUBMIT);
                            intent.putExtra(Constants.INFO_SUBMIT,bundle);

                            sharedPreferences = getSharedPreferences(Constants.INFO_AUTH_STATE,MODE_PRIVATE);
                            editor = sharedPreferences.edit();
                            editor.putInt(Constants.JOB_OR_EDUCATION_INFO_STATE,Constants.INFO_UNFILLED);
                            editor.commit();
                            postParameters.add(new BasicNameValuePair("api", "userBasicStep"));
                            HttpAsyncTask httpAsyncTask = HttpAsyncTask.getInstance(BaseInfoUngraduatedStepTwoActivity.this, PreferenceUtils.getUserId()+"", postParameters);
                            httpAsyncTask.postWithMultiEntrance(intent);
                        }
                    });

                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    builder.create().show();

//                    entrance_flag = getSharedPreferences(Constants.ENTRANCE_FLAG,MODE_PRIVATE);
//                    entrance = entrance_flag.getInt(Constants.ENTRANCE_FLAG,Constants.DEFAULT_ENTRANCE);
//                    switch (entrance) {
//                        case Constants.IS_FROM_APPLICATION:
//                            intent.setClass(this,SubmitInstallmentApplicationStepOneActivity.class);
//                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                            startActivity(intent);
//                            break;
//                        case Constants.IS_FROM_PROFILE:
//                            intent.setClass(this,PersonalProfileActivity.class);
//                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                            startActivity(intent);
//                            break;
//                    }

                }
                break;
        }

    }

    public boolean checkInput() {

        postParameters = new ArrayList<>();
        for (NameValuePair i:((App)getApplication()).postParameters.get("StepOneUG")) {
            postParameters.add(i);
        }

        switch (expend.getSelectedItemPosition()) {
            case 0:
                Toast.makeText(this,getString(R.string.expend_errors),Toast.LENGTH_LONG).show();
                return false;
            default:
                expend_i = expend.getSelectedItemPosition();
                postParameters.add(new BasicNameValuePair("monthlyPayType",expend_i+""));
                break;
        }

        switch (marriage.getSelectedItemPosition()) {
            case 0:
                Toast.makeText(this,getString(R.string.marriage_errors),Toast.LENGTH_LONG).show();
                return false;
            default:
                marriage_i = marriage.getSelectedItemPosition();
                postParameters.add(new BasicNameValuePair("marriageType",marriage_i+""));
                break;
        }

        if (creditCard.getCheckedRadioButtonId() == R.id.with_credit_card) {
            creditCard_i = 1;
            creditAmount_s = creditAmount.getText().toString();
            if (creditAmount_s.equals("")) {
                Toast.makeText(this,getString(R.string.credit_amount_errors),Toast.LENGTH_LONG).show();
                return false;
            } else {
                postParameters.add(new BasicNameValuePair("cardQuota",creditAmount_s));
            }
        } else {
            creditCard_i = 2;
        }
        postParameters.add(new BasicNameValuePair("hasCard",creditCard_i+""));

        if (overdue.getCheckedRadioButtonId() == R.id.overdued) {
            overdue_i = 1;
        } else {
            overdue_i = 2;
        }
        postParameters.add(new BasicNameValuePair("hasOverdue",overdue_i+""));

        return true;
    }

    public void echoHistory() {
        User user = ((App)getApplication()).user;
        if (user == null) {
            Toast.makeText(this,getString(R.string.access_server_errors),Toast.LENGTH_LONG).show();
            return;
        } else if (user.getStatusInt() == null) {
        } else if (user.getStatusInt() == Constants.UNPASS) {
            tips.setText(user.getMemo());
            tips.setVisibility(View.VISIBLE);
        }
        if (user.getMonthlyPayType()!=null){
            expend.setSelection(user.getMonthlyPayType());
        }
        if (user.getMarriageType()!=null){
            marriage.setSelection(user.getMarriageType());
        }
        if (user.getHasCard()!=null){
            if (user.getHasCard() == 1) {
                creditCard.check(R.id.with_credit_card);
            } else {
                creditCard.check(R.id.without_credit_card);
            }
        }
        if (user.getCardQuota()!=null){
            creditAmount.setText(user.getCardQuota()+"");
        }
        if (user.getHasOverdue()!=null){
            if(user.getHasOverdue() == 1) {
                overdue.check(R.id.overdued);
            } else {
                overdue.check(R.id.unoverdued);
            }
        }
    }

}
