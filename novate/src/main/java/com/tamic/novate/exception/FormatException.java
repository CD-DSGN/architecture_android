package com.tamic.novate.exception;

import android.util.Log;

import java.util.logging.Logger;

/**
 * Created by  Tamic on 2016-11-04.
 */

public class FormatException extends RuntimeException {

    public int code = -200;
    public String message = "服务端返回数据格式异常";

    /**
     * Constructs a new runtime exception with {@code null} as its
     * detail message.  The cause is not initialized, and may subsequently be
     * initialized by a call to {@link #initCause}.
     */
    public FormatException(String mMessage) {
        message = mMessage;
        Log.e("FormatException", mMessage );
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
