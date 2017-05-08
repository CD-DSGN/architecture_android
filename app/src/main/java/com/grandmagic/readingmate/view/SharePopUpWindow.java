package com.grandmagic.readingmate.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.grandmagic.readingmate.R;
import com.grandmagic.readingmate.base.AppBaseApplication;
import com.grandmagic.readingmate.consts.AppConsts;
import com.grandmagic.readingmate.utils.AutoUtils;
import com.grandmagic.readingmate.utils.KitUtils;
import com.grandmagic.readingmate.utils.ViewUtils;
import com.orhanobut.logger.Logger;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * Created by zhangmengqi on 2017/3/17.
 */

public class SharePopUpWindow extends PopupWindow {
    public static final int MAX_LEN = 15;
    private Context mContext;
    private Activity mActivity;
    private UMShareListener umShareListener; //默认一个回调监听
    private ShareAction mShareAction_sina;   //为微博做一个特例
    private ShareAction mShareActionDefault;
    private UMShareAPI mShareAPI;


    ProgressDialog mProgressDialog;

    private boolean img_exit = false;


    public SharePopUpWindow(Context context) {
        mContext = context;
        mActivity = (Activity) mContext;
        View mpopview = LayoutInflater.from(mContext).inflate(R.layout.view_sharepop, null);
        AutoUtils.auto(mpopview);
        this.setContentView(mpopview);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);

        mShareAPI = UMShareAPI.get(AppBaseApplication.ctx);

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
            public void onError(SHARE_MEDIA platform, java.lang.Throwable t) {
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

        mShareAction_sina = new ShareAction((Activity) mContext);
        mShareAction_sina.setCallback(umShareListener);
        mShareAction_sina.setPlatform(SHARE_MEDIA.SINA);

        View ll = mpopview.findViewById(R.id.ll_share_sina);
        ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (!mShareAPI.isInstall(mActivity, SHARE_MEDIA.SINA)) {
//                    ViewUtils.showToast(mContext.getString(R.string.install_sina_client));
//                    return;
//                }
                mShareAction_sina.share();
                if (!mProgressDialog.isShowing()) {
                    mProgressDialog.show();
                }
            }
        });

        View ll_wx = mpopview.findViewById(R.id.ll_wx);
        ll_wx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mShareAPI.isInstall(mActivity, SHARE_MEDIA.WEIXIN)) {
                    ViewUtils.showToast(mContext.getString(R.string.install_wx_client));
                    return;
                }

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
                if (!mShareAPI.isInstall(mActivity, SHARE_MEDIA.WEIXIN_CIRCLE)) {
                    ViewUtils.showToast(mContext.getString(R.string.install_wx_client));
                    return;
                }
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


    public void dismissPorgressDlg() {
        if (mProgressDialog != null) {
            ViewUtils.safeCloseDialog(mProgressDialog);
        }
        this.dismiss();
    }


    public void show() {
        this.showAtLocation(mActivity.getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
        WindowManager.LayoutParams params = mActivity.getWindow().getAttributes();
        params.alpha = 0.7f;
        mActivity.getWindow().setAttributes(params);

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

    public ShareAction setSina(String url_img, String text) {
        UMImage image = new UMImage(mContext, url_img);
        mShareAction_sina.withMedia(image).withText(text);
        return mShareAction_sina;
    }

    public ShareAction setSina(int img_res, String text) {
        UMImage image = new UMImage(mContext, img_res);
        mShareAction_sina.withMedia(image).withText(text);
        return mShareAction_sina;
    }

    //分享图书
    public ShareAction setBookData(String book_name, String book_id, String book_cotent, String book_cover, String rate) {
        String str = getSinaBookText(book_name, book_cotent, rate, getShareUrl(book_id, 0));
        if (isImageExist(book_cover)) {
            setSina(KitUtils.getAbsoluteUrl(book_cover), str);
            return setData(book_name, "读家评分:"+ rate + "\n" + book_cotent, KitUtils.getAbsoluteUrl(book_cover), getShareUrl(book_id, 0), "大术读家");
        }else{
            setSina(R.drawable.logo_rect, str);
            return setData(book_name, "读家评分:"+ rate + "\n" + book_cotent, R.drawable.logo_rect, getShareUrl(book_id, 0), "大术读家");
        }
    }


    private String getSinaBookText(String book_name, String content, String rate, String share_url) {
        int end = 0;
        if (!TextUtils.isEmpty(content)) {
            end = content.length() > MAX_LEN ? MAX_LEN : content.length();
        }
        String str =  "《" + book_name + "》" + "读家评分:" + rate  + ",简介:" +
                content.substring(0, end);
        if (content.length() > MAX_LEN) {
            str += "...";
        }
        str += "(来自大术读家:" + share_url;
        return str;
    }


    private String getSinaCommentText(String book_name, String content, String rate, String share_url) {
        int end = 0;
        if (!TextUtils.isEmpty(content)) {
            end = content.length() > MAX_LEN ? MAX_LEN : content.length();
        }
        String str =  "关于对《" + book_name + "》的评论:" + content.substring(0, end);
        if (content.length() > MAX_LEN) {
            str += "...";
        }
        str += "(来自大术读家:" + share_url;
        return str;
    }


    //分享评论
    public ShareAction setCommentData(String book_name, String comment_id, String comment_cotent, String book_cover, String rate) {
        String str = getSinaCommentText(book_name, comment_cotent, rate, getShareUrl(comment_id, 1));
        if (isImageExist(book_cover)) {
            setSina(KitUtils.getAbsoluteUrl(book_cover), str);
            return setData(book_name, "读家评分:"+ rate + "\n" + comment_cotent, KitUtils.getAbsoluteUrl(book_cover), getShareUrl(comment_id, 1), "读家评论");
        }else{
            setSina(R.drawable.logo_rect, str);
            return setData(book_name, "读家评分:"+ rate + "\n" + comment_cotent, R.drawable.logo_rect, getShareUrl(comment_id, 1), "读家评论");
        }
    }


    public String getShareUrl(String id, int type) {
        switch (type) {
            case 0:
                return AppConsts.share_url + "bookId=" + id;
            case 1:
                return AppConsts.share_url + "commentId=" + id;
            default:
                return AppConsts.APP_URL;
        }
    }


    public boolean isImageExist(String url) {  //相对路径
        img_exit = false;
        if (TextUtils.isEmpty(url)) {
            return false;
        }

        String absolute_url = KitUtils.getAbsoluteUrl(url);

        OkHttpClient mOkHttpClient = new OkHttpClient.Builder().
                connectTimeout(1, SECONDS).readTimeout(1,SECONDS).build();
        final Request request = new Request.Builder()
                .url(absolute_url)
                .build();

        final Call call = mOkHttpClient.newCall(request);

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    Response response = call.execute();
                    if (response.isSuccessful()) {
                        img_exit = true;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    img_exit = false;
                }
            }
        });
        t.start();
        try {
            t.join(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return img_exit;
    }
}



