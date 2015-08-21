package com.wezebra.zebraking.model;

public class AuthIdentity extends Auth {
    private Integer id;

    private Integer maximId;

    /**格言*/
    private String  maxim;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMaximId() {
        return maximId;
    }

    public void setMaximId(Integer maximId) {
        this.maximId = maximId;
    }

    public String getMaxim() {
        return maxim;
    }

    public void setMaxim(String maxim) {
        this.maxim = maxim;
    }

}