package com.grandmagic.readingmate.activity;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Color;
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
import com.grandmagic.readingmate.fragment.ChatFragment;
import com.grandmagic.readingmate.fragment.HomeFragment;
import com.grandmagic.readingmate.fragment.PersonalFragment;
import com.grandmagic.readingmate.fragment.SearchFragment;
import com.grandmagic.readingmate.listener.IMMessageListenerMain;
import com.grandmagic.readingmate.utils.AutoUtils;
import com.grandmagic.readingmate.utils.IMHelper;
import com.grandmagic.readingmate.utils.KitUtils;
import com.grandmagic.readingmate.utils.SPUtils;
import com.grandmagic.readingmate.utils.UpdateManager;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.tbruyelle.rxpermissions.RxPermissions;

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
    Fragment mcurrentFragment;
    ImageView mcurrentIV;
    TextView mcurrentTV;
    Map<String, Fragment> mFragments;//存放fragment，如果以创建就从里面加载，不重复创建
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                Log.d("main", "登录聊天服务器成功！");
            }

            @Override
            public void onProgress(int progress, String status) {

            }

            @Override
            public void onError(int code, String message) {
                Log.d("main", "登录聊天服务器失败！" + code + message);
            }
        });

        mIMMessageListenerMain = new IMMessageListenerMain(this);

    }

    private void initdata() {
        mFragmentManager = getFragmentManager();
        mFragments = new HashMap<>();
        mcurrentFragment = mFragmentManager.findFragmentById(R.id.contentframe);
        if (mcurrentFragment == null) {//初始化homefragment
            Fragment mHomeFragment = createFragment(HomeFragment.class);
            mFragmentManager.beginTransaction().add(R.id.contentframe, mHomeFragment).show(mHomeFragment).commit();
            mcurrentFragment = mHomeFragment;
            mcurrentIV = mIvHome;
            mcurrentTV = mTextHome;
            scalelarge();
        }
        checkVersion();
    }

    /**
     * 检测版本
     */
    private void checkVersion() {
// TODO: 2017/2/10 从服务端获取版本号
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
        Fragment to = createFragment(mClass);
        if (to.isAdded()) {
            mFragmentManager.beginTransaction().hide(mcurrentFragment).show(to).commit();
        } else {
            mFragmentManager.beginTransaction().hide(mcurrentFragment).add(R.id.contentframe, to).commit();
        }
        mcurrentFragment = to;
//        图标变化
        if (mClass == HomeFragment.class) {
            scalesmall();
            mcurrentIV = mIvHome;
            mcurrentTV = mTextHome;
            scalelarge();
        } else if (mClass == ChatFragment.class) {
            scalesmall();
            mcurrentIV = mIvChat;
            mcurrentTV = mTextChat;
            scalelarge();
        } else if (mClass == SearchFragment.class) {
            scalesmall();
            mcurrentIV = mIvSearch;
            mcurrentTV = mTextSearch;
            scalelarge();
        } else if (mClass == PersonalFragment.class) {
            scalesmall();
            mcurrentIV = mIvPerson;
            mcurrentTV = mTextPerson;
            scalelarge();
        }
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
    private Fragment createFragment(Class<?> mClass) {
        String mName = mClass.getName();
        Fragment mInstance = null;
        if (mFragments.containsKey(mName)) {
            mInstance = mFragments.get(mName);
        } else {
            try {
                mInstance = (Fragment) Class.forName(mName).newInstance();
            } catch (Exception mE) {
                mE.printStackTrace();
            }
            mFragments.put(mName, mInstance);
        }
        return mInstance;
    }

    // FIXME: 2017/3/3 当还没创建chatFragment的时候无法刷新

   public void newMsg(){
       // TODO: 2017/3/2 更新新消息UI
       runOnUiThread(new Runnable() {
           @Override
           public void run() {
               Toast.makeText(MainActivity.this, "收到新的消息", Toast.LENGTH_SHORT).show();
               try {
                   ((ChatFragment) mFragments.get(ChatFragment.class.getName())).onrefreshConversation();
               } catch (Exception mE) {
                   mE.printStackTrace();
               }
           }
       });
   }

    @Override
    protected void onResume() {
        super.onResume();
        IMHelper.getInstance().pushActivity(this);
        EMClient.getInstance().chatManager().addMessageListener(mIMMessageListenerMain);
    }

    @Override
    protected void onStop() {
        super.onStop();
        IMHelper.getInstance().popActivity(this);
        EMClient.getInstance().chatManager().removeMessageListener(mIMMessageListenerMain);
    }
}
