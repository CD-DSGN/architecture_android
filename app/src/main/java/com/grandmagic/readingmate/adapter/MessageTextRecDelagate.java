package com.grandmagic.readingmate.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.grandmagic.readingmate.R;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * Created by lps on 2017/2/22.
 */
public class MessageTextRecDelagate extends ChatItemViewDelegate {



    public MessageTextRecDelagate(Context mContext) {
        super(mContext);
    }

    @Override
    public boolean isForViewType(EMMessage item) {
        return item.getType() == EMMessage.Type.TXT&&mDirect == EMMessage.Direct.RECEIVE;
    }

    @Override
    protected void childConvert(ViewHolder mHolder, EMMessage mChatMessage, int mPosition) {
        EMTextMessageBody mBody = (EMTextMessageBody) mChatMessage.getBody();
        mHolder.setText(R.id.content, mBody.getMessage());
    }

    @Override
    protected View setContentView() {
        return LayoutInflater.from(mContext).inflate(  R.layout.item_recivetextmsg ,
        null);
    }


}
