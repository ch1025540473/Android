package com.wezebra.zebraking.model;

import java.util.Date;



/**
 * @author zhuchunlan 认证信息材料
 */
public class AuthMaterial {
    private long   code;
    private long   userId;
    /**
     *  1=身份证 2=工作证明 3=学历证明 6=收入证明 7=社保证明  8=公积金证明 12=格言认证
     */
    private int    materialType;
    private String materialUrl;
    private Date   createTime;
    private Date   updateTime;

    public long getCode() {
        return code;
    }

    public void setCode(long code) {
        this.code = code;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public int getMaterialType() {
        return materialType;
    }

    public void setMaterialType(int materialType) {
        this.materialType = materialType;
    }

    public String getMaterialUrl() {
        return materialUrl;
    }

    public void setMaterialUrl(String materialUrl) {
        this.materialUrl = materialUrl;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

}
