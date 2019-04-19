package com.e_commerce.miscroservice.operate.controller.user;

import com.e_commerce.miscroservice.operate.rpc.user.MemberRpcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/12/18 17:44
 * @Copyright 玖远网络
 */
@RestController
@RequestMapping( "operator/user/member" )
public class OperateMemberController{


    @Autowired
    private MemberRpcService memberRpcService;



    /**
     * 提交共享小程序店铺资料
     *
     * @param bossName 店主名称
     * @param shopName 店铺名称
     * @param industry 行业
     * @param phone 手机号
     * @param mainBusiness 主营业务
     * @param address 地址
     * @return com.e_commerce.miscroservice.commons.helper.util.service.Response
     * @author Charlie
     * @date 2018/12/12 13:37
     */
    @RequestMapping( "openInShopMemberOffline" )
    public String openInShopMemberOffline(
            @RequestParam( "bossName" ) String bossName,
            @RequestParam( "shopName" ) String shopName,
            @RequestParam( "industry" ) String industry,
            @RequestParam( "mainBusiness" ) String mainBusiness,
            @RequestParam( "phone" ) String phone,
            @RequestParam( "memberType" ) Integer memberType,
            @RequestParam( "address" ) String address
    ) {
        return memberRpcService.openInShopMemberOffline (
                bossName,
                shopName,
                industry,
                mainBusiness,
                phone,
                memberType,
                address
        );
    }


    /**
     * 更新用户会员状态
     *
     * @param memberId memberId
     * @param delStatus 0 正常  1 删除
     * @return java.lang.String
     * @author Charlie
     * @date 2018/12/19 11:32
     */
    @RequestMapping( "updMember/delStatus" )
    public String updMemberDelStatus(@RequestParam("memberId") Long memberId, @RequestParam("delStatus") Integer delStatus) {
        return memberRpcService.updMemberDelStatus (memberId, delStatus);
    }

}
