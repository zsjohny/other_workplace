package com.jiuyuan.util.oauth.sns.common.user;

import java.io.Serializable;
import java.util.Date;

import com.jiuyuan.constant.Tristate;

public interface ISnsEndUser extends Serializable {

    /** openid/uid，但不保证跨平台（目前只有微信不是跨平台的，其它都是） */
    String getId();

    /** 跨平台ID（目前只有微信对应unionid，其它对应openid/uid） */
    String getPlatformIndependentId();

    String getRealName();

    String getNickName();

    String getSymbolicName();

    String getEmail();

    String getAvatar();

    Tristate getMale();

    String getDescription();

    Tristate getVerified();

    Date getRegisterTime();

    String getHomePage();
}
