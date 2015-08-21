package com.wezebra.zebraking.ui.installment;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.LinearLayout;
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

public class BaseInfoGraduatedStepOneActivity extends BaseActivity implements View.OnClickListener {

    private TextView next_page,tips;
    private RadioGroup employState,socialSecurity,paf;
    private LinearLayout withoutWorkLayout,withWorkLayout;

    private Spinner diploma,companySize;
    private TextView graduationDate,jobEntranceTime;
    private EditText schoolName,companyName,position;

    private int employState_i,diploma_i,socialSecurity_i,paf_i,companySize_i;
    private String schoolName_s,graduationDate_s,companyName_s,position_s,jobEntranceTime_s;
    private List<NameValuePair> postParameters;

    private ProgressBarHolder progressBarHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_info_graduated_step_one);

        initView();
        echoHistory();
    }

    @Override
    protected void onResume() {
        super.onResume();

        postParameters = new ArrayList<>();
        for (NameValuePair i:((App)getApplication()).postParameters.get("StepInitial")) {
            postParameters.add(i);
        }
        Log.i("onResumeParameters", postParameters.toString());
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        showProgressbarWithoutJob();
    }

    private void showProgressbarWithoutJob() {

        final TextView progress = (TextView)findViewById(R.id.progress_text);
        //progress.setText("66%");
        final ImageView progressbar = (ImageView)findViewById(R.id.progressbar);
        progressbar.setVisibility(View.VISIBLE);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.anim_base_info_progressbar_g_one_uj);
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

                ValueAnimator animator = new ObjectAnimator().ofInt(progressBarHolder, "progress", 25, 50).setDuration(750);
                animator.setInterpolator(new LinearInterpolator());
                animator.start();

            }
        });

    }

    private void showProgressbarWithJob() {

        final TextView progress = (TextView)findViewById(R.id.progress_text);
        progress.setText("50%");
        final ImageView progressbar = (ImageView)findViewById(R.id.progressbar);
        progressbar.setVisibility(View.VISIBLE);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.anim_base_info_progressbar_g_one_j);
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

                ValueAnimator animator = new ObjectAnimator().ofInt(progressBarHolder, "progress", 25, 50).setDuration(1250);
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

        employState = (RadioGroup)findViewById(R.id.employ_state);
        withoutWorkLayout = (LinearLayout)findViewById(R.id.without_work_layout);
        withWorkLayout = (LinearLayout)findViewById(R.id.with_work_layout);
        employState.check(R.id.unEmployed);
        employState.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.unEmployed:
                        withoutWorkLayout.setVisibility(View.VISIBLE);
                        withWorkLayout.setVisibility(View.GONE);
                        //showProgressbarWithoutJob();
                        break;
                    case R.id.employed:
                        withoutWorkLayout.setVisibility(View.GONE);
                        withWorkLayout.setVisibility(View.VISIBLE);
                        //showProgressbarWithJob();
                        break;
                }
            }
        });

        socialSecurity = (RadioGroup)findViewById(R.id.social_security);
        paf = (RadioGroup)findViewById(R.id.paf);
        socialSecurity.check(R.id.with_ss);
        paf.check(R.id.with_paf);

        //Init Spinner
        diploma = (Spinner)findViewById(R.id.diploma);
        companySize = (Spinner)findViewById(R.id.company_size);
        SpinnerUtils.initDiploma(this, diploma);
        SpinnerUtils.initCompanySize(this, companySize);

        graduationDate = (TextView)findViewById(R.id.graduation_date);
        graduationDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Time time = new Time("GMT+8");
                time.setToNow();
                int year = time.year;
                int month = time.month;
                MonthPickerDialog monthPickerDialog = new MonthPickerDialog(BaseInfoGraduatedStepOneActivity.this,graduationDateSetListener,year,month,1);
                monthPickerDialog.setTitle(getString(R.string.graduation_date));
                monthPickerDialog.show();
            }
        });

        jobEntranceTime = (TextView)findViewById(R.id.job_entrance_time);
        jobEntranceTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Time time = new Time("GMT+8");
                time.setToNow();
                int year = time.year;
                int month = time.month;
                MonthPickerDialog monthPickerDialog = new MonthPickerDialog(BaseInfoGraduatedStepOneActivity.this,jobDateSetListener,year,month,1);
                monthPickerDialog.setTitle(getString(R.string.job_entrance_time));
                monthPickerDialog.show();
            }
        });

        schoolName = (EditText)findViewById(R.id.school_name);
        companyName = (EditText)findViewById(R.id.company_name);
        position = (EditText)findViewById(R.id.position);
        tips = (TextView)findViewById(R.id.tips);

    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_base_info_graduated_step_one, menu);
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

                    String employState_s = "";
                    if (employState.getCheckedRadioButtonId() == R.id.employed) {
                        employState_s = "在工作";
                    } else if (employState.getCheckedRadioButtonId() == R.id.unEmployed) {
                        employState_s = "没工作";
                    }
                    UmengUtils.baseInfoSubmit(this,diploma.getSelectedItem().toString(),null,employState_s,null,null);

                    Intent intent = new Intent();
                    SharedPreferences sharedPreferences = getSharedPreferences(Constants.INFO_AUTH_STATE,MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    ((App)getApplication()).postParameters.put("StepOneG",postParameters);
                    Log.i("StepOneG",postParameters.toString());
                    if (employState.getCheckedRadioButtonId() == R.id.employed) {
                        editor.putInt(Constants.JOB_OR_EDUCATION,Constants.IS_JOB_AUTH);
                        editor.commit();
                        intent.setClass(this,BaseInfoGraduatedWithJobStepOneActivity.class);
                        startActivity(intent);
                    } else if (employState.getCheckedRadioButtonId() == R.id.unEmployed) {
                        editor.putInt(Constants.JOB_OR_EDUCATION,Constants.IS_EDUCATION_AUTH);
                        editor.commit();
                        intent.setClass(this,BaseInfoGraduatedWithoutJobActivity.class);
                        startActivity(intent);
                    } else {
                        //Nothing
                    }
                }
                break;
        }

    }

    private DatePickerDialog.OnDateSetListener graduationDateSetListener = new DatePickerDialog.OnDateSetListener()
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
                graduationDate.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
                graduationDate.setTextColor(Constants.TEXT_COLOR_DEFAULT);
            }
        }
    };

    private DatePickerDialog.OnDateSetListener jobDateSetListener = new DatePickerDialog.OnDateSetListener()
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

                jobEntranceTime.setText(mYear + "-" + mMonth);
                jobEntranceTime.setGravity(Gravity.LEFT|Gravity.CENTER_VERTICAL);
                jobEntranceTime.setTextColor(Constants.TEXT_COLOR_DEFAULT);
            }
        }
    };

    public boolean checkInput() {

        postParameters = new ArrayList<>();
        for (NameValuePair i:((App)getApplication()).postParameters.get("StepInitial")) {
            postParameters.add(i);
        }

        employState_i = employState.getCheckedRadioButtonId();
        if (employState_i == R.id.employed) {

            employState_i = 1;
            postParameters.add(new BasicNameValuePair("jobStatus",employState_i+""));
            companyName_s = companyName.getText().toString();
            if (companyName_s.equals("")) {
                companyName.requestFocus();
                companyName.setError(getString(R.string.company_name_errors));
                return false;
            } else {
                postParameters.add(new BasicNameValuePair("company",companyName_s));
            }
            position_s = position.getText().toString();
            if (position_s.equals("")) {
                position.requestFocus();
                position.setError(getString(R.string.position_errors));
                return false;
            } else {
                postParameters.add(new BasicNameValuePair("position",position_s));
            }
            jobEntranceTime_s = jobEntranceTime.getText().toString();
            if (jobEntranceTime_s.equals(getString(R.string.choose))) {
                Toast.makeText(this,getString(R.string.job_entrance_time_errors),Toast.LENGTH_LONG).show();
                return false;
            } else {
                postParameters.add(new BasicNameValuePair("workTime",jobEntranceTime_s));
            }
            companySize_i = companySize.getSelectedItemPosition();
            if (companySize_i == 0) {
                Toast.makeText(this,getString(R.string.company_size_errors),Toast.LENGTH_LONG).show();
                return false;
            } else {
                postParameters.add(new BasicNameValuePair("companyScale",companySize_i+""));
            }

        } else {

            employState_i = 2;
            postParameters.add(new BasicNameValuePair("jobStatus",employState_i+""));
            diploma_i = diploma.getSelectedItemPosition();
            if (diploma_i == 0) {
                Toast.makeText(this,getString(R.string.diploma_errors),Toast.LENGTH_LONG).show();
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
                postParameters.add(new BasicNameValuePair("graduateSchool", schoolName_s));
            }
            graduationDate_s = graduationDate.getText().toString();
            if (graduationDate_s.equals(getString(R.string.choose))) {
                Toast.makeText(this,getString(R.string.graduation_date_errors),Toast.LENGTH_LONG).show();
                return false;
            } else {
                postParameters.add(new BasicNameValuePair("graduateTime",graduationDate_s));
            }
            if (socialSecurity.getCheckedRadioButtonId() == R.id.with_ss) {
                socialSecurity_i = 2;
            } else {
                socialSecurity_i = 1;
            }
            postParameters.add(new BasicNameValuePair("hasSecurity",socialSecurity_i+""));
            if (paf.getCheckedRadioButtonId() == R.id.with_paf) {
                paf_i = 2;
            } else {
                paf_i = 1;
            }
            postParameters.add(new BasicNameValuePair("hasReserve",paf_i+""));

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
        int jobstatus=0;
        int education=0;
        if (user.getJobStatus()!=null){
            jobstatus = user.getJobStatus();
        }else {
            jobstatus = PreferenceUtils.getJobStatus();
        }
        if (jobstatus == 1) {
            employState.check(R.id.employed);
        } else {
            employState.check(R.id.unEmployed);
        }

        if (user.getEducation()==null){
            education = PreferenceUtils.getEducational();
        }else {
            education = user.getEducation();
        }
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
        graduationDate.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
        graduationDate.setTextColor(getResources().getColor(R.color.text_dark));
        if (user.getHasSecurity()!=null){
            if (user.getHasSecurity() == 2) {
                socialSecurity.check(R.id.with_ss);
            } else {
                socialSecurity.check(R.id.without_ss);
            }
        }
        if (user.getHasReserve()!=null){
            if (user.getHasReserve() == 2) {
                paf.check(R.id.with_paf);
            } else {
                paf.check(R.id.without_paf);
            }
        }
        companyName.setText(user.getCompany());
        position.setText(user.getPosition());
        jobEntranceTime.setText(user.getWorkTime());
        jobEntranceTime.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
        jobEntranceTime.setTextColor(getResources().getColor(R.color.text_dark));
        if (user.getCompanyScale()!=null){
            companySize.setSelection(user.getCompanyScale());
        }
    }

}
