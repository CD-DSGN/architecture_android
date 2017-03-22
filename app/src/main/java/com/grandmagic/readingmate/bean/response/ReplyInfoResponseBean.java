package com.grandmagic.readingmate.bean.response;

import com.google.gson.annotations.JsonAdapter;
import com.google.gson.internal.bind.ObjectDeserializer;

/**
 * Created by zhangmengqi on 2017/3/22.
 */

public class ReplyInfoResponseBean {
    @JsonAdapter(ObjectDeserializer.class)
    ImageUrlResponseBean from_user_avatar_url;

    String from_user_name;
    String from_user_id;
    String to_user_name;
    String to_user_id;
    String reply_content;
    String reply_pub_time;
    String reply_comment_reply_id;

    public ImageUrlResponseBean getFrom_user_avatar_url() {
        return from_user_avatar_url;
    }

    public void setFrom_user_avatar_url(ImageUrlResponseBean from_user_avatar_url) {
        this.from_user_avatar_url = from_user_avatar_url;
    }

    public String getFrom_user_name() {
        return from_user_name;
    }

    public void setFrom_user_name(String from_user_name) {
        this.from_user_name = from_user_name;
    }

    public String getFrom_user_id() {
        return from_user_id;
    }

    public void setFrom_user_id(String from_user_id) {
        this.from_user_id = from_user_id;
    }

    public String getTo_user_name() {
        return to_user_name;
    }

    public void setTo_user_name(String to_user_name) {
        this.to_user_name = to_user_name;
    }

    public String getTo_user_id() {
        return to_user_id;
    }

    public void setTo_user_id(String to_user_id) {
        this.to_user_id = to_user_id;
    }

    public String getReply_content() {
        return reply_content;
    }

    public void setReply_content(String reply_content) {
        this.reply_content = reply_content;
    }

    public String getReply_pub_time() {
        return reply_pub_time;
    }

    public void setReply_pub_time(String reply_pub_time) {
        this.reply_pub_time = reply_pub_time;
    }

    public String getReply_comment_reply_id() {
        return reply_comment_reply_id;
    }

    public void setReply_comment_reply_id(String reply_comment_reply_id) {
        this.reply_comment_reply_id = reply_comment_reply_id;
    }

    public ImageUrlResponseBean getTo_user_avatar_url() {
        return to_user_avatar_url;
    }

    public void setTo_user_avatar_url(ImageUrlResponseBean to_user_avatar_url) {
        this.to_user_avatar_url = to_user_avatar_url;
    }

    @JsonAdapter(ObjectDeserializer.class)
    ImageUrlResponseBean to_user_avatar_url;

}
