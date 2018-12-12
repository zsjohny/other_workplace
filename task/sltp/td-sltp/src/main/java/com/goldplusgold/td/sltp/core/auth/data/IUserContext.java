package com.goldplusgold.td.sltp.core.auth.data;


import com.goldplusgold.td.sltp.core.auth.constant.PlatformEnum;

/**
 * 上下文所带的用户必要信息接口定义
 */
public interface IUserContext {

    String getJzjUserID();

    void setJzjUserID(String jzjUserID);

    String getJzjUserName();

    void setJzjUserName(String jzjUserName);

    Integer getTdUserID();

    void setTdUserID(Integer tdUserID);

    String getAcctNo();

    void setAcctNo(String acctNo);

    String getCustId();

    void setCustId(String custId);

    PlatformEnum getPlatform();

    void setPlatform(PlatformEnum platform);

    String getImei();

    void setImei(String imei);

    String getClientID();

    void setClientID(String clientID);

    String getVersion();

    void setVersion(String version);

    String getTdSessionID();

    void setTdSessionID(String sessionId);

    String getTdSessionKey();
    void setTdSessionKey(String sessionKey);

}
