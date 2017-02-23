package com.grandmagic.readingmate.bean.response;

/**
 * Created by lps on 2017/2/21.
 * 通讯录的好友列表
 */

public class Contacts {
  private   TYPE type;

    public enum TYPE {
        TYPE_LETTER,//字母索引
        TYPE_NEWFRIEND,//新朋友的头部
        TYPE_FRIEND//普通的好友
    }

   private String text;
   private boolean needline=true;//是否需要下划线

    public boolean isNeedline() {
        return needline;
    }

    public void setNeedline(boolean mNeedline) {
        needline = mNeedline;
    }

    public Contacts() {
    }

    public Contacts(TYPE mType, String mText, boolean mNeedline) {
        needline = mNeedline;
        text = mText;
        type = mType;
    }

    public Contacts(String mText) {
        text = mText;
    }

    public Contacts(TYPE mType) {
        type = mType;
    }

    public Contacts(TYPE mType, String mText) {
        type = mType;
        text = mText;
    }

    public TYPE getType() {
        return type;
    }

    public void setType(TYPE mType) {
        type = mType;
    }

    public String getText() {
        return text;
    }

    public void setText(String mText) {
        text = mText;
    }

    @Override
    public String toString() {
        return "Contacts{" +
                "type=" + type +
                ", text='" + text + '\'' +
                '}';
    }
}
