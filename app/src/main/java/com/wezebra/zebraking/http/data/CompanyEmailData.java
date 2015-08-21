package com.wezebra.zebraking.http.data;

/**
 * Created by admin on 2015/7/25.
 */
public class CompanyEmailData {
    private int id;
    private String email;
    private int status;
    private String memo;
    private String name;
    private String identity;
    private long userId;

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public int getStatus() {
        return status;
    }

    public String getMemo() {
        return memo;
    }

    public String getName() {
        return name;
    }

    public String getIdentity() {
        return identity;
    }

    public long getUserId() {
        return userId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}
