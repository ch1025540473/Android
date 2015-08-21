package com.wezebra.zebraking.ui.installment;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.wezebra.zebraking.R;
import com.wezebra.zebraking.behavior.HttpAsyncTask;
import com.wezebra.zebraking.behavior.SpinnerUtils;
import com.wezebra.zebraking.http.CusSuccessListener;
import com.wezebra.zebraking.http.TaskResponse;
import com.wezebra.zebraking.http.ZebraTask;
import com.wezebra.zebraking.ui.BaseActivity;
import com.wezebra.zebraking.util.Constants;
import com.wezebra.zebraking.util.GenericUtils;
import com.wezebra.zebraking.util.PreferenceUtils;
import com.wezebra.zebraking.util.UmengUtils;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class AssessActivity extends BaseActivity implements View.OnClickListener {

    private RadioGroup graduation,employment,isFound;
    private LinearLayout rentLayout,rentModeLayout,rentBudgetLayout,houseTypeLayout,rentAreaLayout,incomeLayout;

    private TextView submit,city_desc;

    private Spinner diploma,income,howToPay,budget,houseType;

    private ScrollView scrollView;

    private EditText idCard,name,rentFeeMonthly,introducer,rentArea;
    private String name_s,id_card_s,rent_fee_monthly_s,introducer_s,rent_area_s;
    private int diploma_i,graduation_i,employment_i,income_i,isFound_i ,how_to_pay_i,budget_i,house_type_i;
    private List<NameValuePair> postParameters;

    public int qualificationStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assess);

        intiView();

    }

    private void intiView() {

        scrollView = (ScrollView)findViewById(R.id.scroll_view);
        scrollView.setVerticalScrollBarEnabled(Constants.SCROLL_VIEW_VERTICAL_SCROLL_BAR_ENANBLE);

        idCard = (EditText)findViewById(R.id.id_card);
        idCard.setTransformationMethod(HideReturnsTransformationMethod.getInstance());

        submit = (TextView)findViewById(R.id.submit);

        graduation = (RadioGroup)findViewById(R.id.graduation);
        employment = (RadioGroup)findViewById(R.id.employment);
        isFound = (RadioGroup)findViewById(R.id.isFound);
        rentLayout = (LinearLayout)findViewById(R.id.rent_layout);
        rentModeLayout = (LinearLayout)findViewById(R.id.rent_mode_layout);
        rentBudgetLayout = (LinearLayout)findViewById(R.id.rent_budget_layout);
        houseTypeLayout = (LinearLayout)findViewById(R.id.house_type_layout);
        rentAreaLayout = (LinearLayout)findViewById(R.id.rent_area_layout);
        incomeLayout = (LinearLayout)findViewById(R.id.income_layout);

        graduation.check(R.id.graduated);
        employment.check(R.id.employed);
        isFound.check(R.id.found);
        rentBudgetLayout.setVisibility(View.GONE);
        houseTypeLayout.setVisibility(View.GONE);
        rentAreaLayout.setVisibility(View.GONE);
        isFound.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.found:
                        rentLayout.setVisibility(View.VISIBLE);
                        rentModeLayout.setVisibility(View.VISIBLE);
                        rentBudgetLayout.setVisibility(View.GONE);
                        houseTypeLayout.setVisibility(View.GONE);
                        rentAreaLayout.setVisibility(View.GONE);
                        break;
                    case R.id.unFound:
                        rentLayout.setVisibility(View.GONE);
                        rentModeLayout.setVisibility(View.GONE);
                        rentBudgetLayout.setVisibility(View.VISIBLE);
                        houseTypeLayout.setVisibility(View.VISIBLE);
                        rentAreaLayout.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });

        employment.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId) {
                    case R.id.employed:
                        incomeLayout.setVisibility(View.VISIBLE);
                        break;
                    case R.id.unEmployed:
                        incomeLayout.setVisibility(View.INVISIBLE);
                        break;
                }

            }
        });

        submit.setOnClickListener(this);

        //Init Spinner
        diploma = (Spinner)findViewById(R.id.diploma);
        income = (Spinner)findViewById(R.id.income);
        howToPay = (Spinner)findViewById(R.id.how_to_pay);
        budget = (Spinner)findViewById(R.id.budget);
        houseType = (Spinner)findViewById(R.id.house_type);
        SpinnerUtils.initDiploma(this, diploma);
        SpinnerUtils.initIncome(this, income);
        SpinnerUtils.initHowToPay(this, howToPay);
        SpinnerUtils.initBudget(this, budget);
        SpinnerUtils.initHouseType(this,houseType);

        //关联输入项
        name = (EditText)findViewById(R.id.name);
        rentFeeMonthly = (EditText)findViewById(R.id.rent_fee_monthly);
        introducer = (EditText)findViewById(R.id.introducer);
        rentArea = (EditText)findViewById(R.id.rent_area);

        city_desc = (TextView) findViewById(R.id.city_desc);
        TreeMap<String,String> params = new TreeMap<>();
        params.put("api","adMasterApi");
        params.put("code","0814");
        new ZebraTask.Builder(this, params, new CusSuccessListener()
        {
            @Override
            public void onSuccess(TaskResponse response)
            {
                ArrayList<String> citys = new ArrayList<String>();
                if (response.getCode()== Constants.HTTP_REQUEST_SUCCESS){
                    city_desc.setText(response.getData().toString());
                }
            }
        }).build().execute();
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_assess, menu);
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
                Log.i("submit", ".");
                if (checkInput()) {

                    Bundle bundle = new Bundle();
                    bundle.putString("name", name_s);
                    bundle.putInt("diploma", diploma_i);
                    bundle.putInt("employment", employment_i);
                    bundle.putInt("isFound", isFound_i);
                    bundle.putInt("income", income_i);
                    bundle.putInt("how_to_pay", how_to_pay_i);
                    bundle.putBoolean("doFinish", true);
                    initPersonInfo(); // 初始化用户信息
                    Intent intent = new Intent();
                    intent.putExtras(bundle);
                    intent.setClass(this, AssessCountdownActivity.class);
//                    intent.setClass(this,SubmitInstallmentApplicationStepOneActivity.class);

                    postParameters.add(new BasicNameValuePair("api", "userAssessInfo"));
                    HttpAsyncTask httpAsyncTask = HttpAsyncTask.getInstance(AssessActivity.this, PreferenceUtils.getUserId()+"", postParameters);
                    httpAsyncTask.assessPost(intent);

                    String graduation_s = "";
                    if (graduation.getCheckedRadioButtonId() == R.id.graduated) {
                        graduation_s = "已毕业";
                    } else if (graduation.getCheckedRadioButtonId() == R.id.ungraduated) {
                        graduation_s = "未毕业";
                    }
                    String employment_s = "";
                    if (employment.getCheckedRadioButtonId() == R.id.employed) {
                        graduation_s = "已工作";
                    } else if (employment.getCheckedRadioButtonId() == R.id.unEmployed) {
                        graduation_s = "未工作";
                    }
                    Log.i("diploma.toString",diploma.getSelectedItem().toString());
                    UmengUtils.assessSubmit(this, diploma.getSelectedItem().toString(),graduation_s,employment_s,income.getSelectedItem().toString(),rentFeeMonthly.getText().toString(),howToPay.getSelectedItem().toString(),budget.getSelectedItem().toString() );

                }

//                AsyncTask<String,Integer,String> asyncTask = new AsyncTask<String, Integer, String>() {
//
//                    @Override
//                    protected String doInBackground(String... params) {
//
//                        String resultStr = "";
//                        try {
//                            resultStr = HttpClientImp.INSTANCE.postFileForString("http://192.168.1.136:8080/zf/app/file/upload",null , GenericUtils.getBytesFromBitmap(((BitmapDrawable) getResources().getDrawable(R.drawable.advertise)).getBitmap()), 0);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                        Log.i("upload", resultStr);
//                        return null;
//                    }
//
//                };
//                asyncTask.execute();

                break;
        }

    }

    private void initPersonInfo(){
        PreferenceUtils.setUserName(name_s);
        PreferenceUtils.setIdCard(id_card_s);
        PreferenceUtils.setEducational(diploma_i); // 学历
        PreferenceUtils.setGraduation(graduation_i);
        PreferenceUtils.setJobStatus(employment_i);
        PreferenceUtils.setMonthIncome(income_i);
        PreferenceUtils.setMonthRent(rent_fee_monthly_s);
        PreferenceUtils.setMonthRentWay(how_to_pay_i);
        PreferenceUtils.setFourStatus(0);
    }
    private boolean checkInput() {

        postParameters = new ArrayList<>();

        name_s = name.getText().toString();
//        if (!GenericUtils.checkChinese(name_s)) {
//            name.requestFocus();
//            name.setError(getResources().getString(R.string.name_errors));
//            return false;
//        } else {
            postParameters.add(new BasicNameValuePair("userName",name_s));
//        }

        id_card_s = idCard.getText().toString();
        if (!GenericUtils.checkIdCard(id_card_s)) {
            idCard.requestFocus();
            idCard.setError(getResources().getString(R.string.id_card_errors));
            return false;
        } else {
            postParameters.add(new BasicNameValuePair("userIdentity",id_card_s));
        }

        diploma_i = diploma.getSelectedItemPosition();
        if (diploma_i == 1) {
            diploma_i = -1;
        } else if (diploma_i > 1) {
            diploma_i = diploma_i - 1;
        }
        if (diploma_i == 0) {
            Toast.makeText(this,getResources().getString(R.string.diploma_errors),Toast.LENGTH_LONG).show();
            return false;
        } else {
            postParameters.add(new BasicNameValuePair("education",diploma_i+""));
        }

        if (graduation.getCheckedRadioButtonId() == R.id.graduated) {
            graduation_i = 2;
        } else {
            graduation_i = 1;
        }
        postParameters.add(new BasicNameValuePair("academicStatus",graduation_i+""));

        if (employment.getCheckedRadioButtonId() == R.id.employed) {
            employment_i = 1;
        } else {
            employment_i = 2;
        }
        postParameters.add(new BasicNameValuePair("jobStatus",employment_i+""));

        income_i = income.getSelectedItemPosition();
        if ( (employment_i == 1) && (income_i == 0) ) {
            Toast.makeText(this,getResources().getString(R.string.income_errors),Toast.LENGTH_LONG).show();
            return false;
        }
        postParameters.add(new BasicNameValuePair("salary",income_i+""));

        if (isFound.getCheckedRadioButtonId() == R.id.found) {

            isFound_i = 1;
            postParameters.add(new BasicNameValuePair("houseFoundType",isFound_i+""));
            if (!GenericUtils.checkDecimals(rentFeeMonthly.getText().toString())) {
                rentFeeMonthly.requestFocus();
                rentFeeMonthly.setError(getResources().getString(R.string.rent_fee_monthly_errors));
                return false;
            } else {
                rent_fee_monthly_s = rentFeeMonthly.getText().toString();
                postParameters.add(new BasicNameValuePair("monthlyFee",rent_fee_monthly_s));
            }
            how_to_pay_i = howToPay.getSelectedItemPosition();
            if (how_to_pay_i == 0) {
                Toast.makeText(this,getResources().getString(R.string.how_to_pay_errors),Toast.LENGTH_LONG).show();
                return false;
            } else if (how_to_pay_i == 1) {
                how_to_pay_i = 3;
            } else {
                how_to_pay_i = 6;
            }
            postParameters.add(new BasicNameValuePair("stagingDay",how_to_pay_i+""));
            introducer_s = introducer.getText().toString();
            if ( (!introducer_s.equals("")) && (!GenericUtils.checkMobile(introducer_s)) ) {
                introducer.requestFocus();
                introducer.setError(getResources().getString(R.string.introducer_errors));
                return false;
            }
            postParameters.add(new BasicNameValuePair("referee",introducer_s));

        } else {

            isFound_i = 2;
            postParameters.add(new BasicNameValuePair("houseFoundType",isFound_i+""));
            budget_i = budget.getSelectedItemPosition();
            if (budget_i == 0) {
                Toast.makeText(this,getString(R.string.budget_errors),Toast.LENGTH_LONG).show();
                return false;
            } else {
                budget_i = budget_i - 1;
                postParameters.add(new BasicNameValuePair("houseCal",budget_i+""));
            }
            house_type_i = houseType.getSelectedItemPosition();
            if (house_type_i == 0) {
                Toast.makeText(this,getString(R.string.house_type_errors),Toast.LENGTH_LONG).show();
                return false;
            } else {
                switch (house_type_i) {
                    case 1:
                        house_type_i = 99;
                        break;
                    case 2:
                        house_type_i = 100;
                        break;
                    case 3:
                        house_type_i = 200;
                        break;
                    case 4:
                        house_type_i = 300;
                        break;
                    case 5:
                        house_type_i = 400;
                        break;
                    case 6:
                        house_type_i = 0;
                        break;
                }
                postParameters.add(new BasicNameValuePair("roomType",house_type_i+""));
            }
            rent_area_s = rentArea.getText().toString();
            if (rent_area_s.equals("")) {
                rentArea.requestFocus();
                rentArea.setError(getString(R.string.rent_area_errors));
                return false;
            } else {
                postParameters.add(new BasicNameValuePair("region", rent_area_s));
            }
            introducer_s = introducer.getText().toString();
            if ( (!introducer_s.equals("")) && (!GenericUtils.checkMobile(introducer_s)) ) {
                introducer.requestFocus();
                introducer.setError(getResources().getString(R.string.introducer_errors));
                return false;
            } else {
                postParameters.add(new BasicNameValuePair("referee",introducer_s));
            }

        }

        return true;

    }

}
