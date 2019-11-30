package com.example.lndonesiablend.bean;

public class User {


    /**
     * userId : 22019101111285282639769
     * selfMobile : 081246045912
     * token : B1ECE9CC3BD5FB07974CFD03B1034242
     * userName : nwd2EMX
     * vcode : null
     * phonepre : 62
     * phone : 81246045912
     * sign : 69c309bb06df1386b1bc8eed2b050163
     * productId :
     */

    private String userId;
    private String selfMobile;
    private String token;
    private String userName;
    private Object vcode;
    private String phonepre;
    private String phone;
    private String sign;
    private String productId;

    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", selfMobile='" + selfMobile + '\'' +
                ", token='" + token + '\'' +
                ", userName='" + userName + '\'' +
                ", vcode=" + vcode +
                ", phonepre='" + phonepre + '\'' +
                ", phone='" + phone + '\'' +
                ", sign='" + sign + '\'' +
                ", productId='" + productId + '\'' +
                '}';
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSelfMobile() {
        return selfMobile;
    }

    public void setSelfMobile(String selfMobile) {
        this.selfMobile = selfMobile;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Object getVcode() {
        return vcode;
    }

    public void setVcode(Object vcode) {
        this.vcode = vcode;
    }

    public String getPhonepre() {
        return phonepre;
    }

    public void setPhonepre(String phonepre) {
        this.phonepre = phonepre;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }
}
