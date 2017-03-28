package com.grandmagic.readingmate.bean.response;

/**
 * Created by zhangmengqi on 2017/3/20.
 */

public class PersonnalCommentResponseBean {
    private String content;
    private String pub_time;
    private String like_times;
    private String book_id;
    private String book_name;
    private String photo;
    private String comment_id;
    private String reply_count;

    public String getComment_id() {
        return comment_id;
    }

    public void setComment_id(String comment_id) {
        this.comment_id = comment_id;
    }

    public String getReply_count() {
        return reply_count;
    }

    public void setReply_count(String reply_count) {
        this.reply_count = reply_count;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPub_time() {
        return pub_time;
    }

    public void setPub_time(String pub_time) {
        this.pub_time = pub_time;
    }

    public String getLike_times() {
        return like_times;
    }

    public void setLike_times(String like_times) {
        this.like_times = like_times;
    }

    public String getBook_id() {
        return book_id;
    }

    public void setBook_id(String book_id) {
        this.book_id = book_id;
    }

    public String getBook_name() {
        return book_name;
    }

    public void setBook_name(String book_name) {
        this.book_name = book_name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
