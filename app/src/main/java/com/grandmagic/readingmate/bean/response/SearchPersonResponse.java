package com.grandmagic.readingmate.bean.response;

import com.google.gson.annotations.JsonAdapter;
import com.google.gson.internal.bind.ListTypeAdapterFactory;
import com.google.gson.internal.bind.ObjectDeserializer;

import java.util.List;

/**
 * Created by lps on 2017/3/16.
 */
public class SearchPersonResponse {


    /**
     * num : 2
     * info : [{"user_id":5,"user_name":"小紫","gender":1,"signature":"啦啦啦2","avatar_url":{"large":"","mid":""},"is_friend":2,"collection":[{"book_id":"8","book_name":"代码大全","is_both_enjoy":2},{"book_id":"1","book_name":"唐诗三百首精选","is_both_enjoy":1},{"book_id":"9","book_name":"Introduction to Algorithms","is_both_enjoy":1}],"distance":1.1},{"user_id":6,"user_name":"小青","gender":1,"signature":"啦啦啦","avatar_url":{"large":"","mid":""},"is_friend":1,"collection":"","distance":4.5}]
     */

    private int num;
    @JsonAdapter(ListTypeAdapterFactory.class)
    private List<InfoBean> info;

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
         * user_id : 5
         * user_name : 小紫
         * gender : 1
         * signature : 啦啦啦2
         * avatar_url : {"large":"","mid":""}
         * is_friend : 2
         * collection : [{"book_id":"8","book_name":"代码大全","is_both_enjoy":2},{"book_id":"1","book_name":"唐诗三百首精选","is_both_enjoy":1},{"book_id":"9","book_name":"Introduction to Algorithms","is_both_enjoy":1}]
         * distance : 1.1
         */

        private int user_id;
        private String user_name;
        private int gender;
        private String signature;
        @JsonAdapter(ObjectDeserializer.class)
        private AvatarUrlBean avatar_url;
        private int is_friend;
        private double distance;
        @JsonAdapter(ListTypeAdapterFactory.class)
        private List<CollectionBean> collection;
        private String clientid;

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public String getUser_name() {
            return user_name;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }

        public int getGender() {
            return gender;
        }

        public void setGender(int gender) {
            this.gender = gender;
        }

        public String getSignature() {
            return signature;
        }

        public void setSignature(String signature) {
            this.signature = signature;
        }

        public AvatarUrlBean getAvatar_url() {
            return avatar_url;
        }

        public void setAvatar_url(AvatarUrlBean avatar_url) {
            this.avatar_url = avatar_url;
        }

        public int getIs_friend() {
            return is_friend;
        }

        public void setIs_friend(int is_friend) {
            this.is_friend = is_friend;
        }

        public double getDistance() {
            return distance;
        }

        public void setDistance(double distance) {
            this.distance = distance;
        }

        public List<CollectionBean> getCollection() {
            return collection;
        }

        public void setCollection(List<CollectionBean> collection) {
            this.collection = collection;
        }

        public void setClientid(String mClientid) {
            clientid = mClientid;
        }

        public String getClientid() {
            return clientid;
        }

        public static class AvatarUrlBean {
            /**
             * large :
             * mid :
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

        public static class CollectionBean {
            /**
             * book_id : 8
             * book_name : 代码大全
             * is_both_enjoy : 2
             */

            private String book_id;
            private String book_name;
            private int is_both_enjoy;

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

            public int getIs_both_enjoy() {
                return is_both_enjoy;
            }

            public void setIs_both_enjoy(int is_both_enjoy) {
                this.is_both_enjoy = is_both_enjoy;
            }
        }
    }
}
