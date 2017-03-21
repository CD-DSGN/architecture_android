package com.grandmagic.readingmate.bean.response;

import com.google.gson.annotations.JsonAdapter;
import com.google.gson.internal.bind.ListTypeAdapterFactory;

import java.util.List;

/**
 * Created by lps  on 2017/3/10.
 */

public class BookSearchResponse {

    /**
     * total_num : 5
     * num : 3
     * search_result : [{"book_id":"1","book_name":"唐诗三百首精选","photo":"http://192.168.1.115/reading-partner-php/api/web/image/6.jpg","synopsis":"","author":"崔钟雷"},{"book_id":"2","book_name":"唐诗三百首精选1","photo":"http://192.168.1.115/reading-partner-php/api/web/image/6.jpg","synopsis":"","author":"崔钟雷1"},{"book_id":"3","book_name":"唐诗三百首精选2","photo":"http://192.168.1.115/reading-partner-php/api/web/image/6.jpg","synopsis":"","author":"崔钟雷2"}]
     * scan_record : [{"book_id":"1","book_name":"唐诗三百首精选","scan_time":"2017/03/17"},{"book_id":"3","book_name":"唐诗三百首精选2","scan_time":"2017/03/17"}]
     */

    private int total_num;
    private int num;
    @JsonAdapter(ListTypeAdapterFactory.class)
    private List<SearchResultBean> search_result;
    @JsonAdapter(ListTypeAdapterFactory.class)
    private List<ScanRecordBean> scan_record;

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

    public List<ScanRecordBean> getScan_record() {
        return scan_record;
    }

    public void setScan_record(List<ScanRecordBean> scan_record) {
        this.scan_record = scan_record;
    }

    public int getPageCount() {
        if (total_num==0)return 0;
        return (int) Math.ceil(total_num*1.0f/num);
    }

    public static class SearchResultBean {
        /**
         * book_id : 1
         * book_name : 唐诗三百首精选
         * photo : http://192.168.1.115/reading-partner-php/api/web/image/6.jpg
         * synopsis :
         * author : 崔钟雷
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

    public static class ScanRecordBean {
        /**
         * book_id : 1
         * book_name : 唐诗三百首精选
         * scan_time : 2017/03/17
         */

        private String book_id;
        private String book_name;
        private String scan_time;

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

        public String getScan_time() {
            return scan_time;
        }

        public void setScan_time(String scan_time) {
            this.scan_time = scan_time;
        }
    }
}
