package com.grandmagic.readingmate.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.grandmagic.readingmate.R;
import com.grandmagic.readingmate.activity.CollectionActivity;
import com.grandmagic.readingmate.activity.PersonalInfoEditActivity;
import com.grandmagic.readingmate.activity.SettingActivity;
import com.grandmagic.readingmate.adapter.MyCommentAdapter;
import com.grandmagic.readingmate.base.AppBaseActivity;
import com.grandmagic.readingmate.base.AppBaseFragment;
import com.grandmagic.readingmate.base.AppBaseResponseCallBack;
import com.grandmagic.readingmate.bean.response.ImageUrlResponseBean;
import com.grandmagic.readingmate.bean.response.UserInfoResponseBean;
import com.grandmagic.readingmate.model.UserInfoModel;
import com.grandmagic.readingmate.utils.AutoUtils;
import com.grandmagic.readingmate.utils.ImageLoader;
import com.grandmagic.readingmate.utils.KitUtils;
import com.grandmagic.readingmate.view.CircleImageView;
import com.tamic.novate.NovateResponse;

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


    @BindView(R.id.iv_frag_personal_header)
    CircleImageView mIvFragPersonalHeader;
    @BindView(R.id.tv_frag_personal_nickname)
    TextView mTvFragPersonalNickname;
    @BindView(R.id.tv_frag_personal_signature)
    TextView mTvFragPersonalSignature;
    @BindView(R.id.ic_frag_personal_male)
    ImageView mIcFragPersonalMale;
    @BindView(R.id.ic_frag_personal_female)
    ImageView mIcFragPersonalFemale;

    UserInfoModel mUserInfoModel;
    UserInfoResponseBean mUserInfoResponseBean = new UserInfoResponseBean();

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
        loadData();
        return view;
    }

    private void loadData() {
        //获取个人信息
        if (mUserInfoModel == null) {
            mUserInfoModel = new UserInfoModel(mContext, new AppBaseResponseCallBack<NovateResponse<UserInfoResponseBean>>(mContext, true) {
                @Override
                public void onSuccee(NovateResponse<UserInfoResponseBean> response) {
                    if (response != null && response.getData() != null) {
                        mUserInfoResponseBean = (UserInfoResponseBean) response.getData();
                        //设置相应的数据
                        setPersonalView();

                    }
                }
            });
        }
        mUserInfoModel.getUserInfo();

    }

    private void setPersonalView() {
        ImageUrlResponseBean imageUrlResponseBean = mUserInfoResponseBean.getAvatar_url();
        if ( imageUrlResponseBean != null) {
            String url = imageUrlResponseBean.getLarge();
            if (!TextUtils.isEmpty(url)) {
                ImageLoader.loadCircleImage(mContext, KitUtils.getAbsoluteUrl(url),
                        mIvFragPersonalHeader);
            }
        }

        if (!TextUtils.isEmpty(mUserInfoResponseBean.getSignature())) {
            mTvFragPersonalSignature.setText(mUserInfoResponseBean.getSignature());
        }

        if (!TextUtils.isEmpty(mUserInfoResponseBean.getUser_name())) {
            mTvFragPersonalNickname.setText(mUserInfoResponseBean.getUser_name());
        }

        setGenderView(mUserInfoResponseBean.getGender());
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
//                intent_edit.putExtra("personal_data", mUserInfoResponseBean);
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

    public void setGenderView(int genderView) {
        if (genderView == 1) {   //女
            mIcFragPersonalMale.setVisibility(View.GONE);
            mIcFragPersonalFemale.setVisibility(View.VISIBLE);
        }else if(genderView == 2){                  //男
            mIcFragPersonalMale.setVisibility(View.VISIBLE);
            mIcFragPersonalFemale.setVisibility(View.GONE);
        }else{  //未设置
            mIcFragPersonalFemale.setVisibility(View.VISIBLE);
            mIcFragPersonalMale.setVisibility(View.VISIBLE);
        }
    }
}
