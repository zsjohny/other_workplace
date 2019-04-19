package com.e_commerce.miscroservice.product.entity;

import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Id;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Table;
import lombok.Data;

/**
 * @author Charlie
 * @version V1.0
 * @date 2019/1/31 16:06
 */
@Data
@Table(value = "yjj_dynamic_property_category")
public class DynamicPropertyCategory {


    /**
     * 主键id
     */
    @Id
    private Long id;
    /**
     * 分类id
     */
    @Column(value = "cate_id", length = 20, commit = "分类id", isNUll = false)
    private Long cateId;
    /**
     * 动态属性id
     */
    @Column(value = "dyna_prop_id", length = 20, commit = "动态属性id", isNUll = false)
    private Long dynaPropId;
    /**
     * 状态 0：禁用，1：启用
     */
    @Column(value = "status", length = 4, commit = "状态 0：禁用，1：启用", isNUll = false, defaultVal = "1")
    private Integer status;
    /**
     * 排序值
     */
    @Column(value = "weight", length = 11, commit = "排序值", isNUll = false)
    private Integer weight;
    /**
     * 创建时间
     */
    @Column(value = "create_time", length = 20, commit = "创建时间")
    private Long createTime;
    /**
     * 修改时间
     */
    @Column(value = "update_time", length = 20, commit = "修改时间")
    private Long updateTime;


}
