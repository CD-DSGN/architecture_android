package com.tamic.novate;

/**
 * Created by Tamic on 2016-11-03.
 */

public class Throwable extends Exception {

    private int code;
    private String message;
    private String json;
    private java.lang.Throwable mThrowable;

    public Throwable(java.lang.Throwable throwable, int code) {
        super(throwable);
        mThrowable = throwable;
        this.code = code;
    }

    public Throwable(java.lang.Throwable throwable, int code, String json) {
        super(throwable);
        this.code = code;
        this.json = json;
        mThrowable = throwable;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public java.lang.Throwable getThrowable() {
        return mThrowable;
    }
}
