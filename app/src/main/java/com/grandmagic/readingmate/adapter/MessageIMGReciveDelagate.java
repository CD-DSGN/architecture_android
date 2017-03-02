package com.grandmagic.readingmate.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.grandmagic.readingmate.R;
import com.grandmagic.readingmate.bean.response.ChatMessage;
import com.grandmagic.readingmate.utils.ImageLoader;
import com.hyphenate.chat.EMMessage;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * Created by lps on 2017/2/27.
 */
public class MessageIMGReciveDelagate implements ItemViewDelegate<EMMessage> {

    private Context mContext;

    public MessageIMGReciveDelagate(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_reciveimgmsg;
    }

    @Override
    public boolean isForViewType(EMMessage item, int position) {
        return item.getType() == EMMessage.Type.IMAGE
                &&item.direct()== EMMessage.Direct.RECEIVE;
    }

    @Override
    public void convert(ViewHolder holder, EMMessage mChatMessage, int position) {


    }


}
