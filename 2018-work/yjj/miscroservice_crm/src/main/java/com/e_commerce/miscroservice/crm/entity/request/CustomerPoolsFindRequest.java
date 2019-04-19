package com.e_commerce.miscroservice.crm.entity.request;

import com.e_commerce.miscroservice.crm.entity.BaseEntity;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

/**
 * 查询 公海池
 * @author hyf
 * @version V1.0
 * @date 2018/9/13 21:34
 * @Copyright 玖远网络
 */
@Data
public class CustomerPoolsFindRequest extends BaseEntity {

    /**
     * 用户姓名
     */
    private String name;

    /**
     * 用户手机号
     */
    private String phone;

    /**
     * 省
     */
    private String province;

    /**
     * 市
     */
    private String city;

    /**
     * 区
     */
    private String district;

    /**
     * 创建时间 起
     */
    private String timeStart;

    /**
     * 创建时间 止
     */
    private String timeEnd;

    /**
     * 用户类型
     */
    private String type;

    /**
     * 所属员工用户姓名
     */
    private String belonger;

    /**
     * 用户所属id
     */
    private Long userId;

    /**
     * 查询状态 0：所有的 公海池 1： 所属id下的 用户
     */
    private Integer status;
}
