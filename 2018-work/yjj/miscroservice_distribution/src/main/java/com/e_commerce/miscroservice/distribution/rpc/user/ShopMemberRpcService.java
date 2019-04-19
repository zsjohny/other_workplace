package com.e_commerce.miscroservice.distribution.rpc.user;

import com.e_commerce.miscroservice.commons.entity.application.user.ShopMemberVo;
import com.e_commerce.miscroservice.commons.entity.application.user.ShopMemberVo;
import com.e_commerce.miscroservice.commons.entity.user.ShopMemberQuery;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/10/14 22:27
 * @Copyright 玖远网络
 */
@FeignClient(value = "USER", path = "user/rpc/shopMemberRpcController")
public interface ShopMemberRpcService{

    @RequestMapping("findOne")
    ShopMemberVo findOne(@RequestBody ShopMemberQuery query);
}
