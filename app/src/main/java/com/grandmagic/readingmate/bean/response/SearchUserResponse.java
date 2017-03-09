package com.grandmagic.readingmate.bean.response;

import java.util.List;

/**
 * Created by lps on 2017/3/6.
 */

public class SearchUserResponse {


    /**
     * collection : [{"book_name":"深入浅出MySQL","is_both_enjoy":2}]
     * personalinfo : {"user_id":8,"user_name":"小华","gender":1,"signature":"啦啦啦2","avatar_native":"/images/18228170109/751ca8d636454ebebb1190f982031f54.jpg","avatar_thumb":"/images/18228170109/thumb_751ca8d636454ebebb1190f982031f54.jpg"}
     */

    private PersonalinfoBean personalinfo;
    private List<CollectionBean> collection;

    public PersonalinfoBean getPersonalinfo() {
        return personalinfo;
    }

    public void setPersonalinfo(PersonalinfoBean personalinfo) {
        this.personalinfo = personalinfo;
    }

    public List<CollectionBean> getCollection() {
        return collection;
    }

    public void setCollection(List<CollectionBean> collection) {
        this.collection = collection;
    }

    public static class PersonalinfoBean {
        /**
         * user_id : 8
         * user_name : 小华
         * gender : 1
         * signature : 啦啦啦2
         * avatar_native : /images/18228170109/751ca8d636454ebebb1190f982031f54.jpg
         * avatar_thumb : /images/18228170109/thumb_751ca8d636454ebebb1190f982031f54.jpg
         */

        private String user_id;
        private String user_name;
        private int gender;
        private String signature;
        private String avatar_native;
        private String avatar_thumb;

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String mUser_id) {
            user_id = mUser_id;
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

        public String getAvatar_native() {
            return avatar_native;
        }

        public void setAvatar_native(String avatar_native) {
            this.avatar_native = avatar_native;
        }

        public String getAvatar_thumb() {
            return avatar_thumb;
        }

        public void setAvatar_thumb(String avatar_thumb) {
            this.avatar_thumb = avatar_thumb;
        }
    }

    public static class CollectionBean {
        /**
         * book_name : 深入浅出MySQL
         * is_both_enjoy : 2
         */

        private String book_name;
        private int is_both_enjoy;

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
