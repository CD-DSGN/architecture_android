package com.grandmagic.readingmate.bean.response;

import android.text.TextUtils;

import com.google.gson.annotations.JsonAdapter;
import com.google.gson.internal.bind.ListTypeAdapterFactory;
import com.google.gson.internal.bind.ObjectDeserializer;

import java.util.List;

/**
 * Created by zhangmengqi on 2017/3/20.
 */

public class PersonalCommentListResponseBean {
    @JsonAdapter(ListTypeAdapterFactory.class)
    private List<PersonnalCommentResponseBean> comment_info;
    private String username;
    @JsonAdapter(ObjectDeserializer.class)
    private ImageUrlResponseBean avatar_url;
    private String total_num;
    private String num;

    public String getUnread_num() {
        if (TextUtils.isEmpty(unread_num)) {
            return "0";
        }
        return unread_num;
    }

    public void setUnread_num(String unread_num) {
        this.unread_num = unread_num;
    }

    private String unread_num;

    public List<PersonnalCommentResponseBean> getComment_info() {
        return comment_info;
    }

    public void setComment_info(List<PersonnalCommentResponseBean> comment_info) {
        this.comment_info = comment_info;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public ImageUrlResponseBean getAvatar_url() {
        return avatar_url;
    }

    public void setAvatar_url(ImageUrlResponseBean avatar_url) {
        this.avatar_url = avatar_url;
    }

    public String getTotal_num() {
        return total_num;
    }

    public void setTotal_num(String total_num) {
        this.total_num = total_num;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }
}
