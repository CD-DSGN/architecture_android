package com.grandmagic.readingmate.consts;

/**
 * Created by zhangmengqi on 2017/2/10.
 */

public class ApiInterface {
    public  static final String  VERIFY_CODE = "register/sendcode" ;  //获取短信验证码
    public  static final String  REGISTER = "register/actregister" ;     //注册
    public  static final String  LOGIN = "login/logincheck" ;     //登录
    public  static final String  UPDATE = "update" ;     //更新
    public  static final String  RESET_PWD = "register/resetpwd"; //修改密码
    public  static final String  LOGOUT = "logout/exit"; //退出账户
    public  static final String  FEEDBACK = "suggest/useradvise"; //用户反馈
}
