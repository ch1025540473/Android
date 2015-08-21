package com.wezebra.zebraking.ui.installment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.wezebra.zebraking.App;
import com.wezebra.zebraking.R;
import com.wezebra.zebraking.behavior.HttpAsyncTask;
import com.wezebra.zebraking.behavior.SpinnerUtils;
import com.wezebra.zebraking.http.CusSuccessListener;
import com.wezebra.zebraking.http.JacksonUtils;
import com.wezebra.zebraking.http.TaskResponse;
import com.wezebra.zebraking.http.ZebraTask;
import com.wezebra.zebraking.http.data.BankBrachData;
import com.wezebra.zebraking.ui.BaseActivity;
import com.wezebra.zebraking.ui.fragment.AddressDialogFragment;
import com.wezebra.zebraking.ui.fragment.BankDialogFragment;
import com.wezebra.zebraking.util.Constants;
import com.wezebra.zebraking.util.GenericUtils;
import com.wezebra.zebraking.util.L;
import com.wezebra.zebraking.util.PreferenceUtils;
import com.wezebra.zebraking.util.UmengUtils;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LandlordAuthActivity extends BaseActivity implements View.OnClickListener,AddressDialogFragment.OnCitySelectedListener {

    private SharedPreferences entrance_flag;
    private int entrance;
    private TextView submit;

    public Spinner howToPay;
    public TextView attribution;
    public EditText rentFeeMonthly,cardNum,name,idCard,phone;

    public int howToPay_i,city_i;
    public String rentFeeMonthly_s,cardNum_s,name_s,idCard_s,phone_s,bank;

    public TextView tips;
    public int status;
    public String memo;
    public EditText branch_bank_name;
    public ArrayAdapter adapter;
    public BankDialogFragment bankDialogFragment;
    public boolean is_show=true;
    public int max_len;
    public static final int  REQUEST = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landlord_auth);
        initView();
        echoHistory();
    }

    private void initView() {

        ScrollView scrollView = (ScrollView)findViewById(R.id.scroll_view);
        scrollView.setVerticalScrollBarEnabled(Constants.SCROLL_VIEW_VERTICAL_SCROLL_BAR_ENANBLE);

        submit = (TextView)findViewById(R.id.submit);
        submit.setOnClickListener(this);

        //Init Spinner
        howToPay = (Spinner)findViewById(R.id.how_to_pay);
        SpinnerUtils.initHowToPay(this,howToPay);

        attribution = (TextView)findViewById(R.id.attribution);
        attribution.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogFragment();
            }
        });

        rentFeeMonthly = (EditText)findViewById(R.id.rent_fee_monthly);
        cardNum = (EditText)findViewById(R.id.card_num);
        name = (EditText)findViewById(R.id.name);
//        idCard = (EditText)findViewById(R.id.id_card);
//        idCard.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        phone = (EditText)findViewById(R.id.phone);
        city_i = 0;

        tips = (TextView)findViewById(R.id.tips);

        branch_bank_name= (EditText) findViewById(R.id.branch_bank_name);
//        branch_bank_name.setOnClickListener(this);
//        branch_bank_name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (hasFocus){
//                    L.i("test","11111111111");
//                    Intent mIntent = new Intent(LandlordAuthActivity.this, BranchBankChoiceActivity.class);
//                    Bundle mBundle = new Bundle();
//
//
//                    mBundle.putString("account", cardNum.getText().toString());
//                    mBundle.putString("bank", branch_bank_name.getText().toString());
//                    mBundle.putString("cityId",city_i+"");
//
//                    mIntent.putExtras(mBundle);
//                    startActivityForResult(mIntent, REQUEST);
//                }
//
//            }
//        });

        branch_bank_name.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                cardNum_s = cardNum.getText().toString();
                if (cardNum_s.equals("")) {
                    cardNum.requestFocus();
                    cardNum.setError(getString(R.string.bank_card_errors));
                    return true;
                }
                if (city_i == 0) {
                    Toast.makeText(LandlordAuthActivity.this, getString(R.string.attibution_errors), Toast.LENGTH_LONG).show();
                    return false;
                }

                if (event.getAction()==MotionEvent.ACTION_DOWN){
                    Intent mIntent = new Intent(LandlordAuthActivity.this, BranchBankChoiceActivity.class);
                    Bundle mBundle = new Bundle();
                    mBundle.putString("account", cardNum.getText().toString());
                    mBundle.putString("bank", branch_bank_name.getText().toString());
                    mBundle.putString("cityId",city_i+"");

                    mIntent.putExtras(mBundle);
                    startActivityForResult(mIntent, REQUEST);
                }
                return false;
            }
        });

//        adapter = new ArrayAdapter<String>(LandlordAuthActivity.this, R.layout.support_simple_spinner_dropdown_item, new String[]{"12","123","123445"});
//        branch_bank_name.setAdapter(adapter);
//        branch_bank_name.setThreshold(1);

    }
//
//    public void getMin(int len){
//        int a;
//        a = len;
//        if (len<=max_len){
//            is_show = false;
//            max_len = len;
//        }
//        if (len > max_len){
//            is_show = true;
//        }
//    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==REQUEST && (resultCode==0 || resultCode==1)){
            branch_bank_name.setText(data.getStringExtra("bank_name"));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        is_show = false;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.submit:

                rentFeeMonthly_s = rentFeeMonthly.getText().toString();
                howToPay_i = howToPay.getSelectedItemPosition();
                cardNum_s = cardNum.getText().toString();
                name_s = name.getText().toString();
//                idCard_s = idCard.getText().toString();
                phone_s = phone.getText().toString();
                bank = branch_bank_name.getText().toString().trim();

                if (rentFeeMonthly_s.equals("")) {
                    rentFeeMonthly.requestFocus();
                    rentFeeMonthly.setError(getString(R.string.rent_fee_monthly_errors));
                    return;
                }
                switch (howToPay_i) {
                    case 0:
                        Toast.makeText(this, getString(R.string.how_to_pay_errors), Toast.LENGTH_LONG).show();
                        return;
                    case 1:
                        howToPay_i = 3;
                        break;
                    case 2:
                        howToPay_i = 6;
                        break;
                }
                if (cardNum_s.equals("")) {
                    cardNum.requestFocus();
                    cardNum.setError(getString(R.string.bank_card_errors));
                    return;
                }
                if (city_i == 0) {
                    Toast.makeText(this, getString(R.string.attibution_errors), Toast.LENGTH_LONG).show();
                    return;
                }
                if (name_s.equals("")) {
                    name.requestFocus();
                    name.setError(getString(R.string.landlord_name_errors));
                    return;
                }
                if (bank.equals("")) {
                    branch_bank_name.requestFocus();
                    branch_bank_name.setError(getString(R.string.branch_name));
                    return;
                }
//                if ( !GenericUtils.checkIdCard(idCard_s) || (idCard_s.equals("")) ) {
//                    idCard.requestFocus();
//                    idCard.setError(getString(R.string.id_card_errors));
//                    return;
//                }
                if (!GenericUtils.checkMobile(phone_s) || phone_s.equals("")) {
                    phone.requestFocus();
                    phone.setError(getString(R.string.mobile_phone_format_errors));
                    return;
                }

                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putInt(Constants.INFO_SUBMIT, Constants.LANDLORD_INFO_SUBMIT);
                intent.putExtra(Constants.INFO_SUBMIT, bundle);

                entrance_flag = getSharedPreferences(Constants.ENTRANCE_FLAG, MODE_PRIVATE);
                entrance = entrance_flag.getInt(Constants.ENTRANCE_FLAG, Constants.DEFAULT_ENTRANCE);
                switch (entrance) {
                    case Constants.IS_FROM_STEP_TWO:
                        intent.setClass(this, SubmitInstallmentApplicationStepTwoActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        break;
                    case Constants.IS_FROM_PROFILE:
                        intent.setClass(this, PersonalProfileActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        break;
                }

                List<NameValuePair> postParameters = new ArrayList<>();
                postParameters.add(new BasicNameValuePair("monthlyFee", rentFeeMonthly_s));
                postParameters.add(new BasicNameValuePair("bank", bank + ""));

                postParameters.add(new BasicNameValuePair("stagingDay", howToPay_i + ""));
                postParameters.add(new BasicNameValuePair("account", cardNum_s));
                postParameters.add(new BasicNameValuePair("cityId", city_i + ""));
                postParameters.add(new BasicNameValuePair("name", name_s));
//                postParameters.add(new BasicNameValuePair("idCard", idCard_s));
                postParameters.add(new BasicNameValuePair("phone", phone_s));
                postParameters.add(new BasicNameValuePair("orderCode", App.INSTANCE.getActiveApplyData().getOrderCode() + ""));
                postParameters.add(new BasicNameValuePair("api", "payeeInfoSave"));
                HttpAsyncTask httpAsyncTask = HttpAsyncTask.getInstance(LandlordAuthActivity.this, PreferenceUtils.getUserId() + "", postParameters);
                httpAsyncTask.postWithMultiEntrance(intent);

                UmengUtils.landlordInfoSubmit(this, rentFeeMonthly.getText().toString(), howToPay.getSelectedItem().toString());

                break;

        }
    }

    private void showDialogFragment() {

        AddressDialogFragment dialog = new AddressDialogFragment();
        dialog.show(getSupportFragmentManager(), "AddressDialogFragment");

    }

    @Override
    public void onCitySelected(int cityId, String cityName) {
        attribution.setText(cityName);
        attribution.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        attribution.setTextColor(Constants.TEXT_COLOR_DEFAULT);
        this.city_i = cityId;
    }

    public void echoHistory() {

        List<NameValuePair> pullParameters = new ArrayList<>();
        pullParameters.add(new BasicNameValuePair("api", "querybyOrderCode"));
        pullParameters.add(new BasicNameValuePair("orderCode", App.INSTANCE.getActiveApplyData().getOrderCode() + ""));
        HttpAsyncTask.getInstance(this, PreferenceUtils.getUserId() + "", pullParameters).pullLandlordInfo();

    }


}
