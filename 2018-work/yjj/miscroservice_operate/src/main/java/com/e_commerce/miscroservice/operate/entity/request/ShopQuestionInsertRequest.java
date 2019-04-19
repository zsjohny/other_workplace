package com.e_commerce.miscroservice.operate.entity.request;

import com.e_commerce.miscroservice.commons.annotations.application.IsEmptyAnnotation;
import lombok.Data;

/**
 * @author hyf
 * @version V1.0
 * @date 2018/11/26 11:46
 * @Copyright 玖远网络
 */
@Data
public class ShopQuestionInsertRequest {
    @IsEmptyAnnotation
    private Long questionTypeId;
    @IsEmptyAnnotation
    private Integer sort;
    @IsEmptyAnnotation
    private String question;
    @IsEmptyAnnotation
    private String answer;

    private Long  id;
}
