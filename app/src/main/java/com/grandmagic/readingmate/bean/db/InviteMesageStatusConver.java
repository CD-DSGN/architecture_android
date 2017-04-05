package com.grandmagic.readingmate.bean.db;

import com.grandmagic.readingmate.bean.db.InviteMessage;

import org.greenrobot.greendao.converter.PropertyConverter;

/**
 * Created by lps on 2017/3/7.
 */

public class InviteMesageStatusConver implements PropertyConverter<InviteMessage.InviteMesageStatus, String> {

    @Override
    public InviteMessage.InviteMesageStatus convertToEntityProperty(String databaseValue) {
        return InviteMessage.InviteMesageStatus.valueOf(databaseValue);
    }

    @Override
    public String convertToDatabaseValue(InviteMessage.InviteMesageStatus entityProperty) {
        return entityProperty.name();
    }
}