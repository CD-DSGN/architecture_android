package com.grandmagic.readingmate.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.grandmagic.readingmate.R;
import com.hyphenate.easeui.utils.GlideCircleTransform;
import com.hyphenate.easeui.utils.GlideRoundTransform;

/**
 * Created by zhangmengqi on 2017/1/22.
 */

public class ImageLoader {
    /**
     * 普通的加载图片
     *
     * @param mContext context
     * @param url      图片路径
     * @param target   展示的imageview
     */
    public static void loadImage(Context mContext, String url, ImageView target) {
        Glide.with(mContext).load(url).placeholder(R.drawable.app_logo)
                .error(R.drawable.app_logo).into(target);
    }

    /**
     * 专用加载图书的封面的。主要是error和placeholder区别
     * @param mContext
     * @param url
     * @param target
     */
    public static void loadBookImg(Context mContext,String url,ImageView target){
    Glide.with(mContext).load(url).placeholder(R.drawable.iv_no_book_cover)
            .error(R.drawable.iv_no_book_cover).into(target);
}
    /**
     * 加载圆角图片（默认圆角）
     *
     * @param mContext context
     * @param url      路径
     * @param target   展示的imageview
     */
    public static void loadRoundImage(Context mContext, String url, ImageView target) {
        Glide.with(mContext).load(url)
                .transform(new GlideRoundTransform(mContext))
                .placeholder(R.drawable.app_logo)
                .error(R.drawable.app_logo)
                .into(target);
    }

    /**
     * 加载指定大小的圆角
     *
     * @param mContext context
     * @param url      路径
     * @param target   展示的imageview
     * @param round    圆角的大小
     */
    public static void loadRoundImage(Context mContext, String url, ImageView target, float round) {
        Glide.with(mContext).load(url)
                .transform(new GlideRoundTransform(mContext, round))
                .placeholder(R.drawable.app_logo)
                .error(R.drawable.app_logo)
                .into(target);
    }

    /**
     * 加载圆形图片
     *
     * @param mContext context
     * @param url      路径
     * @param target   展示的imageview
     */
    public static void loadCircleImage(Context mContext, String url, ImageView target) {
        Glide.with(mContext).load(url)
                .transform(new GlideCircleTransform(mContext))
                .placeholder(R.drawable.app_logo)
                .error(R.drawable.app_logo)
                .into(target);
    }

    public static void loadImage(Context mContext, String url, ImageView target, int error_id) {
        Glide.with(mContext).load(url).placeholder(error_id)
                .error(error_id).into(target);
    }



}
