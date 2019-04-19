package com.e_commerce.miscroservice.product.vo;

import com.e_commerce.miscroservice.commons.entity.application.order.Product;
import lombok.Data;

/**
 * @author Charlie
 * @version V1.0
 * @date 2019/1/14 16:13
 * @Copyright 玖远网络
 */
@Data
public class ProductVO extends Product {

    private Integer pageSize;

    private Integer pageNumber;

    /**
     * 主播id
     */
    private Long anchorId;

}
