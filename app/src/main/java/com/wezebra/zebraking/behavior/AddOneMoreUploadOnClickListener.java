package com.wezebra.zebraking.behavior;

import android.view.View;

import com.wezebra.zebraking.model.PhotoUploadComponent;

/**
 * Created by HQDev on 2015/4/22.
 */
public class AddOneMoreUploadOnClickListener implements View.OnClickListener{

    private PhotoUploadComponent photoUploadComponent;

    public AddOneMoreUploadOnClickListener(PhotoUploadComponent photoUploadComponent) {
        this.photoUploadComponent = photoUploadComponent;
    }

    @Override
    public void onClick(View v) {

        if (photoUploadComponent.count < 4) {
            photoUploadComponent.count++;
            photoUploadComponent.upload_layout[photoUploadComponent.count-1].setVisibility(View.VISIBLE);
        } else if (photoUploadComponent.count == 4) {
            photoUploadComponent.count++;
            photoUploadComponent.upload_layout[photoUploadComponent.count-1].setVisibility(View.VISIBLE);
            photoUploadComponent.add_one_more.setVisibility(View.GONE);
        }

    }

}
