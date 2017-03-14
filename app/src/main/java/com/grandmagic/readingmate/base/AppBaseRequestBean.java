package com.grandmagic.readingmate.base;

import com.google.gson.Gson;

/**
 * Created by zhangmengqi on 2017/3/13.
 */

public class AppBaseRequestBean {
    public String toGson() {
        return new Gson().toJson(this);
    }
}
