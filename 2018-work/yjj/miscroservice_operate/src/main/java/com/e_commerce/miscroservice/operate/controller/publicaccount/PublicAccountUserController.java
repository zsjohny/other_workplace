package com.e_commerce.miscroservice.operate.controller.publicaccount;

import com.e_commerce.miscroservice.commons.entity.application.user.PublicAccountUser;
import com.e_commerce.miscroservice.commons.entity.user.PublicAccountUserQuery;
import com.e_commerce.miscroservice.commons.helper.util.service.Response;
import com.e_commerce.miscroservice.operate.rpc.user.PublicAccountRpcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/9/25 11:31
 * @Copyright 玖远网络
 */
@RestController
@RequestMapping( "/operate/publicAccountUser" )
public class PublicAccountUserController{

    @Autowired
    private PublicAccountRpcService publicAccountRpcService;


    /**
     * 公众号用户列表
     *
     * @param pageNumber       分页
     * @param pageSize         分页
     * @param wxName           微信名
     * @param userName         用户名
     * @param phone            手机号
     * @param createTimeBefore 注册起始时间
     * @param createTimeAfter  注册最大时间
     * @return com.github.pagehelper.PageInfo<com.e_commerce.miscroservice.commons.entity.user.PublicAccountUser>
     * @author Charlie
     * @date 2018/9/25 12:45
     */
    @RequestMapping( "listUser" )
    public Response listUser(
            @RequestParam( value = "pageNumber", required = false, defaultValue = "14" ) Integer pageNumber,
            @RequestParam( value = "pageSize", required = false, defaultValue = "0" ) Integer pageSize,
            @RequestParam( value = "wxName", required = false ) String wxName,
            @RequestParam( value = "userName", required = false ) String userName,
            @RequestParam( value = "phone", required = false ) String phone,
            @RequestParam( value = "createTimeBefore", required = false ) String createTimeBefore,
            @RequestParam( value = "createTimeAfter", required = false ) String createTimeAfter) {

        PublicAccountUserQuery query = new PublicAccountUserQuery ();
        query.setPageNumber (pageNumber);
        query.setPageSize (pageSize);
        query.setWxName (wxName);
        query.setPhone (phone);
        query.setUserName (userName);
        query.setCreateTimeBefore (createTimeBefore);
        query.setCreateTimeAfter (createTimeAfter);
        return Response.success (publicAccountRpcService.listUser (query));
    }


    /**
     * 公众号用户列表
     *
     * @param publicAccountUserId 公众号用户id
     * @param delStatus           0正常,1删除(禁用)
     * @return com.github.pagehelper.PageInfo<com.e_commerce.miscroservice.commons.entity.user.PublicAccountUser>
     * @author Charlie
     * @date 2018/9/25 12:45
     */
    @RequestMapping( "updateDelStatus" )
    public Response listUser(Long publicAccountUserId, Integer delStatus) {
        PublicAccountUser updInfo = new PublicAccountUser ();
        updInfo.setDelStatus (delStatus);
        updInfo.setId (publicAccountUserId);
        return Response.success (publicAccountRpcService.updateById (updInfo));
    }


}
