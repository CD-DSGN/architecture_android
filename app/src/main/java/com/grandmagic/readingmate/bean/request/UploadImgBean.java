package com.grandmagic.readingmate.bean.request;

import com.grandmagic.readingmate.base.AppBaseRequestBean;

/**
 * Created by zhangmengqi on 2017/3/16.
 */

public class UploadImgBean extends AppBaseRequestBean {
    private String avatar;

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
