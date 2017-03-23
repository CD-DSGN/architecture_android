package com.grandmagic.readingmate.bean.response;

import com.google.gson.annotations.JsonAdapter;
import com.google.gson.internal.bind.ListTypeAdapterFactory;

import java.util.List;

/**
 * Created by lps  on 2017/3/23.
 */

public class PersonCollectBookResponse {

    /**
     * total_num : 5
     * num : 4
     * info : [{"book_id":"9","book_name":"Introduction to Algorithms","author":"T.H.C.E","photo":"/image/3.jpg","synopsis":"该书是一本十分经典的计算机算法书籍，与高德纳（Donald E.Knuth）的《计算机程序设计艺术》（The Art Of Computer Programming）相媲美。 《算法导论》由Thomas H.Cormen、Charles E.Leiserson、Ronald L.Rivest、Clifford Stein四人合作编著（其中Clifford Stein是第二版开始参与的合著者）。本书的最大特点就是将严谨性和全面性融入在了一起。"},{"book_id":"3","book_name":"唐诗三百首精选2","author":"崔钟雷2","photo":"/image/6.jpg","synopsis":""},{"book_id":"12","book_name":"随便再加一个啦","author":"hello world","photo":"/image/6.jpg","synopsis":"；哒关键都按时给京东鞍山建工大厦开关机"},{"book_id":"1","book_name":"唐诗三百首精选","author":"崔钟雷","photo":"/image/6.jpg","synopsis":""}]
     */

    private int total_num;
    private int num;
    @JsonAdapter(ListTypeAdapterFactory.class)
    private List<InfoBean> info;

    public int getTotal_num() {
        return total_num;
    }

    public void setTotal_num(int total_num) {
        this.total_num = total_num;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public List<InfoBean> getInfo() {
        return info;
    }

    public void setInfo(List<InfoBean> info) {
        this.info = info;
    }

    public static class InfoBean {
        /**
         * book_id : 9
         * book_name : Introduction to Algorithms
         * author : T.H.C.E
         * photo : /image/3.jpg
         * synopsis : 该书是一本十分经典的计算机算法书籍，与高德纳（Donald E.Knuth）的《计算机程序设计艺术》（The Art Of Computer Programming）相媲美。 《算法导论》由Thomas H.Cormen、Charles E.Leiserson、Ronald L.Rivest、Clifford Stein四人合作编著（其中Clifford Stein是第二版开始参与的合著者）。本书的最大特点就是将严谨性和全面性融入在了一起。
         */

        private String book_id;
        private String book_name;
        private String author;
        private String photo;
        private String synopsis;

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

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }

        public String getSynopsis() {
            return synopsis;
        }

        public void setSynopsis(String synopsis) {
            this.synopsis = synopsis;
        }
    }
}
