package com.grandmagic.readingmate.activity;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.zxing.Result;
import com.grandmagic.readingmate.R;
import com.grandmagic.readingmate.base.AppBaseResponseCallBack;
import com.grandmagic.readingmate.bean.response.ScanBookResponse;
import com.grandmagic.readingmate.consts.ApiErrorConsts;
import com.grandmagic.readingmate.event.BookStateEvent;
import com.grandmagic.readingmate.model.BookModel;
import com.grandmagic.readingmate.utils.AutoUtils;
import com.tamic.novate.NovateResponse;
import com.tamic.novate.Throwable;
import com.xys.libzxing.zxing.camera.CameraManager;
import com.xys.libzxing.zxing.decode.DecodeThread;
import com.xys.libzxing.zxing.utils.BeepManager;
import com.xys.libzxing.zxing.utils.CaptureActivityHandler;
import com.xys.libzxing.zxing.utils.InactivityTimer;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Field;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CaptureActivity extends com.xys.libzxing.zxing.activity.CaptureActivity implements SurfaceHolder.Callback {

    private static final String TAG = CaptureActivity.class.getSimpleName();
    @BindView(R.id.capture_preview)
    SurfaceView mCapturePreview;
    @BindView(R.id.capture_scan_line)
    ImageView mCaptureScanLine;
    @BindView(R.id.capture_crop_view)
    RelativeLayout mCaptureCropView;
    @BindView(R.id.capture_container)
    RelativeLayout mCaptureContainer;
    @BindView(R.id.tv_error_msg)
    TextView mTvErrorMsg;
    @BindView(R.id.tv_scan_again)
    TextView mTvScanAgain;
    @BindView(R.id.rl_capture_hint)
    RelativeLayout mRlCaptureHint;
    @BindView(R.id.activity_capture)
    RelativeLayout mActivityCapture;

    @BindView(R.id.hint1)
    TextView mHint1;
    @BindView(R.id.hint2)
    TextView mHint2;

    private CameraManager cameraManager;
    private CaptureActivityHandler handler;
    private InactivityTimer inactivityTimer;
    private BeepManager beepManager;
    TranslateAnimation animation;
    private Rect mCropRect = null;
    private boolean isHasSurface = false;

    public Handler getHandler() {
        return handler;
    }

    public CameraManager getCameraManager() {
        return cameraManager;
    }

    /**
     * 状态栏透明
     *
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_capture1);
        ButterKnife.bind(this);
        AutoUtils.auto(this);
        setTranslucentStatus(true);//状态栏透明（APi19+）
        inactivityTimer = new InactivityTimer(this);
        beepManager = new BeepManager(this);
        animation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.0f, Animation
                .RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT,
                0.9f);
        animation.setDuration(4500);
        animation.setRepeatCount(-1);
        animation.setRepeatMode(Animation.RESTART);
        mCaptureScanLine.startAnimation(animation);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // CameraManager must be initialized here, not in onCreate(). This is
        // necessary because we don't
        // want to open the camera driver and measure the screen size if we're
        // going to show the help on
        // first launch. That led to bugs where the scanning rectangle was the
        // wrong size and partially
        // off screen.
        startScan();
    }

    private void startScan() {
        cameraManager = new CameraManager(getApplication());

        handler = null;

        if (isHasSurface) {
            // The activity was paused but not stopped, so the surface still
            // exists. Therefore
            // surfaceCreated() won't be called, so init the camera here.
            initCamera(mCapturePreview.getHolder());
        } else {
            // Install the callback and wait for surfaceCreated() to init the
            // camera.
            mCapturePreview.getHolder().addCallback(this);
        }

        inactivityTimer.onResume();
    }

    @Override
    protected void onPause() {
        stopScan();
        super.onPause();
    }

    private void stopScan() {
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        inactivityTimer.onPause();
        beepManager.close();
        cameraManager.closeDriver();
        if (!isHasSurface) {
            mCapturePreview.getHolder().removeCallback(this);
        }
    }

    @Override
    protected void onDestroy() {
        inactivityTimer.shutdown();
        super.onDestroy();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (holder == null) {
            Log.e(TAG, "*** WARNING *** surfaceCreated() gave us a null surface!");
        }
        if (!isHasSurface) {
            isHasSurface = true;
            initCamera(holder);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        isHasSurface = false;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    /**
     * A valid barcode has been found, so give an indication of success and show
     * the results.
     *
     * @param rawResult The contents of the barcode.
     * @param bundle    The extras
     */
    BookModel mModel;

    public void handleDecode(final Result rawResult, final Bundle bundle) {
        inactivityTimer.onActivity();
        beepManager.playBeepSoundAndVibrate();
        mModel = new BookModel(this);
        mModel.fllowBook(rawResult.getText(), new AppBaseResponseCallBack<NovateResponse<ScanBookResponse>>(this) {
            @Override
            public void onSuccee(NovateResponse<ScanBookResponse> response) {
                Intent resultIntent = new Intent();
                bundle.putInt("width", mCropRect.width());
                bundle.putInt("height", mCropRect.height());
                bundle.putString("result", response.getData().getBook_id());
                resultIntent.putExtras(bundle);
                EventBus.getDefault().post(new BookStateEvent(BookStateEvent.STATE_MOVE,""));
                CaptureActivity.this.setResult(RESULT_OK, resultIntent);
                CaptureActivity.this.finish();
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                //停止动画

                if (e.getCode() == ApiErrorConsts.SCAN_NO_BOOK) {  //
                    mCaptureScanLine.clearAnimation();
                    //显示提示信息
                    mTvErrorMsg.setVisibility(View.VISIBLE);
                    mTvScanAgain.setVisibility(View.VISIBLE);
                    //隐藏扫描线
                    mCaptureScanLine.setVisibility(View.INVISIBLE);

                    mCaptureContainer.setBackgroundResource(R.drawable.bg_main_black);
                    mHint1.setVisibility(View.INVISIBLE);
                    mHint2.setVisibility(View.INVISIBLE);
                    stopScan();
                }else if(e.getCode() == ApiErrorConsts.SUBSCRIBE_ALREADY){  //这本书已经关注过了,跳转到图书详情页面,要改错误处理方式,// TODO: 2017/4/18
                    int book_id = -1;
                    try {
                        String json_str = e.getJson();   //原始json串
                        NovateResponse response = new Gson().fromJson(json_str, NovateResponse.class);
                        String data = (String) response.getData().toString();
                        JSONObject jsonObject = new JSONObject(data);
                        book_id = jsonObject.optInt("book_id");
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    } finally {
                        if (book_id == -1) {
                            restartPreviewAfterDelay(10);
                        }else{
                            Intent intent = new Intent();
                            intent.putExtra("result", book_id + "");
                            CaptureActivity.this.setResult(RESULT_OK, intent);
                            finish();
                        }
                    }
                }else{   //其他情况，toast简单提示一下，继续开始扫描
                    restartPreviewAfterDelay(10);
                }
            }
        });

    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        if (surfaceHolder == null) {
            throw new IllegalStateException("No SurfaceHolder provided");
        }
        if (cameraManager.isOpen()) {
            Log.w(TAG, "initCamera() while already open -- late SurfaceView callback?");
            return;
        }
        try {
            cameraManager.openDriver(surfaceHolder);
            // Creating the handler starts the preview, which can also throw a
            // RuntimeException.
            if (handler == null) {
                handler = new CaptureActivityHandler(this, cameraManager, DecodeThread.ALL_MODE);
            }

            initCrop();
        } catch (IOException ioe) {
            Log.w(TAG, ioe);
            displayFrameworkBugMessageAndExit();
        } catch (RuntimeException e) {
            // Barcode Scanner has seen crashes in the wild of this variety:
            // java.?lang.?RuntimeException: Fail to connect to camera service
            Log.w(TAG, "Unexpected error initializing camera", e);
            displayFrameworkBugMessageAndExit();
        }
    }

    private void displayFrameworkBugMessageAndExit() {
        // camera error
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(com.xys.libzxing.R.string.app_name));
        builder.setMessage("Camera error");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }

        });
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                finish();
            }
        });
        builder.show();
    }

    public void restartPreviewAfterDelay(long delayMS) {
        if (handler != null) {
            handler.sendEmptyMessageDelayed(com.xys.libzxing.R.id.restart_preview, delayMS);
        }
    }

    public Rect getCropRect() {
        return mCropRect;
    }

    /**
     * 初始化截取的矩形区域
     */
    private void initCrop() {
        int cameraWidth = cameraManager.getCameraResolution().y;
        int cameraHeight = cameraManager.getCameraResolution().x;

        /** 获取布局中扫描框的位置信息 */
        int[] location = new int[2];
        mCaptureCropView.getLocationInWindow(location);

        int cropLeft = location[0];
        int cropTop = location[1] - getStatusBarHeight();

        int cropWidth = mCaptureCropView.getWidth();
        int cropHeight = mCaptureCropView.getHeight();

        /** 获取布局容器的宽高 */
        int containerWidth = mCaptureContainer.getWidth();
        int containerHeight = mCaptureContainer.getHeight();

        /** 计算最终截取的矩形的左上角顶点x坐标 */
        int x = cropLeft * cameraWidth / containerWidth;
        /** 计算最终截取的矩形的左上角顶点y坐标 */
        int y = cropTop * cameraHeight / containerHeight;

        /** 计算最终截取的矩形的宽度 */
        int width = cropWidth * cameraWidth / containerWidth;
        /** 计算最终截取的矩形的高度 */
        int height = cropHeight * cameraHeight / containerHeight;

        /** 生成最终的截取的矩形 */
        mCropRect = new Rect(x, y, width + x, height + y);
    }

    private int getStatusBarHeight() {
        try {
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object obj = c.newInstance();
            Field field = c.getField("status_bar_height");
            int x = Integer.parseInt(field.get(obj).toString());
            return getResources().getDimensionPixelSize(x);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    @OnClick({R.id.tv_scan_again,R.id.iv_capture_left_arrow})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_scan_again:
                scanAgain();
                break;
            case R.id.iv_capture_left_arrow:
                finish();
                break;
        }
    }

    private void scanAgain() {
        mCaptureContainer.setBackgroundResource(0);
        mTvScanAgain.setVisibility(View.INVISIBLE);
        mTvErrorMsg.setVisibility(View.INVISIBLE);
        mHint1.setVisibility(View.VISIBLE);
        mHint2.setVisibility(View.VISIBLE);
        mCaptureScanLine.startAnimation(animation);
        mCaptureScanLine.setVisibility(View.VISIBLE);
        startScan();
        restartPreviewAfterDelay(10);
    }
}
