package com.wezebra.zebraking.model;

import java.math.BigDecimal;

public class OrderDetail
{
    private Integer id;
    //申请单号？？？？
    private Long orderCode;
    private double monthlyFee; // 月租
    private BigDecimal depositFee; // 押金
    //违约费？？？？？？
    private BigDecimal brokerFee;
    private Integer stagingDay; // 1=按月 3=季付 6=半年付
    private BigDecimal quaMonthlyFee;// 资质评估中的月租金
    private Integer quaStagingDay;// 资质评估中的分期周期
    //经纬度
    private BigDecimal lng;
    private BigDecimal lat;
    private String channel;
    //倍率
    private float rate;
    //服务费
    private double servFee;
    /**
     * 分期总月数（stagingDay-1）
     */
    private int stagingMonth;

    /**
     * 首期付款对象[1=斑马城，2=房东]
     */
    private Integer payFirstType;

    /**
     * 1=中介，2=个人房源，>2 为二房东
     */
    private Integer houseRes;
    private Integer houseFoundType; // 找房状态，1=已找到房，2=未找到房
    private Integer roomType; // 房屋户型 0不限 100一室 200两室 300三室 400四室及以上 99单间
    private String roomTypeDesc; // 户型描述
    private BigDecimal houseCal; // 租房预算 0不限 1￥1000以下 2=￥1000-1500 3=￥1500-2000
    // 4=￥2000-3000 5=￥3000以上
    private String region; // 租房区域
    private String referee;// 推荐人手机号
    private String isOnline;

    public String getReferee()
    {
        return referee;
    }

    public void setReferee(String referee)
    {
        this.referee = referee;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public Long getOrderCode()
    {
        return orderCode;
    }

    public void setOrderCode(Long orderCode)
    {
        this.orderCode = orderCode;
    }

    public double getMonthlyFee()
    {
        return monthlyFee;
    }

    public void setMonthlyFee(double monthlyFee)
    {
        this.monthlyFee = monthlyFee;
    }

    public BigDecimal getDepositFee()
    {
        return depositFee;
    }

    public void setDepositFee(BigDecimal depositFee)
    {
        this.depositFee = depositFee;
    }

    public BigDecimal getBrokerFee()
    {
        return brokerFee;
    }

    public void setBrokerFee(BigDecimal brokerFee)
    {
        this.brokerFee = brokerFee;
    }

    public Integer getStagingDay()
    {
        return stagingDay;
    }

    public void setStagingDay(Integer stagingDay)
    {
        this.stagingDay = stagingDay;
        if (null != stagingDay)
        {
            this.stagingMonth = stagingDay - 1;
        }
    }

    public int getStagingMonth()
    {
        return stagingMonth;
    }

    public Integer getPayFirstType()
    {
        return payFirstType;
    }

    public void setPayFirstType(Integer payFirstType)
    {
        this.payFirstType = payFirstType;
    }

    public Integer getHouseRes()
    {
        return houseRes;
    }

    public void setHouseRes(Integer houseRes)
    {
        this.houseRes = houseRes;
    }

    public Integer getHouseFoundType()
    {
        return houseFoundType;
    }

    public void setHouseFoundType(Integer houseFoundType)
    {
        this.houseFoundType = houseFoundType;
    }

    public Integer getRoomType()
    {
        return roomType;
    }

    public void setRoomType(Integer roomType)
    {
        this.roomType = roomType;
        switch (roomType)
        {
            case 99:
                this.roomTypeDesc = "单间";
                break;
            case 100:
                this.roomTypeDesc = "一室";
                break;
            case 200:
                this.roomTypeDesc = "两室";
                break;
            case 300:
                this.roomTypeDesc = "三室";
                break;
            case 400:
                this.roomTypeDesc = "四室及以上";
                break;
            default:
                this.roomTypeDesc = "不限";
                break;
        }
    }

    public String getRoomTypeDesc()
    {
        return roomTypeDesc;
    }

    public BigDecimal getHouseCal()
    {
        return houseCal;
    }

    public void setHouseCal(BigDecimal houseCal)
    {
        this.houseCal = houseCal;
    }

    public String getRegion()
    {
        return region;
    }

    public void setRegion(String region)
    {
        this.region = region;
    }

    public BigDecimal getQuaMonthlyFee()
    {
        return quaMonthlyFee;
    }

    public void setQuaMonthlyFee(BigDecimal quaMonthlyFee)
    {
        this.quaMonthlyFee = quaMonthlyFee;
    }

    public Integer getQuaStagingDay()
    {
        return quaStagingDay;
    }

    public void setQuaStagingDay(Integer quaStagingDay)
    {
        this.quaStagingDay = quaStagingDay;
    }

    public BigDecimal getLng()
    {
        return lng;
    }

    public void setLng(BigDecimal lng)
    {
        this.lng = lng;
    }

    public BigDecimal getLat()
    {
        return lat;
    }

    public void setLat(BigDecimal lat)
    {
        this.lat = lat;
    }

    public String getChannel()
    {
        return channel;
    }

    public void setChannel(String channel)
    {
        this.channel = channel;
    }

    public float getRate()
    {
        return rate;
    }

    public void setRate(float rate)
    {
        this.rate = rate;
    }

    public void setStagingMonth(int stagingMonth)
    {
        this.stagingMonth = stagingMonth;
    }

    public void setRoomTypeDesc(String roomTypeDesc)
    {
        this.roomTypeDesc = roomTypeDesc;
    }

    public double getServFee()
    {
        return servFee;
    }

    public void setServFee(double servFee)
    {
        this.servFee = servFee;
    }

    public String getIsOnline()
    {
        return isOnline;
    }

    public void setIsOnLine(String isOnline)
    {
        this.isOnline = isOnline;
    }
}
