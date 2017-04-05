package com.grandmagic.readingmate.bean.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by lps on 2017/4/5.
 *
 * @version 1
 * @see
 * @since 2017/4/5 9:45
 * 为了更好的体验，防止用户写到一半不小心离开了编辑，导致编辑内容丢失
 * 在提交之前保存一份到本地，提交之后删除
 */

@Entity
public class BookComment {
    @Id
    private long id;
    @Unique
    private String bookid;
    private int score;
    private String comment_content;

    @Generated(hash = 746469115)
    public BookComment(long id, String bookid, int score, String comment_content) {
        this.id = id;
        this.bookid = bookid;
        this.score = score;
        this.comment_content = comment_content;
    }

    @Generated(hash = 721209560)
    public BookComment() {
    }

    public long getId() {
        return id;
    }

    public void setId(long mId) {
        id = mId;
    }

    public String getBookid() {
        return bookid;
    }

    public void setBookid(String mBookid) {
        bookid = mBookid;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int mScore) {
        score = mScore;
    }

    public String getComment_content() {
        return comment_content;
    }

    public void setComment_content(String mComment_content) {
        comment_content = mComment_content;
    }

    @Override
    public String toString() {
        return "BookComment{" +
                "id=" + id +
                ", bookid='" + bookid + '\'' +
                ", score=" + score +
                ", comment_content='" + comment_content + '\'' +
                '}';
    }
}
