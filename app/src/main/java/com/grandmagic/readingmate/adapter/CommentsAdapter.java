package com.grandmagic.readingmate.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import com.grandmagic.readingmate.R;
import com.grandmagic.readingmate.activity.CommentsActivity;
import com.grandmagic.readingmate.bean.response.PersonCommentResponse;
import com.grandmagic.readingmate.utils.DateUtil;
import com.grandmagic.readingmate.utils.ImageLoader;
import com.grandmagic.readingmate.utils.KitUtils;
import com.tamic.novate.util.Environment;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * Created by lps on 2017/2/28.
 */

public class CommentsAdapter extends CommonAdapter<PersonCommentResponse.CommentInfoBean> {
    private PersonCommentResponse.AvatarUrlBean mAvatar;
    private String mUsername;

    public CommentsAdapter(Context context, List datas) {
        super(context, R.layout.item_comments, datas);
    }

    @Override
    protected void convert(ViewHolder holder, final PersonCommentResponse.CommentInfoBean data, int position) {
        ImageLoader.loadRoundImage(mContext, Environment.BASEULR_PRODUCTION + this.mAvatar.getMid(), (ImageView) holder.getView(R.id.avatar));
        ImageLoader.loadRoundImage(mContext, Environment.BASEULR_PRODUCTION + data.getPhoto(), (ImageView) holder.getView(R.id.cover));
        holder.setText(R.id.nickname, KitUtils.unicodeDecode(this.mUsername));
        holder.setText(R.id.bookname,data.getBook_name());
        holder.setText(R.id.time, DateUtil.timeTodate(data.getPub_time()));
        holder.setText(R.id.replynum, data.getReply_count() + "回复");
        holder.setText(R.id.content,KitUtils.unicodeDecode( data.getContent()));
        holder.setText(R.id.like_num, data.getLike_times());
        holder.setTextColor(R.id.like_num,data.getIs_thumb()==1?R.color.text_green:R.color.text_gray);
        holder.setImageResource(R.id.iv_like,data.getIs_thumb()==1?R.drawable.iv_like:R.drawable.iv_like_gray);
        holder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(mContext, CommentsActivity.class);
                mIntent.putExtra(CommentsActivity.COMMENT_ID,data.getComment_id());
                mContext.startActivity(mIntent);
            }
        });
    }

    public void setAvatar(PersonCommentResponse.AvatarUrlBean mAvatar) {
        this.mAvatar = mAvatar;
    }

    public void setusername(String mUsername) {
        this.mUsername = mUsername;
    }
}
