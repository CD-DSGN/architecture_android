package com.grandmagic.readingmate.activity;


import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.view.ViewPager;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.grandmagic.readingmate.R;
import com.grandmagic.readingmate.adapter.CommonPagerAdapter;
import com.grandmagic.readingmate.base.AppBaseActivity;
import com.grandmagic.readingmate.base.AppBaseResponseCallBack;
import com.grandmagic.readingmate.bean.request.LoginRequestBean;
import com.grandmagic.readingmate.bean.request.RegisterRequestBean;
import com.grandmagic.readingmate.bean.response.LoginResponseBean;
import com.grandmagic.readingmate.model.LoginModel;
import com.grandmagic.readingmate.model.RegisterModel;
import com.grandmagic.readingmate.model.VerifyModel;
import com.grandmagic.readingmate.utils.AutoUtils;
import com.grandmagic.readingmate.utils.DensityUtil;
import com.grandmagic.readingmate.utils.InputMethodUtils;
import com.grandmagic.readingmate.utils.KitUtils;
import com.tamic.novate.util.SPUtils;
import com.grandmagic.readingmate.utils.ViewUtils;
import com.tamic.novate.NovateResponse;

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
    ImageView mPhoneClear, mIVLogo;
    ImageView mPassEye;
    Button mLogin;
    TextView mForgetpass;
    //act view
    View dashline_register, dashline_login;
    RelativeLayout rela_register, rela_login;
    LinearLayout mActivityMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTranslucentStatus(true);//状态栏透明（APi19+）
        AutoUtils.auto(this);
        initview();
        keybordlistener();
    }

    private void keybordlistener() {
        mActivityMain.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect mRect = new Rect();
                mActivityMain.getWindowVisibleDisplayFrame(mRect);
                int keyHeight = DensityUtil.getScreenHeight(LoginActivity.this) - (mRect.bottom - mRect.top);
                mIVLogo.setVisibility(keyHeight > 200 ? View.GONE : View.VISIBLE);
            }
        });
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
        mViewpager.setAdapter(new CommonPagerAdapter(mViews));
        mViewpager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                dashline_register.setVisibility(position == 0 ? View.VISIBLE : View.GONE);
                dashline_login.setVisibility(position == 1 ? View.VISIBLE : View.GONE);
            }
        });
        mViewpager.setCurrentItem(1);
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
        mActivityMain = (LinearLayout) findViewById(R.id.activity_main);
        mIVLogo = (ImageView) findViewById(R.id.iv_logo);
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
                String phone_num = mEtPhone.getText().toString();
                String pwd = mEtPass.getText().toString();
                login(phone_num,pwd);
                break;
            case R.id.phone_clear:
                mEtPhone.setText("");
                break;
            case R.id.forgetpass:
                startActivity(new Intent(LoginActivity.this, ForgetPassActivity.class));
                break;
            case R.id.send_verify:
                mSendVerify.setEnabled(false);
                mCountDownTimer.start();
                snedVerify();
                break;
            case R.id.rg_phone_clear:
                mEtPhoneRg.setText("");
                break;
            case R.id.register_rg:
                register();
//                new HintDialog(LoginActivity.this, "待实现");
                break;
            case R.id.eye_pass_rg:
                setPasswordVisable(mEtPassRg, mEyePassRg);

                break;
            case R.id.eye_pass_sure_rg:
                setPasswordVisable(mEtPasssureRg, mEyePassSureRg);
                break;

        }
    }

    /**
     * 登陆方法
     * @param phone_num 手机
     * @param pwd 密码
     */
    private void login(final String phone_num, final String pwd) {


        LoginRequestBean loginRequestBean = new LoginRequestBean(phone_num, pwd);
        new LoginModel(LoginActivity.this, loginRequestBean, new AppBaseResponseCallBack<NovateResponse<LoginResponseBean>>(LoginActivity.this, true) {
            @Override
            public void onSuccee(NovateResponse<LoginResponseBean> response) {

                String token = null;
                if (response.getData() != null) {
                    token = response.getData().getAccess_token();
                }
                //保存token
                SPUtils.getInstance().saveToken(LoginActivity.this, token);
                SPUtils.getInstance().putString(LoginActivity.this, SPUtils.INFO_NAME,phone_num);
                SPUtils.getInstance().putString(LoginActivity.this, SPUtils.INFO_PWD,pwd);
                SPUtils.getInstance().putString(LoginActivity.this, SPUtils.IM_NAME,response.getData().getUsername());
                SPUtils.getInstance().putString(LoginActivity.this, SPUtils.IM_PWD,response.getData().getPassword());
                SPUtils.getInstance().saveToken(LoginActivity.this, token);
                //跳转到首页
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                InputMethodUtils.hide(LoginActivity.this);
                startActivity(intent);
                finish();
            }
        }).login();
    }

    private void register() {
        final String phone_num = mEtPhoneRg.getText().toString();
        String verify_code = mEtVerify.getText().toString();

        final String pwd = mEtPassRg.getText().toString();
        String pwd_comfirm = mEtPasssureRg.getText().toString();

        RegisterRequestBean registerBean = new RegisterRequestBean(pwd, phone_num, verify_code);
        new RegisterModel(LoginActivity.this, registerBean, new AppBaseResponseCallBack<NovateResponse<String>>
                (LoginActivity.this, true) {
            @Override
            public void onSuccee(NovateResponse<String> response) {
            login(phone_num,pwd);//改为注册成功调用登陆接口，不返回token
            }
        }, pwd_comfirm).register();
    }

    private void snedVerify() {
        //判断手机合法性
        String phone_num = mEtPhoneRg.getText().toString();
        if (KitUtils.checkMobilePhone(phone_num)) {
            new VerifyModel(LoginActivity.this, new AppBaseResponseCallBack<NovateResponse<Object>>(LoginActivity.this) {
                @Override
                public void onSuccee(NovateResponse<Object> response) {

                }
            }).getVerifyCode(phone_num);
        } else {
            ViewUtils.showToast( getString(R.string.mobile_invalid));
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

    CountDownTimer mCountDownTimer = new CountDownTimer(60000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            mSendVerify.setText((millisUntilFinished) / 1000 + "s重新发送");
        }

        @Override
        public void onFinish() {
            mSendVerify.setText("发送验证码");
            mSendVerify.setEnabled(true);
        }
    };
}
