package com.grandmagic.readingmate.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.grandmagic.readingmate.R;
import com.grandmagic.readingmate.base.AppBaseActivity;
import com.grandmagic.readingmate.utils.AutoUtils;
import com.grandmagic.readingmate.utils.ImageLoader;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddFriendActivity extends AppBaseActivity {

    @BindView(R.id.back)
    ImageView mBack;
    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.titlelayout)
    RelativeLayout mTitlelayout;
    @BindView(R.id.iv_search)
    ImageView mIvSearch;
    @BindView(R.id.et_search)
    EditText mEtSearch;
    @BindView(R.id.avatar)
    ImageView mAvatar;
    @BindView(R.id.name)
    TextView mName;
    @BindView(R.id.iv_addfriend)
    ImageView mIvAddfriend;

    @BindView(R.id.rela_sameInterest)
    RelativeLayout mRelaSameInterest;
    @BindView(R.id.rela_friend)
    RelativeLayout mRelaFriend;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        ButterKnife.bind(this);
        AutoUtils.auto(this);
        setTranslucentStatus(true);
        initview();
    }

    private void initview() {
        mTitle.setText("添加家友");
        ImageLoader.loadCircleImage(this, "https://img6.bdstatic.com/img/image/smallpic/dongman.jpg"
                , mAvatar);
    }


    @OnClick({R.id.back, R.id.rela_friend, R.id.iv_addfriend})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.rela_friend:
                startActivity(new Intent(AddFriendActivity.this, FriendDetailActivity.class));
                break;
            case R.id.iv_addfriend:
                // TODO: 2017/2/28 添加好友
                break;
        }
    }
}
