package com.wezebra.zebraking.model;

import com.wezebra.zebraking.util.Base64Digest;

import java.io.Serializable;
import java.util.Date;


public class UserLogin extends User implements Cloneable, Serializable
{
    /**
     *
     */
    private static final long serialVersionUID = -8045722688367258132L;

    /**
     * 用户微信授权的openid
     */
    private String weixin;

    private String password;
    private String encryptPassword;
    private String encryMobile;// 用户登录手机号码
    private String mobile;// 未加密手机号
    private Date loginTime;

    private Integer lockStatus = 1;

    // 用户访问方式
    private ClientType clientType;

    // 用户操作
    private UserOperate operate;

    // 用户登录／退出状态
    private Integer loginStatusInt = -1;
    private LoginStatus loginStatus;

    private int basicStatusInt = -1;
    private Status basicStatus = Status.WAIT_SUBMIT;
    private int identityStatusInt = -1;
    private Status identityStatus = Status.WAIT_SUBMIT;
    private int educationStatusInt = -1;
    private Status educationStatus = Status.WAIT_SUBMIT;
    private int workStatusInt = -1;
    private Status workStatus = Status.WAIT_SUBMIT;


    private int onJobStatusInt = -1;
    private Status onJobStatus = Status.WAIT_SUBMIT;
    private int incomeStatusInt = -1;
    private Status incomeStatus = Status.WAIT_SUBMIT;
    private int socialSecurityStatusInt = -1;
    private Status socialSecurityStatus = Status.WAIT_SUBMIT;
    private int reserveStatusInt = -1;
    private Status reserveStatus = Status.WAIT_SUBMIT;
    private int contactStatusInt = -1;
    private Status contactStatus = Status.WAIT_SUBMIT;

    private int companyEamilInt = -1;
    private Status companyEamil = Status.WAIT_SUBMIT;

    private int mobileStatusInt = -1;
    private Status mobileStatus = Status.WAIT_SUBMIT;
    private int taobaoStatusInt = -1;
    private Status taobaoStatus = Status.WAIT_SUBMIT;
    private int jdStatusInt = -1;
    private Status jdStatus = Status.WAIT_SUBMIT;

    private int initStatusInt = -1; // 基本认证状态
    private Status initStatus = Status.WAIT_SUBMIT;
    private Date examineTime; // 基本认证审核时间

    private Date lastSubmitTime;

    private Date registerDate;
    /**
     * landlord 1 房东,Borrower 2 借款人，Investor 3 投资人
     */
    private Long role = 2L;// 关联角色

    private Integer times = 5;


    public Integer getTimes()
    {
        return times;
    }

    public void setTimes(Integer times)
    {
        this.times = times;
    }

    public enum LoginStatus
    {
        TIME_OUT(-1, "未退出／超时退出"), ACTIVE_OUT(1, "手动退出");
        private String descripte;
        private int state;

        private LoginStatus(int state, String descript)
        {
            this.descripte = descript;
            this.state = state;
        }

        public String getDescripte()
        {
            return this.descripte;
        }

        public int getState()
        {
            return state;
        }
    }

    public enum UserOperate
    {
        SIGN_IN_WECHAT("sign_in_wechat"),
        SIGN_IN_PHONE("sign_in_browser"),
        SIGN_IN_CLIENT("sign_in_client"),
        LOG_OUT("sign_out"),
        AUTO_LOG_OUT("auto_sign_out"),
        REQUEST_STAGING("request_staging");
        private String descripte;

        private UserOperate(String descript)
        {
            this.descripte = descript;
        }

        public String getDescripte()
        {
            return this.descripte;
        }
    }

    public enum ClientType
    {
        WEIXIN("WEIXIN"),
        BROWSER("BROWSER"),
        CLIENT("CLIENT");
        private String descripte;

        private ClientType(String descript)
        {
            this.descripte = descript;
        }

        public String getDescripte()
        {
            return this.descripte;
        }
    }

    private LockedStatus lockedStatus;

    public enum LockedStatus
    {
        UN_LOCKED(1, "未被锁"),
        LOCKED(-1, "被锁");
        private int state;
        private String desc;

        private LockedStatus(int state, String desc)
        {
            this.state = state;
            this.desc = desc;
        }

        public int getState()
        {
            return state;
        }

        public void setState(int state)
        {
            this.state = state;
        }

        public String getDesc()
        {
            return desc;
        }

        public void setDesc(String desc)
        {
            this.desc = desc;
        }

    }

    public LockedStatus getLockedStatus()
    {
        return lockedStatus;
    }

    public void setLockedStatus(LockedStatus lockedStatus)
    {
        this.lockedStatus = lockedStatus;
        this.lockStatus = lockedStatus.getState();
    }

    public UserOperate getOperate()
    {
        return operate;
    }

    public void setOperate(UserOperate operate)
    {
        this.operate = operate;
    }

    public ClientType getClientType()
    {
        return clientType;
    }

    public void setClientType(ClientType clientType)
    {
        this.clientType = clientType;
    }

    public String getWeixin()
    {
        return weixin;
    }

    public void setWeixin(String weixin)
    {
        this.weixin = weixin;
    }

    public String getPassword()
    {
        return password;
    }


    public String getEncryMobile()
    {
        return encryMobile;
    }

    public void setEncryMobile(String encryMobile)
    {
        this.encryMobile = encryMobile;
        this.mobile = Base64Digest.decode(encryMobile);
    }

    public String getEncryptPassword()
    {
        return encryptPassword;

    }

    public void setEncryptPassword(String encryptPassword)
    {
        this.encryptPassword = encryptPassword;
    }

    public Date getLoginTime()
    {
        return loginTime;
    }

    public void setLoginTime(Date loginTime)
    {
        this.loginTime = loginTime;
    }

    public Integer getLockStatus()
    {
        return lockStatus;
    }

    public void setLockStatus(Integer lockStatus)
    {
        this.lockStatus = lockStatus;
        switch (lockStatus)
        {
            case -1:
                setLockedStatus(LockedStatus.LOCKED);
                break;
            case 1:
                setLockedStatus(LockedStatus.UN_LOCKED);
                break;
            default:
                setLockedStatus(LockedStatus.UN_LOCKED);
                break;
        }
    }

    public Integer getLoginStatusInt()
    {
        return loginStatusInt;
    }

    public void setLoginStatusInt(Integer loginStatusInt)
    {
        this.loginStatusInt = loginStatusInt;
        switch (loginStatusInt)
        {
            case -1:
                setLoginStatus(LoginStatus.TIME_OUT);
                break;
            case 1:
                setLoginStatus(LoginStatus.ACTIVE_OUT);
                break;
        }
    }

    public LoginStatus getLoginStatus()
    {
        return loginStatus;
    }

    public void setLoginStatus(LoginStatus loginStatus)
    {
        this.loginStatus = loginStatus;
        this.loginStatusInt = loginStatus.getState();
    }

    @Override
    public UserLogin clone()
    {
        UserLogin userLogin = null;
        try
        {
            userLogin = (UserLogin) super.clone();
        } catch (CloneNotSupportedException e)
        {
            System.out.println(e.getMessage());
        }
        return userLogin;
    }

    public Status getBasicStatus()
    {
        return basicStatus;
    }

    public void setBasicStatus(Status basicStatus)
    {
        this.basicStatus = basicStatus;
        this.basicStatusInt = basicStatus.getState();
    }

    public int getBasicStatusInt()
    {
        return basicStatusInt;
    }

    public void setBasicStatus(int basicStatus)
    {
        this.basicStatusInt = basicStatus;
        switch (basicStatus)
        {
            case -1:
                setBasicStatus(Status.WAIT_SUBMIT);
                break;
            case 1:
                setBasicStatus(Status.AUDITING);
                break;
            case 2:
                setBasicStatus(Status.PASSED);
                break;
            case 3:
                setBasicStatus(Status.UNPASS);
                break;
        }
    }

    public Status getIdentityStatus()
    {
        return identityStatus;
    }

    public void setIdentityStatus(Status identityStatus)
    {
        this.identityStatus = identityStatus;
        this.identityStatusInt = identityStatus.getState();
    }

    public int getIdentityStatusInt()
    {
        return identityStatusInt;
    }

    public void setIdentityStatus(int identityStatus)
    {
        this.identityStatusInt = identityStatus;
        switch (identityStatus)
        {
            case -1:
                setIdentityStatus(Status.WAIT_SUBMIT);
                break;
            case 1:
                setIdentityStatus(Status.AUDITING);
                break;
            case 2:
                setIdentityStatus(Status.PASSED);
                break;
            case 3:
                setIdentityStatus(Status.UNPASS);
                break;
        }
    }

    public Status getEducationStatus()
    {
        return educationStatus;
    }

    public void setEducationStatus(Status educationStatus)
    {
        this.educationStatus = educationStatus;
        this.educationStatusInt = educationStatus.getState();
    }

    public int getEducationStatusInt()
    {
        return educationStatusInt;
    }

    public void setEducationStatus(int educationStatus)
    {
        this.educationStatusInt = educationStatus;
        switch (educationStatus)
        {
            case -1:
                setEducationStatus(Status.WAIT_SUBMIT);
                break;
            case 1:
                setEducationStatus(Status.AUDITING);
                break;
            case 2:
                setEducationStatus(Status.PASSED);
                break;
            case 3:
                setEducationStatus(Status.UNPASS);
                break;
        }
    }

    public Status getWorkStatus()
    {
        return workStatus;
    }

    public void setWorkStatus(Status workStatus)
    {
        this.workStatus = workStatus;
        this.workStatusInt = workStatus.getState();
    }

    public int getWorkStatusInt()
    {
        return workStatusInt;
    }

    public void setWorkStatus(int workStatus)
    {
        this.workStatusInt = workStatus;
        switch (workStatus)
        {
            case -1:
                setWorkStatus(Status.WAIT_SUBMIT);
                break;
            case 1:
                setWorkStatus(Status.AUDITING);
                break;
            case 2:
                setWorkStatus(Status.PASSED);
                break;
            case 3:
                setWorkStatus(Status.UNPASS);
                break;
        }
    }

    public Status getIncomeStatus()
    {
        return incomeStatus;
    }

    public void setIncomeStatus(Status incomeStatus)
    {
        this.incomeStatus = incomeStatus;
        this.incomeStatusInt = incomeStatus.getState();
    }

    public int getIncomeStatusInt()
    {
        return incomeStatusInt;
    }

    public void setIncomeStatus(int incomeStatus)
    {
        this.incomeStatusInt = incomeStatus;
        switch (incomeStatus)
        {
            case -1:
                setIncomeStatus(Status.WAIT_SUBMIT);
                break;
            case 1:
                setIncomeStatus(Status.AUDITING);
                break;
            case 2:
                setIncomeStatus(Status.PASSED);
                break;
            case 3:
                setIncomeStatus(Status.UNPASS);
                break;
        }
    }

    public Status getSocialSecurityStatus()
    {
        return socialSecurityStatus;
    }

    public void setSocialSecurityStatus(Status socialSecurityStatus)
    {
        this.socialSecurityStatus = socialSecurityStatus;
        this.socialSecurityStatusInt = socialSecurityStatus.getState();
    }

    public int getSocialSecurityStatusInt()
    {
        return socialSecurityStatusInt;
    }

    public void setSocialSecurityStatus(int socialSecurityStatus)
    {
        this.socialSecurityStatusInt = socialSecurityStatus;
        switch (socialSecurityStatus)
        {
            case -1:
                setSocialSecurityStatus(Status.WAIT_SUBMIT);
                break;
            case 1:
                setSocialSecurityStatus(Status.AUDITING);
                break;
            case 2:
                setSocialSecurityStatus(Status.PASSED);
                break;
            case 3:
                setSocialSecurityStatus(Status.UNPASS);
                break;
        }
    }

    public Status getReserveStatus()
    {
        return reserveStatus;
    }

    public void setReserveStatus(Status reserveStatus)
    {
        this.reserveStatus = reserveStatus;
        this.reserveStatusInt = reserveStatus.getState();
    }

    public int getReserveStatusInt()
    {
        return reserveStatusInt;
    }

    public void setReserveStatus(int reserveStatus)
    {
        this.reserveStatusInt = reserveStatus;
        switch (reserveStatus)
        {
            case -1:
                setReserveStatus(Status.WAIT_SUBMIT);
                break;
            case 1:
                setReserveStatus(Status.AUDITING);
                break;
            case 2:
                setReserveStatus(Status.PASSED);
                break;
            case 3:
                setReserveStatus(Status.UNPASS);
                break;
        }
    }

    public Status getContactStatus()
    {
        return contactStatus;
    }

    public void setContactStatus(Status contactStatus)
    {
        this.contactStatus = contactStatus;
        this.contactStatusInt = contactStatus.getState();
    }

    public int getContactStatusInt()
    {
        return contactStatusInt;
    }

    public void setContactStatus(int contactStatus)
    {
        this.contactStatusInt = contactStatus;
        switch (contactStatus)
        {
            case -1:
                setContactStatus(Status.WAIT_SUBMIT);
                break;
            case 1:
                setContactStatus(Status.AUDITING);
                break;
            case 2:
                setContactStatus(Status.PASSED);
                break;
            case 3:
                setContactStatus(Status.UNPASS);
                break;
        }
    }

    public Date getLastSubmitTime()
    {
        return lastSubmitTime;
    }

    public void setLastSubmitTime(Date lastSubmitTime)
    {
        this.lastSubmitTime = lastSubmitTime;
    }

    @Override
    public String toString()
    {
        return "UserLogin [encryMobile=" + this.getEncryMobile() + ", mobile=" + this.getMobile() + ",id=" + this.getUserId() + "]";
    }

    public int getMobileStatusInt()
    {
        return mobileStatusInt;
    }

    public void setMobileStatusInt(int mobileStatusInt)
    {
        this.mobileStatusInt = mobileStatusInt;
        switch (mobileStatusInt)
        {
            case -1:
                setMobileStatus(Status.WAIT_SUBMIT);
                break;
            case 1:
                setMobileStatus(Status.AUDITING);
                break;
            case 2:
                setMobileStatus(Status.PASSED);
                break;
            case 3:
                setMobileStatus(Status.UNPASS);
                break;
        }
    }

    public Status getMobileStatus()
    {
        return mobileStatus;
    }

    public void setMobileStatus(Status mobileStatus)
    {
        this.mobileStatus = mobileStatus;
        this.mobileStatusInt = mobileStatus.getState();
    }

    public int getTaobaoStatusInt()
    {
        return taobaoStatusInt;
    }

    public void setTaobaoStatusInt(int taobaoStatusInt)
    {
        this.taobaoStatusInt = taobaoStatusInt;
        switch (taobaoStatusInt)
        {
            case -1:
                setTaobaoStatus(Status.WAIT_SUBMIT);
                break;
            case 1:
                setTaobaoStatus(Status.AUDITING);
                break;
            case 2:
                setTaobaoStatus(Status.PASSED);
                break;
            case 3:
                setTaobaoStatus(Status.UNPASS);
                break;
        }
    }

    public Status getTaobaoStatus()
    {
        return taobaoStatus;
    }

    public void setTaobaoStatus(Status taobaoStatus)
    {
        this.taobaoStatus = taobaoStatus;
        this.taobaoStatusInt = taobaoStatus.getState();
    }

    public int getJdStatusInt()
    {
        return jdStatusInt;

    }

    public void setJdStatusInt(int jdStatusInt)
    {
        this.jdStatusInt = jdStatusInt;
        switch (jdStatusInt)
        {
            case -1:
                setJdStatus(Status.WAIT_SUBMIT);
                break;
            case 1:
                setJdStatus(Status.AUDITING);
                break;
            case 2:
                setJdStatus(Status.PASSED);
                break;
            case 3:
                setJdStatus(Status.UNPASS);
                break;
        }
    }

    public Status getJdStatus()
    {
        return jdStatus;
    }

    public void setJdStatus(Status jdStatus)
    {
        this.jdStatus = jdStatus;
        this.jdStatusInt = jdStatus.getState();
    }

    public Status getInitStatus()
    {
        return initStatus;
    }

    public void setInitStatus(Status initStatus)
    {
        this.initStatus = initStatus;
        this.initStatusInt = initStatus.getState();
    }

    public int getInitStatusInt()
    {
        return initStatusInt;
    }

    public void setInitStatusInt(int initStatusInt)
    {
        this.initStatusInt = initStatusInt;
        switch (initStatusInt)
        {
            case -1:
                setInitStatus(Status.WAIT_SUBMIT);
                break;
            case 1:
                setInitStatus(Status.AUDITING);
                break;
            case 2:
                setInitStatus(Status.PASSED);
                break;
            case 3:
                setInitStatus(Status.UNPASS);
                break;
        }
    }

    public Date getExamineTime()
    {
        return examineTime;
    }

    public void setExamineTime(Date examineTime)
    {
        this.examineTime = examineTime;
    }

    public Date getRegisterDate()
    {
        return registerDate;
    }

    public void setRegisterDate(Date registerDate)
    {
        this.registerDate = registerDate;
    }

    public Boolean isLock()
    {
        return lockStatus == -1;
    }

    public void setBasicStatusInt(int basicStatusInt)
    {
        this.basicStatusInt = basicStatusInt;
    }

    public void setIdentityStatusInt(int identityStatusInt)
    {
        this.identityStatusInt = identityStatusInt;
    }

    public void setEducationStatusInt(int educationStatusInt)
    {
        this.educationStatusInt = educationStatusInt;
    }

    public void setWorkStatusInt(int workStatusInt)
    {
        this.workStatusInt = workStatusInt;
    }

    public void setIncomeStatusInt(int incomeStatusInt)
    {
        this.incomeStatusInt = incomeStatusInt;
    }

    public void setSocialSecurityStatusInt(int socialSecurityStatusInt)
    {
        this.socialSecurityStatusInt = socialSecurityStatusInt;
    }

    public void setReserveStatusInt(int reserveStatusInt)
    {
        this.reserveStatusInt = reserveStatusInt;
    }

    public void setContactStatusInt(int contactStatusInt)
    {
        this.contactStatusInt = contactStatusInt;
    }

    public String getMobile()
    {
        return mobile;
    }

    public Long getRole()
    {
        return role;
    }

    public void setRole(Long role)
    {
        this.role = role;
    }

    public int getOnJobStatusInt() {
        return onJobStatusInt;
    }

    public void setOnJobStatusInt(int onJobStatusInt) {
        this.onJobStatusInt = onJobStatusInt;
    }


    public int getCompanyEamilInt() {
        return companyEamilInt;
    }

    public void setCompanyEamilInt(int companyEamilInt) {
        this.companyEamilInt = companyEamilInt;
    }

}
