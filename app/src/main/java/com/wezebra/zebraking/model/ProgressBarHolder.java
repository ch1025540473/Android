package com.wezebra.zebraking.model;

import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by HQDev on 2015/5/4.
 */
public class ProgressBarHolder {

    private TextView tv;
    private ImageView img;
    private int tv_width;
    private int progress_width;
    private int progress;

    public ProgressBarHolder(TextView tv,ImageView img,int tv_width,int progress_width) {
        this.tv = tv;
        this.img = img;
        this.tv_width = tv_width;
        this.progress_width = progress_width;
    }

    public TextView getTv() {
        return tv;
    }

    public int getTv_width() {
        return tv_width;
    }

    public int getProgress_width() {
        return progress_width;
    }

    public int getProgress() {
        return progress;
    }

    public ImageView getImg() {
        return img;
    }

    public void setTv(TextView tv) {
        this.tv = tv;
    }

    public void setImg(ImageView img) {
        this.img = img;
    }

    public void setTv_width(int tv_width) {
        this.tv_width = tv_width;
    }

    public void setProgress_width(int progress_width) {
        this.progress_width = progress_width;
    }

    public void setProgress(int progress) {
        this.progress = progress;

        tv.setText(progress+"%");
        tv.setX((float)(tv_width + progress_width*((float)progress/100)));
    }

}
