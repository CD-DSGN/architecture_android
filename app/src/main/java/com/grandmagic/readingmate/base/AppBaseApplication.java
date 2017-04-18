package com.grandmagic.readingmate.base;

import android.app.ActivityManager;
import android.app.Application;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.StrictMode;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.facebook.stetho.Stetho;
import com.grandmagic.readingmate.event.BindDeviceTokenEvent;
import com.grandmagic.readingmate.push.IUmengMessageHandler;
import com.grandmagic.readingmate.push.IUmengNotificationClickHandler;
import com.grandmagic.readingmate.utils.IMHelper;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;
import com.orhanobut.logger.Logger;
import com.squareup.leakcanary.LeakCanary;
import com.tamic.novate.util.SPUtils;
import com.tencent.bugly.crashreport.CrashReport;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

import org.greenrobot.eventbus.EventBus;

import java.util.Iterator;
import java.util.List;

/**
 * Created by zhangmengqi on 2017/1/20.
 */

public class AppBaseApplication extends Application {
    private static final String TAG = "AppBaseApplication";
    public static AppBaseApplication ctx;

    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
        ctx = this;
        Stetho.initializeWithDefaults(this);  //初始化facebook调试工具stetho
        LeakCanary.install(this);    //内存泄漏检测
        MobclickAgent.setCatchUncaughtExceptions(true); //友盟异常捕捉
        //友盟场景设置(普通)
        MobclickAgent.setScenarioType(getApplicationContext(), MobclickAgent.EScenarioType.E_UM_NORMAL);
        initOther();
        initIM();
        //友盟分享配置
        Config.DEBUG = true;
        UMShareAPI.get(this);
        CrashReport.setIsDevelopmentDevice(this, true);
        CrashReport.initCrashReport(getApplicationContext());
        initUPush();
    }

    private void initUPush() {
        PushAgent mPushAgent = PushAgent.getInstance(this);
//注册推送服务，每次调用register方法都会回调该接口
        mPushAgent.register(new IUmengRegisterCallback() {

            @Override
            public void onSuccess(String deviceToken) {
                //注册成功会返回device token
                Log.d(TAG, "onSuccess() called with: deviceToken = [" + deviceToken + "]");
                SPUtils.getInstance().putString(ctx,SPUtils.DEVICE_TOKEN,deviceToken);
                EventBus.getDefault().post(new BindDeviceTokenEvent(deviceToken));
            }

            @Override
            public void onFailure(String s, String s1) {
                Log.d(TAG, "onFailure() called with: s = [" + s + "], s1 = [" + s1 + "]");
            }
        });
//        默认情况下，同一台设备在1分钟内收到同一个应用的多条通知时，不会重复提醒，
// 同时在通知栏里新的通知会替换掉旧的通知。可以通过如下方法来设置冷却时间：
        mPushAgent.setMuteDurationSeconds(3);
      //  是否检查集成配置文件
        mPushAgent.setPushCheck(true);
        mPushAgent.setMessageHandler(new IUmengMessageHandler());
        mPushAgent.setNotificationClickHandler(new IUmengNotificationClickHandler());
    }

    {
        //微信和新浪分享配置
        PlatformConfig.setWeixin("wxdc1e388c3822c80b", "3baf1193c85774b3fd9d18447d76cab0");
        PlatformConfig.setSinaWeibo("3921700954", "04b48b094faeb16683c32669824ebdad", "http://sns.whalecloud.com");
    }

    /**
     * 即时通信相关
     */
    private void initIM() {
        int pid = android.os.Process.myPid();
        String processAppName = getAppName(pid);
// 如果APP启用了远程的service，此application:onCreate会被调用2次
// 为了防止环信SDK被初始化2次，加此判断会保证SDK被初始化1次
// 默认的APP会在以包名为默认的process name下运行，如果查到的process name不是APP的process name就立即返回

        if (processAppName == null || !processAppName.equalsIgnoreCase(this.getPackageName())) {
            Log.e(TAG, "enter the service process!");

            // 则此application::onCreate 是被service 调用的，直接返回
            return;
        }
        EMOptions mEMOptions = new EMOptions();
        mEMOptions.setAcceptInvitationAlways(false);// 默认添加好友时，是不需要验证的，改成需要验证
        EMClient.getInstance().init(this, mEMOptions);
        //在做打包混淆时，关闭debug模式，避免消耗不必要的资源
        EMClient.getInstance().setDebugMode(true);
        IMHelper.getInstance().init(this);
    }

    private String getAppName(int pID) {
        String processName = null;
        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        List l = am.getRunningAppProcesses();
        Iterator i = l.iterator();
        PackageManager pm = this.getPackageManager();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
            try {
                if (info.pid == pID) {
                    processName = info.processName;
                    return processName;
                }
            } catch (Exception e) {
                // Log.d("Process", "Error>> :"+ e.toString());
            }
        }
        return processName;
    }

    private void initOther() {
        /**
         * 7.0的更新安装程序会抛出 FileUriExposedException 异常，需要这么处理
         * http://www.jianshu.com/p/68a4e8132fcd
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder vmpolicy = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(vmpolicy.build());
        }
    }
}
