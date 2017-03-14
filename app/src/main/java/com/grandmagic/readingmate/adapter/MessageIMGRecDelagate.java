package com.grandmagic.readingmate.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
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
public class MessageIMGRecDelagate extends ChatItemViewDelegate {


    public MessageIMGRecDelagate(Context mContext) {
        super(mContext);
    }

    @Override
    public boolean isForViewType(EMMessage item) {
        return item.getType() == EMMessage.Type.IMAGE&&mDirect== EMMessage.Direct.RECEIVE;
    }

    @Override
    protected void childConvert(ViewHolder mHolder, EMMessage data, int mPosition) {
        EMImageMessageBody  mBody= (EMImageMessageBody) data.getBody();
        ImageLoader.loadRoundImage(mContext,mBody.getLocalUrl(), (ImageView) mHolder.getView(R.id.image));
    }

    @Override
    protected View setContentView() {
        return LayoutInflater.from(mContext).inflate(R.layout.item_reciveimgmsg,null);
    }


}
