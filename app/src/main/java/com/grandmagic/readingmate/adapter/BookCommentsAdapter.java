package com.grandmagic.readingmate.adapter;

import android.content.Context;

import com.grandmagic.readingmate.R;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * Created by lps on 2017/3/14.
 */

public class BookCommentsAdapter extends CommonAdapter<String> {
    public BookCommentsAdapter(Context context,  List datas) {
        super(context, R.layout.item_bookcomments, datas);
    }

    @Override
    protected void convert(ViewHolder holder, String mS, int position) {

    }
}
