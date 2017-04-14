package com.grandmagic.readingmate.bean.response;

import com.google.gson.annotations.JsonAdapter;
import com.google.gson.internal.bind.ListTypeAdapterFactory;
import com.google.gson.internal.bind.ObjectDeserializer;

import java.util.List;

/**
 * Created by zhangmengqi on 2017/3/22.
 */

public class CommentsDetailResponoseBean {


    /**
     * is_thumb : 2
     * reply_count : 6
     * user_name : nhj
     * avatar_url : {"large":"/images/18380207432/a060b81df92742fe80028428dda5298b.jpg","mid":"/images/18380207432/thumb_a060b81df92742fe80028428dda5298b.jpg"}
     * comment_id : 7
     * content : 那奂捷加的测试的，一会删了那奂捷加的测试的，一会删了那奂捷加的测试的，一会删了那奂捷加的测试的，一会删了那奂捷加的测试的，一会删了那奂捷加的测试的，一会删了那奂捷加的测试的，一会删了
     * pub_time : 1490255652
     * like_times : 1
     * book_name : 唐诗三百首精选
     * book_id : 1
     * photo : /image/6.jpg
     * thumb_user_avatar : [{"user_id":"15","avatar_url":{"large":"/images/18380207432/a060b81df92742fe80028428dda5298b.jpg","mid":"/images/18380207432/thumb_a060b81df92742fe80028428dda5298b.jpg"}},{"user_id":"13","avatar_url":{"large":"","mid":""}},{"user_id":"3","avatar_url":{"large":"","mid":""}}]
     */

    private int is_thumb;
    private String reply_count;
    private String user_name;
    @JsonAdapter(ObjectDeserializer.class)
    private AvatarUrlBean avatar_url;
    private String comment_id;
    private String content;
    private int pub_time;
    private int like_times;
    private String book_name;
    private int book_id;
    private String photo;

    public String getIs_self_comment() {
        return is_self_comment;
    }

    public void setIs_self_comment(String is_self_comment) {
        this.is_self_comment = is_self_comment;
    }

    private String is_self_comment;

    @JsonAdapter(ListTypeAdapterFactory.class)
    private List<ThumbUserAvatarBean> thumb_user_avatar;


    public int getIs_thumb() {
        return is_thumb;
    }

    public void setIs_thumb(int is_thumb) {
        this.is_thumb = is_thumb;
    }

    public String getReply_count() {
        return reply_count;
    }

    public void setReply_count(String reply_count) {
        this.reply_count = reply_count;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public AvatarUrlBean getAvatar_url() {
        return avatar_url;
    }

    public void setAvatar_url(AvatarUrlBean avatar_url) {
        this.avatar_url = avatar_url;
    }

    public String getComment_id() {
        return comment_id;
    }

    public void setComment_id(String comment_id) {
        this.comment_id = comment_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getPub_time() {
        return pub_time;
    }

    public void setPub_time(int pub_time) {
        this.pub_time = pub_time;
    }

    public int getLike_times() {
        return like_times;
    }

    public void setLike_times(int like_times) {
        this.like_times = like_times;
    }

    public String getBook_name() {
        return book_name;
    }

    public void setBook_name(String book_name) {
        this.book_name = book_name;
    }

    public int getBook_id() {
        return book_id;
    }

    public void setBook_id(int book_id) {
        this.book_id = book_id;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public List<ThumbUserAvatarBean> getThumb_user_avatar() {
        return thumb_user_avatar;
    }

    public void setThumb_user_avatar(List<ThumbUserAvatarBean> thumb_user_avatar) {
        this.thumb_user_avatar = thumb_user_avatar;
    }

    public static class AvatarUrlBean {
        /**
         * large : /images/18380207432/a060b81df92742fe80028428dda5298b.jpg
         * mid : /images/18380207432/thumb_a060b81df92742fe80028428dda5298b.jpg
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

    public static class ThumbUserAvatarBean {
        /**
         * user_id : 15
         * avatar_url : {"large":"/images/18380207432/a060b81df92742fe80028428dda5298b.jpg","mid":"/images/18380207432/thumb_a060b81df92742fe80028428dda5298b.jpg"}
         */

        private String user_id;
        private AvatarUrlBeanX avatar_url;

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public AvatarUrlBeanX getAvatar_url() {
            return avatar_url;
        }

        public void setAvatar_url(AvatarUrlBeanX avatar_url) {
            this.avatar_url = avatar_url;
        }

        public static class AvatarUrlBeanX {
            /**
             * large : /images/18380207432/a060b81df92742fe80028428dda5298b.jpg
             * mid : /images/18380207432/thumb_a060b81df92742fe80028428dda5298b.jpg
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
