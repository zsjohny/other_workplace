package com.ouliao.domain.versionsecond;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by nessary on 16-5-10.
 */
@Table(name = "useradvice")
@Entity
public class UserAdvice {
    private Long userAdviceId;
    private Integer userId;
    private String userNickName;
    private String userPhone;
    private String realIp;
    private String isDeleted;
    private String isHide;
    private Date creatTime;
    private Date modifyTime;

    private String advice;

    @GeneratedValue
    @Id
    public Long getUserAdviceId() {
        return userAdviceId;
    }

    public void setUserAdviceId(Long userAdviceId) {
        this.userAdviceId = userAdviceId;
    }

    @Column(length = 1024)
    public String getAdvice() {
        return advice;
    }

    public void setAdvice(String advice) {
        this.advice = advice;
    }

    @Column(length = 8)
    public String getIsHide() {
        return isHide;
    }

    public void setIsHide(String isHide) {
        this.isHide = isHide;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Column(length = 102)
    public String getUserNickName() {
        return userNickName;
    }

    public void setUserNickName(String userNickName) {
        this.userNickName = userNickName;
    }

    @Column(length = 11)
    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }


    @Column(length = 68)
    public String getRealIp() {
        return realIp;
    }

    public void setRealIp(String realIp) {
        this.realIp = realIp;
    }

    @Column(length = 2)
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
