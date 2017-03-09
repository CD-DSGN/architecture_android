package com.grandmagic.readingmate.bean.response;

/**
 * Created by lps  on 2017/3/9.
 */

public class RequestListResponse {

    /**
     * user_id : 6
     * user_name : 小青
     * gender : 1
     * request_id : 12
     * avatar_native :
     * avatar_thumb :
     * signature : 啦啦啦
     * message :
     */

    private String user_id;
    private String user_name;
    private String gender;
    private String request_id;
    private String avatar_native;
    private String avatar_thumb;
    private String signature;
    private String message;

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

    public String getRequest_id() {
        return request_id;
    }

    public void setRequest_id(String request_id) {
        this.request_id = request_id;
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

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
