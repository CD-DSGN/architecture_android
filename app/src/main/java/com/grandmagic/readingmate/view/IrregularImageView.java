package com.grandmagic.readingmate.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.grandmagic.readingmate.R;

/**
 * Created by lps on 2017/2/13.
 */

public class IrregularImageView extends ImageView {
    private Context mContext;
    private Paint mPaint;
    Bitmap dst;
    private int mWidth;
    private int mHeight;

    public IrregularImageView(Context context) {
        this(context, null);
    }

    public IrregularImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IrregularImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mContext = context;
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec
        );
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (getDrawable() == null || dst == null) return;//如果没有设置图片就不往下绘制了

        int sc = canvas.saveLayer(0, 0, mWidth, mHeight, null, Canvas.MATRIX_SAVE_FLAG |
                Canvas.CLIP_SAVE_FLAG |
                Canvas.HAS_ALPHA_LAYER_SAVE_FLAG |
                Canvas.FULL_COLOR_LAYER_SAVE_FLAG |
                Canvas.CLIP_TO_LAYER_SAVE_FLAG);
//        super.onDraw(canvas);

        // 先画一个图片
        Bitmap mask = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.irregular);
        mask = calculateMask(mask, mWidth, mHeight);
//        dst=calculateMask(dst,mWidth,mHeight);
        canvas.drawBitmap(dst, 0, 0, mPaint);
        // 设置模式
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
        canvas.drawBitmap(mask, 0, 0, mPaint);
        // 还原混合模式
        mPaint.setXfermode(null);
// 还原画布
        canvas.restoreToCount(sc);
    }

    private Bitmap calculateMask(Bitmap mMask, int mWidth, int mHeight) {
        Matrix mMatrix = new Matrix();
        float scaleX = mWidth * 1.0f / (mMask.getWidth() * 1.0f);
        float scaleY = mHeight * 1.0f / (mMask.getHeight() * 1.0f);
        mMatrix.postScale(scaleX, scaleY);
        Bitmap mBitmap = Bitmap.createBitmap(mMask, 0, 0, mMask.getWidth(), mMask.getHeight(), mMatrix, true);
        return mBitmap;
    }

    /**
     * 以下四个函数都是
     * 复写ImageView的setImageXxx()方法
     * 注意这个函数先于构造函数调用之前调用
     *
     * @param bm
     */
    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
        dst = bm;
        setup();
    }


    @Override
    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);
        dst = getBitmapFromDrawable(drawable);
        System.out.println("setImageDrawable -- setup");
        setup();
    }

    @Override
    public void setImageResource(@DrawableRes int resId) {
        super.setImageResource(resId);
        dst = getBitmapFromDrawable(getDrawable());
        setup();
    }

    @Override
    public void setImageURI(Uri uri) {
        super.setImageURI(uri);
        dst = getBitmapFromDrawable(getDrawable());
        setup();
    }

    /**
     * Drawable转Bitmap
     *
     * @param drawable
     * @return
     */
    private Bitmap getBitmapFromDrawable(Drawable drawable) {
        if (drawable == null) {
            return null;
        }

        if (drawable instanceof BitmapDrawable) {
            //通常来说 我们的代码就是执行到这里就返回了。返回的就是我们最原始的bitmap
            return ((BitmapDrawable) drawable).getBitmap();
        }
        if (drawable instanceof TransitionDrawable) {
            GlideBitmapDrawable mDrawableByLayerId = (GlideBitmapDrawable) ((TransitionDrawable) drawable).findDrawableByLayerId(((TransitionDrawable) drawable).getId(1));
            return mDrawableByLayerId.getBitmap();
        }
        if (drawable instanceof GlideBitmapDrawable) {
            return ((GlideBitmapDrawable) drawable).getBitmap();
        }

        return null;
    }

    private void setup() {
        if (dst == null) return;
        invalidate();
    }
}
