package com.grandmagic.readingmate.rvanimation;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.grandmagic.readingmate.rvanimation.AnimatorUtil;
import com.grandmagic.readingmate.rvanimation.ViewAnimator;
import com.grandmagic.readingmate.utils.AutoUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * Created by lps on 2017/2/16.
 * 带有动画效果的adapter
 */

public abstract class AnimationAdapter<T> extends CommonAdapter<T> {
   protected RecyclerView mRecyclerView;
    public AnimationAdapter(Context context, int layoutId, List<T> datas, RecyclerView mRecyclerView) {
        super(context, layoutId, datas);
        this.mRecyclerView=mRecyclerView;
    }
    @Override
    public void onViewHolderCreated(ViewHolder holder, View itemView) {
        AutoUtils.auto(itemView);
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        animateView(holder.getConvertView(),position);
    }

    private ViewAnimator mViewAnimator;
    private void animateView(final View view, final int position) {
        if (mRecyclerView==null)return;
        mViewAnimator=new ViewAnimator(mRecyclerView);
        Animator[] animators = getAnimators(view);
        Animator alphaAnimator = ObjectAnimator.ofFloat(view, "alpha", 0, 1);
        Animator[] concatAnimators = AnimatorUtil.concatAnimators(animators, alphaAnimator);
        mViewAnimator.animateViewIfNecessary(position, view, concatAnimators);
    }

    protected abstract Animator[] getAnimators(View mView);
}
