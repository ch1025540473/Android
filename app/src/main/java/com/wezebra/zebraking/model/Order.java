package com.wezebra.zebraking.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * @author Tangsason
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Order implements java.io.Serializable
{
    private Long orderCode;
    private Long userId;
    // 申请总金额格式化
    private String formatAmount;
    private Integer type;
    private Integer orderStatus;
    private String formatMemo;
    private Integer step;
    private long createTime;
    private String referee;// 推荐人手机号
    private String isOnline;
    private String formatStatus;
    private OrderStatus status;

    public enum OrderStatus
    {
        // 状态 1=审核中，2=审核通过(还款中)，3=审核不通过(关闭)，4=已完成
        /*
		 * WAITING(-1,"等待上传材料"), AUDITING(1, "审核中"), REPAYMENT(2, "审核通过(还款中)"),
		 * UNPASS(3, "审核不通过(关闭)"), REPAYMENT_COMPLETED(4, "已完成"), PAY(5,"放款完成"),
		 * PAY_FAILED(6,"放款失败"),
		 */

        // 流程优化后状态 10:初审中, 11:待完善、 31:审核中， 32:待补充， 34: 处理中， 35 待修改， 37: 待付款， 41:
        // 放款中， 42: 还款中 43: 已完成， -99: 关闭 -98: 不通过'
        AUDITING_FIRST(10, "初审中"), // 初审中
        WAITING_IMPROVE(11, "待完善"), // 系统自动初审通过
        AUDITING_BASIC(31, "审核中"), // 填写租房基本信息
        WAITING_ADDED(32, "待补充"), // 待上传合同，提交房东信息
        AUDITING_ADDED(34, "处理中"), // 待审核补充信息（合同及房东信息）
        WAITING_MODIFY(35, "待修改"), // 需修改补充信息
        WAITING_PAY_FIRST(37, "待付款"), // 等待支付首期款
        WAITING_GRANT(38, "待授权"), // 等待用户授权给房东
        AUDITING_LOAN(41, "放款中"), // 已授权
        WAITING_REPAY(42, "还款中"), // 放款完成
        SUCCESS(43, "已完成"), // 所有分期都已还款
        CLOSED(-99, "关闭"), // 后台关闭|自动关闭
        UNPASS(-98, "不通过");
        private int state;
        private String description;

        private OrderStatus(int state, String description)
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

    public OrderStatus getStatus()
    {
        return status;
    }

    public void setStatus(OrderStatus status)
    {
        this.status = status;
        this.orderStatus = status.getState();
    }

    public Long getOrderCode()
    {
        return orderCode;
    }

    public void setOrderCode(Long orderCode)
    {
        this.orderCode = orderCode;
    }

    public Long getUserId()
    {
        return userId;
    }

    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public String getFormatAmount()
    {
        return formatAmount;
    }

    public void setFormatAmount(String formatAmount)
    {
        this.formatAmount = formatAmount;
    }

    public Integer getType()
    {
        return type;
    }

    public void setType(Integer type)
    {
        this.type = type;
    }

    public Integer getOrderStatus()
    {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus)
    {
        this.orderStatus = orderStatus;
        switch (orderStatus)
        {
            case -98:
                setStatus(OrderStatus.UNPASS);
                break;
            case -99:
                setStatus(OrderStatus.CLOSED);
                break;
            case 10:
                setStatus(OrderStatus.AUDITING_FIRST);
                break;
            case 11:
                setStatus(OrderStatus.WAITING_IMPROVE);
                break;
            case 31:
                setStatus(OrderStatus.AUDITING_BASIC);
                break;
            case 32:
                setStatus(OrderStatus.WAITING_ADDED);
                break;
            case 34:
                setStatus(OrderStatus.AUDITING_ADDED);
                break;
            case 35:
                setStatus(OrderStatus.WAITING_MODIFY);
                break;
            case 37:
                setStatus(OrderStatus.WAITING_PAY_FIRST);
                break;
            case 38:
                setStatus(OrderStatus.WAITING_GRANT);
                break;
            case 41:
                setStatus(OrderStatus.AUDITING_LOAN);
                break;
            case 42:
                setStatus(OrderStatus.WAITING_REPAY);
                break;
            case 43:
                setStatus(OrderStatus.SUCCESS);
                break;
        }
    }

    public String getFormatMemo()
    {
        return formatMemo;
    }

    public void setFormatMemo(String formatMemo)
    {
        this.formatMemo = formatMemo;
    }

    public Integer getStep()
    {
        return step;
    }

    public void setStep(Integer step)
    {
        this.step = step;
    }

    public long getCreateTime()
    {
        return createTime;
    }

    public void setCreateTime(long createTime)
    {
        this.createTime = createTime;
    }

    public String getReferee()
    {
        return referee;
    }

    public void setReferee(String referee)
    {
        this.referee = referee;
    }

    public String getIsOnline()
    {
        return isOnline;
    }

    public void setIsOnline(String isOnline)
    {
        this.isOnline = isOnline;
    }

    public String getFormatStatus()
    {
        return formatStatus;
    }
}
