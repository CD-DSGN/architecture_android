package com.grandmagic.readingmate.bean.db;

import com.hyphenate.chat.EMMessage;

import org.greenrobot.greendao.converter.PropertyConverter;

/**
 * Created by lps on 2017/4/5.
 *
 * @version 1
 * @see
 * @since 2017/4/5 11:14
 */


public class ChatTypeConver implements PropertyConverter<EMMessage.ChatType,String> {
    @Override
    public EMMessage.ChatType convertToEntityProperty(String databaseValue) {
        return EMMessage.ChatType.valueOf(databaseValue);
    }

    @Override
    public String convertToDatabaseValue(EMMessage.ChatType entityProperty) {
        return entityProperty.name();
    }
}
