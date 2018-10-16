package com.jiangxufa.businesslibrary.model;

public class Account extends DoObject {

//    private User user;
    private String userId;
    private String token;
    private String companyId;
    private String hxAccountName;
    private String hxAccountPass;

    private int enableRemind = 1;
    private int EnableRemindVibrate = 1;


    public Account() {
    }

//    public void setUser(User user) {
//        this.user = user;
//    }
//
//    public User getUser() {
//        return user == null ? new User() : user;
//    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getEnableRemind() {
        return enableRemind;
    }

    public void setEnableRemind(int enableRemind) {
        this.enableRemind = enableRemind;
    }

    public int getEnableRemindVibrate() {
        return EnableRemindVibrate;
    }

    public void setEnableRemindVibrate(int enableRemindVibrate) {
        EnableRemindVibrate = enableRemindVibrate;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getHxAccountName() {
        return hxAccountName;
    }

    public void setHxAccountName(String hxAccountName) {
        this.hxAccountName = hxAccountName;
    }

    public String getHxAccountPass() {
        return hxAccountPass;
    }

    public void setHxAccountPass(String hxAccountPass) {
        this.hxAccountPass = hxAccountPass;
    }

    @Override
    public String toString() {
        return "Account{" +
//                "user=" + user +
                ", userId='" + userId + '\'' +
                ", token='" + token + '\'' +
                ", companyId='" + companyId + '\'' +
                ", hxAccountName='" + hxAccountName + '\'' +
                ", hxAccountPass='" + hxAccountPass + '\'' +
                ", enableRemind=" + enableRemind +
                ", EnableRemindVibrate=" + EnableRemindVibrate +
                '}';
    }
}
