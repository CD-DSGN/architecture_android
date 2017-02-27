package com.grandmagic.readingmate.bean.response;

/**
 * Created by lps on 2017/2/22.
*聊天的消息，
 */

public class ChatMessage {
    private String avatar;
    private String name;
    private String msg;
    private String time;
    private String img;
  public enum TYPE{
      SEND,RECICVER,

  }
    public enum MessageType{
        TEXT,IMAGE
    }
 private    TYPE type;
    private MessageType mMessageType;

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

    public String getMsg() {
        return msg;
    }

    public void setMsg(String mMsg) {
        msg = mMsg;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String mTime) {
        time = mTime;
    }

    public TYPE getType() {
        return type;
    }

    public void setType(TYPE mType) {
        type = mType;
    }

    public MessageType getMessageType() {
        return mMessageType;
    }

    public void setMessageType(MessageType mMessageType) {
        this.mMessageType = mMessageType;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String mImg) {
        img = mImg;
    }

    @Override
    public String toString() {
        return "ChatMessage{" +
                "avatar='" + avatar + '\'' +
                ", name='" + name + '\'' +
                ", msg='" + msg + '\'' +
                ", time='" + time + '\'' +
                ", type=" + type +
                '}';
    }
}
