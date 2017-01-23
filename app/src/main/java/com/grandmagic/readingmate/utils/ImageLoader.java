package com.grandmagic.readingmate.utils;

import android.view.View;

/**
 * Created by zhangmengqi on 2017/1/22.
 */

public interface ImageLoader {
    public abstract void init();  //初始化图片加载器

    public abstract void display(String url, View view); //利用图片加载器进行图片加载
}
