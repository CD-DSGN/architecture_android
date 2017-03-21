package com.grandmagic.readingmate.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.grandmagic.readingmate.R;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMVoiceMessageBody;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * Created by lps on 2017/2/22.
 */
public class MessageVoiceRecDelagate extends ChatItemViewDelegate {



    public MessageVoiceRecDelagate(Context mContext) {
        super(mContext);
    }

    @Override
    public boolean isForViewType(EMMessage item) {
        return item.getType() == EMMessage.Type.VOICE&&mDirect == EMMessage.Direct.RECEIVE;
    }

    @Override
    protected void childConvert(ViewHolder mHolder, EMMessage mChatMessage, int mPosition) {
        EMVoiceMessageBody mBody = (EMVoiceMessageBody) mChatMessage.getBody();
        mHolder.setText(R.id.time, mBody.getLength()+"");
    }

    @Override
    protected View setContentView() {
        return LayoutInflater.from(mContext).inflate(  R.layout.item_recivevoicemsg,
        null);
    }


}
