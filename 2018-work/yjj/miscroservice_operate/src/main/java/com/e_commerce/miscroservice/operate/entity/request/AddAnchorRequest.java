package com.e_commerce.miscroservice.operate.entity.request;

import com.e_commerce.miscroservice.commons.annotations.application.IsEmptyAnnotation;
import lombok.Data;

/**
 * @Author hyf
 * @Date 2019/1/15 11:46
 */
@Data
public class AddAnchorRequest {

    /**
     * 姓名
     */
    @IsEmptyAnnotation
    private String name;
    /**
     * 昵称
     */
    @IsEmptyAnnotation
    private String nickName;
    /**
     * 头像
     */
    @IsEmptyAnnotation
    private String icon;
    /**
     * 年龄
     */
    @IsEmptyAnnotation
    private Integer age;

    /**
     * 身份证
     */
    @IsEmptyAnnotation
    private String idCard;

    /**
     * 手机号
     */
    @IsEmptyAnnotation
    private String phone;

    /**
     * 性别
     */
    @IsEmptyAnnotation
    private Integer sex;


    private Long storeId;

    /**
     * 主播类型
     */
    private Integer liveType;
}
