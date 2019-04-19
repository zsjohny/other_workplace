package com.e_commerce.miscroservice.store.rpc;

import com.e_commerce.miscroservice.store.entity.vo.ShopMember;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Author hyf
 * @Date 2019/1/18 16:27
 */
@FeignClient(value = "USER",path = "user/rpc/shopMemberRpcController")
public interface ShopMemberRpcService {

    /**
     * 根据id查询会员
     * @param memberId
     * @return
     */
    @RequestMapping("findById")
    public ShopMember findById(@RequestParam(value = "memberId") Long memberId) ;
}
