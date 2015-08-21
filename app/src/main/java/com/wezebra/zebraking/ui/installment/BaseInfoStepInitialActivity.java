package com.wezebra.zebraking.ui.installment;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import com.wezebra.zebraking.http.CusSuccessListener;
import com.wezebra.zebraking.http.TaskResponse;
import com.wezebra.zebraking.http.ZebraTask;
import com.wezebra.zebraking.model.ProgressBarHolder;
import com.wezebra.zebraking.model.User;
import com.wezebra.zebraking.ui.BaseActivity;
import com.wezebra.zebraking.util.CommonUtils;
import com.wezebra.zebraking.util.Constants;
import com.wezebra.zebraking.util.GenericUtils;
import com.wezebra.zebraking.util.L;
import com.wezebra.zebraking.util.PreferenceUtils;
import com.wezebra.zebraking.util.UmengUtils;
import com.wezebra.zebraking.util.Utils;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BaseInfoStepInitialActivity extends BaseActivity implements View.OnClickListener {

    private Spinner cities;
    private TextView next_page,tips;
    private RadioGroup graduation;
    private ProgressBarHolder progressBarHolder;

    private ScrollView scrollView;

    private EditText name,idCard,qq,address;
    private String name_s,idCard_s,qq_s,address_s;
    private int graduation_i,cities_i;
    private List<NameValuePair> postParameters;
    private List<String> cityId;
    User user = ((App)getApplication()).user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_info_step_initial);

        initView();
        //pullData();
        echoHistory();

//        {
//            CustomSeekBar seekBar = (CustomSeekBar)findViewById(R.id.pro);
//            ValueAnimator animator = new ObjectAnimator().ofFloat(seekBar, "progress", 0.00f,0.33f ).setDuration(1000);
//            animator.setInterpolator(new LinearInterpolator());
//            animator.start();
//        }

    }
    //当avtivity彻底运行起来的时候
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        showProgressbarUngraduated();
    }

    private void showProgressbarUngraduated() {

        final TextView progress = (TextView)findViewById(R.id.progress_text);
        //progress.setText("33%");
        final ImageView progressbar = (ImageView)findViewById(R.id.progressbar);
        progressbar.setVisibility(View.VISIBLE);
        Animation animation = AnimationUtils.loadAnimation(this,R.anim.anim_base_info_progressbar_initial_ug);
        animation.setFillAfter(true);
        progressbar.startAnimation(animation);
        //当全部的布局树都测量完成以后
        final ViewTreeObserver observer = progress.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                ViewTreeObserver obs = progress.getViewTreeObserver();
                //textViewHolder = new TextViewHolder(tv,tv.getWidth(),seekBar.getWidth()-tv.getWidth()-tv.getWidth());
                progressBarHolder = new ProgressBarHolder(progress, progressbar, progress.getWidth(), progressbar.getWidth() * 2 - progress.getWidth());

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    obs.removeOnGlobalLayoutListener(this);
                } else {
                    obs.removeGlobalOnLayoutListener(this);
                }

                ValueAnimator animator = new ObjectAnimator().ofInt(progressBarHolder, "progress", 0, 25).setDuration(750);
                animator.setInterpolator(new LinearInterpolator());
                animator.start();

            }
        });

    }

    private void showProgressbarGraduated() {

        final TextView progress = (TextView)findViewById(R.id.progress_text);
        //progress.setText("25%");
        final ImageView progressbar = (ImageView)findViewById(R.id.progressbar);
        progressbar.setVisibility(View.VISIBLE);
        Animation animation = AnimationUtils.loadAnimation(this,R.anim.anim_base_info_progressbar_initial_j);
        animation.setFillAfter(true);
        progressbar.startAnimation(animation);

        final ViewTreeObserver observer = progress.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                ViewTreeObserver obs = progress.getViewTreeObserver();
                //textViewHolder = new TextViewHolder(tv,tv.getWidth(),seekBar.getWidth()-tv.getWidth()-tv.getWidth());
                progressBarHolder = new ProgressBarHolder(progress, progressbar, progress.getWidth(), progressbar.getWidth() * 2 - progress.getWidth());

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    obs.removeOnGlobalLayoutListener(this);
                } else {
                    obs.removeGlobalOnLayoutListener(this);
                }

                ValueAnimator animator = new ObjectAnimator().ofInt(progressBarHolder, "progress", 0, 25).setDuration(1250);
                animator.setInterpolator(new LinearInterpolator());
                animator.start();

            }
        });

    }

    private void initView() {
        scrollView = (ScrollView)findViewById(R.id.scroll_view);
        scrollView.setVerticalScrollBarEnabled(Constants.SCROLL_VIEW_VERTICAL_SCROLL_BAR_ENANBLE);
        idCard = (EditText)findViewById(R.id.id_card);
        idCard.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        cities = (Spinner)findViewById(R.id.cities);
        TreeMap<String,String> params = new TreeMap<>();
        params.put("api", "allowCitysApi");
        new ZebraTask.Builder(this, params, new CusSuccessListener()
        {
            @Override
            public void onSuccess(TaskResponse response)
            {
                ArrayList<String> citys = new ArrayList<String>();
                if (response.getCode()== Constants.HTTP_REQUEST_SUCCESS){
                    String[] str = response.getData().toString().split(",");
                    citys.add("--请选择--");
                    for (String city:str){
                        // 获取城市名字
                        String regExp = "[\u4E00-\u9FA5]+";
                        Pattern p = Pattern.compile(regExp);
                        Matcher m = p.matcher(city);
                        if (m.find()){
                            String[] strs = city.split("=");
                            citys.add(strs[1]);
                        }
                        // 获取城市id
                        String regex = "[1-9]\\d{4}";
                        Pattern px = Pattern.compile(regex);
                        Matcher mx = px.matcher(city);
                        if (mx.find()){
                            String[] ids = city.split("=");
                            cityId.add(ids[1]);
                        }
                    }

                    ArrayAdapter<String> adapter =new ArrayAdapter<String>(BaseInfoStepInitialActivity.this,R.layout.spinner_center,citys);
                    adapter.setDropDownViewResource(R.layout.spinner_dropdown);
                    cities.setAdapter(adapter);
                    // 初始化服务端数据到app
                    for (int i=0;i<cityId.size();i++){
                        int cityid = Integer.parseInt(cityId.get(i));
                        if (cityid== user.getCityId()){
                            cities.setSelection(i+1);
                            break;
                        }
                    }
                }
            }
        }).build().execute();
//        cities.setAdapter(new ArrayAdapter<String>(this,R.layout.spinner_center,));
//        SpinnerUtils.initCities(this,cities);

        next_page = (TextView)findViewById(R.id.next_page);
        next_page.setOnClickListener(this);

        graduation = (RadioGroup)findViewById(R.id.graduation);
        graduation.check(R.id.ungraduated);
        graduation.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.graduated:
                        //showProgressbarGraduated();
                        break;
                    case R.id.ungraduated:
                        //showProgressbarUngraduated();
                        break;
                }
            }
        });

        name = (EditText)findViewById(R.id.name);
        qq = (EditText)findViewById(R.id.qq);
        address = (EditText)findViewById(R.id.address);
        tips = (TextView)findViewById(R.id.tips);
        cityId = new ArrayList<String>();
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_base_info, menu);
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

                    if ( graduation.getCheckedRadioButtonId() == R.id.graduated) {
                        UmengUtils.baseInfoSubmit(this,null,"已毕业",null,null,null);
                    } else if ( graduation.getCheckedRadioButtonId() == R.id.ungraduated ) {
                        UmengUtils.baseInfoSubmit(this,null,"在校生",null,null,null);
                    }

                    Intent intent = new Intent();
                    HashMap<String,List<NameValuePair>> hashMap = new HashMap<>();
                    hashMap.put("StepInitial",postParameters);
                    ((App)getApplication()).postParameters = hashMap;
                    if (graduation.getCheckedRadioButtonId() == R.id.graduated) {
                        intent.setClass(this,BaseInfoGraduatedStepOneActivity.class);
                        startActivity(intent);
                    } else if (graduation.getCheckedRadioButtonId() == R.id.ungraduated) {
                        SharedPreferences sharedPreferences = getSharedPreferences(Constants.INFO_AUTH_STATE,MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putInt(Constants.JOB_OR_EDUCATION,Constants.IS_EDUCATION_AUTH);
                        editor.commit();
                        intent.setClass(this,BaseInfoUngraduatedStepOneActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(this, "请选择当前状态", Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }

    private boolean checkInput() {

        postParameters = new ArrayList<>();
        name_s = name.getText().toString();
//        if (!GenericUtils.checkChinese(name_s)) {
//            name.requestFocus();
//            name.setError(getString(R.string.name_errors));
//            return false;
//        } else {
            postParameters.add(new BasicNameValuePair("encryptName",name_s));
//        }

        idCard_s = idCard.getText().toString();
        if (!GenericUtils.checkIdCard(idCard_s)) {
            idCard.requestFocus();
            idCard.setError(getString(R.string.id_card_errors));
            return false;
        } else {
            postParameters.add(new BasicNameValuePair("encryptId",idCard_s));
        }

        qq_s = qq.getText().toString();
        if (qq_s.equals("")) {
            qq.requestFocus();
            qq.setError(getString(R.string.no_input_errors));
            return false;
        } else {
            postParameters.add(new BasicNameValuePair("encryptQQ",qq_s));
        }

//        cities_i = cities.getSelectedItemPosition();
//        if (cities_i == 0) {
//            Toast.makeText(this,getString(R.string.city_errors),Toast.LENGTH_LONG).show();
//            return false;
//        } else {
//            if (cities_i == 1) {
//                cities_i = 20256;
//            } else {
//                cities_i = 20001;
//            }
//            postParameters.add(new BasicNameValuePair("cityId",cities_i+""));
//        }

        cities_i = cities.getSelectedItemPosition();
        if (cities_i==0){
            Toast.makeText(this,getString(R.string.city_errors),Toast.LENGTH_LONG).show();
            return false;
        }else {
            cities_i = Integer.parseInt(cityId.get(cities.getSelectedItemPosition()-1));
            L.i("城市",cities_i+"");
            postParameters.add(new BasicNameValuePair("cityId",cities_i+""));
        }

        address_s = address.getText().toString();
        if (address_s.equals("")) {
            address.requestFocus();
            address.setError(getString(R.string.no_input_errors));
            return false;
        } else {
            postParameters.add(new BasicNameValuePair("residenceAddr",address_s));
        }

        if ( graduation.getCheckedRadioButtonId() == R.id.graduated ) {
            graduation_i = 2;
        } else {
            graduation_i = 1;
        }
        postParameters.add(new BasicNameValuePair("academicStatus",graduation_i+""));

        return true;

    }

    public void echoHistory() {

        if (user == null) {
            Toast.makeText(this,getString(R.string.access_server_errors),Toast.LENGTH_LONG).show();
            return;
        } else if (user.getStatusInt() == null) {
        } else if (user.getStatusInt() == Constants.UNPASS) {
            tips.setText(user.getMemo());
            tips.setVisibility(View.VISIBLE);
        }

        L.i("user_name", user.getUserName());
        if (user.getUserName()==null){
            name.setText(PreferenceUtils.getUserName());
        }else {
            name.setText(user.getUserName());
        }
        if (user.getIdentity()==null){
            idCard.setText(PreferenceUtils.getIdCard());
        }else {
            idCard.setText(user.getIdentity());
        }
        qq.setText(user.getQQ());
//        if (user.getCityId()!=null){
//            int id = user.getCityId();
//            L.i("cityids---",cityId.toString());
//            for (int i=0;i<cityId.size();i++){
//                int cityid = Integer.parseInt(cityId.get(i));
//                if (cityid== id){
//                    cities.setSelection(cityId.size()-i);
//                }
//            }
//            switch (user.getCityId()) {
//                case 20001:
//                    cities.setSelection(2);
//                    break;
//                case 20256:
//                    cities.setSelection(1);
//                    break;
//                default:
//                    cities.setSelection(0);
//                    break;
//            }
//        }

        address.setText(user.getResidenceAddr());
        if (user.getAcademicStatus()!=null){
            L.i("graduate",PreferenceUtils.getGraduation()+"");
            if (user.getAcademicStatus() == 2){
                graduation.check(R.id.graduated);
            }
        }else {
            if (PreferenceUtils.getGraduation()==2){
                graduation.check(R.id.graduated);
            }else {
                graduation.check(R.id.ungraduated);
            }
        }
    }

}