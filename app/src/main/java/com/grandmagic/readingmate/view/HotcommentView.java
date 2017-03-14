package com.grandmagic.readingmate.view;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.grandmagic.readingmate.R;
import com.grandmagic.readingmate.adapter.BookCommentsAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

/**
 * Created by dangxiaohui on 2017/3/14.
 */

public class HotcommentView extends FrameLayout {

    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerview;
    @BindView(R.id.refreshLayout)
    BGARefreshLayout mRefreshLayout;
    private Context mContext;

    public HotcommentView(Context context) {
        super(context);
        this.mContext = context;
        initPagerview();
    }

    public HotcommentView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initPagerview();
    }


    BookCommentsAdapter mAdapter;
    List<String> mList;

    private void initPagerview() {
        View mInflate = LayoutInflater.from(mContext).inflate(R.layout.view_hotcomment, this);
        ButterKnife.bind(this,mInflate);
        mRecyclerview.setLayoutManager(new LinearLayoutManager(mContext));
        mList = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            mList.add("asf");
        }
        mAdapter = new BookCommentsAdapter(mContext, mList);
        mRecyclerview.setAdapter(mAdapter);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
