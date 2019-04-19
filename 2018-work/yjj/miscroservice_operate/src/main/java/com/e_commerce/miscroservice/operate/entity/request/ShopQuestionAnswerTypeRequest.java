package com.e_commerce.miscroservice.operate.entity.request;

import com.e_commerce.miscroservice.commons.entity.BaseEntity;
import lombok.Data;

/**
 * @author hyf
 * @version V1.0
 * @date 2018/11/23 16:16
 * @Copyright 玖远网络
 */
@Data
public class ShopQuestionAnswerTypeRequest extends BaseEntity {

    /**
     * 类型名称
     */
    private String typeName;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建时间起
     */
    private String createTimeStart;

    /**
     * 创建时间止
     */
    private String createTimeEnd;
    /**
     * 问题名称
     */
    private String questionName;
    /**
     * 0 显示 1隐藏
     */
    private Integer status;

    /**
     * 问题类型
     */
    private Integer typeStatus;


}
