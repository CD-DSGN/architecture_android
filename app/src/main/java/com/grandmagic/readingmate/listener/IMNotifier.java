package com.grandmagic.readingmate.listener;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;

import com.hyphenate.chat.EMMessage;

import java.util.List;

/**
 * Created by lps on 2017/3/2.
 * IM提示相关
 */

public class IMNotifier {

    protected static int notifyID = 0525; // start notification id
    protected static int foregroundNotifyID = 0555;
    private NotificationManager mNotificationManager;
    private String packName;
    private AudioManager mAudioManager;
    private Vibrator mVibrator;
    private Context mAppContext;

    public void init(Context mAppContext) {
        mNotificationManager = (NotificationManager) mAppContext.getSystemService(Context.NOTIFICATION_SERVICE);
        packName = mAppContext.getApplicationInfo().packageName;
        mAudioManager = (AudioManager) mAppContext.getSystemService(Context.AUDIO_SERVICE);
        mVibrator = (Vibrator) mAppContext.getSystemService(Context.VIBRATOR_SERVICE);
        this.mAppContext = mAppContext;
    }

    public void newMsg(EMMessage mMessage) {
        sendNotification(mMessage, isAppRunningForeground());
    }

    protected void sendNotification(EMMessage message, boolean isForeground) {
        sendNotification(message, isForeground, true);
    }

    protected final static String[] msgs = {"发来一条消息", "发来一张图片", "发来一段语音", "发来位置信息", "发来一个视频", "发来一个文件",
            "%1个联系人发来%2条消息"
    };

    /**
     * send it to notification bar
     * This can be override by subclass to provide customer implementation
     *未完善
     * @param message
     */
    protected void sendNotification(EMMessage message, boolean isForeground, boolean numIncrease) {
        // TODO: 2017/3/2 免打扰等设置判断，暂时没处理
        String username = message.getFrom();
        try {
            String notifytxt = username + "";
            switch (message.getType()) {
                case TXT:
                    notifytxt += msgs[0];
                    break;
                case IMAGE:
                    notifytxt += msgs[1];
                    break;
            }
            PackageManager mPackageManager = mAppContext.getPackageManager();
            String appname = (String) mPackageManager.getApplicationLabel(mAppContext.getApplicationInfo());
            String contenttitle = appname;
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mAppContext)
                    .setSmallIcon(mAppContext.getApplicationInfo().icon)
                    .setWhen(System.currentTimeMillis())
                    .setAutoCancel(true);
            Intent msgIntent = mAppContext.getPackageManager().getLaunchIntentForPackage(packName);
            PendingIntent mPendingIntent = PendingIntent.getActivity(mAppContext, notifyID, msgIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder.setContentTitle(contenttitle);
            mBuilder.setTicker(notifytxt);
            mBuilder.setContentText(message.getBody().toString());
            mBuilder.setContentIntent(mPendingIntent);
            Notification mNotification = mBuilder.build();
            mNotificationManager.notify(foregroundNotifyID, mNotification);
        } catch (Exception e) {

        }
    }

    /**
     * app是否在后台运行
     *
     * @return
     */
    public boolean isAppRunningForeground() {
        ActivityManager mActivityManager = (ActivityManager) mAppContext.getSystemService(Context.ACTIVITY_SERVICE);
        try {
            List<ActivityManager.RunningTaskInfo> mRunningTasks = mActivityManager.getRunningTasks(1);
            if (mRunningTasks != null && mRunningTasks.size() > 0) {
                return mAppContext.getPackageName().equalsIgnoreCase(((ActivityManager.RunningTaskInfo) mRunningTasks.get(0)).baseActivity.getPackageName());

            } else {
                return false;
            }
        } catch (SecurityException mE) {
            mE.printStackTrace();
            return false;
        }
    }
}
