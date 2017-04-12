package com.grandmagic.readingmate.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.grandmagic.readingmate.R;
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
         EMImageMessageBody  mBody= (EMImageMessageBody) data.getBody();
        ImageLoader.loadImage(mContext,mBody.getRemoteUrl(),(ImageView) mHolder.getView(R.id.image));
    }

    @Override
    protected View setContentView(EMMessage mChatMessage, RelativeLayout mHolderView) {
        return LayoutInflater.from(mContext).inflate(R.layout.item_reciveimgmsg,mHolderView,true);
    }


}
