/**
 *
 */
package com.ouliao.domain.versionfirst;

import javax.persistence.*;
import java.util.Date;

/**
 * @author xiaoluo
 * @version $Id: UserSupportSay.java, 2016年2月19日 下午9:15:35
 */
@Entity
@Table(name = "usersupportsay")
public class UserSupportSay {
    private Integer userSupportSayId;
    private Integer userId;

    private Integer userSayContentId;

    private String isDeleted;
    private Date createTime;
    private Date modifyTime;


    //第二版
    private Integer userSupportId;

    private String isReader;

    @Column(length = 8)
    public String getIsReader() {
        return isReader;
    }

    public void setIsReader(String isReader) {
        this.isReader = isReader;
    }

    public Integer getUserSupportId() {
        return userSupportId;
    }

    public void setUserSupportId(Integer userSupportId) {
        this.userSupportId = userSupportId;
    }

    @Id
    @GeneratedValue
    /**
     * @return the userSupportSayId
     */
    public Integer getUserSupportSayId() {
        return userSupportSayId;
    }


    public Date getCreateTime() {

        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    /**
     * @return the userId
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * @return the userSayContentId
     */
    public Integer getUserSayContentId() {
        return userSayContentId;
    }

    /**
     * @param userSupportSayId the userSupportSayId to set
     */
    public void setUserSupportSayId(Integer userSupportSayId) {
        this.userSupportSayId = userSupportSayId;
    }

    /**
     * @param userId the userId to set
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * @param userSayContentId the userSayContentId to set
     */
    public void setUserSayContentId(Integer userSayContentId) {
        this.userSayContentId = userSayContentId;
    }

    @Column(length = 4)
    /**
     * @return the isDeleted
     */
    public String getIsDeleted() {
        return isDeleted;
    }

    /**
     * @param isDeleted the isDeleted to set
     */
    public void setIsDeleted(String isDeleted) {
        this.isDeleted = isDeleted;
    }

}
