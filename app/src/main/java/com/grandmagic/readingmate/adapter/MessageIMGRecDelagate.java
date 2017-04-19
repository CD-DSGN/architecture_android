package com.grandmagic.readingmate.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.grandmagic.readingmate.R;
import com.grandmagic.readingmate.activity.BigImageActivity;
import com.grandmagic.readingmate.utils.ImageLoader;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMImageMessageBody;
import com.hyphenate.chat.EMMessage;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * Created by lps on 2017/2/27.
 */
public class MessageIMGRecDelagate extends ChatItemViewDelegate {


    public MessageIMGRecDelagate(Context mContext) {
        super(mContext);
    }

    @Override
    public boolean isForViewType(EMMessage item) {
        return item.getType() == EMMessage.Type.IMAGE&&mDirect== EMMessage.Direct.RECEIVE;
    }

    private static final String TAG = "MessageIMGRecDelagate";
    @Override
    protected void childConvert(final ViewHolder mHolder, EMMessage data, int mPosition) {
         final EMImageMessageBody  mBody= (EMImageMessageBody) data.getBody();
        Glide.with(mContext).load(mBody.getRemoteUrl()).error(R.drawable.img_load_err).into((ImageView) mHolder.getView(R.id.image));
        mHolder.getView(R.id.image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(mContext,BigImageActivity.class);
                mIntent.putExtra(BigImageActivity.IMG_URL, mBody.getRemoteUrl());
                mContext.startActivity(mIntent);
            }
        });
    }

    @Override
    protected View setContentView(EMMessage mChatMessage, RelativeLayout mHolderView) {
        return LayoutInflater.from(mContext).inflate(R.layout.item_reciveimgmsg,mHolderView,true);
    }


}
