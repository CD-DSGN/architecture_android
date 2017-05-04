package com.grandmagic.readingmate.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.grandmagic.readingmate.R;
import com.grandmagic.readingmate.base.AppBaseActivity;
import com.grandmagic.readingmate.base.AppBaseResponseCallBack;
import com.grandmagic.readingmate.bean.request.UploadImgBean;
import com.grandmagic.readingmate.bean.response.ImageUrlResponseBean;
import com.grandmagic.readingmate.bean.response.UploadAvarResponseBean;
import com.grandmagic.readingmate.bean.response.UserInfoResponseBean;
import com.grandmagic.readingmate.consts.ApiInterface;
import com.grandmagic.readingmate.fragment.PersonalFragment;
import com.grandmagic.readingmate.model.UserInfoModel;
import com.grandmagic.readingmate.ui.CustomDialogWithOneBtn;
import com.grandmagic.readingmate.ui.GenderListDialog;
import com.grandmagic.readingmate.ui.ListDialog;
import com.grandmagic.readingmate.ui.UploadAvarDlg;
import com.grandmagic.readingmate.utils.AutoUtils;
import com.grandmagic.readingmate.utils.KitUtils;
import com.grandmagic.readingmate.utils.ViewUtils;
import com.tamic.novate.Novate;
import com.tamic.novate.NovateResponse;
import com.tamic.novate.Throwable;
import com.tamic.novate.download.DownLoadCallBack;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.yuyh.library.imgsel.ImageLoader;
import com.yuyh.library.imgsel.ImgSelActivity;
import com.yuyh.library.imgsel.ImgSelConfig;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.functions.Action1;

public class PersonalInfoEditActivity extends AppBaseActivity {

    @BindView(R.id.back)
    ImageView mBack;
    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.iv_avar)
    ImageView mIvAvar;
    @BindView(R.id.tv_nickname)
    TextView mTvNickname;
    @BindView(R.id.tv_signature)
    TextView mTvSignature;
    @BindView(R.id.iv_gender)
    ImageView mIvGender;
    @BindView(R.id.rl_set_gender)
    RelativeLayout mRlSetGender;
    @BindView(R.id.tv_gender)
    TextView mTvGender;

    private UserInfoModel mUserInfoModel;
    private UploadAvarDlg mUploadAvarDlg;

    private static final int SEL_IMG = 1;
    private static final int TAKE_PHOTO = 2;
    private static final int CROP_SMALL_PICTURE = 3; //压缩拍照图片

    private String mPhotoName;
    private String PHOTO_TMP = "DSGN:tmp.jpg";

    private String path;

    private UserInfoResponseBean mUserInfoResponseBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info_edit);
        setTranslucentStatus(true);
        AutoUtils.auto(this);
        ButterKnife.bind(this);
        initView();
        loadData();
    }


    private void loadData() {
        mUserInfoModel = new UserInfoModel(PersonalInfoEditActivity.this,
                new AppBaseResponseCallBack<NovateResponse<UserInfoResponseBean>>(PersonalInfoEditActivity.this, true) {

                    @Override
                    public void onSuccee(NovateResponse<UserInfoResponseBean> response) {
                        PersonalFragment.NEED_REFRESH = true;
                        if (response != null && response.getData() != null) {
                            mUserInfoResponseBean = response.getData();
                            setView(mUserInfoResponseBean);
                        }
                    }
                });
        mUserInfoModel.getUserInfo();
    }

    private void setView(UserInfoResponseBean userInfoResponseBean) {
        if (!TextUtils.isEmpty(userInfoResponseBean.getUser_name())) {
            mTvNickname.setText(KitUtils.unicodeDecode(userInfoResponseBean.getUser_name()));
        }

        if (!TextUtils.isEmpty(userInfoResponseBean.getSignature())) {
            mTvSignature.setText(KitUtils.unicodeDecode(userInfoResponseBean.getSignature()));
        }

        setGenderView(userInfoResponseBean.getGender());

        ImageUrlResponseBean imageUrlResponseBean = userInfoResponseBean.getAvatar_url();
        if (imageUrlResponseBean != null) {
            if (!TextUtils.isEmpty(imageUrlResponseBean.getLarge())) {
                com.grandmagic.readingmate.utils.ImageLoader.loadCircleImage(
                        PersonalInfoEditActivity.this,
                        KitUtils.getAbsoluteUrl(imageUrlResponseBean.getLarge()), mIvAvar);
            }
        }
    }

    private void initView() {
        mTitle.setText(getString(R.string.edit_info));
    }


    @OnClick({R.id.back, R.id.iv_avar, R.id.tv_nickname, R.id.tv_signature, R.id.rl_set_gender})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.iv_avar:
                //弹出相应的dlg
                if (mUploadAvarDlg == null) {
                    mUploadAvarDlg = new UploadAvarDlg(PersonalInfoEditActivity.this);
                    mUploadAvarDlg.setOnitemClickListener(new ListDialog.OnitemClickListener() {
                        @Override
                        public void onClick(int postion) {
                            switch (postion) {
                                case 0:             //拍照
                                    takePhoto();
                                    break;
                                case 1:
                                    pickFromAlbum();
                                    break;          //从相册选择
                                case 2:
                                    download();
                                    break;          //保存图片
                            }
                            mUploadAvarDlg.dismiss();
                        }
                    });

                }
                mUploadAvarDlg.show();

                break;
            case R.id.tv_nickname:
                showNickDialog();
                break;
            case R.id.tv_signature:
                showSignDialog();
                break;
            case R.id.rl_set_gender:
                showSetGenderDlg();
                break;
        }
    }

    private void download() {
        if (mUserInfoResponseBean != null) {
            ImageUrlResponseBean image_url = mUserInfoResponseBean.getAvatar_url();
            if (image_url != null) {
                String url = image_url.getLarge();
                Novate novate = new Novate.Builder(this).build();
                novate.download(KitUtils.getAbsoluteUrl(url), new DownLoadCallBack() {
                    @Override
                    public void onError(Throwable e) {
                        ViewUtils.showToast(getString(R.string.download_fail));
                    }

                    @Override
                    public void onSucess(String key, String path, String name, long fileSize) {
                        ViewUtils.showToast(getString(R.string.downlaod_success));
                    }
                });
            }
        }


    }

    private GenderListDialog mGenderDlg;

    private void showSetGenderDlg() {
        if (mGenderDlg == null) {
            mGenderDlg = new GenderListDialog(PersonalInfoEditActivity.this);
        }
        mGenderDlg.show();
        mGenderDlg.setOnitemClickListener(new ListDialog.OnitemClickListener() {
            @Override
            public void onClick(int postion) {
                int gender = -1;
                if (postion == 0) {  //男
                    gender = 2;
                } else {         //女
                    gender = 1;
                }
                mUserInfoModel.setGender(gender);
                mGenderDlg.dismiss();
            }
        });
    }

    private CustomDialogWithOneBtn mSignDlg;

    private void showSignDialog() {
        if (mSignDlg == null) {
            mSignDlg = new CustomDialogWithOneBtn(PersonalInfoEditActivity.this, R.style.CustomDialog_bgdim);
            mSignDlg.setMaxNum(20);

            mSignDlg.setYesStr(getString(R.string.save));
            mSignDlg.setTitle(getString(R.string.change_sign));
        }
        mSignDlg.show();
        mSignDlg.setNeedTextLimit(true);
        mSignDlg.setOnBtnOnclickListener(new CustomDialogWithOneBtn.BtnOnclickListener() {
            @Override
            public void onYesClick() {
                String sign = mSignDlg.getMessage();
                mUserInfoModel.setsign(sign);
                mSignDlg.dismiss();
            }
        });
    }

    private CustomDialogWithOneBtn mNickNameDialog;

    private void showNickDialog() {
        if (mNickNameDialog == null) {
            mNickNameDialog = new CustomDialogWithOneBtn(PersonalInfoEditActivity.this, R.style.CustomDialog_bgdim);
            mNickNameDialog.setMaxNum(10);

            mNickNameDialog.setYesStr(getString(R.string.save));
            mNickNameDialog.setTitle(getString(R.string.edit_nickname));
        }
        mNickNameDialog.show();
        mNickNameDialog.setNeedTextLimit(true);
        mNickNameDialog.setOnBtnOnclickListener(new CustomDialogWithOneBtn.BtnOnclickListener() {
            @Override
            public void onYesClick() {
                String user_name = mNickNameDialog.getMessage();
                mUserInfoModel.setUserName(user_name);
                mNickNameDialog.dismiss();
            }
        });
    }


    public void setGenderView(int genderView) {
        if (genderView == 1) {   //女
            mIvGender.setImageResource(R.drawable.iv_female);
            mTvGender.setText(getString(R.string.female));
        } else if (genderView == 2) {                  //男
            mIvGender.setImageResource(R.drawable.iv_male);
            mTvGender.setText(getString(R.string.male));
        } else {  //未设置

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case SEL_IMG:
                    List<String> paths = data.getStringArrayListExtra(ImgSelActivity.INTENT_RESULT);
                    path = paths.get(0);
                    File file = new File(path);
                    upload_img(file);
                    break;

                case TAKE_PHOTO:
                    File file1 = new File(Environment.getExternalStorageDirectory(), mPhotoName);  //拍照上传还需要压缩
                    startPhotoZoom(Uri.fromFile(file1));
                    break;

                case CROP_SMALL_PICTURE:  //拍照的压缩
                    compressAndUpload(data);
                    break;

            }
        }
    }

    private void compressAndUpload(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            Bitmap bitmap = extras.getParcelable("data");
            FileOutputStream b = null;
            File tmp = new File(Environment.getExternalStorageDirectory(), PHOTO_TMP);
            try {
                b = new FileOutputStream(tmp);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                try {
                    b.flush();
                    b.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (tmp.exists()) {
                upload_img(tmp);
            }
        }
    }

    //改用base64传
    private void upload_img(File file) {
        Novate novate = new Novate.Builder(this).build();

        try {
            String str = KitUtils.encodeBase64File(file);
            UploadImgBean uploadImgBean = new UploadImgBean();
            uploadImgBean.setAvatar(str);
            novate.executeJson(ApiInterface.UPLOAD_AVAR, uploadImgBean.toGson(), new AppBaseResponseCallBack<NovateResponse<UploadAvarResponseBean>>(this) {

                @Override
                public void onSuccee(NovateResponse<UploadAvarResponseBean> response) {
                    UploadAvarResponseBean uploadAvarResponseBean = response.getData();
                    PersonalFragment.NEED_REFRESH = true; //上传成功
                    ImageUrlResponseBean imageUrlResponseBean = uploadAvarResponseBean.getAvatar_url();
                    if (imageUrlResponseBean != null) {
                        String url_large = imageUrlResponseBean.getLarge();
                        if (!TextUtils.isEmpty(url_large))
                            com.grandmagic.readingmate.utils.ImageLoader.loadCircleImage(PersonalInfoEditActivity.this,
                                    KitUtils.getAbsoluteUrl(url_large), mIvAvar);
                    }

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void pickFromAlbum() {
        ImageLoader loader = new ImageLoader() {
            @Override
            public void displayImage(Context context, String path, ImageView imageView) {
                com.grandmagic.readingmate.utils.ImageLoader.loadImage(context, path, imageView);
            }
        };
        // 配置选项
        ImgSelConfig config = new ImgSelConfig.Builder(this, loader)
                // 是否多选
                .multiSelect(false)
                //是否记住以前的选择
                .rememberSelected(false)
                // “确定”按钮背景色
                .btnBgColor(Color.GRAY)
                // “确定”按钮文字颜色
                .btnTextColor(Color.BLUE)
                // 标题
                .title("图片")
                // 标题文字颜色
                .titleColor(Color.WHITE)
                // TitleBar背景色
                .titleBgColor(Color.parseColor("#3F51B5"))
                // 裁剪大小。needCrop为true的时候配置
                .cropSize(1, 1, 200, 200)
                .needCrop(true)
                // 第一个是否显示相机
                .needCamera(false)
                // 最大选择图片数量
                .maxNum(1)
                .build();
        ImgSelActivity.startActivity(this, config, SEL_IMG);
    }

    private void takePhoto() {
        //获取权限
        final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        mPhotoName = "DSGN:" + System.currentTimeMillis() + ".jpg";
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment
                .getExternalStorageDirectory(), mPhotoName
        )));

        new RxPermissions(this).request(Manifest.permission.CAMERA).subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean b) {
                if (b) {
                    startActivityForResult(intent, TAKE_PHOTO);
                } else {
                    Toast.makeText(PersonalInfoEditActivity.this, "你没有给予权限。无法打开相机", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    protected void startPhotoZoom(Uri uri) {
        if (uri == null) {
            Log.i("tag", "The uri is not exist.");
        }
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 200);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, CROP_SMALL_PICTURE);
    }
}
