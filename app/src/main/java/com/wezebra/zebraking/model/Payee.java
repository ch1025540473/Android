package com.wezebra.zebraking.model;


import com.wezebra.zebraking.util.Base64Digest;

import java.io.Serializable;
import java.math.BigDecimal;


/**
 * 收款人信息
 *
 * @author Mars
 */
public class Payee implements Serializable
{

    /**
     * 意义，目的和功能，以及被用到的地方<br>
     */
    private static final long serialVersionUID = 6589426342123141468L;
    // 订单流水号
    private long orderCode;
    // 房东用户id
    private long userId;
    // 支付方式
    private int payTypeInt;
    private PayType payType;
    // 收款方姓名
    private String name;
    // 收款方姓名(加密)
    private String encryptName;
    // 收款方账号
    private String accountNo;
    // 收款方账号格式化（123*******123）
    private String formatAccountNo;
    // 收款方账号(加密)
    private String encryptAccountNo;
    // 收款方电话
    private String accountPhone;
    // 收款方电话（加密）
    private String encryptPhone;
    // 收款人类型
    private int type;
    private PayeeType payeeType;
    // 收款金额
    private BigDecimal amount;
    // 收款金额int类型
    private String formatAmount;
    /**
     * 身份证号码
     */
    private String payIdentity;

    private String encryptPayIdentity;

    private String bank;
    private String encryptBank;

    private Integer cityId;
    private String cityName;
    /**
     * 房东代提现状态 默认 0 已提现1 ,房东代提现状态 默认 0, 已提现1 , 2 处理中 , 3失败
     */
    private String status;

    private Integer stateAudit;// 审核状态-1未提交审核 1审核中 2通过 3不通过

    private String memo;// 备注


    public enum PayeeType
    {
        // 收款人类型 1=房东 2=经纪人
        LANDLORD(1, "房东"),
        AGENT(2, "经纪人"),
        SECOND_LANDLORD(3, "二房东");
        private int type;
        private String description;

        private PayeeType(int type, String description)
        {
            this.type = type;
            this.description = description;
        }

        public int getType()
        {
            return type;
        }

        public String getDescription()
        {
            return description;
        }
    }

    public enum PayType
    {
        // 支付方式 1=支付宝 2=银行卡
        ALIPAY(1, "支付宝"),
        BANKPAY(2, "银行卡（个人）"),
        BANK_ORGAN_PAY(22, "银行卡（企业）");
        private int type;
        private String description;

        private PayType(int type, String description)
        {
            this.type = type;
            this.description = description;
        }

        public int getType()
        {
            return type;
        }

        public String getDescription()
        {
            return description;
        }
    }

    public String getEncryptBank()
    {
        return encryptBank;
    }

    public void setEncryptBank(String encryptBank)
    {
        this.encryptBank = encryptBank;
        this.setBank(Base64Digest.decode(encryptBank));
    }

    public String getBank()
    {
        return bank;
    }

    public void setBank(String bank)
    {
        this.bank = bank;
    }

    public long getOrderCode()
    {
        return orderCode;
    }

    public void setOrderCode(long orderCode)
    {
        this.orderCode = orderCode;
    }

    public PayType getPayType()
    {
        return payType;
    }

    public void setPayType(PayType payType)
    {
        this.payType = payType;
        this.payTypeInt = payType.getType();
    }

    public int getPayTypeInt()
    {
        return payTypeInt;
    }

    public void setPayTypeInt(int type)
    {
        this.payTypeInt = type;
        switch (type)
        {
            case 1:
                this.setPayType(PayType.ALIPAY);
                break;
            case 2:
                this.setPayType(PayType.BANKPAY);
                break;
            case 22:
                this.setPayType(PayType.BANK_ORGAN_PAY);
                break;
        }
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getAccountNo()
    {
        return accountNo;
    }

    public void setAccountNo(String accountNo)
    {
        this.accountNo = accountNo;
    }

    public String getAccountPhone()
    {
        return accountPhone;
    }

    public void setAccountPhone(String accountPhone)
    {
        this.accountPhone = accountPhone;
    }

    public PayeeType getPayeeType()
    {
        return payeeType;
    }

    public void setPayeeType(PayeeType payeeType)
    {
        this.payeeType = payeeType;
        this.type = payeeType.getType();
    }

    public int getType()
    {
        return type;
    }

    public void setType(int payeeType)
    {
        this.type = payeeType;
        switch (payeeType)
        {
            case 1:
                this.setPayeeType(PayeeType.LANDLORD);
                return;
            case 2:
                this.setPayeeType(PayeeType.AGENT);
                return;
            case 3:
                this.setPayeeType(PayeeType.SECOND_LANDLORD);
                return;
        }
    }

    public BigDecimal getAmount()
    {
        return amount;
    }

    public void setAmount(BigDecimal amount)
    {
        this.amount = amount;
//		this.formatAmount = null == amount ? "0.00" : DataFormatUtils.data2NumDecimal(amount.doubleValue(), "0.00");
    }

    public String getFormatAmount()
    {
        return formatAmount;
    }

    public String getEncryptName()
    {
        return encryptName;
    }

    public void setEncryptName(String encryptName)
    {
        this.encryptName = encryptName;
        this.setName(Base64Digest.decode(encryptName));
    }

    public String getEncryptAccountNo()
    {
        return encryptAccountNo;
    }

    public void setEncryptAccountNo(String encryptAccountNo)
    {
        this.encryptAccountNo = encryptAccountNo;
//		if (!StringUtils.isBlank(encryptAccountNo)) {
//			String accountNo = Base64Digest.decode(encryptAccountNo);
//			this.setAccountNo(accountNo);
//			this.formatAccountNo = DataFormatUtils.formatStr(accountNo, -1, 6);
//		}
    }

    public String getFormatAccountNo()
    {
        return formatAccountNo;
    }

    public String getEncryptPhone()
    {
        return encryptPhone;
    }

    public void setEncryptPhone(String encryptPhone)
    {
        this.encryptPhone = encryptPhone;
        this.setAccountPhone(Base64Digest.decode(encryptPhone));
    }

    public String getPayIdentity()
    {
        return payIdentity;
    }

    public void setPayIdentity(String payIdentity)
    {
        this.payIdentity = payIdentity;
    }

    public String getEncryptPayIdentity()
    {
        return encryptPayIdentity;
    }

    public void setEncryptPayIdentity(String encryptPayIdentity)
    {
        this.encryptPayIdentity = encryptPayIdentity;
        this.setPayIdentity(Base64Digest.decode(encryptPayIdentity));
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

    public long getUserId()
    {
        return userId;
    }

    public void setUserId(long userId)
    {
        this.userId = userId;
    }

    @Override
    public String toString()
    {
        return "Payee [orderCode=" + orderCode + ",userId=" + userId + " payTypeInt=" + payTypeInt + ", payType=" + payType + ", name="
                + name + ", encryptName=" + encryptName + ", accountNo=" + accountNo + ", encryptAccountNo=" + encryptAccountNo
                + ", accountPhone=" + accountPhone + ", encryptPhone=" + encryptPhone + ", type=" + type + ", payeeType=" + payeeType
                + ", amount=" + amount + ", formatAmount=" + formatAmount + ", payIdentity=" + payIdentity + ", encryptPayIdentity="
                + encryptPayIdentity + ", bank=" + bank + ", encryptBank=" + encryptBank + ", cityId=" + cityId + ", cityName=" + cityName
                + "stateAudit=" + stateAudit + ",memo=" + memo + "]";
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public Integer getStateAudit()
    {
        return stateAudit;
    }

    public void setStateAudit(Integer stateAudit)
    {
        this.stateAudit = stateAudit;
    }

    public String getMemo()
    {
        return memo;
    }

    public void setMemo(String memo)
    {
        this.memo = memo;
    }

}
