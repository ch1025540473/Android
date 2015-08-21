package com.wezebra.zebraking.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * HfAccount entity. @author MyEclipse Persistence Tools
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class HfAccount implements java.io.Serializable
{

    // Fields

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    //	private Long id;// userid 斑马用户id
    private String usrCustId;// 汇付账户
    //	private String trxId;// 汇付交易号
//	private String dr = CommonResource.INIT_DELETE_STATUS;// 删除状态
//	private Date createDate;
//	private Date updateDate;
//	private String usrId;// 汇付用户昵称
//	private String idType;// id类型：身份证。。
//	private String idNo;// 身份标识号：身份证号。。
    private String usrMp;// 手机号
//    private String usrEmail;// 邮箱
    private String usrName;// 用户真实姓名

//    private String status;

    // Constructors

    /**
     * default constructor
     */
    public HfAccount()
    {
    }

    /**
     * minimal constructor
     */
    public HfAccount(String usrCustId)
    {
        this.usrCustId = usrCustId;
    }

    /**
     * full constructor
     */
//	public HfAccount(String usrCustId, String trxId, String dr, Date createDate, Date updateDate) {
//		this.usrCustId = usrCustId;
//		this.trxId = trxId;
//		this.dr = dr;
//		this.createDate = createDate;
//		this.updateDate = updateDate;
//	}

    // Property accessors
//    public Long getId()
//    {
//        return this.id;
//    }
//
//    public void setId(Long id)
//    {
//        this.id = id;
//    }

    public String getUsrCustId()
    {
        return this.usrCustId;
    }

    public void setUsrCustId(String usrCustId)
    {
        this.usrCustId = usrCustId;
    }

//    public String getTrxId()
//    {
//        return this.trxId;
//    }
//
//    public void setTrxId(String trxId)
//    {
//        this.trxId = trxId;
//    }

//	public String getDr() {
//		return this.dr;
//	}
//
//	public void setDr(String dr) {
//		this.dr = dr;
//	}

//    public Date getCreateDate()
//    {
//        return this.createDate;
//    }
//
//    public void setCreateDate(Date createDate)
//    {
//        this.createDate = createDate;
//    }
//
//    public Date getUpdateDate()
//    {
//        return this.updateDate;
//    }
//
//    public void setUpdateDate(Date updateDate)
//    {
//        this.updateDate = updateDate;
//    }
//
//    @Override
//    public String toString()
//    {
//        return "HfAccount [id=" + id + ", usrCustId=" + usrCustId + ", trxId=" + trxId + ", createDate=" + createDate
//                + ", updateDate=" + updateDate + ", userName=" + usrId + "]";
//    }

//    public String getStatus()
//    {
//        return status;
//    }
//
//    public void setStatus(String status)
//    {
//        this.status = status;
//    }
//
//    public String getUsrId()
//    {
//        return usrId;
//    }
//
//    public void setUsrId(String usrId)
//    {
//        this.usrId = usrId;
//    }
//
//    public String getIdType()
//    {
//        return idType;
//    }
//
//    public void setIdType(String idType)
//    {
//        this.idType = idType;
//    }
//
//    public String getIdNo()
//    {
//        return idNo;
//    }
//
//    public void setIdNo(String idNo)
//    {
//        this.idNo = idNo;
//    }

    public String getUsrMp()
    {
        return usrMp;
    }

    public void setUsrMp(String usrMp)
    {
        this.usrMp = usrMp;
    }

//    public String getUsrEmail()
//    {
//        return usrEmail;
//    }
//
//    public void setUsrEmail(String usrEmail)
//    {
//        this.usrEmail = usrEmail;
//    }

    public String getUsrName()
    {
        return usrName;
    }

    public void setUsrName(String usrName)
    {
        this.usrName = usrName;
    }

}