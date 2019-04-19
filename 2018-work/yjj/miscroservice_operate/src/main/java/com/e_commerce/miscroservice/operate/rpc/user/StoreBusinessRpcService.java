package com.e_commerce.miscroservice.operate.rpc.user;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/12/17 20:14
 * @Copyright 玖远网络
 */
@FeignClient(value = "USER", path = "user/rpc/storeBusinessRpcController")
public interface StoreBusinessRpcService{

    /**
     * 打开店铺状态
     *
     * @param storeId storeId
     * @param isOpenWxa  是否开通小程序,0未开通(正常)1已开通(正常),2冻结(手工关闭)
     * @return boolean
     * @author Charlie
     * @date 2018/12/17 20:17
     */
    @RequestMapping("openWxaStatus")
    boolean openWxaStatus(@RequestParam("storeId") Long storeId, @RequestParam("isOpenWxa") Integer isOpenWxa);




    /**
     * 初始化小程序开通时间
     *
     * @param storeId 用户id
     * @param wxaOpenTime 开通时间
     * @param wxaCloseTime 结束时间
     * @return boolean
     * @author Charlie
     * @date 2018/12/17 20:46
     */
    @RequestMapping("initWxaOpenTime")
    boolean initWxaOpenTime(@RequestParam("storeId") Long storeId, @RequestParam("wxaOpenTime") Long wxaOpenTime, @RequestParam("wxaCloseTime") Long wxaCloseTime);



}
