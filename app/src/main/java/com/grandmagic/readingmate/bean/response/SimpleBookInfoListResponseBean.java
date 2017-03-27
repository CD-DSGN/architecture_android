package com.grandmagic.readingmate.bean.response;

import java.util.List;

/**
 * Created by zhangmengqi on 2017/3/27.
 */

public class SimpleBookInfoListResponseBean {
    List<SimpleBookInfo> data;
    public static class SimpleBookInfo{
        private String book_id;
        private String book_name;
        private String author;

        public String getBook_id() {
            return book_id;
        }

        public void setBook_id(String book_id) {
            this.book_id = book_id;
        }

        public String getBook_name() {
            return book_name;
        }

        public void setBook_name(String book_name) {
            this.book_name = book_name;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }
    }
}
