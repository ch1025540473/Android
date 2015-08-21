package com.wezebra.zebraking.http.data;

/**
 * Created by Duan Junjie on 2015/7/3.
 */
public class WxPayData
{
    private String outTradeNo;
    private String prepayid;
    private String err_code;
    private String err_code_des;

    public String getOutTradeNo()
    {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo)
    {
        this.outTradeNo = outTradeNo;
    }

    public String getPrepayid()
    {
        return prepayid;
    }

    public void setPrepayid(String prepayid)
    {
        this.prepayid = prepayid;
    }

    public String getErr_code()
    {
        return err_code;
    }

    public void setErr_code(String err_code)
    {
        this.err_code = err_code;
    }

    public String getErr_code_des()
    {
        return err_code_des;
    }

    public void setErr_code_des(String err_code_des)
    {
        this.err_code_des = err_code_des;
    }
}
