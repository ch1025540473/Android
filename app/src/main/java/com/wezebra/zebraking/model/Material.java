package com.wezebra.zebraking.model;

import com.wezebra.zebraking.util.DateUtils;

import java.util.Calendar;
import java.util.Date;

/**
 * 用户认证材料
 * 
 * @author guofy
 *
 */
public class Material {
    private long         userId;
    private long         code;
    private long         orderCode;     // 订单id
    private int          type;          // 材料类型
    private MaterialType materialType;  // 材料类型
    private String       uri;           // 图片路径
    private int          status;
    private Status       materialStatus; // 认证状态 -1=未提交审核 1=审核中，2=审核完成，3=审核不通过
    private String       memo;
    private Date         gmtCreate;
    private String       createStr;     // yyyy-MM-dd HH:mm:ss
    private Date         gmtModified;
    private String       modifyStr;     // yyyy-MM-dd HH:mm:ss

    public enum MaterialType {
        IDENTITY(1, "身份证"), 
        WORK(2, "工作证明"), 
        EDUCATION(3, "学历证明"), 
        RENT_CONTACT(4, "租房合同"), 
        NAMED(5,"名片"), 
        INCOME(6,"收入证明"), 
        SECURITY(7,"社保证明"),
        RESERVE( 8,"公积金证明"), 
        PROPERTY(9,"房东产权证明 "), 
        WE_INVOICE(10,"水电气发票 "), 
        OTHERS(-1,"其他"), 
        PROPERTY_WE(11,"房产证、水电气缴费单"),/*改版*/
        MAXIM(12,"格言认证");
        private int    type;
        private String description;

        private MaterialType(int type, String description) {
            this.type = type;
            this.description = description;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getCode() {
        return code;
    }

    public void setCode(long code) {
        this.code = code;
    }

    public long getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(long orderCode) {
        this.orderCode = orderCode;
    }

    public int getType() {
        return type;
    }

    public MaterialType getMaterialType() {
        return materialType;
    }

    public void setMaterialType(MaterialType materialType) {
        this.materialType = materialType;
        this.type = materialType.getType();
    }


	public void setMaterialType(int type) {
		this.type = type;
		switch (type) {
		case -1:
			this.setMaterialType(MaterialType.OTHERS);
			break;
		case 1:
			this.setMaterialType(MaterialType.IDENTITY);
			break;
		case 2:
			this.setMaterialType(MaterialType.WORK);
			break;
		case 3:
			this.setMaterialType(MaterialType.EDUCATION);
			break;
		case 4:
			this.setMaterialType(MaterialType.RENT_CONTACT);
			break;
		case 5:
			this.setMaterialType(MaterialType.NAMED);
			break;
		case 6:
			this.setMaterialType(MaterialType.INCOME);
			break;
		case 7:
			this.setMaterialType(MaterialType.SECURITY);
			break;
		case 8:
			this.setMaterialType(MaterialType.RESERVE);
			break;
		case 9:
			this.setMaterialType(MaterialType.PROPERTY);
			break;
		case 10:
			this.setMaterialType(MaterialType.WE_INVOICE);
			break;
		case 11:
			this.setMaterialType(MaterialType.PROPERTY_WE);
			break;
		}
	}
    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public Status getMaterialStatus() {
        return materialStatus;
    }

    public void setMaterialStatus(Status materialStatus) {
        this.materialStatus = materialStatus;
        this.status = materialStatus.getState();
    }

    public int getStatus() {
        return status;
    }

    public void setMaterialStatus(int materialStatus) {
        this.status = materialStatus;
        switch (materialStatus) {
            case -1:
                setMaterialStatus(Status.WAIT_SUBMIT);
                break;
            case 1:
                setMaterialStatus(Status.AUDITING);
                break;
            case 2:
                setMaterialStatus(Status.PASSED);
                break;
            case 3:
                setMaterialStatus(Status.UNPASS);
                break;
        }
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
        if (null != gmtCreate) {
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(gmtCreate.getTime());
            this.createStr = DateUtils.toTime(gmtCreate, "yyyy-MM-dd HH:mm:ss");
        }
    }

    public String getCreateStr() {
        return createStr;
    }

    public Date getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
        if (null != gmtModified) {
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(gmtModified.getTime());
            this.modifyStr = DateUtils.toTime(gmtModified, "yyyy-MM-dd HH:mm:ss");

        }
    }

    public String getModifyStr() {
        return modifyStr;
    }

}
