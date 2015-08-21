package com.wezebra.zebraking.model;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;


public class Bill implements Serializable
{
    private Long billCode;
    //账单期数
    private Integer billNum;// 账单期数
    //userid
    private Long userId;
    //申请订单编码
    private Long orderCode;
    //付款金额
    private double repayAmount;
    //付款日期
    private Long repayDate;
    //格式化的付款日期
    private String formatRepayDate;
    //类型
    private Integer type;
    //还款状态
    private Integer status;
    //订单状态
    private BillRepayment billStatus;

    private Integer smsSend;
    //付款时间
    private Long repayTime;
    //格式化的付款时间
    private String formatRepayTime;
    // 逾期 天数
    private int overdueDay = 0;
    // 逾期费用（repayAmount*0.006*overdueDay）
    private double overdueFee;
    private String formatOverdueFee;
    // 总费用(逾期)
    private double overCount;

    public String getFormatRepayDate()
    {
        return formatRepayDate;
    }

    public void setFormatRepayDate(String formatRepayDate)
    {
        this.formatRepayDate = formatRepayDate;
    }

    public double getOverCount()
    {
        return overCount;
    }

    public void setOverCount(double overCount)
    {
        this.overCount = overCount;
    }

    public Long getBillCode()
    {
        return billCode;
    }

    public void setBillCode(Long billCode)
    {
        this.billCode = billCode;
    }

    public Long getUserId()
    {
        return userId;
    }

    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public Long getOrderCode()
    {
        return orderCode;
    }

    public void setOrderCode(Long orderCode)
    {
        this.orderCode = orderCode;
    }

    public double getRepayAmount()
    {
        return repayAmount;
    }

    public void setRepayAmount(double repayAmount)
    {
        this.repayAmount = repayAmount;
    }

    public Long getRepayDate()
    {
        return repayDate;
    }

    public void setRepayDate(Long repayDate)
    {
        this.repayDate = repayDate;
        this.formatRepayDate = new SimpleDateFormat("yyyy-MM-dd").format(repayDate);
    }

    public Integer getType()
    {
        return type;
    }

    public void setType(Integer type)
    {
        this.type = type;
    }

    public Integer getSmsSend()
    {
        return smsSend;
    }

    public void setSmsSend(Integer smsSend)
    {
        this.smsSend = smsSend;
    }

    public Long getRepayTime()
    {
        return repayTime;
    }

    public void setRepayTime(Long repayTime)
    {
        this.repayTime = repayTime;
        this.formatRepayTime = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(repayTime);
    }

    public String getFormatRepayTime()
    {
        return formatRepayTime;
    }

    public void setOverdueDay(int overdueDay)
    {
        this.overdueDay = overdueDay;
    }

    public void setOverdueFee(double overdueFee)
    {
        DecimalFormat df = new DecimalFormat("0.00");
        this.overdueFee = Double.parseDouble(df.format(overdueFee));
//        if (overdueFee != 0)
//        {
//            double num = (overdueFee + repayAmount);
//            this.overCount = df.format(num);
//        }
    }

    public int getOverdueDay()
    {
        return overdueDay;
    }

    public double getOverdueFee()
    {
        return overdueFee;
    }

    public void setFormatOverdueFee(String formatOverdueFee)
    {
        this.formatOverdueFee = formatOverdueFee;
    }

    public String getFormatOverdueFee()
    {
        return formatOverdueFee;
    }

    // 还款状态 1=未还款 2=已还款
    public enum BillRepayment
    {
        NO_REPAYMENT(1, "去还款"), REPAYMENT(2, "已还款"), PROCESSIONG(3, "处理中");
        private int state;
        private String description;

        private BillRepayment(int state, String description)
        {
            this.state = state;
            this.description = description;
        }

        public int getState()
        {
            return state;
        }

        public String getDescription()
        {
            return description;
        }
    }

    public Integer getStatus()
    {
        return status;
    }

    public void setStatus(Integer status)
    {
        this.status = status;
        switch (status)
        {
            case 1:
                setBillStatus(BillRepayment.NO_REPAYMENT);
                break;
            case 2:
                setBillStatus(BillRepayment.REPAYMENT);
                break;
            case 3:
                setBillStatus(BillRepayment.PROCESSIONG);
                break;
        }
    }

    public BillRepayment getBillStatus()
    {
        return billStatus;
    }

    public void setBillStatus(BillRepayment billStatus)
    {
        this.billStatus = billStatus;
        this.status = billStatus.getState();
    }

    public Integer getBillNum()
    {
        return billNum;
    }

    public void setBillNum(Integer billNum)
    {
        this.billNum = billNum;
    }

    // 还款状态 1=未还款 2=已还款
    public enum HuiFuStatus
    {
        INIT(-1, "初始"), ERROR(0, "失败"), SUCCESS(1, "成功");
        private int state;
        private String description;

        private HuiFuStatus(int state, String description)
        {
            this.state = state;
            this.description = description;
        }

        public int getState()
        {
            return state;
        }

        public String getDescription()
        {
            return description;
        }
    }
}