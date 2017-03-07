package com.grandmagic.readingmate.bean.response;

import com.grandmagic.readingmate.bean.response.InviteMessage;

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