package com.e_commerce.miscroservice.product.vo;

import com.e_commerce.miscroservice.product.entity.ProductSku;
import lombok.Data;

import java.util.List;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/11/28 13:00
 * @Copyright 玖远网络
 */
@Data
public class ProductSkuQuery extends ProductSku{
    /**
     * 状态:-3废弃，-2停用，-1下架，0正常，1定时上架
     */
    private List<Integer> statusList;

}
