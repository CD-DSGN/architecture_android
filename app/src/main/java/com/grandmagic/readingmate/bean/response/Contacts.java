package com.grandmagic.readingmate.bean.response;

/**
 * Created by lps on 2017/2/21.
 */

public class Contacts {
      TYPE type;
  public   enum  TYPE{
        TYPE_LETTER,//字母索引
        TYPE_NEWFRIEND,//新朋友的头部
        TYPE_FRIEND//普通的好友
    }
    String text;

    public Contacts() {
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
