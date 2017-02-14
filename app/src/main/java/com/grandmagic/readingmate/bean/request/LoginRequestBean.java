package com.grandmagic.readingmate.bean.request;

/**
 * Created by zhangmengqi on 2017/2/10.
 */

public class LoginRequestBean  {
    private String mobile_phone;
    private String password;

    public LoginRequestBean(String phone_num, String password) {
        this.mobile_phone = phone_num;
        this.password = password;
    }

    public String getMobile_phone() {
        return mobile_phone;
    }

    public void setMobile_phone(String mobile_phone) {
        this.mobile_phone = mobile_phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
