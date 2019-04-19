package com.e_commerce.miscroservice.operate.rpc.user;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/12/19 1:07
 * @Copyright 玖远网络
 */
@FeignClient(value = "USER", path = "user/rpc/memberRpcController")
public interface MemberRpcService{


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
    @RequestMapping("openInShopMemberOffline")
    String openInShopMemberOffline(
            @RequestParam( "bossName" ) String bossName,
            @RequestParam( "shopName" ) String shopName,
            @RequestParam( "industry" ) String industry,
            @RequestParam( "mainBusiness" ) String mainBusiness,
            @RequestParam( "phone" ) String phone,
            @RequestParam( "memberType" ) Integer memberType,
            @RequestParam( "address" ) String address
    );


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
    String updMemberDelStatus(@RequestParam("memberId") Long memberId, @RequestParam("delStatus") Integer delStatus);
}
