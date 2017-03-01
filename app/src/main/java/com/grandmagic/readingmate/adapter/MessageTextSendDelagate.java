package com.grandmagic.readingmate.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.grandmagic.readingmate.R;
import com.grandmagic.readingmate.bean.response.ChatMessage;
import com.grandmagic.readingmate.utils.GlideRoundTransform;
import com.grandmagic.readingmate.utils.ImageLoader;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * Created by lps on 2017/2/22.
 */
public class MessageTextSendDelagate implements ItemViewDelegate<ChatMessage> {

    private Context mContext;

    public MessageTextSendDelagate(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_sendtextmsg;
    }

    @Override
    public boolean isForViewType(ChatMessage item, int position) {
        return item.getType() == ChatMessage.TYPE.SEND
                &&item.getMessageType()== ChatMessage.MessageType.TEXT;
    }

    @Override
    public void convert(ViewHolder holder, ChatMessage mChatMessage, int position) {
        ImageLoader.loadRoundImage(mContext,mChatMessage.getAvatar(), (ImageView) holder.getView(R.id.avatar));
        holder.setText(R.id.content, mChatMessage.getMsg());
    }


}