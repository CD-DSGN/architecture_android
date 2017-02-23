package com.grandmagic.readingmate.bean.response;

/**
 * Created by lps on 2017/1/11.
 * 模拟的服务端返回的好友列表的假数据
 */

public class DataBean {
    public DataBean(String name ) {
        this.name = name;
    }

    public DataBean() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    public String getTag() {
        return tag;
    }

    private String name;
    private String pyName;
    private String tag;

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getPyName() {
        return pyName;
    }

    public void setPyName(String pyName) {
        this.pyName = pyName;
    }
}
