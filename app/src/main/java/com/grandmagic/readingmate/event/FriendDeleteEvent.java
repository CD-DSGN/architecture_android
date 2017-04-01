package com.grandmagic.readingmate.event;

/**
 * Created by lps  on 2017/3/13.
 * 删除好友的event
 */

public class FriendDeleteEvent {
    String user_id;

    public FriendDeleteEvent(String mUser_id) {
        user_id = mUser_id;
    }

    public FriendDeleteEvent() {
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String mUser_id) {
        user_id = mUser_id;
    }
}
