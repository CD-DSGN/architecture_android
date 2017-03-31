package com.grandmagic.readingmate.bean.response;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;
import com.tamic.novate.util.Environment;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.converter.PropertyConverter;

/**
 * Created by lps on 2017/2/21.
 * 通讯录的好友列表
 *
 * @since 2017年3月29日13:48:26 添加签名字段
 */
@Entity
public class Contacts {
    @Id
    Long id;
    @Transient
    private TYPE type;

    public Contacts(TYPE mTypeNewfriend, int mNewFriendCOunt) {
        type=mTypeNewfriend;
        gender=mNewFriendCOunt;//新朋友的时候用不到gender字段。借用来保存请求数量
    }


    public enum TYPE {
        TYPE_LETTER,//字母索引
        TYPE_NEWFRIEND,//新朋友的头部
        TYPE_FRIEND//普通的好友
    }

    @Unique
    private int user_id;
    @Convert(converter = AvatarCoverter.class, columnType = String.class)
    private AvatarUrlBean avatar_url;
    private String user_name;
    private boolean needline = true;//是否需要下划线
    private String letter;//首字母
    private String pyName;
    private String clientid;
    private String remark;
    private int gender;
    private String signature;

    public String getSignature() {
        return signature;
    }

    public void setSignature(String mSignature) {
        signature = mSignature;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int mGender) {
        gender = mGender;
    }

    public Contacts(TYPE mTypeLetter, String mLetter) {
        type = mTypeLetter;
        letter = mLetter;
    }

    public AvatarUrlBean getAvatar_url() {
        return avatar_url;
    }

    public void setAvatar_url(String mAvatar_url) {
        if (avatar_url==null)avatar_url=new AvatarUrlBean();
        avatar_url.setLarge(mAvatar_url);
    }

    public String getLetter() {
        return letter;
    }

    public void setLetter(String mLetter) {
        letter = mLetter;
    }

    public String getUser_name() {
        return TextUtils.isEmpty(remark) ? user_name : remark;//如果有昵称返回昵称
    }

    public String getPyName() {
        return pyName;
    }

    public String getClientid() {
        return clientid;
    }

    public void setClientid(String mClientid) {
        clientid = mClientid;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String mRemark) {
        remark = mRemark;
    }

    public void setUser_id(int mUser_id) {
        user_id = mUser_id;
    }


    public Contacts(TYPE mType) {
        type = mType;
    }

    @Generated(hash = 2024653179)
    public Contacts(Long id, int user_id, AvatarUrlBean avatar_url, String user_name,
                    boolean needline, String letter, String pyName, String clientid, String remark,
                    int gender, String signature) {
        this.id = id;
        this.user_id = user_id;
        this.avatar_url = avatar_url;
        this.user_name = user_name;
        this.needline = needline;
        this.letter = letter;
        this.pyName = pyName;
        this.clientid = clientid;
        this.remark = remark;
        this.gender = gender;
        this.signature = signature;
    }

    @Generated(hash = 1804918957)
    public Contacts() {
    }


    public TYPE getType() {
        return type;
    }

    public Contacts setType(TYPE mType) {
        type = mType;
        return this;
    }

    @Override
    public String toString() {
        return "Contacts{" +
                "id=" + id +
                ", type=" + type +
                ", user_id=" + user_id +
                ", avatar_url=" + avatar_url +
                ", user_name='" + user_name + '\'' +
                ", needline=" + needline +
                ", letter='" + letter + '\'' +
                ", pyName='" + pyName + '\'' +
                ", clientid='" + clientid + '\'' +
                ", remark='" + remark + '\'' +
                ", gender=" + gender +
                ", signature='" + signature + '\'' +
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

    public void setAvatar_url(AvatarUrlBean avatar_url) {
        this.avatar_url = avatar_url;
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

        public AvatarUrlBean setLarge(String large) {
            this.large = large;
            return this;
        }

        public String getMid() {
            return mid;
        }

        public void setMid(String mid) {
            this.mid = mid;
        }

        @Override
        public String toString() {
            return "AvatarUrlBean{" +
                    "large='" + large + '\'' +
                    ", mid='" + mid + '\'' +
                    '}';
        }
    }

    public static class AvatarCoverter implements PropertyConverter<AvatarUrlBean, String> {

        @Override
        public AvatarUrlBean convertToEntityProperty(String databaseValue) {
            return new AvatarUrlBean().setLarge(databaseValue);
        }


        @Override
        public String convertToDatabaseValue(AvatarUrlBean entityProperty) {
            return entityProperty.getLarge();
        }
    }
}
