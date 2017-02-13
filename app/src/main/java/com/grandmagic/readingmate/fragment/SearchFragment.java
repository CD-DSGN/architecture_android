package com.grandmagic.readingmate.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.grandmagic.readingmate.R;
import com.grandmagic.readingmate.base.AppBaseFragment;
import com.grandmagic.readingmate.view.IrregularImageView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass. test
 */
public class SearchFragment extends AppBaseFragment {


    @BindView(R.id.testimage)
    IrregularImageView mTestimage;
    @BindView(R.id.img)
    ImageView mImg;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        ButterKnife.bind(this, view);
        Glide.with(this).load("http://inthecheesefactory.com/uploads/source/glidepicasso/cover.jpg")

                .into(mTestimage);
        Glide.with(this).load("http://inthecheesefactory.com/uploads/source/glidepicasso/cover.jpg")


                .into(mImg);
        return view;
    }

}
