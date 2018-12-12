package com.ouliao.domain.versionfirst;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;


/**
 * Created by nessary on 16-5-10.
 */
@Entity
@Table(name = "useractive")
public class UserActive {
    private Long userActiveId;
    private Integer userId;
    private Integer userCount;
    private Date createTime;

    @GeneratedValue
    @Id
    public Long getUserActiveId() {
        return userActiveId;
    }

    public void setUserActiveId(Long userActiveId) {
        this.userActiveId = userActiveId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getUserCount() {
        return userCount;
    }

    public void setUserCount(Integer userCount) {
        this.userCount = userCount;
    }

    public Date getCreateTime() {
        return createTime;
    }


    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
