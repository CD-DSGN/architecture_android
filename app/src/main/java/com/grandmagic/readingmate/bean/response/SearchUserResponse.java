package com.grandmagic.readingmate.bean.response;

/**
 * Created by lps on 2017/3/6.
 */

public class SearchUserResponse {

    /**
     * user_name : 小花
     * gender : 1
     * user_id : 1
     * avatar_native : /images/18508236987/3fc5d1ac659846368d522a6ea5ffa427.jpg
     * avatar_thumb : /images/18508236987/thumb_3fc5d1ac659846368d522a6ea5ffa427.jpg
     * signature : 啦啦啦
     */

    private String user_name;
    private String gender;
    private String user_id;
    private String avatar_native;
    private String avatar_thumb;
    private String signature;

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

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
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
}
