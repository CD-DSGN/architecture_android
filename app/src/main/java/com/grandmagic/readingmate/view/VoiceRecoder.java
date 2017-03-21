package com.grandmagic.readingmate.view;

import android.content.Context;
import android.media.MediaRecorder;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;

import com.hyphenate.EMError;

import java.io.File;
import java.io.IOException;
import java.util.Date;

/**
 * Created by lps on 2017/3/21.
 * 录音
 */

public class VoiceRecoder {
    private static final String TAG = "VoiceRecoder";
    MediaRecorder mRecorder;
    static final String PREFIX = "voice";
    static final String EXTENSION = ".amr";
    private boolean isRecording = false;//是否正在录音
    private long startTime;
    private String voiceFilePath = null;
    private String voiceFileName = null;
    private File mFile;
    private Handler mHandler;

    public VoiceRecoder(Handler mHandler) {
        this.mHandler = mHandler;
    }
    boolean mMkdirs;

    public String startRecord(Context mContext) {
        mFile = null;
        try {
            if (mRecorder != null) {
                mRecorder.release();
                mRecorder = null;
            }
            mRecorder = new MediaRecorder();
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mRecorder.setAudioChannels(1);
            mRecorder.setAudioSamplingRate(8000);
            mRecorder.setAudioEncodingBitRate(64);
            voiceFileName = PREFIX + System.currentTimeMillis() + EXTENSION;
            voiceFilePath =   mContext.getExternalCacheDir() + "/voice1/" ;
            mFile = new File(voiceFilePath+voiceFileName);
            if (!mFile.getParentFile().exists()){
                mMkdirs=mFile.getParentFile().mkdirs();
            }
            if (!mFile.exists()){
                mFile.createNewFile();
            }
            mRecorder.setOutputFile(mFile.getAbsolutePath());

            mRecorder.prepare();
            isRecording = true;
            mRecorder.start();
        } catch (IOException mE) {
            mE.printStackTrace();
            Log.e(TAG, "startRecord() called with: mContext = [" + mContext + "]" + mE.getMessage());
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (isRecording) {
                    int msgwhat = mRecorder.getMaxAmplitude() * 13 / 0x7FFF;
                    mHandler.hasMessages(msgwhat);
                    SystemClock.sleep(100);
                }
            }
        }).start();
        startTime = new Date().getTime();
        Log.e(TAG, "startRecord() called with: mfile = [" + mFile.getAbsolutePath() + "]");
        return mFile == null ? null : mFile.getAbsolutePath();
    }

    /**
     * 取消录音
     */
    public void discardRecording() {
        if (mRecorder != null) {
            try {
                mRecorder.stop();
                mRecorder.release();
                mRecorder = null;
                if (mFile != null && mFile.exists() && !mFile.isDirectory()) {
                    mFile.delete();
                }
            } catch (IllegalStateException mE) {
                Log.e(TAG, "discardRecording() called"+mE);
                mE.printStackTrace();
            }
            isRecording = false;
        }
    }

    public int stopRecoding() {
        if(mRecorder != null){
            isRecording = false;
            mRecorder.stop();
            mRecorder.release();
            mRecorder = null;

            if(mFile == null || !mFile.exists() || !mFile.isFile()){
                return EMError.FILE_INVALID;
            }
            if (mFile.length() == 0) {
                mFile.delete();
                return EMError.FILE_INVALID;
            }
            int seconds = (int) (new Date().getTime() - startTime) / 1000;
            Log.e(TAG, "voice recording finished. seconds:" + seconds + " file length:" + mFile.length());
            return seconds;
        }
        return 0;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        if (mRecorder!=null)mRecorder.release();
    }

    public boolean isRecording() {
        return isRecording;
    }

    public String getVoiceFilePath() {
        return mFile.getAbsolutePath();
    }

    public String getVoiceFileName() {
        return voiceFileName;
    }
}
