package com.grandmagic.readingmate.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.grandmagic.readingmate.R;
import com.grandmagic.readingmate.activity.CollectionActivity;
import com.grandmagic.readingmate.activity.CommentsActivity;
import com.grandmagic.readingmate.activity.MessageNotificationActivity;
import com.grandmagic.readingmate.activity.PersonalInfoEditActivity;
import com.grandmagic.readingmate.activity.SettingActivity;
import com.grandmagic.readingmate.adapter.DefaultEmptyAdapter;
import com.grandmagic.readingmate.adapter.MyCommentAdapter;
import com.grandmagic.readingmate.base.AppBaseActivity;
import com.grandmagic.readingmate.base.AppBaseFragment;
import com.grandmagic.readingmate.base.AppBaseResponseCallBack;
import com.grandmagic.readingmate.bean.response.ImageUrlResponseBean;
import com.grandmagic.readingmate.bean.response.PersonalCommentListResponseBean;
import com.grandmagic.readingmate.bean.response.PersonnalCommentResponseBean;
import com.grandmagic.readingmate.bean.response.UserInfoResponseBean;
import com.grandmagic.readingmate.model.BookModel;
import com.grandmagic.readingmate.model.MyCommentsModel;
import com.grandmagic.readingmate.model.UserInfoModel;
import com.grandmagic.readingmate.ui.DeleteConfirmDlg;
import com.grandmagic.readingmate.utils.AutoUtils;
import com.grandmagic.readingmate.utils.ImageLoader;
import com.grandmagic.readingmate.utils.KitUtils;
import com.grandmagic.readingmate.utils.Page;
import com.grandmagic.readingmate.utils.ViewUtils;
import com.refreshlab.PullLoadMoreRecyclerView;
import com.tamic.novate.NovateResponse;
import com.tamic.novate.Throwable;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class PersonalFragment extends AppBaseFragment implements MyCommentAdapter.OnitemDeleteListener {
    Context mContext;

    PullLoadMoreRecyclerView mRvMyComments;
    @BindView(R.id.tv_edit_personal_info)
    TextView mTvEditPersonalInfo;
    @BindView(R.id.ll_collect)
    LinearLayout mLlCollect;
    @BindView(R.id.ll_setting)
    LinearLayout mLlSetting;


    @BindView(R.id.iv_frag_personal_header)
    ImageView mIvFragPersonalHeader;
    @BindView(R.id.tv_frag_personal_nickname)
    TextView mTvFragPersonalNickname;
    @BindView(R.id.tv_frag_personal_signature)
    TextView mTvFragPersonalSignature;
    @BindView(R.id.ic_frag_personal_male)
    ImageView mIcFragPersonalMale;
    @BindView(R.id.ic_frag_personal_female)
    ImageView mIcFragPersonalFemale;

    UserInfoModel mUserInfoModel;
    UserInfoResponseBean mUserInfoResponseBean = new UserInfoResponseBean();
    @BindView(R.id.subscriped_book_num)
    TextView mSubscripedBookNum;
    @BindView(R.id.tv_unread_num)
    TextView mTvUnreadNum;

    private MyCommentAdapter mMAdapter;
    private HeaderAndFooterWrapper mMHeaderAndFooterWrapper;
    private View mView;

    MyCommentsModel mMyCommentsModel;
    public static boolean NEED_REFRESH = false;

    Page mPage;

    private ArrayList<PersonnalCommentResponseBean> mComments = new ArrayList<>();
    BookModel mBookModel;

    public static final int PF = 1;
    public static final int UNREAD = 2;

    private DeleteConfirmDlg mCommentDlg;

    private DefaultEmptyAdapter mDefaultEmptyAdapter;

    public PersonalFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_personal, container, false);
        AutoUtils.auto(view);
        mRvMyComments = (PullLoadMoreRecyclerView) view.findViewById(R.id.rv_my_comments);
        mContext = getActivity();
        ButterKnife.bind(view);
        mPage = new Page(mComments, MyCommentsModel.PAGE_SIZE);
        initView();
        loadData();

        return view;
    }

    private void loadData() {
        mUserInfoModel.getUserInfo();
        mMyCommentsModel.getMyComment(1);
    }


    private void setPersonalView() {
        ImageUrlResponseBean imageUrlResponseBean = mUserInfoResponseBean.getAvatar_url();
        if (imageUrlResponseBean != null) {
            String url = imageUrlResponseBean.getLarge();
            if (!TextUtils.isEmpty(url)) {
                ImageLoader.loadCircleImage(mContext, KitUtils.getAbsoluteUrl(url),
                        mIvFragPersonalHeader);
            }
        }

        if (!TextUtils.isEmpty(mUserInfoResponseBean.getSignature())) {
            mTvFragPersonalSignature.setText(mUserInfoResponseBean.getSignature());
        }

        if (!TextUtils.isEmpty(mUserInfoResponseBean.getUser_name())) {
            mTvFragPersonalNickname.setText(mUserInfoResponseBean.getUser_name());
        }

        setGenderView(mUserInfoResponseBean.getGender());

        //设置关注图书本数
        setScanCountView(mUserInfoResponseBean.getScan_count());

    }

    private void initView() {
        if (mBookModel == null) {
            mBookModel = new BookModel(mContext);
        }

        mView = LayoutInflater.from(mContext).inflate(R.layout.personal_fragment_header, mRvMyComments, false);
        ButterKnife.bind(this, mView);
        mMAdapter = new MyCommentAdapter(mContext, mComments, "", "", this);
        mMAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent = new Intent(mContext, CommentsActivity.class);
                intent.putExtra("comment_id", mComments.get(position - 1).getComment_id());  //因为有头部的原因，所以-1
                PersonalFragment.this.startActivityForResult(intent, PF);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });

        if (mDefaultEmptyAdapter == null) {
            mDefaultEmptyAdapter = new DefaultEmptyAdapter(mMAdapter, mContext);
            mDefaultEmptyAdapter.setEmptyViewTextview(mContext.getString(R.string.no_reply_yet));
        }

        AutoUtils.auto(mView);
        mMHeaderAndFooterWrapper = new HeaderAndFooterWrapper(mDefaultEmptyAdapter);
        mMHeaderAndFooterWrapper.addHeaderView(mView);
        mRvMyComments.setAdapter(mMHeaderAndFooterWrapper);
        mMHeaderAndFooterWrapper.notifyDataSetChanged();

        //获取个人信息
        if (mUserInfoModel == null) {
            mUserInfoModel = new UserInfoModel(mContext, new AppBaseResponseCallBack<NovateResponse<UserInfoResponseBean>>(mContext, false) {
                @Override
                public void onSuccee(NovateResponse<UserInfoResponseBean> response) {
                    if (response != null && response.getData() != null) {
                        mUserInfoResponseBean = (UserInfoResponseBean) response.getData();
                        //设置相应的数据
                        setPersonalView();
                        mMAdapter.setUsername(mUserInfoResponseBean.getUser_name());
                        if (mMHeaderAndFooterWrapper != null) {
                            mMHeaderAndFooterWrapper.notifyDataSetChanged();
                        }
                        NEED_REFRESH = false;

                    }
                }
            });
        }

        if (mMyCommentsModel == null) {
            mcallback = new AppBaseResponseCallBack<NovateResponse<PersonalCommentListResponseBean>>(mContext) {

                @Override
                public void onSuccee(NovateResponse<PersonalCommentListResponseBean> response) {

                    PersonalCommentListResponseBean personalCommentListResponseBean = response.getData();
                    ImageUrlResponseBean imageUrlResponseBean = personalCommentListResponseBean.getAvatar_url();
                    String url = "";
                    if (imageUrlResponseBean != null) {
                        url = imageUrlResponseBean.getLarge();
                    }

                    String unread_num = personalCommentListResponseBean.getUnread_num();
                    if (TextUtils.isEmpty(unread_num) || unread_num.equals("0")) {
                        mTvUnreadNum.setVisibility(View.INVISIBLE);
                    } else {
                        mTvUnreadNum.setText(personalCommentListResponseBean.getUnread_num());
                        mTvUnreadNum.setVisibility(View.VISIBLE);
                    }


                    //                    mMAdapter.setUsername(personalCommentListResponseBean.getUsername());
                    if (personalCommentListResponseBean != null) {
                        List<PersonnalCommentResponseBean> list = personalCommentListResponseBean.getComment_info();

                        mMAdapter.setUrl(url);
                        if (mcallback.isRefresh) {
                            mPage.refresh(list);  //刷新
                        } else {
                            mPage.more(list);  //加载更多
                        }
                        try {
                            mPage.total_num = Integer.parseInt(personalCommentListResponseBean.getTotal_num());
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }

                    }
                    mDefaultEmptyAdapter.refresh();
                    mMHeaderAndFooterWrapper.notifyDataSetChanged();
                    mRvMyComments.setPullLoadMoreCompleted();
                }

                @Override
                public void onError(Throwable e) {
                    super.onError(e);

                    mDefaultEmptyAdapter.refresh();
                    mMHeaderAndFooterWrapper.notifyDataSetChanged();
                    mRvMyComments.setPullLoadMoreCompleted();
                    //展示出错界面
                }
            };
            mMyCommentsModel = new MyCommentsModel(mContext, mcallback);
        }

        if (mCommentDlg == null) {
            mCommentDlg = new DeleteConfirmDlg(mContext);
        }
        setSystemBarColor(false);
        initRefresh();
    }


    @OnClick({R.id.tv_edit_personal_info, R.id.ll_collect, R.id.ll_setting, R.id.rela_notification})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_edit_personal_info:
                //跳转到个人设置页面
                Intent intent_edit = new Intent(mContext, PersonalInfoEditActivity.class);
                //                intent_edit.putExtra("personal_data", mUserInfoResponseBean);
                startActivity(intent_edit);
                break;
            case R.id.ll_collect:
                Intent intent_collection = new Intent(mContext, CollectionActivity.class);
                startActivity(intent_collection);
                break;
            case R.id.ll_setting:
                Intent intent_setting = new Intent(mContext, SettingActivity.class);
                String username = mUserInfoResponseBean == null ? " " : mUserInfoResponseBean.getUser_name();
                intent_setting.putExtra(SettingActivity.USR_NAME, username);
                startActivity(intent_setting);
                break;
            case R.id.rela_notification:
                startActivityForResult(new Intent(getActivity(), MessageNotificationActivity.class), UNREAD);
                break;
            default:
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (NEED_REFRESH) {
            mUserInfoModel.getUserInfo();
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        setSystemBarColor(hidden);
    }

    @Override
    public void setSystemBarColor(boolean hidden) {
        if (!hidden) ((AppBaseActivity) (mContext)).setSystemBarColor(android.R.color.white);
    }


    public void setGenderView(int genderView) {
        if (genderView == 1) {   //女
            mIcFragPersonalMale.setVisibility(View.GONE);
            mIcFragPersonalFemale.setVisibility(View.VISIBLE);
        } else if (genderView == 2) {                  //男
            mIcFragPersonalMale.setVisibility(View.VISIBLE);
            mIcFragPersonalFemale.setVisibility(View.GONE);
        } else {  //未设置
            mIcFragPersonalFemale.setVisibility(View.VISIBLE);
            mIcFragPersonalMale.setVisibility(View.VISIBLE);
        }
    }

    public void setScanCountView(String scanCountView) {
        if (!TextUtils.isEmpty(scanCountView)) {
            mSubscripedBookNum.setText(scanCountView);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMAdapter.mSharePopUpWindow.dismiss(); //否则会报错，泄漏了窗口
    }

    private void initRefresh() {
        mRvMyComments.setLinearLayout();
        mRvMyComments.setOnPullLoadMoreListener(new PullLoadMoreRecyclerView.PullLoadMoreListener() {
            @Override
            public void onRefresh() {
                mMyCommentsModel.getMyComment(1);
                mcallback.isRefresh = true;
            }

            @Override
            public void onLoadMore() {
                if (mPage.hasMore()) {
                    mMyCommentsModel.getMyComment(mPage.cur_page);
                    mcallback.isRefresh = false;
                } else {
//                    ViewUtils.showToast("暂无更多数据");
                    mRvMyComments.setPullLoadMoreCompleted();
                }
            }
        });
    }


    AppBaseResponseCallBack<NovateResponse<PersonalCommentListResponseBean>> mcallback;

    @Override
    public void onDelete(PersonnalCommentResponseBean personnalCommentResponseBean) {
        final String comment_id = personnalCommentResponseBean.getComment_id();
        //展示是否确定的弹框
        mCommentDlg.setOnClickYes(new DeleteConfirmDlg.OnClickYes() {
            @Override
            public void onClick() {
                mBookModel.deleteBookComment(comment_id,
                        new AppBaseResponseCallBack<NovateResponse<Object>>(mContext) {
                            @Override
                            public void onSuccee(NovateResponse<Object> response) {
                                //删除成功,更新本地数据
                                deleteLocalComment(comment_id);
                            }
                        });
                mCommentDlg.dismiss();
            }
        });
        mCommentDlg.show();
    }

    private void deleteLocalComment(String comment_id) {
        for (int i = 0; i < mComments.size(); i++) {
            PersonnalCommentResponseBean comment = mComments.get(i);
            if (comment != null) {
                if (comment.getComment_id().equals(comment_id)) {
                    mPage.delete(i);
                    mMHeaderAndFooterWrapper.notifyDataSetChanged();
                    break;
                }

            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (PF == requestCode && resultCode == CommentsActivity.RESULT_DEL) {
            String comment_id = data.getStringExtra(CommentsActivity.COMMENT_ID);
            deleteLocalComment(comment_id);
        } else if (UNREAD == requestCode) {
            mTvUnreadNum.setVisibility(View.INVISIBLE);
        }
    }
}
