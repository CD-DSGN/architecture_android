package com.grandmagic.readingmate.bean.response;

import com.google.gson.annotations.JsonAdapter;
import com.google.gson.internal.bind.ObjectDeserializer;

import java.util.List;

/**
 * Created by lps on 2017/3/16.
 */

public class SearchPersonResponse {

    /**
     * num : 2
     * info : [{"user_id":"5","user_name":"小紫","gender":"1","distance":1.1,"avatar_url":""},{"user_id":"6","user_name":"小青","gender":"1","distance":0,"avatar_url":""}]
     */

    private int num;
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
         * distance : 1.1
         * avatar_url :
         */

        private String user_id;
        private String user_name;
        private String gender;
        private double distance;

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getUser_name() {
            return user_name;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public double getDistance() {
            return distance;
        }

        public void setDistance(double distance) {
            this.distance = distance;
        }
    }
    @JsonAdapter(ObjectDeserializer.class)
    private ImageUrlResponseBean avatar_url;

}
