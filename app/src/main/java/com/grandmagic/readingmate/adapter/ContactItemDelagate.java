package com.grandmagic.readingmate.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.grandmagic.readingmate.R;
import com.grandmagic.readingmate.bean.response.Contacts;
import com.grandmagic.readingmate.ui.CustomDialog;
import com.grandmagic.readingmate.utils.AutoUtils;
import com.grandmagic.readingmate.utils.ImageLoader;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * Created by lps on 2017/2/21.
 */

public class ContactItemDelagate implements ItemViewDelegate<Contacts>, CustomDialog.BtnOnclickListener {
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
        holder.setText(R.id.name, mContacts.getUser_name());
        ImageLoader.loadRoundImage(mContext, mContacts.getAvatar_native(),
                (ImageView) holder.getView(R.id.avatar));
        holder.setOnClickListener(R.id.notename, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNoteNameDialog();
            }
        });
    }

    private CustomDialog mNoteNameDialog;

    private void showNoteNameDialog() {
        if (mNoteNameDialog == null) {
            mNoteNameDialog = new CustomDialog(mContext, R.style.CustomDialog_bgdim);
            mNoteNameDialog.setOnBtnOnclickListener(this);
            mNoteNameDialog.setYesStr("保存");
            mNoteNameDialog.setTitle("备注姓名");
        }
        mNoteNameDialog.show();
        mNoteNameDialog.setNeedTextLimit(false);
    }

    @Override
    public void onYesClick() {

    }

    @Override
    public void onNoClick() {
        if (mNoteNameDialog.isShowing()) {
            mNoteNameDialog.dismiss();
        }
    }
}
