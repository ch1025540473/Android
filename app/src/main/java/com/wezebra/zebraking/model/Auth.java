package com.wezebra.zebraking.model;

import com.wezebra.zebraking.util.DateUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * 
 * @author guofy
 *
 */
public class Auth {
    private long               userId;
    private Integer            status;
    // 状态
    private Status             statusEnum;
    private String             memo;         // 备注

    /**材料列表*/
    private List<AuthMaterial> materials;

    private Date               createTime;   // 创建时间
    private String             createTimeStr; //yyyy-MM-dd HH:mm:ss
    private Date               modifyTime;   // 修改时间
    private String             modifyTimeStr; //yyyy-MM-dd HH:mm:ss

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
        switch (status) {
            case -1:
                this.statusEnum = Status.WAIT_SUBMIT;
                break;
            case 1:
                this.statusEnum = Status.AUDITING;
                break;
            case 2:
                this.statusEnum = Status.PASSED;
                break;
            case 3:
                this.statusEnum = Status.UNPASS;
                break;
        }
    }

    public Status getStatusEnum() {
        return statusEnum;
    }

    public void setStatusEnum(Status statusEnum) {
        this.statusEnum = statusEnum;
        this.status = statusEnum.getState();
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public List<AuthMaterial> getMaterials() {
        return materials;
    }

    public void setMaterials(List<AuthMaterial> materials) {
        this.materials = materials;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
        if (null != createTime) {
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(createTime.getTime());
            this.createTimeStr = DateUtils.toTime(createTime, "yyyy-MM-dd HH:mm:ss");
        }
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
        if (null != modifyTime) {
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(modifyTime.getTime());
            this.modifyTimeStr = DateUtils.toTime(modifyTime, "yyyy-MM-dd HH:mm:ss");
        }
    }

    public String getModifyTimeStr() {
        return modifyTimeStr;
    }

    public String getCreateTimeStr() {
        return createTimeStr;
    }

}
