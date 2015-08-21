package com.wezebra.zebraking.util;

import android.content.Context;
import android.graphics.Bitmap;

import com.nostra13.universalimageloader.core.assist.FlushedInputStream;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URLConnection;

/**
 * Created by HQDev on 2015/6/3.
 */
public class WezebraImageLoader extends BaseImageDownloader {

    private int connectTimeout;
    private int readTimeout;

    public WezebraImageLoader(Context context) {
        super(context);
        this.connectTimeout = Constants.PICTURE_DOWNLOAD_CONNECTION_TIME_OUT;
        this.readTimeout = Constants.PICTURE_DOWNLOAD_READ_TIME_OUT;
    }

    @Override
    protected InputStream getStreamFromNetwork(String imageUri, Object extra) throws IOException {

        URLConnection conn = URI.create(imageUri).toURL().openConnection();
        conn.setConnectTimeout(connectTimeout);
        conn.setReadTimeout(readTimeout);
        conn.setRequestProperty("Referer","http://www.wezebra.com");

        return new FlushedInputStream(new BufferedInputStream(conn.getInputStream()));

    }

    @Override
    protected InputStream getStreamFromOtherSource(String imageUri, Object extra) throws IOException {

        Bitmap bm = GenericUtils.decodeSampleImage(new File(imageUri), 1024, 1024);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        InputStream isBm = new ByteArrayInputStream(baos.toByteArray());

        return isBm;

    }

}
