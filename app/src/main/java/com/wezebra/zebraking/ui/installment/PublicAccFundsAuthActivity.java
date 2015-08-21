package com.wezebra.zebraking.ui.installment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.method.HideReturnsTransformationMethod;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
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
import com.wezebra.zebraking.ui.adapter.AuthViewPagerAdapter;
import com.wezebra.zebraking.ui.fragment.AddressDialogFragment;
import com.wezebra.zebraking.ui.fragment.ImageViewDialogFragment;
import com.wezebra.zebraking.util.Base64Digest;
import com.wezebra.zebraking.util.Constants;
import com.wezebra.zebraking.util.PreferenceUtils;
import com.wezebra.zebraking.util.ViewPagerExampleActivity;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PublicAccFundsAuthActivity extends BaseActivity implements View.OnClickListener,AddressDialogFragment.OnCitySelectedListener {

    public RadioGroup choice;
    private ViewPager viewPager;
    private List<View> viewList;
    private AuthViewPagerAdapter viewPagerAdapter;

    private TextView submit_one,submit_two;

    private LinearLayout add_one_more;
    private FrameLayout upload_one,upload_two,upload_three,upload_four,upload_five;
    private ImageView photo_one,photo_two,photo_three,photo_four,photo_five;
    public PhotoUploadComponent photoUploadComponent;

    private ImageView examPAF;

    public Spinner selfPayAmount;
    public TextView attribution;

    public EditText account,password;

    private SharedPreferences entrance_flag;
    private int entrance;

    public String account_s,password_s;
    public int cityId,selfPayAmount_i;
    public String[] echoUrl;
    public List<NameValuePair> postParameters;

    public TextView tips;
    public String tips_s;
    public int status;

    private ScrollView scrollView1,scrollView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_acc_funds_auth);

        initView();
        echoHistory();
    }

    private void initView() {

        tips = (TextView)findViewById(R.id.tips);
        choice = (RadioGroup)findViewById(R.id.choice);
        viewPager = (ViewPager)findViewById(R.id.view_pager);

        choice.check(R.id.choice_one);
        choice.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.choice_one:
                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.choice_two:
                        viewPager.setCurrentItem(1);
                        break;
                }
            }
        });

        viewList = new ArrayList<>();
        viewList.add(getLayoutInflater().inflate(R.layout.public_acc_funds_auth_choice_one_layout,null));
        viewList.add(getLayoutInflater().inflate(R.layout.public_acc_funds_auth_choice_two_layout, null));
        viewPagerAdapter = new AuthViewPagerAdapter(viewList);
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                switch (position) {
                    case 0:
                        choice.check(R.id.choice_one);
                        break;
                    case 1:
                        choice.check(R.id.choice_two);
                        break;
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        scrollView1 = (ScrollView)viewList.get(0).findViewById(R.id.scroll_view);
        scrollView1.setVerticalScrollBarEnabled(false);
        scrollView2 = (ScrollView)viewList.get(1).findViewById(R.id.scroll_view);
        scrollView2.setVerticalScrollBarEnabled(false);

        submit_one = (TextView)viewList.get(0).findViewById(R.id.submit_one);
        submit_two = (TextView)viewList.get(1).findViewById(R.id.submit_two);
        submit_one.setOnClickListener(this);
        submit_two.setOnClickListener(this);

        add_one_more = (LinearLayout)viewList.get(1).findViewById(R.id.add_one_more);
        upload_one = (FrameLayout)viewList.get(1).findViewById(R.id.upload_one);
        upload_two = (FrameLayout)viewList.get(1).findViewById(R.id.upload_two);
        upload_three = (FrameLayout)viewList.get(1).findViewById(R.id.upload_three);
        upload_four = (FrameLayout)viewList.get(1).findViewById(R.id.upload_four);
        upload_five = (FrameLayout)viewList.get(1).findViewById(R.id.upload_five);
        photo_one = (ImageView)viewList.get(1).findViewById(R.id.photo_one);
        photo_two = (ImageView)viewList.get(1).findViewById(R.id.photo_two);
        photo_three = (ImageView)viewList.get(1).findViewById(R.id.photo_three);
        photo_four = (ImageView)viewList.get(1).findViewById(R.id.photo_four);
        photo_five = (ImageView)viewList.get(1).findViewById(R.id.photo_five);
        photoUploadComponent = new PhotoUploadComponent(1,add_one_more,new FrameLayout[] {upload_one,upload_two,upload_three,upload_four,upload_five},new ImageView[] {photo_one,photo_two,photo_three,photo_four,photo_five});
        add_one_more.setOnClickListener(new AddOneMoreUploadOnClickListener(photoUploadComponent));
        upload_one.setOnClickListener(new SelectPhotoOnClickListener(photoUploadComponent,this));
        upload_two.setOnClickListener(new SelectPhotoOnClickListener(photoUploadComponent,this));
        upload_three.setOnClickListener(new SelectPhotoOnClickListener(photoUploadComponent,this));
        upload_four.setOnClickListener(new SelectPhotoOnClickListener(photoUploadComponent,this));
        upload_five.setOnClickListener(new SelectPhotoOnClickListener(photoUploadComponent,this));

        examPAF = (ImageView)viewList.get(1).findViewById(R.id.exam_paf);
        examPAF.setOnClickListener(new ExamOnClickListener());

        account = (EditText)viewList.get(0).findViewById(R.id.account);
        account.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        password = (EditText)viewList.get(0).findViewById(R.id.password);

        //Init Spinner
        selfPayAmount = (Spinner)viewList.get(0).findViewById(R.id.self_pay_amount);
        SpinnerUtils.initSelfPayAmount(this,selfPayAmount);

        attribution = (TextView)viewList.get(0).findViewById(R.id.attribution);
        cityId = 0;
        attribution.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogFragment();
            }
        });

    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_public_acc_funds_auth, menu);
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
            case R.id.submit_one:

                selfPayAmount_i = selfPayAmount.getSelectedItemPosition();
                account_s = account.getText().toString();
                password_s = password.getText().toString();
                if (cityId == 0) {
                    Toast.makeText(this, getString(R.string.paf_auth_city_select_errors), Toast.LENGTH_LONG).show();
                    return;
                }
                if (selfPayAmount_i == 0) {
                    Toast.makeText(this,getString(R.string.paf_auth_self_pay_amount_errors),Toast.LENGTH_LONG).show();
                    return;
                }
                if (account_s.equals("")) {
                    account.requestFocus();
                    account.setError(getString(R.string.paf_auth_account_errors));
                    return;
                } else if (password_s.equals("")) {
                    password.requestFocus();
                    password.setError(getString(R.string.paf_auth_password_errors));
                    return;
                }

                postParameters = new ArrayList<>();
                postParameters.add(new BasicNameValuePair("cityId",cityId+""));
                postParameters.add(new BasicNameValuePair("cityName",attribution.getText().toString()));
                postParameters.add(new BasicNameValuePair("paymentMoney",selfPayAmount_i+""));
                postParameters.add(new BasicNameValuePair("cardNum",account_s));
                postParameters.add(new BasicNameValuePair("passwd", Base64Digest.encode(password_s)));

                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putInt(Constants.INFO_SUBMIT, Constants.PAF_SUBMIT);
                intent.putExtra(Constants.INFO_SUBMIT,bundle);

                entrance_flag = getSharedPreferences(Constants.ENTRANCE_FLAG, MODE_PRIVATE);
                entrance = entrance_flag.getInt(Constants.ENTRANCE_FLAG,Constants.IS_FROM_PROFILE);
                switch (entrance) {
//                    case Constants.IS_FROM_APPLICATION:
//                        intent.setClass(this, SubmitInstallmentApplicationStepOneActivity.class);
//                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        //startActivity(intent);
//                        break;
                    case Constants.IS_FROM_PROFILE:
                        intent.setClass(this, PersonalProfileActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        //startActivity(intent);
                        break;
                }

                PreferenceUtils.setFourStatus(1);
                postParameters.add(new BasicNameValuePair("api", "authReserveApi"));
                HttpAsyncTask asyncTask = HttpAsyncTask.getInstance(this, PreferenceUtils.getUserId()+"",postParameters);
                asyncTask.postWithMultiEntrance(intent);

                break;
            case R.id.submit_two:

                boolean isSelected = false;
                for (int i=0;i<photoUploadComponent.count;i++) {
                    if (photoUploadComponent.photoPath[i] != null) {
                        isSelected = true;
                    }
                    if (photoUploadComponent.uri[i] !=  null) {
                        isSelected = true;
                    }
                }
                if (isSelected == false) {
                    Toast.makeText(this,getString(R.string.picture_not_selected_errors),Toast.LENGTH_LONG).show();
                    break;
                }

                intent = new Intent();
                bundle = new Bundle();
                bundle.putInt(Constants.INFO_SUBMIT,Constants.PAF_SUBMIT);
                intent.putExtra(Constants.INFO_SUBMIT, bundle);

                entrance_flag = getSharedPreferences(Constants.ENTRANCE_FLAG,MODE_PRIVATE);
                entrance = entrance_flag.getInt(Constants.ENTRANCE_FLAG,Constants.IS_FROM_PROFILE);
                switch (entrance) {
                    case Constants.IS_FROM_APPLICATION:
                        intent.setClass(this, SubmitInstallmentApplicationStepOneActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        break;
                    case Constants.IS_FROM_PROFILE:
                        intent.setClass(this, PersonalProfileActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        break;
                }

                PreferenceUtils.setFourStatus(1);
                postParameters = new ArrayList<>();
                postParameters.add(new BasicNameValuePair("api", "authReserveApi"));
                UploadPictureTask uploadPictureTask = UploadPictureTask.getUniqueInstance(this,postParameters);
                uploadPictureTask.submitPictureOfOneGroup(photoUploadComponent, intent);

                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        ProcessPhotoPickResult.process(photoUploadComponent, this, requestCode, resultCode, data);
    }

    public class ExamOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            Intent intent = new Intent(PublicAccFundsAuthActivity.this, ViewPagerExampleActivity.class);
            int pos = 0;
            intent.putStringArrayListExtra("imgs", new ArrayList<String>(Arrays.asList(new String[]{"paf_ex"})));

            switch (v.getId())
            {
                case R.id.exam_paf:
                    intent.putExtra("pos",0);
                    pos = 0;
                    break;
            }

            ArrayList<Integer> list = new ArrayList<>(Arrays.asList(new Integer[]{R.drawable.exam_paf}));

            ImageViewDialogFragment dialogFragment = ImageViewDialogFragment.newInstance(pos, list);
            dialogFragment.show(getSupportFragmentManager(), "ImageViewDialogFragment");

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
        this.cityId = cityId;
    }

    public void echoHistory() {

        List<NameValuePair> pullParameters = new ArrayList<>();
        pullParameters.add(new BasicNameValuePair("api", "authReserveApi"));
        pullParameters.add(new BasicNameValuePair("query", "1"));
        HttpAsyncTask.getInstance(this,PreferenceUtils.getUserId()+"",pullParameters).pullPAFInfo();

    }

}
