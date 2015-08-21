package com.wezebra.zebraking.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by 俊杰 on 2015/5/20.
 */
public class OrderLog implements Parcelable
{
    private String memo;
    private long opTime;
    private int opType;
    private long orderCode;

    public OrderLog()
    {

    }

    public String getMemo()
    {
        return memo;
    }

    public void setMemo(String memo)
    {
        this.memo = memo;
    }

    public long getOpTime()
    {
        return opTime;
    }

    public void setOpTime(long opTime)
    {
        this.opTime = opTime;
    }

    public int getOpType()
    {
        return opType;
    }

    public void setOpType(int opType)
    {
        this.opType = opType;
    }

    public long getOrderCode()
    {
        return orderCode;
    }

    public void setOrderCode(long orderCode)
    {
        this.orderCode = orderCode;
    }

    public static final Creator<OrderLog> CREATOR = new Creator<OrderLog>()
    {
        @Override
        public OrderLog createFromParcel(Parcel source)
        {
            return new OrderLog(source);
        }

        @Override
        public OrderLog[] newArray(int size)
        {
            return new OrderLog[size];
        }
    };

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(memo);
        dest.writeLong(opTime);
        dest.writeInt(opType);
        dest.writeLong(orderCode);
    }

    private OrderLog(Parcel source)
    {
        memo = source.readString();
        opTime = source.readLong();
        opType = source.readInt();
        orderCode = source.readLong();
    }
}
