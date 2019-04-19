package com.e_commerce.miscroservice.operate.entity.request;

import com.e_commerce.miscroservice.commons.annotations.application.IsEmptyAnnotation;
import lombok.Data;

/**
 * @Author hyf
 * @Date 2019/1/8 17:20
 */
@Data
public class ActivityImageShareAddRequest {

    private Long id;

    @IsEmptyAnnotation
    private Integer shareType;
    @IsEmptyAnnotation
    private Integer type;
    @IsEmptyAnnotation
    private String mainMap;
    @IsEmptyAnnotation
    private String wxImg;


}
