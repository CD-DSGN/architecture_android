package com.grandmagic.readingmate.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.os.Handler;
import android.os.PowerManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.grandmagic.readingmate.R;
import com.grandmagic.readingmate.listener.VoicePlayClickListener;
import com.hyphenate.EMError;

/**
 * Created by lps  on 2017/3/21.
 * <p>
 * 录制语音的View
 */

public class VoiceRecordView extends RelativeLayout {
    protected Context mContext;
    protected Drawable[] micImages;
    protected VoiceRecoder mVoiceRecorder;

    protected PowerManager.WakeLock mWakeLock;
    protected ImageView micImage;
    protected TextView recordingHint;

    protected Handler micImageHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            // change image
            if (-1<msg.what&&msg.what<7)//保证数组不越界
            micImage.setImageDrawable(micImages[msg.what]);
        }
    };

    public VoiceRecordView(Context context) {
        this(context, null);
    }

    public VoiceRecordView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VoiceRecordView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    private void init() {
        mVoiceRecorder = new VoiceRecoder(micImageHandler);
        LayoutInflater.from(mContext).inflate(R.layout.view_recordvoice, this);
        micImage = (ImageView) findViewById(R.id.mic_image);
        recordingHint = (TextView) findViewById(R.id.recording_hint);
        // animation resources, used for recording
        micImages = new Drawable[]{getResources().getDrawable(R.drawable.ease_record_animate_01),
//                getResources().getDrawable(R.drawable.ease_record_animate_02),
                getResources().getDrawable(R.drawable.ease_record_animate_03),
//                getResources().getDrawable(R.drawable.ease_record_animate_04),
                getResources().getDrawable(R.drawable.ease_record_animate_05),
//                getResources().getDrawable(R.drawable.ease_record_animate_06),
                getResources().getDrawable(R.drawable.ease_record_animate_07),
//                getResources().getDrawable(R.drawable.ease_record_animate_08),
                getResources().getDrawable(R.drawable.ease_record_animate_09),
//                getResources().getDrawable(R.drawable.ease_record_animate_10),
//                getResources().getDrawable(R.drawable.ease_record_animate_11),
                getResources().getDrawable(R.drawable.ease_record_animate_12),
//                getResources().getDrawable(R.drawable.ease_record_animate_13),
                getResources().getDrawable(R.drawable.ease_record_animate_14),};

        mWakeLock = ((PowerManager) mContext.getSystemService(Context.POWER_SERVICE)).newWakeLock(
                PowerManager.SCREEN_DIM_WAKE_LOCK, "demo");
    }

    /**
     * 按下开始录音
     *
     * @param v
     * @param event
     * @param recorderCallback
     * @return
     */
    public boolean onPressToSpeakBtnTouch(View v, MotionEvent event, VoiceRecordCallBack recorderCallback) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                try {
                    if (VoicePlayClickListener.isPlaying)
                        VoicePlayClickListener.currentPlayListener.stopPlayVoice();
                    v.setPressed(true);
                    startRecording();
                } catch (Exception e) {
                    v.setPressed(false);
                }
                return true;
            case MotionEvent.ACTION_MOVE:
                if (event.getY() < 0) {
                    showReleaseToCancelHint();
                } else {
                    showMoveUpToCancelHint();
                }
                return true;
            case MotionEvent.ACTION_UP:
                v.setPressed(false);
                if (event.getY() < 0) {
                    // discard the recorded audio.
                    discardRecording();
                    if (recorderCallback!=null)recorderCallback.onVoiceCancle();
                } else {
                    // stop recording and send voice file
                    try {
                        int length = stopRecoding();
                        if (length > 0) {
                            if (recorderCallback != null) {
                                recorderCallback.onVoiceRecordComplete(getVoiceFilePath(), length);
                            }
                        } else if (length == EMError.FILE_INVALID) {
                            Toast.makeText(mContext, "无录音权限", Toast.LENGTH_SHORT).show();
                            recorderCallback.onVoiceCancle();
                        } else {
                            Toast.makeText(mContext, "录音时间太短", Toast.LENGTH_SHORT).show();
                            recorderCallback.onVoiceCancle();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(mContext, "发送失败，请检测服务器是否连接", Toast.LENGTH_SHORT).show();
                    }
                }
                return true;
            default:
                discardRecording();
                if (recorderCallback!=null)recorderCallback.onVoiceCancle();
                return false;
        }
    }

    private void startRecording() {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Toast.makeText(mContext, "SD卡不存在", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            mWakeLock.acquire();
            this.setVisibility(VISIBLE);
            recordingHint.setText("手指上滑，取消发送");
            recordingHint.setBackgroundColor(Color.TRANSPARENT);
            mVoiceRecorder.startRecord(mContext);
        } catch (Exception mE) {
            mE.printStackTrace();
            if (mWakeLock.isHeld()) mWakeLock.release();
            if (mVoiceRecorder != null) mVoiceRecorder.discardRecording();
            this.setVisibility(INVISIBLE);
            Toast.makeText(mContext, "录音失败，请重试！", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    private int stopRecoding() {
        this.setVisibility(View.INVISIBLE);
        if (mWakeLock.isHeld())
            mWakeLock.release();
        return mVoiceRecorder.stopRecoding();
    }

    private void showMoveUpToCancelHint() {
        recordingHint.setText("手指上滑，取消发送");
        recordingHint.setBackgroundColor(Color.TRANSPARENT);
    }

    private void showReleaseToCancelHint() {
        recordingHint.setText("松开手指，取消发送");
        recordingHint.setBackgroundColor(Color.parseColor("#55FF0000"));
    }

    private void discardRecording() {
        this.setVisibility(View.INVISIBLE);
        if (mWakeLock.isHeld())
            mWakeLock.release();
        try {
            // stop recording
            if (mVoiceRecorder.isRecording()) {
                mVoiceRecorder.discardRecording();
            }
        } catch (Exception e) {
        }
    }

    public String getVoiceFilePath() {
        return mVoiceRecorder.getVoiceFilePath();

    }

    public interface VoiceRecordCallBack {
        void onVoiceRecordComplete(String voiceFilePath, int voiceTimeLength);
        void onVoiceCancle();
    }
}
