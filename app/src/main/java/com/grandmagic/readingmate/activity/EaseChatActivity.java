package com.grandmagic.readingmate.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.grandmagic.readingmate.R;
import com.grandmagic.readingmate.base.AppBaseActivity;
import com.grandmagic.readingmate.bean.response.PersonInfo;
import com.grandmagic.readingmate.event.ContactDeletedEvent;
import com.grandmagic.readingmate.fragment.ConversationFragment;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.ui.EaseChatFragment;
import com.hyphenate.easeui.widget.chatrow.EaseChatRowText;
import com.hyphenate.exceptions.HyphenateException;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class EaseChatActivity extends AppBaseActivity {
    public static final String CHAT_NAME = "chat_name";
    public static final String GENDER = "gender";
    public static final String CHAT_IM_NAME = "chat_im_name";
    //聊天的在环信的name,为我们的userid
    private String toChatUserName;
    //聊天的name（title）
    private String chat_name;
    int gender;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ease_chat);
        initdata();
      setSystemBarColor(android.R.color.white);
        initlistener();
    }

    private void initlistener() {

    }

    private void initdata() {
        chat_name = getIntent().getStringExtra(CHAT_NAME);
        toChatUserName = getIntent().getStringExtra(CHAT_IM_NAME);
        gender = getIntent().getIntExtra(GENDER, 3);
        EaseChatFragment mEaseChatFragment=new ConversationFragment();
        //传入参数
        Bundle args = new Bundle();
        args.putInt(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_SINGLE);
        args.putString(EaseConstant.EXTRA_USER_ID, toChatUserName);
        args.putInt(GENDER,gender);
        args.putString(CHAT_NAME,chat_name);
        mEaseChatFragment.setArguments(args);
        getSupportFragmentManager().beginTransaction().add(R.id.chatfram, mEaseChatFragment).commit();
    }
}
