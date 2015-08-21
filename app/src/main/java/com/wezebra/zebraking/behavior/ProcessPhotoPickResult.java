package com.wezebra.zebraking.behavior;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;

import com.wezebra.zebraking.App;
import com.wezebra.zebraking.model.PhotoUploadComponent;
import com.wezebra.zebraking.util.FileUtils;
import com.wezebra.zebraking.util.GenericUtils;
import com.wezebra.zebraking.util.L;
import com.wezebra.zebraking.util.ThreadPoolUtil;

import java.io.File;
import java.io.IOException;

/**
 * Created by HQDev on 2015/4/22.
 */
public class ProcessPhotoPickResult {

    public static final void process(PhotoUploadComponent photoUploadComponent,Activity activity, int requestCode, int resultCode, Intent data) {

        if (resultCode == Activity.RESULT_OK)
        {
            switch (requestCode / 10)
            {
                case 10:
                    Uri uri = data.getData();
                    ContentResolver cr = activity.getContentResolver();
                    photoUploadComponent.focusPhotoPath = GenericUtils.getFilePathFromContentUri(uri, cr);
                    photoUploadComponent.photoPath[photoUploadComponent.focus] = photoUploadComponent.focusPhotoPath;

                    Bitmap bmp = GenericUtils.decodeSampleImage(new File(photoUploadComponent.focusPhotoPath), 1024, 1024);

                    photoUploadComponent.focusImg.setScaleType(ImageView.ScaleType.FIT_XY);
                    photoUploadComponent.focusImg.setImageBitmap(bmp);
                    break;
                case 20:
                    String sdStatus = Environment.getExternalStorageState();
                    if (!sdStatus.equals(Environment.MEDIA_MOUNTED))
                    { // 检测sd是否可用
                        Log.i("No SDCard", "...");
                        return;
                    }

                    Log.i("PicProcess","...");
                    if (photoUploadComponent.tempPath == null) {
                        Log.i("photoUploadComponent","null");
                        photoUploadComponent = ((App)(activity.getApplication())).photoUploadComponent.clone();
                    } else {
                        Log.i("photoUploadComponent",photoUploadComponent.toString());
                        Log.i("tempPath",photoUploadComponent.tempPath);
                    }

                    Bitmap bm = GenericUtils.decodeSampleImage(new File(photoUploadComponent.tempPath), 1024, 1024);
                    final String t = photoUploadComponent.tempPath;
                    final String f = photoUploadComponent.focusPhotoPath;
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

                    photoUploadComponent.photoPath[photoUploadComponent.focus] = photoUploadComponent.focusPhotoPath;
                    photoUploadComponent.focusImg.setScaleType(ImageView.ScaleType.FIT_XY);
                    photoUploadComponent.focusImg.setImageBitmap(bm);
                    break;
                default:
                    break;
            }
        }

        for (int i=0;i<5;i++) {
            System.out.println(i + " : " + photoUploadComponent.photoPath[i]);
        }

    }

}
