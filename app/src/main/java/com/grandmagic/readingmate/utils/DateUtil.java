package com.grandmagic.readingmate.utils;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by lps on 2017/3/13.
 */

public class DateUtil {

     /* <pre>
     *                          HH:mm    15:44
            *                         h:mm a    3:44 下午
     *                        HH:mm z    15:44 CST
     *                        HH:mm Z    15:44 +0800
            *                     HH:mm zzzz    15:44 中国标准时间
     *                       HH:mm:ss    15:44:40
            *                     yyyy-MM-dd    2016-08-12
            *               yyyy-MM-dd HH:mm    2016-08-12 15:44
            *            yyyy-MM-dd HH:mm:ss    2016-08-12 15:44:40
            *       yyyy-MM-dd HH:mm:ss zzzz    2016-08-12 15:44:40 中国标准时间
     *  EEEE yyyy-MM-dd HH:mm:ss zzzz    星期五 2016-08-12 15:44:40 中国标准时间
     *       yyyy-MM-dd HH:mm:ss.SSSZ    2016-08-12 15:44:40.461+0800
            *     yyyy-MM-dd'T'HH:mm:ss.SSSZ    2016-08-12T15:44:40.461+0800
            *   yyyy.MM.dd G 'at' HH:mm:ss z    2016.08.12 公元 at 15:44:40 CST
     *                         K:mm a    3:44 下午
     *               EEE, MMM d, ''yy    星期五, 八月 12, '16
            *          hh 'o''clock' a, zzzz    03 o'clock 下午, 中国标准时间
            *   yyyyy.MMMMM.dd GGG hh:mm aaa    02016.八月.12 公元 03:44 下午
     *     EEE, d MMM yyyy HH:mm:ss Z    星期五, 12 八月 2016 15:44:40 +0800
            *                  yyMMddHHmmssZ    160812154440+0800
            *     yyyy-MM-dd'T'HH:mm:ss.SSSZ    2016-08-12T15:44:40.461+0800
            * EEEE 'DATE('yyyy-MM-dd')' 'TIME('HH:mm:ss')' zzzz    星期五 DATE(2016-08-12) TIME(15:44:40) 中国标准时间
     * </pre>
            * 注意：SimpleDateFormat不是线程安全的，线程安全需用{@code ThreadLocal<SimpleDateFormat>}
     */
     public static final String DEFAULT_PATTERN = "yyyy-MM-dd HH:mm:ss";
     public static final String CHINESE_PATTERN = "yyyy年MM月dd日 HH:mm";//汉字日期
     public static final String SLANT_PATTERN = "yyyy/MM/dd HH:mm";//斜线分割的日期

    /**
     * 默认的格式化方法
     * @param time 时间戳字符串
     * @return 转换的日期
     */
    public static String timeTodate(String time) {
        long time1 = 0;
        if (time != null && time.length() == 10) {//unix日期是10位，java是13位。需要补齐
            try {
                time1 = Long.parseLong(time) * 1000;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                time1 = Long.parseLong(time);
            } catch (NumberFormatException mE) {
                mE.printStackTrace();
            }
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DEFAULT_PATTERN, Locale.getDefault());
        return simpleDateFormat.format(time1);
    }

    //没有直接返回null
    public static String timeTodate1(String time) {
        long time1 = 0;
        if (time != null && time.length() == 10) {//unix日期是10位，java是13位。需要补齐
            try {
                time1 = Long.parseLong(time) * 1000;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            return null;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DEFAULT_PATTERN, Locale.getDefault());
        return simpleDateFormat.format(time1);
    }


    /**
     * 时间转换
     * @param format 自己定义format格式，默认有三种
     * @param time 时间戳字符串
     * @return 转换后的日期
     */
    public static String timeTodate(String format,String time) {
        long time1 = 0;
        if (time != null && time.length() == 10) {
            try {
                time1 = Long.parseLong(time) * 1000;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                time1 = Long.parseLong(time);
            } catch (NumberFormatException mE) {
                mE.printStackTrace();
            }
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.getDefault());
        return simpleDateFormat.format(time1);
    }

    public static String getNowTime() {
     return    new SimpleDateFormat("HH:mm").format(System.currentTimeMillis());

    }
}