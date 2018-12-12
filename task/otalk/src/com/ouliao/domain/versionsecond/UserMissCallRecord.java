package com.ouliao.domain.versionsecond;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by nessary on 16-5-14.
 */

@Table(name = "usermisscallrecord")
@Entity

public class UserMissCallRecord {

    private Long userMissCallRecordId;
    private Integer userCallId;
    private Integer userCalledId;
    private Integer userCallRingTime;
    private String isRead;
    private String cid;
    private String isDeleted;
    private Date creatTime;
    private Date modifyTime;


    @Column(length = 128)
    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    @GeneratedValue
    @Id
    public Long getUserMissCallRecordId() {
        return userMissCallRecordId;
    }

    public Integer getUserCallRingTime() {
        return userCallRingTime;
    }

    public void setUserCallRingTime(Integer userCallRingTime) {
        this.userCallRingTime = userCallRingTime;
    }

    public void setUserMissCallRecordId(Long userMissCallRecordId) {
        this.userMissCallRecordId = userMissCallRecordId;
    }

    public Integer getUserCallId() {
        return userCallId;
    }

    public void setUserCallId(Integer userCallId) {
        this.userCallId = userCallId;
    }

    public Integer getUserCalledId() {
        return userCalledId;
    }

    public void setUserCalledId(Integer userCalledId) {
        this.userCalledId = userCalledId;
    }

    @Column(length = 8)
    public String getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(String isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Date getCreatTime() {
        return creatTime;
    }

    public void setCreatTime(Date creatTime) {
        this.creatTime = creatTime;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    @Column(length = 8)
    public String getIsRead() {
        return isRead;
    }

    public void setIsRead(String isRead) {
        this.isRead = isRead;
    }
}
