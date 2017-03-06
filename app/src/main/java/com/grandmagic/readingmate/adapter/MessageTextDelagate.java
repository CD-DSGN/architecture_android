package com.grandmagic.readingmate.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
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
import com.orhanobut.logger.Logger;
import com.tamic.novate.util.Environment;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * Created by lps on 2017/2/22.
 */
public class MessageTextDelagate extends ChatItemViewDelegate {



    public MessageTextDelagate(Context mContext) {
        super(mContext);
    }

    @Override
    public boolean isForViewType(EMMessage item, int position) {
        return item.getType() == EMMessage.Type.TXT;
    }

    @Override
    protected void childConvert(ViewHolder mHolder, EMMessage mChatMessage, int mPosition) {
        EMTextMessageBody mBody = (EMTextMessageBody) mChatMessage.getBody();
        mHolder.setText(R.id.content, mBody.getMessage());
    }

    @Override
    protected View setContentView() {
        return LayoutInflater.from(mContext).inflate(mDirect == EMMessage.Direct.RECEIVE ? R.layout.item_recivetextmsg :
                R.layout.item_sendtextmsg, null);
    }


}
