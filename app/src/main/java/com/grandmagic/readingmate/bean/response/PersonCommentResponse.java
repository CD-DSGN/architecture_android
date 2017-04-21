package com.grandmagic.readingmate.bean.response;

import com.google.gson.annotations.JsonAdapter;
import com.google.gson.internal.bind.ListTypeAdapterFactory;
import com.google.gson.internal.bind.ObjectDeserializer;

import java.util.List;

/**
 * Created by lps on 2017/3/29.
 *
 * @version 1
 * @see
 * @since 2017/3/29 11:13
 * 个人详细，的评论
 */


public class PersonCommentResponse {

    /**
     * total_num : 39
     * num : 3
     * comment_info : [{"comment_id":"71","reply_count":"0","content":"还我漂漂拳39","pub_time":"1490182352","like_times":"0","book_id":"1","book_name":"唐诗三百首精选","photo":"/image/6.jpg"},{"comment_id":"70","reply_count":"0","content":"还我漂漂拳38","pub_time":"1490182352","like_times":"0","book_id":"1","book_name":"唐诗三百首精选","photo":"/image/6.jpg"},{"comment_id":"69","reply_count":"0","content":"还我漂漂拳37","pub_time":"1490182352","like_times":"0","book_id":"1","book_name":"唐诗三百首精选","photo":"/image/6.jpg"}]
     * user_name : 哈哈
     * avatar_url : {"large":"/images/18161239627/be2ca648986a448f99fdf2b6eae09959.jpg","mid":"/images/18161239627/thumb_be2ca648986a448f99fdf2b6eae09959.jpg"}
     */
    @JsonAdapter(ObjectDeserializer.class)
    private int total_num;
    @JsonAdapter(ObjectDeserializer.class)
    private int num;
    private String user_name;
    private AvatarUrlBean avatar_url;
    @JsonAdapter(ListTypeAdapterFactory.class)
    private List<CommentInfoBean> comment_info;

    public int getTotal_num() {
        return total_num;
    }

    public void setTotal_num(int total_num) {
        this.total_num = total_num;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
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

    public List<CommentInfoBean> getComment_info() {
        return comment_info;
    }

    public void setComment_info(List<CommentInfoBean> comment_info) {
        this.comment_info = comment_info;
    }

    public static class AvatarUrlBean {
        /**
         * large : /images/18161239627/be2ca648986a448f99fdf2b6eae09959.jpg
         * mid : /images/18161239627/thumb_be2ca648986a448f99fdf2b6eae09959.jpg
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

    public static class CommentInfoBean {
        /**
         * comment_id : 71
         * reply_count : 0
         * content : 还我漂漂拳39
         * pub_time : 1490182352
         * like_times : 0
         * book_id : 1
         * book_name : 唐诗三百首精选
         * photo : /image/6.jpg
         */

        private String comment_id;
        private String reply_count;
        private String content;
        private String pub_time;
        private String like_times;
        private String book_id;
        private String book_name;
        private String photo;
        private int is_thumb;

        public int getIs_thumb() {
            return is_thumb;
        }

        public void setIs_thumb(int mIs_thumb) {
            is_thumb = mIs_thumb;
        }

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

    public int getPageCount() {
        try {
            return (int) Math.ceil(total_num * 1f / num);
        } catch (Exception mE) {
            mE.printStackTrace();
        }
        return 1;
    }

}
