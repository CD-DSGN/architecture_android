package com.grandmagic.readingmate.bean.request;

/**
 * Created by zhangmengqi on 2017/2/10.
 */

public class RegisterRequestBean {
    private String password; //密码
    private String mobile_phone; //手机号
    private String verification_code; //验证码

    public RegisterRequestBean(String password, String phoneNum, String verifyCode) {
        this.password = password;
        this.mobile_phone = phoneNum;
        this.verification_code = verifyCode;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMobile_phone() {
        return mobile_phone;
    }

    public void setMobile_phone(String mobile_phone) {
        this.mobile_phone = mobile_phone;
    }

    public String getVerification_code() {
        return verification_code;
    }

    public void setVerification_code(String verification_code) {
        this.verification_code = verification_code;
    }
}
