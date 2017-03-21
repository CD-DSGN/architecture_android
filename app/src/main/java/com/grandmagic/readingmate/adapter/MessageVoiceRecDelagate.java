package com.grandmagic.readingmate.adapter;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.grandmagic.readingmate.R;
import com.grandmagic.readingmate.listener.VoicePlayClickListener;
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
    protected void childConvert(ViewHolder mHolder, final EMMessage mChatMessage, int mPosition) {
        EMVoiceMessageBody mBody = (EMVoiceMessageBody) mChatMessage.getBody();
        mHolder.setText(R.id.time, mBody.getLength() + "");
        final ImageView mvoiceImageview = mHolder.getView(R.id.voice);
        if (VoicePlayClickListener.playMsgId!=null&&
                VoicePlayClickListener.playMsgId.equals(mChatMessage.getMsgId())
                &&VoicePlayClickListener.isPlaying){
            AnimationDrawable mAnimationDrawable;
            mvoiceImageview.setImageResource(R.drawable.voice_from_icon);
            mAnimationDrawable= (AnimationDrawable) mvoiceImageview.getDrawable();
            mAnimationDrawable.start();
        }
        else {
            mvoiceImageview.setImageResource(R.drawable.ease_chatfrom_voice_playing);
        }
        mvoiceImageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new VoicePlayClickListener(mChatMessage,mvoiceImageview,mContext);
            }
        });
    }

    @Override
    protected View setContentView() {
        return LayoutInflater.from(mContext).inflate(  R.layout.item_recivevoicemsg,
        null);
    }


}
