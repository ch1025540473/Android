package com.wezebra.zebraking.ui.installment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.wezebra.zebraking.R;
import com.wezebra.zebraking.behavior.HttpAsyncTask;
import com.wezebra.zebraking.behavior.SpinnerUtils;
import com.wezebra.zebraking.ui.BaseActivity;
import com.wezebra.zebraking.util.Base64Digest;
import com.wezebra.zebraking.util.Constants;
import com.wezebra.zebraking.util.GenericUtils;
import com.wezebra.zebraking.util.PreferenceUtils;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class ContactsAuthActivity extends BaseActivity implements View.OnClickListener {

    private TextView submit;
    public TextView tips;

    private SharedPreferences entrance_flag;
    private int entrance;

    public Spinner familyRelation,friendRelation;
    public EditText familyName,familyPhone,friendName,friendPhone;

    public int id1,id2;
    public int familyRelation_i,friendRelation_i,status;
    public String familyName_s,familyPhone_s,friendName_s,friendPhone_s,tips_s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_auth);

        initView();
        echoHistory();
    }

    private void initView() {

        submit = (TextView)findViewById(R.id.submit);
        submit.setOnClickListener(this);

        familyRelation = (Spinner)findViewById(R.id.family_relation);
        friendRelation = (Spinner)findViewById(R.id.friend_relation);
        SpinnerUtils.initFamilyRelation(this, familyRelation);
        SpinnerUtils.initFriendRelation(this, friendRelation);

        familyName = (EditText)findViewById(R.id.family_name);
        familyPhone = (EditText)findViewById(R.id.family_phone);
        friendName = (EditText)findViewById(R.id.friend_name);
        friendPhone = (EditText)findViewById(R.id.friend_phone);
        tips = (TextView)findViewById(R.id.tips);

        id1 = 0;
        id2 = 0;

    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_contacts_auth, menu);
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

                familyRelation_i = familyRelation.getSelectedItemPosition();
                switch (familyRelation_i) {
                    case 1:
                        break;
                    case 2:
                        familyRelation_i = 8;
                        break;
                }
                friendRelation_i = friendRelation.getSelectedItemPosition();
                friendRelation_i = friendRelation_i+3;
                familyName_s = familyName.getText().toString();
                familyPhone_s = familyPhone.getText().toString();
                friendName_s = friendName.getText().toString();
                friendPhone_s = friendPhone.getText().toString();
                if (familyRelation_i == 0) {
                    Toast.makeText(this,getString(R.string.family_relation_select_errors),Toast.LENGTH_LONG).show();
                    return;
                }
                if (familyName_s.equals("")) {
                    familyName.requestFocus();
                    familyName.setError(getString(R.string.family_name_errors));
                    return;
                }
                if ( !GenericUtils.checkMobile(familyPhone_s) || (familyPhone_s.equals("")) ) {
                    familyPhone.requestFocus();
                    familyPhone.setError(getString(R.string.family_phone_errors));
                    return;
                }
                if (friendRelation_i == 3) {
                    Toast.makeText(this,getString(R.string.friend_relation_select_errors),Toast.LENGTH_LONG).show();
                    return;
                }
                if (friendName_s.equals("")) {
                    friendName.requestFocus();
                    friendName.setError(getString(R.string.friend_name_errors));
                    return;
                }
                if ( !GenericUtils.checkMobile(friendPhone_s) || (friendPhone_s.equals("")) ) {
                    friendPhone.requestFocus();
                    friendPhone.setError(getString(R.string.friend_phone_errors));
                    return;
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("提示");
                builder.setMessage("请确认所填信息正确无误，完成后将暂时无法更改哦");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                        Intent intent = new Intent();
                        Bundle bundle = new Bundle();
                        bundle.putInt(Constants.INFO_SUBMIT,Constants.CONTACTS_INFO_SUBMIT);
                        intent.putExtra(Constants.INFO_SUBMIT, bundle);

                        entrance_flag = getSharedPreferences(Constants.ENTRANCE_FLAG,MODE_PRIVATE);
                        entrance = entrance_flag.getInt(Constants.ENTRANCE_FLAG,Constants.DEFAULT_ENTRANCE);
                        switch (entrance) {
                            case Constants.IS_FROM_APPLICATION:
                                intent.setClass(ContactsAuthActivity.this,SubmitInstallmentApplicationStepOneActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                break;
                            case Constants.IS_FROM_PROFILE:
                                intent.setClass(ContactsAuthActivity.this,PersonalProfileActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                break;
                        }

                        List<NameValuePair> postParameters = new ArrayList<>();
                        if (id1 != 0) {
                            postParameters.add(new BasicNameValuePair("id1",id1+""));
                        }
                        if (id2 != 0) {
                            postParameters.add(new BasicNameValuePair("id2",id2+""));
                        }
                        postParameters.add(new BasicNameValuePair("encryptName1", Base64Digest.encode(familyName_s)));
                        postParameters.add(new BasicNameValuePair("encryptMobile1",Base64Digest.encode(familyPhone_s)));
                        postParameters.add(new BasicNameValuePair("relationship1", familyRelation_i+""));
                        postParameters.add(new BasicNameValuePair("encryptName2", Base64Digest.encode(friendName_s)));
                        postParameters.add(new BasicNameValuePair("encryptMobile2",Base64Digest.encode(friendPhone_s)));
                        postParameters.add(new BasicNameValuePair("relationship2", friendRelation_i+""));
                        postParameters.add(new BasicNameValuePair("api", "authContactApi"));
                        HttpAsyncTask httpAsyncTask = HttpAsyncTask.getInstance(ContactsAuthActivity.this, PreferenceUtils.getUserId()+"", postParameters);
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

                break;
        }

    }

    public void echoHistory() {

        List<NameValuePair> pullParameters = new ArrayList<>();
        pullParameters.add(new BasicNameValuePair("api", "authContactApi"));
        pullParameters.add(new BasicNameValuePair("query", "1"));
        HttpAsyncTask.getInstance(this, PreferenceUtils.getUserId() + "", pullParameters).pullContactsInfo();

    }

}
