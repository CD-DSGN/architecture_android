package com.grandmagic.readingmate.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.grandmagic.readingmate.R;
import com.grandmagic.readingmate.bean.response.ChatMessage;
import com.grandmagic.readingmate.bean.response.Contacts;
import com.grandmagic.readingmate.utils.GlideRoundTransform;
import com.grandmagic.readingmate.utils.IMHelper;
import com.grandmagic.readingmate.utils.ImageLoader;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMessageBody;
import com.hyphenate.chat.EMTextMessageBody;
import com.tamic.novate.util.Environment;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * Created by lps on 2017/2/22.
 */
public class MessageTextDelagate implements ItemViewDelegate<EMMessage> {

    private Context mContext;
    EMMessage.Direct mDirect;

    public MessageTextDelagate(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public int getItemViewLayoutId() {
        return mDirect == EMMessage.Direct.RECEIVE ? R.layout.item_recivetextmsg : R.layout.item_sendtextmsg;
    }

    @Override
    public boolean isForViewType(EMMessage item, int position) {
        EMMessage.Type mType = item.getType();
        mDirect = item.direct();
        return mType == EMMessage.Type.TXT;
    }

    @Override
    public void convert(ViewHolder holder, EMMessage mChatMessage, int position) {
        EMTextMessageBody mBody = (EMTextMessageBody) mChatMessage.getBody();
        Contacts mUserInfo = IMHelper.getInstance()
                .getUserInfo(mChatMessage.getFrom());
        holder.setText(R.id.content, mBody.getMessage());
        if (mUserInfo == null) return;
        ImageLoader.loadRoundImage(mContext,
                Environment.BASEULR_PRODUCTION+ mUserInfo.getAvatar_native(),
                (ImageView) holder.getView(R.id.avatar)
        );
    }


}
