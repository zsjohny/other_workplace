package com.e_commerce.miscroservice.operate.rpc.dstb;

import com.e_commerce.miscroservice.commons.entity.distribution.EarningsStrategyVo;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/10/17 11:54
 * @Copyright 玖远网络
 */
@FeignClient(value = "DISTRIBUTION", path = "dstb/rpc/earningStrategy")
public interface DstbEarningsRpcService{

    /**
     * 更新分销收益策略
     *
     * @param strategyList strategyList
     * @return java.lang.String
     * @author Charlie
     * @date 2018/10/17 13:44
     */
    @RequestMapping("earningsRatio/upd")
    String earningsRatioUpd(@RequestBody List<EarningsStrategyVo> strategyList);


    /**
     * 角色的所有分销收益策略
     *
     * @return java.lang.String
     * @author Charlie
     * @date 2018/10/17 13:44
     */
    @GetMapping("earningsRatio/allStrategy")
    String dstbEarningsRatioAllStrategy();


}
