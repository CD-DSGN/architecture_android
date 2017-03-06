package com.grandmagic.readingmate.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.grandmagic.readingmate.R;
import com.grandmagic.readingmate.bean.response.ChatMessage;
import com.grandmagic.readingmate.bean.response.Contacts;
import com.grandmagic.readingmate.utils.IMHelper;
import com.grandmagic.readingmate.utils.ImageLoader;
import com.hyphenate.chat.EMImageMessageBody;
import com.hyphenate.chat.EMMessage;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * Created by lps on 2017/2/27.
 */
public class MessageIMGDelagate implements ItemViewDelegate<EMMessage> {

    private Context mContext;
    private EMMessage.Direct mDirect;

    public MessageIMGDelagate(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public int getItemViewLayoutId() {
        return mDirect == EMMessage.Direct.RECEIVE ? R.layout.item_reciveimgmsg : R.layout.item_sendimgmsg;
    }

    @Override
    public boolean isForViewType(EMMessage item, int position) {
        mDirect = item.direct();
        return item.getType() == EMMessage.Type.IMAGE
                && mDirect == EMMessage.Direct.RECEIVE;
    }

    @Override
    public void convert(ViewHolder holder, EMMessage mChatMessage, int position) {
        EMImageMessageBody mBody = (EMImageMessageBody) mChatMessage.getBody();
        ImageLoader.loadRoundImage(mContext, mBody.getRemoteUrl(), (ImageView) holder.getView(R.id.image));
        Contacts mUserInfo = IMHelper.getInstance()
                .getUserInfo( mChatMessage.getFrom());
        if (mUserInfo == null) return;
        ImageLoader.loadRoundImage(mContext,
                mUserInfo.getAvatar_native(),
                (ImageView) holder.getView(R.id.avatar));
    }


}
