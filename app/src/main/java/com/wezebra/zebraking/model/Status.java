package com.wezebra.zebraking.model;

/**
 * 用户审核当前状态枚举
 * @author Mars
 *
 */
public enum Status {
	NO_LOGIN(0, "未登录/未注册"),
	UNBOUND_PHONE(10, "未绑定手机"),
	UNBOUND_WECHAT(11, "未绑定微信"),
	
	WAIT_SUBMIT(-1, "未认证"),
	AUDITING(1, "审核中"), 
	PASSED(2, "审核完成"),
	UNPASS(3, "审核不通过");
	
	private int state;
	private String description;

	private Status(int state, String description) {
		this.state = state;
		this.description = description;
	}

	public int getState() {
		return state;
	}

	public String getDescription() {
		return description;
	}
	
	public static Status getStatus(int state){
		switch(state){
			case 1:
				return Status.AUDITING;
			case 2:
				return Status.PASSED;
			case 3:
				return Status.UNPASS;
			case -1:
				return Status.WAIT_SUBMIT;
			default:
				return null;
		}
	}
}
