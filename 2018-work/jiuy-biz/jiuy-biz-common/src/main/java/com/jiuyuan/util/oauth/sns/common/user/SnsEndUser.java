package com.jiuyuan.util.oauth.sns.common.user;

import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.jiuyuan.constant.Tristate;

public class SnsEndUser implements ISnsEndUser {

    private static final long serialVersionUID = 7762198778972903375L;

    /** openid/uid，但不保证跨平台（目前只有微信不是跨平台的，其它都是） */
    private String id;

    /** 跨平台ID（目前只有微信对应UnionId，其它对应openid/uid） */
    private String platformIndependentId;

    private String realName;

    private String nickName;

    private String symbolicName;

    private String email;

    private String avatar;

    private Tristate male = Tristate.UNCERTAIN;

    private String description;

    private Tristate verified = Tristate.UNCERTAIN;

    private Date registerTime;

    private String homePage;

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getPlatformIndependentId() {
        return platformIndependentId;
    }

    public void setPlatformIndependentId(String platformIndependentId) {
        this.platformIndependentId = platformIndependentId;
    }

    @Override
    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    @Override
    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    @Override
    public String getSymbolicName() {
        return symbolicName;
    }

    public void setSymbolicName(String symbolicName) {
        this.symbolicName = symbolicName;
    }

    @Override
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @Override
    public Tristate getMale() {
        return male;
    }

    public void setMale(Tristate male) {
        this.male = male;
    }

    @Override
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public Tristate getVerified() {
        return verified;
    }

    public void setVerified(Tristate verified) {
        this.verified = verified;
    }

    @Override
    public Date getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(Date registerTime) {
        this.registerTime = registerTime;
    }

    public String getHomePage() {
        return homePage;
    }

    public void setHomePage(String homePage) {
        this.homePage = homePage;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append("id", id).append(
            "platformIndependentId", platformIndependentId).append("realName", realName).append("nickName", nickName).append(
            "symbolicName", symbolicName).append("email", email).append("avatar", avatar).append("male", male).append(
            "description", description).append("verified", verified).append("registerTime", registerTime).append(
            "homePage", homePage).toString();
    }
}