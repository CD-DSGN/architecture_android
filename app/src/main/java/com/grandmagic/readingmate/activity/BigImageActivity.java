package com.grandmagic.readingmate.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.grandmagic.readingmate.R;
import com.grandmagic.readingmate.utils.ImageLoader;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BigImageActivity extends AppCompatActivity {
public static final String IMG_URL="img_url";
    @BindView(R.id.photoView)
    PhotoView mPhotoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_big_image);
        ButterKnife.bind(this);
        initdata();
    }

    private void initdata() {
        String url = getIntent().getStringExtra(IMG_URL);
        Glide.with(this).load(url).into(mPhotoView);
    }
}
