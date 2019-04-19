package com.e_commerce.miscroservice.product.rpc;

import com.e_commerce.miscroservice.commons.entity.order.ShopMemberOrderItemQuery;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Charlie
 * @version V1.0
 * @date 2019/1/30 10:18
 */
@FeignClient(value = "ORDER", path = "order/rpc/shopMemberOrder")
public interface ShopMemberOrderRpcService {

    @RequestMapping( "findItemByItemId" )
    ShopMemberOrderItemQuery findItemByItemId(@RequestParam( "orderItemId" ) Long orderItemId);
}
