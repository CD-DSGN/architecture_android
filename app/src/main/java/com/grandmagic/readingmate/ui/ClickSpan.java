package com.grandmagic.readingmate.ui;

import android.content.Context;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

/**
 * Created by zhangmengqi on 2017/3/29.
 */

public class ClickSpan extends ClickableSpan {
    private Context context;
    private String memberName="";

    public ClickSpan(Context context, String memberName){
        this.context = context;
        this.memberName = memberName;
    }

    @Override
    public void onClick(View view) {
        /** 跳转 **/
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        /** 给文字染色 **/
        ds.setARGB(0xff,0x1c,0xc9,0xa2);
        /** 去掉下划线 ， 默认自带下划线 **/
        ds.setUnderlineText(false);
    }
}
