package com.grandmagic.readingmate.bean.response;

import com.google.gson.annotations.JsonAdapter;
import com.google.gson.internal.bind.ListTypeAdapterFactory;
import com.google.gson.internal.bind.ObjectDeserializer;

import java.util.List;

import static java.lang.Math.ceil;

/**
 * Created by lps on 2017/3/20.
 */

public class BookCommentResponse {

    /**
     * total_num : 2
     * num : 2
     * comments : [{"user_name":null,"avatar":"","pub_time":"1489666174","like_times":"0","contents":"haoshu","user_score":"9","reply_count":""},{"user_name":null,"avatar":"","pub_time":"1489654126","like_times":"1","contents":"这本书碉堡了","user_score":"9","reply_count":""}]
     */

    private int total_num;
    private int num;
    @JsonAdapter(ListTypeAdapterFactory.class)
    private List<CommentsBean> comments;

    public int getPageCount() {
        if (total_num == 0) return 0;
        return (int) ceil(total_num * 1.0f / num);
    }

    public int getTotal_num() {
        return total_num;
    }

    public void setTotal_num(int mTotal_num) {
        total_num = mTotal_num;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public List<CommentsBean> getComments() {
        return comments;
    }

    public void setComments(List<CommentsBean> comments) {
        this.comments = comments;
    }

    public static class CommentsBean {
        /**
         * user_name : null
         * avatar :
         * pub_time : 1489666174
         * like_times : 0
         * contents : haoshu
         * user_score : 9
         * reply_count :
         */

        private String user_name;
        @JsonAdapter(ObjectDeserializer.class)
        private ImageUrlResponseBean avatar;
        private String pub_time;
        private int like_times;
        private String contents;
        private String user_score;
        private String reply_count;
        private String comment_id;
        private String thumb_up;

        public String getComment_id() {
            return comment_id;
        }

        public void setComment_id(String mComment_id) {
            comment_id = mComment_id;
        }

        public String getThumb_up() {
            return thumb_up;
        }

        public void setThumb_up(String mThumb_up) {
            thumb_up = mThumb_up;
        }

        public String getUser_name() {
            return user_name;
        }

        public void setUser_name(String mUser_name) {
            user_name = mUser_name;
        }

        public ImageUrlResponseBean getAvatar() {
            return avatar;
        }

        public void setAvatar(ImageUrlResponseBean mAvatar) {
            avatar = mAvatar;
        }

        public String getPub_time() {
            return pub_time;
        }

        public void setPub_time(String mPub_time) {
            pub_time = mPub_time;
        }

        public int getLike_times() {
            return like_times;
        }

        public void setLike_times(int mLike_times) {
            like_times = mLike_times;
        }

        public String getContents() {
            return contents;
        }

        public void setContents(String mContents) {
            contents = mContents;
        }

        public String getUser_score() {
            return user_score;
        }

        public void setUser_score(String mUser_score) {
            user_score = mUser_score;
        }

        public String getReply_count() {
            return reply_count;
        }

        public void setReply_count(String mReply_count) {
            reply_count = mReply_count;
        }
    }
}
