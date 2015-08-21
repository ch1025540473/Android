package com.wezebra.zebraking.ui.installment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.wezebra.zebraking.R;
import com.wezebra.zebraking.behavior.AddOneMoreUploadOnClickListener;
import com.wezebra.zebraking.behavior.HttpAsyncTask;
import com.wezebra.zebraking.behavior.ProcessPhotoPickResult;
import com.wezebra.zebraking.behavior.SelectPhotoOnClickListener;
import com.wezebra.zebraking.behavior.UploadPictureTask;
import com.wezebra.zebraking.model.PhotoUploadComponent;
import com.wezebra.zebraking.ui.BaseActivity;
import com.wezebra.zebraking.ui.adapter.AuthViewPagerAdapter;
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

public class EducationAuthActivity extends BaseActivity implements View.OnClickListener {

    public RadioGroup choice;
    private RadioButton choice_one;
    private ViewPager viewPager;
    private List<View> viewList;
    private AuthViewPagerAdapter viewPagerAdapter;
    private TextView submit_one,submit_two;
    public TextView tips;

    private SharedPreferences entrance_flag;
    private int entrance;

    private LinearLayout add_one_more;
    private FrameLayout upload_one,upload_two,upload_three,upload_four,upload_five;
    private ImageView photo_one,photo_two,photo_three,photo_four,photo_five;
    public PhotoUploadComponent photoUploadComponent;

    private ImageView diploma,studentCard;

    public EditText account,password;
    public String account_s,password_s;
    private List<NameValuePair> postParameters;
    public String[] echoUrl;
    public String tips_s;
    public int status;

    private ScrollView scrollView1,scrollView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_education_auth);

        initView();
        echoHistory();
    }

    private void initView() {

        tips = (TextView)findViewById(R.id.tips);
        choice = (RadioGroup)findViewById(R.id.choice);
        choice_one = (RadioButton)findViewById(R.id.choice_one);
        viewPager = (ViewPager)findViewById(R.id.view_pager);

        choice.check(R.id.choice_one);
        Drawable drawable = getResources().getDrawable(R.drawable.edu_recommend_selected);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        choice_one.setCompoundDrawables(null, null, drawable, null);
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
        viewList.add(getLayoutInflater().inflate(R.layout.education_auth_choice_one_layout, null));
        viewList.add(getLayoutInflater().inflate(R.layout.education_auth_choice_two_layout, null));
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
        account = (EditText)viewList.get(0).findViewById(R.id.account);
        password = (EditText)viewList.get(0).findViewById(R.id.password);
        submit_two = (TextView)viewList.get(1).findViewById(R.id.submit_two);

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

        submit_one.setOnClickListener(this);
        submit_two.setOnClickListener(this);
        choice_one.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Drawable drawable;
                if (isChecked) {
                    drawable = getResources().getDrawable(R.drawable.edu_recommend_selected);
                    drawable.setBounds(0,0,drawable.getMinimumWidth(),drawable.getMinimumHeight());
                    choice_one.setCompoundDrawables(null,null,drawable,null);
                } else {
                    drawable = getResources().getDrawable(R.drawable.edu_recommend_unselected);
                    drawable.setBounds(0,0,drawable.getMinimumWidth(),drawable.getMinimumHeight());
                    choice_one.setCompoundDrawables(null,null,drawable,null);
                }
            }
        });

        diploma = (ImageView)viewList.get(1).findViewById(R.id.diploma);
        studentCard = (ImageView)viewList.get(1).findViewById(R.id.student_card);
        diploma.setOnClickListener(new ExamOnClickListener());
        studentCard.setOnClickListener(new ExamOnClickListener());

    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_education_auth, menu);
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

                account_s = account.getText().toString();
                password_s = password.getText().toString();
                if (account_s.equals("")) {
                    account.requestFocus();
                    account.setError(getString(R.string.edu_auth_account_errors));
                    return;
                } else if (password_s.equals("")) {
                    password.requestFocus();
                    password.setError(getString(R.string.edu_auth_password_errors));
                    return;
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("提示");
                builder.setMessage("请确认所填信息正确无误，完成后将暂时无法更改哦");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                        postParameters = new ArrayList<>();
                        postParameters.add(new BasicNameValuePair("chsiName",account_s));
                        postParameters.add(new BasicNameValuePair("chsiPwd", Base64Digest.encode(password_s)));

                        Intent intent = new Intent();
                        Bundle bundle = new Bundle();
                        bundle.putInt(Constants.INFO_SUBMIT, Constants.JOB_OR_EDUCATION_INFO_SUBMIT);
                        intent.putExtra(Constants.INFO_SUBMIT,bundle);

                        entrance_flag = getSharedPreferences(Constants.ENTRANCE_FLAG, MODE_PRIVATE);
                        entrance = entrance_flag.getInt(Constants.ENTRANCE_FLAG,Constants.DEFAULT_ENTRANCE);
                        switch (entrance) {
                            case Constants.IS_FROM_APPLICATION:
                                intent.setClass(EducationAuthActivity.this, SubmitInstallmentApplicationStepOneActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                //startActivity(intent);
                                break;
                            case Constants.IS_FROM_PROFILE:
                                intent.setClass(EducationAuthActivity.this, PersonalProfileActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                //startActivity(intent);
                                break;
                        }

                        postParameters.add(new BasicNameValuePair("api", "authEducationApi"));
                        HttpAsyncTask asyncTask = HttpAsyncTask.getInstance(EducationAuthActivity.this, PreferenceUtils.getUserId()+"",postParameters);
                        asyncTask.postWithMultiEntrance(intent);
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
            case R.id.submit_two:

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

                builder = new AlertDialog.Builder(this);
                builder.setTitle("提示");
                builder.setMessage("请确认所填信息正确无误，完成后将暂时无法更改哦");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                        Intent intent = new Intent();
                        Bundle bundle = new Bundle();
                        bundle.putInt(Constants.INFO_SUBMIT,Constants.JOB_OR_EDUCATION_INFO_SUBMIT);
                        intent.putExtra(Constants.INFO_SUBMIT,bundle);

                        entrance_flag = getSharedPreferences(Constants.ENTRANCE_FLAG,MODE_PRIVATE);
                        entrance = entrance_flag.getInt(Constants.ENTRANCE_FLAG,Constants.DEFAULT_ENTRANCE);
                        switch (entrance) {
                            case Constants.IS_FROM_APPLICATION:
                                intent.setClass(EducationAuthActivity.this, SubmitInstallmentApplicationStepOneActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                break;
                            case Constants.IS_FROM_PROFILE:
                                intent.setClass(EducationAuthActivity.this, PersonalProfileActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                break;
                        }

                        postParameters = new ArrayList<>();
                        postParameters.add(new BasicNameValuePair("api","authEducationApi"));
                        UploadPictureTask uploadPictureTask = UploadPictureTask.getUniqueInstance(EducationAuthActivity.this,postParameters);
                        uploadPictureTask.submitPictureOfOneGroup(photoUploadComponent,intent);
                    }
                });

                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builder.create().show();

//                String[] uri = new String[photoUploadComponent.count];
//                for (int i=0;i<photoUploadComponent.count;i++) {
//                    if (photoUploadComponent.photoPath[i] != null) {
//                        try {
//                            uri[i] = HttpClientImp.INSTANCE.postFileForString(Constants.FILE_UPLOAD_URL, GenericUtils.getBytesFromBitmap(GenericUtils.decodeSampleImage(new File(photoUploadComponent.photoPath[i]), 1024, 1024)));
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//                Log.i("PostFileResult",uri.toString());

//                AsyncTask task = new AsyncTask() {
//                    @Override
//                    protected Object doInBackground(Object[] params) {
//                        String s = "";
//                        try {
//                            s = HttpClientImp.INSTANCE.postFileForString(Constants.FILE_UPLOAD_URL, GenericUtils.getBytesFromBitmap(GenericUtils.compressBitmap(photoUploadComponent.photoPath[0], 720, 720)));
//                            Log.i("UploadPicSize",GenericUtils.compressBitmap(photoUploadComponent.photoPath[0], 720, 720).getByteCount()+"");
//                            Log.i("UploadPicSize2",GenericUtils.getBytesFromBitmap(GenericUtils.compressBitmap(photoUploadComponent.photoPath[0], 720, 720)).length+"");
//                            Log.i("BitMapInfo",GenericUtils.compressBitmap(photoUploadComponent.photoPath[0], 720, 720).getHeight()+"");
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                        Log.i("PostFileResult",s);
//                        return null;
//                    }
//                };
//                task.execute();

//                StringBuilder builder = new StringBuilder("");
//                for (int i=0;i<uri.length;i++) {
//                    if (uri[i] != null) {
//                        builder.append(uri[i]+",");
//                    }
//                }
//                builder.deleteCharAt(builder.length() - 1);
//                Log.i("UriString",builder.toString());

//                intent = new Intent();
//                bundle = new Bundle();
//                bundle.putInt(Constants.INFO_SUBMIT,Constants.JOB_OR_EDUCATION_INFO_SUBMIT);
//                intent.putExtra(Constants.INFO_SUBMIT,bundle);
//
//                entrance_flag = getSharedPreferences(Constants.ENTRANCE_FLAG,MODE_PRIVATE);
//                entrance = entrance_flag.getInt(Constants.ENTRANCE_FLAG,Constants.DEFAULT_ENTRANCE);
//                switch (entrance) {
//                    case Constants.IS_FROM_APPLICATION:
//                        intent.setClass(this,SubmitInstallmentApplicationStepOneActivity.class);
//                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        startActivity(intent);
//                        break;
//                    case Constants.IS_FROM_PROFILE:
//                        intent.setClass(this,PersonalProfileActivity.class);
//                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        startActivity(intent);
//                        break;
//                }
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

            Intent intent = new Intent(EducationAuthActivity.this, ViewPagerExampleActivity.class);
            int pos = 0;
            intent.putStringArrayListExtra("imgs", new ArrayList<String>(Arrays.asList(new String[]{"edu_ex"})));

            switch (v.getId())
            {
                case R.id.diploma:
                    intent.putExtra("pos",0);
                    pos = 0;
                    break;
                case R.id.student_card:
                    intent.putExtra("pos",1);
                    pos = 1;
                    break;
            }

            ArrayList<Integer> list = new ArrayList<>(Arrays.asList(new Integer[]{R.drawable.exam_diploma,R.drawable.exam_student_card}));

            ImageViewDialogFragment dialogFragment = ImageViewDialogFragment.newInstance(pos, list);
            dialogFragment.show(getSupportFragmentManager(), "ImageViewDialogFragment");
        }

    }

    public void echoHistory() {

//        //photoUploadComponent.count = 3;
//        photoUploadComponent.photoPath = new String[] {"http://image.tianjimedia.com/uploadImages/2013/220/5EATV70TN931_1920x1080.jpg","http://image.tianjimedia.com/uploadImages/2013/220/5EATV70TN931_1920x1080.jpg","http://image.tianjimedia.com/uploadImages/2013/220/5EATV70TN931_1920x1080.jpg"};
//        photoUploadComponent.showMore(3-1);
//        ImageLoader imageLoader = ImageLoader.getInstance();
//        for (int i=0;i<3;i++) {
//            imageLoader.displayImage(photoUploadComponent.photoPath[i],photoUploadComponent.photo_img[i]);
//        }
        List<NameValuePair> pullParameters = new ArrayList<>();
        pullParameters.add(new BasicNameValuePair("api", "authEducationApi"));
        pullParameters.add(new BasicNameValuePair("query", "1"));
        HttpAsyncTask.getInstance(this,PreferenceUtils.getUserId()+"",pullParameters).pullEduInfo();

    }

}
