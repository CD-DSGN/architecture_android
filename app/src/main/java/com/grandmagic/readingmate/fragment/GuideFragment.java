package com.grandmagic.readingmate.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.grandmagic.readingmate.R;
import com.grandmagic.readingmate.activity.LoginActivity;
import com.grandmagic.readingmate.activity.MainActivity;
import com.grandmagic.readingmate.utils.AutoUtils;
import com.tamic.novate.util.SPUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


/**
 * A simple {@link Fragment} subclass.
 */
public class GuideFragment extends Fragment {


    Unbinder unbinder;
    @BindView(R.id.iv)
    ImageView mIv;
    @BindView(R.id.guide_start)
    ImageView mGuideStart;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_guide, container, false);
        AutoUtils.auto(view);
        unbinder = ButterKnife.bind(this, view);
        intitview(view);
        return view;
    }

    private void intitview(View mView) {
        int mPosition = getArguments().getInt("position", -1);
        switch (mPosition) {
            case 0:

                mIv.setImageResource(R.drawable.guide_1);
                break;
            case 1:
                mIv.setImageResource(R.drawable.guide_2);

                break;
            case 2:
                mIv.setImageResource(R.drawable.guide_3);
                break;
            case 3:
                mIv.setImageResource(R.drawable.guide_4);
                mGuideStart.setVisibility(View.VISIBLE);
                break;
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.guide_start)
    public void onViewClicked() {
        Intent mIntent = new Intent(getActivity(), SPUtils.getInstance().isLogin(getActivity()) ? MainActivity.class : LoginActivity.class);
        startActivity(mIntent);
        getActivity().finish();
    }
}
