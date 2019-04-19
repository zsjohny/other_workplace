package com.e_commerce.miscroservice.publicaccount.entity.vo;

import com.e_commerce.miscroservice.commons.entity.BaseEntity;
import com.e_commerce.miscroservice.commons.utils.TimeUtils;
import lombok.Data;

import java.sql.Timestamp;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/9/26 7:22
 * @Copyright 玖远网络
 */
@Data
public class ProxyRefereeUserInfo extends BaseEntity{

    /**
     * 关系id
     */
    private Long refereeId;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 微信昵称
     */
    private String wxName;
    /**
     * 真实名称
     */
    private String userName;
    /**
     * 头像
     */
    private String wxUserIcon;

    private String phone;
    private String idCardNo;

    private String province;
    private String city;
    private String county;
    private String addressDetail;

    private Timestamp createTime;

    /**
     * @see ProxyRefereeUserInfo#createTime
     */
    private String createTimeReadable;

    public String getCreateTimeReadable() {
        return TimeUtils.stamp2Str (getCreateTime ());
    }
}
