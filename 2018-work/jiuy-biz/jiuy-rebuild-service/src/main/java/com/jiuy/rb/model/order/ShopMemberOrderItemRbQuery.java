package com.jiuy.rb.model.order; 

import lombok.Data; 

/**
 * ShopMemberOrderItemRb的拓展实体。
 * 添加此类是为了避免污染映射的pojo,并解决查询使用map维护难的问题
 * 
 * @author Aison
 * @version V1.0  
 * @date 2018年07月18日 上午 09:18:34
 * @Copyright 玖远网络 
*/
@Data
public class ShopMemberOrderItemRbQuery extends ShopMemberOrderItemRb {

    /**
     * 商品详情图片
     */
    private String detailImage;

    /**
     * 商品名称
     */
    private String productName;

    /**
     * 商品款号
     */
    private String clothesNumber;
 
} 
