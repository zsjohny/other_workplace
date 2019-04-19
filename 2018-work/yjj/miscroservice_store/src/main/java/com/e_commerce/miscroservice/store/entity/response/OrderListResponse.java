package com.e_commerce.miscroservice.store.entity.response;

import com.e_commerce.miscroservice.store.entity.vo.StoreOrder;
import com.e_commerce.miscroservice.store.entity.vo.OrderDetailsVo;
import lombok.Data;

import java.util.List;

/**
 * @author hyf
 * @version V1.0
 * @date 2018/9/29 10:33
 * @Copyright 玖远网络
 */
@Data
public class OrderListResponse extends StoreOrder {
    /**
     * 详情列表
     */
    private List<OrderDetailsVo> orderDetailsVoList;



    /**
     * 品牌名称
     */
    private String brandName;

    /**
     * 品牌id
     */
    private Long brandId;





}
