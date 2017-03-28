package com.grandmagic.readingmate.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.airbnb.lottie.LottieAnimationView;
import com.grandmagic.readingmate.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * A simple {@link Fragment} subclass.
 */
public class GuideFragment extends Fragment {


    @BindView(R.id.animaview)
    LottieAnimationView mAnimaview;
    Unbinder unbinder;

    public GuideFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_guide, container, false);
        unbinder = ButterKnife.bind(this, view);
       intitview();
        return view;
    }

    private void intitview() {
        int mPosition = getArguments().getInt("position", -1);
        if (mPosition==-1)return;
        mAnimaview.setAnimation("guidepage"+mPosition+".json");
        mAnimaview.setImageAssetsFolder("guidepage"+mPosition+"/");
        mAnimaview.playAnimation();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
