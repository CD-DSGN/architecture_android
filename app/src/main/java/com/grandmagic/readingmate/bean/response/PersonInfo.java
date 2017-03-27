package com.grandmagic.readingmate.bean.response;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by lps on 2017/3/22.
 * 在别人的详细页使用。
 * 通过聊天界面{@link com.grandmagic.readingmate.activity.ChatActivity }
 * 和搜索结果页{@link com.grandmagic.readingmate.fragment.SearchFragment}
 * 以及通过手机号{@link com.grandmagic.readingmate.activity.AddFriendActivity}
 * 搜索跳转的时候携带个人信息,因为之前的接口返回的实体类已经包含这些信息 ，但是各自是不同的类，
 * 字段也不完全一致用Intent分别传输需要的每个字段页较为繁琐
 */

public class PersonInfo implements Parcelable {
    private String avatar;
    private String user_id;
    private String nickname;
    private String clientid;//读家账号字段
    private boolean isFriend;//是否已经是好友关系
    private int gender = 3;

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String mAvatar) {
        avatar = mAvatar;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String mUser_id) {
        user_id = mUser_id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String mNickname) {
        nickname = mNickname;
    }

    public String getClientid() {
        return clientid;
    }

    public void setClientid(String mClientid) {
        clientid = mClientid;
    }

    public boolean isFriend() {
        return isFriend;
    }

    public void setFriend(boolean mFriend) {
        isFriend = mFriend;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int mGender) {
        gender = mGender;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.avatar);
        dest.writeString(this.user_id);
        dest.writeString(this.nickname);
        dest.writeString(this.clientid);
        dest.writeByte(this.isFriend ? (byte) 1 : (byte) 0);
        dest.writeInt(this.gender);
    }

    public PersonInfo() {
    }

    protected PersonInfo(Parcel in) {
        this.avatar = in.readString();
        this.user_id = in.readString();
        this.nickname = in.readString();
        this.clientid = in.readString();
        this.isFriend = in.readByte() != 0;
        this.gender = in.readInt();
    }

    public static final Creator<PersonInfo> CREATOR = new Creator<PersonInfo>() {
        @Override
        public PersonInfo createFromParcel(Parcel source) {
            return new PersonInfo(source);
        }

        @Override
        public PersonInfo[] newArray(int size) {
            return new PersonInfo[size];
        }
    };
}
