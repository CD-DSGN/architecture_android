package com.grandmagic.readingmate.bean.response;

import com.google.gson.annotations.JsonAdapter;
import com.google.gson.internal.bind.ListTypeAdapterFactory;

import java.util.List;

/**
 * Created by lps on ${DATA}.
 *
 * @version 1
 * @see
 * @since 16:58
 */


public class BookFollowersResponse {

    /**
     * total_num : 4
     * num : 4
     * info : [{"user_id":"11","avatar_url":{"large":"/images/18161239627/be2ca648986a448f99fdf2b6eae09959.jpg","mid":"/images/18161239627/thumb_be2ca648986a448f99fdf2b6eae09959.jpg"},"user_name":"哈哈","client_id":"djid_JMSz8QHemakZy1","gender":2,"signature":"你好","is_friend":2},{"user_id":"15","avatar_url":{"large":"/images/18380207432/a060b81df92742fe80028428dda5298b.jpg","mid":"/images/18380207432/thumb_a060b81df92742fe80028428dda5298b.jpg"},"user_name":"nhj","client_id":"djid_1W7kayFhlKPUSn","gender":2,"signature":"nhjnhj","is_friend":2},{"user_id":"5","avatar_url":{"large":"/images/18228170108/b13490073c2642148a6bd1eb50d232d2.jpg","mid":"/images/18228170108/thumb_b13490073c2642148a6bd1eb50d232d2.jpg"},"user_name":"小紫","client_id":"djid_1wyiB9UH8g3cKr","gender":1,"signature":"啦啦啦2","is_friend":2},{"user_id":"8","avatar_url":{"large":"/images/18228170109/b4bd0210c18b4d72a213212ef1a5fae0.jpg","mid":"/images/18228170109/thumb_b4bd0210c18b4d72a213212ef1a5fae0.jpg"},"user_name":"lps","client_id":"djid_qQ6fgAY9wKjct2","gender":1,"signature":"啦啦啦2","is_friend":1}]
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
         * user_id : 11
         * avatar_url : {"large":"/images/18161239627/be2ca648986a448f99fdf2b6eae09959.jpg","mid":"/images/18161239627/thumb_be2ca648986a448f99fdf2b6eae09959.jpg"}
         * user_name : 哈哈
         * client_id : djid_JMSz8QHemakZy1
         * gender : 2
         * signature : 你好
         * is_friend : 2
         */

        private String user_id;
        private AvatarUrlBean avatar_url;
        private String user_name;
        private String client_id;
        private int gender;
        private String signature;
        private int is_friend;

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

        public String getUser_name() {
            return user_name;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }

        public String getClient_id() {
            return client_id;
        }

        public void setClient_id(String client_id) {
            this.client_id = client_id;
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

        public int getIs_friend() {
            return is_friend;
        }

        public void setIs_friend(int is_friend) {
            this.is_friend = is_friend;
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

    /**
     * 获取页数
     * @return
     */
    public int getPageCount(){
        try {
            return Integer.valueOf(total_num)/num;
        } catch (NumberFormatException mE) {
            mE.printStackTrace();
            return 0;
        }
    }
}
