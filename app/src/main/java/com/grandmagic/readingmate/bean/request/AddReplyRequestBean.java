package com.grandmagic.readingmate.bean.request;

import com.grandmagic.readingmate.base.AppBaseRequestBean;

/**
 * Created by zhangmengqi on 2017/3/31.
 */

public class AddReplyRequestBean extends AppBaseRequestBean {
    private String comment_id;
    private String pid;
    private String content;

    public String getComment_id() {
        return comment_id;
    }

    public void setComment_id(String comment_id) {
        this.comment_id = comment_id;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
