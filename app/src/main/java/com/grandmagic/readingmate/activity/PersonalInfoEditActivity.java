package com.grandmagic.readingmate.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.grandmagic.readingmate.R;
import com.grandmagic.readingmate.base.AppBaseActivity;
import com.grandmagic.readingmate.utils.AutoUtils;
import com.grandmagic.readingmate.view.CircleImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PersonalInfoEditActivity extends AppBaseActivity {

    @BindView(R.id.back)
    ImageView mBack;
    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.iv_avar)
    CircleImageView mIvAvar;
    @BindView(R.id.tv_nickname)
    TextView mTvNickname;
    @BindView(R.id.tv_signature)
    TextView mTvSignature;
    @BindView(R.id.iv_gender)
    ImageView mIvGender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info_edit);
        ButterKnife.bind(this);
        AutoUtils.auto(this);
        initView();
    }

    private void initView() {
        mTitle.setText(getString(R.string.edit_info));
    }


    @OnClick({R.id.back, R.id.iv_avar, R.id.tv_nickname, R.id.tv_signature, R.id.iv_gender})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.iv_avar:
                break;
            case R.id.tv_nickname:
                break;
            case R.id.tv_signature:
                break;
            case R.id.iv_gender:
                break;
        }
    }
}
