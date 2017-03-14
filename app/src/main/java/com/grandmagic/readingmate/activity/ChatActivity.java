package com.grandmagic.readingmate.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import com.grandmagic.readingmate.adapter.ChatItemViewDelegate;
import com.grandmagic.readingmate.adapter.MessageIMGRecDelagate;
import com.grandmagic.readingmate.adapter.MessageIMGSendDelagate;
import com.grandmagic.readingmate.adapter.MessageTextRecDelagate;
import com.grandmagic.readingmate.adapter.MessageTextSendDelagate;
import com.grandmagic.readingmate.adapter.MultiItemTypeAdapter;
import com.grandmagic.readingmate.base.AppBaseActivity;
import com.grandmagic.readingmate.event.ContactDeleteEvent;
import com.grandmagic.readingmate.utils.AutoUtils;
import com.grandmagic.readingmate.utils.IMHelper;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.orhanobut.logger.Logger;
import com.yuyh.library.imgsel.ImageLoader;
import com.yuyh.library.imgsel.ImgSelActivity;
import com.yuyh.library.imgsel.ImgSelConfig;
import com.yuyh.library.imgsel.common.Constant;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import cn.bingoogolapple.refreshlayout.BGAStickinessRefreshViewHolder;

/**
 * 聊天界面
 */
public class ChatActivity extends AppBaseActivity implements EMMessageListener, ChatItemViewDelegate.chatClickListener {
    private static final String TAG = "ChatActivity";
    public static final String CHAT_NAME = "chat_name";
    public static final String CHAT_IM_NAME = "chat_im_name";
    public static final String CHAT_TYPE = "chat_type";
    public static final int REQUEST_DETAIL = 101;
    private static final int REQUEST_SELIMG = 102;
    @BindView(R.id.refreshLayout)
    BGARefreshLayout mRefreshLayout;
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
    private int DEFAULT_PAGESIZE = 10;//默认显示消息数量和加载更多时候显示的消息数量


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);
        AutoUtils.auto(this);
        setTranslucentStatus(true);
        EventBus.getDefault().register(this);
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

    private void initrefreshlayout() {
        BGAStickinessRefreshViewHolder mRefreshViewHolder = new BGAStickinessRefreshViewHolder(this, false);
        mRefreshViewHolder.setStickinessColor(R.color.colorAccent);
        mRefreshViewHolder.setRotateImage(R.drawable.bga_refresh_stickiness);
//        mRefreshLayout.offsetTopAndBottom(88);
        mRefreshLayout.setRefreshViewHolder(mRefreshViewHolder);
        mRefreshLayout.setDelegate(new BGARefreshLayout.BGARefreshLayoutDelegate() {
            @Override
            public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
                loadMoreMsg();
                mRefreshLayout.endRefreshing();
            }

            @Override
            public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
                return false;
            }
        });
    }

    /**
     * 下拉加载更多的消息
     */
    private void loadMoreMsg() {
        int mMsgCount = mConversation.getAllMsgCount();
        if (mMsgCount > DEFAULT_PAGESIZE) {
            List<EMMessage> mEMMessages = mConversation.loadMoreMsgFromDB(mMessageList.get(0).getMsgId(), DEFAULT_PAGESIZE);
            mMessageList.addAll(0, mEMMessages);
            mAdapter.setData(mMessageList);
            mRefreshLayout.endRefreshing();
        } else {
            mRefreshLayout.endRefreshing();
        }
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
        mAdapter.addItemViewDelegate(new MessageTextSendDelagate(this).setChatClickListener(this));
        mAdapter.addItemViewDelegate(new MessageTextRecDelagate(this).setChatClickListener(this));
        mAdapter.addItemViewDelegate(new MessageIMGSendDelagate(this).setChatClickListener(this));
        mAdapter.addItemViewDelegate(new MessageIMGRecDelagate(this).setChatClickListener(this));
        mMessagerecyclerview.setAdapter(mAdapter);
        conversationInit();
        initrefreshlayout();
    }

    // FIXME: 2017/3/2 mConversation有时候返回空，环信处理的比较奇怪

    private void conversationInit() {
        mConversation = EMClient.getInstance().chatManager().getConversation(toChatUserName);
        if (mConversation == null) return;
        mConversation.markAllMessagesAsRead();
        final List<EMMessage> msgs = mConversation.getAllMessages();
        int msgCount = msgs != null ? msgs.size() : 0;
        int pagesize = DEFAULT_PAGESIZE;//默认最左加载最近的20条。其他通过用户下拉刷新获取
        if (msgCount < mConversation.getAllMsgCount() && msgCount < pagesize) {
            String msgId = null;
            if (msgs != null && msgs.size() > 0) {
                msgId = msgs.get(0).getMsgId();
            }
            List<EMMessage> mEMMessages = mConversation.loadMoreMsgFromDB(msgId, pagesize - msgCount);
            mMessageList.addAll(mEMMessages);
        } else if (msgs != null) {
            mMessageList.addAll(msgs);
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
                chooseImg();
                break;
            case R.id.voice:
                // TODO: 2017/2/22 录音
                break;
        }
    }

    private void chooseImg() {
        ImageLoader mLoader = new ImageLoader() {
            @Override
            public void displayImage(Context context, String path, ImageView imageView) {
                com.grandmagic.readingmate.utils.ImageLoader.loadImage(ChatActivity.this, path, imageView);
            }
        };
        ImgSelConfig mConfig = new ImgSelConfig.Builder(this, mLoader)
                .multiSelect(false)
                .btnBgColor(Color.GRAY)// “确定”按钮背景 色
                // “确定”按钮文字颜色
                .btnTextColor(Color.BLUE)
                // 使用沉浸式状态栏
                .statusBarColor(Color.parseColor("#3F51B5"))
                // 返回图标ResId
                .backResId(R.drawable.ic_back)
                // 标题
                .title("图片")
                // 标题文字颜色
                .titleColor(Color.WHITE)
                // TitleBar背景色
                .titleBgColor(Color.parseColor("#3F51B5"))
                // 裁剪大小。needCrop为true的时候配置
//                .cropSize(1, 1, 200, 200)
                .needCrop(false)
                // 第一个是否显示相机
                .needCamera(true)
                .build();
        // 跳转到图片选择器
        ImgSelActivity.startActivity(this, mConfig, REQUEST_SELIMG);
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

    /**
     * 点击头像事件
     */
    @Override
    public void clickAvatar() {
        Intent mIntent = new Intent(ChatActivity.this, FriendDetailActivity.class);
        mIntent.putExtra(FriendDetailActivity.USER_ID, toChatUserName);
        startActivityForResult(mIntent, REQUEST_DETAIL);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SELIMG && resultCode == RESULT_OK) {
            Constant.imageList.clear();
            List<String> path = data.getStringArrayListExtra(ImgSelActivity.INTENT_RESULT);
            sendImgMessage(path.get(0));
        }
    }

    /**
     * 创建图片类型消息发送
     * @param path
     */
    private void sendImgMessage(String path) {
        //imagePath为图片本地路径，false为不发送原图（默认超过100k的图片会压缩后发给对方），需要发送原图传true
        EMMessage mMessage = EMMessage.createImageSendMessage(path, false, toChatUserName);
//如果是群聊，设置chattype，默认是单聊
        sendMessage(mMessage);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void contactDelete(ContactDeleteEvent mEvent) {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
