package com.grandmagic.readingmate.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.grandmagic.readingmate.R;
import com.grandmagic.readingmate.activity.CollectionActivity;
import com.grandmagic.readingmate.activity.PersonalInfoEditActivity;
import com.grandmagic.readingmate.activity.SettingActivity;
import com.grandmagic.readingmate.adapter.MyCommentAdapter;
import com.grandmagic.readingmate.base.AppBaseActivity;
import com.grandmagic.readingmate.base.AppBaseFragment;
import com.grandmagic.readingmate.utils.AutoUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class PersonalFragment extends AppBaseFragment {
    Context mContext;
    @BindView(R.id.rv_my_comments)
    RecyclerView mRvMyComments;
    @BindView(R.id.tv_edit_personal_info)
    TextView mTvEditPersonalInfo;
    @BindView(R.id.ll_collect)
    LinearLayout mLlCollect;
    @BindView(R.id.ll_setting)
    LinearLayout mLlSetting;


    public PersonalFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_personal, container, false);
        AutoUtils.auto(view);
        ButterKnife.bind(this, view);
        mContext = getActivity();
        initView();
        return view;
    }

    private void initView() {
        mRvMyComments.setLayoutManager(new LinearLayoutManager(mContext));
        ArrayList<String> data = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            data.add("电影不错");
            data.add("结局缺乏新意，其他没什么");
        }
        mRvMyComments.setAdapter(new MyCommentAdapter(mContext, data));
    }


    @OnClick({R.id.tv_edit_personal_info, R.id.ll_collect, R.id.ll_setting})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_edit_personal_info:
                //跳转到个人设置页面
                Intent intent_edit = new Intent(mContext, PersonalInfoEditActivity.class);
                startActivity(intent_edit);
                break;
            case R.id.ll_collect:
                Intent intent_collection = new Intent(mContext, CollectionActivity.class);
                startActivity(intent_collection);
                break;
            case R.id.ll_setting:
                Intent intent_setting = new Intent(mContext, SettingActivity.class);
                startActivity(intent_setting);
                break;
            default:
                break;
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        setSystemBarColor(false);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        setSystemBarColor(hidden);
    }

    private void setSystemBarColor(boolean hidden) {
        if (!hidden) ((AppBaseActivity) (mContext)).setSystemBarColor(android.R.color.white);
    }
}
