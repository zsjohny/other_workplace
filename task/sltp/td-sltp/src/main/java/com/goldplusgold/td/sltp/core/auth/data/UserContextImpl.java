package com.goldplusgold.td.sltp.core.auth.data;

import com.goldplusgold.td.sltp.core.auth.constant.PlatformEnum;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.stereotype.Component;

/**
 * 上下文所带的用户必要信息实现
 */
@Component
public class UserContextImpl implements IUserContext {

    @Lookup("userContextData")
    public UserContextData getUserContextData() {
        return null;
    }

    @Override
    public String getJzjUserID() {
        return this.getUserContextData().getJzjUserID();
    }

    @Override
    public void setJzjUserID(String jzjUserID) {
        this.getUserContextData().setJzjUserID(jzjUserID);
    }

    @Override
    public PlatformEnum getPlatform() {
        return this.getUserContextData().getPlatform();
    }

    @Override
    public void setPlatform(PlatformEnum platform) {
        this.getUserContextData().setPlatform(platform);
    }

    @Override
    public String getJzjUserName() {
        return this.getUserContextData().getJzjUserName();
    }

    @Override
    public void setJzjUserName(String jzjUserName) {
        this.getUserContextData().setJzjUserName(jzjUserName);
    }

    @Override
    public String getImei() {
        return this.getUserContextData().getImei();
    }

    @Override
    public void setImei(String imei) {
        this.getUserContextData().setImei(imei);
    }

    @Override
    public String getClientID() {
        return this.getUserContextData().getClientID();
    }

    @Override
    public void setClientID(String clientID) {
        this.getUserContextData().setClientID(clientID);
    }

    @Override
    public Integer getTdUserID() {
        return this.getUserContextData().getTdUserID();
    }

    @Override
    public void setTdUserID(Integer tdUserID) {
        this.getUserContextData().setTdUserID(tdUserID);
    }

    @Override
    public String getAcctNo() {
        return this.getUserContextData().getAcctNo();
    }

    @Override
    public void setAcctNo(String acctNo) {
        this.getUserContextData().setAcctNo(acctNo);
    }

    @Override
    public String getCustId() {
        return this.getUserContextData().getCustId();
    }

    @Override
    public void setCustId(String custId) {
        this.getUserContextData().setCustId(custId);
    }

    @Override
    public String getVersion() {
        return this.getUserContextData().getVersion();
    }

    @Override
    public void setVersion(String version) {
        this.getUserContextData().setVersion(version);
    }

    @Override
    public String getTdSessionID() {
        return this.getUserContextData().getTdSessionID();
    }

    @Override
    public void setTdSessionID(String sessionId) {
        this.getUserContextData().setTdSessionID(sessionId);
    }

    @Override
    public String getTdSessionKey() {
        return this.getUserContextData().getTdSessionKey();
    }

    @Override
    public void setTdSessionKey(String sessionKey) {
        this.getUserContextData().setTdSessionKey(sessionKey);
    }
}
