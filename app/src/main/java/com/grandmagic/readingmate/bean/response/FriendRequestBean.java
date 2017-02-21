package com.grandmagic.readingmate.bean.response;

/**
 * Created by lps on 2017/2/21.
 */
public class FriendRequestBean {
    private String avatar;
    private String name;
    private String verify;
    private int state;

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String mAvatar) {
        avatar = mAvatar;
    }

    public String getName() {
        return name;
    }

    public void setName(String mName) {
        name = mName;
    }

    public int getState() {
        return state;
    }

    public void setState(int mState) {
        state = mState;
    }

    public String getVerify() {
        return verify;
    }

    public void setVerify(String mVerify) {
        verify = mVerify;
    }

    @Override
    public String toString() {
        return "FriendRequestBean{" +
                "avatar='" + avatar + '\'' +
                ", name='" + name + '\'' +
                ", verify='" + verify + '\'' +
                ", state=" + state +
                '}';
    }
}
