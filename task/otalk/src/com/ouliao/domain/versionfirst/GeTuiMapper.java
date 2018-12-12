/**
 *
 */
package com.ouliao.domain.versionfirst;

import javax.persistence.*;
import java.util.Date;

/**
 * @author xiaoluo
 * @version $Id: GeTuiMapper.java, 2016年2月29日 下午10:22:27
 */
@Entity
@Table(name = "getuimapper")
public class GeTuiMapper {

    private Long geTuiMapperId;

    private Integer userId;

    private String clientId;
    private String isDeleted;
    private Integer clientCata;
    private String getuiDeviceToken;
    private Date userCreateTime;
    private Date userModifyTime;
    private int version;

    @Version
    /**
     * @return the version
     */
    public int getVersion() {
        return version;
    }

    /**
     * @param version the version to set
     */
    public void setVersion(int version) {
        this.version = version;
    }

    @GeneratedValue
    @Id

    public Long getGeTuiMapperId() {
        return geTuiMapperId;
    }

    public void setGeTuiMapperId(Long geTuiMapperId) {
        this.geTuiMapperId = geTuiMapperId;
    }

    @Column(length = 128)
    /**
     * @return the getuiDeviceToken
     */
    public String getGetuiDeviceToken() {
        return getuiDeviceToken;
    }

    /**
     * @param getuiDeviceToken the getuiDeviceToken to set
     */
    public void setGetuiDeviceToken(String getuiDeviceToken) {
        this.getuiDeviceToken = getuiDeviceToken;
    }

    /**
     * @return the clientCata
     */
    public Integer getClientCata() {
        return clientCata;
    }

    /**
     * @param clientCata the clientCata to set
     */
    public void setClientCata(Integer clientCata) {
        this.clientCata = clientCata;
    }

    @Column(length = 512)
    /**
     * @return the clientId
     */
    public String getClientId() {
        return clientId;
    }

    /**
     * @param clientId the clientId to set
     */
    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    /**
     * @return the userId
     */
    public Integer getUserId() {
        return userId;
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
     * @param userId the userId to set
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
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
