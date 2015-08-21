package com.wezebra.zebraking.http;

/**
 * Created by superalex on 2015/1/8.
 */
public class TaskResponse<T>
{
    private int code;
    private String desc;
    private T data;

    public int getCode()
    {
        return code;
    }

    public void setCode(int code)
    {
        this.code = code;
    }

    public String getDesc()
    {
        return desc;
    }

    public void setDesc(String desc)
    {
        this.desc = desc;
    }

    public T getData()
    {
        return data;
    }

    public void setData(T data)
    {
        this.data = data;
    }

    @Override
    public String toString()
    {
        String mDesc = desc != null ? desc : "null";
        String mData = data != null ? data.toString() : "null";

        return "[code = " + code + ", desc = " + mDesc + ", data:" + mData + "]";
    }
}
