package com.wezebra.zebraking.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * Created by HQDev on 2015/6/2.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Banner {

    private String adAndroidurl;
    private String adName;
    private String adTarget;

    public String getAdAndroidurl()
    {
        return adAndroidurl;
    }

    public void setAdAndroidurl(String adAndroidurl)
    {
        this.adAndroidurl = adAndroidurl;
    }

    public String getAdName() {
        return adName;
    }

    public String getAdTarget() {
        return adTarget;
    }

    public void setAdName(String adName) {
        this.adName = adName;
    }

    public void setAdTarget(String adTarget) {
        this.adTarget = adTarget;
    }

}
