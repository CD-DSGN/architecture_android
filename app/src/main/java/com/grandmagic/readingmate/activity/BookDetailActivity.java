package com.grandmagic.readingmate.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.grandmagic.readingmate.R;
import com.grandmagic.readingmate.adapter.CommonPagerAdapter;
import com.grandmagic.readingmate.base.AppBaseActivity;
import com.grandmagic.readingmate.base.AppBaseResponseCallBack;
import com.grandmagic.readingmate.bean.db.BookComment;
import com.grandmagic.readingmate.bean.response.BookdetailResponse;
import com.grandmagic.readingmate.bean.response.HistoryComment;
import com.grandmagic.readingmate.db.BookCommentDao;
import com.grandmagic.readingmate.db.DBHelper;
import com.grandmagic.readingmate.event.BookStateEvent;
import com.grandmagic.readingmate.event.RefreshHotCommentEvent;
import com.grandmagic.readingmate.model.BookModel;
import com.grandmagic.readingmate.utils.AutoUtils;
import com.grandmagic.readingmate.utils.DateUtil;
import com.grandmagic.readingmate.utils.DensityUtil;
import com.grandmagic.readingmate.utils.ImageLoader;
import com.grandmagic.readingmate.utils.InputMethodUtils;
import com.grandmagic.readingmate.view.HotcommentView;
import com.grandmagic.readingmate.view.SharePopUpWindow;
import com.grandmagic.readingmate.view.StarView;
import com.tamic.novate.NovateResponse;
import com.tamic.novate.util.Environment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BookDetailActivity extends AppBaseActivity implements View.OnLayoutChangeListener {
    public static final String BOOK_ID = "book_id";
    @BindView(R.id.back)
    ImageView mBack;
    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.title_more)
    ImageView mTitleMore;
    @BindView(R.id.titlelayout)
    RelativeLayout mTitlelayout;
    @BindView(R.id.iv1)
    ImageView mIv1;
    @BindView(R.id.et_comment)
    EditText mEtComment;
    @BindView(R.id.lin_share)
    LinearLayout mLinShare;
    @BindView(R.id.tv1)
    TextView mTv1;
    @BindView(R.id.ratingbar)
    StarView mRatingbar;
    @BindView(R.id.his_score)
    TextView mHisScore;
    @BindView(R.id.submit)
    TextView mSubmit;
    @BindView(R.id.bottomlayout)
    LinearLayout mBottomlayout;
    @BindView(R.id.bookname)
    TextView mBookname;
    @BindView(R.id.iv_conver)
    ImageView mIvConver;
    @BindView(R.id.avatar)
    ImageView mAvatar;
    @BindView(R.id.author)
    TextView mAuthor;
    @BindView(R.id.tv_publisher)
    TextView mTvPublisher;
    @BindView(R.id.tv_publistime)
    TextView mTvPublistime;
    @BindView(R.id.total_score)
    StarView mTotalScore;
    @BindView(R.id.score)
    TextView mScore;
    @BindView(R.id.num_people)
    TextView mNumPeople;
    @BindView(R.id.rela_rating)
    RelativeLayout mRelaRating;
    @BindView(R.id.about)
    TextView mAbout;

    @BindView(R.id.tv2)
    TextView mTv2;
    @BindView(R.id.lin_collection)
    LinearLayout mLinCollection;
    @BindView(R.id.coll_more)
    ImageView mCollMore;
    @BindView(R.id.collectionNum)
    TextView mCollectionNum;
    @BindView(R.id.tv_last)
    TextView mTvLast;
    @BindView(R.id.dashline_tvlast)
    View mDashlineTvlast;
    @BindView(R.id.tv_hot)
    TextView mTvHot;
    @BindView(R.id.dashline_tvhot)
    View mDashlineTvhot;
    @BindView(R.id.viewpager)
    ViewPager mViewpager;
    @BindView(R.id.activityView)
    RelativeLayout mActivityView;
    @BindView(R.id.rl_collect_person)
    RelativeLayout mRlCollectPerson;
    @BindView(R.id.commentnum)
    TextView mCommentnum;


    private String book_id;
    private SharePopUpWindow mSharePopUpWindow;
    private BookdetailResponse mBookdetailResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);
        ButterKnife.bind(this);
        AutoUtils.auto(this);
        setTranslucentStatus(true);
        initdata();
        initView();
        initlistener();
    }


    private void initlistener() {
        mBottomlayout.addOnLayoutChangeListener(this);
        mViewpager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                if (position == 0) lastSelected();
                else hotSelected();
            }

        });
        mRatingbar.setStarChangeListener(new StarView.OnStarChangeListener() {
            @Override
            public void onChange(float score) {
                mHisScore.setText(score + "分");
            }
        });
    }

    private void hotSelected() {
        mTvLast.setTextSize(TypedValue.COMPLEX_UNIT_PX, 24);
        mTvLast.setTextColor(getResources().getColor(R.color.gray_noselect));
        mDashlineTvlast.setVisibility(View.GONE);
        mTvHot.setTextSize(TypedValue.COMPLEX_UNIT_PX, 28);
        mTvHot.setTextColor(getResources().getColor(R.color.gray_select));
        mDashlineTvhot.setVisibility(View.VISIBLE);
        AutoUtils.autoTextSize(mTvLast);
        AutoUtils.autoTextSize(mTvHot);
        mViewpager.setCurrentItem(1);
    }

    private void lastSelected() {
        mTvLast.setTextSize(TypedValue.COMPLEX_UNIT_PX, 32);
        mTvLast.setTextColor(getResources().getColor(R.color.gray_select));
        mDashlineTvlast.setVisibility(View.VISIBLE);
        mTvHot.setTextSize(TypedValue.COMPLEX_UNIT_PX, 28);
        mTvHot.setTextColor(getResources().getColor(R.color.gray_noselect));
        mDashlineTvhot.setVisibility(View.GONE);
        AutoUtils.autoTextSize(mTvLast);
        AutoUtils.autoTextSize(mTvHot);
        mViewpager.setCurrentItem(0);
    }

    HotcommentView mHotView;
    HotcommentView mRecentView;

    private void initView() {
        if (mSharePopUpWindow == null) {
            mSharePopUpWindow = new SharePopUpWindow(this);
        }

        List<View> mViews = new ArrayList<>();
        //        为了减少Activity的代码，将评论的相关功能抽离到HotcommentView了

        mRecentView = new HotcommentView(this, HotcommentView.COMMENT_TIME, mModel, book_id);
        mHotView = new HotcommentView(this, HotcommentView.COMMENT_LIKE, mModel, book_id);
        mRecentView.addNumView(mCommentnum);
        mViews.add(mRecentView);
        mViews.add(mHotView);
        mTitle.setText("图书详情");
        mViewpager.setAdapter(new CommonPagerAdapter(mViews));
    }

    BookModel mModel;
    int hisScore;//我之前对这本书的评分，

    private void initdata() {
        mModel = new BookModel(this);
        book_id = getIntent().getStringExtra(BOOK_ID);
        mModel.getBookDetail(book_id, new AppBaseResponseCallBack<NovateResponse<BookdetailResponse>>(this) {
            @Override
            public void onSuccee(NovateResponse<BookdetailResponse> response) {
                mBookdetailResponse = response.getData();
                setbookView(response.getData());
            }
        });
        mModel.getMyComment(book_id, new AppBaseResponseCallBack<NovateResponse<HistoryComment>>(this) {
            @Override
            public void onSuccee(NovateResponse<HistoryComment> response) {
                hisScore = response.getData().getScore_num();
                mRatingbar.setScore(hisScore * 1.0f);
                mHisScore.setText(hisScore + "分");
            }
        });
        loadCommentFromDB();
    }

    /**
     * 从db加载之前是否有未提交的评论
     */
    BookComment mHisComment;

    private void loadCommentFromDB() {
        mHisComment = DBHelper.getBookCommentDao(this).queryBuilder().where(BookCommentDao.Properties.Bookid.eq(book_id)).build().unique();
        DBHelper.close();
        if (mHisComment != null) {
            mEtComment.setText(mHisComment.getComment_content());
            mScore.setText(mHisComment.getScore() + "");
        }
    }


    @Override
    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
        LinearLayout.LayoutParams mParams = (LinearLayout.LayoutParams) mEtComment.getLayoutParams();

        if (oldBottom != 0 && bottom != 0 && oldBottom - bottom > DensityUtil.getScreenHeight(this) / 3) {
            //            键盘弹出
            mRelaRating.setVisibility(View.VISIBLE);
            mLinShare.setVisibility(View.GONE);
            mSubmit.setVisibility(View.VISIBLE);
            mParams.height = 3 * mEtComment.getMeasuredHeight();
        } else if (oldBottom != 0 && bottom != 0 && bottom - oldBottom > DensityUtil.getScreenHeight(this) / 3) {
            mRelaRating.setVisibility(View.GONE);
            mLinShare.setVisibility(View.VISIBLE);
            mSubmit.setVisibility(View.GONE);
            mParams.height = mEtComment.getMeasuredHeight() / 3;
            //            键盘收起
        }
        mEtComment.setLayoutParams(mParams);
    }

    /**
     * 设置书的详情
     *
     * @param s
     */
    public void setbookView(BookdetailResponse s) {
        mBookname.setText(s.getBook_name());

        mAuthor.setText("" + s.getAuthor());
        mAuthor.setVisibility(TextUtils.isEmpty(s.getAuthor()) ? View.GONE : View.VISIBLE);
        mTvPublisher.setText("" + s.getPublisher());
        mTvPublisher.setVisibility(TextUtils.isEmpty(s.getPublisher()) ? View.GONE : View.VISIBLE);
        mTvPublistime.setText("" + DateUtil.timeTodate("yyyy-MM-dd", s.getPub_date()));
        mTvPublistime.setVisibility(TextUtils.isEmpty(s.getPub_date()) ? View.GONE : View.VISIBLE);
        mAbout.setText(s.getSynopsis());
        mCollectionNum.setText(s.getCollect_count());
        try {
            mCollMore.setVisibility(Integer.valueOf(s.getCollect_count()) > 4 ? View.VISIBLE : View.GONE);
        } catch (NumberFormatException mE) {
            mE.printStackTrace();
        }

        try {
            int collect_num = Integer.valueOf(s.getCollect_count());
            if (collect_num <= 0) {
                mRlCollectPerson.setVisibility(View.GONE);
            } else {
                mRlCollectPerson.setVisibility(View.VISIBLE);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            mRlCollectPerson.setVisibility(View.GONE);
        }

        ImageLoader.loadBookImg(this, Environment.getUrl() + s.getPhoto(), mIvConver);
        setCollectView(s.getCollect_user());
        mTotalScore.setScore(Float.valueOf(s.getTotal_score()));
        mScore.setText(s.getTotal_score());
        mNumPeople.setText("(" + s.getScore_times() + "人评)");
        mBottomlayout.setVisibility(s.getIs_follow() == 1 ? View.VISIBLE : View.GONE);
    }

    @OnClick({R.id.back, R.id.submit, R.id.tv_last, R.id.tv_hot, R.id.coll_more, R.id.lin_share, R.id.lin_collection})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.submit:
                submitComment();
                break;
            case R.id.tv_last:
                lastSelected();
                break;
            case R.id.tv_hot:
                hotSelected();
                break;
            case R.id.coll_more:
            case R.id.lin_collection:
                Intent mIntent = new Intent(BookDetailActivity.this, CollectedPersonActivity.class);
                mIntent.putExtra(BOOK_ID, book_id);
                startActivity(mIntent);
                break;
            case R.id.lin_share:
                if (mBookdetailResponse != null) {
                    mSharePopUpWindow.setBookData(mBookdetailResponse.getBook_name(), book_id,
                            mBookdetailResponse.getSynopsis(), mBookdetailResponse.getPhoto(),
                            mBookdetailResponse.getTotal_score());
                    mSharePopUpWindow.show();
                }
                break;
        }
    }

    /**
     * 提交评论到后台
     */
    int mbookScore;

    private void submitComment() {
        mbookScore = (int) mRatingbar.getScore();
        String content = mEtComment.getText().toString();
        if (mbookScore == 0) {
            Toast.makeText(this, "还是评个分再提交吧", Toast.LENGTH_SHORT).show();
            return;
        }
        mModel.ScoreBook(book_id, mbookScore, content, new AppBaseResponseCallBack<NovateResponse>(this) {
            @Override
            public void onSuccee(NovateResponse response) {
                InputMethodUtils.hide(BookDetailActivity.this);
                mEtComment.setText("");
                if (mHisComment != null) {
                    DBHelper.getBookCommentDao(BookDetailActivity.this).delete(mHisComment);
                    DBHelper.close();
                }
                mRecentView.loadData(1);//评论成功刷新评论列表
                mHotView.loadData(1);
                EventBus.getDefault().post(new BookStateEvent(BookStateEvent.STATE_MOVE, book_id));
            }
        });
    }

    /**
     * 设置收藏的UI
     *
     * @param mCollectView
     */
    public void setCollectView(List<BookdetailResponse.CollectUserBean> mCollectView) {
        for (BookdetailResponse.CollectUserBean user : mCollectView) {
            ImageView mView = new ImageView(this);
            LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(70, 70);
            mParams.setMargins(10, 10, 10, 10);
            mView.setLayoutParams(mParams);
            AutoUtils.auto(mView);
            ImageLoader.loadCircleImage(this, Environment.BASEULR_PRODUCTION + user.getAvatar_url().getMid(), mView);
            mLinCollection.addView(mView);
        }
    }

    @Subscribe
    public void refreshHotcommentView(RefreshHotCommentEvent mEvent) {
        ((HotcommentView) mHotView).loadData(1);
    }

    @Override
    protected void onDestroy() {
        if (!TextUtils.isEmpty(mEtComment.getText())) {//如果有未提交的评论则保存到本地
            BookComment mBookComment = new BookComment();
            mBookComment.setScore(mbookScore);
            mBookComment.setBookid(book_id);
            mBookComment.setComment_content(mEtComment.getText().toString());
            DBHelper.getBookCommentDao(this).insertOrReplace(mBookComment);
            DBHelper.close();
        }
        super.onDestroy();
    }


}
