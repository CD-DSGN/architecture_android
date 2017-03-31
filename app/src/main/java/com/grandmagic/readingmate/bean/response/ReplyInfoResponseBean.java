package com.grandmagic.readingmate.bean.response;

import com.google.gson.annotations.JsonAdapter;
import com.google.gson.internal.bind.ListTypeAdapterFactory;
import com.google.gson.internal.bind.ObjectDeserializer;

import java.util.List;

/**
 * Created by zhangmengqi on 2017/3/22.
 */

public class ReplyInfoResponseBean {

    /**
     * total_num : 2
     * num : 2
     * info : [{"is_commented":1,"is_reply_thumb":2,"like_times":"0","from_user_avatar_url":{"large":"/images/18508236987/3804f901bba742e0b0a92d885aaaf5d7.jpg","mid":"/images/18508236987/thumb_3804f901bba742e0b0a92d885aaaf5d7.jpg"},"from_user_name":"小花","from_user_id":"1","to_user_avatar_url":"","to_user_name":"","to_user_id":"","to_user_comment":"","reply_content":"有几分道理","reply_pub_time":"1490090247","reply_comment_reply_id":"7","pid":"0"},{"is_commented":2,"is_reply_thumb":1,"like_times":"1","from_user_avatar_url":{"large":"","mid":""},"from_user_name":"小呆","from_user_id":"10","to_user_avatar_url":"","to_user_name":"","to_user_id":"","to_user_comment":"","reply_content":"这个评论要逆天","reply_pub_time":"1490607239","reply_comment_reply_id":"9","pid":"8"}]
     */

    private String total_num;
    private int num;

    @JsonAdapter(ListTypeAdapterFactory.class)
    private List<InfoBean> info;

    public String getTotal_num() {
        return total_num;
    }

    public void setTotal_num(String total_num) {
        this.total_num = total_num;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public List<InfoBean> getInfo() {
        return info;
    }

    public void setInfo(List<InfoBean> info) {
        this.info = info;
    }

    public static class InfoBean {
        /**
         * is_commented : 1
         * is_reply_thumb : 2
         * like_times : 0
         * from_user_avatar_url : {"large":"/images/18508236987/3804f901bba742e0b0a92d885aaaf5d7.jpg","mid":"/images/18508236987/thumb_3804f901bba742e0b0a92d885aaaf5d7.jpg"}
         * from_user_name : 小花
         * from_user_id : 1
         * to_user_avatar_url :
         * to_user_name :
         * to_user_id :
         * to_user_comment :
         * reply_content : 有几分道理
         * reply_pub_time : 1490090247
         * reply_comment_reply_id : 7
         * pid : 0
         */

        private int is_commented;
        private int is_reply_thumb;
        private String like_times;
        @JsonAdapter(ObjectDeserializer.class)
        private FromUserAvatarUrlBean from_user_avatar_url;
        private String from_user_name;
        private String from_user_id;
        @JsonAdapter(ObjectDeserializer.class)
        private FromUserAvatarUrlBean to_user_avatar_url;
        private String to_user_name;
        private String to_user_id;
        private String to_user_comment;
        private String reply_content;
        private String reply_pub_time;
        private String reply_comment_reply_id;
        private String pid;

        public int getIs_commented() {
            return is_commented;
        }

        public void setIs_commented(int is_commented) {
            this.is_commented = is_commented;
        }

        public int getIs_reply_thumb() {
            return is_reply_thumb;
        }

        public void setIs_reply_thumb(int is_reply_thumb) {
            this.is_reply_thumb = is_reply_thumb;
        }

        public String getLike_times() {
            return like_times;
        }

        public void setLike_times(String like_times) {
            this.like_times = like_times;
        }

        public FromUserAvatarUrlBean getFrom_user_avatar_url() {
            return from_user_avatar_url;
        }

        public void setFrom_user_avatar_url(FromUserAvatarUrlBean from_user_avatar_url) {
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

        public FromUserAvatarUrlBean getTo_user_avatar_url() {
            return to_user_avatar_url;
        }

        public void setTo_user_avatar_url(FromUserAvatarUrlBean to_user_avatar_url) {
            this.to_user_avatar_url = to_user_avatar_url;
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

        public String getTo_user_comment() {
            return to_user_comment;
        }

        public void setTo_user_comment(String to_user_comment) {
            this.to_user_comment = to_user_comment;
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

        public String getPid() {
            return pid;
        }

        public void setPid(String pid) {
            this.pid = pid;
        }

        public static class FromUserAvatarUrlBean {
            /**
             * large : /images/18508236987/3804f901bba742e0b0a92d885aaaf5d7.jpg
             * mid : /images/18508236987/thumb_3804f901bba742e0b0a92d885aaaf5d7.jpg
             */

            private String large;
            private String mid;

            public String getLarge() {
                return large;
            }

            public void setLarge(String large) {
                this.large = large;
            }

            public String getMid() {
                return mid;
            }

            public void setMid(String mid) {
                this.mid = mid;
            }
        }
    }
}
