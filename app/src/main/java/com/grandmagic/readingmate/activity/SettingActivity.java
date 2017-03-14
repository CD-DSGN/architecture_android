package com.grandmagic.readingmate.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.grandmagic.readingmate.R;
import com.grandmagic.readingmate.base.AppBaseActivity;
import com.grandmagic.readingmate.base.AppBaseResponseCallBack;
import com.grandmagic.readingmate.model.LoginModel;
import com.grandmagic.readingmate.utils.AutoUtils;
import com.grandmagic.readingmate.utils.KitUtils;
import com.grandmagic.readingmate.utils.ViewUtils;
import com.tamic.novate.NovateResponse;
import com.tamic.novate.Throwable;
import com.tamic.novate.util.SPUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingActivity extends AppBaseActivity {
    @BindView(R.id.logout)
    TextView mLogout;
    @BindView(R.id.tv_setting_current_version)
    TextView mTvSettingCurrentVersion;


    @BindView(R.id.tv_setting_account)
    TextView mTvSettingAccount;
    @BindView(R.id.tv_setting_mobile)
    TextView mTvSettingMobile;
    @BindView(R.id.back)
    ImageView mBack;
    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.switch_setting_push)
    ImageButton mSwitchSettingPush;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        setTranslucentStatus(true);
        AutoUtils.auto(this);
        ButterKnife.bind(this);
        initView();
        loadData();
    }

    //完成一些网络数据的获取及相应view的更新
    private void loadData() {
        //1.获取绑定手机、账号、积分
        mTvSettingAccount.setText("多情剑客无情剑");
        mTvSettingMobile.setText("13518143569");
    }

    private void initView() {
        //设置版本信息
        //        mSwitchSettingPush = (SwitchCompat) findViewById(R.id.switch_setting_push);
        mTitle.setText(getString(R.string.settings));
        String Version_name = KitUtils.getVersionName(SettingActivity.this);
        mTvSettingCurrentVersion.setText(Version_name);
        //根据sp信息，设置推送通知的打开和关闭状态
        //1.sp没有相关信息，默认为打开状态
        boolean checked = SPUtils.getInstance().getPushSetting(SettingActivity.this);
        changeSwitch(checked);
    }

    private void changeSwitch(boolean checked) {
        if (checked) {
            mSwitchSettingPush.setBackgroundResource(R.drawable.ic_off);
        }else{
            mSwitchSettingPush.setBackgroundResource(R.drawable.ic_on);
        }
    }

    private void logout() {
        //先弹对话框，要求用户确认是否退出，如果真的要退出，才执行以下操作
        //无论成功失败,直接跳转登录页,也不需要提示用户额外的错误信息
        new LoginModel(SettingActivity.this, null,
                new AppBaseResponseCallBack<NovateResponse<Object>>(SettingActivity.this, true) {
                    @Override
                    public void onSuccee(NovateResponse<Object> response) {
                        ViewUtils.showToast(
                                getString(R.string.logout_success));
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

    @OnClick({R.id.logout, R.id.back, R.id.switch_setting_push})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.logout:
                logout();
                break;
            case R.id.back:
                finish();
            case R.id.switch_setting_push:
                boolean checked = SPUtils.getInstance().getPushSetting(SettingActivity.this);
                changeSwitch(!checked);
                //保存状态
                SPUtils.getInstance().setPushSetting(this, !checked);
            default:
                break;
        }
    }

}
