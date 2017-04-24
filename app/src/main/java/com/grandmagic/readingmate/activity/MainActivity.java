package com.grandmagic.readingmate.activity;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.grandmagic.readingmate.R;
import com.grandmagic.readingmate.base.AppBaseActivity;
import com.grandmagic.readingmate.base.AppBaseFragment;
import com.grandmagic.readingmate.base.AppBaseResponseCallBack;
import com.grandmagic.readingmate.bean.db.Contacts;
import com.grandmagic.readingmate.bean.response.SearchUserResponse;
import com.grandmagic.readingmate.consts.AppConsts;
import com.grandmagic.readingmate.db.ContactsDao;
import com.grandmagic.readingmate.db.DBHelper;
import com.grandmagic.readingmate.event.BindDeviceTokenEvent;
import com.grandmagic.readingmate.event.LogoutEvent;
import com.grandmagic.readingmate.fragment.ChatFragment;
import com.grandmagic.readingmate.fragment.HomeFragment;
import com.grandmagic.readingmate.fragment.PersonalFragment;
import com.grandmagic.readingmate.fragment.SearchFragment;
import com.grandmagic.readingmate.listener.IMMessageListenerApp;
import com.grandmagic.readingmate.listener.IMMessageListenerMain;
import com.grandmagic.readingmate.model.BookModel;
import com.grandmagic.readingmate.model.SearchUserModel;
import com.grandmagic.readingmate.utils.AutoUtils;
import com.grandmagic.readingmate.utils.IMHelper;
import com.grandmagic.readingmate.utils.KitUtils;
import com.grandmagic.readingmate.utils.UpdateManager;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.orhanobut.logger.Logger;
import com.tamic.novate.NovateResponse;
import com.tamic.novate.util.SPUtils;
import com.tbruyelle.rxpermissions.RxPermissions;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.functions.Action1;

public class MainActivity extends AppBaseActivity {
    @BindView(R.id.contentframe)
    FrameLayout mContentframe;
    @BindView(R.id.layout_home)
    RelativeLayout mLayoutHome;
    @BindView(R.id.layout_chat)
    RelativeLayout mLayoutChat;
    @BindView(R.id.layout_search)
    RelativeLayout mLayoutSearch;
    @BindView(R.id.layout_personal)
    RelativeLayout mLayoutPersonal;
    @BindView(R.id.bottomlayout)
    LinearLayout mBottomlayout;

    FragmentManager mFragmentManager;
    AppBaseFragment mcurrentFragment;
    ImageView mcurrentIV;
    TextView mcurrentTV;
    Map<String, AppBaseFragment> mFragments;//存放fragment，如果以创建就从里面加载，不重复创建
    @BindView(R.id.iv_home)
    ImageView mIvHome;
    @BindView(R.id.iv_chat)
    ImageView mIvChat;
    @BindView(R.id.iv_search)
    ImageView mIvSearch;
    @BindView(R.id.iv_person)
    ImageView mIvPerson;
    @BindView(R.id.text_home)
    TextView mTextHome;
    @BindView(R.id.text_chat)
    TextView mTextChat;
    @BindView(R.id.text_search)
    TextView mTextSearch;
    @BindView(R.id.text_person)
    TextView mTextPerson;
    IMMessageListenerMain mIMMessageListenerMain;
    @BindView(R.id.unredmsg)
    TextView mUnredmsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AutoUtils.setSize(this, false, 750, 1334);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        AutoUtils.auto(this);

        initdata();
        new RxPermissions(this).request(Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean mBoolean) {

            }
        });
        initIM();
        initSelfInfo();
        setTranslucentStatus(true);
        Intent intent = getIntent();
        startActivityFromShare(intent);
    }

    private void startActivityFromShare(Intent intent) {
        String action = intent.getAction();
        if (Intent.ACTION_VIEW.equals(action)) {
            Uri uri = intent.getData();
            if (uri != null) {
                String path = uri.getPath();
                if (path.contains(AppConsts.BOOK_DETAIL)) {
                    goToBookDetail(uri);
                } else if (path.contains(AppConsts.COMMENT_DETAIL)) {
                    goToCommentDetail(uri);
                }
            }
        }
    }


    private void goToCommentDetail(Uri uri) {
        String comment_id = uri.getQueryParameter(CommentsActivity.COMMENT_ID);
        Intent intent = new Intent(this, CommentsActivity.class);
        intent.putExtra(CommentsActivity.COMMENT_ID, comment_id);
        startActivity(intent);
    }

    private void goToBookDetail(Uri uri) {
        String book_id = uri.getQueryParameter(BookDetailActivity.BOOK_ID);
        Intent intent = new Intent(this, BookDetailActivity.class);
        intent.putExtra(BookDetailActivity.BOOK_ID, book_id);
        startActivity(intent);
    }

    /**
     * 获取自己在环信的信息
     */
    private void initSelfInfo() {
        new SearchUserModel(this).searchUser(SPUtils.getInstance().getString(this, SPUtils.INFO_NAME), new AppBaseResponseCallBack<NovateResponse<SearchUserResponse>>(this) {
            @Override
            public void onSuccee(NovateResponse<SearchUserResponse> response) {
                SearchUserResponse responseData = response.getData();
                if (responseData != null) {

                    Contacts mContacts = new Contacts();
                    mContacts.setAvatar_url(responseData.getAvatar_url().getLarge());
                    mContacts.setUser_id(Integer.valueOf(responseData.getUser_id()));
                    mContacts.setUser_name(responseData.getUser_name());
                    mContacts.setClientid(responseData.getClientid());
                    mContacts.setSignature(responseData.getSignature());
                    ContactsDao mContactsDao = DBHelper.getContactsDao(MainActivity.this);
                    mContactsDao.insertOrReplace(mContacts);
                    DBHelper.close();
                }
            }
        });
    }

    private void initIM() {
        String name = SPUtils.getInstance().getString(this, SPUtils.IM_NAME);
        String pwd = SPUtils.getInstance().getString(this, SPUtils.IM_PWD);
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(pwd)) {
            Toast.makeText(this, "登陆信息失效", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            return;
        }
        EMClient.getInstance().login(name, pwd, new EMCallBack() {//回调
            @Override
            public void onSuccess() {
                EMClient.getInstance().groupManager().loadAllGroups();
                EMClient.getInstance().chatManager().loadAllConversations();
                Logger.e("登录聊天服务器成功！");
            }

            @Override
            public void onProgress(int progress, String status) {

            }

            @Override
            public void onError(int code, String message) {
                Logger.e("登录聊天服务器失败！" + code + message);
            }
        });

        mIMMessageListenerMain = new IMMessageListenerMain(this);

    }

    private void initdata() {
        bindDeviceToken(SPUtils.getInstance().getString(this, SPUtils.DEVICE_TOKEN));
        mFragmentManager = getFragmentManager();
        mFragments = new HashMap<>();
        mcurrentFragment = (AppBaseFragment) mFragmentManager.findFragmentById(R.id.contentframe);
        //初始化homefragment
        AppBaseFragment mHomeFragment = createFragment(HomeFragment.class);
        AppBaseFragment mchatfragment = createFragment(ChatFragment.class);//初始化的时候吧聊天页也初始化了，可能需要调用他的消息方法
        mFragmentManager.beginTransaction().add(R.id.contentframe, mchatfragment).show(mchatfragment).hide(mchatfragment).commit();
        mFragmentManager.beginTransaction().add(R.id.contentframe, mHomeFragment).show(mHomeFragment).commit();
        mcurrentFragment = mHomeFragment;
        mcurrentIV = mIvHome;
        mcurrentIV.setSelected(true);
        mcurrentTV = mTextHome;
        scalelarge();

        checkVersion();
    }

    private void bindDeviceToken(String devicetoken) {
        if (devicetoken == null || devicetoken.isEmpty()) return;
        new BookModel(this).bindDeviceToken(devicetoken, new AppBaseResponseCallBack<NovateResponse>(this) {
            @Override
            public void onSuccee(NovateResponse response) {

            }
        });

    }

    /**
     * 检测版本
     */
    private void checkVersion() {

        int localVerionCode = KitUtils.getVersionCode(this);
        UpdateManager mUpdateManager = new UpdateManager(this);
        mUpdateManager.getVersionCode(localVerionCode);
    }

    @OnClick({R.id.layout_home, R.id.layout_chat, R.id.layout_search, R.id.layout_personal})
    public void onClick(View view) {
        Class<?> mClass = null;
        switch (view.getId()) {
            case R.id.layout_home:
                mClass = HomeFragment.class;
                break;
            case R.id.layout_chat:
                mClass = ChatFragment.class;
                break;
            case R.id.layout_search:
                mClass = SearchFragment.class;
                break;
            case R.id.layout_personal:
                mClass = PersonalFragment.class;
                break;
        }
        if (mClass != null) {
            switchFragment(mClass);
        }
    }

    /**
     * 切换选项
     *
     * @param mClass
     */
    private void switchFragment(Class<?> mClass) {
        if (mClass == null) return;
        AppBaseFragment to = createFragment(mClass);
        if (to.isAdded()) {
            mFragmentManager.beginTransaction().hide(mcurrentFragment).show(to).commit();
        } else {
            mFragmentManager.beginTransaction().hide(mcurrentFragment).add(R.id.contentframe, to).commit();
        }
        mcurrentFragment = to;
//        图标变化
        if (mClass == HomeFragment.class) {
            scalesmall();
            mcurrentIV.setSelected(false);
            mcurrentIV = mIvHome;
            mcurrentTV = mTextHome;
            mcurrentIV.setSelected(true);
            scalelarge();
        } else if (mClass == ChatFragment.class) {
            mcurrentIV.setSelected(false);
            scalesmall();
            mcurrentIV = mIvChat;
            mcurrentTV = mTextChat;
            mcurrentIV.setSelected(true);
            scalelarge();
        } else if (mClass == SearchFragment.class) {
            mcurrentIV.setSelected(false);
            scalesmall();
            mcurrentIV = mIvSearch;
            mcurrentTV = mTextSearch;
            mcurrentIV.setSelected(true);
            scalelarge();
        } else if (mClass == PersonalFragment.class) {
            mcurrentIV.setSelected(false);
            scalesmall();
            mcurrentIV = mIvPerson;
            mcurrentTV = mTextPerson;
            mcurrentIV.setSelected(true);
            scalelarge();
        }
    }

    /**
     * app级别的新消息处理
     *
     * @param intent
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String mStringExtra = intent.getStringExtra(IMMessageListenerApp.FLAG_NEWMESSAGE);
        if (IMMessageListenerApp.FLAG_NEWMESSAGE.equals(mStringExtra)) {
            newMsg();
            mLayoutChat.performClick();
        }

        startActivityFromShare(intent);
    }

    /**
     * 选中项图标放大
     */
    private void scalelarge() {
        ObjectAnimator.ofFloat(mcurrentIV, "scaleX", 1.0f, 1.3f).start();
        ObjectAnimator.ofFloat(mcurrentIV, "scaleY", 1.0f, 1.3f).start();
        mcurrentTV.setTextColor(Color.parseColor("#1cc9a2"));
    }

    /**
     * 切换选中项时之前选中项图标缩回原图
     */
    private void scalesmall() {
        ObjectAnimator.ofFloat(mcurrentIV, "scaleX", 1.3f, 1.0f).start();
        ObjectAnimator.ofFloat(mcurrentIV, "scaleY", 1.3f, 1.0f).start();
        mcurrentTV.setTextColor(Color.parseColor("#666666"));
    }

    /**
     * 创建或获取选中项的fragment
     *
     * @param mClass
     * @return
     */
    private AppBaseFragment createFragment(Class<?> mClass) {
        String mName = mClass.getName();
        AppBaseFragment mInstance = null;
        if (mFragments.containsKey(mName)) {
            mInstance = mFragments.get(mName);
        } else {
            try {
                mInstance = (AppBaseFragment) Class.forName(mName).newInstance();
            } catch (Exception mE) {
                mE.printStackTrace();
            }
            mFragments.put(mName, mInstance);
        }
        return mInstance;
    }


    public void newMsg() {
        // TODO: 2017/3/2 更新新消息UI
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, "收到新的消息", Toast.LENGTH_SHORT).show();
                try {
                    ((ChatFragment) mFragments.get(ChatFragment.class.getName())).onrefreshConversation();
                } catch (Exception mE) {
                    mE.printStackTrace();
                    Log.d("Exception", "run() called" + mE);
                }
            }
        });
    }

    public void setUnredmsg(int count) {
        mUnredmsg.setVisibility(count > 0 ? View.VISIBLE : View.GONE);
        mUnredmsg.setText("" + count);
    }

    @Override
    protected void onResume() {
        super.onResume();
        IMHelper.getInstance().pushActivity(this);
        EMClient.getInstance().chatManager().addMessageListener(mIMMessageListenerMain);
        mcurrentFragment.setSystemBarColor(false);
        Log.e(TAG, "onResume() called+fragment="+mcurrentFragment);
    }

    private static final String TAG = "MainActivity";

    @Override
    protected void onStop() {
        super.onStop();
        IMHelper.getInstance().popActivity(this);
        EMClient.getInstance().chatManager().removeMessageListener(mIMMessageListenerMain);
    }



    @Subscribe(threadMode = ThreadMode.MAIN)
    public void bindDeviceToken(BindDeviceTokenEvent mEvent) {
        bindDeviceToken(mEvent.getDevicetoken());
    }

    /**
     * 退出账号时结束掉Mainactivity
     * @param mEvent
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void logoutEvent(LogoutEvent mEvent) {
        finish();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
