package com.wezebra.zebraking.model;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Created by HQDev on 2015/4/22.
 */
public class PhotoUploadComponent {

    public int count;
    public LinearLayout add_one_more;
    public FrameLayout[] upload_layout;
    public ImageView[] photo_img;
    public String tempPath;
    public String[] photoPath;
    public String[] uri;

    public ImageView focusImg;
    public String focusPhotoPath;
    public int focus;

    public PhotoUploadComponent(int count,LinearLayout add_one_more,FrameLayout[] upload_layout,ImageView[] photo_img) {
        this.count = count;
        this.add_one_more = add_one_more;
        this.upload_layout = upload_layout;
        this.photo_img = photo_img;
        this.tempPath = null;
        photoPath = new String[5];
        uri = new String[5];
        focusImg = null;
        focusPhotoPath = null;
        focus = 0;
    }

    public PhotoUploadComponent() {

    }

    public PhotoUploadComponent clone() {

        PhotoUploadComponent photoUploadComponent = new PhotoUploadComponent();

        photoUploadComponent.count = this.count;
        photoUploadComponent.add_one_more = this.add_one_more;
        photoUploadComponent.upload_layout = this.upload_layout;
        photoUploadComponent.photo_img = this.photo_img;
        photoUploadComponent.tempPath = this.tempPath;
        photoUploadComponent.photoPath = this.photoPath;
        photoUploadComponent.uri = this.uri;
        photoUploadComponent.focusImg = this.focusImg;
        photoUploadComponent.focusPhotoPath = this.focusPhotoPath;
        photoUploadComponent.focus = this.focus;

        return photoUploadComponent;

    }

    public void showOneMore() {

        if (count < 4) {
            count++;
            upload_layout[count-1].setVisibility(View.VISIBLE);
        } else if (count == 4) {
            count++;
            upload_layout[count-1].setVisibility(View.VISIBLE);
            add_one_more.setVisibility(View.GONE);
        }

    }

    public void showMore(int number) {

        if ( (number < 1) || (number > 4) )
            return;

        for (int i=0;i < number;i++) {

            if (count < 4) {
                count++;
                upload_layout[count - 1].setVisibility(View.VISIBLE);
                add_one_more.setGravity(View.VISIBLE);
            } else if (count == 4) {
                count++;
                upload_layout[count - 1].setVisibility(View.VISIBLE);
                add_one_more.setVisibility(View.GONE);
            }

        }

    }

}
