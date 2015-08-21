package com.wezebra.zebraking.ui.installment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.wezebra.zebraking.R;
import com.wezebra.zebraking.ui.BaseActivity;

public class ReFillActivity extends BaseActivity implements View.OnClickListener{

    private String title;
    private int redirect;

    private TextView submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_re_fill);

        title = getIntent().getExtras().getString("title");
        redirect = getIntent().getExtras().getInt("redirect");

        submit = (TextView)findViewById(R.id.submit);
        submit.setOnClickListener(this);

    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_re_fill, menu);
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
    //根据传过来的标识符来判断应该修改那个类型
    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (redirect) {
            case 1:
                intent.setClass(this,BaseInfoStepInitialActivity.class);
                break;
            case 2:
                intent.setClass(this,IdentificationActivity.class);
                break;
            case 3:
                intent.setClass(this,JobInfoActivity.class);
//                intent.setClass(this,JobInfoChoiceActivity.class);
                break;
            case 4:
                intent.setClass(this,EducationAuthActivity.class);
                break;
            case 5:
                intent.setClass(this,ContactsAuthActivity.class);
                break;
            case 6:
                intent.setClass(this,IncomeAuthActivity.class);
                break;
            case 7:
                intent.setClass(this,SocialSecurityAuthActivity.class);
                break;
            case 8:
                intent.setClass(this,PublicAccFundsAuthActivity.class);
                break;
            case 9:
                intent.setClass(this,PhoneAuthStepOneActivity.class);
                break;
            case 10:
                intent.setClass(this,CompanyEmailAuth.class);
                break;
        }
        startActivity(intent);
        this.finish();
    }

}
