package com.grandmagic.readingmate.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.grandmagic.readingmate.R;
import com.grandmagic.readingmate.activity.CommentsActivity;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * Created by zhangmengqi on 2017/3/8.
 */

public class MyCommentAdapter extends CommonAdapter<String> {



    public MyCommentAdapter(Context context, List datas) {
        super(context, R.layout.item_my_comments, datas);
    }

    @Override
    protected void convert(ViewHolder holder, String s, int position) {
        holder.setText(R.id.content, s);
        holder.setOnClickListener(R.id.ll_item_my_comment, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, CommentsActivity.class);
                //到时候肯定要把评论id带过去
                mContext.startActivity(intent);
            }
        });
    }

}
