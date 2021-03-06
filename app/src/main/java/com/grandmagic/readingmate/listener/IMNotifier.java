package com.grandmagic.readingmate.listener;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.bumptech.glide.BitmapRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.grandmagic.readingmate.R;
import com.grandmagic.readingmate.activity.MainActivity;
import com.grandmagic.readingmate.base.AppBaseResponseCallBack;
import com.grandmagic.readingmate.bean.db.Contacts;
import com.grandmagic.readingmate.bean.db.InviteMessage;
import com.grandmagic.readingmate.bean.response.OtherInfoResponse;
import com.grandmagic.readingmate.db.DBHelper;
import com.grandmagic.readingmate.db.InviteMessageDao;
import com.grandmagic.readingmate.model.ContactModel;
import com.grandmagic.readingmate.utils.KitUtils;
import com.hyphenate.easeui.utils.GlideCircleTransform;
import com.grandmagic.readingmate.utils.IMHelper;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.tamic.novate.NovateResponse;
import com.tamic.novate.util.Environment;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;

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
    Ringtone mRingtone;
    private Context mAppContext;
    private long lastNotifiyTime;//上次提示时间
    private IMSettingsProvider mSettingsProvider;

    public void init(Context mAppContext) {
        mNotificationManager = (NotificationManager) mAppContext.getSystemService(Context.NOTIFICATION_SERVICE);
        packName = mAppContext.getApplicationInfo().packageName;
        mAudioManager = (AudioManager) mAppContext.getSystemService(Context.AUDIO_SERVICE);
        mVibrator = (Vibrator) mAppContext.getSystemService(Context.VIBRATOR_SERVICE);
        this.mAppContext = mAppContext;
    }

    public void newMsg(EMMessage mMessage) {
        mSettingsProvider = IMHelper.getInstance().getSettingsProvider();
        if (!mSettingsProvider.isMsgNotifyAllowed(mMessage)) {
            return;
        }
        sendNotification(mMessage, isAppRunningForeground());
        vibrateAndPlayTone(mMessage);
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
     * 未完善
     *
     * @param message
     */
    protected void sendNotification(EMMessage message, boolean isForeground, boolean numIncrease) {
        // TODO: 2017/3/2 免打扰等设置判断，暂时没处理
        Contacts mUserInfo = IMHelper.getInstance().getUserInfo(message.getFrom());
        String username = mUserInfo == null ? message.getFrom() : mUserInfo.getUser_name();
        String notifytxt = username + "";
        switch (message.getType()) {
            case TXT:
                notifytxt += msgs[0];
                break;
            case IMAGE:
                notifytxt += msgs[1];
                break;
        }
        getIconBitmap(mUserInfo, notifytxt, message);

    }

    private void getIconBitmap(final Contacts mUserInfo, final String mNotifytxt, final EMMessage mMessage) {

        Observable
                .just(Environment.BASEULR_PRODUCTION + mUserInfo.getAvatar_url().getLarge())
                .flatMap(new Func1<String, Observable<BitmapRequestBuilder<String, Bitmap>>>() {
                    @Override
                    public Observable<BitmapRequestBuilder<String, Bitmap>> call(String mS) {
                        BitmapRequestBuilder<String, Bitmap> mTransform = Glide.with(mAppContext).load(mS).asBitmap().transform(new GlideCircleTransform(mAppContext));
                        return Observable.just(mTransform);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BitmapRequestBuilder<String, Bitmap>>() {
                    @Override
                    public void onCompleted() {
                        Log.e(TAG, "onCompleted: ");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: ");
                        showNotification(mUserInfo, mNotifytxt, mMessage, null);
                    }

                    @Override
                    public void onNext(BitmapRequestBuilder<String, Bitmap> mStringBitmapTypeRequest) {
                        Log.e(TAG, "onNext: ");
                        mStringBitmapTypeRequest.into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                Log.e(TAG, "onResourceReady: ");
                                showNotification(mUserInfo, mNotifytxt, mMessage, resource);
                            }

                            /**
                             * {@inheritDoc}
                             *
                             * @param e
                             * @param errorDrawable
                             */
                            @Override
                            public void onLoadFailed(Exception e, Drawable errorDrawable) {
                                super.onLoadFailed(e, errorDrawable);
                                showNotification(mUserInfo, mNotifytxt, mMessage, null);
                            }
                        });
                    }
                });


    }

    private void showNotification(Contacts mUserInfo, String notifytxt, EMMessage message, Bitmap mResource) {
        Log.e(TAG, "showNotification() called with: mUserInfo = [" + mUserInfo + "], notifytxt = [" + notifytxt + "], message = [" + message + "], mResource = [" + mResource + "]");
        PackageManager mPackageManager = mAppContext.getPackageManager();
        String appname = (String) mPackageManager.getApplicationLabel(mAppContext.getApplicationInfo());
        String contenttitle = mUserInfo == null ? appname : mUserInfo.getUser_name();
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mAppContext)
                .setWhen(System.currentTimeMillis())
                .setColor(mAppContext.getResources().getColor(R.color.text_green))
                .setSmallIcon(R.drawable.app_logo)
                .setLargeIcon(mResource)
                .setAutoCancel(true);
        Intent msgIntent = new Intent(mAppContext, MainActivity.class);
        msgIntent.putExtra(IMMessageListenerApp.FLAG_NEWMESSAGE, IMMessageListenerApp.FLAG_NEWMESSAGE);
        PendingIntent mPendingIntent = PendingIntent.getActivity(mAppContext, notifyID, msgIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentTitle(KitUtils.unicodeDecode(contenttitle));
        mBuilder.setTicker(KitUtils.unicodeDecode(notifytxt));
        if (message.getType() == EMMessage.Type.TXT) {
            mBuilder.setContentText( ((EMTextMessageBody) message.getBody()).getMessage());
        } else if (message.getType() == EMMessage.Type.IMAGE) {
            mBuilder.setContentText( "[图片]");
        } else if (message.getType() == EMMessage.Type.VOICE) {
            mBuilder.setContentText("[语音]");
        } else if (message.getType() == EMMessage.Type.LOCATION) {
            mBuilder.setContentText("[地理位置]");
        } else {
            mBuilder.setContentText("有新的消息");
        }
        mBuilder.setContentIntent(mPendingIntent);
        Notification mNotification = mBuilder.build();
        mNotificationManager.notify(foregroundNotifyID, mNotification);
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

    private static final String TAG = "IMNotifier";

    /**
     * 声音和震动提示
     *
     * @param mMessage
     */
    public void vibrateAndPlayTone(EMMessage mMessage) {
//        间隔时间小于一秒则不提示
        if (System.currentTimeMillis() - lastNotifiyTime < 1 * 1000) {
            return;
        }
        lastNotifiyTime = System.currentTimeMillis();
        if (mAudioManager.getRingerMode() == AudioManager.RINGER_MODE_SILENT) {
            Log.e(TAG, "vibrateAndPlayTone: 手机静音模式不提shi");
            return;
        }
        if (mMessage == null || mSettingsProvider.isMsgVibrateAllowed(mMessage)) {
            long pattern[] = new long[]{0, 180, 80, 20};
            mVibrator.vibrate(pattern, -1);
        }
        if (mMessage == null || mSettingsProvider.isMsgSoundAllowed(mMessage)) {
            if (mRingtone == null) {
                Uri mDefaultUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                mRingtone = RingtoneManager.getRingtone(mAppContext, mDefaultUri);
                if (mRingtone == null) {
                    Log.e(TAG, "vibrateAndPlayTone: 没找到提示文件");
                    return;
                }
            }

            if (!mRingtone.isPlaying()) {
                String vendor = Build.MANUFACTURER;
                mRingtone.play();
                // for samsung S3, we meet a bug that the phone will
                // continue ringtone without stop
                // so add below special handler to stop it after 3s if
                // needed
                if (vendor != null && vendor.toLowerCase().contains("samsung")) {
                    Thread ctlThread = new Thread() {
                        public void run() {
                            try {
                                Thread.sleep(3000);
                                if (mRingtone.isPlaying()) {
                                    mRingtone.stop();
                                }
                            } catch (Exception e) {
                            }
                        }
                    };
                    ctlThread.run();
                }
            }
        }
    }

    /**
     * 好友请求相关提示
     *
     * @param mInviteMessage
     */
    public void newInvaiteMsg(final InviteMessage mInviteMessage) {
        InviteMessageDao mInviteDao = DBHelper.getInviteDao(mAppContext);
        mInviteDao.insertOrReplace(mInviteMessage);//每次收到新的好友请求保存到表里
        DBHelper.close();
        vibrateAndPlayTone(null);
        new ContactModel(mAppContext).getOtherUserInfo(mInviteMessage.getFrom(), new AppBaseResponseCallBack<NovateResponse<OtherInfoResponse>>(mAppContext) {
            @Override
            public void onSuccee(NovateResponse<OtherInfoResponse> response) {
                mInviteMessage.setFrom(response.getData().getUser_name());
                mInviteMessage.setAvatar(response.getData().getAvatar_url().getLarge());
                sendInviteNotification(mInviteMessage);
            }
        });
    }

    /**
     * 好友相关的提示
     *
     * @param msg
     */
    private void sendInviteNotification(final InviteMessage msg) {
        Observable.just(Environment.BASEULR_PRODUCTION+msg.getAvatar())
                .flatMap(new Func1<String, Observable<BitmapRequestBuilder<String, Bitmap>>>() {
                    @Override
                    public Observable<BitmapRequestBuilder<String, Bitmap>> call(String mS) {
                        BitmapRequestBuilder<String, Bitmap> mTransform = Glide.with(mAppContext).load(mS).asBitmap().transform(new GlideCircleTransform(mAppContext));
                        return Observable.just(mTransform);
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BitmapRequestBuilder<String, Bitmap>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        showInviteNotifi(msg,null);

                    }

                    @Override
                    public void onNext(BitmapRequestBuilder<String, Bitmap> mStringBitmapBitmapRequestBuilder) {
                        mStringBitmapBitmapRequestBuilder.into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                Log.e(TAG, "onResourceReady: ");
                                showInviteNotifi(msg,resource);
                            }

                            @Override
                            public void onLoadFailed(Exception e, Drawable errorDrawable) {
                                super.onLoadFailed(e, errorDrawable);
                                showInviteNotifi(msg, null);
                            }
                        });

                    }
                });


    }

    private void showInviteNotifi(InviteMessage msg, Bitmap mResource) {
        PackageManager mPackageManager = mAppContext.getPackageManager();
        String appname = (String) mPackageManager.getApplicationLabel(mAppContext.getApplicationInfo());
        String contenttitle = appname;
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mAppContext)
                .setSmallIcon(mAppContext.getApplicationInfo().icon)
                .setWhen(System.currentTimeMillis())
                .setContentTitle("验证消息")
//                .setContentInfo(msg.getFrom()+msg.getStatus().name())
                .setAutoCancel(true);
        mBuilder.setLargeIcon(mResource);
        if (msg.getStatus().equals(InviteMessage.InviteMesageStatus.BEAGREED)) {
            mBuilder.setContentText(msg.getFrom() + "同意了你的好友请求")
                    .setTicker(msg.getFrom() + "同意了你的好友请求");
            PendingIntent mPendingIntent = PendingIntent.getActivities(mAppContext, 0, makechatIntentStack(), PendingIntent.FLAG_CANCEL_CURRENT);
            mBuilder.setContentIntent(mPendingIntent);
        } else if (msg.getStatus().equals(InviteMessage.InviteMesageStatus.BEINVITEED)) {
            mBuilder.setContentText(msg.getFrom() + "请求添加你为好友")
                    .setTicker(msg.getFrom() + "请求添加你为好友");
            PendingIntent mPendingIntent = PendingIntent.getActivities(mAppContext, 0, makerequestIntentStack(), PendingIntent.FLAG_CANCEL_CURRENT);
            mBuilder.setContentIntent(mPendingIntent);
        }
        Notification mNotification = mBuilder.build();
        mNotificationManager.notify(foregroundNotifyID, mNotification);
    }

    /**
     * 跳转到好友请求的Intent
     *
     * @return
     */
    private Intent[] makerequestIntentStack() {
        Intent[] intents = new Intent[3];
        intents[0] = Intent.makeRestartActivityTask(new ComponentName(mAppContext, com.grandmagic.readingmate.activity.MainActivity.class));
        intents[1] = new Intent(mAppContext, com.grandmagic.readingmate.activity.FriendActivity.class);
        intents[2] = new Intent(mAppContext, com.grandmagic.readingmate.activity.FriendRequestActivity.class);
        return intents;
    }

    /**
     * 跳转到聊天相关的Intent
     *
     * @return
     */
    private Intent[] makechatIntentStack() {
        Intent[] intents = new Intent[2];
        intents[0] = Intent.makeRestartActivityTask(new ComponentName(mAppContext, com.grandmagic.readingmate.activity.MainActivity.class));
        intents[1] = new Intent(mAppContext, com.grandmagic.readingmate.activity.EaseChatActivity.class);
        return intents;
    }
}
