package com.grandmagic.readingmate.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.grandmagic.readingmate.R;
import com.grandmagic.readingmate.base.AppBaseActivity;
import com.grandmagic.readingmate.ui.CustomDialogWithOneBtn;
import com.grandmagic.readingmate.ui.UploadAvarDlg;
import com.grandmagic.readingmate.utils.AutoUtils;
import com.grandmagic.readingmate.utils.ViewUtils;
import com.grandmagic.readingmate.view.CircleImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PersonalInfoEditActivity extends AppBaseActivity {

    @BindView(R.id.back)
    ImageView mBack;
    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.iv_avar)
    CircleImageView mIvAvar;
    @BindView(R.id.tv_nickname)
    TextView mTvNickname;
    @BindView(R.id.tv_signature)
    TextView mTvSignature;
    @BindView(R.id.iv_gender)
    ImageView mIvGender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info_edit);
        AutoUtils.auto(this);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        mTitle.setText(getString(R.string.edit_info));
    }


    @OnClick({R.id.back, R.id.iv_avar, R.id.tv_nickname, R.id.tv_signature, R.id.iv_gender})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.iv_avar:
                //弹出相应的dlg
                new UploadAvarDlg(PersonalInfoEditActivity.this).show();
                break;
            case R.id.tv_nickname:
                showNickDialog();
                break;
            case R.id.tv_signature:
                showSignDialog();
                break;
            case R.id.iv_gender:
                break;
        }
    }

    private CustomDialogWithOneBtn mSignDlg;
    private void showSignDialog() {
        if (mSignDlg == null) {
            mSignDlg = new CustomDialogWithOneBtn(PersonalInfoEditActivity.this, R.style.CustomDialog_bgdim);
            mSignDlg.setMaxNum(20);
            mSignDlg.setOnBtnOnclickListener(new CustomDialogWithOneBtn.BtnOnclickListener(){
                @Override
                public void onYesClick() {
                    ViewUtils.showToast(getString(R.string.save));
                    //调用接口，修改签名
                }

            });
            mSignDlg.setYesStr(getString(R.string.save));
            mSignDlg.setTitle(getString(R.string.change_sign));
        }
        mSignDlg.show();
        mSignDlg.setNeedTextLimit(true);
    }

    private CustomDialogWithOneBtn mNickNameDialog;

    private void showNickDialog() {
        if (mNickNameDialog == null) {
            mNickNameDialog = new CustomDialogWithOneBtn(PersonalInfoEditActivity.this, R.style.CustomDialog_bgdim);
            mNickNameDialog.setMaxNum(10);
            mNickNameDialog.setOnBtnOnclickListener(new CustomDialogWithOneBtn.BtnOnclickListener(){
                @Override
                public void onYesClick() {
                    ViewUtils.showToast(getString(R.string.save));
                    //调用接口，修改昵称

                }

            });
            mNickNameDialog.setYesStr(getString(R.string.save));
            mNickNameDialog.setTitle(getString(R.string.edit_nickname));
        }
        mNickNameDialog.show();
        mNickNameDialog.setNeedTextLimit(true);
    }
}
