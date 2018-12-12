package com.ouliao.domain.versionsecond;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by nessary on 16-5-17.
 */
@Table(name = "userpersionalshow")
@Entity
public class UserPersionalShow {

    private Long userPersionalShowId;
    private Integer userId;
    private String describeByPersional;
    private String persionalShowPic;
    private String isDeleted;
    private Date creatTime;
    private Date modifyTime;

    @GeneratedValue
    @Id
    public Long getUserPersionalShowId() {
        return userPersionalShowId;
    }

    @Column(length = 2048)
    public String getPersionalShowPic() {
        return persionalShowPic;
    }

    public void setPersionalShowPic(String persionalShowPic) {
        this.persionalShowPic = persionalShowPic;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public void setUserPersionalShowId(Long userPersionalShowId) {
        this.userPersionalShowId = userPersionalShowId;
    }

    @Column(length = 128)
    public String getDescribeByPersional() {
        return describeByPersional;
    }

    public void setDescribeByPersional(String describeByPersional) {
        this.describeByPersional = describeByPersional;
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
