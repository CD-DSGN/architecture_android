package com.grandmagic.readingmate.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by zhangmengqi on 2017/1/23.
 */
@Entity
public class UserModel {
    private String name;
    private String password;
    @Id
    private long id;

    @Generated(hash = 1762950718)
    public UserModel(String name, String password, long id) {
        this.name = name;
        this.password = password;
        this.id = id;
    }

    @Generated(hash = 782181818)
    public UserModel() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
