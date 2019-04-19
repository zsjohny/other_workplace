package com.e_commerce.miscroservice.store.entity.vo;

import lombok.Data;

@Data
public class ShopMemberOrderRequest  extends BaseEntity{
    //商品状态
    private Integer orderStatus;
    //商家id
    private Long shopId;
    //会员id
    private Long userId;
}
