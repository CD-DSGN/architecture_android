package com.grandmagic.readingmate.bean.response;

/**
 * Created by zhangmengqi on 2017/2/10.
 */

public class LoginResponseBean {
    private String access_token;
    /**
     * username : 7
     * password : e10adc3949ba59abbe56e057f20f883e
     */

    private String username;
    private String password;

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String mUsername) {
        username = mUsername;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
