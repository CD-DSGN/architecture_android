package com.grandmagic.readingmate.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.grandmagic.readingmate.R;
import com.grandmagic.readingmate.base.AppBaseActivity;
import com.grandmagic.readingmate.base.AppBaseResponseCallBack;
import com.grandmagic.readingmate.bean.response.SearchUserResponse;
import com.grandmagic.readingmate.model.SearchUserModel;
import com.grandmagic.readingmate.utils.AutoUtils;
import com.grandmagic.readingmate.utils.IMHelper;
import com.grandmagic.readingmate.utils.ImageLoader;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.orhanobut.logger.Logger;
import com.tamic.novate.NovateResponse;
import com.tamic.novate.util.Environment;
import com.tamic.novate.util.SPUtils;

import java.util.List;

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
        initlisenter();
    }

    private void initlisenter() {
        mEtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                String mString = v.getText().toString();
                if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                        (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    search(mString);
                    return true;
                }
                return false;
            }
        });
    }

    /**
     * 从服务端查询用户
     *
     * @param mString
     */
    SearchUserModel mModel;

    private void search(String mString) {
        mRelaFriend.setVisibility(View.GONE);
        mModel = new SearchUserModel(this);
        mModel.searchUser(mString, new AppBaseResponseCallBack<NovateResponse<SearchUserResponse>>(this) {
            @Override
            public void onSuccee(NovateResponse<SearchUserResponse> response) {
                SearchUserResponse responseData = response.getData();
                    setUserView(responseData);
                }
        });
    }
    String mUser_id;
    private void setUserView(SearchUserResponse mSearchUserResponse) {
        mRelaFriend.setVisibility(View.VISIBLE);
        ImageLoader.loadCircleImage(this, Environment.BASEULR_PRODUCTION+mSearchUserResponse.getAvatar_native(), mAvatar);
        mName.setText(mSearchUserResponse.getUser_name());
         mUser_id = mSearchUserResponse.getUser_id();
        if (mUser_id.equals(SPUtils.getInstance().getString(this,SPUtils.IM_NAME))
                ){
          mIvAddfriend.setVisibility(View.GONE);

        }
        if (IMHelper.getInstance().getUserInfo(mUser_id)!=null){
            mIvAddfriend.setVisibility(mUser_id.equals(SPUtils.getInstance().getString(this,SPUtils.IM_NAME))//如果搜索的是自己
                    || IMHelper.getInstance().getUserInfo(mUser_id)!=null? View.GONE:View.VISIBLE);//或这已经是好友关系
        }

    }

    private void initview() {
        mTitle.setText("添加家友");
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
                addContact();
                break;
        }
    }

    /**
     * 添加好友，
     */
    private void addContact() {
        if (mUser_id.equals(SPUtils.getInstance().getString(this,SPUtils.IM_NAME))){
            Toast.makeText(this, "不能添加自己为好友", Toast.LENGTH_SHORT).show();
            return;
        }
        if (IMHelper.getInstance().getUserInfo(mUser_id)!=null){
            Toast.makeText(this, "你们已经是好友了,不能再添加", Toast.LENGTH_SHORT).show();
        }
        try {
            EMClient.getInstance().contactManager().addContact(mUser_id,"加个好友呗");
        } catch (HyphenateException mE) {
            Logger.e(mE.getMessage());
            mE.printStackTrace();
        }
    }
}
