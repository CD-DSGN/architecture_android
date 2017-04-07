package com.grandmagic.readingmate.ui;

import android.content.Context;
import android.os.Bundle;

import com.grandmagic.readingmate.R;

/**
 * Created by zhangmengqi on 2017/3/22.
 */

public class DeleteConfirmDlg extends TextDlg implements TextDlg.BtnOnclickListener {

    private Context mContext;

    public void setOnClickYes(OnClickYes onClickYes) {
        mOnClickYes = onClickYes;
    }

    private OnClickYes mOnClickYes;

    public DeleteConfirmDlg(Context context) {
        super(context);
        mContext = context;
    }


    public DeleteConfirmDlg(Context context, int style) {
        super(context, style);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        setTitle(R.string.delete);
        setMessage(mContext.getString(R.string.confirm_delete));
        setOnBtnOnclickListener(this);
    }


    @Override
    public void onYesClick() {
        if (mOnClickYes != null) {
            mOnClickYes.onClick();
        }
    }

    @Override
    public void onNoClick() {
        cancel();   //取消对话框
    }

    public interface OnClickYes{
        void onClick();
    }
}
