package com.wezebra.zebraking.http.data;

/**
 * Created by Duan Junjie on 2015/7/3.
 */
public class PayStatusData
{
    private String success;
    private String err_code;
    private String err_code_des;

    public String getSuccess()
    {
        return success;
    }

    public void setSuccess(String success)
    {
        this.success = success;
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
