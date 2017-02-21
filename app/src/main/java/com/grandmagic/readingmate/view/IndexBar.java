package com.grandmagic.readingmate.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.grandmagic.readingmate.R;
import com.grandmagic.readingmate.bean.response.DataBean;

import java.util.Arrays;
import java.util.List;

/**
 * Created by lps on 2017/1/11.
 */

public class IndexBar extends View {
    public static final int DEFAULT_TEXTSIZE = 14;//默认字体大小
    public static final String[] INDEX_STRING = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J",
            "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "#"};//字母
    int textSize;
    int pressBackground = Color.BLACK;//按下时候的颜色
    private List<String> indexDatas;//索引
    Paint mPaint;//画笔
    private TextView hintTextView;//滑动时候显示的文字
    private LinearLayoutManager layoutmanager;//recycle的LayoutManager
    private List<DataBean> souseData;//源数据

    public IndexBar(Context context) {
        this(context, null);
    }

    public IndexBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IndexBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.getTheme().obtainStyledAttributes(attrs, R.styleable.IndexBar, defStyleAttr, 0);

        textSize = array.getDimensionPixelSize(R.styleable.IndexBar_textSize,
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, DEFAULT_TEXTSIZE, getResources().getDisplayMetrics())
        );

        pressBackground = array.getColor(R.styleable.IndexBar_pressBackground, Color.BLACK);


        //获取自定义属性
        array.recycle();
        indexDatas = Arrays.asList(INDEX_STRING);
        mPaint = new Paint();
        mPaint.setTextSize(textSize);
        setListener(new OnIndexPressedListener() {//用户滑动的监听
            @Override
            public void onpressed(int position, String Letter) {
                hintTextView.setVisibility(View.VISIBLE);
                hintTextView.setText(Letter);
                if (layoutmanager != null) {
                    int pos = getPosByTag(Letter);
                    layoutmanager.scrollToPositionWithOffset(pos, 0);
                }
            }

            @Override
            public void up() {
                hintTextView.setVisibility(View.GONE);
            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int wmode = MeasureSpec.getMode(widthMeasureSpec);
        int wsize = MeasureSpec.getSize(widthMeasureSpec);
        int hmode = MeasureSpec.getMode(heightMeasureSpec);
        int hsize = MeasureSpec.getSize(heightMeasureSpec);

        int measurewidth = 0, measureheight = 0;
        Rect indexBounds = new Rect();
        String index;
        for (int i = 0; i < indexDatas.size(); i++) {
            index = indexDatas.get(i);
            mPaint.getTextBounds(index, 0, index.length(), indexBounds);
            measurewidth = Math.max(indexBounds.width(), measurewidth);
            measureheight = Math.max(indexBounds.height(), measureheight);
        }
        measureheight *= indexDatas.size();
        switch (wmode) {
            case MeasureSpec.EXACTLY:
                measurewidth = wsize;
                break;
            case MeasureSpec.AT_MOST:
                measurewidth = Math.min(measurewidth, wsize);
                break;
            case MeasureSpec.UNSPECIFIED:
                break;
        }
        switch (hmode) {
            case MeasureSpec.EXACTLY:
                measureheight = hsize;
                break;
            case MeasureSpec.AT_MOST:
                measureheight = Math.min(measureheight, hsize);
                break;
            case MeasureSpec.UNSPECIFIED:
                break;
        }
        setMeasuredDimension(measurewidth, measureheight);
    }

    int mWidth, mHeight, mGapHeight;//宽高，文字间隙

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        mGapHeight = (mHeight - getPaddingBottom() - getPaddingTop()) / indexDatas.size();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int t = getPaddingTop();
        String index;
        mPaint.setColor(Color.parseColor("#801cc9a3"));
        Rect indexBounds = new Rect();
        for (int i = 0; i < indexDatas.size(); i++) {
            index = indexDatas.get(i);
            mPaint.getTextBounds(index, 0, index.length(), indexBounds);
            Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
            int baseLine = (int) ((mGapHeight - fontMetrics.bottom - fontMetrics.top) / 2);
            canvas.drawText(index, mWidth / 2 - indexBounds.width() / 2, t + mGapHeight * i + baseLine, mPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                setBackgroundColor(pressBackground);
                break;
            case MotionEvent.ACTION_MOVE:
                float y = event.getY();
                int pressI = (int) ((y - getPaddingTop()) / mGapHeight);
                if (pressI < 0) {
                    pressI = 0;
                } else if (pressI > indexDatas.size() - 1) {
                    pressI = indexDatas.size() - 1;
                }
                if (listener != null) {
                    listener.onpressed(pressI, indexDatas.get(pressI));
                }

                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
            default:
                setBackgroundResource(android.R.color.transparent);
                if (listener != null) {
                    listener.up();
                }
                break;
        }
        return true;
    }

    public IndexBar setHintTextView(TextView hintTextView) {
        this.hintTextView = hintTextView;
        return this;
    }

    public IndexBar setLayoutmanager(LinearLayoutManager layoutmanager) {
        this.layoutmanager = layoutmanager;
        return this;
    }

    public IndexBar setSouseData(List<DataBean> souseData) {
        this.souseData = souseData;
        return this;
    }

    private int getPosByTag(String letter) {
        if (TextUtils.isEmpty(letter)) {
            return -1;
        }
        for (int i = 0; i < souseData.size(); i++) {
            if (letter.equals(souseData.get(i).getTag())) {
                return i;
            }
        }
        return -1;
    }

    public void setIndexDatas(List<String> indexDatas) {
        this.indexDatas = indexDatas;
    }

    public interface OnIndexPressedListener {
        void onpressed(int position, String Letter);

        void up();
    }

    OnIndexPressedListener listener;


    public void setListener(OnIndexPressedListener listener) {
        this.listener = listener;
    }
}
