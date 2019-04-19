package com.e_commerce.miscroservice.activity.entity;

import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Id;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Table;
import com.e_commerce.miscroservice.commons.helper.handler.DbHandler;
import lombok.Data;

import java.sql.Timestamp;

/**
 * @author hyf
 * @version V1.0
 * @date 2018/11/22 16:16
 * @Copyright 玖远网络
 */
@Data
@Table("yjj_activity_user")
public class ActivityUser {

    @Id
    private Long id;

    @Column(value = "name",commit = "姓名",length = 256)
    private String name;

    @Column(value = "phone",commit = "手机号",length = 256)
    private String phone;

    @Column(value = "province",commit = "省",length = 256)
    private String province;

    @Column(value = "city",commit = "市",length = 256)
    private String city;

    @Column(value = "district",commit = "区",length = 256)
    private String district;

    @Column(value = "apply_time",commit = "提交时间",length = 20)
    private Long applyTime;

    @Column(value = "status",defaultVal = "0",length = 4,commit = "用户类型")
    private Integer status;

    @Column(value = "status_name",length = 256,commit = "用户类型 名称")
    private String statusName;

    @Column(value = "del_status",defaultVal = "0",length = 4,commit = "是否删除 默认0 未删除  1删除")
    private Integer delStatus;

    @Column(value = "create_time",dateGeneStrategy = DbHandler.DateGeneStrategy.CREATE,commit = "创建时间")
    private Timestamp createTime;

    @Column(value = "update_time",dateGeneStrategy = DbHandler.DateGeneStrategy.UPDATE,commit = "更新时间")
    private Timestamp updateTime;

}
