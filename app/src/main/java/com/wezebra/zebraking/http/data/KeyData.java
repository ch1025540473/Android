package com.wezebra.zebraking.http.data;

/**
 * Created by 俊杰 on 2015/5/13.
 */
public class KeyData
{
    private String key;

    public String getKey()
    {
        return key;
    }

    public void setKey(String key)
    {
        this.key = key;
    }

    @Override
    public String toString()
    {
        return "[key = " + key + "]";
    }
}
