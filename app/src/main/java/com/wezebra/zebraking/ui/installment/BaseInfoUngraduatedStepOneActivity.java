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

public class BaseInfoUngraduatedStepOneActivity extends BaseActivity implements View.OnClickListener {

    private TextView next_page;

    private RadioGroup incomeInfo;
    private LinearLayout incomeOrginLayout,incomePerMonthLayout;

    private Spinner diploma,sourceOfIncome,income;
    private EditText schoolName;
    private TextView eduEntranceTime,tips;

    private ProgressBarHolder progressBarHolder;

    private String schoolName_s,eduEntranceTime_s;
    private int diploma_i,incomeInfo_i,sourceOfIncome_i,income_i;
    private List<NameValuePair> postParameters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_info_ungraduated_step_one);

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
        Log.i("onResumeParameters",postParameters.toString());
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
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.anim_base_info_progressbar_ug_one);
        animation.setFillAfter(true);
        progressbar.startAnimation(animation);

        final ViewTreeObserver observer = progress.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                ViewTreeObserver obs = progress.getViewTreeObserver();
                //textViewHolder = new TextViewHolder(tv,tv.getWidth(),seekBar.getWidth()-tv.getWidth()-tv.getWidth());
                progressBarHolder = new ProgressBarHolder(progress, progressbar, progress.getWidth(), progressbar.getWidth() - progress.getWidth());

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    obs.removeOnGlobalLayoutListener(this);
                } else {
                    obs.removeGlobalOnLayoutListener(this);
                }

                ValueAnimator animator = new ObjectAnimator().ofInt(progressBarHolder, "progress", 25, 66).setDuration(910);
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

        incomeInfo = (RadioGroup)findViewById(R.id.income_info);
        incomeInfo.check(R.id.with_income);
        incomeInfo.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.with_income:
                        incomeOrginLayout.setVisibility(View.VISIBLE);
                        incomePerMonthLayout.setVisibility(View.VISIBLE);
                        break;
                    case R.id.without_income:
                        incomeOrginLayout.setVisibility(View.INVISIBLE);
                        incomePerMonthLayout.setVisibility(View.INVISIBLE);
                        break;
                }
            }
        });

        incomeOrginLayout = (LinearLayout)findViewById(R.id.income_origin_layout);
        incomePerMonthLayout = (LinearLayout)findViewById(R.id.income_per_month_layout);

        //Init Spinner
        diploma = (Spinner)findViewById(R.id.diploma);
        sourceOfIncome = (Spinner)findViewById(R.id.source_of_live);
        income = (Spinner)findViewById(R.id.income);
        SpinnerUtils.initDiploma(this,diploma);
        SpinnerUtils.initSourceOfIncome(this, sourceOfIncome);
        SpinnerUtils.initIncome(this, income);

        eduEntranceTime = (TextView)findViewById(R.id.edu_entrance_time);
        eduEntranceTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Time time = new Time("GMT+8");
                time.setToNow();
                int year = time.year;
                int month = time.month;
                MonthPickerDialog monthPickerDialog = new MonthPickerDialog(BaseInfoUngraduatedStepOneActivity.this,dateSetListener,year,month,1);
                monthPickerDialog.setTitle(getString(R.string.edu_entrance_time));
                monthPickerDialog.show();
            }
        });

        schoolName = (EditText)findViewById(R.id.school_name);
        tips = (TextView)findViewById(R.id.tips);
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

                eduEntranceTime.setText(mYear + "-" + mMonth);
                eduEntranceTime.setGravity(Gravity.LEFT|Gravity.CENTER_VERTICAL);
                eduEntranceTime.setTextColor(Constants.TEXT_COLOR_DEFAULT);
            }
        }
    };

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_base_info_un_graduated_step_one, menu);
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

                    UmengUtils.baseInfoSubmit(this,diploma.getSelectedItem().toString(),null,null,income.getSelectedItem().toString(),null);

                            ((App) getApplication()).postParameters.put("StepOneUG",postParameters);
                    Log.i("postParameters2-2", postParameters.toString());
                    Intent intent = new Intent();
                    intent.setClass(this,BaseInfoUngraduatedStepTwoActivity.class);
                    startActivity(intent);
                }
                break;
        }

    }

    private boolean checkInput() {

        postParameters = new ArrayList<>();
        for (NameValuePair i:((App)getApplication()).postParameters.get("StepInitial")) {
            postParameters.add(i);
        }

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
            postParameters.add(new BasicNameValuePair("onEducation", diploma_i + ""));
        }

        schoolName_s = schoolName.getText().toString();
        if (schoolName_s.equals("")) {
            schoolName.requestFocus();
            schoolName.setError(getString(R.string.no_input_errors));
            return false;
        } else {
            postParameters.add(new BasicNameValuePair("graduateSchool", schoolName_s));
        }

        eduEntranceTime_s = eduEntranceTime.getText().toString();
        if (eduEntranceTime_s.equals(getString(R.string.choose)) || eduEntranceTime_s.equals("")) {
            Toast.makeText(this,getString(R.string.edu_entrance_time_errors),Toast.LENGTH_LONG).show();
            return false;
        } else {
            postParameters.add(new BasicNameValuePair("schoolInTime", eduEntranceTime_s));
        }

        if (incomeInfo.getCheckedRadioButtonId() == R.id.with_income) {
            incomeInfo_i = 1;
            switch (sourceOfIncome.getSelectedItemPosition()) {
                case 0:
                    Toast.makeText(this,getString(R.string.source_of_income_errors),Toast.LENGTH_LONG).show();
                    return false;
                default:
                    sourceOfIncome_i = sourceOfIncome.getSelectedItemPosition();
                    postParameters.add(new BasicNameValuePair("salaryType", sourceOfIncome_i + ""));
                    break;
            }
            switch (income.getSelectedItemPosition()) {
                case 0:
                    Toast.makeText(this,getString(R.string.income_errors),Toast.LENGTH_LONG).show();
                    return false;
                default:
                    income_i = income.getSelectedItemPosition();
                    postParameters.add(new BasicNameValuePair("salary", income_i + ""));
                    break;
            }
        } else {
            incomeInfo_i = 2;
        }
        postParameters.add(new BasicNameValuePair("hasIncome", incomeInfo_i + ""));

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
        int education;
        if (user.getOnEducation()==null){
            education = PreferenceUtils.getEducational();
        }else {
            education = user.getOnEducation();
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
        eduEntranceTime.setText(user.getSchoolInTime());
        eduEntranceTime.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
        eduEntranceTime.setTextColor(getResources().getColor(R.color.text_dark));
        if (user.getHasIncome()!=null){
            if (user.getHasIncome() == 1) {
                incomeInfo.check(R.id.with_income);
            } else {
                incomeInfo.check(R.id.without_income);
            }
        }
        if (user.getSalaryType()!=null){
            sourceOfIncome.setSelection(user.getSalaryType());
        }
        if (user.getSalary()==null){
            income.setSelection(PreferenceUtils.getMonthIncome());
        }else {
            income.setSelection(user.getSalary());
        }
    }

}
