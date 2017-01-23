package com.grandmagic.readingmate.base;

import android.content.Context;

import com.tamic.novate.BaseSubscriber;
import com.tamic.novate.Throwable;

/**
 * Created by zhangmengqi on 2017/1/22.
 */

public abstract class  AppBaseSubscriber<T> extends BaseSubscriber<T> {

    public AppBaseSubscriber(Context context) {
        super(context);
    }

    //此处做统一错误处理
    @Override
    public void onError(Throwable e) {






    }
}
