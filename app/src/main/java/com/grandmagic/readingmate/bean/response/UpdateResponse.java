package com.grandmagic.readingmate.bean.response;

/**
 * Created by lps on 2017/2/10.
 *
 */

public class UpdateResponse {
    /**
     * is_update : true
     * is_required : false
     * latest_version : 1.5.2
     * update_note : 修复几处漏洞
     * down_link : http://www.123.com
     */

    private boolean is_update;
    private boolean is_required;
    private String latest_version;
    private String update_note;
    private String down_link;

    public boolean isIs_update() {
        return is_update;
    }

    public void setIs_update(boolean is_update) {
        this.is_update = is_update;
    }

    public boolean isIs_required() {
        return is_required;
    }

    public void setIs_required(boolean is_required) {
        this.is_required = is_required;
    }

    public String getLatest_version() {
        return latest_version;
    }

    public void setLatest_version(String latest_version) {
        this.latest_version = latest_version;
    }

    public String getUpdate_note() {
        return update_note;
    }

    public void setUpdate_note(String update_note) {
        this.update_note = update_note;
    }

    public String getDown_link() {
        return down_link;
    }

    public void setDown_link(String down_link) {
        this.down_link = down_link;
    }
}
