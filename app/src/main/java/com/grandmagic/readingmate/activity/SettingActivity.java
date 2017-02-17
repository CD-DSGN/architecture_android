package com.grandmagic.readingmate.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.grandmagic.readingmate.R;
import com.grandmagic.readingmate.base.AppBaseActivity;
import com.grandmagic.readingmate.base.AppBaseResponseCallBack;
import com.grandmagic.readingmate.bean.request.FeedBackRequestBean;
import com.grandmagic.readingmate.model.FeedBackModel;
import com.grandmagic.readingmate.model.LoginModel;
import com.grandmagic.readingmate.ui.CustomDialog;
import com.grandmagic.readingmate.utils.KitUtils;
import com.grandmagic.readingmate.utils.SPUtils;
import com.grandmagic.readingmate.utils.ViewUtils;
import com.tamic.novate.NovateResponse;
import com.tamic.novate.Throwable;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingActivity extends AppBaseActivity implements CompoundButton.OnCheckedChangeListener {
    @BindView(R.id.feed_back)
    TextView mFeedBack;
    @BindView(R.id.logout)
    TextView mLogout;
    @BindView(R.id.tv_setting_current_version)
    TextView mTvSettingCurrentVersion;

    SwitchCompat mSwitchSettingPush;
    @BindView(R.id.test)
    Button mTest;
    @BindView(R.id.tv_setting_account)
    TextView mTvSettingAccount;
    @BindView(R.id.tv_setting_mobile)
    TextView mTvSettingMobile;
    @BindView(R.id.tv_seeting_points)
    TextView mTvSeetingPoints;


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
        //1.获取绑定手机、账号、积分
        mTvSettingAccount.setText("多情剑客无情剑");
        mTvSettingMobile.setText("13518143569");
        mTvSeetingPoints.setText("2000");
    }

    private void initView() {
        //设置版本信息
        mSwitchSettingPush = (SwitchCompat) findViewById(R.id.switch_setting_push);
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


    //处理用户反馈
    private void feedback() {
        //显示dialog
        showFeedBackDialog();

    }

    private void sendfeedBackNetworkData(String content) {
        new FeedBackModel(SettingActivity.this, new FeedBackRequestBean(content),
                new AppBaseResponseCallBack<NovateResponse<Object>>(SettingActivity.this, true) {
                    @Override
                    public void onSuccee(NovateResponse<Object> response) {
                        ViewUtils.showToast(SettingActivity.this, getString(R.string.feedback_success));
                    }
                }).feedBack();
    }

    private void showFeedBackDialog() {
        final CustomDialog customDialog = new CustomDialog(this);
        customDialog.setTitle(getString(R.string.your_feedback));
        customDialog.setMaxNum(300).setOnBtnOnclickListener(new CustomDialog.BtnOnclickListener() {
            @Override
            public void onYesClick() {
                String content = customDialog.getMessage();
                if (TextUtils.isEmpty(content)) {
                    ViewUtils.showToast(SettingActivity.this, getString(R.string.feedback_content_empty));
                    return;
                }else{
                    sendfeedBackNetworkData(content);
                }
                customDialog.dismiss();
            }

            @Override
            public void onNoClick() {
                customDialog.dismiss();
            }
        });
        customDialog.show();
    }


    @OnClick({R.id.feed_back, R.id.logout, R.id.test})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.feed_back:
                feedback();
                break;
            case R.id.logout:
                logout();
                break;
            case R.id.test:
                boolean checked = SPUtils.getInstance().getPushSetting(SettingActivity.this);
                ViewUtils.showToast(SettingActivity.this, "checked:" + checked);
                break;
            default:
                break;
        }
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView.getId() == R.id.switch_setting_push) {
            //        保存开关状态
            ViewUtils.showToast(SettingActivity.this, "开关状态" + isChecked);
            SPUtils.getInstance().setPushSetting(SettingActivity.this, isChecked);
        }
    }
}
