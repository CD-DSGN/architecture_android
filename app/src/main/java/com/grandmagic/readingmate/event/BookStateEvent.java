package com.grandmagic.readingmate.event;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by lps on 2017/4/14.
 *
 * @version 1
 * @see
 * @since 2017/4/14 10:20
 * 对书进行了操作的Event
 */


public class BookStateEvent {
    public static final int STATE_MOVE =1;
    public static final int STATE_DELETE=2;
    @State
    private int mState;
    private String bookid;

    @IntDef({STATE_MOVE,STATE_DELETE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface State{}

    public BookStateEvent(int mState, String mBookid) {
        this.mState = mState;
        bookid = mBookid;
    }

    public BookStateEvent() {
    }

    public int getState() {
        return mState;
    }

    public void setState(int mState) {
        this.mState = mState;
    }

    public String getBookid() {
        return bookid;
    }

    public void setBookid(String mBookid) {
        bookid = mBookid;
    }
}
