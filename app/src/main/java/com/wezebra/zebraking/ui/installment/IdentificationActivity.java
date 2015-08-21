package com.wezebra.zebraking.ui.installment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wezebra.zebraking.R;
import com.wezebra.zebraking.behavior.HttpAsyncTask;
import com.wezebra.zebraking.behavior.UploadPictureTask;
import com.wezebra.zebraking.model.OnePhotoUploadComponent;
import com.wezebra.zebraking.ui.BaseActivity;
import com.wezebra.zebraking.ui.fragment.ImageViewDialogFragment;
import com.wezebra.zebraking.util.Constants;
import com.wezebra.zebraking.util.FileUtils;
import com.wezebra.zebraking.util.GenericUtils;
import com.wezebra.zebraking.util.L;
import com.wezebra.zebraking.util.PreferenceUtils;
import com.wezebra.zebraking.util.ThreadPoolUtil;
import com.wezebra.zebraking.util.ViewPagerExampleActivity;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class IdentificationActivity extends BaseActivity implements View.OnClickListener
{

    private TextView submit;
    public TextView tips;
    private ImageView personal_exam, id_exam_front, id_exam_back;
    public ImageView upload_id, upload_id_front, upload_id_back;
    public OnePhotoUploadComponent id, id_front, id_back, currentComponent;

    private SharedPreferences entrance_flag;
    private int entrance;

    private List<NameValuePair> postParameters;
    public String[] echoUrl;
    public int status;
    public String tips_s;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identification);

        initView();
        echoHistory();

        if (savedInstanceState != null)
        {
            id = savedInstanceState.getParcelable("id");
            id_front = savedInstanceState.getParcelable("id_front");
            id_back = savedInstanceState.getParcelable("id_back");
            currentComponent = savedInstanceState.getParcelable("current");
        }
    }

    private void initView()
    {

        submit = (TextView) findViewById(R.id.submit);
        tips = (TextView) findViewById(R.id.tips);
        personal_exam = (ImageView) findViewById(R.id.personal_exam);
        id_exam_front = (ImageView) findViewById(R.id.id_exam_front);
        id_exam_back = (ImageView) findViewById(R.id.id_exam_back);

        submit.setOnClickListener(new SubmitOnClickListener());
        personal_exam.setOnClickListener(this);
        id_exam_front.setOnClickListener(this);
        id_exam_back.setOnClickListener(this);

        upload_id = (ImageView) findViewById(R.id.upload_id);
        upload_id_front = (ImageView) findViewById(R.id.upload_id_front);
        upload_id_back = (ImageView) findViewById(R.id.upload_id_back);
        upload_id.setOnClickListener(new SelectOnePhotoOnClickListener());
        upload_id_front.setOnClickListener(new SelectOnePhotoOnClickListener());
        upload_id_back.setOnClickListener(new SelectOnePhotoOnClickListener());

        id = new OnePhotoUploadComponent(upload_id);
        id_front = new OnePhotoUploadComponent(upload_id_front);
        id_back = new OnePhotoUploadComponent(upload_id_back);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);

        outState.putParcelable("id", id);
        outState.putParcelable("id_front", id_front);
        outState.putParcelable("id_back", id_back);
        outState.putParcelable("current", currentComponent);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);

        id = savedInstanceState.getParcelable("id");
        id_front = savedInstanceState.getParcelable("id_front");
        id_back = savedInstanceState.getParcelable("id_back");
        currentComponent = savedInstanceState.getParcelable("current");
        id.photo_img = (ImageView) findViewById(R.id.upload_id);
        id_front.photo_img = (ImageView) findViewById(R.id.upload_id_front);
        id_back.photo_img = (ImageView) findViewById(R.id.upload_id_back);

    }

    //    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_identification, menu);
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

    public class SubmitOnClickListener implements View.OnClickListener
    {

        @Override
        public void onClick(View v)
        {

            if (status != Constants.UNPASS)
            {
                if ((id.photoPath == null) && (id.uri == null))
                {
                    Toast.makeText(IdentificationActivity.this, getString(R.string.id_select_errors), Toast.LENGTH_LONG).show();
                    return;
                }
                if ((id_front.photoPath == null) && (id_front.uri == null))
                {
                    Toast.makeText(IdentificationActivity.this, getString(R.string.id_front_select_errors), Toast.LENGTH_LONG).show();
                    return;
                }
                if ((id_back.photoPath == null) && (id_back.uri == null))
                {
                    Toast.makeText(IdentificationActivity.this, getString(R.string.id_back_select_errors), Toast.LENGTH_LONG).show();
                    return;
                }
            } else
            {
                if ((id.photoPath == null) && (id_front.photoPath == null) && (id_back.photoPath == null))
                {
                    Toast.makeText(IdentificationActivity.this, getString(R.string.id_modify_errors), Toast.LENGTH_LONG).show();
                    return;
                }
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(IdentificationActivity.this);
            builder.setTitle("提示");
            builder.setMessage("请确认所填信息正确无误，完成后将暂时无法更改哦");
            builder.setPositiveButton("确认", new DialogInterface.OnClickListener()
            {

                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    dialog.dismiss();

                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putInt(Constants.INFO_SUBMIT, Constants.ID_INFO_SUBMIT);
                    intent.putExtra(Constants.INFO_SUBMIT, bundle);

                    entrance_flag = getSharedPreferences(Constants.ENTRANCE_FLAG, MODE_PRIVATE);
                    entrance = entrance_flag.getInt(Constants.ENTRANCE_FLAG, Constants.DEFAULT_ENTRANCE);
                    switch (entrance)
                    {
                        case Constants.IS_FROM_APPLICATION:
                            intent.setClass(IdentificationActivity.this, SubmitInstallmentApplicationStepOneActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            break;
                        case Constants.IS_FROM_PROFILE:
                            intent.setClass(IdentificationActivity.this, PersonalProfileActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            break;
                    }

                    postParameters = new ArrayList<>();
                    postParameters.add(new BasicNameValuePair("api", "authIdentity"));
                    UploadPictureTask task = UploadPictureTask.getUniqueInstance(IdentificationActivity.this, postParameters);
                    //对象数组，所有的图片对象都存放在这个数组里面
                    task.submitIdentityAuthPictures(new OnePhotoUploadComponent[]{id, id_front, id_back}, intent);
                }
            });

            builder.setNegativeButton("取消", new DialogInterface.OnClickListener()
            {

                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    dialog.dismiss();
                }
            });

            builder.create().show();

        }
    }

    @Override
    public void onClick(View v)
    {

        Intent intent = new Intent(this, ViewPagerExampleActivity.class);
        int pos = 0;
        intent.putStringArrayListExtra("imgs", new ArrayList<String>(Arrays.asList(new String[]{"id_ex"})));

        switch (v.getId())
        {
            case R.id.personal_exam:
                intent.putExtra("pos", 0);
                pos = 0;
                break;
            case R.id.id_exam_front:
                intent.putExtra("pos", 1);
                pos = 1;
                break;
            case R.id.id_exam_back:
                intent.putExtra("pos", 2);
                pos = 2;
                break;
        }

        ArrayList<Integer> list = new ArrayList<>(Arrays.asList(new Integer[]{R.drawable.personal_id, R.drawable.exam_id_front, R.drawable.exam_id_back}));

        ImageViewDialogFragment dialogFragment = ImageViewDialogFragment.newInstance(pos, list);
        dialogFragment.show(getSupportFragmentManager(), "ImageViewDialogFragment");

    }

    public String createPicPath()
    {
        String str = null;
        Date date = null;
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        date = new Date();
        str = format.format(date);
        return Constants.IMAGE_DIR + File.separator + str + ".jpg";
    }

    public void showDialog(final OnePhotoUploadComponent onePhotoUploadComponent)
    {
        //显示一个dialog，选择图片，分为两种方式，一种是从相机中重新照相，一种是从相册中选择
        new AlertDialog.Builder(this)
                .setTitle("选择图片")
                .setItems(Constants.ONLY_CAMERA, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        switch (which)
                        {
                            case 0:
                                //这个是开启相机，重新去照照片
                                Intent openCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                onePhotoUploadComponent.photoPath = createPicPath();
                                //Log.i("picPath", onePhotoUploadComponent.photoPath);
                                onePhotoUploadComponent.tempPath = Constants.IMAGE_DIR + File.separator + "temp.jpg";
                                //Log.i("tempPath", onePhotoUploadComponent.tempPath);
                                File file = new File(Constants.IMAGE_DIR);
                                if (!file.exists())
                                {
                                    file.mkdirs();
                                }
                                Uri imageUri = Uri.fromFile(new File(onePhotoUploadComponent.tempPath));
                                openCamera.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                                //Log.i("Before startAcForResult", "");
                                startActivityForResult(openCamera, 200);
                                dialog.dismiss();
                                break;
                            //这个是从相册中选取
                            case 1:
                                Intent openAlbum = new Intent();
                                if (Build.VERSION.SDK_INT < 19)
                                {
                                    openAlbum.setAction(Intent.ACTION_GET_CONTENT);
                                } else
                                {
                                    openAlbum.setAction(Intent.ACTION_PICK);
                                    openAlbum.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                }

                                openAlbum.setType("image/*");
                                startActivityForResult(openAlbum, 100);
                                dialog.dismiss();
                                break;
                        }
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        dialog.dismiss();
                    }
                }).show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        new ProcessOnePhotoPickResult().process(currentComponent, requestCode, resultCode, data);
    }

    //点击选择一张图片，或者照一张图片
    public class SelectOnePhotoOnClickListener implements View.OnClickListener
    {

        @Override
        public void onClick(View v)
        {

            switch (v.getId())
            {
                case R.id.upload_id:
                    currentComponent = id;
                    showDialog(id);
                    break;
                case R.id.upload_id_front:
                    currentComponent = id_front;
                    showDialog(id_front);
                    break;
                case R.id.upload_id_back:
                    currentComponent = id_back;
                    showDialog(id_back);
                    break;
            }

        }

    }

//    public class OnePhotoUploadComponent {
//
//        public ImageView photo_img;
//        public String tempPath;
//        public String photoPath;
//        public String uri;
//
//        public OnePhotoUploadComponent(ImageView photo_img) {
//            this.photo_img = photo_img;
//            this.tempPath = null;
//            this.photoPath = null;
//            this.uri = null;
//        }
//
//    }

    public class ProcessOnePhotoPickResult
    {

        public void process(final OnePhotoUploadComponent onePhotoUploadComponent, int requestCode, int resultCode, Intent data)
        {
            //操作成功
            if (resultCode == Activity.RESULT_OK)
            {
                switch (requestCode)
                {   //这个是从相册里面取得
                    case 100:
                        Uri uri = data.getData();
                        ContentResolver cr = getContentResolver();
                        onePhotoUploadComponent.photoPath = GenericUtils.getFilePathFromContentUri(uri, cr);

                        Bitmap bmp = GenericUtils.decodeSampleImage(new File(onePhotoUploadComponent.photoPath), 1024, 1024);

                        onePhotoUploadComponent.photo_img.setScaleType(ImageView.ScaleType.FIT_XY);
                        onePhotoUploadComponent.photo_img.setImageBitmap(bmp);
                        break;
                    //是相机的照片
                    case 200:
                        String sdStatus = Environment.getExternalStorageState();
                        if (!sdStatus.equals(Environment.MEDIA_MOUNTED))
                        { // 检测sd是否可用
                            Log.i("No SDCard", "...");
                            return;
                        }

                        Bitmap bm = GenericUtils.decodeSampleImage(new File(onePhotoUploadComponent.tempPath), 1024, 1024);
                        ThreadPoolUtil.submit(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                try
                                {
                                    FileUtils.copyFile(new File(onePhotoUploadComponent.tempPath), new File(onePhotoUploadComponent.photoPath));
                                } catch (IOException e)
                                {
                                    e.printStackTrace();
                                    L.e("AuthBase", "copy file fail");
                                }
                            }
                        });

                        onePhotoUploadComponent.photo_img.setScaleType(ImageView.ScaleType.FIT_XY);
                        onePhotoUploadComponent.photo_img.setImageBitmap(bm);
                        break;
                    default:
                        break;
                }
            } else if (resultCode == Activity.RESULT_CANCELED)//如果是取消的
            {

                Log.i("result_canceled", "");
                switch (requestCode)
                {
                    case 100:
                        break;
                    case 200:
                        onePhotoUploadComponent.photoPath = null;//如果相机中没有照图片，而是取消了
                        break;
                    default:
                        break;
                }

            }

        }

    }

    public void echoHistory()
    {

        List<NameValuePair> pullParameters = new ArrayList<>();
        pullParameters.add(new BasicNameValuePair("api", "authIdentity"));
        pullParameters.add(new BasicNameValuePair("query", "1"));
        HttpAsyncTask.getInstance(this, PreferenceUtils.getUserId() + "", pullParameters).pullIdentityInfo();

    }

}
