package com.grandmagic.readingmate.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.provider.MediaStore;
import android.widget.ImageView;

import com.grandmagic.readingmate.R;
import com.yuyh.library.imgsel.ImageLoader;
import com.yuyh.library.imgsel.ImgSelActivity;
import com.yuyh.library.imgsel.ImgSelConfig;

import java.util.ArrayList;

/**
 * Created by zhangmengqi on 2017/3/8.
 */

public class UploadAvarDlg extends ListDialog implements ListDialog.OnitemClickListener {
    public static final int PICK_FROM_ALBUM = 1;

    public UploadAvarDlg(Context context) {
        super(context);
        setTitleStr(context.getString(R.string.change_avar));
        ArrayList<String> data = new ArrayList<String>();
        data.add(context.getString(R.string.take_photo));
        data.add(context.getString(R.string.pick_from_album));
        data.add(context.getString(R.string.save_photo));
        setData(data);
        setOnitemClickListener(this);
    }

    @Override
    public void onClick(int postion) {
        switch (postion) {
            case 0:
                //照相
                takePhoto();
                break;
            case 1:
                //从相册选择照片
                pickFromAlbum();
                break;
            case 2:
                //保存图片
                break;
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
        ImgSelConfig config = new ImgSelConfig.Builder(mContext,loader)
                // 是否多选
                .multiSelect(false)
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
                ImgSelActivity.startActivity((Activity) mContext, config, PICK_FROM_ALBUM);
    }

    private void takePhoto() {
        //需要权限
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        mContext.startActivity(intent);
    }
}
