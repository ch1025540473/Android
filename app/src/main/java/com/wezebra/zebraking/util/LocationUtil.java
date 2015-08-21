package com.wezebra.zebraking.util;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.support.v7.app.ActionBarActivity;

import org.apache.http.message.BasicNameValuePair;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Created by HQDev on 2015/5/18.
 */
public class LocationUtil {

    private static LocationManager locationManager;
    private static Location location;
    private static float lng,lat;
    private static ArrayList<BasicNameValuePair> result;

    public static ArrayList<BasicNameValuePair> getLocation(ActionBarActivity actionBarActivity) {
        locationManager = (LocationManager)actionBarActivity.getSystemService(Context.LOCATION_SERVICE);
        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if (location == null) {
            lng = 0.0000000f;
            lat = 0.0000000f;
        } else {
            lng = new BigDecimal(location.getLongitude()).setScale(7,BigDecimal.ROUND_HALF_UP).floatValue();
            lat = new BigDecimal(location.getLatitude()).setScale(7,BigDecimal.ROUND_HALF_UP).floatValue();
        }

        result = new ArrayList<>();
        result.add(new BasicNameValuePair("lng",lng+""));
        result.add(new BasicNameValuePair("lat",lat+""));
        return result;
    }

}
