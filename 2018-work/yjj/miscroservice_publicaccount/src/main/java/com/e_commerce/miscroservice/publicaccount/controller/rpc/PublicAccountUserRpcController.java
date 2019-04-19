package com.e_commerce.miscroservice.publicaccount.controller.rpc;

import com.alibaba.fastjson.JSON;
import com.e_commerce.miscroservice.commons.annotation.service.InnerRestController;
import com.e_commerce.miscroservice.commons.entity.application.user.PublicAccountUser;
import com.e_commerce.miscroservice.commons.entity.user.PublicAccountUserQuery;
import com.e_commerce.miscroservice.commons.helper.util.service.Response;
import com.e_commerce.miscroservice.publicaccount.service.user.PublicAccountUserService;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 用户查询
 *
 * @author Charlie
 * @version V1.0
 * @date 2018/9/21 18:24
 * @Copyright 玖远网络
 */
@InnerRestController
@RequestMapping("public/rpc/publicAccountUser")
public class PublicAccountUserRpcController {

    @Autowired
    private PublicAccountUserService publicAccountUserService;

    /**
     * 根据手机号查询用户信息
     *
     * @param phone 手机号
     * @return com.e_commerce.miscroservice.commons.utils.Response
     * @author Charlie
     * @date 2018/9/24 15:11
     */
    @RequestMapping("findByPhone")
    public PublicAccountUser findByPhone(String phone) {
        return StringUtils.isBlank(phone) ? null : publicAccountUserService.findByPhone(phone);
    }

    /**
     * 更新用户信息
     *
     * @param publicAccountUser 根据id修改用户信息
     * @return com.e_commerce.miscroservice.commons.utils.Response
     * @author Charlie
     * @date 2018/9/24 15:11
     */
    @RequestMapping("updateById")
    public int updateById(@RequestBody PublicAccountUser publicAccountUser) {
        return publicAccountUserService.updateById(publicAccountUser);
    }

    /**
     * 更新用户信息
     *
     * @param updateInfo 根据手机号修改用户信息
     * @return com.e_commerce.miscroservice.commons.utils.Response
     * @author Charlie
     * @date 2018/9/24 15:11
     */
    @RequestMapping("updateByPhone")
    public PublicAccountUser updateByPhone(@RequestBody PublicAccountUser updateInfo) {
        return publicAccountUserService.updateByPhone(updateInfo);
    }

    /**
     * 查询用户
     */
    @RequestMapping("listUser")
    public PageInfo<PublicAccountUserQuery> listUser(@RequestBody PublicAccountUserQuery query) {
        return publicAccountUserService.listUser(query);
    }



    /**
     * 描述 禁用代理
     * @param userId
     * @author hyq
     * @date 2018/10/17 11:20
     * @return int
     */
    @RequestMapping( "stopCustomer" )
    public String stopCustomer(long userId,int type){
        return JSON.toJSONString(Response.success(publicAccountUserService.stopCustomer(userId,type)));
    }
}
