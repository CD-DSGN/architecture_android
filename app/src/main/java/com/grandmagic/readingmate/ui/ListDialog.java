package com.grandmagic.readingmate.ui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.grandmagic.readingmate.R;
import com.grandmagic.readingmate.utils.AutoUtils;

import java.util.ArrayList;

import butterknife.ButterKnife;

/**
 * Created by zhangmengqi on 2017/2/17.
 */

public class ListDialog extends Dialog {

    View mView;
    TextView mTvItemDlg;
    LinearLayout mLlItemDlg;

    TextView mTitle;

    LinearLayout mLlContent;

    Button mNo;


    private String mTitleStr;

    public void setData(ArrayList<String> data) {
        mData = data;
    }

    private ArrayList<String> mData;
    private String mBtnStr;

    protected Context mContext;

    public void setOnitemClickListener(OnitemClickListener onitemClickListener) {
        mOnitemClickListener = onitemClickListener;
    }

    private OnitemClickListener mOnitemClickListener;


    public ListDialog(Context context) {
        super(context, R.style.CustomDialog);
        mContext = context;
    }

    public ListDialog(Context context, String title) {
        super(context, R.style.CustomDialog);
        mContext = context;
        mTitleStr = title;
    }

    public void setTitleStr(String titleStr) {
        mTitleStr = titleStr;
    }

    public ListDialog(Context context, ArrayList data, String title) {
        super(context, R.style.CustomDialog_bgdim);
        mContext = context;
        mData = data;
        mTitleStr = title;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mView = LayoutInflater.from(getContext()).inflate(R.layout.custome_dlg_list_layout, null);
        setContentView(mView);
        ButterKnife.bind(mView);
        AutoUtils.auto(mView);
        initView();
    }

    //根据数组元素，动态添加
    private void initView() {
        mTitle = (TextView) mView.findViewById(R.id.title);
        mLlContent = (LinearLayout) mView.findViewById(R.id.ll_content);
        mNo = (Button) mView.findViewById(R.id.no);
        mNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListDialog.this.dismiss();
            }
        });

        mTitle.setText(mTitleStr);
        if (mData != null && mData.size() > 0) {
            for (int i = 0; i < mData.size(); i++) {
                View v = LayoutInflater.from(mContext).inflate(R.layout.item_list_dlg_basic, mLlContent, false);
                AutoUtils.auto(v);
                mTvItemDlg = (TextView) v.findViewById(R.id.tv_item_dlg);
                mTvItemDlg.setText(mData.get(i) + "");
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
    }



    public interface OnitemClickListener {
        public void onClick(int postion);
    }


}
