package com.wezebra.zebraking.ui.installment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wezebra.zebraking.App;
import com.wezebra.zebraking.R;
import com.wezebra.zebraking.behavior.AddOneMoreUploadOnClickListener;
import com.wezebra.zebraking.behavior.HttpAsyncTask;
import com.wezebra.zebraking.behavior.ProcessPhotoPickResult;
import com.wezebra.zebraking.behavior.SelectPhotoOnClickListener;
import com.wezebra.zebraking.behavior.UploadPictureTask;
import com.wezebra.zebraking.model.PhotoUploadComponent;
import com.wezebra.zebraking.ui.BaseActivity;
import com.wezebra.zebraking.ui.adapter.ImageChooserAdapter;
import com.wezebra.zebraking.ui.fragment.ImageViewDialogFragment;
import com.wezebra.zebraking.util.Constants;
import com.wezebra.zebraking.util.FileUtils;
import com.wezebra.zebraking.util.GenericUtils;
import com.wezebra.zebraking.util.L;
import com.wezebra.zebraking.util.PreferenceUtils;
import com.wezebra.zebraking.util.ThreadPoolUtil;
import com.wezebra.zebraking.util.ViewPagerExampleActivity;
import com.wezebra.zebraking.widget.CustomGridView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class HouseDataAuthActivity extends BaseActivity implements View.OnClickListener {

    private SharedPreferences entrance_flag;
    private int entrance;
    private TextView submit;

    private int componentPosition;
//    private LinearLayout add_one_more_first;
//    private FrameLayout upload_one_first,upload_two_first,upload_three_first,upload_four_first,upload_five_first;
//    private ImageView photo_one_first,photo_two_first,photo_three_first,photo_four_first,photo_five_first;
//    public PhotoUploadComponent photoUploadComponent_first;

    private LinearLayout add_one_more_second;
    private FrameLayout upload_one_second,upload_two_second,upload_three_second,upload_four_second,upload_five_second;
    private ImageView photo_one_second,photo_two_second,photo_three_second,photo_four_second,photo_five_second;
    public PhotoUploadComponent photoUploadComponent_second;

    private ImageView examRentalContract,examPOC,examPurchaseContract,examElectricityContract;
    public String[] uri1,uri2;

    public TextView tips;
    public int status;
    public String memo;

    private CustomGridView contract;
    public ArrayList<String> contractPath = new ArrayList<>();
    public ImageChooserAdapter contractAdapter;
    private int currentPosition;
    private String tempPath,picPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house_data_auth);

        initView();
        echoHistory();

        Log.i("ActivityReC","hd");
    }

    private void initView() {

        contract = (CustomGridView)findViewById(R.id.contract);
        contractPath.add("null");
        contractPath.add("camera_default");
        contractAdapter = new ImageChooserAdapter(this, contractPath);
        contract.setAdapter(contractAdapter);
        contract.setOnItemClickListener(new GridView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if ( (position == contractPath.size()-1) && (contractPath.get(position).equals("camera_default")) ) {

                    if (contractPath.size() < 10) {
                        contractPath.remove(position);
                        contractPath.add(position,"null");
                        contractPath.add("camera_default");
                        contractAdapter.notifyDataSetChanged();
                    } else if (contractPath.size() == 10) {
                        contractPath.remove(position);
                        contractPath.add(position,"null");
                        contractAdapter.notifyDataSetChanged();
                    } else {
                        return;
                    }

                } else {

                    currentPosition = position;
                    Log.i("GridPosition",currentPosition+"");
                    new AlertDialog.Builder(HouseDataAuthActivity.this)
                            .setTitle("选择图片")
                            .setItems(Constants.SELECT_PHOTO, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    switch (which) {
                                        case 0:
                                            Intent openCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                            picPath = createPicPath();
                                            Log.i("picPath", picPath);
                                            tempPath = Constants.IMAGE_DIR + File.separator + "temp.jpg";
                                            Log.i("tempPath",tempPath);
                                            File file = new File(Constants.IMAGE_DIR);
                                            if (!file.exists()) {
                                                file.mkdirs();
                                            }
                                            Uri imageUri = Uri.fromFile(new File(tempPath));
                                            openCamera.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                                            Log.i("Before startAcForResult", "AAA");
                                            startActivityForResult(openCamera, 200+1);

                                            dialog.dismiss();
                                            break;
                                        case 1:
                                            Intent openAlbum = new Intent();
                                            if (Build.VERSION.SDK_INT < 19) {
                                                openAlbum.setAction(Intent.ACTION_GET_CONTENT);
                                            } else {
                                                openAlbum.setAction(Intent.ACTION_PICK);
                                                openAlbum.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                            }

                                            openAlbum.setType("image/*");
                                            startActivityForResult(openAlbum, 100+1);

                                            dialog.dismiss();
                                            break;
                                    }
                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).show();

                }

                }

        });

//        add_one_more_first = (LinearLayout)findViewById(R.id.add_one_more);
//        upload_one_first = (FrameLayout)findViewById(R.id.upload_one);
//        upload_two_first = (FrameLayout)findViewById(R.id.upload_two);
//        upload_three_first = (FrameLayout)findViewById(R.id.upload_three);
//        upload_four_first = (FrameLayout)findViewById(R.id.upload_four);
//        upload_five_first = (FrameLayout)findViewById(R.id.upload_five);
//        photo_one_first = (ImageView)findViewById(R.id.photo_one);
//        photo_two_first = (ImageView)findViewById(R.id.photo_two);
//        photo_three_first = (ImageView)findViewById(R.id.photo_three);
//        photo_four_first = (ImageView)findViewById(R.id.photo_four);
//        photo_five_first = (ImageView)findViewById(R.id.photo_five);
//        photoUploadComponent_first = new PhotoUploadComponent(1,add_one_more_first,new FrameLayout[] {upload_one_first,upload_two_first,upload_three_first,upload_four_first,upload_five_first},new ImageView[] {photo_one_first,photo_two_first,photo_three_first,photo_four_first,photo_five_first});
//        add_one_more_first.setOnClickListener(new AddOneMoreUploadOnClickListener(photoUploadComponent_first));
//        upload_one_first.setOnClickListener(new SelectPhotoOnClickListener(photoUploadComponent_first,this, 1));
//        upload_two_first.setOnClickListener(new SelectPhotoOnClickListener(photoUploadComponent_first,this, 1));
//        upload_three_first.setOnClickListener(new SelectPhotoOnClickListener(photoUploadComponent_first,this, 1));
//        upload_four_first.setOnClickListener(new SelectPhotoOnClickListener(photoUploadComponent_first,this, 1));
//        upload_five_first.setOnClickListener(new SelectPhotoOnClickListener(photoUploadComponent_first,this, 1));

        add_one_more_second = (LinearLayout)findViewById(R.id.add_one_more_second);
        upload_one_second = (FrameLayout)findViewById(R.id.upload_one_second);
        upload_two_second = (FrameLayout)findViewById(R.id.upload_two_second);
        upload_three_second = (FrameLayout)findViewById(R.id.upload_three_second);
        upload_four_second = (FrameLayout)findViewById(R.id.upload_four_second);
        upload_five_second = (FrameLayout)findViewById(R.id.upload_five_second);
        photo_one_second = (ImageView)findViewById(R.id.photo_one_second);
        photo_two_second = (ImageView)findViewById(R.id.photo_two_second);
        photo_three_second = (ImageView)findViewById(R.id.photo_three_second);
        photo_four_second = (ImageView)findViewById(R.id.photo_four_second);
        photo_five_second = (ImageView)findViewById(R.id.photo_five_second);
        photoUploadComponent_second = new PhotoUploadComponent(1,add_one_more_second,new FrameLayout[] {upload_one_second,upload_two_second,upload_three_second,upload_four_second,upload_five_second},new ImageView[] {photo_one_second,photo_two_second,photo_three_second,photo_four_second,photo_five_second});
        add_one_more_second.setOnClickListener(new AddOneMoreUploadOnClickListener(photoUploadComponent_second));
        upload_one_second.setOnClickListener(new SelectPhotoOnClickListener(photoUploadComponent_second,this, 2));
        upload_two_second.setOnClickListener(new SelectPhotoOnClickListener(photoUploadComponent_second,this, 2));
        upload_three_second.setOnClickListener(new SelectPhotoOnClickListener(photoUploadComponent_second,this, 2));
        upload_four_second.setOnClickListener(new SelectPhotoOnClickListener(photoUploadComponent_second,this, 2));
        upload_five_second.setOnClickListener(new SelectPhotoOnClickListener(photoUploadComponent_second,this, 2));

        submit = (TextView)findViewById(R.id.submit);
        submit.setOnClickListener(this);

        examRentalContract = (ImageView)findViewById(R.id.exam_rental_contract);
        examPOC = (ImageView)findViewById(R.id.exam_poc);
        examPurchaseContract = (ImageView)findViewById(R.id.exam_purchase_contract);
        examElectricityContract = (ImageView)findViewById(R.id.exam_electricity_bill);
        examRentalContract.setOnClickListener(new ExamOnClickListener());
        examPOC.setOnClickListener(new ExamOnClickListener());
        examPurchaseContract.setOnClickListener(new ExamOnClickListener());
        examElectricityContract.setOnClickListener(new ExamOnClickListener());

        tips = (TextView)findViewById(R.id.tips);

    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_house_data_auth, menu);
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

                if (status != Constants.UNPASS) {
                    boolean isContractSelected = false;
//                    for (int i=0;i<photoUploadComponent_first.count;i++) {
//                        if ( (photoUploadComponent_first.uri[i] != null) && (!photoUploadComponent_first.uri[i].equals("")) ) {
//                            isContractSelected = true;
//                        }
//                        if ( (photoUploadComponent_first.photoPath[i] != null) && (!photoUploadComponent_first.photoPath[i].equals("")) ) {
//                            isContractSelected = true;
//                        }
//                    }
                    if (contractPath.size() > 0) {
                        isContractSelected = true;
                    }
                    if (isContractSelected == false) {
                        Toast.makeText(this,getString(R.string.contract_errors),Toast.LENGTH_LONG).show();
                        return;
                    }
                    boolean isOtherMaterialSelected = false;
                    for (int i=0;i<photoUploadComponent_second.count;i++) {
                        if ( (photoUploadComponent_second.uri[i] != null) && (!photoUploadComponent_second.uri[i].equals("")) ) {
                            isOtherMaterialSelected = true;
                        }
                        if ( (photoUploadComponent_second.photoPath[i] != null) && (!photoUploadComponent_second.photoPath[i].equals("")) ) {
                            isOtherMaterialSelected = true;
                        }
                    }
                    if (isOtherMaterialSelected == false) {
                        Toast.makeText(this,getString(R.string.other_material_errors),Toast.LENGTH_LONG).show();
                        return;
                    }
                } else {
                    boolean isModified = false;
//                    for (int i=0;i<photoUploadComponent_first.count;i++) {
//                        if ( (photoUploadComponent_first.uri[i] != null) && (!photoUploadComponent_first.uri[i].equals("")) ) {
//                            isModified = true;
//                        }
//                        if ( (photoUploadComponent_first.photoPath[i] != null) && (!photoUploadComponent_first.photoPath[i].equals("")) ) {
//                            isModified = true;
//                        }
//                    }
                    for (String s:contractPath) {
                        if (!s.contains("http")) {
                            isModified = true;
                        }
                    }
                    for (int i=0;i<photoUploadComponent_second.count;i++) {
                        if ( (photoUploadComponent_second.uri[i] != null) && (!photoUploadComponent_second.uri[i].equals("")) ) {
                            isModified = true;
                        }
                        if ( (photoUploadComponent_second.photoPath[i] != null) && (!photoUploadComponent_second.photoPath[i].equals("")) ) {
                            isModified = true;
                        }
                    }
                    if (isModified == false) {
                        Toast.makeText(this,getString(R.string.other_material_errors),Toast.LENGTH_LONG).show();
                        return;
                    }
                }

                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putInt(Constants.INFO_SUBMIT,Constants.HOUSE_DATA_SUBMIT);
                intent.putExtra(Constants.INFO_SUBMIT, bundle);

                entrance_flag = getSharedPreferences(Constants.ENTRANCE_FLAG,MODE_PRIVATE);
                entrance = entrance_flag.getInt(Constants.ENTRANCE_FLAG,Constants.DEFAULT_ENTRANCE);
                switch (entrance) {
                    case Constants.IS_FROM_STEP_TWO:
                        intent.setClass(this,SubmitInstallmentApplicationStepTwoActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        break;
                    case Constants.IS_FROM_PROFILE:
                        intent.setClass(this,PersonalProfileActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        break;
                }

                List<NameValuePair> postParameters = new ArrayList<>();
                postParameters.add(new BasicNameValuePair("api","rentalCail"));
                postParameters.add(new BasicNameValuePair("orderCode", App.INSTANCE.getActiveApplyData().getOrderCode() + ""));
                UploadPictureTask.getUniqueInstance(HouseDataAuthActivity.this,postParameters).submitPictureOfTwoGroup(contractPath, photoUploadComponent_second, intent);

                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        componentPosition = requestCode % 10;

        switch (componentPosition) {
            case 1:

                if (resultCode == Activity.RESULT_OK)
                {
                    switch (requestCode / 10)
                    {
                        case 10:
                            Uri uri = data.getData();
                            ContentResolver cr = getContentResolver();
                            contractPath.remove(currentPosition);
                            contractPath.add(currentPosition,GenericUtils.getFilePathFromContentUri(uri, cr));
                            contractAdapter.notifyDataSetChanged();

                            break;
                        case 20:
                            String sdStatus = Environment.getExternalStorageState();
                            if (!sdStatus.equals(Environment.MEDIA_MOUNTED))
                            { // 检测sd是否可用
                                Log.i("No SDCard", "...");
                                return;
                            }

                            final String t = tempPath;
                            final String f = picPath;
                            ThreadPoolUtil.submit(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        FileUtils.copyFile(new File(t), new File(f));
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                        L.e("AuthBase", "copy file fail");
                                    }
                                }
                            });

                            contractPath.remove(currentPosition);
                            contractPath.add(currentPosition, picPath);
                            contractAdapter.notifyDataSetChanged();

                            break;
                        default:
                            break;
                    }
                }

                for (String s:contractPath) {
                    System.out.println( "ContractPath: " + s);
                }

                break;
            case 2:

                if (photoUploadComponent_second.tempPath == null) {
                    Log.i("ActivityRecreate",".");
                }

                ProcessPhotoPickResult.process(photoUploadComponent_second, this, requestCode, resultCode, data);
                break;
            case 0:
                Log.i("data_retrieve_error", "HouseDataAuth");
                break;
        }

    }

    public class ExamOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            Intent intent = new Intent(HouseDataAuthActivity.this, ViewPagerExampleActivity.class);
            int pos = 0;
            intent.putStringArrayListExtra("imgs", new ArrayList<String>(Arrays.asList(new String[]{"house_data_ex"})));

            switch (v.getId())
            {
                case R.id.exam_rental_contract:
                    intent.putExtra("pos",0);
                    pos = 0;
                    break;
                case R.id.exam_poc:
                    intent.putExtra("pos",1);
                    pos = 1;
                    break;
                case R.id.exam_purchase_contract:
                    intent.putExtra("pos",2);
                    pos = 2;
                    break;
                case R.id.exam_electricity_bill:
                    intent.putExtra("pos",3);
                    pos = 3;
                    break;
            }

            ArrayList<Integer> list = new ArrayList<>(Arrays.asList(new Integer[]{R.drawable.exam_rental_contract,R.drawable.exam_poc,R.drawable.exam_purchase_contract,R.drawable.exam_electricity_bill}));

            ImageViewDialogFragment dialogFragment = ImageViewDialogFragment.newInstance(pos, list);
            dialogFragment.show(getSupportFragmentManager(), "ImageViewDialogFragment");
        }
    }

    public void echoHistory() {

        List<NameValuePair> pullParameters = new ArrayList<>();
        pullParameters.add(new BasicNameValuePair("api", "selectMaterialInfo"));
        pullParameters.add(new BasicNameValuePair("orderCode", App.INSTANCE.getActiveApplyData().getOrderCode()+""));
        HttpAsyncTask.getInstance(this, PreferenceUtils.getUserId() + "", pullParameters).pullHouseDataInfo();

    }

    public String createPicPath() {
        String str = null;
        Date date = null;
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        date = new Date();
        str = format.format(date);
        return Constants.IMAGE_DIR + File.separator + str + ".jpg";
    }

}
