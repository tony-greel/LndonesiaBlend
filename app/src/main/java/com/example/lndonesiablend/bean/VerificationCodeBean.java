package com.example.lndonesiablend.bean;

public class VerificationCodeBean {
    private String phone;
    private String smsCode;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSmsCode() {
        return smsCode;
    }

    public void setSmsCode(String smsCode) {
        this.smsCode = smsCode;
    }

    @Override
    public String toString() {
        return "GetCodeReturnBean{" +
                "phone='" + phone + '\'' +
                ", smsCode='" + smsCode + '\'' +
                '}';
    }
}
