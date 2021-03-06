package com.grandmagic.readingmate.ui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.StyleRes;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.grandmagic.readingmate.R;
import com.grandmagic.readingmate.utils.AutoUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * TODO: document your custom view class.
 */
public class CustomDialogWithOneBtn extends Dialog {
    @BindView(R.id.title)
    TextView titleTv;
    @BindView(R.id.message)
    EditText messageEt;

    @BindView(R.id.yes)
    Button yes;

    @BindView(R.id.num_hint)
    TextView mNumHint;

    private String titleStr;//从外界设置的title文本
    private String messageStr;//从外界设置的消息文本

    private BtnOnclickListener mBtnOnclickListener;
    private String yesStr;

    private int max_num;
    private boolean needTextLimit = true;//是否需要字数限制

    /**
     * 设置取消按钮的显示内容和监听
     */
    public void setOnBtnOnclickListener(BtnOnclickListener btnOnclickListener) {
        this.mBtnOnclickListener = btnOnclickListener;
    }


    public CustomDialogWithOneBtn(Context context) {
        super(context, R.style.CustomDialog);
        yesStr = context.getString(R.string.save);

    }

    public CustomDialogWithOneBtn(Context context, @StyleRes int style) {
        super(context, style);
        yesStr = context.getString(R.string.save);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View mView= LayoutInflater.from(getContext()).inflate(R.layout.custome_dialog_onebtn_layout,null);
        AutoUtils.auto(mView);
        setContentView(mView);

        ButterKnife.bind(this);
        //按空白处不能取消动画
        setCanceledOnTouchOutside(false);
        //初始化界面数据
        initData();
        initView();
    }

    private void initView() {
        //跟踪字数变化
        mNumHint.setText(0 + "/" + max_num);
        messageEt.addTextChangedListener(new TextWatcher() {
            private CharSequence temp;
            private int selectionStart;
            private int selectionEnd;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                temp = s;
            }

            @Override
            public void afterTextChanged(Editable s) {
                int num_input = s.length();
                int number = max_num - num_input;
                mNumHint.setText("" + num_input + "/" + max_num);
                selectionStart = messageEt.getSelectionStart();
                selectionEnd = messageEt.getSelectionEnd();
                if (temp.length() > max_num) {
                    s.delete(selectionStart - 1, selectionEnd);
                    int tempSelection = selectionEnd;
                    messageEt.setText(s);
                    messageEt.setSelection(tempSelection);//设置光标在最后
                }
            }
        });
    }


    //设置最大字数限制
    public CustomDialogWithOneBtn setMaxNum(int num) {
        max_num = num;
        return this;
    }


    /**
     * 初始化界面控件的显示数据
     */
    private void initData() {
        //如果用户自定了title和message
        if (titleStr != null) {
            titleTv.setText(titleStr);
        }
        if (messageStr != null) {
            messageEt.setText(messageStr);
        }
        //如果设置按钮的文字
        if (yesStr != null) {
            yes.setText(yesStr);
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
        if (!TextUtils.isEmpty(messageStr)) {
            messageEt.setText(messageStr);
        }
    }

    /**
     * 获取dialog文本框的内容
     */
    public String getMessage() {
        return messageEt.getText().toString();
    }

    @OnClick({R.id.yes})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.yes:
                mBtnOnclickListener.onYesClick();
                break;
            default:
                break;
        }
    }

    public CustomDialogWithOneBtn setNeedTextLimit(boolean mNeedTextLimit) {
        needTextLimit = mNeedTextLimit;
        if (!needTextLimit) {
            mNumHint.setVisibility(View.GONE);
            setMaxNum(Integer.MAX_VALUE);
        }
        return this;
    }

    /**
     * 设置确定按钮和取消被点击的接口
     */
    public interface BtnOnclickListener {
        public void onYesClick();
    }

}
