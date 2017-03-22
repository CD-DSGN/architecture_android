package com.grandmagic.readingmate.bean.response;

import com.google.gson.annotations.JsonAdapter;
import com.google.gson.internal.bind.ListTypeAdapterFactory;
import com.google.gson.internal.bind.ObjectDeserializer;

import java.util.ArrayList;

/**
 * Created by zhangmengqi on 2017/3/22.
 */

public class CommentsDetailResponoseBean {
    private String is_thumb;
    private String replay_count;
    private String user_name;
    @JsonAdapter(ObjectDeserializer.class)
    private ImageUrlResponseBean avatar_url;
    private String comment_id;
    private String content;
    private String pub_time;
    private String like_times;
    private String book_name;
    private String book_id;
    private String photo;

    @JsonAdapter(ListTypeAdapterFactory.class)
    private ArrayList<UserSimpleInfoResponseBean> thumb_user_avatar;



}
