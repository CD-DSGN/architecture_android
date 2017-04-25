package com.grandmagic.readingmate.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.grandmagic.readingmate.R;
import com.grandmagic.readingmate.activity.ChatActivity;
import com.grandmagic.readingmate.base.AppBaseResponseCallBack;
import com.grandmagic.readingmate.bean.db.Contacts;
import com.grandmagic.readingmate.model.ContactModel;
import com.grandmagic.readingmate.ui.CustomDialog;
import com.grandmagic.readingmate.utils.ImageLoader;
import com.tamic.novate.NovateResponse;
import com.tamic.novate.util.Environment;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * Created by lps on 2017/2/21.
 */

public class ContactItemDelagate implements ItemViewDelegate<Contacts>, CustomDialog.BtnOnclickListener {
    private Context mContext;
    private String mUser_id;
private int remarkPosition=-1;
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
    public void convert(ViewHolder holder, final Contacts mContacts, final int position) {
        holder.setVisible(R.id.dashline, mContacts.isNeedline());
        holder.setText(R.id.name, TextUtils.isEmpty(mContacts.getRemark())?mContacts.getUser_name():mContacts.getRemark());
        ImageLoader.loadCircleImage(mContext, Environment.BASEULR_PRODUCTION + mContacts.getAvatar_url().getLarge(),
                (ImageView) holder.getView(R.id.avatar));
        holder.setOnClickListener(R.id.notename, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNoteNameDialog(mContacts.getUser_id() + "");
                remarkPosition=position;
            }
        });
        holder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(mContext, ChatActivity.class);
                mIntent.putExtra(ChatActivity.CHAT_NAME, mContacts.getUser_name());
                mIntent.putExtra(ChatActivity.CHAT_IM_NAME, mContacts.getUser_id() + "");
                mIntent.putExtra(ChatActivity.GENDER, mContacts.getGender());
                mContext.startActivity(mIntent);
            }
        });
    }

    private CustomDialog mNoteNameDialog;

    private void showNoteNameDialog(String mUser_id) {
        this.mUser_id = mUser_id;
        if (mNoteNameDialog == null) {
            mNoteNameDialog = new CustomDialog(mContext, R.style.CustomDialog_bgdim);
            mNoteNameDialog.setOnBtnOnclickListener(this);
            mNoteNameDialog.setYesStr("保存");
            mNoteNameDialog.setTitle("备注姓名");
        }
        mNoteNameDialog.show();
        mNoteNameDialog.setMessage("");//再次弹起置空
        mNoteNameDialog.setNeedTextLimit(false);
    }

    ContactModel mModel;

    @Override
    public void onYesClick() {
        mNoteNameDialog.dismiss();
        mModel = new ContactModel(mContext);
        final String mMessage = mNoteNameDialog.getMessage();
        mModel.remarkName(mUser_id, mMessage, new AppBaseResponseCallBack<NovateResponse>(mContext) {
            @Override
            public void onSuccee(NovateResponse response) {
                if (mRemarkListener != null) mRemarkListener.remark(mMessage,remarkPosition);
            }
        });
    }

    @Override
    public void onNoClick() {
        if (mNoteNameDialog.isShowing()) {
            mNoteNameDialog.dismiss();
        }
    }

    remarkListener mRemarkListener;

    public ContactItemDelagate setRemarkListener(remarkListener mRemarkListener) {
        this.mRemarkListener = mRemarkListener;
        return this;
    }

    public interface remarkListener {
        void remark(String mMessage, int mRemarkPosition);
    }
}
