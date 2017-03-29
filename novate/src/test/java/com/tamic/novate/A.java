package com.tamic.novate;

import java.util.List;

/**
 * Created by lps on 2017/3/29.
 *
 * @version 1
 * @see
 * @since 2017/3/29 10:33
 */


public class A {

    /**
     * code : 200
     * message : 已成功查询到该书详情
     * data : {"book_name":"唐诗三百首精选2","synopsis":"","author":"崔钟雷2","photo":"/image/6.jpg","publisher":"人民出版社","pub_date":"2009-9-3","collect_count":"4","collect_user":[{"user_id":"11","avatar_url":{"large":"/images/18161239627/be2ca648986a448f99fdf2b6eae09959.jpg","mid":"/images/18161239627/thumb_be2ca648986a448f99fdf2b6eae09959.jpg"}},{"user_id":"8","avatar_url":{"large":"/images/18228170109/b4bd0210c18b4d72a213212ef1a5fae0.jpg","mid":"/images/18228170109/thumb_b4bd0210c18b4d72a213212ef1a5fae0.jpg"}},{"user_id":"1","avatar_url":{"large":"/images/18508236987/3804f901bba742e0b0a92d885aaaf5d7.jpg","mid":"/images/18508236987/thumb_3804f901bba742e0b0a92d885aaaf5d7.jpg"}},{"user_id":"3","avatar_url":{"large":"","mid":""}}],"score_times":"","total_score":"","is_follow":1}
     */

    private int code;
    private String message;
    private DataBean data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        @Override
        public String toString() {
            return "DataBean{" +
                    "book_name='" + book_name + '\'' +
                    ", synopsis='" + synopsis + '\'' +
                    ", author='" + author + '\'' +
                    ", photo='" + photo + '\'' +
                    ", publisher='" + publisher + '\'' +
                    ", pub_date='" + pub_date + '\'' +
                    ", collect_count='" + collect_count + '\'' +
                    ", score_times='" + score_times + '\'' +
                    ", total_score='" + total_score + '\'' +
                    ", is_follow=" + is_follow +
                    ", collect_user=" + collect_user +
                    '}';
        }

        /**
         * book_name : 唐诗三百首精选2
         * synopsis :
         * author : 崔钟雷2
         * photo : /image/6.jpg
         * publisher : 人民出版社
         * pub_date : 2009-9-3
         * collect_count : 4
         * collect_user : [{"user_id":"11","avatar_url":{"large":"/images/18161239627/be2ca648986a448f99fdf2b6eae09959.jpg","mid":"/images/18161239627/thumb_be2ca648986a448f99fdf2b6eae09959.jpg"}},{"user_id":"8","avatar_url":{"large":"/images/18228170109/b4bd0210c18b4d72a213212ef1a5fae0.jpg","mid":"/images/18228170109/thumb_b4bd0210c18b4d72a213212ef1a5fae0.jpg"}},{"user_id":"1","avatar_url":{"large":"/images/18508236987/3804f901bba742e0b0a92d885aaaf5d7.jpg","mid":"/images/18508236987/thumb_3804f901bba742e0b0a92d885aaaf5d7.jpg"}},{"user_id":"3","avatar_url":{"large":"","mid":""}}]
         * score_times :
         * total_score :
         * is_follow : 1
         */

        private String book_name;
        private String synopsis;
        private String author;
        private String photo;
        private String publisher;
        private String pub_date;
        private String collect_count;
        private int score_times;
        private float total_score;
        private int is_follow;
        private List<CollectUserBean> collect_user;

        public String getBook_name() {
            return book_name;
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


        public int getScore_times() {
            return score_times;
        }

        public void setScore_times(int mScore_times) {
            score_times = mScore_times;
        }

        public float getTotal_score() {
            return total_score;
        }

        public void setTotal_score(float mTotal_score) {
            total_score = mTotal_score;
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
             * user_id : 11
             * avatar_url : {"large":"/images/18161239627/be2ca648986a448f99fdf2b6eae09959.jpg","mid":"/images/18161239627/thumb_be2ca648986a448f99fdf2b6eae09959.jpg"}
             */

            private String user_id;
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
        }
    }

    @Override
    public String toString() {
        return "A{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
