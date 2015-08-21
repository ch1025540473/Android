package com.wezebra.zebraking.ui.myaccount;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.wezebra.zebraking.R;
import com.wezebra.zebraking.model.Bill;
import com.wezebra.zebraking.ui.BaseActivity;
import com.wezebra.zebraking.util.CommonUtils;

import utils.LlPayHelper;

public class PersonLLInfoCertificate extends BaseActivity implements View.OnClickListener{


    private EditText wezebra_et_id_card_code;

    private EditText wezebra_tv_cardhodler_name;

    private Button wezebra_first_lianlian_certificate;

    private Bill bill ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_llpay_info_certificate);
        initView();
        initData();
        initListener();
    }

    public void initView(){
        //持卡人姓名
        wezebra_tv_cardhodler_name=(EditText)findViewById(R.id.wezebra_tv_cardhodler_name);

        wezebra_et_id_card_code=(EditText)findViewById(R.id.wezebra_et_id_card_code);
        wezebra_first_lianlian_certificate=(Button)findViewById(R.id.wezebra_first_lianlian_certificate);
    }

    public void initData(){
        bill=(Bill)getIntent().getSerializableExtra("bill");
    }


    public void initListener(){
        wezebra_first_lianlian_certificate.setOnClickListener(this);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_person_llinfo_certificate, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Log.e("item",id+"");
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.wezebra_first_lianlian_certificate:
                //在这个之前需要对输入得结果进行验证，银行卡号的的验证和姓名的验证
                if(!CommonUtils.checkChinese(wezebra_tv_cardhodler_name.getText().toString())){
                    Toast.makeText(PersonLLInfoCertificate.this,"持卡人姓名只能是汉字呕！",Toast.LENGTH_LONG).show();
                    break;
                }

                if(!CommonUtils.checkIdCard(wezebra_et_id_card_code.getText().toString())){
                    Toast.makeText(PersonLLInfoCertificate.this,"请检查身份证号是否正确",Toast.LENGTH_LONG).show();
                    break;
                }
                LlPayHelper.getInstance().pay(bill,
                        this,
                        wezebra_tv_cardhodler_name.getText().toString(),
                        wezebra_et_id_card_code.getText().toString());
                break;
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //如果是menu，则屏幕
        if(keyCode ==  KeyEvent.KEYCODE_MENU){
           return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
