package com.grandmagic.readingmate.bean.request;

/**
 * Created by zhangmengqi on 2017/2/10.
 */

public class LoginRequestBean  {
    private String phone_num;
    private String password;

    public LoginRequestBean(String phone_num, String password) {
        this.phone_num = phone_num;
        this.password = password;
    }

    public String getPhone_num() {
        return phone_num;
    }

    public void setPhone_num(String phone_num) {
        this.phone_num = phone_num;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
