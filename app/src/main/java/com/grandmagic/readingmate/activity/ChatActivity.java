package com.grandmagic.readingmate.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.grandmagic.readingmate.R;
import com.grandmagic.readingmate.adapter.MessageIMGRecDelagate;
import com.grandmagic.readingmate.adapter.MessageIMGSendDelagate;
import com.grandmagic.readingmate.adapter.MessageTextRecDelagate;
import com.grandmagic.readingmate.adapter.MessageTextSendDelagate;
import com.grandmagic.readingmate.adapter.MultiItemTypeAdapter;
import com.grandmagic.readingmate.base.AppBaseActivity;
import com.grandmagic.readingmate.utils.AutoUtils;
import com.grandmagic.readingmate.utils.IMHelper;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 聊天界面
 */
public class ChatActivity extends AppBaseActivity implements EMMessageListener {
    private static final String TAG = "ChatActivity";
    public static final String CHAT_NAME = "chat_name";
    public static final String CHAT_IM_NAME = "chat_im_name";
    public static final String CHAT_TYPE = "chat_type";
    //聊天的在环信的name,为我们的userid
    private String toChatUserName;
    private int chat_type;
    //聊天的name（title）
    private String chat_name;
    @BindView(R.id.back)
    ImageView mBack;
    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.titlelayout)
    RelativeLayout mTitlelayout;
    @BindView(R.id.messagerecyclerview)
    RecyclerView mMessagerecyclerview;
    @BindView(R.id.iv_select_img)
    ImageView mIvSelectImg;
    @BindView(R.id.et_input)
    EditText mEtInput;
    @BindView(R.id.voice)
    ImageView mVoice;
    private EMConversation mConversation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);
        AutoUtils.auto(this);
        setTranslucentStatus(true);
        initview();
        initlistener();
    }

    private void initlistener() {

        mEtInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                String mString = v.getText().toString();
                if (actionId == EditorInfo.IME_ACTION_SEND ||
                        (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    sendTextMsg(mString);
                    return true;
                }
                return false;
            }
        });
    }

    /**
     * 发送文本消息
     *
     * @param mString
     */
    private void sendTextMsg(String mString) {
//如果String 为空。则返回的message为null
        EMMessage message = EMMessage.createTxtSendMessage(mString, toChatUserName);
        if (message != null) {
            sendMessage(message);
        }
    }

    /**
     * 统一的发送消息
     *
     * @param mMessage
     */
    private void sendMessage(EMMessage mMessage) {
        EMClient.getInstance().chatManager().sendMessage(mMessage);
        mMessageList.add(mMessage);
        mAdapter.setData(mMessageList);
        mEtInput.setText("");
        mMessagerecyclerview.smoothScrollToPosition(mMessageList.size() - 1);
        mMessage.setMessageStatusCallback(new EMCallBack() {
            @Override
            public void onSuccess() {
                Log.e(TAG, "setMessageStatusCallback  onSuccess: " );
            }

            @Override
            public void onError(int mI, String mS) {
                Logger.e( "setMessageStatusCallback  onError: " +mI+mS);

            }

            @Override
            public void onProgress(int mI, String mS) {
              Logger.e("setMessageStatusCallback  onProgress: "+mI+mS );

            }
        });
    }

    MultiItemTypeAdapter mAdapter;
    List<EMMessage> mMessageList = new ArrayList<>();

    private void initview() {
        mTitlelayout.setBackgroundResource(R.color.white);
        chat_name = getIntent().getStringExtra(CHAT_NAME);
        mTitle.setText(chat_name);
        toChatUserName = getIntent().getStringExtra(CHAT_IM_NAME);
        mMessagerecyclerview.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new MultiItemTypeAdapter(this, mMessageList);
        mAdapter.addItemViewDelegate(new MessageTextSendDelagate(this));
        mAdapter.addItemViewDelegate(new MessageTextRecDelagate(this));
        mAdapter.addItemViewDelegate(new MessageIMGSendDelagate(this));
        mAdapter.addItemViewDelegate(new MessageIMGRecDelagate(this));
        mMessagerecyclerview.setAdapter(mAdapter);
        conversationInit();
    }

    // FIXME: 2017/3/2 mConversation有时候返回空，环信处理的比较奇怪

    private void conversationInit() {
        mConversation = EMClient.getInstance().chatManager().getConversation(toChatUserName);
        if (mConversation == null) return;
        mConversation.markAllMessagesAsRead();
        final List<EMMessage> msgs = mConversation.getAllMessages();
        mMessageList.addAll(msgs);
        int msgCount = msgs != null ? msgs.size() : 0;
        int pagesize = 20;//默认最左加载最近的20条。其他通过用户下拉刷新获取
        if (msgCount < mConversation.getAllMsgCount() && msgCount < pagesize) {
            String msgId = null;
            if (msgs != null && msgs.size() > 0) {
                msgId = msgs.get(0).getMsgId();
            }
            List<EMMessage> mEMMessages = mConversation.loadMoreMsgFromDB(msgId, pagesize - msgCount);
            mMessageList.addAll(mEMMessages);
        }
        mAdapter.setData(mMessageList);
    }


    @OnClick({R.id.back, R.id.iv_select_img, R.id.voice})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.iv_select_img:
                // TODO: 2017/2/22 从相册选择图片
                break;
            case R.id.voice:
                // TODO: 2017/2/22 录音
                break;
        }
    }

    /**
     * 收到消息的回调(在子线程执行)
     *
     * @param mList
     */
    @Override
    public void onMessageReceived(List<EMMessage> mList) {
        Logger.d(mList);
        for (EMMessage msg : mList) {
            String username = null;
            if (msg.getChatType() == EMMessage.ChatType.GroupChat ||
                    msg.getChatType() == EMMessage.ChatType.ChatRoom) {//如果不是单聊
                username = msg.getTo();
            } else {
                username = msg.getFrom();
            }
            //判断是否就是当前会话
            if (toChatUserName.equals(username)) {
                mMessageList.add(msg);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.setData(mMessageList);
                        mMessagerecyclerview.smoothScrollToPosition(mMessageList.size() - 1);
                    }
                });

            }

        }
    }

    @Override
    public void onCmdMessageReceived(List<EMMessage> mList) {

    }

    @Override
    public void onMessageRead(List<EMMessage> mList) {

    }

    @Override
    public void onMessageDelivered(List<EMMessage> mList) {

    }

    @Override
    public void onMessageChanged(EMMessage mEMMessage, Object mO) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        IMHelper.getInstance().pushActivity(this);
        EMClient.getInstance().chatManager().addMessageListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EMClient.getInstance().chatManager().removeMessageListener(this);//移除监听
        IMHelper.getInstance().popActivity(this);
    }
}
