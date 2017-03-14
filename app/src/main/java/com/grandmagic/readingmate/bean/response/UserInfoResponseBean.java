package com.grandmagic.readingmate.bean.response;

/**
 * Created by zhangmengqi on 2017/3/13.
 */

public class UserInfoResponseBean{
    private String user_name;
    private int  gender;
    private String signature;

    public ImageUrlResponseBean getAvatar_url() {
        return avatar_url;
    }

    public void setAvatar_url(ImageUrlResponseBean avatar_url) {
        this.avatar_url = avatar_url;
    }

    private ImageUrlResponseBean avatar_url;




    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }


}
