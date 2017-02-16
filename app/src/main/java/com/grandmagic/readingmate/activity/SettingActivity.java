package com.grandmagic.readingmate.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.TextView;

import com.grandmagic.readingmate.R;
import com.grandmagic.readingmate.base.AppBaseActivity;
import com.grandmagic.readingmate.base.AppBaseResponseCallBack;
import com.grandmagic.readingmate.bean.request.FeedBackRequestBean;
import com.grandmagic.readingmate.model.FeedBackModel;
import com.grandmagic.readingmate.model.LoginModel;
import com.grandmagic.readingmate.utils.KitUtils;
import com.grandmagic.readingmate.utils.SPUtils;
import com.grandmagic.readingmate.utils.ViewUtils;
import com.tamic.novate.NovateResponse;
import com.tamic.novate.Throwable;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingActivity extends AppBaseActivity {
    @BindView(R.id.feed_back)
    TextView mFeedBack;
    @BindView(R.id.logout)
    TextView mLogout;
    @BindView(R.id.tv_setting_current_version)
    TextView mTvSettingCurrentVersion;
    @BindView(R.id.switch_setting_push)
    SwitchCompat mSwitchSettingPush;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        initView();
        loadData();
    }

    //完成一些网络数据的获取及相应view的更新
    private void loadData() {


    }

    private void initView() {
        //设置版本信息
        String Version_name = KitUtils.getVersionName(SettingActivity.this);
        mTvSettingCurrentVersion.setText(Version_name);
        //根据sp信息，设置推送通知的打开和关闭状态
        //1.sp没有相关信息，默认为打开状态
        boolean checked = SPUtils.getInstance().getPushSetting(SettingActivity.this);
        mSwitchSettingPush.setChecked(checked);
    }

    private void logout() {
        //先弹对话框，要求用户确认是否退出，如果真的要退出，才执行以下操作
        //无论成功失败,直接跳转登录页,也不需要提示用户额外的错误信息
        new LoginModel(SettingActivity.this, null,
                new AppBaseResponseCallBack<NovateResponse<Object>>(SettingActivity.this, true) {
                    @Override
                    public void onSuccee(NovateResponse<Object> response) {
                        ViewUtils.showToast(SettingActivity.this,
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
        Intent intent = new Intent(SettingActivity.this,
                LoginActivity.class);
        startActivity(intent);
    }


    //处理用户反馈
    private void feedback() {
        new FeedBackModel(SettingActivity.this, new FeedBackRequestBean("缺少某功能"),
                new AppBaseResponseCallBack<NovateResponse<Object>>(SettingActivity.this, true) {
                    @Override
                    public void onSuccee(NovateResponse<Object> response) {
                        ViewUtils.showToast(SettingActivity.this, getString(R.string.feedback_success));
                    }
                }).feedBack();
    }

    @OnClick({R.id.feed_back, R.id.logout, R.id.switch_setting_push})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.feed_back:
                feedback();
                break;
            case R.id.logout:
                logout();
                break;
            case R.id.switch_setting_push:
                //保存开关状态
                SPUtils.getInstance().setPushSetting(SettingActivity.this, mSwitchSettingPush.isChecked());
            default:
                break;
        }
    }


}
