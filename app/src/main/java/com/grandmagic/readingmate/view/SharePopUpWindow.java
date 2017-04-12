package com.grandmagic.readingmate.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.grandmagic.readingmate.R;
import com.grandmagic.readingmate.utils.AutoUtils;
import com.grandmagic.readingmate.utils.ViewUtils;
import com.orhanobut.logger.Logger;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

/**
 * Created by zhangmengqi on 2017/3/17.
 */

public class SharePopUpWindow extends PopupWindow {
    private Context mContext;
    private Activity mActivity;
    private UMShareListener umShareListener; //默认一个回调监听
    private ShareAction mShareAction;
    private ShareAction mShareActionDefault;

    ProgressDialog mProgressDialog;

    public SharePopUpWindow(Context context) {
        mContext = context;
        mActivity = (Activity) mContext;
        View mpopview = LayoutInflater.from(mContext).inflate(R.layout.view_sharepop, null);
        AutoUtils.auto(mpopview);
        this.setContentView(mpopview);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);

        this.setBackgroundDrawable(new BitmapDrawable());
        this.setClippingEnabled(true);
        this.setOutsideTouchable(true);
        this.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams params = mActivity.getWindow().getAttributes();
                params.alpha = 1.0f;
                mActivity.getWindow().setAttributes(params);
            }
        });
        this.setFocusable(true);

        mProgressDialog = new ProgressDialog(mContext);

        umShareListener = new UMShareListener() {
            @Override
            public void onStart(SHARE_MEDIA platform) {
                //分享开始的回调
                ViewUtils.safeShowDialog(mProgressDialog);
            }
            @Override
            public void onResult(SHARE_MEDIA platform) {

                Toast.makeText(mContext, platform + " 分享成功啦", Toast.LENGTH_SHORT).show();
                ViewUtils.safeCloseDialog(mProgressDialog);
                dismiss();

            }

            @Override
            public void onError(SHARE_MEDIA platform, Throwable t) {
                Toast.makeText(mContext,platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
                ViewUtils.safeCloseDialog(mProgressDialog);
                if(t!=null){
                    Logger.e(t.getMessage());   //输出失败错误信息
                    dismiss();
                }
            }

            @Override
            public void onCancel(SHARE_MEDIA platform) {
                Toast.makeText(mContext,platform + " 分享取消了", Toast.LENGTH_SHORT).show();
                ViewUtils.safeCloseDialog(mProgressDialog);
                dismiss();
            }
        };

        mShareActionDefault = new ShareAction((Activity) mContext);
        mShareActionDefault.setCallback(umShareListener);


//        final UMImage imagelocal; //测试
//        imagelocal = new UMImage(mContext, R.drawable.logo);
//        imagelocal.setThumb(new UMImage(mContext, R.drawable.logo));
//        final UMWeb web = new UMWeb("http://www.baidu.com");
//        web.setThumb(imagelocal);
        View ll = mpopview.findViewById(R.id.ll_share_sina);
        ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mShareActionDefault.setPlatform(SHARE_MEDIA.SINA).share();
//                new ShareAction(mActivity).setPlatform(SHARE_MEDIA.SINA)
//                        .withText("hello")
//                        .withMedia(web)
//                        .setCallback(umShareListener)
//                        .share();
                if (!mProgressDialog.isShowing()) {
                    mProgressDialog.show();
                }
            }
        });

        View ll_wx = mpopview.findViewById(R.id.ll_wx);
        ll_wx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                new ShareAction(mActivity).setPlatform(SHARE_MEDIA.WEIXIN)
//                        .withText("hello")
//                        .withMedia(web)
//                        .setCallback(umShareListener)
//                        .share();
                mShareActionDefault.setPlatform(SHARE_MEDIA.WEIXIN).share();
                if (!mProgressDialog.isShowing()) {
                    mProgressDialog.show();
                }
            }
        });

        View ll_friends_circle = mpopview.findViewById(R.id.ll_friends_circle);
        ll_friends_circle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                new ShareAction(mActivity).setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE)
//                        .withText("hello")
//                        .withMedia(web)
//                        .setCallback(umShareListener)
//                        .share();
                mShareActionDefault.setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE).share();
                if (!mProgressDialog.isShowing()) {
                    mProgressDialog.show();
                }
            }
        });

        View cancel = mpopview.findViewById(R.id.tv_pop_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharePopUpWindow.this.dismiss();
            }
        });

    }


    public void show() {
        this.showAtLocation(mActivity.getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
        WindowManager.LayoutParams params = mActivity.getWindow().getAttributes();
        params.alpha = 0.7f;
        mActivity.getWindow().setAttributes(params);

    }

    public void setShareAction(ShareAction shareAction) {
        mShareAction = shareAction;
    }


    /***
     *
     * @param title
     * @param desc
     * @param pic_id R.drawable.id
     * @param url
     * @return
     */


    public ShareAction setData(String title, String desc, int pic_id, String url, String content) {
        UMWeb web = new UMWeb(url);
        web.setTitle(title);
        web.setDescription(desc);
        UMImage image = new UMImage(mContext, pic_id);
        web.setThumb(image);
        mShareActionDefault.withMedia(web).withText(content);
        return mShareActionDefault;
    }

    /****
     *
     * @param title
     * @param desc
     * @param pic_url  图片网址
     * @param url
     * @param content
     * @return
     */

    public ShareAction setData(String title, String desc, String pic_url, String url, String content) {
        UMWeb web = new UMWeb(url);
        web.setTitle(title);
        web.setDescription(desc);
        UMImage image = new UMImage(mContext, pic_url);
        web.setThumb(image);
        mShareActionDefault.withMedia(web).withText(content);
        return mShareActionDefault;
    }
}



