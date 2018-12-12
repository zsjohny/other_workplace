package com.finace.miscroservice.activity.po.entity;

import com.finace.miscroservice.commons.annotation.Column;
import com.finace.miscroservice.commons.annotation.Id;
import com.finace.miscroservice.commons.annotation.Table;
import lombok.Data;

@Table("user_channel_generalize")
@Data
public class UserChannelGeneralize {

    @Id
    private Integer id;
//    String cid, String idfa, String keywords, String channel, String timestamp, String sign, String callback, String phone
    @Column(commit = "广告主推广app的Appstore ID")
    private String appid;

    @Column(commit = "idfa")
    private String idfa;

    @Column(commit = "关键字")
    private String keywords;

    @Column(commit = "渠道名称",isNUll = false)
    private String channel;

    @Column(commit = "时间戳",isNUll = false)
    private String timestamp;

    @Column(commit = "回调地址",isNUll = false)
    private String callback;

    @Column(commit = "区分手机系统 android ios",isNUll = false)
    private String phone;

    @Column(commit = "是否激活 default=0 未激活  1 激活",isNUll = false,defaultVal = "0")
    private Integer activate;



}
