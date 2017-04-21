package com.grandmagic.readingmate.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.grandmagic.readingmate.R;
import com.grandmagic.readingmate.activity.BigImageActivity;
import com.grandmagic.readingmate.utils.ImageLoader;
import com.hyphenate.chat.EMImageMessageBody;
import com.hyphenate.chat.EMMessage;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * Created by lps on 2017/2/27.
 */
public class MessageIMGSendDelagate extends ChatItemViewDelegate {


    public MessageIMGSendDelagate(Context mContext) {
        super(mContext);
    }

    @Override
    public boolean isForViewType(EMMessage item) {
        return item.getType() == EMMessage.Type.IMAGE && mDirect == EMMessage.Direct.SEND;
    }

    @Override
    protected void childConvert(ViewHolder mHolder, EMMessage data, int mPosition) {
        final EMImageMessageBody mBody = (EMImageMessageBody) data.getBody();
        ImageLoader.loadImage(mContext, mBody.thumbnailLocalPath(), (ImageView) mHolder.getView(R.id.image));
        mHolder.getView(R.id.image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(mContext,BigImageActivity.class);
                mIntent.putExtra(BigImageActivity.IMG_URL, mBody.getLocalUrl());
                mContext.startActivity(mIntent);
            }
        });
    }

    @Override
    protected View setContentView(EMMessage mChatMessage, RelativeLayout mHolderView) {
        return LayoutInflater.from(mContext).inflate(R.layout.item_sendimgmsg, mHolderView, true);
    }


}
