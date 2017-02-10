package com.grandmagic.readingmate.activity;

import android.animation.ObjectAnimator;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.grandmagic.readingmate.R;
import com.grandmagic.readingmate.base.AppBaseActivity;
import com.grandmagic.readingmate.fragment.ChatFragment;
import com.grandmagic.readingmate.fragment.HomeFragment;
import com.grandmagic.readingmate.fragment.PersonalFragment;
import com.grandmagic.readingmate.fragment.SearchFragment;
import com.grandmagic.readingmate.utils.KitUtils;
import com.grandmagic.readingmate.utils.UpdateManager;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
    Map<String, Fragment> mFragments;//存放fragment，如果以创建就从里面加载，不重复创建
    @BindView(R.id.iv_home)
    ImageView mIvHome;
    @BindView(R.id.iv_chat)
    ImageView mIvChat;
    @BindView(R.id.iv_search)
    ImageView mIvSearch;
    @BindView(R.id.iv_person)
    ImageView mIvPerson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initdata();
    }

    private void initdata() {
        mFragmentManager = getFragmentManager();
        mFragments = new HashMap<>();
        mcurrentFragment = mFragmentManager.findFragmentById(R.id.contentframe);
        if (mcurrentFragment == null) {//初始化homefragment
            Fragment mHomeFragment = createFragment(HomeFragment.class);
            mFragmentManager.beginTransaction().add(R.id.contentframe, mHomeFragment).show(mHomeFragment).commit();
            mcurrentFragment = mHomeFragment;
            mcurrentIV =mIvHome;
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
                checkVersion();
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
        if (mClass==HomeFragment.class){
            scalesmall();
            mcurrentIV=mIvHome;
            scalelarge();
        }else if (mClass==ChatFragment.class){
            scalesmall();
            mcurrentIV=mIvChat;
            scalelarge();
        }else if (mClass==SearchFragment.class){
            scalesmall();
            mcurrentIV=mIvSearch;
            scalelarge();
        }else if (mClass==PersonalFragment.class){
            scalesmall();
            mcurrentIV=mIvPerson;
            scalelarge();
        }
    }

    /**
     * 选中项图标放大
     */
    private void scalelarge() {
        ObjectAnimator.ofFloat(mcurrentIV,"scaleX",1.0f,1.5f).start();
        ObjectAnimator.ofFloat(mcurrentIV,"scaleY",1.0f,1.5f).start();
    }

    /**
     * 切换选中项时之前选中项图标缩回原图
     */
    private void scalesmall() {
        ObjectAnimator.ofFloat(mcurrentIV,"scaleX",1.5f,1.0f).start();
        ObjectAnimator.ofFloat(mcurrentIV,"scaleY",1.5f,1.0f).start();
    }

    /**
     * 创建或获取选中项的fragment
     * @param mClass
     * @return
     */
    private Fragment createFragment(Class<?> mClass) {
        String mName = mClass.getName();
        Fragment mInstance = null;
        if (mFragments.containsKey(mName)) {
            mInstance = mFragments.get(mName);
        }else {
            try {
                mInstance = (Fragment) Class.forName(mName).newInstance();
            } catch (Exception mE) {
                mE.printStackTrace();
            }
            mFragments.put(mName, mInstance);
        }
        return mInstance;
    }
}
