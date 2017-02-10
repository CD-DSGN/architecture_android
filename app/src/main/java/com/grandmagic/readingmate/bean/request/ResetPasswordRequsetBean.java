package com.grandmagic.readingmate.bean.request;

/**
 * Created by zhangmengqi on 2017/2/10.
 */

public class ResetPasswordRequsetBean {
    private String password; //密码
    private String phone_num; //手机号
    private String verify_code; //验证码

    public ResetPasswordRequsetBean(String password, String phoneNum, String verifyCode) {
        this.password = password;
        this.phone_num = phoneNum;
        this.verify_code = verifyCode;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone_num() {
        return phone_num;
    }

    public void setPhone_num(String phone_num) {
        this.phone_num = phone_num;
    }

    public String getVerify_code() {
        return verify_code;
    }

    public void setVerify_code(String verify_code) {
        this.verify_code = verify_code;
    }

}
