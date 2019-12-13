package com.example.lndonesiablend.activity.Interface;

public interface JavaCallback {
    void jumpPictureUpload(); // 提交工作证页面
    void jumpIdUpload(); // 提交身份证页面
    void jumpLiveAuthentication(); // 进入活体页面

    void openLoad(); // 开启加载弹窗
    void closeLoad(); // 关闭加载弹窗

    void submitPersonalInformation(); // 提交个人信息
    void personalInformationSubmittedSuccessfully(); // 提交个人成功
    void submitContact(); // 提交联系人
    void contactSubmittedSuccessfully(); // 提交联系人成功
    void submitWorkInformation(); // 提交工作信息
    void workInformationSubmittedSuccessfully(); // 提交工作信息成功
    void submitBankCardInformation(); // 提交银行卡信息
    void bankCardInformationSubmittedSuccessfully(); // 提交银行卡信息成功

    void accountLoginSucceeded(); // 账号登录成功
    void accountLoginFailed(); // 账号登录失败
    void verifyLoginSuccess(); // 验证登录成功
    void failedToVerifyLogin(); // 验证登录失败
    void verificationCodeSentSuccessfully(); // 验证码发送成功
    void failedToSendVerificationCode(); // 验证码发送失败

//    void uploadData(); // 上传adjust数据
}
