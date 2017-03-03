package com.grandmagic.readingmate.listener;

import com.hyphenate.chat.EMMessage;

/**
 * Created by lps on 2017/3/3.
 */

public class DefaultSettingsProvider implements IMSettingsProvider{
    @Override
    public boolean isMsgNotifyAllowed(EMMessage message) {
        return true;
    }

    @Override
    public boolean isMsgSoundAllowed(EMMessage message) {
        return true;
    }

    @Override
    public boolean isMsgVibrateAllowed(EMMessage message) {
        return true;
    }

    @Override
    public boolean isSpeakerOpened() {
        return true;
    }
}
