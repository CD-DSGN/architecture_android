package com.grandmagic.readingmate.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.grandmagic.readingmate.R;
import com.grandmagic.readingmate.bean.response.BookCommentResponse;
import com.grandmagic.readingmate.utils.DateUtil;
import com.grandmagic.readingmate.utils.ImageLoader;
import com.tamic.novate.util.Environment;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * Created by lps on 2017/3/14.
 */

public class BookCommentsAdapter extends CommonAdapter<BookCommentResponse.CommentsBean> {
    public BookCommentsAdapter(Context context,  List datas) {
        super(context, R.layout.item_replay, datas);
    }

    @Override
    protected void convert(ViewHolder holder, BookCommentResponse.CommentsBean data, int position) {
        ImageLoader.loadCircleImage(mContext, Environment.BASEULR_PRODUCTION+data.getAvatar().getMid(), (ImageView) holder.getView(R.id.iv_replay_header));
        holder.setText(R.id.tv_nickname_reply,data.getUser_name());
        holder.setText(R.id.tv_signature_reply,data.getContents());
        holder.setText(R.id.like_num,data.getLike_times());
        holder.setText(R.id.tv_time_replay, DateUtil.timeTodate(data.getPub_time()));


    }
}
