/**
 *
 */
package com.ouliao.domain.versionfirst;

import javax.persistence.*;
import java.util.Date;

/**
 * @author xiaoluo
 * @version $Id: UserConcern.java, 2016年2月23日 下午9:51:50
 */
@Entity
@Table(name = "userconcern")
public class UserConcern {
    private Integer userConcernId;
    private Integer userId;
    private Integer userOnfocusId;
    private String userConract;

    private String isDeleted;
    private Date userCreateTime;
    private Date userModifyTime;

    @Column(length = 8)
    public String getUserConract() {
        return userConract;
    }

    public void setUserConract(String userConract) {
        this.userConract = userConract;
    }

    @Id
    @GeneratedValue
    /**
     * @return the userConcernId
     */
    public Integer getUserConcernId() {
        return userConcernId;
    }

    /**
     * @return the userId
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * @return the userOnfocusId
     */
    public Integer getUserOnfocusId() {
        return userOnfocusId;
    }

    @Column(length = 8)
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
     * @param userConcernId the userConcernId to set
     */
    public void setUserConcernId(Integer userConcernId) {
        this.userConcernId = userConcernId;
    }

    /**
     * @param userId the userId to set
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * @param userOnfocusId the userOnfocusId to set
     */
    public void setUserOnfocusId(Integer userOnfocusId) {
        this.userOnfocusId = userOnfocusId;
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
