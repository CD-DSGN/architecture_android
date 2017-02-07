package com.grandmagic.readingmate.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.widget.ViewUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.grandmagic.readingmate.R;
import com.grandmagic.readingmate.base.AppBaseActivity;
import com.grandmagic.readingmate.fragment.ChatFragment;
import com.grandmagic.readingmate.fragment.HomeFragment;
import com.grandmagic.readingmate.fragment.PersonalFragment;
import com.grandmagic.readingmate.fragment.SearchFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    Map<String, Fragment> mFragments;

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
        if (mcurrentFragment == null) {
            Fragment mHomeFragment = createFragment(HomeFragment.class);
            mFragmentManager.beginTransaction().add(R.id.contentframe, mHomeFragment).show(mHomeFragment).commit();
            mcurrentFragment=mHomeFragment;
        }
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

    private void switchFragment(Class<?> mClass) {
        if (mClass == null) return;
        Fragment to = createFragment(mClass);
        if (to.isAdded()) {
            mFragmentManager.beginTransaction().hide(mcurrentFragment).show(to).commit();
        } else {
            mFragmentManager.beginTransaction().hide(mcurrentFragment).add(R.id.contentframe, to).commit();
        }
        mcurrentFragment = to;
    }

    private Fragment createFragment(Class<?> mClass) {
        String mName = mClass.getName();
        Fragment mInstance = null;
        if (mFragments.containsKey(mName)) {
            mInstance = mFragments.get(mName);
        }
        try {
            mInstance = (Fragment) Class.forName(mName).newInstance();
        } catch (InstantiationException mE) {
            mE.printStackTrace();
        } catch (IllegalAccessException mE) {
            mE.printStackTrace();
        } catch (ClassNotFoundException mE) {
            mE.printStackTrace();
        }
        mFragments.put(mName, mInstance);
        return mInstance;
    }
}
