package com.grandmagic.readingmate.consts;

/**
 * Created by zhangmengqi on 2017/2/9.
 */

public class ApiErrorConsts {
    public static int token_invalid = 1000010; // 未登录或登录过期
    public static int SCAN_ERROR = 2000001; //条形码有错
    public static int SCAN_NO_BOOK = 2000002; //数据库没有书籍信息
    public static int SUBSCIRBE_ERROR = 2000003; //关注图书失败
    public static int SUBSCRIBE_ALREADY = 2000004; //已经关注过此书
    public static int NO_UPDATE = 7000000; //没有更新

}
