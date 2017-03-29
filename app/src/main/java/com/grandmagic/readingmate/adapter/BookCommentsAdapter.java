package com.grandmagic.readingmate.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import com.grandmagic.readingmate.R;
import com.grandmagic.readingmate.activity.CommentsActivity;
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
    public BookCommentsAdapter(Context context, List datas) {
        super(context, R.layout.item_replay_book, datas);
    }

    @Override
    protected void convert(ViewHolder holder, final BookCommentResponse.CommentsBean data, final int position) {
        ImageLoader.loadCircleImage(mContext, Environment.BASEULR_PRODUCTION + data.getAvatar().getMid(), (ImageView) holder.getView(R.id.iv_replay_header));
        holder.setText(R.id.tv_nickname_reply, data.getUser_name());
        holder.setText(R.id.tv_signature_reply, data.getContents());
        holder.setText(R.id.like_num, data.getLike_times() + "");
        holder.setText(R.id.tv_time_replay, DateUtil.timeTodate(data.getPub_time()));
        holder.getView(R.id.ib_good).setSelected(data.getThumb_up().equals("1"));
        holder.setVisible(R.id.reply_count,!(data.getReply_count().equals("0")||data.getReply_count().isEmpty()));
        holder.setText(R.id.reply_count,data.getReply_count()+"条回复");
        holder.setOnClickListener(R.id.lin_like, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null && !data.getThumb_up().equals("1")) {
                    mListener.thumb(data.getComment_id(), position);
                }
            }
        });
        holder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(mContext, CommentsActivity.class);
                mIntent.putExtra(CommentsActivity.COMMENT_ID, data.getComment_id());
                mContext.startActivity(mIntent);
            }
        });
    }

    AdapterListener mListener;

    public void setListener(AdapterListener mListener) {
        this.mListener = mListener;
    }

    public interface AdapterListener {
        void thumb(String commentid, int position);
    }
}
