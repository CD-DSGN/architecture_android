package com.grandmagic.readingmate.bean.response;

import com.google.gson.annotations.JsonAdapter;
import com.google.gson.internal.bind.ObjectDeserializer;
import com.grandmagic.readingmate.bean.db.Contacts;

/**
 * Created by lps on 2017/4/6.
 *
 * @version 1
 * @see
 * @since 2017/4/6 16:47
 */


public class OtherInfoResponse {

    /**
     * user_id : 49
     * user_name : lps的小号
     * clientid : djid_bWgt4XvUID8xrF
     * gender : 3
     * signature :
     * avatar_url : {"large":"/images/17608062092/32a7df38f45d4c3785fab668192c6725.jpg","mid":"/images/17608062092/thumb_32a7df38f45d4c3785fab668192c6725.jpg"}
     * is_friend : 1
     */

    private int user_id;
    private String user_name;
    private String clientid;
    private int gender;
    private String signature;
    @JsonAdapter(ObjectDeserializer.class)
    private Contacts.AvatarUrlBean avatar_url;
    private int is_friend;

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

    public String getClientid() {
        return clientid;
    }

    public void setClientid(String clientid) {
        this.clientid = clientid;
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

    public Contacts.AvatarUrlBean getAvatar_url() {
        return avatar_url;
    }

    public void setAvatar_url(Contacts.AvatarUrlBean mAvatar_url) {
        avatar_url = mAvatar_url;
    }

    public int getIs_friend() {
        return is_friend;
    }

    public void setIs_friend(int is_friend) {
        this.is_friend = is_friend;
    }

    public static class AvatarUrlBean {
        /**
         * large : /images/17608062092/32a7df38f45d4c3785fab668192c6725.jpg
         * mid : /images/17608062092/thumb_32a7df38f45d4c3785fab668192c6725.jpg
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
