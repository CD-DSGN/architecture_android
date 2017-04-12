package com.grandmagic.readingmate.utils;

import android.app.Dialog;
import android.widget.Toast;

import com.grandmagic.readingmate.base.AppBaseApplication;

/**
 * Created by zhangmengqi on 2017/2/9.
 */

public class ViewUtils {
    public static void showToast(String str) {
        Toast.makeText(AppBaseApplication.ctx, str, Toast.LENGTH_SHORT).show();
    }

    public static void safeShowDialog(Dialog dialog) {
        try {
            if(dialog != null && !dialog.isShowing()) {
                dialog.show();
            }
        } catch (Exception var2) {

        }
    }

    public static void safeCloseDialog(Dialog dialog) {
        try {
            if(dialog != null && dialog.isShowing()) {
                dialog.dismiss();
                dialog = null;
            }
        } catch (Exception var2) {

        }

    }
}
