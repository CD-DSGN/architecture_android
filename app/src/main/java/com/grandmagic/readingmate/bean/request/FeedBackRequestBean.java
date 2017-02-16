package com.grandmagic.readingmate.bean.request;

/**
 * Created by zhangmengqi on 2017/2/16.
 */

public class FeedBackRequestBean {
    public String getSuggestion() {
        return suggestion;
    }

    public void setSuggestion(String suggestion) {
        this.suggestion = suggestion;
    }

    private String suggestion;


    public FeedBackRequestBean(String suggestion) {
        this.suggestion = suggestion;
    }
}
