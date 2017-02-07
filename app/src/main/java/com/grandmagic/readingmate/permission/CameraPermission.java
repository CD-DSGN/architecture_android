package com.grandmagic.readingmate.permission;

import android.content.Context;
import android.widget.Toast;

import rx.functions.Action1;

/**
 * Created by lps on 2017/2/7.
 */

public class CameraPermission implements Action1<Boolean> {
    private Context mContext;

    public CameraPermission(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void call(Boolean b) {
        if (b) {

        } else {
            Toast.makeText(mContext, "你没有给予相机权限", Toast.LENGTH_SHORT).show();
        }
    }
}
