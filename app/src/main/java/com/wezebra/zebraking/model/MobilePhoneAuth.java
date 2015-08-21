package com.wezebra.zebraking.model;

/**
 * Created by HQDev on 2015/5/25.
 */
public class MobilePhoneAuth {

    private String mobilePhoneNumber;
    private int index;
    private String authCode;
    private String smsCode;
    private String source;
    private String url;

    //记录是否需要验证码，详细请查看API文档
    private Boolean isNeed;

    private int nextStep;

    public String getMobilePhoneNumber() {
        return mobilePhoneNumber;
    }

    public int getIndex() {
        return index;
    }

    public String getAuthCode() {
        return authCode;
    }

    public String getSmsCode() {
        return smsCode;
    }

    public String getSource() {
        return source;
    }

    public void setMobilePhoneNumber(String mobilePhoneNumber) {
        this.mobilePhoneNumber = mobilePhoneNumber;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    public void setSmsCode(String smsCode) {
        this.smsCode = smsCode;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Boolean getIsNeed() {
        return isNeed;
    }

    public void setIsNeed(Boolean isNeed) {
        this.isNeed = isNeed;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getNextStep() {
        return nextStep;
    }

    public void setNextStep(int nextStep) {
        this.nextStep = nextStep;
    }

}
