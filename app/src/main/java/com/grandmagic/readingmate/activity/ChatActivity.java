package com.grandmagic.readingmate.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.grandmagic.readingmate.R;
import com.grandmagic.readingmate.adapter.MessageIMGReciveDelagate;
import com.grandmagic.readingmate.adapter.MessageIMGSendDelagate;
import com.grandmagic.readingmate.adapter.MessageTextSendDelagate;
import com.grandmagic.readingmate.adapter.MessageTextReciveDelagate;
import com.grandmagic.readingmate.adapter.MultiItemTypeAdapter;
import com.grandmagic.readingmate.base.AppBaseActivity;
import com.grandmagic.readingmate.bean.response.ChatMessage;
import com.grandmagic.readingmate.utils.AutoUtils;
import com.grandmagic.readingmate.utils.SPUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 聊天界面
 */
public class ChatActivity extends AppBaseActivity {
public static final String CHAT_NAME="chat_name";
public static final String CHAT_IM_NAME="chat_im_name";
    private String chat_name;//聊天的在环信的name,为我们的userid
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
    @BindView(R.id.activity_chat)
    LinearLayout mActivityChat;

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
        mEtInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    MultiItemTypeAdapter mAdapter;
    List<ChatMessage> mMessageList;

    private void initview() {
        mTitlelayout.setBackgroundResource(R.color.white);
        mTitle.setText(getIntent().getStringExtra(CHAT_NAME));
        chat_name=getIntent().getStringExtra(CHAT_IM_NAME);
        mMessagerecyclerview.setLayoutManager(new LinearLayoutManager(this));
        inittestData();
        mAdapter = new MultiItemTypeAdapter(this, mMessageList);
        mAdapter.addItemViewDelegate(new MessageTextReciveDelagate(this));
        mAdapter.addItemViewDelegate(new MessageTextSendDelagate(this));
        mAdapter.addItemViewDelegate(new MessageIMGSendDelagate(this));
        mAdapter.addItemViewDelegate(new MessageIMGReciveDelagate(this));
        mMessagerecyclerview.setAdapter(mAdapter);
    }

    /**
     * 创建的测试数据
     */
    private void inittestData() {
        mMessageList = new ArrayList<>();
        for (int i = 0; i < 40; i++) {
            ChatMessage mChatMessage = new ChatMessage();

            if (i % 2 == 0) {
                mChatMessage.setName("发送方");
                mChatMessage.setMsg("发送文字消息发送文字消息发送文字消息发送文字消息发送文字消息发送文字消息"+i);
                mChatMessage.setAvatar("https://img6.bdstatic.com/img/image/smallpic/duorouzhiwu.jpg");
                mChatMessage.setType(ChatMessage.TYPE.SEND);
                if (i%6==0){
                    mChatMessage.setMessageType(ChatMessage.MessageType.IMAGE);
                    mChatMessage.setImg("http://upload.jianshu.io/admin_banners/web_images/2805/4928a468b704d6e721d778c0f7d714feadda469e.jpg");
                }else {
                    mChatMessage.setMessageType(ChatMessage.MessageType.TEXT);
                }
            } else {
                if (i%6==1){
                    mChatMessage.setMessageType(ChatMessage.MessageType.IMAGE);
                    mChatMessage.setImg("http://img5.imgtn.bdimg.com/it/u=4152427209,241902858&fm=23&gp=0.jpg");

                }else {
                    mChatMessage.setMessageType(ChatMessage.MessageType.TEXT);
                }
                mChatMessage.setName("接受方");
                mChatMessage.setMsg("收到文字消息收到文字消息收到文字消息收到文字消息收到文字消息收到文字消息收到文字消息"+i);
                mChatMessage.setAvatar("http://upload.jianshu.io/users/upload_avatars/1795423/03f6584b-deaa-4226-a455-925ac5b6b0c5.png?imageMogr/thumbnail/120x120/quality/100");
                mChatMessage.setType(ChatMessage.TYPE.RECICVER);
            }
            mMessageList.add(mChatMessage);
        }
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
}
