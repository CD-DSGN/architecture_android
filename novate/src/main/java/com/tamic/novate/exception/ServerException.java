package com.tamic.novate.exception;

/**
 * Created by Tamic on 2016-11-04.
 */

public class ServerException extends RuntimeException {

    public int code;
    public String message;
    public String json;

    public ServerException(int code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public ServerException(int code, String message, String json) {
        this(code, message);
        this.json = json;
    }
}
