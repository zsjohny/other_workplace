package com.e_commerce.miscroservice.operate.entity.request;

import com.e_commerce.miscroservice.commons.utils.TimeUtils;
import lombok.Data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author hyf
 * @version V1.0
 * @date 2018/10/18 11:07
 * @Copyright 玖远网络
 */
@Data
public class AuditManagementRequest {
    /**
     * 申请时间 起
     */
    private Long commitTimeStart;
    /**
     * 申请时间 止
     */
    private Long commitTimeEnd;
    /**
     * 商铺名称
     */
    private String storeName;
    /**
     * 单号id
     */
    private Long id;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 用户id 会员id
     */
    private Long userId;
    /**
     * 昵称
     */
    private String nickName;
    /**
     * 审核时间 起
     */
    private Long auditTimeStart;
    /**
     * 审核时间 止
     */
    private Long auditTimeEnd;

    /**
     * 真实姓名
     */
    private String realName;


    public void setCommitTimeStart(String commitTimeStart) {
        this.commitTimeStart = TimeUtils.str2Long (commitTimeStart);
    }

    public void setCommitTimeEnd(String commitTimeEnd) {
        this.commitTimeEnd = TimeUtils.str2Long (commitTimeEnd);
    }

    public void setAuditTimeStart(String auditTimeStart) {
        this.auditTimeStart = TimeUtils.str2Long (auditTimeStart);
    }

    public void setAuditTimeEnd(String auditTimeEnd) {
        this.auditTimeEnd = TimeUtils.str2Long (auditTimeEnd);
    }

    /**
     * 页码
     */
    private Integer pageNum;
    private Integer pageSize;


}
