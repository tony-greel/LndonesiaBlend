package com.example.lndonesiablend.bean;

public class LoginBean {

    /**
     * phone : undefined
     * phonepre : undefined
     * user_id : undefined
     * userName : undefined
     * custId : undefined
     * signKeyToken : undefined
     */

    private String phone;
    private String phonepre;
    private String user_id;
    private String userName;
    private String custId;
    private String signKeyToken;

    @Override
    public String toString() {
        return "LoginBean{" +
                "phone='" + phone + '\'' +
                ", phonepre='" + phonepre + '\'' +
                ", user_id='" + user_id + '\'' +
                ", userName='" + userName + '\'' +
                ", custId='" + custId + '\'' +
                ", signKeyToken='" + signKeyToken + '\'' +
                '}';
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhonepre() {
        return phonepre;
    }

    public void setPhonepre(String phonepre) {
        this.phonepre = phonepre;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public String getSignKeyToken() {
        return signKeyToken;
    }

    public void setSignKeyToken(String signKeyToken) {
        this.signKeyToken = signKeyToken;
    }
}
