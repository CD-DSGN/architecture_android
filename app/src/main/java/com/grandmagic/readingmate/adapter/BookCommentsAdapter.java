package com.grandmagic.readingmate.adapter;

import android.content.Context;
import android.view.View;
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
    public BookCommentsAdapter(Context context, List datas) {
        super(context, R.layout.item_replay, datas);
    }

    @Override
    protected void convert(ViewHolder holder, final BookCommentResponse.CommentsBean data, final int position) {
        ImageLoader.loadCircleImage(mContext, Environment.BASEULR_PRODUCTION + data.getAvatar().getMid(), (ImageView) holder.getView(R.id.iv_replay_header));
        holder.setText(R.id.tv_nickname_reply, data.getUser_name());
        holder.setText(R.id.tv_signature_reply, data.getContents());
        holder.setText(R.id.like_num, data.getLike_times()+"");
        holder.setText(R.id.tv_time_replay, DateUtil.timeTodate(data.getPub_time()));
        holder.getView(R.id.ib_good).setSelected(data.getThumb_up().equals("1"));
        holder.setOnClickListener(R.id.lin_like, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null && !data.getThumb_up().equals("1")) {
                    mListener.thumb(data.getComment_id(), position);
                }
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
