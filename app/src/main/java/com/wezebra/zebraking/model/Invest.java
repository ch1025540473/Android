package com.wezebra.zebraking.model;

/**
 * Created by 俊杰 on 2015/4/9.
 */
public class Invest
{
    public static final int STATE_AVAILABLE = 1;
    public static final int STATE_UNAVAILABLE = 2;
    public static final int STATE_REPAYING = 3;
    public static final int STATE_DONE = 4;
    private String name;
    private int rate;
    private int time;
    private int amount;
    private int status;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public int getRate()
    {
        return rate;
    }

    public void setRate(int rate)
    {
        this.rate = rate;
    }

    public int getTime()
    {
        return time;
    }

    public void setTime(int time)
    {
        this.time = time;
    }

    public int getAmount()
    {
        return amount;
    }

    public void setAmount(int amount)
    {
        this.amount = amount;
    }

    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }
}
