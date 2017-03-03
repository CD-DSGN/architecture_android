package com.grandmagic.readingmate.listener;

import com.grandmagic.readingmate.bean.response.Contacts;

/**
 * Created by lps on 2017/3/3.
 */

public interface IMUserProfileProvider {
    Contacts getUser(String usernam);
}
