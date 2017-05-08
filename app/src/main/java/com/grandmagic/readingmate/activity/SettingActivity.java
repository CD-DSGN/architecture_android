package com.grandmagic.readingmate.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.grandmagic.readingmate.R;
import com.grandmagic.readingmate.base.AppBaseActivity;
import com.grandmagic.readingmate.base.AppBaseResponseCallBack;
import com.grandmagic.readingmate.consts.AppConsts;
import com.grandmagic.readingmate.event.LogoutEvent;
import com.grandmagic.readingmate.model.LoginModel;
import com.grandmagic.readingmate.ui.CleanCacheDlg;
import com.grandmagic.readingmate.utils.AutoUtils;
import com.grandmagic.readingmate.utils.KitUtils;
import com.grandmagic.readingmate.utils.ViewUtils;
import com.grandmagic.readingmate.view.SharePopUpWindow;
import com.hyphenate.chat.EMClient;
import com.tamic.novate.NovateResponse;
import com.tamic.novate.Throwable;
import com.tamic.novate.util.SPUtils;

import org.greenrobot.eventbus.EventBus;

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
    @BindView(R.id.rl_clean_cache)
    RelativeLayout mRlCleanCache;
    public static final String USR_NAME = "USR_NAME";
    @BindView(R.id.iv_share_app)
    ImageView mIvShareApp;
    private String mUserName;

    private SharePopUpWindow mSharePopUpWindow;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        setTranslucentStatus(true);
        AutoUtils.auto(this);
        ButterKnife.bind(this);
        initData();
        initView();
    }

    private void initData() {
        mUserName = getIntent().getStringExtra(USR_NAME);
        mTvSettingAccount.setText(KitUtils.unicodeDecode(mUserName));
        //从sp获取手机号
        String tel = SPUtils.getInstance().getString(this, SPUtils.INFO_NAME);
        mTvSettingMobile.setText(tel);
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

        if (mSharePopUpWindow == null) {
            mSharePopUpWindow = new SharePopUpWindow(this);
        }
    }

    private void changeSwitch(boolean checked) {
        if (checked) {
            mSwitchSettingPush.setBackgroundResource(R.drawable.ic_off);
        } else {
            mSwitchSettingPush.setBackgroundResource(R.drawable.ic_on);
        }
    }

    private void logout() {
        //先弹对话框，要求用户确认是否退出，如果真的要退出，才执行以下操作
        //无论成功失败,直接跳转登录页,也不需要提示用户额外的错误信息
        EMClient.getInstance().logout(true);//环信的退出
        new LoginModel(SettingActivity.this, null,
                new AppBaseResponseCallBack<NovateResponse<Object>>(SettingActivity.this, true) {
                    @Override
                    public void onSuccee(NovateResponse<Object> response) {
                        ViewUtils.showToast(
                                getString(R.string.logout_success));
                        jumpToLoginActivity();
                        finish();
                        EventBus.getDefault().post(new LogoutEvent());
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


    @Override
    protected void onResume() {
        super.onResume();
        if (mSharePopUpWindow != null) {
            mSharePopUpWindow.dismissPorgressDlg();
        }
    }

    @OnClick({R.id.logout, R.id.back, R.id.switch_setting_push, R.id.rl_clean_cache, R.id.iv_share_app})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.logout:
                logout();
                break;
            case R.id.back:
                finish();
                break;
            case R.id.switch_setting_push:
                boolean checked = SPUtils.getInstance().getPushSetting(SettingActivity.this);
                changeSwitch(!checked);
                //保存状态
                SPUtils.getInstance().setPushSetting(this, !checked);
                break;
            case R.id.rl_clean_cache:
                CleanCacheDlg cleanCacheDlg = new CleanCacheDlg(this, false);
                cleanCacheDlg.show();
                break;
            case R.id.iv_share_app:
                mSharePopUpWindow.setData("大术读家", "分享阅读。获取海量图书资源，打造阅读圈子，纸书阅读新体验", R.drawable.logo_rect,
                        AppConsts.share_url, "大术读家:一款阅读交友软件，寻找和你兴趣一致的好友，一起读书吧！");
                mSharePopUpWindow.setSina(R.drawable.logo_rect,
                        "大术读家:一款阅读交友软件，寻找和你兴趣一致的好友，一起读书吧！(来自大术读家" + AppConsts.share_url);
                mSharePopUpWindow.show();
                break;
            default:
                break;
        }
    }
}
