package com.wezebra.zebraking.ui.installment;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.wezebra.zebraking.R;
import com.wezebra.zebraking.http.CusSuccessListener;
import com.wezebra.zebraking.http.TaskResponse;
import com.wezebra.zebraking.http.ZebraTask;
import com.wezebra.zebraking.ui.BaseActivity;
import com.wezebra.zebraking.ui.fragment.BankDialogFragment;
import com.wezebra.zebraking.ui.fragment.CompanyEmailCompleteFragment;
import com.wezebra.zebraking.ui.fragment.CompanyEmailInputFragment;
import com.wezebra.zebraking.util.CommonUtils;
import com.wezebra.zebraking.util.Constants;
import com.wezebra.zebraking.util.L;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by admin on 2015/7/31.
 */
public class BranchBankChoiceActivity extends BaseActivity {

    private static final int RESULT_OK_TEXT = 1;
    private EditText search_bank;
    private String account,bank,cityId;
    private FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    BankDialogFragment bankDialogFragment;
    CompanyEmailInputFragment inputFragment;
    FrameLayout not_data_info;
    TextView cancle,not_data_info_text,choice_ok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.branch_bank_choice);
        init();
        fragmentManager = getFragmentManager();
//        cancle = (TextView) findViewById(R.id.cancle);
//        cancle.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                setResult(RESULT_OK);
//                finish();
//            }
//        });
    }

    private void init() {
        search_bank = (EditText) findViewById(R.id.search_bank);
        search_bank.addTextChangedListener(myTextWatcher);
        Bundle bundle = getIntent().getExtras();
        account = bundle.getString("account");
        bank = bundle.getString("bank");
        cityId = bundle.getString("cityId");
        not_data_info = (FrameLayout) findViewById(R.id.not_data_info);
        not_data_info_text = (TextView) findViewById(R.id.not_data_info_text);
        choice_ok = (TextView) findViewById(R.id.choice_ok);
        choice_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = search_bank.getText().toString().trim();
                if (!text.equals("")){
                    Intent intent = new Intent();
                    intent.putExtra("bank_name",text);
                    setResult(RESULT_OK_TEXT, intent);
                    finish();
                }
            }
        });

//        cancle = (TextView) findViewById(R.id.cancle);
//        cancle.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                BranchBankChoiceActivity.this.setResult(2);
//                BranchBankChoiceActivity.this.finish();
//            }
//        });
    }

    TextWatcher myTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            int len = s.length();
            if (len>0 && CommonUtils.checkChinese(s.toString())){
                Map<String,String> params = new TreeMap<>();
                params.put("api", "bankSuggestQuery");
                params.put("account",account);
                params.put("bank",search_bank.getText().toString());
                params.put("cityId", cityId);

                L.i("params",params.toString());
                new ZebraTask.Builder(BranchBankChoiceActivity.this, params, new CusSuccessListener()
                {
                    @Override
                    public void onSuccess(TaskResponse response)
                    {
                        L.i("bankdatacode",response.getCode()+"");
                        if (Constants.HTTP_REQUEST_SUCCESS == response.getCode()){
                            ArrayList<String> res = new ArrayList<String>();
                            String data = response.getData().toString();
                            String[] arrs = data.split(",");
                            for (String arr : arrs){
                                String regExp = "[\u4E00-\u9FA5]+";
                                Pattern p = Pattern.compile(regExp);
                                Matcher m = p.matcher(arr);
                                if (m.find()){
                                    String[] str = arr.split("=");
                                    res.add(str[1]);

                                    L.i("bankdata", str[1] + "");
                                }
                            }
                            if (res.size()!=0){
                                not_data_info_text.setVisibility(View.INVISIBLE);
                                fragmentTransaction = fragmentManager.beginTransaction();
                                bankDialogFragment = new BankDialogFragment(res);
                                fragmentTransaction.replace(R.id.not_data_info, bankDialogFragment);
                                fragmentTransaction.show(bankDialogFragment);
                                fragmentTransaction.commit();
                            }
                        }
                    }
                }).build().execute();
            }
        }
    };

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        Intent intent = new Intent(BranchBankChoiceActivity.this,LandlordAuthActivity.class);
        BranchBankChoiceActivity.this.setResult(RESULT_OK,intent);
        BranchBankChoiceActivity.this.finish();
    }
}
