package com.grandmagic.readingmate.adapter;

import android.content.Context;

import com.grandmagic.readingmate.R;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * Created by zhangmengqi on 2017/3/8.
 */

public class CommentDetailAdapter extends CommonAdapter<String> {



    public CommentDetailAdapter(Context context, List datas) {
        super(context, R.layout.item_replay, datas);
    }

    @Override
    protected void convert(ViewHolder holder, String s, int position) {
        holder.setText(R.id.tv_signature_reply, s);

    }
}
