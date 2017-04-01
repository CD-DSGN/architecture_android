package com.grandmagic.readingmate.event;

/**
 * Created by lps on 2017/4/1.
 *
 * @version 1
 * @see
 * @since 2017/4/1 10:56
 * 被对方删除时的事件
 */


public class ContactDeletedEvent {
    private String name;

    public ContactDeletedEvent() {
    }

    public ContactDeletedEvent(String mName) {
        name = mName;
    }

    public String getName() {
        return name;
    }

    public void setName(String mName) {
        name = mName;
    }
}
