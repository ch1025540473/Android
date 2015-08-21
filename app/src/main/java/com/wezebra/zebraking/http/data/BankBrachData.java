package com.wezebra.zebraking.http.data;

/**
 * Created by admin on 2015/7/28.
 */
public class BankBrachData {
    private String bank;
    private int prcptcd;

    public void setBank(String bank) {
        this.bank = bank;
    }

    public void setPrcptcd(int prcptcd) {
        this.prcptcd = prcptcd;
    }

    public String getBank() {
        return bank;
    }

    public int getPrcptcd() {
        return prcptcd;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
