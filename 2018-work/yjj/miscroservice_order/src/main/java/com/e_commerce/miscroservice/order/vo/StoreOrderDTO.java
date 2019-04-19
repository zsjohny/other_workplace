package com.e_commerce.miscroservice.order.vo;

import com.e_commerce.miscroservice.order.entity.StoreOrder;
import lombok.Builder;
import lombok.Data;

/**
 * @author Charlie
 * @version V1.0
 * @date 2019/2/17 11:09
 */
@Data
public class StoreOrderDTO extends StoreOrder {

    /**
     * 小程序订单item
     */
    private Long shopOrderItemId;

}
