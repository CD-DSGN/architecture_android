package com.grandmagic.readingmate.bean.response;

/**
 * Created by zhangmengqi on 2017/3/22.
 */

public class UserSimpleInfoResponseBean {
    private String user_id;
    private ImageUrlResponseBean avatar_url;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public ImageUrlResponseBean getAvatar_url() {
        return avatar_url;
    }

    public void setAvatar_url(ImageUrlResponseBean avatar_url) {
        this.avatar_url = avatar_url;
    }
}
