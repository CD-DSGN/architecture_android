package com.grandmagic.readingmate.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import com.tamic.novate.util.Environment;

/**
 * Created by zhangmengqi on 2017/2/9.
 */

public class KitUtils {
    //判断是否是合法手机号
    public static boolean checkMobilePhone(String phoneNum) {
            /*
     * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
     * 联通：130、131、132、152、155、156、185、186 电信：133、153、180、189、（1349卫通）
     * 总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
     */
        String telRegex = "[1][358]\\d{9}";// "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(phoneNum)) {
            return false;
        }
        else {
            return phoneNum.matches(telRegex);
        }
    }

    //获取应用版本名
    public static String getVersionName(Context context) {
        return getPackageInfo(context).versionName;
    }

    //获取应用版本号
    public static int getVersionCode(Context context) {
        return getPackageInfo(context).versionCode;
    }

    private static PackageInfo getPackageInfo(Context context) {
        PackageInfo pi = null;

        try {
            PackageManager pm = context.getPackageManager();
            pi = pm.getPackageInfo(context.getPackageName(),
                    PackageManager.GET_CONFIGURATIONS);

            return pi;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return pi;
    }

    public static String getAbsoluteUrl(String url) {
        return Environment.getUrl() + url;
    }

}
