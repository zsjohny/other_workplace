package com.ouliao.domain.versionsecond;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by nessary on 16-5-19.
 */
@Table(name = "usersuremsgcount")
@Entity
public class UserSureMsgCount {
    private Long userSureMsgCountId;
    private Integer userId;
    private Integer readCount;
    private String isDeleted;
    private Date creatTime;
    private Date modifyTime;

    @GeneratedValue
    @Id
    public Long getUserSureMsgCountId() {
        return userSureMsgCountId;
    }


    public void setUserSureMsgCountId(Long userSureMsgCountId) {
        this.userSureMsgCountId = userSureMsgCountId;
    }


    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getReadCount() {
        return readCount;
    }

    public void setReadCount(Integer readCount) {
        this.readCount = readCount;
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
}
