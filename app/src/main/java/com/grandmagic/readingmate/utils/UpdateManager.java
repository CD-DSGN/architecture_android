package com.grandmagic.readingmate.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.grandmagic.readingmate.base.AppBaseResponseCallBack;
import com.grandmagic.readingmate.bean.response.UpdateResponse;
import com.grandmagic.readingmate.consts.ApiInterface;
import com.grandmagic.readingmate.dialog.HintDialog;
import com.tamic.novate.Novate;
import com.tamic.novate.NovateResponse;
import com.tamic.novate.Throwable;
import com.tamic.novate.download.DownLoadCallBack;
import com.tamic.novate.util.NetworkUtil;

import java.io.File;
import java.util.HashMap;

/**
 * Created by lps
 * on 2017/2/10. 检测版本
 */

public class UpdateManager {
    public static final int VERSION_SERVER = -1;
    private Context mContext;
    private Novate mNovate;
    private AlertDialog mAlertDialog;
    private AlertDialog.Builder mDialog;

    public UpdateManager(Context mContext) {
        this.mContext = mContext;
    }

    public void getVersionCode(final int localVersion) {
        mNovate = new Novate.Builder(mContext).build();
        mNovate.executeGet(ApiInterface.UPDATE, new HashMap<String, Object>(),
                new AppBaseResponseCallBack<NovateResponse<UpdateResponse>>(mContext) {
            @Override
            public void onSuccee(NovateResponse<UpdateResponse> response) {
                if (localVersion != response.getData().getServersion()) {
                    showUpdateDialog(response.getData().getUrl());
                }
            }
        });
    }

    private void showUpdateDialog(final String url) {
        mDialog = new AlertDialog.Builder(mContext);
        String s = NetworkUtil.isWifi(mContext) ? "您目前在WIFI环境，是否升级" : "您目前不在在WIFI环境，是否升级";
        mDialog.setTitle("有新的版本");
        mDialog.setMessage(s);
        mDialog.setNegativeButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                download(url);
            }
        });
        mDialog.setPositiveButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mAlertDialog.dismiss();
            }
        });
        mAlertDialog = mDialog.create();
        mAlertDialog.show();
        if (NetworkUtil.isWifi(mContext)) {
            download(url);
        }
    }

    private void download(String mUrl) {
        Novate mNovate1 = new Novate.Builder(mContext).baseUrl("http://alensw.com/").build();
        mNovate1.download(mUrl, new DownLoadCallBack() {
            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onProgress(String key, long fileSizeDownloaded, long totalSize) {
                Log.e("download", "onProgress: "+fileSizeDownloaded/totalSize );
            }

            @Override
            public void onSucess(String key, String path, String name, long fileSize) {
                File apkfile = new File(path);
                if (!apkfile.exists()) {
                    Toast.makeText(mContext, "安装遇到问题,文件不存在?", Toast.LENGTH_SHORT).show();
                } else {
                    Intent i = new Intent();
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.setDataAndType(Uri.parse("file://" + apkfile.getAbsolutePath()), "application/vnd.android.package-archive");
               mContext.startActivity(i);
                }
            }
        });
    }
}
