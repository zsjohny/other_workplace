package com.ouliao.domain.versionsecond;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by nessary on 16-5-18.
 */
@Table(name = "userdisconery")
@Entity

public class UserDisconery {

    private Long userDicoveryId;

    private Integer userId;
    private String classicProgram;
    private String listenProgram;
    private String disPicUrls;

    private String isContract;

    private String isDeleted;
    private Date creatTime;
    private Date modifyTime;

    @GeneratedValue
    @Id
    public Long getUserDicoveryId() {
        return userDicoveryId;
    }

    public void setUserDicoveryId(Long userDicoveryId) {
        this.userDicoveryId = userDicoveryId;
    }

    @Column(length = 8)
    public String getIsContract() {
        return isContract;
    }

    public void setIsContract(String isContract) {
        this.isContract = isContract;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Column(length = 512)
    public String getClassicProgram() {
        return classicProgram;
    }

    public void setClassicProgram(String classicProgram) {
        this.classicProgram = classicProgram;
    }

    @Column(length = 512)
    public String getListenProgram() {
        return listenProgram;
    }

    public void setListenProgram(String listenProgram) {
        this.listenProgram = listenProgram;
    }

    @Column(length = 1024)
    public String getDisPicUrls() {
        return disPicUrls;
    }

    public void setDisPicUrls(String disPicUrls) {
        this.disPicUrls = disPicUrls;
    }

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
