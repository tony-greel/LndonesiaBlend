package com.example.lndonesiablend.event;

public enum BuryingPointEvent {

    APP_INSTALL("xabu31", "01-APP安装"),
    SUBMIT_PERSONAL_INFORMATION("bo2ax6","02-提交个人信息"),
    PERSONAL_INFORMATION_SUBMITTED_SUCCESSFULLY("u4alay","03-提交个人成功"),
    SUBMIT_CONTACT("pvcl1t","04-提交联系人"),
    CONTACT_SUBMITTED_SUCCESSFULLY("mq17da","05-提交联系人成功"),
    SUBMIT_WORK_INFORMATION("zcyo9k","06-提交工作信息"),
    WORK_INFORMATION_SUBMITTED_SUCCESSFULLY("r4kpkg","07-提交工作信息成功"),
    SUBMIT_BANK_CARD_INFORMATION("uyhblw","08-提交银行卡信息"),
    BANK_CARD_INFORMATION_SUBMITTED_SUCCESSFULLY("bsstsz","09-提交银行卡信息成功"),
    IN_SUBMISSION("20d846","10-提交订单"),
    SUBMIT_SUCCESSFULLY("r5eb31","11-提交订单成功"),
    ORDER_SUBMISSION_FAILED("kywfx6","13-提交订单失败"),

    ACCOUNT_LOGIN_SUCCEEDED("4reea4","14-账号登录成功"),
    ACCOUNT_LOGIN_FAILED("71x50c","15-账号登录失败"),
    VERIFY_LOGIN_SUCCESS("wkyviy","16-验证登录成功"),
    FAILED_TO_VERIFY_LOGIN("4odtil","17-验证登录失败"),
    VERIFICATION_CODE_SENT_SUCCESSFULLY("kuenyy","18-验证码发送成功"),
    FAILED_TO_SEND_VERIFICATION_CODE("7c0osy","19-验证码发送失败"),

    LIVE_AUTHENTICATION_SUCCESSFUL("q6boev","20-活体验证成功"),
    LIVE_AUTHENTICATION_FAILED("959cie","21-活体验证失败"),

    UPLOAD_DATA("fz2v94","22-上传adjust数据"),

    ADJUST_UPLOAD_AD_GAID_EVENT("vjm69c", "23-GAID");

    private String code;
    private String name;

    BuryingPointEvent(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
