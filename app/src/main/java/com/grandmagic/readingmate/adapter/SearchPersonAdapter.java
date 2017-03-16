package com.grandmagic.readingmate.adapter;

import android.content.Context;

import com.grandmagic.readingmate.R;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * Created by dangxiaohui on 2017/3/16.
 */

public class SearchPersonAdapter  extends CommonAdapter<String>{
    public SearchPersonAdapter(Context context, List datas) {
        super(context, R.layout.item_searchperson, datas);
    }

    @Override
    protected void convert(ViewHolder holder, String mS, int position) {

    }
}
