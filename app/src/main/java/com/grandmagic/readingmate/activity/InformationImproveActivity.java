package com.grandmagic.readingmate.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.grandmagic.readingmate.R;
import com.grandmagic.readingmate.base.AppBaseActivity;
import com.grandmagic.readingmate.base.AppBaseResponseCallBack;
import com.grandmagic.readingmate.model.InfoImproveModel;
import com.grandmagic.readingmate.utils.AutoUtils;
import com.grandmagic.readingmate.utils.KitUtils;
import com.tamic.novate.NovateResponse;
import com.yuyh.library.imgsel.ImageLoader;
import com.yuyh.library.imgsel.ImgSelActivity;
import com.yuyh.library.imgsel.ImgSelConfig;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by lps on 2017/4/24.
 * 补全资料的activity
 *
 * @since 2017/4/24 16:37
 */


public class InformationImproveActivity extends AppBaseActivity {
    private static final int REQUEST_SELIMG = 1;
    @BindView(R.id.back)
    ImageView mBack;
    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.title_more)
    ImageView mTitleMore;
    @BindView(R.id.titlelayout)
    RelativeLayout mTitlelayout;
    @BindView(R.id.avatar)
    ImageView mAvatar;
    @BindView(R.id.dashline)
    View mDashline;
    @BindView(R.id.iv_nickname)
    ImageView mIvNickname;
    @BindView(R.id.et_nickname)
    EditText mEtNickname;
    @BindView(R.id.iv_sel_gender)
    ImageView mIvSelGender;
    @BindView(R.id.check_male)
    ImageView mCheckMale;
    @BindView(R.id.check_female)
    ImageView mCheckFemale;
    @BindView(R.id.complete)
    Button mComplete;
    private String mAvatarString;
    boolean nickNameInputed;
    InfoImproveModel mModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infomation_improve);
        ButterKnife.bind(this);
        setTranslucentStatus(true);
        AutoUtils.auto(this);
        initview();
        initlisenter();
    }

    private void initlisenter() {
        mEtNickname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                nickNameInputed = !TextUtils.isEmpty(s);
                if (nickNameInputed && mCheckMale.isSelected() || mCheckFemale.isSelected()) {
                    mComplete.setEnabled(true);
                } else {
                    mComplete.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void initview() {
        mComplete.setEnabled(false);
        mTitle.setText("完善资料");
        mTitle.setTextColor(Color.WHITE);
        mCheckFemale.setSelected(false);
        mCheckMale.setSelected(false);
    }

    @OnClick({R.id.back, R.id.avatar, R.id.check_male, R.id.check_female, R.id.complete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.avatar:
                chooseImg();
                break;
            case R.id.check_male:
                if (mCheckFemale.isSelected()) mCheckFemale.setSelected(false);
                mCheckMale.setSelected(!mCheckMale.isSelected());
                if (nickNameInputed && mCheckMale.isSelected() || mCheckFemale.isSelected()) {
                    mComplete.setEnabled(true);
                } else {
                    mComplete.setEnabled(false);
                }

                break;
            case R.id.check_female:
                if (mCheckMale.isSelected()) mCheckMale.setSelected(false);
                mCheckFemale.setSelected(!mCheckFemale.isSelected());
                if (nickNameInputed && mCheckMale.isSelected() || mCheckFemale.isSelected()) {
                    mComplete.setEnabled(true);
                } else {
                    mComplete.setEnabled(false);
                }
                break;
            case R.id.complete:
                completeInfo();
                break;
        }
    }

    private void completeInfo() {
        if (mModel==null)
        mModel=new InfoImproveModel(this);
        String mNickName = mEtNickname.getText().toString();
        int gender = 3;//默认性别为3
        if (mCheckFemale.isSelected()) gender = 1;
        if (mCheckMale.isSelected()) gender = 2;
        mModel.addInfo(mNickName, gender, new AppBaseResponseCallBack<NovateResponse>(this) {
            @Override
            public void onSuccee(NovateResponse response) {
                setResult(RESULT_OK);
                finish();
            }
        });
    }

    private void uploadAvatar() {
        mModel = new InfoImproveModel(this);
        mModel.uploadAvatar(mAvatarString, new AppBaseResponseCallBack<NovateResponse>(this) {
            @Override
            public void onSuccee(NovateResponse response) {

            }
        });
    }

    private void chooseImg() {
        ImageLoader mLoader = new ImageLoader() {
            @Override
            public void displayImage(Context context, String path, final ImageView imageView) {
                Glide.with(context).load(path).placeholder(R.drawable.app_logo).into(new ImageViewTarget<GlideDrawable>(imageView) {
                    @Override
                    protected void setResource(GlideDrawable resource) {
                        imageView.setImageDrawable(resource);
                    }
                });
            }
        };
        ImgSelConfig mConfig = new ImgSelConfig.Builder(this, mLoader)
                .multiSelect(false)
                .btnBgColor(Color.GRAY)// “确定”按钮背景 色
                // “确定”按钮文字颜色
                .btnTextColor(Color.BLUE)
                // 使用沉浸式状态栏
                .statusBarColor(Color.parseColor("#3F51B5"))
                // 返回图标ResId
                .backResId(R.drawable.ic_back)
                // 标题
                .title("图片")
                .rememberSelected(false)
                // 标题文字颜色
                .titleColor(Color.WHITE)
                // TitleBar背景色
                .titleBgColor(Color.parseColor("#3F51B5"))
                // 裁剪大小。needCrop为true的时候配置
//                .cropSize(1, 1, 200, 200)
                .needCrop(false)
                // 第一个是否显示相机
                .needCamera(true)
                .build();
        // 跳转到图片选择器
        ImgSelActivity.startActivity(this, mConfig, REQUEST_SELIMG);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SELIMG) {
            List<String> paths = data.getStringArrayListExtra(ImgSelActivity.INTENT_RESULT);
            if (paths != null && paths.size() > 0) {
                File mFile = new File(paths.get(0));
                com.grandmagic.readingmate.utils.ImageLoader.loadCircleImage(InformationImproveActivity.this
                        , paths.get(0), mAvatar);
                try {
                    mAvatarString = KitUtils.encodeBase64File(mFile);
                    uploadAvatar();
                } catch (Exception mE) {
                    mE.printStackTrace();
                }
            }
        }
    }
}
