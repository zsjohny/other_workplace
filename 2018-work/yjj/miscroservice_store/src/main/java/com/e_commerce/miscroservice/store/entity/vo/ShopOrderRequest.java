package com.e_commerce.miscroservice.store.entity.vo;

import lombok.Data;

@Data
public class ShopOrderRequest extends BaseEntity {
    private Integer status;//订单状态
    private  Long storeId;//用户id
    private Long supplierId;//供应商Id
}
