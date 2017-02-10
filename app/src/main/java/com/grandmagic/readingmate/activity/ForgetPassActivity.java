package com.grandmagic.readingmate.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.grandmagic.readingmate.R;
import com.grandmagic.readingmate.base.AppBaseActivity;
import com.grandmagic.readingmate.utils.AutoUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ForgetPassActivity extends AppBaseActivity {
    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.et_phone)
    EditText mEtPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pass);
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
                // TODO: 2017/2/10 请求服务器发送验证码 成功后跳转
                startActivity(new Intent(ForgetPassActivity.this,ResetPassActivity.class));
                finish();
                break;
        }
    }
}
