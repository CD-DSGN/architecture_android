package com.grandmagic.readingmate.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.grandmagic.readingmate.R;
import com.grandmagic.readingmate.base.AppBaseActivity;
import com.grandmagic.readingmate.base.AppBaseResponseCallBack;
import com.grandmagic.readingmate.bean.request.ResetPasswordRequsetBean;
import com.grandmagic.readingmate.model.ResetPasswordModel;
import com.grandmagic.readingmate.utils.AutoUtils;
import com.grandmagic.readingmate.utils.ViewUtils;
import com.tamic.novate.NovateResponse;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ResetPassActivity extends AppBaseActivity {
    public static final int CHANG_SUCCESS = 1;

    @BindView(R.id.back)
    ImageView mBack;
    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.et_verify)
    EditText mEtVerify;
    @BindView(R.id.eye_pass)
    ImageView mEyePass;
    @BindView(R.id.et_pass)
    EditText mEtPass;
    @BindView(R.id.et_passsure)
    EditText mEtPasssure;
    @BindView(R.id.eye_pass_sure)
    ImageView mEyePassSure;
    @BindView(R.id.reset)
    TextView mReset;

    private String mPhoneNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pass);
        AutoUtils.auto(this);
        setTranslucentStatus(true);
        ButterKnife.bind(this);
        initview();
        initData();
    }

    private void initData() {
        mPhoneNum = getIntent().getStringExtra("phone_num");
    }

    private void initview() {
        mTitle.setText(R.string.resetpass);
        mTitle.setTextColor(Color.parseColor("#ffffff"));
    }

    @OnClick({R.id.back, R.id.eye_pass, R.id.eye_pass_sure, R.id.reset})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.eye_pass:
                setPasswordVisable(mEtPass,mEyePass);
                break;
            case R.id.eye_pass_sure:
                setPasswordVisable(mEtPasssure,mEyePassSure);
                break;
            case R.id.reset:
                resetPassWord();
                break;
        }
    }

    private void resetPassWord() {
        String verify = mEtVerify.getText().toString();
        String pwd = mEtPasssure.getText().toString();
        String pwd_confirm = mEtPass.getText().toString();

        ResetPasswordRequsetBean resetPasswordRequsetBean =new ResetPasswordRequsetBean(pwd, mPhoneNum, verify);
        new ResetPasswordModel(ResetPassActivity.this, resetPasswordRequsetBean,
                new AppBaseResponseCallBack<NovateResponse<Object>>(ResetPassActivity.this, true) {
            @Override
            public void onSuccee(NovateResponse<Object> response) {
                ResetPassActivity.this.setResult(CHANG_SUCCESS);
                finish();
            }
        }, pwd_confirm).resetPassword();
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
