package com.grandmagic.readingmate.bean.response;

/**
 * Created by lps on 2017/3/17.
 */

public class HistoryComment {

    /**
     * comment_conent : 这本书碉堡了
     * score_num : 9
     */

    private String comment_conent;
    private int score_num;

    public String getComment_conent() {
        return comment_conent;
    }

    public void setComment_conent(String comment_conent) {
        this.comment_conent = comment_conent;
    }

    public int getScore_num() {
        return score_num;
    }

    public void setScore_num(int score_num) {
        this.score_num = score_num;
    }
}
