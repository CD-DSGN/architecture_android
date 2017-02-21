package com.grandmagic.readingmate.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.grandmagic.readingmate.R;
import com.grandmagic.readingmate.bean.response.Contacts;
import com.grandmagic.readingmate.utils.AutoUtils;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * Created by lps on 2017/2/21.
 */

public class ContactItemDelagate implements ItemViewDelegate<Contacts> {
    private Context mContext;

    public ContactItemDelagate(Context mContext) {

        this.mContext = mContext;
    }

    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_friendlist;
    }

    @Override
    public boolean isForViewType(Contacts item, int position) {
        return item.getType() == Contacts.TYPE.TYPE_FRIEND;
    }

    @Override
    public void convert(ViewHolder holder, Contacts mContacts, int position) {
        holder.setVisible(R.id.dashline, mContacts.isNeedline());
        holder.setText(R.id.name, mContacts.getText());
        Glide.with(mContext).load("https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=3845314423,334172753&fm=21&gp=0.jpg")
                .into((ImageView) holder.getView(R.id.avatar));
    }
}
