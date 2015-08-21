package com.wezebra.zebraking.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;

/**
 * Created by HQDev on 2015/6/29.
 */
public class OnePhotoUploadComponent implements Parcelable {

    public ImageView photo_img;
    public String tempPath;
    public String photoPath;
    public String uri;

    public OnePhotoUploadComponent(ImageView photo_img) {
        this.photo_img = photo_img;
        this.tempPath = null;
        this.photoPath = null;
        this.uri = null;
    }

    public ImageView getPhoto_img() {
        return photo_img;
    }

    public String getTempPath() {
        return tempPath;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public String getUri() {
        return uri;
    }

    public void setPhoto_img(ImageView photo_img) {
        this.photo_img = photo_img;
    }

    public void setTempPath(String tempPath) {
        this.tempPath = tempPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(tempPath);
        dest.writeString(photoPath);
        dest.writeString(uri);

    }

    public static final Parcelable.Creator<OnePhotoUploadComponent> CREATOR = new Parcelable.Creator<OnePhotoUploadComponent>() {


        @Override
        public OnePhotoUploadComponent createFromParcel(Parcel source) {
            return new OnePhotoUploadComponent(source);
        }

        @Override
        public OnePhotoUploadComponent[] newArray(int size) {
            return new OnePhotoUploadComponent[size];
        }

    };

    private OnePhotoUploadComponent(Parcel in) {

        tempPath = in.readString();
        photoPath = in.readString();
        uri = in.readString();

    }

}
