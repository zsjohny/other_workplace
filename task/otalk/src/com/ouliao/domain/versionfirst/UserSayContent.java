/**
 *
 */
package com.ouliao.domain.versionfirst;

import javax.persistence.*;
import java.util.Date;

/**
 * @author xiaoluo
 * @version $Id: UserSay.java, 2016年2月19日 下午7:43:29
 */
@Entity
@Table(name = "usersaycontent")
public class UserSayContent {

    private Integer userSayContentId;
    // 用于ID的数据转移
    private Integer id;
    private Integer userId;
    private String userContent;

    private String isDeleted;
    private Date userCreateTime;
    private Date userModifyTime;

    //第二版内容
    private String userSayContentSubject;

    private String userPicUrls;

    @Column(length = 4084)
    public String getUserPicUrls() {
        return userPicUrls;
    }

    public void setUserPicUrls(String userPicUrls) {
        this.userPicUrls = userPicUrls;
    }

    @Column(length = 102)
    public String getUserSayContentSubject() {
        return userSayContentSubject;
    }

    public void setUserSayContentSubject(String userSayContentSubject) {
        this.userSayContentSubject = userSayContentSubject;
    }
//---------------------------------------------------------------------------------

    /**
     * @return the userId
     */
    public Integer getUserId() {
        return userId;
    }

    @Id
    @GeneratedValue

    /**
     * @return the userSayContentId
     */
    public Integer getUserSayContentId() {
        return userSayContentId;
    }

    /**
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @param userSayContentId the userSayContentId to set
     */
    public void setUserSayContentId(Integer userSayContentId) {
        this.userSayContentId = userSayContentId;
    }

    @Column(length = 2014)
    /**
     * @return the userContent
     */
    public String getUserContent() {
        return userContent;
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
     * @param userId the userId to set
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * @param userContent the userContent to set
     */
    public void setUserContent(String userContent) {
        this.userContent = userContent;
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
