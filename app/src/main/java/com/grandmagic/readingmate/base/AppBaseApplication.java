package com.grandmagic.readingmate.base;

import android.app.Application;

import com.facebook.stetho.Stetho;
import com.squareup.leakcanary.LeakCanary;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by zhangmengqi on 2017/1/20.
 */

public class AppBaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);  //初始化facebook调试工具stetho
        LeakCanary.install(this);    //内存泄漏检测
        MobclickAgent.setCatchUncaughtExceptions(true); //友盟异常捕捉
        //友盟场景设置(普通)
        MobclickAgent.setScenarioType(getApplicationContext(), MobclickAgent.EScenarioType.E_UM_NORMAL);
    }
}
