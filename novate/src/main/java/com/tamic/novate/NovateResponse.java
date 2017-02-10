package com.tamic.novate;


import android.content.Context;

import com.tamic.novate.config.ConfigLoader;

/**
 * BaseResponse Data T
 * Created by Tamic on 2016-06-06.
 */
public class NovateResponse<T> {
    //结果码
    private int code;
    /*错误信息:msg, error, message*/
    private String msg;
    /*真实数据 data或者result*/
    private T data;


    public int getCode() {
        return code;
    }

    public void setCode(int mCode) {
        code = mCode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String mMsg) {
        msg = mMsg;
    }

    public T getData() {
        return data;
    }

    public void setData(T mData) {
        data = mData;
    }
    public boolean isOk(Context context) {
        return ConfigLoader.checkSucess(context, getCode());
    }
    @Override
    public String toString() {
        return "NovateResponse{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
