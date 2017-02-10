package com.grandmagic.readingmate.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by zhangmengqi on 2017/2/9.
 */

public class ViewUtils {
    public static void showToast(Context context, String str) {
        Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
    }
}
