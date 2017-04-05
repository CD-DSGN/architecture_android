package com.grandmagic.readingmate.adapter;

import com.grandmagic.readingmate.R;
import com.grandmagic.readingmate.bean.db.Contacts;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * Created by lps on 2017/2/21.
 */

public class ContactLetterDelagate implements ItemViewDelegate<Contacts> {
    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_letter;
    }

    @Override
    public boolean isForViewType(Contacts item, int position) {
        return item.getType() == Contacts.TYPE.TYPE_LETTER;
    }

    @Override
    public void convert(ViewHolder holder, Contacts mContacts, int position) {
        holder.setText(R.id.letter, mContacts.getLetter());
    }
}
