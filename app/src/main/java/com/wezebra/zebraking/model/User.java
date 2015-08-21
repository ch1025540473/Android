package com.wezebra.zebraking.model;

import com.wezebra.zebraking.util.Base64Digest;
import com.wezebra.zebraking.util.DateUtils;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;


public class User implements Serializable
{

    private static final long serialVersionUID = -8045722688367258133L;

    private String totalAmt;// 总金额
    private String freezeAmt;// 冻结金额
    private String availableAmt;// 可用余额

    private String usrCustId;// 汇付账户

    /**
     * 用户ID
     */
    private long userId;
    /**
     * 用户名称（未加密）
     */
    private String userName;
    /**
     * 用户名称（加密）
     */
    private String encryptName;

    /**
     * 用户QQ号码
     */
    private String QQ;
    /**
     * 加密QQ
     */
    private String encryptQQ;
    /**
     * 身份证号码（未加密）
     */
    private String identity;
    /**
     * 身份证号码（加密）
     */
    private String encryptIdentity;
    /**
     * 所在城市
     */
    private Integer cityId;
    private String cityName;
    /**
     * 最高学历-1=高中以下，1=高中，2=专科，3=本科，4=硕士，5=博士
     */
    private Integer education;
    /**
     * 毕业学校
     */
    private String graduateSchool;
    /**
     * 毕业时间
     */
    private String graduateTime;
    /**
     * 职业状态1=在工作，2=无业
     */
    private Integer jobStatus;
    /**
     * 学业状态 1=在校生，2=已毕业
     */
    private Integer academicStatus;
    /**
     * 公司名字
     */
    private String company;
    /**
     * 职务
     */
    private String position;
    /**
     * 入职日期
     */
    private String workTime;
    /**
     * 公司规模 1=50人以下 2=50-200 3=200-500 4=500-1000 5=1千人以上 6=1万人以上
     */
    private Integer companyScale;
    /**
     * 是否有收入 1=有 2=无
     */
    private Integer hasIncome;
    /**
     * 月收入1=3000以下， 2=3000-5000 ，3=5000-8000
     * ，4=8000-12000，5=12000-20000，6=12000以上
     */
    private Integer salary;
    /**
     * 收入来源 1=兼职/打工，2=参加实习，3=其他
     */
    private Integer salaryType;
    /**
     * 是否有社保 1=没有，2=有
     */
    private Integer hasSecurity;
    /**
     * 是否有公积金 1=无，2=有
     */
    private Integer hasReserve;
    /**
     * 是否有信用卡 1=无，2=有
     */
    private Integer hasCard;
    /**
     * 信用卡额度
     */
    private Integer cardQuota;
    /**
     * 信用卡是否有逾期1=无 2=有
     */
    private Integer hasOverdue;
    /**
     * 在读学历-1=高中以下，1=高中，2=专科，3=本科，4=硕士，5=博士
     */
    private Integer onEducation;
    /**
     * 入学时间
     */
    private String schoolInTime;
    /**
     * 用户填写的步骤
     */
    private int step;
    /**
     * 用户当前资料状态
     */
    private Integer statusInt;
    private Status status;
    /**
     * 备注
     */
    private String memo;
    private String ipAddress;
    private String srcName;
    private Date gmtCreate;
    private String gmtCreateStr; // yyyy-MM-dd HH:mm:ss

    private String userFrom = "无";

    private Date gmtModified;

    public String getIpAddress()
    {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress)
    {
        this.ipAddress = ipAddress;
    }

    public String getSrcName()
    {
        return srcName;
    }

    public void setSrcName(String srcName)
    {
        this.srcName = srcName;
    }

    public String getQQ()
    {
        return QQ;
    }

    public void setQQ(String qQ)
    {
        QQ = qQ;
    }

    public String getEncryptQQ()
    {
        return encryptQQ;
    }

    public void setEncryptQQ(String encryptQQ)
    {
        this.encryptQQ = encryptQQ;
        try
        {
            this.setQQ(Base64Digest.decode(encryptQQ));
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public String getMemo()
    {
        return memo;
    }

    public void setMemo(String memo)
    {
        this.memo = memo;
    }

    public int getStep()
    {
        return step;
    }

    public void setStep(int step)
    {
        this.step = step;
    }

    public long getUserId()
    {
        return userId;
    }

    public void setUserId(long userId)
    {
        this.userId = userId;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getEncryptName()
    {
        return encryptName;
    }

    public void setEncryptName(String encryptName)
    {
        this.encryptName = encryptName;
        try
        {
            this.setUserName(Base64Digest.decode(encryptName));
        } catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    public String getIdentity()
    {
        return identity;
    }

    public void setIdentity(String identity)
    {
        this.identity = identity;
    }

    public String getEncryptIdentity()
    {
        return encryptIdentity;
    }

    public void setEncryptIdentity(String encryptIdentity)
    {
        this.encryptIdentity = encryptIdentity;
        try
        {
            this.setIdentity(Base64Digest.decode(encryptIdentity));
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public Integer getEducation()
    {
        return education;
    }

    public void setEducation(Integer education)
    {
        this.education = education;
    }

    public String getGraduateSchool()
    {
        return graduateSchool;
    }

    public void setGraduateSchool(String graduateSchool)
    {
        this.graduateSchool = graduateSchool;
    }

    public Integer getJobStatus()
    {
        return jobStatus;
    }

    public void setJobStatus(Integer jobStatus)
    {
        this.jobStatus = jobStatus;
    }

    public Integer getAcademicStatus()
    {
        return academicStatus;
    }

    public void setAcademicStatus(Integer academicStatus)
    {
        this.academicStatus = academicStatus;
    }

    public String getCompany()
    {
        return company;
    }

    public void setCompany(String company)
    {
        this.company = company;
    }

    public String getPosition()
    {
        return position;
    }

    public void setPosition(String position)
    {
        this.position = position;
    }

    public Integer getCompanyScale()
    {
        return companyScale;
    }

    public void setCompanyScale(Integer companyScale)
    {
        this.companyScale = companyScale;
    }

    public Integer getHasIncome()
    {
        return hasIncome;
    }

    public void setHasIncome(Integer hasIncome)
    {
        this.hasIncome = hasIncome;
    }

    public Integer getHasSecurity()
    {
        return hasSecurity;
    }

    public void setHasSecurity(Integer hasSecurity)
    {
        this.hasSecurity = hasSecurity;
    }

    public Integer getHasReserve()
    {
        return hasReserve;
    }

    public void setHasReserve(Integer hasReserve)
    {
        this.hasReserve = hasReserve;
    }

    public Integer getHasCard()
    {
        return hasCard;
    }

    public void setHasCard(Integer hasCard)
    {
        this.hasCard = hasCard;
    }

    public Integer getCardQuota()
    {
        return cardQuota;
    }

    public void setCardQuota(Integer cardQuota)
    {
        this.cardQuota = cardQuota;
    }

    public Integer getHasOverdue()
    {
        return hasOverdue;
    }

    public void setHasOverdue(Integer hasOverdue)
    {
        this.hasOverdue = hasOverdue;
    }

    public String getGraduateTime()
    {
        return graduateTime;
    }

    public void setGraduateTime(String graduateTime)
    {
        this.graduateTime = graduateTime;
    }

    public String getWorkTime()
    {
        return workTime;
    }

    public void setWorkTime(String workTime)
    {
        this.workTime = workTime;
    }

    public Status getStatus()
    {
        return status;
    }

    public void setStatus(Status status)
    {
        this.status = status;
        this.statusInt = status.getState();
    }

    public Integer getStatusInt()
    {
        return statusInt;
    }

    public void setStatus(Integer state)
    {
        this.statusInt = state;
        switch (state)
        {
            case -1:
                setStatus(Status.WAIT_SUBMIT);
                break;
            case 1:
                setStatus(Status.AUDITING);
                break;
            case 2:
                setStatus(Status.PASSED);
                break;
            case 3:
                setStatus(Status.UNPASS);
                break;
        }
    }

    public Integer getCityId()
    {
        return cityId;
    }

    public void setCityId(Integer cityId)
    {
        this.cityId = cityId;
    }

    public String getCityName()
    {
        return cityName;
    }

    public void setCityName(String cityName)
    {
        this.cityName = cityName;
    }

    public Integer getSalary()
    {
        return salary;
    }

    public void setSalary(Integer salary)
    {
        this.salary = salary;
    }

    public Integer getSalaryType()
    {
        return salaryType;
    }

    public void setSalaryType(Integer salaryType)
    {
        this.salaryType = salaryType;
    }

    public Integer getOnEducation()
    {
        return onEducation;
    }

    public void setOnEducation(Integer onEducation)
    {
        this.onEducation = onEducation;
    }

    public String getSchoolInTime()
    {
        return schoolInTime;
    }

    public void setSchoolInTime(String schoolInTime)
    {
        this.schoolInTime = schoolInTime;
    }

    public Date getGmtCreate()
    {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate)
    {
        this.gmtCreate = gmtCreate;
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(gmtCreate.getTime());
        this.gmtCreateStr = DateUtils.toTime(cal.getTime(), "yyyy-MM-dd HH:mm:ss");
    }

    public String getGmtCreateStr()
    {
        return gmtCreateStr;
    }

    public Date getGmtModified()
    {
        return gmtModified;
    }

    public void setGmtModified(Date gmtModified)
    {
        this.gmtModified = gmtModified;
    }

    /**
     * user detail
     */
    private UserDetail userDetail;

    public UserDetail getUserDetail()
    {
        return userDetail;
    }

    public void setUserDetail(UserDetail userDetail)
    {
        this.userDetail = userDetail;
    }

    public void setStatusInt(int statusInt)
    {
        this.statusInt = statusInt;
    }

    public void setGmtCreateStr(String gmtCreateStr)
    {
        this.gmtCreateStr = gmtCreateStr;
    }

    public String getTotalAmt()
    {
        return totalAmt;
    }

    public void setTotalAmt(String totalAmt)
    {
        double b = Double.parseDouble(totalAmt) / 100;
        DecimalFormat df = new DecimalFormat("#####0.00");
        this.totalAmt = String.valueOf(df.format(b));
    }

    public String getFreezeAmt()
    {
        return freezeAmt;
    }

    public void setFreezeAmt(String freezeAmt)
    {
        double b = Double.parseDouble(freezeAmt) / 100;
        DecimalFormat df = new DecimalFormat("#####0.00");
        this.freezeAmt = String.valueOf(df.format(b));
    }

    public String getAvailableAmt()
    {
        return availableAmt;
    }

    public void setAvailableAmt(String availableAmt)
    {
        double b = Double.parseDouble(availableAmt) / 100;
        DecimalFormat df = new DecimalFormat("#####0.00");
        this.availableAmt = String.valueOf(df.format(b));
    }

    public String getUsrCustId()
    {
        return usrCustId;
    }

    public void setUsrCustId(String usrCustId)
    {
        this.usrCustId = usrCustId;
    }

    public String getUserFrom()
    {
        return userFrom;
    }

    public void setUserFrom(String userFrom)
    {
        this.userFrom = userFrom;
    }

    @Override
    public String toString()
    {
        return "User [userId=" + userId + ", userName=" + userName + ", QQ=" + QQ + ", encryptQQ=" + encryptQQ + ", encryptName="
                + encryptName + ", identity=" + identity + ", encryptIdentity=" + encryptIdentity + ", cityId=" + cityId + ", cityName="
                + cityName + ", education=" + education + ", graduateSchool=" + graduateSchool + ", graduateTime=" + graduateTime
                + ", jobStatus=" + jobStatus + ", academicStatus=" + academicStatus + ", company=" + company + ", position=" + position
                + ", workTime=" + workTime + ", companyScale=" + companyScale + ", hasIncome=" + hasIncome + ", salary=" + salary
                + ", salaryType=" + salaryType + ", hasSecurity=" + hasSecurity + ", hasReserve=" + hasReserve + ", hasCard=" + hasCard
                + ", cardQuota=" + cardQuota + ", hasOverdue=" + hasOverdue + ", onEducation=" + onEducation + ", schoolInTime="
                + schoolInTime + ", step=" + step + ", statusInt=" + statusInt + ", status=" + status + ", memo=" + memo + ", ipAddress="
                + ipAddress + ", srcName=" + srcName + ", gmtCreate=" + gmtCreate + ", gmtCreateStr=" + gmtCreateStr + ", gmtModified="
                + gmtModified + ", userDetail=" + userDetail + "]";
    }

    private String residenceAddr;
    private Integer lifeType;
    private Integer monthlyPayType;
    private Integer marriageType;

    public void setResidenceAddr(String residenceAddr)
    {
        this.residenceAddr = residenceAddr;
    }

    public void setLifeType(Integer lifeType)
    {
        this.lifeType = lifeType;
    }

    public void setMonthlyPayType(Integer monthlyPayType)
    {
        this.monthlyPayType = monthlyPayType;
    }

    public void setMarriageType(Integer marriageType)
    {
        this.marriageType = marriageType;
    }

    public String getResidenceAddr()
    {

        return residenceAddr;
    }

    public Integer getLifeType()
    {
        return lifeType;
    }

    public Integer getMonthlyPayType()
    {
        return monthlyPayType;
    }

    public Integer getMarriageType()
    {
        return marriageType;
    }
}
