package com.grandmagic.readingmate.adapter;

import android.content.Context;

import com.grandmagic.readingmate.R;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * Created by lps on 2017/2/28.
 */

public class CommentsAdapter extends CommonAdapter<String> {
    public CommentsAdapter(Context context, List datas) {
        super(context, R.layout.item_simpletext, datas);
    }

    @Override
    protected void convert(ViewHolder holder, String mS, int position) {

    }
}
