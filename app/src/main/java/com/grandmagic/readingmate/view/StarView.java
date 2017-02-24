package com.grandmagic.readingmate.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.grandmagic.readingmate.R;

/**
 * Created by lps on 2017/2/23. 星形评分控件
 * <p>
 * 参照http://blog.csdn.net/a756213932/article/details/51939422
 */

public class StarView extends View {
    private Paint mPaint;
    private int starDistance;//间距
    private int starCount;
    private Drawable stareEmptyDrawable;//暗星星
    private Bitmap starFillBitmap;//填充星星
    private float starMask;//星星的个数（可以为小数）
    private int score;
    private int total;//默认10分（步进为0.5）
    private int starwidth, starheight;
    private boolean isIndicator;//是否只作为指示，不可滑动

    public StarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }


    public StarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context mContext, AttributeSet mAttrs) {
        TypedArray mTypedArray = mContext.obtainStyledAttributes(mAttrs, R.styleable.StarView);
        this.starDistance = (int) mTypedArray.getDimension(R.styleable.StarView_starDistance, 10);
        this.starwidth = (int) mTypedArray.getDimension(R.styleable.StarView_starWidth, 40);
        this.starheight = (int) mTypedArray.getDimension(R.styleable.StarView_starHeight, 40);
        this.starCount = (int) mTypedArray.getDimension(R.styleable.StarView_starCount, 5);
        this.stareEmptyDrawable = mTypedArray.getDrawable(R.styleable.StarView_starEmpty);
        this.starFillBitmap = drawableToBitmap(mTypedArray.getDrawable(R.styleable.StarView_starFill));
        this.isIndicator = mTypedArray.getBoolean(R.styleable.StarView_isIndicator, false);
        this.score = mTypedArray.getInt(R.styleable.StarView_score, 5);
        this.total = mTypedArray.getInt(R.styleable.StarView_totalscore, 10);
        mTypedArray.recycle();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setShader(new BitmapShader(starFillBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(starwidth * starCount + (starCount - 1) * starDistance, starheight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        starMask = score * starCount * 1.0f / total;

        if (stareEmptyDrawable == null || starFillBitmap == null) {
            throw new NullPointerException("stareEmptyDrawable或者starFillBitmap不能为空");
        }
        for (int i = 0; i < starCount; i++) {
            int left = starDistance * i + starwidth * i;
            stareEmptyDrawable.setBounds(left, 0, left + starwidth, starheight);
            stareEmptyDrawable.draw(canvas);
        }
        if (starMask > 1) {
            canvas.drawRect(0, 0, starwidth, starheight, mPaint);
            if (starMask - (int) starMask == 0) {//整数分数
                for (int i = 1; i < starMask; i++) {
                    canvas.translate(starDistance + starwidth, 0);
                    canvas.drawRect(0, 0, starwidth, starheight, mPaint);
                }

            } else {
                for (int i = 1; i < starMask - 1; i++) {//先绘制整数个数的星星
                    canvas.translate(starDistance + starwidth, 0);
                    canvas.drawRect(0, 0, starwidth, starheight, mPaint);
                }
                canvas.translate(starDistance + starwidth, 0);
                float a = Math.round((starMask - (int) starMask) * 10) * 1.0f / 10;
                canvas.drawRect(0, 0, starwidth * a, starheight, mPaint);
            }
        } else {
            canvas.drawRect(0, 0, starwidth * starMask, starheight, mPaint);
        }


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isIndicator)
            return super.onTouchEvent(event);
        int x = (int) event.getX();
        if (x < 0) x = 0;
        if (x > getMeasuredWidth()) x = getMeasuredWidth();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                setStarMask((x * 1.0f / (getMeasuredWidth() * 1.0f / starCount)));
                return true;
            case MotionEvent.ACTION_MOVE:
                setStarMask(x * 1.0f / (getMeasuredWidth() * 1.0f / starCount));
                break;
        }

        return super.onTouchEvent(event);
    }

    private void setStarMask(float mStarMask) {
        score = (int) (mStarMask * total / starCount);
        if (mStarChangeListener != null) {
            mStarChangeListener.onChange(score);
        }
        invalidate();
    }

    /**
     * drawable 转bitmap
     *
     * @param mDrawable
     * @return
     */
    private Bitmap drawableToBitmap(Drawable mDrawable) {
        if (mDrawable == null) return null;
        Bitmap mBitmap = Bitmap.createBitmap(starwidth, starheight, Bitmap.Config.ARGB_8888);
        Canvas mCanvas = new Canvas(mBitmap);
        mDrawable.setBounds(0, 0, starwidth, starheight);
        mDrawable.draw(mCanvas);
        return mBitmap;
    }


    /**
     * 评分
     *
     * @return
     */
    public int getScore() {
        return score;
    }

    /**
     * 设置分数
     *
     * @param mScore
     */
    public void setScore(int mScore) {
        score = mScore;
        invalidate();
    }

    public int getTotal() {
        return total;
    }

    /**
     * 设置总分，默认为10
     *
     * @param mTotal
     */
    public void setTotal(int mTotal) {
        total = mTotal;
        invalidate();
    }

    /**
     * 星星数量
     *
     * @return
     */
    public int getStarCount() {
        return starCount;
    }

    public void setStarCount(int mStarCount) {
        starCount = mStarCount;
        invalidate();
    }

    /**
     * 监听评分变化
     */
    public interface OnStarChangeListener {
        void onChange(int score);
    }

    OnStarChangeListener mStarChangeListener;

    public void setStarChangeListener(OnStarChangeListener mStarChangeListener) {
        this.mStarChangeListener = mStarChangeListener;
    }
}
