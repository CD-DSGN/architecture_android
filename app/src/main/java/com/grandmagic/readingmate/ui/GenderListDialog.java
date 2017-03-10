package com.grandmagic.readingmate.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.grandmagic.readingmate.R;
import com.grandmagic.readingmate.utils.AutoUtils;
import com.grandmagic.readingmate.utils.ViewUtils;

import java.util.ArrayList;

/**
 * Created by zhangmengqi on 2017/3/9.
 */

public class GenderListDialog extends ListDialog implements ListDialog.OnitemClickListener{

    ImageView mIvItemDlg;
    public GenderListDialog(Context context) {
        super(context, context.getString(R.string.choose_gender));
        ArrayList<String> data = new ArrayList<String>();
        data.add(mContext.getString(R.string.male));
        data.add(mContext.getString(R.string.female));
        setData(data);
        setOnitemClickListener(this);
    }

    @Override
    protected void initView() {
        initial();
        for (int i = 0; i < mData.size(); i++) {
            View v = LayoutInflater.from(mContext).inflate(R.layout.item_list_dlg_gender, mLlContent, false);
            AutoUtils.auto(v);
            mTvItemDlg = (TextView) v.findViewById(R.id.tv_item_dlg);
            mIvItemDlg = (ImageView) v.findViewById(R.id.iv_gender);
            mTvItemDlg.setText(mData.get(i) + "");
            if (i == 0) {
                mIvItemDlg.setImageResource(R.drawable.iv_male);
            }else{
                mIvItemDlg.setImageResource(R.drawable.iv_female);
            }

            mLlContent.addView(v);
            final int index = i;
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnitemClickListener.onClick(index);
                }
            });
        }

    }

    @Override
    public void onClick(int postion) {
        if (postion == 0) {
            ViewUtils.showToast("点击男");
        } else {
            ViewUtils.showToast("点击女");
        }
    }
}
