package com.goldplusgold.td.sltp.core.auth.data;

import com.goldplusgold.td.sltp.core.auth.constant.PlatformEnum;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * 用户上下文数据（包含金专家的用户数据，TD的用户）
 */
@Component("userContextData")
@Scope(value = "request")
public class UserContextData implements Serializable {

    /**
     * 金专家用户ID
     */
    private String jzjUserID;

    /**
     * 金专家登录帐号,就是手机号码
     */
    private String jzjUserName;

    /**
     * TD的用户ID
     */
    private Integer tdUserID;

    /**
     * 客户号
     */
    private String acctNo;

    /**
     * 黄金交易编码
     */
    private String custId;

    /**
     * 平台
     */
    private PlatformEnum platform;

    /**
     * 识别码
     */
    private String imei;

    /**
     * 友盟ID
     */
    private String clientID;

    /**
     * app版本
     */
    private String version;

    /**
     * 会话ID
     */
    private String tdSessionID;

    /**
     * 会话秘钥
     */
    private String tdSessionKey;


    public String getJzjUserID() {
        return jzjUserID;
    }

    public void setJzjUserID(String jzjUserID) {
        this.jzjUserID = jzjUserID;
    }

    public String getJzjUserName() {
        return jzjUserName;
    }

    public void setJzjUserName(String jzjUserName) {
        this.jzjUserName = jzjUserName;
    }

    public Integer getTdUserID() {
        return tdUserID;
    }

    public void setTdUserID(Integer tdUserID) {
        this.tdUserID = tdUserID;
    }

    public String getAcctNo() {
        return acctNo;
    }

    public void setAcctNo(String acctNo) {
        this.acctNo = acctNo;
    }

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public PlatformEnum getPlatform() {
        return platform;
    }

    public void setPlatform(PlatformEnum platform) {
        this.platform = platform;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getClientID() {
        return clientID;
    }

    public void setClientID(String clientID) {
        this.clientID = clientID;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getTdSessionID() {
        return tdSessionID;
    }

    public void setTdSessionID(String tdSessionID) {
        this.tdSessionID = tdSessionID;
    }

    public String getTdSessionKey() {
        return tdSessionKey;
    }

    public void setTdSessionKey(String tdSessionKey) {
        this.tdSessionKey = tdSessionKey;
    }
}
