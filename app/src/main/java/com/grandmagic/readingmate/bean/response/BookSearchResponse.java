package com.grandmagic.readingmate.bean.response;

import com.google.gson.annotations.JsonAdapter;
import com.google.gson.internal.bind.ListTypeAdapterFactory;

import java.util.List;

/**
 * Created by lps  on 2017/3/10.
 */

public class BookSearchResponse {

    /**
     * total_num : 1
     * num : 1
     * search_result : [{"book_id":"6","book_name":"深入浅出MySQL","photo":"","synopsis":"","author":"唐汉明"}]
     * scan_record : []
     */

    private int total_num;
    private int num;
    private List<SearchResultBean> search_result;
    @JsonAdapter(ListTypeAdapterFactory.class)
    private List<?> scan_record;

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

    public List<SearchResultBean> getSearch_result() {
        return search_result;
    }

    public void setSearch_result(List<SearchResultBean> search_result) {
        this.search_result = search_result;
    }

    public List<?> getScan_record() {
        return scan_record;
    }

    public void setScan_record(List<?> scan_record) {
        this.scan_record = scan_record;
    }

    public static class SearchResultBean {
        /**
         * book_id : 6
         * book_name : 深入浅出MySQL
         * photo :
         * synopsis :
         * author : 唐汉明
         */

        private String book_id;
        private String book_name;
        private String photo;
        private String synopsis;
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

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }
    }
}
