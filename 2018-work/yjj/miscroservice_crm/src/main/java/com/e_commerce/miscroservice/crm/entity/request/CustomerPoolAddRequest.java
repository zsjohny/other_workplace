package com.e_commerce.miscroservice.crm.entity.request;

import com.e_commerce.miscroservice.commons.annotation.colligate.table.Table;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Transient;
import com.e_commerce.miscroservice.commons.annotations.application.AnnotationUtils;
import com.e_commerce.miscroservice.commons.annotations.application.IsEmptyAnnotation;
import lombok.Data;

/**
 * 添加公海池客户
 *
 * @author hyf
 * @version V1.0
 * @date 2018/9/14 16:37
 * @Copyright 玖远网络
 */
@Data
@Table("yjj_customer_pool")
public class CustomerPoolAddRequest {


    /**
     * 归属用户id
     */
    private Long userId;


    /**
     * 企业名称
     */
    private String businessName;


    /**
     * 法人姓名
     */
    private String artificialPersonName;


    /**
     * 营业执照
     */
    private String businessLicence;


    /**
     * 公司网址
     */
    private String businessUrl;


    /**
     * 职位
     */
    private String position;


    /**
     * 客户来源
     */
    @IsEmptyAnnotation
    private String customerSource;


    /**
     * 客户类型
     */
    @IsEmptyAnnotation
    private String customerGrade;


    /**
     * 姓名
     */
    @IsEmptyAnnotation
    private String name;


    /**
     * 客户电话
     */
    @IsEmptyAnnotation
    private String phone;


    /**
     * 省
     */
    @IsEmptyAnnotation
    private String province;


    /**
     * 市
     */
    @IsEmptyAnnotation
    private String city;


    /**
     * 区
     */
    @IsEmptyAnnotation
    private String district;


    /**
     * 所属行业
     */
    @IsEmptyAnnotation
    private String profession;


    /**
     * 主营业务
     */
    @IsEmptyAnnotation
    private String mainBusiness;


    /**
     * 状态 0：未领取 1：已领取 2：已废弃 3：已签约
     */
    private String type;

    /**
     * 添加状态 0公海池 1 客户管理池
     */
    @IsEmptyAnnotation
    @Transient
    private Integer addStatus;

    /**
     * 最新跟进记录
     */
    @IsEmptyAnnotation
    private String lastestRecord;

    /**
     * 归属人
     */
    @IsEmptyAnnotation
    private String belonger;

    public static void main(String[] args) {
        CustomerPoolAddRequest customerPoolAddRequest = new CustomerPoolAddRequest();
        customerPoolAddRequest.setCustomerSource("aa");
        System.out.println(AnnotationUtils.validate(customerPoolAddRequest));
    }
}

