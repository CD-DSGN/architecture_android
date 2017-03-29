package com.grandmagic.readingmate.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.grandmagic.readingmate.R;
import com.grandmagic.readingmate.activity.FriendDetailActivity;
import com.grandmagic.readingmate.bean.response.PersonInfo;
import com.grandmagic.readingmate.utils.ImageLoader;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.exceptions.HyphenateException;
import com.tamic.novate.util.Environment;
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
        return item.getType() == EMMessage.Type.TXT && mDirect == EMMessage.Direct.RECEIVE;
    }

    @Override
    protected void childConvert(ViewHolder mHolder, final EMMessage mChatMessage, int mPosition) {
        EMTextMessageBody mBody = (EMTextMessageBody) mChatMessage.getBody();
        if ("card".equals(mChatMessage.getStringAttribute("type", ""))) {//名片类型消息
            try {
                cardMsgClick(mHolder, mChatMessage);
                ImageLoader.loadRoundImage(mContext, mChatMessage.getStringAttribute("avatar"), (ImageView) mHolder.getView(R.id.cardavatar));
                mHolder.setText(R.id.cardname, mChatMessage.getStringAttribute("nickname"));
                mHolder.setText(R.id.cardsignature, mChatMessage.getStringAttribute("signature"));
            }catch (HyphenateException mE) {
                mE.printStackTrace();
            }

        } else {//普通的文本消息
            mHolder.setText(R.id.content, mBody.getMessage());
        }
    }

    /**
     * 名片的点击事件
     * @param mHolder
     * @param mChatMessage
     */
    private void cardMsgClick(ViewHolder mHolder, final EMMessage mChatMessage) {
        mHolder.getConvertView().setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent mIntent = new Intent(mContext, FriendDetailActivity.class);
                        Bundle mBundle = new Bundle();
                        PersonInfo mPersonInfo = new PersonInfo();
                        try {
                            mPersonInfo.setAvatar(mChatMessage.getStringAttribute("avatar"));
                            mPersonInfo.setNickname(mChatMessage.getStringAttribute("nickname"));
                            mPersonInfo.setClientid(mChatMessage.getStringAttribute("clientid"));
                            mPersonInfo.setUser_id(mChatMessage.getStringAttribute("userid"));
                            mPersonInfo.setGender(mChatMessage.getIntAttribute("gender"));
                            mPersonInfo.setSignature(mChatMessage.getStringAttribute("signture"));
                            mBundle.putParcelable(FriendDetailActivity.PERSON_INFO, mPersonInfo);
                            mIntent.putExtras(mBundle);
                            mContext.startActivity(mIntent);
                        } catch (HyphenateException mE) {
                            mE.printStackTrace();
                        }

                    }
                });
    }

    @Override
    protected View setContentView(EMMessage mChatMessage, RelativeLayout mHolderView) {
        return "card".equals(mChatMessage.getStringAttribute("type", ""))
                ? LayoutInflater.from(mContext).inflate(R.layout.item_recivecardmsg, mHolderView,true)
                : LayoutInflater.from(mContext).inflate(R.layout.item_recivetextmsg, mHolderView,true);
    }


}
