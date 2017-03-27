package com.grandmagic.readingmate.bean.response;

import com.google.gson.annotations.JsonAdapter;
import com.google.gson.internal.bind.ListTypeAdapterFactory;
import com.google.gson.internal.bind.ObjectDeserializer;

import java.util.List;

/**
 * Created by lps  on 2017/3/15.
 */

public class BookdetailResponse {

    /**
     * book_name : 深入浅出MySQL
     * synopsis :
     * author : 唐汉明
     * photo : http://192.168.1.115/reading-partner-php/api/web/image/6.jpg
     * publisher : 人民邮电出版社
     * pub_date : 2009-10-1
     * collect_count : 2
     * collect_user : [{"user_id":"8","avatar_url":{"large":"/images/18228170109/ad83d40f8ba444b1b90ce6173d20aff8.jpg","mid":"/images/18228170109/thumb_ad83d40f8ba444b1b90ce6173d20aff8.jpg"}},{"user_id":"1","avatar_url":{"large":"/images/18508236987/3804f901bba742e0b0a92d885aaaf5d7.jpg","mid":"/images/18508236987/thumb_3804f901bba742e0b0a92d885aaaf5d7.jpg"}}]
     * comment : {"score_times":1,"total_score":9}
     * is_follow : 1
     */

    private String book_name;
    private String synopsis;
    private String author;
    private String photo;
    private String publisher;
    private String pub_date;
    private String collect_count;
    private int is_follow;
    private String score_times;
    private String total_score;
    @JsonAdapter(ListTypeAdapterFactory.class)
    private List<CollectUserBean> collect_user;

    public String getBook_name() {
        return book_name;
    }

    public String getScore_times() {
        return score_times;
    }

    public void setScore_times(String mScore_times) {
        score_times = mScore_times;
    }

    public String getTotal_score() {
        return total_score;
    }

    public void setTotal_score(String mTotal_score) {
        total_score = mTotal_score;
    }

    public void setBook_name(String book_name) {
        this.book_name = book_name;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getPub_date() {
        return pub_date;
    }

    public void setPub_date(String pub_date) {
        this.pub_date = pub_date;
    }

    public String getCollect_count() {
        return collect_count;
    }

    public void setCollect_count(String collect_count) {
        this.collect_count = collect_count;
    }

    public int getIs_follow() {
        return is_follow;
    }

    public void setIs_follow(int is_follow) {
        this.is_follow = is_follow;
    }

    public List<CollectUserBean> getCollect_user() {
        return collect_user;
    }

    public void setCollect_user(List<CollectUserBean> collect_user) {
        this.collect_user = collect_user;
    }

    public static class CollectUserBean {
        /**
         * user_id : 8
         * avatar_url : {"large":"/images/18228170109/ad83d40f8ba444b1b90ce6173d20aff8.jpg","mid":"/images/18228170109/thumb_ad83d40f8ba444b1b90ce6173d20aff8.jpg"}
         */

        private String user_id;
        @JsonAdapter(ObjectDeserializer.class)
        private AvatarUrlBean avatar_url;

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public AvatarUrlBean getAvatar_url() {
            return avatar_url;
        }

        public void setAvatar_url(AvatarUrlBean avatar_url) {
            this.avatar_url = avatar_url;
        }

        public static class AvatarUrlBean {
            /**
             * large : /images/18228170109/ad83d40f8ba444b1b90ce6173d20aff8.jpg
             * mid : /images/18228170109/thumb_ad83d40f8ba444b1b90ce6173d20aff8.jpg
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
