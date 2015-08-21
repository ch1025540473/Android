package com.wezebra.zebraking.http.data;

import java.io.Serializable;

/**
 * Created by 俊杰 on 2015/5/20.
 */
public class ActiveApplyData implements Serializable
{
    private long orderCode;
    private long userId;
    private int status;
    private String statemsg;
    private int step;

    private int serverAccessed;
    private String isOnLine;

    public long getOrderCode()
    {
        return orderCode;
    }

    public void setOrderCode(long orderCode)
    {
        this.orderCode = orderCode;
    }

    public long getUserId()
    {
        return userId;
    }

    public void setUserId(long userId)
    {
        this.userId = userId;
    }

    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }

    public String getStatemsg()
    {
        return statemsg;
    }

    public void setStatemsg(String statemsg)
    {
        this.statemsg = statemsg;
    }

    public int getStep()
    {
        return step;
    }

    public void setStep(int step)
    {
        this.step = step;
    }

    public int getServerAccessed() {
        return serverAccessed;
    }

    public void setServerAccessed(int serverAccessed) {
        this.serverAccessed = serverAccessed;
    }

    public String getIsOnLine() {
        return isOnLine;
    }

    public void setIsOnLine(String isOnLine) {
        this.isOnLine = isOnLine;
    }

}
