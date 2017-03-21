package com.grandmagic.readingmate.bean.response;

import com.google.gson.annotations.JsonAdapter;
import com.google.gson.internal.bind.ObjectDeserializer;

/**
 * Created by zhangmengqi on 2017/3/17.
 */

public class UploadAvarResponseBean {
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

    private String user_id;
    @JsonAdapter(ObjectDeserializer.class)
    private ImageUrlResponseBean avatar_url;
}
