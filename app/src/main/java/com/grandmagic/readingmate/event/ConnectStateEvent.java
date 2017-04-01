package com.grandmagic.readingmate.event;

/**
 * Created by lps on 2017/3/8.
 * 与IM服务器连接状态变更的事件
 */

public class ConnectStateEvent {
    public int errcode;

    public ConnectStateEvent() {
    }

    public ConnectStateEvent(int mErrcode) {
        errcode = mErrcode;
    }
}
