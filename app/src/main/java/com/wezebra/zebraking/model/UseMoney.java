package com.wezebra.zebraking.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * UseMoney entity. @author MyEclipse Persistence Tools
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class UseMoney implements java.io.Serializable {

	// Fields

	/**
	 * 意义，目的和功能，以及被用到的地方<br>
	 */
	private static final long serialVersionUID = 8337182009258881890L;
//	private Long id;
//	private Long totalAmt;// 总金额
	private String totalAmtStr;// 格式化总金额0.00（元）
//	private Long freezeAmt;// 冻结金额
	private String freezeAmtStr;// 格式化冻结金额0.00（元）
//	private Long tenderAmt;// 投出金额
	private String tenderAmtStr;// 格式化投出金额0.00（元）

	private String availableAmtStr;// 格式化可用金额0.00（元）

//	private Long userId;
//	private String dr = CommonResource.INIT_DELETE_STATUS;
//	private Date createDate;
//	private Date updateDate;

	// Constructors

	/** default constructor */
	public UseMoney() {
	}

//	/** full constructor */
//	public UseMoney(Long totalAmt, Long freezeAmt, Long tenderAmt, Long userId, String dr, Date createDate, Date updateDate) {
//		this.totalAmt = totalAmt;
//		this.freezeAmt = freezeAmt;
//		this.tenderAmt = tenderAmt;
//		this.userId = userId;
////		this.dr = dr;
//		this.createDate = createDate;
//		this.updateDate = updateDate;
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
//	public Long getTotalAmt() {
//		return this.totalAmt;
//	}
//
//	public void setTotalAmt(Long totalAmt) {
//		this.totalAmt = totalAmt;
//		if (null != totalAmt) {
////			this.totalAmtStr = StringUtil.formatAmtForLong(totalAmt);
//		}
//	}

	public void setTotalAmtStr(String totalAmtStr)
	{
		this.totalAmtStr = totalAmtStr;
	}
	public String getTotalAmtStr() {
		return totalAmtStr;
	}

//	public Long getFreezeAmt() {
//		return this.freezeAmt;
//	}
//
//	public void setFreezeAmt(Long freezeAmt) {
//		this.freezeAmt = freezeAmt;
//		if (null != freezeAmt) {
////			this.freezeAmtStr = StringUtil.formatAmtForLong(freezeAmt);
//		}
//	}

	public void setFreezeAmtStr(String freezeAmtStr)
	{
		this.freezeAmtStr = freezeAmtStr;
	}
	public String getFreezeAmtStr() {
		return freezeAmtStr;
	}

//	public Long getTenderAmt() {
//		return this.tenderAmt;
//	}
//
//	public void setTenderAmt(Long tenderAmt) {
//		this.tenderAmt = tenderAmt;
//		if (null != tenderAmt) {
////			this.tenderAmtStr = StringUtil.formatAmtForLong(tenderAmt);
//		}
//	}
	public void setTenderAmtStr(String tenderAmtStr)
	{
		this.tenderAmtStr = tenderAmtStr;
	}

	public String getTenderAmtStr() {
		return tenderAmtStr;
	}

	public void setAvailableAmtStr(String availableAmtStr)
	{
		this.availableAmtStr = availableAmtStr;
	}
	public String getAvailableAmtStr() {
//		Long availabelAmt = totalAmt - freezeAmt;
//		this.availableAmtStr = StringUtil.formatAmtForLong(availabelAmt);
		return availableAmtStr;
	}

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

}
