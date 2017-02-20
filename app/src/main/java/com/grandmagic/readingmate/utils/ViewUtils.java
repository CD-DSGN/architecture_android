package com.grandmagic.readingmate.utils;

import android.widget.Toast;

import com.grandmagic.readingmate.base.AppBaseApplication;

/**
 * Created by zhangmengqi on 2017/2/9.
 */

public class ViewUtils {
    public static void showToast(String str) {
        Toast.makeText(AppBaseApplication.ctx, str, Toast.LENGTH_SHORT).show();
    }
}
