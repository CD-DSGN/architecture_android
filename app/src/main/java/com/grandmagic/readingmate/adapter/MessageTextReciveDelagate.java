package com.grandmagic.readingmate.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.grandmagic.readingmate.R;
import com.grandmagic.readingmate.bean.response.ChatMessage;
import com.grandmagic.readingmate.utils.GlideRoundTransform;
import com.grandmagic.readingmate.utils.ImageLoader;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMessageBody;
import com.hyphenate.chat.EMTextMessageBody;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * Created by lps on 2017/2/22.
 */
public class MessageTextReciveDelagate implements ItemViewDelegate<EMMessage> {

    private Context mContext;

    public MessageTextReciveDelagate(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_recivetextmsg;
    }

    @Override
    public boolean isForViewType(EMMessage item, int position) {
        EMMessage.Type mType = item.getType();
        EMMessage.Direct mDirect = item.direct();
        return mType == EMMessage.Type.TXT&& mDirect == EMMessage.Direct.RECEIVE;
    }

    @Override
    public void convert(ViewHolder holder, EMMessage mChatMessage, int position) {
        EMTextMessageBody mBody = (EMTextMessageBody) mChatMessage.getBody();
        holder.setText(R.id.content,mBody.getMessage());
    }


}
