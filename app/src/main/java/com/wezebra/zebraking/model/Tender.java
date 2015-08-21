package com.wezebra.zebraking.model;

import java.util.Date;

/**
 * Tender entity. @author MyEclipse Persistence Tools
 */

public class Tender implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;

	private String proId;

	private String borrCustId;

	private Long borrTotAmt;// 借款总金额

	// 用于页面显示借款总额
	private Long borrTotAmtFormart;

	private Long amt;// 已募集金额

	// 用于页面显示可投金额，借款总额 - 已募集金额
	private Long tenderAmt;

	private Long alreadyRetAmt;

	private Integer tenderStatusInt;

	private TenderStatus tenderStatus;

	private Date bidStartDate;

	private Date bidEndDate;

	private Date retDate;

	private String guarCompId;

	private Long guarAmt;

	private String proArea;

	private String retType;

	private Long yearRate;
	private String formatRate;// 12%

	private Long retAmt;

	private String dr;

	private Date createDate;

	private Date updateDate;

	private Long stagesNum;

	private Long rateId;

	private Long ordTypeId;

	private Date ordDate;

	private long orderCode;

	private String status;

	// Constructors

	/** default constructor */
	public Tender() {

	}

	public enum TenderStatus {
		WIND_CONTROL_PASS(0, "风控已通过"), WAIT_PUBLISH(1, "待发布"), INVESTING(2, "投资中"), FULL_SCALE(3, "已满标"), WAITING_REPAY(4, "还款中"), SUCCESS(
				5, "已完成"), // 对应前端页面已还款
		CLOSED(6, "已关闭");
		private int state;
		private String description;

		private TenderStatus(int state, String description) {
			this.state = state;
			this.description = description;
		}

		public int getState() {
			return state;
		}

		public String getDescription() {
			return description;
		}

		@Override
		public String toString() {
			return state + "";
		}

	}

	public Integer getTenderStatusInt() {
		return tenderStatusInt;
	}

	public void setTenderStatusInt(Integer tenderStatusInt) {
		this.tenderStatusInt = tenderStatusInt;
		switch (tenderStatusInt) {
		case 0:
			setTenderStatus(TenderStatus.WIND_CONTROL_PASS);
			break;
		case 1:
			setTenderStatus(TenderStatus.WAIT_PUBLISH);
			break;
		case 2:
			setTenderStatus(TenderStatus.INVESTING);
			break;
		case 3:
			setTenderStatus(TenderStatus.FULL_SCALE);
			break;
		case 4:
			setTenderStatus(TenderStatus.WAITING_REPAY);
			break;
		case 5:
			setTenderStatus(TenderStatus.SUCCESS);
			break;
		case 6:
			setTenderStatus(TenderStatus.CLOSED);
			break;

		default:
			break;
		}
	}

	public TenderStatus getTenderStatus() {
		return tenderStatus;
	}

	public void setTenderStatus(TenderStatus tenderStatus) {
		this.tenderStatus = tenderStatus;
		this.tenderStatusInt = tenderStatus.getState();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getProId() {
		return proId;
	}

	public void setProId(String proId) {
		this.proId = proId;
	}

	public Long getBorrTotAmt() {
		return borrTotAmt;
	}

	public void setBorrTotAmt(Long borrTotAmt) {
		this.borrTotAmt = borrTotAmt;
//		if (borrTotAmt != null) {
//			this.borrTotAmtFormart = borrTotAmt / 100;
//		} else {
//			this.borrTotAmtFormart = 0L;
//		}
	}

	public Long getAmt() {
		return amt;
	}

	public void setAmt(Long amt) {
		this.amt = amt;
//		if (borrTotAmt >= amt) {
//			this.tenderAmt = (borrTotAmt - amt) / 100;
//		} else {
//			this.tenderAmt = 0l;
//		}
	}

	public Long getAlreadyRetAmt() {
		return alreadyRetAmt;
	}

	public void setAlreadyRetAmt(Long alreadyRetAmt) {
		this.alreadyRetAmt = alreadyRetAmt;
	}

	public Date getBidStartDate() {
		return bidStartDate;
	}

	public void setBidStartDate(Date bidStartDate) {
		this.bidStartDate = bidStartDate;
	}

	public Date getBidEndDate() {
		return bidEndDate;
	}

	public void setBidEndDate(Date bidEndDate) {
		this.bidEndDate = bidEndDate;
	}

	public Date getRetDate() {
		return retDate;
	}

	public void setRetDate(Date retDate) {
		this.retDate = retDate;
	}

	public String getGuarCompId() {
		return guarCompId;
	}

	public void setGuarCompId(String guarCompId) {
		this.guarCompId = guarCompId;
	}

	public Long getGuarAmt() {
		return guarAmt;
	}

	public void setGuarAmt(Long guarAmt) {
		this.guarAmt = guarAmt;
	}

	public String getProArea() {
		return proArea;
	}

	public void setProArea(String proArea) {
		this.proArea = proArea;
	}

	public String getRetType() {
		return retType;
	}

	public void setRetType(String retType) {
		this.retType = retType;
	}

	public Long getYearRate() {
		return yearRate;
	}

	public void setYearRate(Long yearRate) {
		this.yearRate = yearRate;
//		if (null != yearRate) {
//			Double d = Double.parseDouble(NumberUtil.scaleByPowerOfTenToDecimal(yearRate, 4));
//			DecimalFormat df = new DecimalFormat("#####0");
//			this.formatRate = df.format(d);
//		}
	}

	public String getFormatRate() {
		return formatRate;
	}

	public Long getRetAmt() {
		return retAmt;
	}

	public void setRetAmt(Long retAmt) {
		this.retAmt = retAmt;
	}

	public String getDr() {
		return dr;
	}

	public void setDr(String dr) {
		this.dr = dr;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public Long getStagesNum() {
		return stagesNum;
	}

	public void setStagesNum(Long stagesNum) {
		this.stagesNum = stagesNum;
	}

	public Long getRateId() {
		return rateId;
	}

	public void setRateId(Long rateId) {
		this.rateId = rateId;
	}

	public Long getOrdTypeId() {
		return ordTypeId;
	}

	public void setOrdTypeId(Long ordTypeId) {
		this.ordTypeId = ordTypeId;
	}

	public Date getOrdDate() {
		return ordDate;
	}

	public void setOrdDate(Date ordDate) {
		this.ordDate = ordDate;
	}

	public long getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(long orderCode) {
		this.orderCode = orderCode;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getBorrCustId() {
		return borrCustId;
	}

	public void setBorrCustId(String borrCustId) {
		this.borrCustId = borrCustId;
	}

	public Long getTenderAmt() {
		return tenderAmt;
	}

	public Long getBorrTotAmtFormart() {
		return borrTotAmtFormart;
	}

}
