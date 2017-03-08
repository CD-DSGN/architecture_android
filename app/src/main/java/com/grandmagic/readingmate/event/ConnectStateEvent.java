package com.grandmagic.readingmate.event;

/**
 * Created by lps on 2017/3/8.
 */

public class ConnectStateEvent {
    public int errcode;

    public ConnectStateEvent() {
    }

    public ConnectStateEvent(int mErrcode) {
        errcode = mErrcode;
    }
}
