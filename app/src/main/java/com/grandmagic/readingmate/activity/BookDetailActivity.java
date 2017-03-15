package com.grandmagic.readingmate.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
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
import com.grandmagic.readingmate.bean.response.BookdetailResponse;
import com.grandmagic.readingmate.model.BookModel;
import com.grandmagic.readingmate.utils.AutoUtils;
import com.grandmagic.readingmate.utils.DensityUtil;
import com.grandmagic.readingmate.utils.ImageLoader;
import com.grandmagic.readingmate.view.HotcommentView;
import com.grandmagic.readingmate.view.StarView;
import com.tamic.novate.NovateResponse;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BookDetailActivity extends AppBaseActivity implements View.OnLayoutChangeListener {
    public static final String ISBN_CODE = "isbn_code";
    public static final String BOOK_ID = "book_id";
    @BindView(R.id.rela_score)
    RelativeLayout mRelaScore;
    private String book_id;
    @BindView(R.id.back)
    ImageView mBack;
    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.title_more)
    ImageView mTitleMore;
    @BindView(R.id.titlelayout)
    RelativeLayout mTitlelayout;
    @BindView(R.id.bookname)
    TextView mBookname;
    @BindView(R.id.avatar)
    ImageView mAvatar;
    @BindView(R.id.author)
    TextView mAuthor;
    @BindView(R.id.ratingbar)
    StarView mRatingbar;
    @BindView(R.id.score)
    TextView mScore;
    @BindView(R.id.num_people)
    TextView mNumPeople;
    @BindView(R.id.t_publisher)
    TextView mTPublisher;
    @BindView(R.id.tv_publisher)
    TextView mTvPublisher;
    @BindView(R.id.t_publishtime)
    TextView mTPublishtime;
    @BindView(R.id.tv_publistime)
    TextView mTvPublistime;
    @BindView(R.id.about)
    TextView mAbout;
    @BindView(R.id.iv_conver)
    ImageView mIvConver;
    @BindView(R.id.lin_collection)
    LinearLayout mLinCollection;
    @BindView(R.id.collectionNum)
    TextView mCollectionNum;
    @BindView(R.id.viewpager)
    ViewPager mViewpager;
    @BindView(R.id.iv1)
    ImageView mIv1;
    @BindView(R.id.bottomlayout)
    LinearLayout mBottomlayout;
    @BindView(R.id.tv1)
    TextView mTv1;
    @BindView(R.id.rela_rating)
    RelativeLayout mRelaRating;
    @BindView(R.id.submit)
    Button mSubmit;
    @BindView(R.id.activityView)
    RelativeLayout mActivityView;
    @BindView(R.id.lin_share)
    LinearLayout mLinShare;
    @BindView(R.id.et_comment)
    EditText mEtComment;


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
    }

    private void initView() {
        List<View> mViews = new ArrayList<>();
        View mRecentView = new HotcommentView(this);
        View mHotView = new HotcommentView(this);
        mViews.add(mRecentView);
        mViews.add(mHotView);
        mViewpager.setAdapter(new CommonPagerAdapter(mViews));
    }

    BookModel mModel;

    private void initdata() {
        mModel = new BookModel(this);
        book_id = getIntent().getStringExtra(BOOK_ID);
        mModel.getBookDetail(book_id, new AppBaseResponseCallBack<NovateResponse<BookdetailResponse>>(this) {
            @Override
            public void onSuccee(NovateResponse<BookdetailResponse> response) {
                setbookView(response.getData());
            }
        });

    }


    @Override
    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
        LinearLayout.LayoutParams mParams = (LinearLayout.LayoutParams) mEtComment.getLayoutParams();

        if (oldBottom != 0 && bottom != 0 && oldBottom - bottom > DensityUtil.getScreenHeight(this) / 3) {
//            键盘弹出
            mRelaRating.setVisibility(View.VISIBLE);
            mLinShare.setVisibility(View.GONE);
            mSubmit.setVisibility(View.VISIBLE);
            mParams.height = 4 * mEtComment.getMeasuredHeight();

        } else if (oldBottom != 0 && bottom != 0 && bottom - oldBottom > DensityUtil.getScreenHeight(this) / 3) {
            mRelaRating.setVisibility(View.GONE);
            mLinShare.setVisibility(View.VISIBLE);
            mSubmit.setVisibility(View.GONE);
            mParams.height = mEtComment.getMeasuredHeight() / 4;
//            键盘收起
        }
        mEtComment.setLayoutParams(mParams);
    }

    public void setbookView(BookdetailResponse s) {
        mBookname.setText(s.getBook_name());
        mAuthor.setText(s.getAuthor());
        mTvPublisher.setText(s.getPublisher());
        mTvPublistime.setText(s.getPub_date());
//        mAbout.setText(s.getSynopsis());
        ImageLoader.loadImage(this, "http://files.jb51.net/do/uploads/litimg/160809/1FR52JC7.jpg", mIvConver);
    }

    @OnClick({R.id.back, R.id.submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.submit:
                submitComment();
                break;
        }
    }

    private void submitComment() {
        int mScore = mRatingbar.getScore();
        String content = mEtComment.getText().toString();
        if (TextUtils.isEmpty(content)) {
            Toast.makeText(this, "请输入评论内容再提交", Toast.LENGTH_SHORT).show();
            return;
        }
        mModel.ScoreBook(book_id, mScore, content, new AppBaseResponseCallBack<NovateResponse>(this) {
            @Override
            public void onSuccee(NovateResponse response) {

            }
        });
    }
}
