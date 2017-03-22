package com.grandmagic.readingmate.ui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.StyleRes;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.grandmagic.readingmate.R;
import com.grandmagic.readingmate.utils.AutoUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zhangmengqi on 2017/3/22.
 */

public class TextDlg extends Dialog {


    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.tv_content)
    TextView mTvContent;
    @BindView(R.id.no)
    Button mNo;
    @BindView(R.id.yes)
    Button mYes;
    private String titleStr;//从外界设置的title文本
    private String messageStr;//从外界设置的消息文本

    private BtnOnclickListener mBtnOnclickListener;
    private String yesStr;
    private String noStr;

    /**
     * 设置取消按钮的显示内容和监听
     */
    public void setOnBtnOnclickListener(BtnOnclickListener btnOnclickListener) {
        this.mBtnOnclickListener = btnOnclickListener;
    }


    public TextDlg(Context context) {
        super(context, R.style.CustomDialog_bgdim);
        yesStr = context.getString(R.string.confirm);
        noStr = context.getString(R.string.cancel);
    }

    public TextDlg(Context context, @StyleRes int style) {
        super(context, style);
        yesStr = context.getString(R.string.confirm);
        noStr = context.getString(R.string.cancel);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View mView = LayoutInflater.from(getContext()).inflate(R.layout.custome_dialog_text_layout, null);
        setContentView(mView);
        AutoUtils.auto(mView);

        ButterKnife.bind(this);
        //按空白处不能取消动画
        setCanceledOnTouchOutside(false);
        //初始化界面数据
        initData();
        initView();
    }

    private void initView() {

    }


    /**
     * 初始化界面控件的显示数据
     */
    private void initData() {
        //如果用户自定了title和message
        if (titleStr != null) {
            mTitle.setText(titleStr);
        }
        if (messageStr != null) {
            mTvContent.setText(messageStr);
        }
        //如果设置按钮的文字
        if (yesStr != null) {
            mYes.setText(yesStr);
        }
        if (noStr != null) {
            mNo.setText(noStr);
        }
    }


    /**
     * 从外界Activity为Dialog设置标题
     *
     * @param title
     */
    public void setTitle(String title) {
        titleStr = title;
    }

    public void setYesStr(String mYesStr) {
        yesStr = mYesStr;
    }

    /**
     * 从外界Activity为Dialog设置dialog的message
     *
     * @param message
     */
    public void setMessage(String message) {
        messageStr = message;
        mTvContent.setText(messageStr);
    }

    /**
     * 获取dialog文本框的内容
     */
    public String getMessage() {
        return mTvContent.getText().toString();
    }

    @OnClick({R.id.no, R.id.yes})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.no:
                mBtnOnclickListener.onNoClick();
                break;
            case R.id.yes:
                mBtnOnclickListener.onYesClick();
                break;
            default:
                break;
        }
    }

    /**
     * 设置确定按钮和取消被点击的接口
     */
    public interface BtnOnclickListener {
        public void onYesClick();

        public void onNoClick();
    }


}
