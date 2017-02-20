package com.grandmagic.readingmate.adapter;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.grandmagic.readingmate.R;
import com.grandmagic.readingmate.rvanimation.AnimationAdapter;

import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * Created by lps  on 2017/2/16.
 */

public class HomeBookAdapter extends AnimationAdapter<String> {

    public HomeBookAdapter(Context context, List<String> datas, RecyclerView mRecyclerView) {
        super(context, R.layout.item_homepage, datas, mRecyclerView);
        setUseanimation(false);
    }

    @Override
    protected void convert(ViewHolder holder, String mS, final int position) {
        Glide.with(mContext).load("https://ss1.baidu.com/6ONXsjip0QIZ8tyhnq/it/u=1937046046,1905495319&fm=58")
                .into((ImageView) holder.getView(R.id.iv_conver));
        Glide.with(mContext).load("https://ss2.baidu.com/6ONYsjip0QIZ8tyhnq/it/u=1460995985,2991423940&fm=58")
                .into((ImageView) holder.getView(R.id.avatar));
        holder.setOnClickListener(R.id.linear_share, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mClickListener != null) {
                    mClickListener.bookShare(position);
                }
            }
        });
        holder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mClickListener != null) {
                    mClickListener.onItemClickListener(position);
                }
            }
        });
    }

    public String TRANSLATION_X = "translationX";
    public String TRANSLATION_Y = "translationY";
    public String SCALE_X = "scaleX";
    public String SCALE_Y = "scaleY";

    //其他特效暂时不用

    public Animator[] getAnimators(@NonNull View view) {
//        return new Animator[]{ObjectAnimator.ofFloat(view, "translationX", mRecyclerView.getLayoutManager().getWidth()/2, 0)};

        return new Animator[]{};
//       return new Animator[]{ObjectAnimator.ofFloat(view, TRANSLATION_X, 0 - mRecyclerView.getLayoutManager().getWidth(), 0)};
//        return new Animator[]{ObjectAnimator.ofFloat(view, TRANSLATION_Y, mRecyclerView.getMeasuredHeight() >> 1, 0)};
//
//        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, SCALE_X, 0.5f, 1f);
//        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, SCALE_Y, 0.5f, 1f);
//        return new ObjectAnimator[]{scaleX, scaleY};

//        float mOriginalY = mRecyclerView.getLayoutManager().getDecoratedTop(view);
//        float mDeltaY = mRecyclerView.getHeight() - mOriginalY;
//
//        return new Animator[]{ObjectAnimator.ofFloat(view, TRANSLATION_Y, mDeltaY, 0)};


    }

    ClickListener mClickListener;

    public void setClickListener(ClickListener mClickListener) {
        this.mClickListener = mClickListener;
    }

    public interface ClickListener {
        void bookShare(int position);

        void onItemClickListener(int position);
    }
}
