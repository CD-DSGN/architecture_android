package com.grandmagic.readingmate.listener;

import com.hyphenate.chat.EMMessage;

/**
 * Created by lps on 2017/3/3.
 */

public interface IMSettingsProvider {
    boolean isMsgNotifyAllowed(EMMessage message);
    boolean isMsgSoundAllowed(EMMessage message);
    boolean isMsgVibrateAllowed(EMMessage message);
    boolean isSpeakerOpened();
}
