package com.grandmagic.readingmate.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

/**
 * Created by lps on 2017/2/23.
 */

public class GlideRoundTransform extends BitmapTransformation {
    float radius;

    public GlideRoundTransform(Context context) {
        super(context);
        radius = 10f;
    }

    public GlideRoundTransform(Context context, float mRadius) {
        super(context);
        radius = mRadius;
    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        return roundCrop(pool, toTransform);
    }

    private Bitmap roundCrop(BitmapPool pool, Bitmap source) {
        if (source == null)
            return null;
        Bitmap result = pool.get(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
        if (result == null) {
            result = Bitmap.createBitmap(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
        }
        Canvas mCanvas = new Canvas(result);
        Paint mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setShader(new BitmapShader(source, BitmapShader.TileMode.CLAMP, Shader.TileMode.CLAMP));
        RectF mRectF = new RectF(0F, 0F, source.getWidth(), source.getWidth());
        mCanvas.drawRoundRect(mRectF, radius, radius, mPaint);
        return result;
    }

    @Override
    public String getId() {
        return getClass().getName()+Math.round(radius);
    }
}
