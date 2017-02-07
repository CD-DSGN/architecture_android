package com.grandmagic.readingmate.utils;


import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by lps on 2017/2/7.
 */

public class SPUtils {
    public static final String DEFAULT_SP_NAME ="userinfo";
    public static final String IS_FIRST="isFirst";
    public static final String IS_LOGIN="islogin";
static SPUtils mSPUtils;
    public static SPUtils getInstance(){
        if (mSPUtils==null){
            mSPUtils=new SPUtils();
        }
        return mSPUtils;
    }
    /**
     * 是否第一次进入app
     * @param mContext
     * @return
     */
    public  boolean isFirst(Context mContext) {
        SharedPreferences mSharedPreferences = mContext.getSharedPreferences(DEFAULT_SP_NAME, Context.MODE_PRIVATE);
       return mSharedPreferences.getBoolean(IS_FIRST,true);
    }

    /**
     * 用户是否登录
     * @param mContext
     * @return
     */
    public  boolean isLogin(Context mContext){
        SharedPreferences mSharedPreferences = mContext.getSharedPreferences(DEFAULT_SP_NAME, Context.MODE_PRIVATE);
        return mSharedPreferences.getBoolean(IS_LOGIN,false);
    }

    /**
     * 向DEFAULT_SP_NAME中putstring
     * @param mContext
     * @param key
     * @param value
     */
    public  void putString(Context mContext,String key,String value){
        SharedPreferences mSharedPreferences = mContext.getSharedPreferences(DEFAULT_SP_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putString(key,value);
        mEditor.apply();
    }
    public  void putString(Context mContext,String spname,String key,String value){
        SharedPreferences mSharedPreferences = mContext.getSharedPreferences(spname, Context.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putString(key,value);
        mEditor.apply();
    }

    /**
     * 从DEFAULT_SP_NAME中getString
     * @param mContext
     * @param key
     * @return
     */
    public  String getString(Context mContext,String key){
        SharedPreferences mSharedPreferences = mContext.getSharedPreferences(DEFAULT_SP_NAME, Context.MODE_PRIVATE);
      return mSharedPreferences.getString(key,"");
    }
    public  String getString(Context mContext,String spname,String key){
        SharedPreferences mSharedPreferences = mContext.getSharedPreferences(DEFAULT_SP_NAME, Context.MODE_PRIVATE);
        return mSharedPreferences.getString(key,"");
    }

    /**
     * 向DEFAULT_SP_NAME中putBoolean
     * @param mContext
     * @param key
     * @param value
     */
    public void putBoolean(Context mContext,String key,boolean value){
        SharedPreferences mSharedPreferences = mContext.getSharedPreferences(DEFAULT_SP_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putBoolean(key,value);
        mEditor.apply();
    }

}
