package com.grandmagic.readingmate.bean.response;

/**
 * Created by lps on 2017/3/30.
 *
 * @version 1
 * @see
 * @since 2017/3/30 17:32
 */


public class ScanBookResponse {

    /**
     * book_id : 6
     * book_name : 深入浅出MySQL
     * photo : /image/6.jpg
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
