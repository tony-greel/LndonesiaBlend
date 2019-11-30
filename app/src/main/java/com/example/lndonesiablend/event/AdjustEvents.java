package com.example.lndonesiablend.event;

public enum AdjustEvents {

    APP_INSTALL("grncoo", "00-01-APP安装"),
    FIND_PSD_TO_GET_PSD("4o13p0", "01-01-找回密码界面-获取验证码"),
    MAIN_TO_GET_PSD("87n0az", "02-01-00-首页-获取验证码"),
    MAIN_INPUT_SMS_CODE("8mkfvd", "02-01-01-首页输入了验证码"),
    MAIN_INPUT_ID_CARD("ertis5", "02-01-02-首页输入了身份证"),
    MAIN_INPUT_PASSWORD("q7f4u0", "02-01-03-首页输入了密码"),
    MAIN_TO_REGISTER("1skij8", "02-02-首页-注册"),
    LOGIN_TO_DO_LOGIN("5ny19d", "03-01-登陆界面-登录完成"),
    PERSONAL_TO_WRITE_OVER("fpikxg", "04-01-个人信息-填写完成"),
    EMERGENCY_TO_WRITE_OVER("414hh8", "05-01-紧急联系人-填写完成"),
    WORK_INFO_TO_WRITE_OVER("rn6qsp", "06-01-工作信息-填写完成"),
    BANK_CARD_TO_WRITE_OVER("ebcwqt", "07-01-银行卡信息-填写完成"),
    JOB_SHOOT_OVER("4sl5mz", "08-01-工作证明拍摄完成"),
    ID_CARD_SHOOT_OVER("sj0lry", "09-01-身份证拍摄完成"),
    SUCCESSFUL_LIVING_CERTIFICATION("jruw08", "10-01-活体认证成功"),
    ORDERS_SUBMITTED_SUCCESSFULLY("vhmuz0", "11-01-订单提交成功"),
    ORDERS_SUBMITTED_FAILED("723b3v", "11-02-订单提交失败"),

    GAID("706zni", "12-01-GAID"),
    ADJUST_UPLOAD_RESULT_DATA_EVENT("i3ci6s", "13-01-登陆完成上传Adjust数据"),

    AGREE_READ_CONTACTS_PERMISSIONS("p5t6vj", "同意联系人权限");

    private String code;
    private String name;

    AdjustEvents(String code, String name) {
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
