package com.grandmagic.readingmate.bean.response;

/**
 * Created by lps on 2017/2/22.
 * 最近会话列表 模拟数据实体
 */

public class RecentConversation {
    private String avatar;
    private String name;
    private String content;
    private String time;
private int type=0;

    public int getType() {
        return type;
    }

    public void setType(int mType) {
        type = mType;
    }

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

    public String getContent() {
        return content;
    }

    public void setContent(String mContent) {
        content = mContent;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String mTime) {
        time = mTime;
    }

    @Override
    public String toString() {
        return "RecentConversation{" +
                "avatar='" + avatar + '\'' +
                ", name='" + name + '\'' +
                ", content='" + content + '\'' +
                ", time='" + time + '\'' +
                ", type=" + type +
                '}';
    }
}
