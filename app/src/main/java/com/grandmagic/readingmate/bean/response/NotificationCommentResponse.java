package com.grandmagic.readingmate.bean.response;

import com.google.gson.annotations.JsonAdapter;
import com.google.gson.internal.bind.ListTypeAdapterFactory;
import com.google.gson.internal.bind.ObjectDeserializer;

import java.util.List;

/**
 * Created by lps on 2017/4/6.
 *
 * @version 1
 * @see
 * @since 2017/4/6 13:48
 */


public class NotificationCommentResponse {

    /**
     * total_num : 1
     * num : 1
     * info : [{"from_user_id":"41","user_name":"奂捷","avatar_url":{"large":"","mid":""},"book_id":6,"book_name":"深入浅出MySQL","time":"1491017408","native_comment":"1","reply_comment":"培生66"}]
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
         * from_user_id : 41
         * user_name : 奂捷
         * avatar_url : {"large":"","mid":""}
         * book_id : 6
         * book_name : 深入浅出MySQL
         * time : 1491017408
         * native_comment : 1
         * reply_comment : 培生66
         */

        private String from_user_id;
        private String user_name;
        @JsonAdapter(ObjectDeserializer.class)
        private AvatarUrlBean avatar_url;
        private int book_id;
        private String book_name;
        private String time;
        private String native_comment;
        private String reply_comment;
        private int type;

        public int getType() {
            return type;
        }

        public void setType(int mType) {
            type = mType;
        }

        public String getFrom_user_id() {
            return from_user_id;
        }

        public void setFrom_user_id(String from_user_id) {
            this.from_user_id = from_user_id;
        }

        public String getUser_name() {
            return user_name;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }

        public AvatarUrlBean getAvatar_url() {
            return avatar_url;
        }

        public void setAvatar_url(AvatarUrlBean avatar_url) {
            this.avatar_url = avatar_url;
        }

        public int getBook_id() {
            return book_id;
        }

        public void setBook_id(int book_id) {
            this.book_id = book_id;
        }

        public String getBook_name() {
            return book_name;
        }

        public void setBook_name(String book_name) {
            this.book_name = book_name;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getNative_comment() {
            return native_comment;
        }

        public void setNative_comment(String native_comment) {
            this.native_comment = native_comment;
        }

        public String getReply_comment() {
            return reply_comment;
        }

        public void setReply_comment(String reply_comment) {
            this.reply_comment = reply_comment;
        }

        public static class AvatarUrlBean {
            /**
             * large :
             * mid :
             */

            private String large;
            private String mid;

            public String getLarge() {
                return large;
            }

            public void setLarge(String large) {
                this.large = large;
            }

            public String getMid() {
                return mid;
            }

            public void setMid(String mid) {
                this.mid = mid;
            }
        }
    }
    public int  getPage(){
        try {
            return (int) Math.ceil(total_num/num);
        } catch (Exception mE) {
            mE.printStackTrace();
            return 0;
        }
    }
}
