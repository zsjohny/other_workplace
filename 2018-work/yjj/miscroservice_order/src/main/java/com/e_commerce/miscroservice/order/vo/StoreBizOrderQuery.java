package com.e_commerce.miscroservice.order.vo;

import com.alibaba.fastjson.JSONObject;
import com.e_commerce.miscroservice.order.entity.StoreBizOrder;
import lombok.Data;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/12/11 15:01
 * @Copyright 玖远网络
 */
@Data
public class StoreBizOrderQuery extends StoreBizOrder{

    private Long storeId;

    private Integer memberType;

    /**
     * 购买渠道1app购买,2线下,3公众号购买,4店中店购买
     */
    private Integer canal;


    /**
     * 1购买店中店会员,2购买APP会员,10购买APP商品
     */
    private Integer operType;

    private Long inShopMemberId;

    private String ip;

    private JSONObject attach;
}
