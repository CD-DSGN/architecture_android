/**
 * Copyright (C) 2016 Hyphenate Inc. All rights reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

//好友请求相关实体类
package com.grandmagic.readingmate.bean.response;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.converter.PropertyConverter;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class InviteMessage {

    private String from;
    private long time;
    private String reason;
    @Convert(converter = InviteMesageStatusConver.class, columnType = String.class)
    private InviteMesageStatus status;
    private String groupId;
    private String groupName;
    private String groupInviter;
private int count;
    @Id
    private Long id;

    
    public InviteMessage(String from, long time, String reason,
            InviteMesageStatus status, String groupId, String groupName,
            String groupInviter, long id) {
        this.from = from;
        this.time = time;
        this.reason = reason;
        this.status = status;
        this.groupId = groupId;
        this.groupName = groupName;
        this.groupInviter = groupInviter;
        this.id = id;
    }



    @Generated(hash = 1711924327)
    public InviteMessage(String from, long time, String reason,
            InviteMesageStatus status, String groupId, String groupName,
            String groupInviter, Long id) {
        this.from = from;
        this.time = time;
        this.reason = reason;
        this.status = status;
        this.groupId = groupId;
        this.groupName = groupName;
        this.groupInviter = groupInviter;
        this.id = id;
    }



    @Generated(hash = 1613074736)
    public InviteMessage() {
    }



    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int mCount) {
        count = mCount;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public InviteMesageStatus getStatus() {
        return status;
    }

    public void setStatus(InviteMesageStatus status) {
        this.status = status;
    }


    public Long getId() {
        return id;
    }


    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void setGroupInviter(String inviter) {
        groupInviter = inviter;
    }

    public String getGroupInviter() {
        return groupInviter;
    }



    public void setId(Long id) {
        this.id = id;
    }



    public enum InviteMesageStatus {

        //==contact
        //请求添加别人为好友
        INVITEED,
        /**
         * 被添加
         */
        BEINVITEED,
        /**
         * 被拒绝
         */
        BEREFUSED,
        /**
         * 被对方同意
         */
        BEAGREED,

        //==group application
        /**
         * 同意加入群聊
         */
        BEAPPLYED,
        /**
         *同意
         */
        AGREED,
        /**
         *拒绝加入群聊
         */
        REFUSED,

        //==group invitation
        /**
         * 收到群邀请
         **/
        GROUPINVITATION,
        /**
         * 群同意了你的申请
         **/
        GROUPINVITATION_ACCEPTED,
        /**
         * 群拒绝了你的请求
         **/
        GROUPINVITATION_DECLINED
    }

    @Override
    public String toString() {
        return "InviteMessage{" +
                "from='" + from + '\'' +
                ", time=" + time +
                ", reason='" + reason + '\'' +
                ", status=" + status +
                ", groupId='" + groupId + '\'' +
                ", groupName='" + groupName + '\'' +
                ", groupInviter='" + groupInviter + '\'' +
                ", id=" + id +
                '}';
    }
}



