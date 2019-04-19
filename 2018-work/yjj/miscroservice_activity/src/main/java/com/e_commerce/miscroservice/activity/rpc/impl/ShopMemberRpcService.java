package com.e_commerce.miscroservice.activity.rpc.impl;

import com.e_commerce.miscroservice.commons.entity.application.user.ShopMemberVo;
import com.e_commerce.miscroservice.commons.entity.user.ShopMemberQuery;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/11/22 11:45
 * @Copyright 玖远网络
 */

@FeignClient(value = "USER", path = "user/rpc/shopMemberRpcController")
@Component
public interface ShopMemberRpcService{

    /**
     * 查询一个用户
     */
    @RequestMapping("findOne")
    ShopMemberVo findOne(@RequestBody ShopMemberQuery query);

}
