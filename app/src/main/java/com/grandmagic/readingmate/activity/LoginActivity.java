package com.grandmagic.readingmate.activity;


import android.os.Bundle;
import android.support.design.widget.CheckableImageButton;
import android.support.v4.view.ViewPager;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.grandmagic.readingmate.R;
import com.grandmagic.readingmate.adapter.LoginPagerAdapter;
import com.grandmagic.readingmate.base.AppBaseActivity;
import com.grandmagic.readingmate.dialog.HintDialog;
import com.grandmagic.readingmate.utils.AutoUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
    @BindView(R.id.eye_pass_sure_rg)
    ImageView mEyePassSureRg;
    //login view
    EditText mEtPhone, mEtPass;
    ImageView mPhoneClear;
    ImageView mPassEye;
    Button mLogin;
    TextView mForgetpass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
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
    }

    private void initLoginView(View mView) {
        mEtPhone = (EditText) mView.findViewById(R.id.et_phone);
        mEtPass = (EditText) mView.findViewById(R.id.et_pass);
        mPhoneClear = (ImageView) mView.findViewById(R.id.phone_clear);
        mPassEye = (ImageView) mView.findViewById(R.id.pass_eye);
        mLogin = (Button) mView.findViewById(R.id.login);
        mForgetpass = (TextView) mView.findViewById(R.id.forgetpass);
        mViewpager = (ViewPager) findViewById(R.id.viewpager);
        mForgetpass.setOnClickListener(this);
        mLogin.setOnClickListener(this);
        mPhoneClear.setOnClickListener(this);
        mPassEye.setOnClickListener(this);
        mRgPhoneClear.setOnClickListener(this);
        mRegisterRg.setOnClickListener(this);
        mEyePassRg.setOnClickListener(this);
        mEyePassSureRg.setOnClickListener(this);

    }


    public void onClick(View view) {
        switch (view.getId()) {
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
                // TODO: 2017/2/9 忘记密码
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

    private void setPasswordVisable(EditText et, ImageView eye) {
        eye.setSelected(!eye.isSelected());
        int selection = et.getSelectionEnd();
        et.setTransformationMethod(eye.isSelected() ?
                PasswordTransformationMethod.getInstance() : null);
        et.setSelection(selection);
    }
}
