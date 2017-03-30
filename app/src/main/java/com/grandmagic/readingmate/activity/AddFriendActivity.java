package com.grandmagic.readingmate.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.grandmagic.readingmate.R;
import com.grandmagic.readingmate.base.AppBaseActivity;
import com.grandmagic.readingmate.base.AppBaseResponseCallBack;
import com.grandmagic.readingmate.bean.response.PersonInfo;
import com.grandmagic.readingmate.bean.response.SearchUserResponse;
import com.grandmagic.readingmate.model.SearchUserModel;
import com.grandmagic.readingmate.ui.CustomDialog;
import com.grandmagic.readingmate.utils.AutoUtils;
import com.grandmagic.readingmate.utils.ImageLoader;
import com.grandmagic.readingmate.utils.InputMethodUtils;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.tamic.novate.NovateResponse;
import com.tamic.novate.util.Environment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddFriendActivity extends AppBaseActivity {

    private static final int NOFRIEND = 1;
    private static final int ISBOTHENJOY = 1;
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
    @BindView(R.id.hassamebook)
    TextView mHassamebook;
    @BindView(R.id.lin_collection)
    LinearLayout mLinCollection;
    @BindView(R.id.text1)
    TextView mText1;
    @BindView(R.id.rela_addfriend)
    RelativeLayout mRelaAddfriend;
    SearchUserResponse responseData;


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
                responseData = response.getData();
                setUserView(responseData);
            }
        });
    }


    String mUser_id;

    /**
     * 搜索结果展示
     *
     * @param mSearchUserResponse
     */
    private void setUserView(SearchUserResponse mSearchUserResponse) {
        mRelaFriend.setVisibility(View.VISIBLE);
        ImageLoader.loadCircleImage(this, Environment.BASEULR_PRODUCTION + mSearchUserResponse.getAvatar_url().getLarge(), mAvatar);
        mName.setText(mSearchUserResponse.getUser_name());
        mUser_id = mSearchUserResponse.getUser_id();
        mRelaAddfriend.setVisibility(mSearchUserResponse.getIs_friend() == NOFRIEND ? View.GONE : View.VISIBLE);
        setCollectionbook(mSearchUserResponse.getCollection());
    }

    private void initview() {
        mTitle.setText("添加读友");
    }


    @OnClick({R.id.back, R.id.rela_friend, R.id.rela_addfriend})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.rela_friend:
                intoDetailActivity();
                break;
            case R.id.rela_addfriend:
                showAddFriendDialog();
                break;
        }
    }

    private void intoDetailActivity() {
        Intent mIntent = new Intent(AddFriendActivity.this, FriendDetailActivity.class);
        Bundle mBundle=new Bundle();
        PersonInfo mInfo=new PersonInfo();
        mInfo.setAvatar(responseData.getAvatar_url().getLarge());
        mInfo.setClientid(responseData.getClientid());
        mInfo.setUser_id(responseData.getUser_id());
        mInfo.setFriend(responseData.getIs_friend()==1);
        mInfo.setNickname(responseData.getUser_name());
        mInfo.setSignature(responseData.getSignature());
        mBundle.putParcelable(FriendDetailActivity.PERSON_INFO,mInfo);
        mIntent.putExtras(mBundle);
        startActivity(mIntent);
    }

    private void showAddFriendDialog() {
        final CustomDialog mDialog = new CustomDialog(this);
        mDialog.setMaxNum(20);
        mDialog.setYesStr("发送");
        mDialog.setOnBtnOnclickListener(new CustomDialog.BtnOnclickListener() {
            @Override
            public void onYesClick() {
                addContact(mDialog.getMessage());
                mDialog.dismiss();
            }

            @Override
            public void onNoClick() {
                mDialog.dismiss();
            }
        });
        mDialog.show();
    }

    /**
     * 添加好友，
     *
     * @param reason
     */
    private void addContact(final String reason) {

        InputMethodUtils.hide(AddFriendActivity.this);
        mModel.requestAddFriend(mUser_id, reason, new AppBaseResponseCallBack<NovateResponse>(this) {
            @Override
            public void onSuccee(NovateResponse response) {
                Toast.makeText(AddFriendActivity.this, "" + response.getMessage(), Toast.LENGTH_SHORT).show();
                mRelaAddfriend.setVisibility(View.GONE);
            }
        });
    }

    boolean hassamebook = false;

    public void setCollectionbook(List<SearchUserResponse.CollectionBean> mCollectionbook) {
        for (SearchUserResponse.CollectionBean coll : mCollectionbook) {
            TextView mTextView = new TextView(this);
            mTextView.setText("《" + coll.getBook_name() + "》");
            if (coll.getIs_both_enjoy() == ISBOTHENJOY) {
                hassamebook = true;
                mTextView.setTextColor(getResources().getColor(R.color.text_green));
            }
            mLinCollection.addView(mTextView);
        }
        mRelaSameInterest.setVisibility(hassamebook ? View.VISIBLE : View.GONE);
    }
}
