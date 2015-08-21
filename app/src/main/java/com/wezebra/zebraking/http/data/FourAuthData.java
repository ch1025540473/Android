package com.wezebra.zebraking.http.data;

/**
 * Created by admin on 2015/7/25.
 */
public class FourAuthData {
    private int basicStatusInt;
    private int identityStatusInt;
    private int onJobStatusInt;
    private int contactStatusInt;
    private int educationStatusInt;
    private int socialSecurityStatusInt;
    private int reserveStatusInt;
    private int companyEamilInt;
    private int orderStatus;
    private String formatMemo;
    private int step;
    private int academicStatus;

    @Override
    public String toString() {
        return super.toString();
    }

    public int getBasicStatusInt() {
        return basicStatusInt;
    }

    public int getIdentityStatusInt() {
        return identityStatusInt;
    }

    public int getOnJobStatusInt() {
        return onJobStatusInt;
    }

    public int getContactStatusInt() {
        return contactStatusInt;
    }

    public int getEducationStatusInt() {
        return educationStatusInt;
    }

    public int getSocialSecurityStatusInt() {
        return socialSecurityStatusInt;
    }

    public int getReserveStatusInt() {
        return reserveStatusInt;
    }

    public int getCompanyEamilInt() {
        return companyEamilInt;
    }

    public int getOrderStatus() {
        return orderStatus;
    }

    public String getFormatMemo() {
        return formatMemo;
    }

    public int getStep() {
        return step;
    }

    public int getAcademicStatus() {
        return academicStatus;
    }

    public void setBasicStatusInt(int basicStatusInt) {
        this.basicStatusInt = basicStatusInt;
    }

    public void setIdentityStatusInt(int identityStatusInt) {
        this.identityStatusInt = identityStatusInt;
    }

    public void setOnJobStatusInt(int onJobStatusInt) {
        this.onJobStatusInt = onJobStatusInt;
    }

    public void setContactStatusInt(int contactStatusInt) {
        this.contactStatusInt = contactStatusInt;
    }

    public void setEducationStatusInt(int educationStatusInt) {
        this.educationStatusInt = educationStatusInt;
    }

    public void setSocialSecurityStatusInt(int socialSecurityStatusInt) {
        this.socialSecurityStatusInt = socialSecurityStatusInt;
    }

    public void setReserveStatusInt(int reserveStatusInt) {
        this.reserveStatusInt = reserveStatusInt;
    }

    public void setCompanyEamilInt(int companyEamilInt) {
        this.companyEamilInt = companyEamilInt;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }

    public void setFormatMemo(String formatMemo) {
        this.formatMemo = formatMemo;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public void setAcademicStatus(int academicStatus) {
        this.academicStatus = academicStatus;
    }
}
