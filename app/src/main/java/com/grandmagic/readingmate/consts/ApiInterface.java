package com.grandmagic.readingmate.consts;

/**
 * Created by zhangmengqi on 2017/2/10.
 */

public class ApiInterface {
    public static final String VERIFY_CODE = "register/sendcode";  //获取短信验证码
    public static final String REGISTER = "register/actregister";     //注册
    public static final String LOGIN = "login/logincheck";     //登录
    public static final String UPDATE = "version/updateversion";     //更新
    public static final String RESET_PWD = "register/resetpwd"; //修改密码
    public static final String LOGOUT = "logout/exit"; //退出账户
    public static final String FEEDBACK = "suggest/useradvise"; //用户反馈
    public static final String SHOWFRIEND = "friendinfo/showfriend"; //获取好友列表
    public static final String SEARCH_USER = "friendinfo/searchuser"; //查找用户
    public static final String COLLECT_SCAN = "collect/scan"; //根据ISBN查找图书
    public static final String COLLECT_BOOKDISPLAY = "collect/bookdisplay"; //首页显示的图书
    public static final String REQUEST_ADD = "requestinfo/requestadd"; //首页显示的图书
    public static final String REQUESTINFO_REQUESTLIST = "requestinfo/requestlist"; //好友请求列表
    public static final String REQUEST_REFUSE = "requestinfo/requestrefuse"; //拒绝好友请求
    public static final String REQUEST_AGREE = "requestinfo/requestagree"; //同意好友请求
    public static final String BOOK_SEARCH = "keyword/booksearch"; //根据关键字搜索图书
    public static final String BOOK_HOT = "keyword/gethotword"; //获取热门搜索
    public static final String BOOK_DETAIL = "collect/getbookdetail"; //获取图书详情
    public static final String BOOK_COMMENTSCORESTATUS = "collect/getcommentscorestatus"; //我过往对该书的评价
    public static final String SCORE_BOOK = "collect/commentorscorebook"; //对图书评论
    public static final String DELETE_BOOKCOMMENT = "collect/deletebookcomment"; //删除图书的评论
    public static final String THUMB_BOOKCOMMENT = "collect/thumbupbookcomment"; //对评论点赞
    public static final String REMOVE_FRIEEND = "friendinfo/removefriend"; //解除好友关系
    public static final String REMARK_FRIEND = "friendinfo/remarkfriend"; //设置备注
    public static final String STEP_LOCATION = "location/setposition"; //上传用户定位
    public static final String GET_LOCATION_PERSON = "location/getposition"; //获取定位附近的好友
    public static final String BOOK_COMMENT_DETAIL = "collect/getbookcommentsdetail"; //获取图书的评论
    public static final String PERSON_COMMENT_DETAIL = "comment/commentuser"; //获取用户详细页的评论
    public static final String BOOK_PERSON_COLLECT = "comment/collectuser"; //查找用户对应的收藏书籍
    public static final String BOOK_ALL_FOLLOWERS = "collect/getallfollowers"; //获取某本图书的所有收藏的人

    public static final String GET_USR_INFO = "userinfo/getuserinfo"; //获取用户基本信息
    public static final String SET_USR_INFO = "userinfo/setpersonal"; //设置用户信息
    public static final String UPLOAD_AVAR = "userinfo/uploadavatar"; //上传用户头像

    public static final String MY_COMMENTS_LIST = "comment/commentrecord"; //个人评论列表
    public static final String COLLECT_BOOK_SEARCH = "keyword/scansearch"; //个人收藏图书搜索
    public static final String COLLECT_BOOK_DELETE = "collect/deletecollection"; //删除已关注的个人图书
    public static final String COMMENT_DETAIL = "comment/commentdetail"; //评论详情页
    public static final String LIKERS_INFO = "comment/getallthumbers"; //点赞者信息
    public static final String REPLY_INFO = "reply/replydetail"; //回复详情
    public static final String REPLY_ADD = "reply/commentreply"; //回复详情



    


}