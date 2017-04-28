package com.hyphenate.easeui.widget.chatrow;

import com.bumptech.glide.Glide;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMessage.ChatType;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.easeui.R;
import com.hyphenate.easeui.utils.EaseSmileUtils;
import com.hyphenate.easeui.utils.GlideRoundTransform;
import com.hyphenate.exceptions.HyphenateException;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.BufferType;

public class EaseChatRowText extends EaseChatRow {

    private TextView contentView, name, signature;
    private ImageView avatar;
    private RelativeLayout relaCard;

    public EaseChatRowText(Context context, EMMessage message, int position, BaseAdapter adapter) {
        super(context, message, position, adapter);
    }

    @Override
    protected void onInflateView() {
        if ("card".equals(message.getStringAttribute("type", ""))) {
            inflater.inflate(message.direct() == EMMessage.Direct.RECEIVE ?
                    R.layout.item_recivecardmsg : R.layout.item_sendcardmsg, this);
            return;
        }
        inflater.inflate(message.direct() == EMMessage.Direct.RECEIVE ?
                R.layout.ease_row_received_message : R.layout.ease_row_sent_message, this);
    }

    @Override
    protected void onFindViewById() {
        if ("card".equals(message.getStringAttribute("type", ""))) {//名片类型消息
            signature = (TextView) findViewById(R.id.cardsignature);
            name = (TextView) findViewById(R.id.cardname);
            avatar = (ImageView) findViewById(R.id.cardavatar);
            relaCard = (RelativeLayout) findViewById(R.id.relacard);
        } else {
            contentView = (TextView) findViewById(R.id.tv_chatcontent);

        }

    }

    @Override
    public void onSetUpView() {

        EMTextMessageBody txtBody = (EMTextMessageBody) message.getBody();
        if ("card".equals(message.getStringAttribute("type", ""))) {//名片类型消息
            try {
                signature.setText(message.getStringAttribute("signature"));
                name.setText(message.getStringAttribute("nickname"));
                Glide.with(context).load(message.getStringAttribute("avatar")).transform(new GlideRoundTransform(context)).into(avatar);
            } catch (HyphenateException mE) {
                mE.printStackTrace();
            }
        } else {
            Spannable span = EaseSmileUtils.getSmiledText(context, txtBody.getMessage());
            // 设置内容
            contentView.setText(span, BufferType.SPANNABLE);

        }
        handleTextMessage();
    }

    protected void handleTextMessage() {
        if (message.direct() == EMMessage.Direct.SEND) {
            setMessageSendCallback();
            switch (message.status()) {
                case CREATE:
                    progressBar.setVisibility(View.GONE);
                    statusView.setVisibility(View.VISIBLE);
                    break;
                case SUCCESS:
                    progressBar.setVisibility(View.GONE);
                    statusView.setVisibility(View.GONE);
                    break;
                case FAIL:
                    progressBar.setVisibility(View.GONE);
                    statusView.setVisibility(View.VISIBLE);
                    break;
                case INPROGRESS:
                    progressBar.setVisibility(View.VISIBLE);
                    statusView.setVisibility(View.GONE);
                    break;
                default:
                    break;
            }
        } else {
            if (!message.isAcked() && message.getChatType() == ChatType.Chat) {
                try {
                    EMClient.getInstance().chatManager().ackMessageRead(message.getFrom(), message.getMsgId());
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void onUpdateView() {
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onBubbleClick() {
        // TODO Auto-generated method stub
    }


}
