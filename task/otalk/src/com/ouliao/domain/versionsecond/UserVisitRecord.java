package com.ouliao.domain.versionsecond;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by nessary on 16-5-17.
 */
@Table(name = "uservisitrecord")
@Entity
public class UserVisitRecord {
    private Long userVisitRecordId;
    private Integer userId;
    private Integer visitUserId;
    private String isReader;
    private String isDeleted;
    private Date creatTime;
    private Date modifyTime;

    @GeneratedValue
    @Id
    public Long getUserVisitRecordId() {

        return userVisitRecordId;
    }

    @Column(length = 8)
    public String getIsReader() {

        return isReader;
    }

    public void setIsReader(String isReader) {
        this.isReader = isReader;
    }

    public void setUserVisitRecordId(Long userVisitRecordId) {
        this.userVisitRecordId = userVisitRecordId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getVisitUserId() {
        return visitUserId;
    }

    public void setVisitUserId(Integer visitUserId) {
        this.visitUserId = visitUserId;
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

    @Override
    public String toString() {
        return "UserVisitRecord{" +
                "userVisitRecordId=" + userVisitRecordId +
                ", userId=" + userId +
                ", visitUserId=" + visitUserId +
                ", isReader='" + isReader + '\'' +
                ", isDeleted='" + isDeleted + '\'' +
                ", creatTime=" + creatTime +
                ", modifyTime=" + modifyTime +
                '}';
    }
}
