/**
 *
 */
package com.ouliao.domain.versionfirst;

import javax.persistence.*;
import java.util.Date;

/**
 * @author xiaoluo
 * @version $Id: UserGainCount.java, 2016年2月18日 下午11:02:45
 */
@Entity
@Table(name = "usergaincount")
public class UserGainCount {
    private Integer userId;
    private String userRealIp;
    private int userGainCount;
    private String isDeleted;

    private Date userCreateTime;
    private Date userModifyTime;

    @Id
    @GeneratedValue
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Column(length = 10)
    public int getUserGainCount() {
        return userGainCount;
    }

    public void setUserGainCount(int userGainCount) {
        this.userGainCount = userGainCount;
    }

    @Column(length = 52)
    public String getUserRealIp() {
        return userRealIp;
    }

    public void setUserRealIp(String userRealIp) {
        this.userRealIp = userRealIp;
    }

    @Column(length = 4)
    /**
     * @return the isDeleted
     */
    public String getIsDeleted() {
        return isDeleted;
    }

    /**
     * @return the userCreateTime
     */
    public Date getUserCreateTime() {
        return userCreateTime;
    }

    /**
     * @return the userModifyTime
     */
    public Date getUserModifyTime() {
        return userModifyTime;
    }

    /**
     * @param isDeleted the isDeleted to set
     */
    public void setIsDeleted(String isDeleted) {
        this.isDeleted = isDeleted;
    }

    /**
     * @param userCreateTime the userCreateTime to set
     */
    public void setUserCreateTime(Date userCreateTime) {
        this.userCreateTime = userCreateTime;
    }

    /**
     * @param userModifyTime the userModifyTime to set
     */
    public void setUserModifyTime(Date userModifyTime) {
        this.userModifyTime = userModifyTime;
    }

}
