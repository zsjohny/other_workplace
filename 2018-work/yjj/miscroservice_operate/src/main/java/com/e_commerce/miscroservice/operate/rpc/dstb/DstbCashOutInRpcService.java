package com.e_commerce.miscroservice.operate.rpc.dstb;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


/**
 * @author Charlie
 * @version V1.0
 * @date 2018/11/5 15:15
 * @Copyright 玖远网络
 */
@FeignClient(value = "DISTRIBUTION", path = "dstb/rpc/cashOutIn")
public interface DstbCashOutInRpcService{

    /**
     * 提现审核
     *
     * @param cashOutId 流水id
     * @param isPass 1 通过,0 拒绝提现
     * @param ipAddress ip
     * @return com.e_commerce.miscroservice.commons.helper.util.service.Response
     * @author Charlie
     * @date 2018/10/30 20:43
     */
    @RequestMapping("cashOutAudit")
    String cashOutAudit(@RequestParam("cashOutId") Long cashOutId, @RequestParam("isPass") Integer isPass, @RequestParam("ipAddress") String ipAddress);



}
