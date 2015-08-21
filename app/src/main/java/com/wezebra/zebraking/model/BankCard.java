package com.wezebra.zebraking.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * BankCard entity. @author MyEclipse Persistence Tools
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class BankCard implements java.io.Serializable
{

    // Fields

    /**
     *
     */
    private static final long serialVersionUID = 1L;
//	private Long id;
//	private String isdefalut = "1";// 1是,0不是

    //	/**
//	 * 1借记卡 2贷记卡 3借贷一体卡4借记卡
//	 */
//	private Long bankType;// 银行类型id
//	private String bankTypeStr;
    private String code;// 银行类型 ICBC。。
    private String bankName;// 银行名称

    private String openAcctId;// 开户银行卡号
    private String formatAcctId;// 格式化银行卡号

//	private String trxId;// 汇付交易唯一标识
//	private Long userId;
////	private String dr = CommonResource.INIT_DELETE_STATUS;
//	private Date createDate;
//	private Date updateDate;
//	private Long orderType;
//
//	private String status;

    // Constructors

    /**
     * default constructor
     */
    public BankCard()
    {
    }

    /**
     * full constructor
     */
//	public BankCard(String isdefalut, Long bankType, String openAcctId, String trxId, Long userId, String dr, Date createDate,
//			Date updateDate, Long orderType) {
//		this.isdefalut = isdefalut;
//		this.bankType = bankType;
//		this.openAcctId = openAcctId;
//		this.trxId = trxId;
//		this.userId = userId;
//		this.dr = dr;
//		this.createDate = createDate;
//		this.updateDate = updateDate;
//		this.orderType = orderType;
//	}

    // Property accessors

//	public Long getId() {
//		return this.id;
//	}
//
//	public void setId(Long id) {
//		this.id = id;
//	}
//
//	public String getIsdefalut() {
//		return this.isdefalut;
//	}
//
//	public void setIsdefalut(String isdefalut) {
//		this.isdefalut = isdefalut;
//	}
//
//	public Long getBankType() {
//		return this.bankType;
//	}
//
//	public void setBankType(Long bankType) {
//		this.bankType = bankType;
//		if (bankType == 1) {
//			this.bankTypeStr = "bankTypeStr";
//		}
//	}
    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
//		this.bankTypeStr = code.toLowerCase();
    }

    public String getBankName()
    {
        return bankName;
    }

    public void setBankName(String bankName)
    {
        this.bankName = bankName;
    }

    public String getOpenAcctId()
    {
        return this.openAcctId;
    }

    public void setOpenAcctId(String openAcctId)
    {
        this.openAcctId = openAcctId;
//		if (!StringUtils.isBlank(openAcctId)) {
//			this.formatAcctId = DataFormatUtils.formatStr(4, 8, 4, openAcctId);
//		}
    }

    public String getFormatAcctId()
    {
        return formatAcctId;
    }

//	public String getTrxId() {
//		return this.trxId;
//	}
//
//	public void setTrxId(String trxId) {
//		this.trxId = trxId;
//	}
//
//	public Long getUserId() {
//		return this.userId;
//	}
//
//	public void setUserId(Long userId) {
//		this.userId = userId;
//	}

//	public String getDr() {
//		return this.dr;
//	}
//
//	public void setDr(String dr) {
//		this.dr = dr;
//	}

//	public Date getCreateDate() {
//		return this.createDate;
//	}
//
//	public void setCreateDate(Date createDate) {
//		this.createDate = createDate;
//	}
//
//	public Date getUpdateDate() {
//		return this.updateDate;
//	}
//
//	public void setUpdateDate(Date updateDate) {
//		this.updateDate = updateDate;
//	}
//
//	public Long getOrderType() {
//		return this.orderType;
//	}
//
//	public void setOrderType(Long orderType) {
//		this.orderType = orderType;
//	}
//
//	public String getStatus() {
//		return status;
//	}
//
//	public void setStatus(String status) {
//		this.status = status;
//	}
//
//	public String getBankTypeStr() {
//		return bankTypeStr;
//	}
//
//	public void setBankTypeStr(String bankTypeStr) {
//		this.bankTypeStr = bankTypeStr;
//	}

    public void setFormatAcctId(String formatAcctId)
    {
        this.formatAcctId = formatAcctId;
    }

}