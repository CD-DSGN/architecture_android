package com.grandmagic.readingmate.bean.response;

import com.tamic.novate.util.Environment;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Unique;

/**
 * Created by lps on 2017/2/21.
 * 通讯录的好友列表
 */
@Entity
public class Contacts {
    @Id
    Long id;
    @Transient
    private TYPE type;


    public enum TYPE {
        TYPE_LETTER,//字母索引
        TYPE_NEWFRIEND,//新朋友的头部
        TYPE_FRIEND//普通的好友
    }
@Unique
    private int user_id;
    private String avatar_native;
    private String user_name;
    private boolean needline = true;//是否需要下划线
    private String letter;//首字母
    private String pyName;

    public Contacts() {
    }

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
        return avatar_native;
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

    @Generated(hash = 1100261463)
    public Contacts(Long id, int user_id, String avatar_native, String user_name, boolean needline,
            String letter, String pyName) {
        this.id = id;
        this.user_id = user_id;
        this.avatar_native = avatar_native;
        this.user_name = user_name;
        this.needline = needline;
        this.letter = letter;
        this.pyName = pyName;
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

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean getNeedline() {
        return this.needline;
    }
}
