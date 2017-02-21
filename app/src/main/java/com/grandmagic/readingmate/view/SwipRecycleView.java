package com.grandmagic.readingmate.view;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

/**
 * Created by lps on 2017/2/21.http://www.jianshu.com/p/af9f940d8d1c
 */

public class SwipRecycleView extends RecyclerView {
    private float startX;
    private float startY;
    private int touchSlop;
    private boolean isChildHandle;
    private View touchView;
    private float distanceX;
    private float distanceY;

    public SwipRecycleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int action = ev.getActionMasked();
        if (action == MotionEvent.ACTION_DOWN) {
            isChildHandle = false;
            startX = ev.getX();
            startY = ev.getY();
            distanceX = 0;
            distanceY = 0;
            int position = pointToPosition((int) startX, (int) startY);
            touchView = getChildAt(position);
            if (hasChildOpen()) {
                if (touchView != null && touchView instanceof SwipeItemLayout && ((SwipeItemLayout) touchView).isOpen()) {
                    isChildHandle = true;
                } else {
                    closeAllSwipeItem();
                    return false;
                }
            }

        }

        return super.dispatchTouchEvent(ev);
    }

    //处理和侧滑菜单的冲突
    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        int action = e.getActionMasked();
        switch (action) {
            case MotionEvent.ACTION_MOVE:
                float endX = e.getX();
                float endY = e.getY();
                distanceX = Math.abs(endX - startX);
                distanceY = Math.abs(endY - startY);
                if (isChildHandle) return false;//如果子view已经获得事件
                if (distanceX > touchSlop && distanceX > distanceY) {
                    isChildHandle = true;
                    return false;
                }
                break;
            case MotionEvent.ACTION_UP:
                if (touchView != null && touchView instanceof SwipeItemLayout) {
                    SwipeItemLayout mSwipeItemLayout = (SwipeItemLayout) touchView;
                    if (mSwipeItemLayout.isOpen() && mSwipeItemLayout.getCurrentState() != 1) {
                        if (distanceX < touchSlop && distanceY < touchSlop) {
                            mSwipeItemLayout.close();
                        }
                    }
                }
                break;
        }
        return super.onInterceptTouchEvent(e);
    }

    private void closeAllSwipeItem() {
        int count = getChildCount();
        for (int i = count - 1; i >= 0; i--) {
            final View child = getChildAt(i);
            if (child != null && child instanceof SwipeItemLayout) {
                ((SwipeItemLayout) child).close();
            }
        }
    }

    /**
     * 当前手指位置的position，屏幕显示的第一个item为0
     *
     * @return
     */
    private Rect tounchFrame;

    private int pointToPosition(int x, int y) {
        Rect frame = tounchFrame;
        if (frame == null) {
            tounchFrame = new Rect();
            frame = tounchFrame;
        }
        int mChildCount = getChildCount();
        for (int i = 0; i < mChildCount; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() == VISIBLE) {
                child.getHitRect(frame);
                if (frame.contains(x, y)) return i;
            }
        }
        return -1;
    }

    private boolean hasChildOpen() {
        int mChildCount = getChildCount();
        for (int i = mChildCount - 1; i > -1; i--) {
            View mChildAt = getChildAt(i);
            if (mChildAt != null && mChildAt instanceof SwipeItemLayout&&((SwipeItemLayout)mChildAt).isOpen()) {
                return true;
            }
        }
        return false;
    }

}
