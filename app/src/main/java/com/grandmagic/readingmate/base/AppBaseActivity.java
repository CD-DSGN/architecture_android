package com.grandmagic.readingmate.base;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.grandmagic.readingmate.R;
import com.grandmagic.readingmate.activity.LoginActivity;
import com.grandmagic.readingmate.activity.MainActivity;
import com.grandmagic.readingmate.event.ConnectStateEvent;
import com.grandmagic.readingmate.utils.SystemBarTintManager;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.tamic.novate.RxApiManager;
import com.tamic.novate.util.SPUtils;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

/**
 * Created by zhangmengqi on 2017/1/22.
 */

public class AppBaseActivity extends AppCompatActivity {
    RxApiManager mRxApiManager = new RxApiManager();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        PushAgent.getInstance(this).onAppStart();
        EventBus.getDefault().register(this);
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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRxApiManager.removeAll();
        EventBus.getDefault().unregister(this);
    }

//    protected static void addSubscriber(Subscription subscription) {
//        if (mRxApiManager != null) {
//            mRxApiManager.add(subscription);
//        }
//    }

    /**
     * 状态栏着色
     *
     * @param color
     */
    public void setSystemBarColor(int color) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        }

        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(color);
    }

    /**
     * 状态栏透明
     *
     * @param on
     */
    @TargetApi(19)
    public void setTranslucentStatus(boolean on) {
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
    AlertDialog mDialog;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ConnectStateEvent mEvent) {
        ActivityManager mActivityManager = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> mRunningTasks = mActivityManager.getRunningTasks(1);
        if (mRunningTasks!=null&&mRunningTasks.size()>0){
            ComponentName mName=mRunningTasks.get(0).topActivity;
            if (this.getClass().getName().equals(mName.getClassName())){//判断activity是否在栈顶
                showLoginAnotherDevice();
            }
        }
    }

    private void showLoginAnotherDevice() {
        String state = SPUtils.getInstance().getString(this, SPUtils.IM_STATE);
        if (Integer.valueOf(state) == (EMError.USER_LOGIN_ANOTHER_DEVICE)) {
            if (mDialog==null) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
                mBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mDialog.dismiss();
                    }
                });
                mBuilder.setPositiveButton("重新登陆", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mDialog.dismiss();
                        SPUtils.getInstance().clearToken(AppBaseActivity.this);
                        EMClient.getInstance().logout(true);//环信的退出
                        startActivity(new Intent(AppBaseActivity.this, LoginActivity.class));
                        finish();
                    }
                });
                mDialog = mBuilder.create();
            }
            mDialog.setTitle("下线通知");
            mDialog.setMessage("账号已在其他设备登陆，您被迫下线");
            mDialog.show();
        }
    }
}
