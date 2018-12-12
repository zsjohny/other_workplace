package com.finace.miscroservice.user.rpc;

import com.finace.miscroservice.commons.entity.UserAuth;
import com.finace.miscroservice.user.rpc.impl.AuthorizeRpcServiceImpl;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 认证中心的rpc调用
 */
@FeignClient(value = "AUTHORIZE", fallback = AuthorizeRpcServiceImpl.class)
public interface AuthorizeRpcService {

    /**
     * 插入用户
     *
     * @param users 用户的po数组集合
     */
    @RequestMapping(value = "saves", method = RequestMethod.POST)
    Boolean insertUsers(@RequestBody List<UserAuth> users);


    /**
     * 更新用户
     *
     * @param user 用户
     */
    @RequestMapping(value = "update", method = RequestMethod.POST)
    void updateUser(@RequestBody UserAuth user);

    /**
     * 验证用户的密码
     *
     * @param user 用户
     */
    @RequestMapping(value = "auth", method = RequestMethod.POST)
    Boolean checkUserPass(@RequestBody UserAuth user);

    /**
     * 根据用户名查询用户信息
     *
     * @param userName 用户名
     * @return
     */
    @RequestMapping(value = "query", method = RequestMethod.POST)
    UserAuth findUserByName(@RequestParam("userName") String userName);

}
