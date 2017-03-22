package com.grandmagic.readingmate.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by lps on 2017/3/13.
 */

public class DateUtil {
    public static String timeTodate(String time){
        long time1 = 0;
        if (time!=null&&time.length()==10){
            try {
                time1 = Long.parseLong(time) * 1000;
            }catch (Exception e){
                e.printStackTrace();
            }
        }else {
            try {
                time1 = Long.parseLong(time);
            } catch (NumberFormatException mE) {
                mE.printStackTrace();
            }
        }
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy年MM月dd日 HH:mm", Locale.getDefault());
        String d=simpleDateFormat.format(time1);
        return d;
    }
}