package com.grandmagic.readingmate.bean.db;

import com.hyphenate.chat.EMMessage;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;
import com.hyphenate.chat.EMMessage.ChatType;

/**
 * Created by lps on 2017/4/5.
 *
 * @version 1
 * @see
 * @since 2017/4/5 11:10
 * 聊天草稿箱
 * 当用户编辑了文本类型消息确没有发送
 */

@Entity
public class ChatDraftBox {
    @Id
    long id;
    @Unique
    private String tochatuserid;
    @Convert(columnType = String.class,converter = ChatTypeConver.class)
    private EMMessage.ChatType mType;
    private String txt;

    @Generated(hash = 1435297750)
    public ChatDraftBox(long id, String tochatuserid, EMMessage.ChatType mType,
            String txt) {
        this.id = id;
        this.tochatuserid = tochatuserid;
        this.mType = mType;
        this.txt = txt;
    }

    @Generated(hash = 1333705257)
    public ChatDraftBox() {
    }

    public long getId() {
        return id;
    }

    public void setId(long mId) {
        id = mId;
    }

    public String getTochatuserid() {
        return tochatuserid;
    }

    public void setTochatuserid(String mTochatuserid) {
        tochatuserid = mTochatuserid;
    }

    public ChatType getType() {
        return mType;
    }

    public void setType(ChatType mType) {
        this.mType = mType;
    }

    public String getTxt() {
        return txt;
    }

    public void setTxt(String mTxt) {
        txt = mTxt;
    }

    public EMMessage.ChatType getMType() {
        return this.mType;
    }

    public void setMType(EMMessage.ChatType mType) {
        this.mType = mType;
    }
}
