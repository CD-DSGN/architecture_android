package com.grandmagic.readingmate.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.grandmagic.readingmate.R;

/**
 * Created by lps on 2017/3/28.
 *
 * @version 1
 * @see
 * @since 2017/3/28 13:58
 */


public class GuideView extends RelativeLayout{

    public GuideView(Context context) {
        super(context);
    }

    public GuideView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GuideView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initview(context);
    }

    private void initview(Context mContext) {
        View mInflate = View.inflate(mContext, R.layout.view_guide, null);
    }
}
