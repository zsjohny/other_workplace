package com.e_commerce.miscroservice.operate.rpc.user;

import com.e_commerce.miscroservice.commons.entity.application.user.PublicAccountUser;
import com.e_commerce.miscroservice.commons.entity.user.PublicAccountUserQuery;
import com.github.pagehelper.PageInfo;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/9/25 12:37
 * @Copyright 玖远网络
 */
@FeignClient(value = "PUBLICACCOUNT", path = "public/rpc/publicAccountUser"/*, fallback = ProxyCustomerRpcService.class*/)
public interface PublicAccountRpcService{

    /**
     * 公众号用户列表
     *
     * @return com.github.pagehelper.PageInfo<com.e_commerce.miscroservice.commons.entity.user.PublicAccountUser>
     * @author Charlie
     * @date 2018/9/25 12:45
     */
    @GetMapping( "listUser" )
    PageInfo<PublicAccountUserQuery> listUser(@RequestBody PublicAccountUserQuery query);

    @RequestMapping( "updateById" )
    int updateById(@RequestBody PublicAccountUser publicAccountUser);

    @RequestMapping( "stopCustomer" )
    String stopCustomer(@RequestParam("userId") long userId, @RequestParam("type") int type);
}
