package com.grandmagic.readingmate.bean.response;

import com.tamic.novate.util.Environment;

/**
 * Created by lps on 2017/2/21.
 * 通讯录的好友列表
 */

public class Contacts {
    private TYPE type;


    public enum TYPE {
        TYPE_LETTER,//字母索引
        TYPE_NEWFRIEND,//新朋友的头部
        TYPE_FRIEND//普通的好友
    }

    private int user_id;
    private String avatar_native;
    private String user_name;
    private boolean needline = true;//是否需要下划线
    private String letter;//首字母
    private String pyName;


    public Contacts(TYPE mTypeFriend, String mUser_name, String mAvatar_native) {
        this.type = mTypeFriend;
        this.user_name = mUser_name;
        this.avatar_native = mAvatar_native;
    }

    public String getLetter() {
        return letter;
    }

    public void setLetter(String mLetter) {
        letter = mLetter;
    }

    public String getUser_name() {
        return user_name;
    }

    public String getPyName() {
        return pyName;
    }

    public void setPyName(String mPyName) {
        pyName = mPyName;
    }

    public void setUser_name(String mUser_name) {
        user_name = mUser_name;
    }

    public boolean isNeedline() {
        return needline;
    }

    public void setNeedline(boolean mNeedline) {
        needline = mNeedline;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int mUser_id) {
        user_id = mUser_id;
    }

    public String getAvatar_native() {
        return Environment.BASEULR_PRODUCTION +avatar_native;
    }

    public void setAvatar_native(String mAvatar_native) {
        avatar_native = mAvatar_native;
    }

    public Contacts(TYPE mTypeFriend, int mUser_id, String mUser_name, String mAvatar_native) {
    this.type=mTypeFriend;
        this.user_id=mUser_id;
        this.user_name=mUser_name;
        this.avatar_native=mAvatar_native;
    }

    public Contacts(TYPE mType, String mLetter) {
        type = mType;
        letter = mLetter;
    }

    public Contacts(TYPE mType, String mText, boolean mNeedline) {
        needline = mNeedline;
        type = mType;
    }


    public Contacts(TYPE mType) {
        type = mType;
    }


    public TYPE getType() {
        return type;
    }

    public void setType(TYPE mType) {
        type = mType;
    }

    @Override
    public String toString() {
        return "Contacts{" +
                "type=" + type +
                ", user_id=" + user_id +
                ", avatar_native='" + getAvatar_native() + '\'' +
                ", user_name='" + user_name + '\'' +
                ", needline=" + needline +
                '}';
    }
}
