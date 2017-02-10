package com.grandmagic.readingmate.base;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.grandmagic.readingmate.R;
import com.grandmagic.readingmate.utils.SystemBarTintManager;
import com.tamic.novate.RxApiManager;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by zhangmengqi on 2017/1/22.
 */

public class AppBaseActivity extends AppCompatActivity {
    private static RxApiManager mRxApiManager = RxApiManager.get();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mRxApiManager != null) {
            mRxApiManager.cancelAll();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

//    protected static void addSubscriber(Subscription subscription) {
//        if (mRxApiManager != null) {
//            mRxApiManager.add(subscription);
//        }
//    }

    /**
     * 状态栏着色
     * @param color
     */
    protected void setSystemBarColor(int color) {

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
        setTranslucentStatus(true);
    }

    SystemBarTintManager tintManager = new SystemBarTintManager(this);
    tintManager.setStatusBarTintEnabled(true);
    tintManager.setStatusBarTintResource(color);
}

    /**
     * 状态栏透明
     * @param on
     */
    @TargetApi(19)
    protected void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }
}
