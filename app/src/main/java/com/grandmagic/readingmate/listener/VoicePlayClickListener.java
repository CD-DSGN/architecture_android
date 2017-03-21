package com.grandmagic.readingmate.listener;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.grandmagic.readingmate.R;
import com.grandmagic.readingmate.utils.IMHelper;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMVideoMessageBody;
import com.hyphenate.chat.EMVoiceMessageBody;
import com.hyphenate.util.EMLog;

import java.io.File;

/**
 * Created by lps  on 2017/3/21.
 * 处理播放voice类型消息
 */

public class VoicePlayClickListener implements View.OnClickListener{

    private static final String TAG = "VoicePlayClickListener";
EMMessage mMessage;
EMVoiceMessageBody  mVideoMessageBody;
    private AnimationDrawable voiceAnimation = null;
    MediaPlayer mediaPlayer = null;
    ImageView iv_read_status;
    Context activity;
    private EMMessage.ChatType chatType;
    ImageView voiceIconView;
    public static boolean isPlaying = false;
    public static VoicePlayClickListener currentPlayListener = null;
    public static String playMsgId;

    public VoicePlayClickListener(EMMessage message, ImageView v,   Context context) {
        this.mMessage = message;
        mVideoMessageBody = (EMVoiceMessageBody) message.getBody();
        voiceIconView = v;
        this.activity = context;
        this.chatType = message.getChatType();
    }

    /**
     * 停止播放
     */
    public void stopPlayVoice() {
        voiceAnimation.stop();
        if (mMessage.direct() == EMMessage.Direct.RECEIVE) {
            voiceIconView.setImageResource(R.drawable.ease_chatfrom_voice_playing);
        } else {
            voiceIconView.setImageResource(R.drawable.ease_chatto_voice_playing);
        }
        // stop play voice
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        isPlaying = false;
        playMsgId = null;
//        adapter.notifyDataSetChanged();
    }

    public void playVoice(String filePath) {
        if (!(new File(filePath).exists())) {
            return;
        }
        playMsgId = mMessage.getMsgId();
        AudioManager audioManager = (AudioManager) activity.getSystemService(Context.AUDIO_SERVICE);

        mediaPlayer = new MediaPlayer();
        if (IMHelper.getInstance().getSettingsProvider().isSpeakerOpened()) {
            audioManager.setMode(AudioManager.MODE_NORMAL);
            audioManager.setSpeakerphoneOn(true);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_RING);
        } else {
            audioManager.setSpeakerphoneOn(false);// 关闭扬声器
            // 把声音设定成Earpiece（听筒）出来，设定为正在通话中
            audioManager.setMode(AudioManager.MODE_IN_CALL);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_VOICE_CALL);
        }
        try {
            mediaPlayer.setDataSource(filePath);
            mediaPlayer.prepare();
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                @Override
                public void onCompletion(MediaPlayer mp) {
                    // TODO Auto-generated method stub
                    mediaPlayer.release();
                    mediaPlayer = null;
                    stopPlayVoice(); // stop animation
                }

            });
            isPlaying = true;
            currentPlayListener = this;
            mediaPlayer.start();
            showAnimation();

            // 如果是接收的消息
            if (mMessage.direct() == EMMessage.Direct.RECEIVE) {
                if (!mMessage.isAcked() && chatType == EMMessage.ChatType.Chat) {
                    // 告知对方已读这条消息
                    EMClient.getInstance().chatManager().ackMessageRead(mMessage.getFrom(), mMessage.getMsgId());
                }
                if (!mMessage.isListened() && iv_read_status != null && iv_read_status.getVisibility() == View.VISIBLE) {
                    // 隐藏自己未播放这条语音消息的标志
                    iv_read_status.setVisibility(View.INVISIBLE);
                    mMessage.setListened(true);
                    EMClient.getInstance().chatManager().setVoiceMessageListened(mMessage);
                }

            }

        } catch (Exception e) {
            Log.e(TAG, "playVoice: "+e );
        }
    }

    private void showAnimation() {
        // play voice, and start animation
        if (mMessage.direct() == EMMessage.Direct.RECEIVE) {
            voiceIconView.setImageResource(R.drawable.voice_from_icon);
        } else {
            voiceIconView.setImageResource(R.drawable.voice_to_icon);
        }
        voiceAnimation = (AnimationDrawable) voiceIconView.getDrawable();
        voiceAnimation.start();
    }

    @Override
    public void onClick(View v) {
        String st = "正在下载语音，稍后点击";
        if (isPlaying) {
            if (playMsgId != null && playMsgId.equals(mMessage.getMsgId())) {
                currentPlayListener.stopPlayVoice();
                return;
            }
            currentPlayListener.stopPlayVoice();
        }

        if (mMessage.direct() == EMMessage.Direct.SEND) {
            // for sent msg, we will try to play the voice file directly
            playVoice(mVideoMessageBody.getLocalUrl());
        } else {
            if (mMessage.status() == EMMessage.Status.SUCCESS) {
                File file = new File(mVideoMessageBody.getLocalUrl());
                if (file.exists() && file.isFile())
                    playVoice(mVideoMessageBody.getLocalUrl());
                else
                    EMLog.e(TAG, "file not exist");

            } else if (mMessage.status() == EMMessage.Status.INPROGRESS) {
                Toast.makeText(activity, st, Toast.LENGTH_SHORT).show();
            } else if (mMessage.status() == EMMessage.Status.FAIL) {
                Toast.makeText(activity, st, Toast.LENGTH_SHORT).show();
                new AsyncTask<Void, Void, Void>() {

                    @Override
                    protected Void doInBackground(Void... params) {
                        EMClient.getInstance().chatManager().downloadAttachment(mMessage);
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void result) {
                        super.onPostExecute(result);
                        // TODO: 2017/3/21 处理读了消息的UI更新
//                        adapter.notifyDataSetChanged();
                    }

                }.execute();

            }

        }
    }
}
