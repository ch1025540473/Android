package com.wezebra.zebraking.behavior;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

import com.wezebra.zebraking.App;
import com.wezebra.zebraking.model.PhotoUploadComponent;
import com.wezebra.zebraking.ui.BaseActivity;
import com.wezebra.zebraking.util.Constants;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by HQDev on 2015/4/22.
 */
public class SelectPhotoOnClickListener implements View.OnClickListener{

    private PhotoUploadComponent photoUploadComponent;
    private BaseActivity activity;
    public int componentPosition;

    public SelectPhotoOnClickListener(PhotoUploadComponent photoUploadComponent,BaseActivity activity) {
        this.photoUploadComponent = photoUploadComponent;
        this.activity = activity;
        this.componentPosition = 0;
    }

    public SelectPhotoOnClickListener(PhotoUploadComponent photoUploadComponent,BaseActivity activity,int componentPosition) {
        this.photoUploadComponent = photoUploadComponent;
        this.activity = activity;
        this.componentPosition = componentPosition;
    }

    @Override
    public void onClick(View v) {

        for (int i=0;i< Constants.UPLOAD_LIMIT;i++) {
            if (v.getId() == photoUploadComponent.upload_layout[i].getId()) {
                photoUploadComponent.focusImg = photoUploadComponent.photo_img[i];
                photoUploadComponent.focus = i;
                showDialog();
            }
        }

    }

    public String createPicPath() {
        String str = null;
        Date date = null;
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        date = new Date();
        str = format.format(date);
        return Constants.IMAGE_DIR + File.separator + str + ".jpg";
    }

    public void showDialog() {

        new AlertDialog.Builder(activity)
                .setTitle("选择图片")
                .setItems(Constants.SELECT_PHOTO, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                Intent openCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                photoUploadComponent.focusPhotoPath = createPicPath();
                                Log.i("picPath", photoUploadComponent.focusPhotoPath);
                                photoUploadComponent.tempPath = Constants.IMAGE_DIR + File.separator + "temp.jpg";
                                Log.i("tempPath",photoUploadComponent.tempPath);
                                File file = new File(Constants.IMAGE_DIR);
                                if (!file.exists()) {
                                    file.mkdirs();
                                }
                                Uri imageUri = Uri.fromFile(new File(photoUploadComponent.tempPath));
                                openCamera.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                                Log.i("Before startAcForResult", "AAA");
                                ((App)(activity.getApplication())).photoUploadComponent = photoUploadComponent.clone();
                                if (componentPosition > 0) {
                                    activity.startActivityForResult(openCamera, 200+componentPosition);
                                } else {
                                    activity.startActivityForResult(openCamera, 200);
                                }

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
                                if (componentPosition > 0) {
                                    activity.startActivityForResult(openAlbum, 100+componentPosition);
                                } else {
                                    activity.startActivityForResult(openAlbum, 100);
                                }
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
