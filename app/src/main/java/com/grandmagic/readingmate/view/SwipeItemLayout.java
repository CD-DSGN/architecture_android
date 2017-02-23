package com.grandmagic.readingmate.view;

import android.content.Context;
import android.graphics.Rect;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by lps  on 2017/2/21.
 */

public class SwipeItemLayout extends FrameLayout{



    public SwipeItemLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mDragHelper=ViewDragHelper.create(this,rigthCallback);
    }



    private View mContentView;
    private View mMenuView;
    private  final ViewDragHelper mDragHelper;
    private boolean isOpen;
   private int currentState;

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mContentView=getChildAt(1);//侧滑菜单必须包含2个子view。一个为content，一个为右边的菜单
        mMenuView=getChildAt(0);//
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mDragHelper.processTouchEvent(event);
        return true;

    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (isOpen()&&contentClick(ev)){
            return true;//当侧滑出来状态的时候，拦截contentview的点击事件，进行关闭。
            // 防止点击直接跳到新界面。如果希望侧滑菜单展开的时候点击列表也能跳转，注释掉这里
        }
        return mDragHelper.shouldInterceptTouchEvent(ev);
    }

    private boolean contentClick(MotionEvent e) {
        int downX = (int) e.getX();
        int downY = (int) e.getY();
        Rect mRect=new Rect();
        mContentView.getHitRect(mRect);
        if (mRect.contains(downX,downY))return true;
        return false;
    }

    ViewDragHelper.Callback rigthCallback=new ViewDragHelper.Callback() {
        // 触摸到View的时候就会回调这个方法。
        // return true表示抓取这个View。
       @Override
       public boolean tryCaptureView(View child, int pointerId) {
           return mContentView==child;
       }

        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            return left>0?0:left<-mMenuView.getWidth()?-mMenuView.getWidth():left;
        }
//松开手的时候
        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
         if (isOpen){
             if (xvel>mMenuView.getWidth()||-mContentView.getWidth()<mMenuView.getWidth()/2){
                 close();
             }else {
                 open();
             }
         }else {
             if (-xvel > mMenuView.getWidth() || -mContentView.getLeft() > mMenuView.getWidth() / 2) {
                 open();
             } else {
                 close();
             }
         }

        }
        @Override
        public int getViewHorizontalDragRange(View child) {
            return 1;
        }

        @Override
        public int getViewVerticalDragRange(View child) {
            return 1;
        }

        @Override
        public void onViewDragStateChanged(int state) {
            super.onViewDragStateChanged(state);
            currentState = state;
        }
    };
    public void close() {
        mDragHelper.smoothSlideViewTo(mContentView, 0, 0);
        isOpen = false;
        invalidate();
    }

    public void open() {
        mDragHelper.smoothSlideViewTo(mContentView, -mMenuView.getWidth(), 0);
        isOpen = true;
        invalidate();
    }

    public boolean isOpen() {
        return isOpen;
    }

    public int getCurrentState() {
        return currentState;
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mDragHelper.continueSettling(true)) {
            invalidate();
        }
    }
    public void setOnClickListener(OnClickListener mListener){
        mContentView.setOnClickListener(mListener);
    }
}
