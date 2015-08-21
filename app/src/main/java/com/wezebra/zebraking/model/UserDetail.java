package com.wezebra.zebraking.model;

import java.io.Serializable;


public class UserDetail implements Serializable {
	private static final long serialVersionUID = 1928032749572341695L;

	private Integer id;

	private Long userId;

	private Integer provinceId;

	private String residenceAddr;

	/**
	 * 生活来源 <br>
	 * 1=以往存款<br>
	 * 2=父母资助<br>
	 * 3=亲戚资助<br>
	 * 4=朋友资助<br>
	 */
	private Integer lifeType;

	/**
	 * 月均支出<br>
	 * 1 = 1000以下 <br>
	 * 2 = 1000~200 <br>
	 * 3 = 2000~300 <br>
	 * 4 = 3000~500 <br>
	 * 5 = 5000~800 <br>
	 * 5 = 8000~120 <br>
	 * 7 = 12000以上 <br>
	 */
	private Integer monthlyPayType;

	/**
	 * 婚姻状况 <br>
	 * 1=已婚 <br>
	 * 2=未婚 <br>
	 * 3=离异 <br>
	 */
	private Integer marriageType;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Integer getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(Integer provinceId) {
		this.provinceId = provinceId;
	}

	public String getResidenceAddr() {
		return residenceAddr;
	}

	public void setResidenceAddr(String residenceAddr) {
		this.residenceAddr = residenceAddr;
	}

	public Integer getLifeType() {
		return lifeType;
	}

	public void setLifeType(Integer lifeType) {
		this.lifeType = lifeType;
	}

	public Integer getMonthlyPayType() {
		return monthlyPayType;
	}

	public void setMonthlyPayType(Integer monthlyPayType) {
		this.monthlyPayType = monthlyPayType;
	}

	public Integer getMarriageType() {
		return marriageType;
	}

	public void setMarriageType(Integer marriageType) {
		this.marriageType = marriageType;
	}
}