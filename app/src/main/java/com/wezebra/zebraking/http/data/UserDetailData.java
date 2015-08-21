package com.wezebra.zebraking.http.data;

import com.wezebra.zebraking.model.BankCard;
import com.wezebra.zebraking.model.HfAccount;
import com.wezebra.zebraking.model.UseMoney;

/**
 * Created by 俊杰 on 2015/5/19.
 */
public class UserDetailData
{
    private BankCard bankCard;
    private HfAccount hfAccount;
    private UseMoney useMoney;

    public BankCard getBankCard()
    {
        return bankCard;
    }

    public void setBankCard(BankCard bankCard)
    {
        this.bankCard = bankCard;
    }

    public HfAccount getHfAccount()
    {
        return hfAccount;
    }

    public void setHfAccount(HfAccount hfAccount)
    {
        this.hfAccount = hfAccount;
    }

    public UseMoney getUseMoney()
    {
        return useMoney;
    }

    public void setUseMoney(UseMoney useMoney)
    {
        this.useMoney = useMoney;
    }
}
