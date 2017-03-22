package com.grandmagic.readingmate.bean.request;

import com.grandmagic.readingmate.base.AppBaseRequestBean;

/**
 * Created by zhangmengqi on 2017/3/22.
 */

public class CommentDetailRequesetBean extends AppBaseRequestBean {
    private String comment_id;

    public String getComment_id() {
        return comment_id;
    }

    public void setComment_id(String comment_id) {
        this.comment_id = comment_id;
    }
}
