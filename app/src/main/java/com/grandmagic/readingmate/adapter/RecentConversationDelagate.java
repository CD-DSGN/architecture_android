package com.grandmagic.readingmate.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.grandmagic.readingmate.R;
import com.grandmagic.readingmate.activity.ChatActivity;
import com.grandmagic.readingmate.bean.response.RecentConversation;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * Created by lps on 2017/2/22.
 */

public class RecentConversationDelagate implements ItemViewDelegate<RecentConversation> {
    private Context mContext;

    public RecentConversationDelagate(Context mContext) {

        this.mContext = mContext;
    }

    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_recentconversation;
    }

    @Override
    public boolean isForViewType(RecentConversation item, int position) {
        return item.getType() == 0;
    }

    @Override
    public void convert(ViewHolder holder, final RecentConversation data, int position) {
        Glide.with(mContext).load(data.getAvatar()).into((ImageView) holder.getView(R.id.avatar));
        holder.setText(R.id.name, data.getName());
        holder.setText(R.id.content, data.getContent());
        holder.setText(R.id.time, data.getTime());
        holder.setOnClickListener(R.id.delete, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "delete", Toast.LENGTH_SHORT).show();
            }
        });
        holder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(mContext, ChatActivity.class);
                mIntent.putExtra("name", data.getName());
                mContext.startActivity(mIntent);

            }
        });
    }
}
