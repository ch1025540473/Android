package com.wezebra.zebraking.ui.installment;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.wezebra.zebraking.App;
import com.wezebra.zebraking.R;
import com.wezebra.zebraking.behavior.SpinnerUtils;
import com.wezebra.zebraking.model.ProgressBarHolder;
import com.wezebra.zebraking.model.User;
import com.wezebra.zebraking.ui.BaseActivity;
import com.wezebra.zebraking.util.Constants;
import com.wezebra.zebraking.util.PreferenceUtils;
import com.wezebra.zebraking.util.UmengUtils;
import com.wezebra.zebraking.widget.MonthPickerDialog;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class BaseInfoGraduatedWithJobStepOneActivity extends BaseActivity implements View.OnClickListener {

    private TextView next_page,tips;

    private RadioGroup socialSecurity,paf;

    private Spinner diploma;
    private TextView graduationDate;
    private EditText schoolName;

    private ProgressBarHolder progressBarHolder;

    private int socialSecurity_i,paf_i,diploma_i;
    private String schoolName_s,graduationDate_s;
    private List<NameValuePair> postParameters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_info_graduated_with_job_step_one);

        initView();
        echoHistory();
    }

    @Override
    protected void onResume() {
        super.onResume();

        postParameters = new ArrayList<>();
        for (NameValuePair i:((App)getApplication()).postParameters.get("StepOneG")) {
            postParameters.add(i);
        }
        Log.i("onResumeParameters", postParameters.toString());

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
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.anim_base_info_progressbar_g_j_one);
        animation.setFillAfter(true);
        progressbar.startAnimation(animation);

        final ViewTreeObserver observer = progress.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                ViewTreeObserver obs = progress.getViewTreeObserver();
                //textViewHolder = new TextViewHolder(tv,tv.getWidth(),seekBar.getWidth()-tv.getWidth()-tv.getWidth());
                progressBarHolder = new ProgressBarHolder(progress,progressbar, progress.getWidth(), progressbar.getWidth() - progress.getWidth());

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    obs.removeOnGlobalLayoutListener(this);
                } else {
                    obs.removeGlobalOnLayoutListener(this);
                }

                ValueAnimator animator = new ObjectAnimator().ofInt(progressBarHolder, "progress", 50, 75).setDuration(750);
                animator.setInterpolator(new LinearInterpolator());
                animator.start();

            }
        });

    }

    private void initView() {

        ScrollView scrollView = (ScrollView)findViewById(R.id.scroll_view);
        scrollView.setVerticalScrollBarEnabled(Constants.SCROLL_VIEW_VERTICAL_SCROLL_BAR_ENANBLE);

        next_page = (TextView)findViewById(R.id.next_page);
        next_page.setOnClickListener(this);

        socialSecurity = (RadioGroup)findViewById(R.id.social_security);
        socialSecurity.check(R.id.with_ss);
        paf = (RadioGroup)findViewById(R.id.paf);
        paf.check(R.id.with_paf);

        //Init Spinner
        diploma = (Spinner)findViewById(R.id.diploma);
        SpinnerUtils.initDiploma(this,diploma);

        graduationDate = (TextView)findViewById(R.id.graduation_date);
        graduationDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Time time = new Time("GMT+8");
                time.setToNow();
                int year = time.year;
                int month = time.month;
                MonthPickerDialog monthPickerDialog = new MonthPickerDialog(BaseInfoGraduatedWithJobStepOneActivity.this,dateSetListener,year,month,1);
                monthPickerDialog.setTitle(getString(R.string.graduation_date));
                monthPickerDialog.show();
            }
        });
        schoolName = (EditText)findViewById(R.id.school_name);
        tips = (TextView)findViewById(R.id.tips);

    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_base_info_graduated_with_job_step_one, menu);
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
            case R.id.next_page:
                if (checkInput()) {

                    UmengUtils.baseInfoSubmit(this,diploma.getSelectedItem().toString(),null,null,null,null);

                    ((App) getApplication()).postParameters.put("StepGJOne",postParameters);
                    Log.i("postParameters2-2", postParameters.toString());
                    Intent intent = new Intent();
                    intent.setClass(this,BaseInfoGraduatedWithJobStepTwoActivity.class);
                    startActivity(intent);
                }
                break;
        }

    }

    private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener()
    {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
        {
            if (view.isShown())
            {
                String mYear = "" + year;
                String mMonth = "";
                if (monthOfYear > 8)
                {
                    mMonth = mMonth + (monthOfYear + 1);
                } else
                {
                    mMonth = mMonth + "0"
                            + (monthOfYear + 1);
                }

                graduationDate.setText(mYear + "-" + mMonth);
                graduationDate.setGravity(Gravity.LEFT|Gravity.CENTER_VERTICAL);
                graduationDate.setTextColor(Constants.TEXT_COLOR_DEFAULT);
            }
        }
    };

    public boolean checkInput() {

        postParameters = new ArrayList<>();
        for (NameValuePair i:((App)getApplication()).postParameters.get("StepOneG")) {
            postParameters.add(i);
        }

        if (socialSecurity.getCheckedRadioButtonId() == R.id.with_ss) {
            socialSecurity_i = 1;
        } else {
            socialSecurity_i = 2;
        }
        postParameters.add(new BasicNameValuePair("hasSecurity",socialSecurity_i+""));
        if (paf.getCheckedRadioButtonId() == R.id.with_paf) {
            paf_i = 1;
        } else {
            paf_i = 2;
        }
        postParameters.add(new BasicNameValuePair("hasReserve",paf_i+""));
        if (diploma.getSelectedItemPosition() == 0) {
            Toast.makeText(this, getString(R.string.diploma_errors), Toast.LENGTH_LONG).show();
            return false;
        } else {
            switch (diploma.getSelectedItemPosition()) {
                case 1:
                    diploma_i = -1;
                    break;
                default:
                    diploma_i = diploma.getSelectedItemPosition()-1;
                    break;
            }
            postParameters.add(new BasicNameValuePair("education", diploma_i + ""));
        }
        schoolName_s = schoolName.getText().toString();
        if (schoolName_s.equals("")) {
            schoolName.requestFocus();
            schoolName.setError(getString(R.string.school_name_errors));
            return false;
        } else {
            postParameters.add(new BasicNameValuePair("graduteSchool",schoolName_s));
        }
        graduationDate_s = graduationDate.getText().toString();
        if (graduationDate_s.equals(getString(R.string.choose))) {
            Toast.makeText(this,getString(R.string.graduation_date_errors),Toast.LENGTH_LONG).show();
            return false;
        } else {
            postParameters.add(new BasicNameValuePair("graduateTime",graduationDate_s));
        }
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
        if (user.getHasSecurity()!=null){
            if (user.getHasSecurity() == 1) {
                socialSecurity.check(R.id.with_ss);
            } else {
                socialSecurity.check(R.id.without_ss);
            }
        }
        if (user.getHasReserve()!=null){
            if (user.getHasReserve() == 1) {
                paf.check(R.id.with_paf);
            } else {
                paf.check(R.id.without_paf);
            }
        }

        int education;
        if (user.getEducation()!=null){
            education =user.getEducation();
        }else {
            education = PreferenceUtils.getEducational();
        }
        Log.i("test..------------","");
        Log.i("education",education+"");
        switch (education) {
            case 0:
                diploma.setSelection(0);
                break;
            case -1:
                diploma.setSelection(1);
                break;
            default:
                diploma.setSelection(education+1);
                break;
        }
        schoolName.setText(user.getGraduateSchool());
        graduationDate.setText(user.getGraduateTime());
        graduationDate.setGravity(Gravity.CENTER_VERTICAL|Gravity.LEFT);
        graduationDate.setTextColor(getResources().getColor(R.color.text_dark));
    }

}
