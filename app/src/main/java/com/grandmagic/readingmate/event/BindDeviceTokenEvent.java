package com.grandmagic.readingmate.event;

/**
 * Created by lps on 2017/3/31.
 *
 * @version 1
 * @see
 * @since 2017/3/31 10:25 绑定设备device token的事件
 */


public class BindDeviceTokenEvent {
    private String devicetoken;

    public BindDeviceTokenEvent() {
    }

    public BindDeviceTokenEvent(String mDevicetoken) {
        devicetoken = mDevicetoken;
    }

    public String getDevicetoken() {
        return devicetoken;
    }

    public void setDevicetoken(String mDevicetoken) {
        devicetoken = mDevicetoken;
    }
}
