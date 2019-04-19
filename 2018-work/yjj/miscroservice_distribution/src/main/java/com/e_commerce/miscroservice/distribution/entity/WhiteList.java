package com.e_commerce.miscroservice.distribution.entity;

import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Id;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Table;
import com.e_commerce.miscroservice.commons.helper.handler.DbHandler;
import lombok.Data;

import java.sql.Timestamp;

/**
 * 分销白名单
 *
 * @author Charlie
 * @version V1.0
 * @date 2018/12/14 15:04
 * @Copyright 玖远网络
 */
@Data
@Table("yjj_white_list")
public class WhiteList{

    @Id
    private Long id;

    /**
     * 被禁用者,根据type判断
     */
    @Column(value = "target_id", commit = "被禁用者,根据type判断", length = 20, defaultVal = "0")
    private Long targetId;

    /**
     * 白名单类型 1.分销白名单
     */
    @Column(value = "type", commit = "白名单类型 1.分销白名单", length = 4, defaultVal = "1")
    private Integer type;


    @Column(value = "note", commit = "备注", length = 1000)
    private String note;

    /**
     * 0可用,1删除
     */
    @Column(value = "del_status", commit = "0可用,1删除", length = 4, defaultVal = "0")
    private Integer delStatus;

    @Column(value = "create_time",dateGeneStrategy = DbHandler.DateGeneStrategy.CREATE,commit = "创建时间")
    private Timestamp createTime;

    @Column(value = "update_time",dateGeneStrategy = DbHandler.DateGeneStrategy.UPDATE,commit = "更新时间")
    private Timestamp updateTime;

}
