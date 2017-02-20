package com.grandmagic.readingmate.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.grandmagic.readingmate.R;
import com.grandmagic.readingmate.base.AppBaseActivity;
import com.grandmagic.readingmate.base.AppBaseResponseCallBack;
import com.grandmagic.readingmate.model.VerifyModel;
import com.grandmagic.readingmate.utils.AutoUtils;
import com.grandmagic.readingmate.utils.KitUtils;
import com.grandmagic.readingmate.utils.ViewUtils;
import com.tamic.novate.NovateResponse;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ForgetPassActivity extends AppBaseActivity {
    private static final int FORGET = 1;
    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.et_phone)
    EditText mEtPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pass);
        setTranslucentStatus(true);
        AutoUtils.auto(this);
        ButterKnife.bind(this);
        initview();
    }

    private void initview() {
        mTitle.setText(R.string.resetpass);
    }

    @OnClick({R.id.back, R.id.phone_clear, R.id.tv_next})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.phone_clear:
                mEtPhone.setText("");
                break;
            case R.id.tv_next:
                snedVerify();
                break;
        }
    }

    private void snedVerify() {
        //判断手机合法性
        final String phone_num = mEtPhone.getText().toString();
        if (KitUtils.checkMobilePhone(phone_num)) {
            new VerifyModel(ForgetPassActivity.this, new AppBaseResponseCallBack<NovateResponse<Object>>(ForgetPassActivity.this, true) {
                @Override
                public void onSuccee(NovateResponse<Object> response) {
                    Intent intent = new Intent(ForgetPassActivity.this,ResetPassActivity.class);
                    intent.putExtra("phone_num", phone_num);
                    startActivityForResult(intent, FORGET);
                }
            }).getVerifyCode(phone_num);
        } else {
            ViewUtils.showToast( getString(R.string.mobile_invalid));
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == FORGET && resultCode == ResetPassActivity.CHANG_SUCCESS) {
            //跳转到登录页
            ViewUtils.showToast( getString(R.string.reset_success));
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
