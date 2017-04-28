package com.grandmagic.readingmate.bean.response;

/**
 * Created by lps on 2017/4/28.
 *
 * @version 1
 * @see
 * @since 2017/4/28 17:05
 */


public class SplashResponse {
    private int theme_id;
    private String photo;
    private String holiday_time;

    public int getTheme_id() {
        return theme_id;
    }

    public void setTheme_id(int mTheme_id) {
        theme_id = mTheme_id;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String mPhoto) {
        photo = mPhoto;
    }

    public String getHoliday_time() {
        return holiday_time;
    }

    public void setHoliday_time(String mHoliday_time) {
        holiday_time = mHoliday_time;
    }
}
