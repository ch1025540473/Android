package com.wezebra.zebraking.model;

import com.wezebra.zebraking.util.DateUtils;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * MoneyFlow entity. @author MyEclipse Persistence Tools
 */

public class MoneyFlow implements java.io.Serializable
{
    public static final int STATE_INCOME = 1;
    public static final int STATE_OUTCOME = 2;
    public static final int STATE_UNFREEZE = 4;
    public static final int STATE_FREEZE = 3;
    /**
     * 取现免费次数/天
     */
    public static final int CASH_FREE_COUNT_EVERYDAY = 3;

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private Long id;
    private Long userId;
    private Long useMoneyId;

    /**
     * 资金变动金额（收入存正数，支出存负数，冻结解冻设0）
     */
    private Long amt = 0L;

    private String mark;
    private Long parentId;
    private String ordId;
    //	private String dr = CommonResource.INIT_DELETE_STATUS;
    private Long createDate;
    private Date updateDate;
    private Long orderTypeId;
    private String detailsTableName;

    private String createDateStr;

    private String updateDateStr;
    private String amtStr;

    private String formatCreateDate;

    public final static String INCOME_ICON = "income-icon";
    public final static String BRANCH_ICON = "branch-icon";
    public final static String SOLUTION_ICON = "solution-icon";
    public final static String FROZEN_ICON = "frozen-icon";
    /**
     * 收：income-icon 支：branch-icon 解：solution-icon 冻：frozen-icon
     */
    public String transactionTypeIcon;

    /**
     * 交易涉及金额
     */
    private Long involveAmt = 0L;

    private String involveAmtStr;

    private int transType;// 交易类型1收 2支 3冻结 4解冻

    private int viewType;

    public int getViewType()
    {
        return viewType;
    }

    public void setViewType(int viewType)
    {
        this.viewType = viewType;
    }
    // Constructors

    public String getTransactionTypeIcon()
    {
        return transactionTypeIcon;
    }

    public String getInvolveAmtStr()
    {
        return involveAmtStr;
    }

    public void setInvolveAmtStr(String involveAmtStr)
    {
        this.involveAmtStr = involveAmtStr;
    }

    public void setTransactionTypeIcon(String transactionTypeIcon)
    {
        this.transactionTypeIcon = transactionTypeIcon;
    }

    public String getFormatCreateDate()
    {
        return formatCreateDate;
    }

    public void setFormatCreateDate(String formatCreateDate)
    {
        this.formatCreateDate = formatCreateDate;
    }

    /**
     * default constructor
     */
    public MoneyFlow()
    {
    }

//    /**
//     * full constructor
//     */
//    public MoneyFlow(Long useMoneyId, Long amt, String mark, Long parentId, String ordId, String dr, Date createDate, Date updateDate,
//                     Long orderTypeId, String detailsTableName)
//    {
//        this.useMoneyId = useMoneyId;
//        this.amt = amt;
//        this.mark = mark;
//        this.parentId = parentId;
//        this.ordId = ordId;
////		this.dr = dr;
//        this.createDate = createDate;
//        this.updateDate = updateDate;
//        this.orderTypeId = orderTypeId;
//        this.detailsTableName = detailsTableName;
//    }

    // Property accessors

    public Long getId()
    {
        return this.id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getUserId()
    {
        return userId;
    }

    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public Long getUseMoneyId()
    {
        return this.useMoneyId;
    }

    public void setUseMoneyId(Long useMoneyId)
    {
        this.useMoneyId = useMoneyId;
    }

    public Long getAmt()
    {
        return this.amt;
    }

    public void setAmt(Long amt)
    {
        this.amt = amt;
        this.amtStr = String.valueOf(new DecimalFormat("0.00").format((double) amt / (100 * 1.00)));
    }

    public String getMark()
    {
        return this.mark;
    }

    public void setMark(String mark)
    {
        this.mark = mark;
    }

    public Long getParentId()
    {
        return this.parentId;
    }

    public void setParentId(Long parentId)
    {
        this.parentId = parentId;
    }

    public String getOrdId()
    {
        return this.ordId;
    }

    public void setOrdId(String ordId)
    {
        this.ordId = ordId;
    }

//	public String getDr() {
//		return this.dr;
//	}
//
//	public void setDr(String dr) {
//		this.dr = dr;
//	}

    public Long getCreateDate()
    {
        return this.createDate;
    }

    public void setCreateDate(Long createDate)
    {
        this.createDate = createDate;
        this.createDateStr = DateUtils.toTime(createDate, "yyyy-MM-dd HH:mm:ss");
        this.formatCreateDate = DateUtils.toTime(createDate, "yyyy年MM月");
    }

    public Date getUpdateDate()
    {
        return this.updateDate;
    }

    public void setUpdateDate(Date updateDate)
    {
        this.updateDate = updateDate;
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(updateDate.getTime());
        this.updateDateStr = DateUtils.toTime(cal.getTime(), "yyyy-MM-dd HH:mm:ss");
    }

    public Long getOrderTypeId()
    {
        return this.orderTypeId;
    }

    public void setOrderTypeId(Long orderTypeId)
    {
        this.orderTypeId = orderTypeId;
    }

    public String getDetailsTableName()
    {
        return this.detailsTableName;
    }

    public void setDetailsTableName(String detailsTableName)
    {
        this.detailsTableName = detailsTableName;
    }

    public String getCreateDateStr()
    {
        return createDateStr;
    }

    public void setCreateDateStr(String createDateStr)
    {
        this.createDateStr = createDateStr;
    }

    public String getUpdateDateStr()
    {
        return updateDateStr;
    }

    public void setUpdateDateStr(String updateDateStr)
    {
        this.updateDateStr = updateDateStr;
    }

    public String getAmtStr()
    {
        return amtStr;
    }

    public void setAmtStr(String amtStr)
    {
        this.amtStr = amtStr;
    }

    public Long getInvolveAmt()
    {
        return involveAmt;
    }

    public void setInvolveAmt(Long involveAmt)
    {
        this.involveAmt = involveAmt;
        this.involveAmtStr = String.valueOf(new DecimalFormat("0.00").format(Math.abs((double) involveAmt / (100 * 1.00))));
    }

    public int getTransType()
    {
        return transType;
    }

    public void setTransType(int transType)
    {
        this.transType = transType;
    }

}
