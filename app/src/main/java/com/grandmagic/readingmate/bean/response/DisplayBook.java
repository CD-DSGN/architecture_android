package com.grandmagic.readingmate.bean.response;

import java.util.List;

/**
 * Created by lps on 2017/3/7.
 */

public class DisplayBook {

    /**
     * total_num : 5
     * num : 3
     * info : [{"book_id":"1","book_name":"唐诗三百首精选","author":"崔钟雷","photo":"","synopsis":""},{"book_id":"2","book_name":"唐诗三百首精选1","author":"崔钟雷1","photo":"","synopsis":""},{"book_id":"3","book_name":"唐诗三百首精选2","author":"崔钟雷2","photo":"","synopsis":""}]
     */

    private int total_num;
    private int num;
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
         * book_id : 1
         * book_name : 唐诗三百首精选
         * author : 崔钟雷
         * photo :
         * synopsis :
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
    public int  getpage(){
        try {
            return (int) Math.ceil(total_num*1.0f/num);
        } catch (Exception mE) {
            mE.printStackTrace();
         return 0;
        }
    }
}
