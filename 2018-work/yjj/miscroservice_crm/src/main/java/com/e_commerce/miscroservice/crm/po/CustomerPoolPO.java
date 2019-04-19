package com.e_commerce.miscroservice.crm.po;

import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Id;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Table;
import com.e_commerce.miscroservice.commons.annotations.application.IsEmptyAnnotation;
import com.e_commerce.miscroservice.commons.helper.handler.DbHandler;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 公海池类
 *
 * @author hyf
 * @version V1.0
 * @date 2018/9/13 19:49
 * @Copyright 玖远网络
 */
@Data
@Table("yjj_customer_pool")
public class CustomerPoolPO implements Serializable {
    @Id
    private Long id;

    @Column(value = "user_id", commit = "所属Id", length = 20)
    private Long userId;

    @Column(value = "business_name", commit = "企业名称", length = 256)
    private String businessName;

    @Column(value = "artificial_person_name", commit = "法人姓名", length = 256)
    private String artificialPersonName;

    @Column(value = "business_licence", commit = "营业执照", length = 256)
    private String businessLicence;

    @Column(value = "business_url", commit = "公司网址", length = 256)
    private String businessUrl;

    @Column(value = "position", commit = "职位", length = 256)
    private String position;

    @IsEmptyAnnotation
    @Column(value = "customer_source", commit = "客户来源", length = 256)
    private String customerSource;

    @IsEmptyAnnotation
    @Column(value = "customer_grade", commit = "客户类型", length = 256)
    private String customerGrade;

    @IsEmptyAnnotation
    @Column(value = "name", commit = "姓名", length = 256)
    private String name;

    @IsEmptyAnnotation
    @Column(value = "phone", commit = "商家电话", length = 256)
    private String phone;

    @IsEmptyAnnotation
    @Column(value = "province", defaultVal = "-", commit = "省", length = 256)
    private String province;

    @IsEmptyAnnotation
    @Column(value = "city", defaultVal = "-", commit = "市", length = 256)
    private String city;

    @IsEmptyAnnotation
    @Column(value = "district", defaultVal = "-", commit = "区/县", length = 256)
    private String district;

    @IsEmptyAnnotation
    @Column(value = "profession", commit = "行业", length = 256)
    private String profession;

    @IsEmptyAnnotation
    @Column(value = "main_business", commit = "主营业务", length = 256)
    private String mainBusiness;

    @IsEmptyAnnotation
    @Column(value = "lastest_record", defaultVal = "-", commit = "最新跟进记录", length = 256)
    private String lastestRecord;

    @IsEmptyAnnotation
    @Column(value = "belonger", defaultVal = "-", commit = "归属人", length = 256)
    private String belonger;

    @Column(value = "type", defaultVal = "未领取", commit = "状态 0：未领取 1：已领取 2：已废弃 3：已签约", length = 256)
    private String type;

    @Column(value = "del_state", defaultVal = "0", commit = "默认0 正常  1 删除", length = 4)
    private Integer delState;

    @Column(value = "create_time", dateGeneStrategy = DbHandler.DateGeneStrategy.CREATE, commit = "创建时间")
    private Timestamp createTime;

    @Column(value = "update_time", dateGeneStrategy = DbHandler.DateGeneStrategy.UPDATE, commit = "修改时间")
    private Timestamp updateTime;


}
