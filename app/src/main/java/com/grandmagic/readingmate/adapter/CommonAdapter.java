package com.grandmagic.readingmate.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.grandmagic.readingmate.utils.AutoUtils;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * Created by zhangmengqi on 2017/2/21.
 */

public abstract class CommonAdapter<T> extends com.zhy.adapter.recyclerview.CommonAdapter<T> {
    public CommonAdapter(Context context, int layoutId, List datas) {
        super(context, layoutId, datas);
    }

    @Override
    public void onViewHolderCreated(ViewHolder holder, View itemView) {
        AutoUtils.auto(itemView);
    }

    public void refreshData(List data) {
        mDatas = data;
        notifyDataSetChanged();
    }
}
