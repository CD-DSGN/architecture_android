package com.grandmagic.readingmate.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.grandmagic.readingmate.R;
import com.grandmagic.readingmate.bean.response.ChatMessage;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * Created by lps on 2017/2/22.
 */
public class MessageTextReciveDelagate implements ItemViewDelegate<ChatMessage> {

    private Context mContext;

    public MessageTextReciveDelagate(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_recivetextmsg;
    }

    @Override
    public boolean isForViewType(ChatMessage item, int position) {
        return item.getType() == ChatMessage.TYPE.RECICVER;
    }

    @Override
    public void convert(ViewHolder holder, ChatMessage mChatMessage, int position) {
        Glide.with(mContext).load(mChatMessage.getAvatar()).into((ImageView) holder.getView(R.id.avatar));
        holder.setText(R.id.content, mChatMessage.getMsg());
    }


}
