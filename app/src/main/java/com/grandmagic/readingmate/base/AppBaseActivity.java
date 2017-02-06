package com.grandmagic.readingmate.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

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

}
