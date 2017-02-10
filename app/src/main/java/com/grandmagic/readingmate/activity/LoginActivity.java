package com.grandmagic.readingmate.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.grandmagic.readingmate.R;
import com.grandmagic.readingmate.adapter.LoginPagerAdapter;
import com.grandmagic.readingmate.base.AppBaseActivity;
import com.grandmagic.readingmate.dialog.HintDialog;
import com.grandmagic.readingmate.model.VerifyModel;
import com.grandmagic.readingmate.utils.AutoUtils;
import com.grandmagic.readingmate.utils.KitUtils;
import com.grandmagic.readingmate.utils.ViewUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

//登录
public class LoginActivity extends AppBaseActivity implements View.OnClickListener {


    ViewPager mViewpager;
    @BindView(R.id.et_phone_rg)
    EditText mEtPhoneRg;
    @BindView(R.id.et_verify)
    EditText mEtVerify;
    @BindView(R.id.et_pass_rg)
    EditText mEtPassRg;
    @BindView(R.id.et_passsure_rg)
    EditText mEtPasssureRg;
    @BindView(R.id.register_rg)
    Button mRegisterRg;
    @BindView(R.id.rg_phone_clear)
    ImageView mRgPhoneClear;
    @BindView(R.id.eye_pass_rg)
    ImageView mEyePassRg;
    @BindView(R.id.send_verify)
    TextView mSendVerify;
    @BindView(R.id.eye_pass_sure_rg)
    ImageView mEyePassSureRg;
    //login view

    EditText mEtPhone, mEtPass;
    ImageView mPhoneClear;
    ImageView mPassEye;
    Button mLogin;
    TextView mForgetpass;
    //act view
    View dashline_register, dashline_login;
    RelativeLayout rela_register, rela_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTranslucentStatus(true);//状态栏透明（APi19+）
        AutoUtils.auto(this);
        initview();
    }

    List<View> mViews = new ArrayList<>();

    private void initview() {
        View mview_register = LayoutInflater.from(this).inflate(R.layout.view_register, null);
        View mview_login = LayoutInflater.from(this).inflate(R.layout.view_login, null);
        ButterKnife.bind(this, mview_register);
        initLoginView(mview_login);
        AutoUtils.auto(mview_login);
        AutoUtils.auto(mview_register);
        mViews.add(mview_register);
        mViews.add(mview_login);
        mViewpager.setAdapter(new LoginPagerAdapter(mViews));
        mViewpager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                dashline_register.setVisibility(position == 0 ? View.VISIBLE : View.GONE);
                dashline_login.setVisibility(position == 1 ? View.VISIBLE : View.GONE);
            }
        });
    }

    private void initLoginView(View mView) {
        mEtPhone = (EditText) mView.findViewById(R.id.et_phone);
        mEtPass = (EditText) mView.findViewById(R.id.et_pass);
        mPhoneClear = (ImageView) mView.findViewById(R.id.phone_clear);
        mPassEye = (ImageView) mView.findViewById(R.id.pass_eye);
        mLogin = (Button) mView.findViewById(R.id.login);
        mForgetpass = (TextView) mView.findViewById(R.id.forgetpass);
        mViewpager = (ViewPager) findViewById(R.id.viewpager);
        rela_register = (RelativeLayout) findViewById(R.id.relativelayout_register);
        rela_login = (RelativeLayout) findViewById(R.id.rela_login);
        dashline_login = findViewById(R.id.line_login);
        dashline_register = findViewById(R.id.line_register);
        mForgetpass.setOnClickListener(this);
        mLogin.setOnClickListener(this);
        mPhoneClear.setOnClickListener(this);
        mPassEye.setOnClickListener(this);
        mRgPhoneClear.setOnClickListener(this);
        mRegisterRg.setOnClickListener(this);
        mEyePassRg.setOnClickListener(this);
        mEyePassSureRg.setOnClickListener(this);
        mSendVerify.setOnClickListener(this);
        rela_login.setOnClickListener(this);
        rela_register.setOnClickListener(this);
    }


    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rela_login:
                mViewpager.setCurrentItem(1);
                break;
            case R.id.relativelayout_register:
                mViewpager.setCurrentItem(0);
                break;
            case R.id.pass_eye:
                setPasswordVisable(mEtPass, mPassEye);
                break;
            case R.id.login:
                // TODO: 2017/2/9 登陆
                break;
            case R.id.phone_clear:
                mEtPhone.setText("");
                break;
            case R.id.forgetpass:
               startActivity(new Intent(LoginActivity.this,ForgetPassActivity.class));
                break;
            case R.id.send_verify:
                // TODO: 2017/2/9 请求验证码
                break;
            case R.id.rg_phone_clear:
                mEtPhoneRg.setText("");
                break;
            case R.id.register_rg:
// TODO: 2017/2/9 zhuce
                new HintDialog(LoginActivity.this, "待实现");
                break;
            case R.id.eye_pass_rg:
                setPasswordVisable(mEtPassRg, mEyePassRg);

                break;
            case R.id.eye_pass_sure_rg:
                setPasswordVisable(mEtPasssureRg, mEyePassSureRg);
                break;

        }
    }


    private void snedVerify() {
        String phone_num = mEtPhoneRg.getText().toString();
        if (KitUtils.checkMobilePhone(phone_num)) {
            VerifyModel.getVerifyCode(this, phone_num);
        }else{
            ViewUtils.showToast(this, getString(R.string.mobile_invalid));
        }
    }


    /**
     * 密码显示切换
     *
     * @param et  输入框
     * @param eye 图标
     */
    private void setPasswordVisable(EditText et, ImageView eye) {
        eye.setSelected(!eye.isSelected());
        int selection = et.getSelectionEnd();
        et.setTransformationMethod(eye.isSelected() ?
                PasswordTransformationMethod.getInstance() : null);
        et.setSelection(selection);
    }
}
