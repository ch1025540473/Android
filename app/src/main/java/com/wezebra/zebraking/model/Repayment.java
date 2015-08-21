package com.wezebra.zebraking.model;

/**
 * Created by 俊杰 on 2015/4/8.
 */
public class Repayment
{
    private int stage;
    private float money;
    private long time;
    private int status;

    public int getStage()
    {
        return stage;
    }

    public void setStage(int stage)
    {
        this.stage = stage;
    }

    public float getMoney()
    {
        return money;
    }

    public void setMoney(float money)
    {
        this.money = money;
    }

    public long getTime()
    {
        return time;
    }

    public void setTime(long time)
    {
        this.time = time;
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
