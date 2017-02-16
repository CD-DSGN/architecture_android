package com.grandmagic.readingmate.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.grandmagic.readingmate.R;
import com.grandmagic.readingmate.activity.LoginActivity;
import com.grandmagic.readingmate.base.AppBaseFragment;
import com.grandmagic.readingmate.base.AppBaseResponseCallBack;
import com.grandmagic.readingmate.model.LoginModel;
import com.grandmagic.readingmate.utils.ViewUtils;
import com.tamic.novate.NovateResponse;
import com.tamic.novate.Throwable;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class PersonalFragment extends AppBaseFragment {
    @BindView(R.id.logout)
    Button mLogout;
    private Context mContext;

    public PersonalFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mContext = this.getActivity();
        View view = inflater.inflate(R.layout.fragment_personal, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    private void logout() {
        //先弹对话框，要求用户确认是否退出，如果真的要退出，才执行以下操作
        //无论成功失败,直接跳转登录页,也不需要提示用户额外的错误信息
        new LoginModel(mContext, null,
                new AppBaseResponseCallBack<NovateResponse<Object>>(mContext, true) {
                    @Override
                    public void onSuccee(NovateResponse<Object> response) {
                        ViewUtils.showToast(mContext,
                                "退出成功");
                        jumpToLoginActivity();
                    }

                    @Override
                    public void onError(Throwable e) {
                        jumpToLoginActivity();
                    }
                }).logout();
    }


    private void jumpToLoginActivity() {
        Intent intent = new Intent(mContext,
                LoginActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.logout)
    public void onClick(View v) {
        //退出账户
        switch (v.getId()) {
            case R.id.logout:
                logout();
                break;
            default:
                break;
        }
    }
}
