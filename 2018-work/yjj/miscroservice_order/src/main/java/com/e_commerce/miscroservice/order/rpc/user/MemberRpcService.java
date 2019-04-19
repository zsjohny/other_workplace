package com.e_commerce.miscroservice.order.rpc.user;

import com.e_commerce.miscroservice.commons.entity.user.MemberOperatorRequest;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/12/11 15:33
 * @Copyright 玖远网络
 */
@FeignClient(value = "USER", path = "user/rpc/memberRpcController")
public interface MemberRpcService{

    /**
     * 购买会员成功
     *
     * @param request request
     * @return boolean
     * @author Charlie
     * @date 2018/12/13 9:11
     */
    @RequestMapping("buyMemberSuccess")
    boolean buyMemberSuccess(@RequestBody MemberOperatorRequest request);

}
