package com.e_commerce.miscroservice.crm.entity.request;

import com.e_commerce.miscroservice.commons.annotations.application.IsEmptyAnnotation;
import lombok.Data;

/**
 * 领取 退回公海池用户
 * @author hyf
 * @version V1.0
 * @date 2018/9/18 11:12
 * @Copyright 玖远网络
 */
@Data
public class CustomerPoolGetRequest {
    /**
     * 客户id  用“,”分割
     */
    @IsEmptyAnnotation
    private String ids;

    /**
     * 所属id
     */
    @IsEmptyAnnotation
    private Long userId;

    /**
     * 领取 退回  公海池状态
     * 0 领取  1 退回 2分配
     */
    @IsEmptyAnnotation
    private Integer status;

    /**
     * 被分配的用户 姓名
     */
    private String allotName;
}
