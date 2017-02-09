package com.grandmagic.readingmate.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.grandmagic.readingmate.R;
import com.grandmagic.readingmate.utils.AutoUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by lps on 2017/2/9.
 */

public class HintDialog extends AlertDialog {


    private  int mWindowwidth=502;
    @BindView(R.id.msg)
    TextView mMsg;
    @BindView(R.id.sure)
    TextView mSure;

    private String msg;

    public HintDialog(@NonNull Context context, String msg) {
        super(context);
        this.msg = msg;
        View mView = LayoutInflater.from(context).inflate(R.layout.hint_dialog, null);
        ButterKnife.bind(this, mView);
        mMsg.setText(msg);
        mMsg.setTextSize(TypedValue.COMPLEX_UNIT_PX,30);
        mSure.setTextSize(TypedValue.COMPLEX_UNIT_PX,30);
        setView(mView);
        WindowManager.LayoutParams mAttributes = getWindow().getAttributes();
        mAttributes.width= mWindowwidth;
        getWindow().setAttributes(mAttributes);
        AutoUtils.auto(mView);
        show();
    }


    @OnClick(R.id.sure)
    public void onClick() {
        this.dismiss();
    }

    /**
     * 改变窗口的宽度
     * @param w
     */
    private void setWindowwidth(int w) {
        // 动态调节宽度，如果需要
    }

    /**
     * 改变显示的提示信息
     * @param s
     */
    public void setMsg(String s) {
        msg = s;
        mMsg.setText(s);
    }

    public String getMsg() {
        return msg;
    }

    /**
     * 设置提示信息字体大小
     * @param px
     */
    public void setmsgTextSize(int px){
        mMsg.setTextSize(TypedValue.COMPLEX_UNIT_PX,px);
    }

    /**
     * 设置按钮的字体大小
     * @param px
     */
    public void setBtnSureSize(int px){
        mSure.setTextSize(TypedValue.COMPLEX_UNIT_PX,px);
    }
}
