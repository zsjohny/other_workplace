/**
 *
 */
package com.ouliao.domain.versionfirst;

import javax.persistence.*;
import java.util.Date;

/**
 * @author xiaoluo
 * @version $Id: UserCommont.java, 2016年2月19日 下午4:01:29
 */
@Entity
@Table(name = "usercommont")
public class UserCommont {
    private Integer userCommontId;
    private Integer userId;
    private Integer replyUserId;
    private Integer userSayContentId;
    private String userCommont;
    private String isDeleted;
    private Date userCreateTime;
    private Date userModifyTime;

    //第二版内容
    private Integer userContractId;

    private String isReader;

    @Column(length = 8)
    public String getIsReader() {

        return isReader;
    }

    public void setIsReader(String isReader) {
        this.isReader = isReader;
    }

    @Id
    @GeneratedValue
    /**
     * @return the userCommontId
     */
    public Integer getUserCommontId() {
        return userCommontId;
    }

    public Integer getUserContractId() {
        return userContractId;
    }

    public void setUserContractId(Integer userContractId) {
        this.userContractId = userContractId;
    }

    /**
     * @return the replyUserId
     */
    public Integer getReplyUserId() {
        return replyUserId;
    }

    /**
     * @param replyUserId the replyUserId to set
     */
    public void setReplyUserId(Integer replyUserId) {
        this.replyUserId = replyUserId;
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
     * @param userSayContentId the userSayContentId to set
     */
    public void setUserSayContentId(Integer userSayContentId) {
        this.userSayContentId = userSayContentId;
    }

    @Column(length = 512)
    /**
     * @return the userCommont
     */
    public String getUserCommont() {
        return userCommont;
    }

    /**
     * @param userCommontId the userCommontId to set
     */
    public void setUserCommontId(Integer userCommontId) {
        this.userCommontId = userCommontId;
    }

    /**
     * @param userId the userId to set
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * @param userCommont the userCommont to set
     */
    public void setUserCommont(String userCommont) {
        this.userCommont = userCommont;
    }

    @Column(length = 6)
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
