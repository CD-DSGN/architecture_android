package com.grandmagic.readingmate.bean.response;

/**
 * Created by lps  on 2017/3/15.
 */

public class BookdetailResponse {

    /**
     * book_id : 6
     * book_name : 深入浅出MySQL
     * author : 唐汉明
     * photo :
     * synopsis :
     * code : 9787115335494
     * publisher : 人民邮电出版社
     * pub_date : 2009-10-1
     * price : 99.00
     * cate_id : 5
     */

    private String book_id;
    private String book_name;
    private String author;
    private String photo;
    private String synopsis;
    private String code;
    private String publisher;
    private String pub_date;
    private String price;
    private String cate_id;

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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getPub_date() {
        return pub_date;
    }

    public void setPub_date(String pub_date) {
        this.pub_date = pub_date;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCate_id() {
        return cate_id;
    }

    public void setCate_id(String cate_id) {
        this.cate_id = cate_id;
    }
}
