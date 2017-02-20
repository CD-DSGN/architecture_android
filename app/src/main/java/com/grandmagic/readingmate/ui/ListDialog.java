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

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zhangmengqi on 2017/2/17.
 */

public class ListDialog extends Dialog {

    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.no)
    Button mNo;
    TextView mTvItemDlg;
    LinearLayout mLlItemDlg;
    @BindView(R.id.ll_content)
    LinearLayout mLlContent;

    private String mTitleStr;
    private ArrayList<String> mData;
    private String mBtnStr;

    private Context mContext;

    public ListDialog(Context context, ArrayList data) {
        super(context, R.style.CustomDialog);
        mContext = context;
        mData = data;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custome_dlg_list_layout);
        ButterKnife.bind(this);
        initView();
    }

    //根据数组元素，动态添加
    private void initView() {
        if (mData != null && mData.size() > 0) {
            for (String str : mData) {
                View v = LayoutInflater.from(mContext).inflate(R.layout.item_list_dlg_basic, null);
                mTvItemDlg = (TextView) v.findViewById(R.id.tv_item_dlg);
                mTvItemDlg.setText(str + "");
                mLlContent.addView(v);
            }
        }
    }
}
