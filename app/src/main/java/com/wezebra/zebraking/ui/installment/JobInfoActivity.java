package com.wezebra.zebraking.ui.installment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.wezebra.zebraking.R;
import com.wezebra.zebraking.behavior.AddOneMoreUploadOnClickListener;
import com.wezebra.zebraking.behavior.HttpAsyncTask;
import com.wezebra.zebraking.behavior.ProcessPhotoPickResult;
import com.wezebra.zebraking.behavior.SelectPhotoOnClickListener;
import com.wezebra.zebraking.behavior.SpinnerUtils;
import com.wezebra.zebraking.behavior.UploadPictureTask;
import com.wezebra.zebraking.model.PhotoUploadComponent;
import com.wezebra.zebraking.ui.BaseActivity;
import com.wezebra.zebraking.ui.fragment.ImageViewDialogFragment;
import com.wezebra.zebraking.util.Constants;
import com.wezebra.zebraking.util.PreferenceUtils;
import com.wezebra.zebraking.util.ViewPagerExampleActivity;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JobInfoActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout add_one_more;
    private FrameLayout upload_one,upload_two,upload_three,upload_four,upload_five;
    private ImageView photo_one,photo_two,photo_three,photo_four,photo_five;
    public PhotoUploadComponent photoUploadComponent;

    private TextView submit;
    public TextView tips;
    private ScrollView scrollView;
    private ImageView exam1,exam2,exam3;

    private SharedPreferences entrance_flag;
    private int entrance;

    public Spinner companyType;
    public EditText department,phone,address;

    public int companyType_i,status;
    public String department_s,phone_s,address_s,tips_s;
    private List<NameValuePair> postParameters;
    public String[] echoUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.job_info_choice_one);

        initView();
        echoHistory();
    }

    private void initView() {

        scrollView = (ScrollView)findViewById(R.id.scroll_view);
        scrollView.setVerticalScrollBarEnabled(false);

        add_one_more = (LinearLayout)findViewById(R.id.add_one_more);
        upload_one = (FrameLayout)findViewById(R.id.upload_one);
        upload_two = (FrameLayout)findViewById(R.id.upload_two);
        upload_three = (FrameLayout)findViewById(R.id.upload_three);
        upload_four = (FrameLayout)findViewById(R.id.upload_four);
        upload_five = (FrameLayout)findViewById(R.id.upload_five);
        photo_one = (ImageView)findViewById(R.id.photo_one);
        photo_two = (ImageView)findViewById(R.id.photo_two);
        photo_three = (ImageView)findViewById(R.id.photo_three);
        photo_four = (ImageView)findViewById(R.id.photo_four);
        photo_five = (ImageView)findViewById(R.id.photo_five);
        submit = (TextView)findViewById(R.id.submit);
        photoUploadComponent = new PhotoUploadComponent(1,add_one_more,new FrameLayout[] {upload_one,upload_two,upload_three,upload_four,upload_five},new ImageView[] {photo_one,photo_two,photo_three,photo_four,photo_five});

        add_one_more.setOnClickListener(new AddOneMoreUploadOnClickListener(photoUploadComponent));
        upload_one.setOnClickListener(new SelectPhotoOnClickListener(photoUploadComponent,this));
        upload_two.setOnClickListener(new SelectPhotoOnClickListener(photoUploadComponent,this));
        upload_three.setOnClickListener(new SelectPhotoOnClickListener(photoUploadComponent,this));
        upload_four.setOnClickListener(new SelectPhotoOnClickListener(photoUploadComponent,this));
        upload_five.setOnClickListener(new SelectPhotoOnClickListener(photoUploadComponent,this));
        submit.setOnClickListener(this);

        //Init Spinner
        companyType = (Spinner)findViewById(R.id.company_type);
        SpinnerUtils.initCompanyType(this, companyType);

        department = (EditText)findViewById(R.id.department);
        phone = (EditText)findViewById(R.id.phone);
        address = (EditText)findViewById(R.id.address);
        tips = (TextView)findViewById(R.id.tips);

        exam1 = (ImageView) findViewById(R.id.exam_one);
        exam2 = (ImageView) findViewById(R.id.exam_two);
        exam3 = (ImageView) findViewById(R.id.exam_three);
        exam1.setOnClickListener(new ExamOnClickListener());
        exam2.setOnClickListener(new ExamOnClickListener());
        exam3.setOnClickListener(new ExamOnClickListener());


    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_job_info, menu);
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

                companyType_i = companyType.getSelectedItemPosition();
                department_s = department.getText().toString();
                phone_s = phone.getText().toString();
                address_s = address.getText().toString();
                if (companyType_i == 0) {
                    Toast.makeText(this,getString(R.string.company_type_errors),Toast.LENGTH_LONG).show();
                    return;
                }
                if (department_s.equals("")) {
                    department.requestFocus();
                    department.setError(getString(R.string.department_errors));
                    return;
                }
                if (phone_s.equals("")) {
                    phone.requestFocus();
                    phone.setError(getString(R.string.company_phone_errors));
                    return;
                }
                if (address_s.equals("")) {
                    address.requestFocus();
                    address.setError(getString(R.string.company_address_errors));
                    return;
                }
                boolean isSelected = false;
                for (int i=0;i<photoUploadComponent.count;i++) {
                    if (photoUploadComponent.photoPath[i] != null) {
                        isSelected = true;
                    }
                    if (photoUploadComponent.uri[i] != null) {
                        isSelected = true;
                    }
                }
                if (isSelected == false) {
                    Toast.makeText(this,getString(R.string.picture_not_selected_errors),Toast.LENGTH_LONG).show();
                    break;
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
                        bundle.putInt(Constants.INFO_SUBMIT, Constants.JOB_OR_EDUCATION_INFO_SUBMIT);
                        intent.putExtra(Constants.INFO_SUBMIT,bundle);

                        entrance_flag = getSharedPreferences(Constants.ENTRANCE_FLAG, MODE_PRIVATE);
                        entrance = entrance_flag.getInt(Constants.ENTRANCE_FLAG,Constants.DEFAULT_ENTRANCE);
                        switch (entrance) {
                            case Constants.IS_FROM_APPLICATION:
                                intent.setClass(JobInfoActivity.this,SubmitInstallmentApplicationStepOneActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                break;
                            case Constants.IS_FROM_PROFILE:
                                intent.setClass(JobInfoActivity.this,PersonalProfileActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                break;
                        }

                        postParameters = new ArrayList<>();
                        postParameters.add(new BasicNameValuePair("companyType",companyType_i+""));
                        postParameters.add(new BasicNameValuePair("position",department_s));
                        postParameters.add(new BasicNameValuePair("companyPhone",phone_s));
                        postParameters.add(new BasicNameValuePair("companyAddr",address_s));
                        postParameters.add(new BasicNameValuePair("api", "authWorkApi2"));
                        UploadPictureTask.getUniqueInstance(JobInfoActivity.this,postParameters).submitJobInfo(photoUploadComponent,intent);

//                        HttpAsyncTask httpAsyncTask = HttpAsyncTask.getInstance(JobInfoActivity.this, PreferenceUtils.getUserId()+"", postParameters);
//                        httpAsyncTask.postWithMultiEntrance(intent);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        ProcessPhotoPickResult.process(photoUploadComponent, this, requestCode, resultCode, data);
    }

    public void echoHistory() {

        List<NameValuePair> pullParameters = new ArrayList<>();
        pullParameters.add(new BasicNameValuePair("api", "authWorkApi"));
        pullParameters.add(new BasicNameValuePair("query", "1"));
        HttpAsyncTask.getInstance(this, PreferenceUtils.getUserId() + "", pullParameters).pullJobInfo();

    }

    public class ExamOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            Intent intent = new Intent(JobInfoActivity.this, ViewPagerExampleActivity.class);
            int pos = 0;
            intent.putStringArrayListExtra("imgs", new ArrayList<String>(Arrays.asList(new String[]{"job_ex"})));

            switch (v.getId())
            {
                case R.id.exam_one:
                    intent.putExtra("pos",0);
                    pos = 0;
                    break;
                case R.id.exam_two:
                    intent.putExtra("pos",1);
                    pos = 1;
                    break;
                case R.id.exam_three:
                    intent.putExtra("pos",2);
                    pos = 2;
                    break;
            }

            ArrayList<Integer> list = new ArrayList<>(Arrays.asList(new Integer[]{R.drawable.job_info_one,R.drawable.job_info_two,R.drawable.job_info_three}));

            ImageViewDialogFragment dialogFragment = ImageViewDialogFragment.newInstance(pos, list);
            dialogFragment.show(getSupportFragmentManager(), "ImageViewDialogFragment");

        }
    }

}