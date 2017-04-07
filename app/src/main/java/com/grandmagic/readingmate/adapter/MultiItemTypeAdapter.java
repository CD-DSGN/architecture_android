package com.grandmagic.readingmate.adapter;

import android.content.Context;
import android.view.View;

import com.grandmagic.readingmate.utils.AutoUtils;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * Created by lps on 2017/2/21.
 */

public class MultiItemTypeAdapter extends com.zhy.adapter.recyclerview.MultiItemTypeAdapter {

    public MultiItemTypeAdapter(Context context, List datas) {
        super(context, datas);
    }

    @Override
    public void onViewHolderCreated(ViewHolder holder, View itemView) {
        AutoUtils.auto(itemView);

    }


    public void setData(List mMessageList) {
        this.mDatas=mMessageList;
        notifyDataSetChanged();
    }


}
