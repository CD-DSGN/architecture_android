package com.grandmagic.readingmate.activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.grandmagic.readingmate.R;
import com.grandmagic.readingmate.base.AppBaseActivity;
import com.grandmagic.readingmate.base.AppBaseResponseCallBack;
import com.grandmagic.readingmate.model.BookModel;
import com.tamic.novate.NovateResponse;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BookDetailActivity extends AppBaseActivity {
    public static final String ISBN_CODE = "isbn_code";
    private String isbn_code;
    @BindView(R.id.back)
    ImageView mBack;
    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.titlelayout)
    RelativeLayout mTitlelayout;
    @BindView(R.id.json)
    TextView mJson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);
        ButterKnife.bind(this);
        setTranslucentStatus(true);
        initdata();
    }

    BookModel mModel;

    private void initdata() {
        isbn_code = getIntent().getStringExtra(ISBN_CODE);
        mModel = new BookModel(this);
        mModel.fllowBook(isbn_code, new AppBaseResponseCallBack<NovateResponse>(this) {
            @Override
            public void onSuccee(NovateResponse response) {
                mJson.setText(response.toString());
            }
        });
    }

    @OnClick(R.id.back)
    public void onClick() {
        finish();
    }
}
