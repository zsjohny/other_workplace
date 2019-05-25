package com.songxm.credit.dao.credit.deversion.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Table(name = "t_user_login_info")
@Data
public class TUserLoginInfo extends BaseDO {
    /**
     * 作为用户ID
     */
    @Id
    private String id;

    /**
     * 登录号
     */
    @Column(name = "login_name")
    private String loginName;

    /**
     * 登录密码
     */
    @Column(name = "login_pass")
    private String loginPass;

    /**
     * 盐值
     */
    @Column(name = "login_salt")
    private String loginSalt;

    /**
     * 登录状态
     */
    @Column(name = "login_status")
    private String loginStatus;

    /**
     * 最后登录时间
     */
    @Column(name = "last_login_time")
    private Date lastLoginTime;

    /**
     * 登录号签约渠道
     */
    @Column(name = "logid_signchannel")
    private String logidSignchannel;

    /**
     * 修改前登录用户号
     */
    @Column(name = "loginid_beformodify")
    private String loginidBeformodify;




}