package com.grandmagic.readingmate.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.grandmagic.readingmate.R;
import com.grandmagic.readingmate.base.AppBaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

//登录
public class LoginActivity extends AppBaseActivity {

    @BindView(R.id.et_phone)
    EditText mEtPhone;
    @BindView(R.id.phone_clear)
    ImageView mPhoneClear;
    @BindView(R.id.et_pass)
    EditText mEtPass;
    @BindView(R.id.login)
    Button mLogin;
    @BindView(R.id.forgetpass)
    TextView mForgetpass;
    @BindView(R.id.register)
    TextView mRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.phone_clear, R.id.login, R.id.forgetpass, R.id.register})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.phone_clear:
                mEtPhone.setText("");
                break;
            case R.id.login:
                // TODO: 2017/2/7 登lu验证
                startActivity(new Intent(LoginActivity.this,MainActivity.class));
                finish();
                break;
            case R.id.forgetpass:
                // TODO: 2017/2/7 忘记密码
                break;
            case R.id.register:
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                finish();
                break;
        }
    }
}
