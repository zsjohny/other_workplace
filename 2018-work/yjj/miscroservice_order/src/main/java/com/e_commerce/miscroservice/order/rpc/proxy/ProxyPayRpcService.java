package com.e_commerce.miscroservice.order.rpc.proxy;

import com.e_commerce.miscroservice.commons.entity.proxy.PreOrderResult;
import com.e_commerce.miscroservice.order.rpc.impl.ProxyPayRpcServiceImpl;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.SortedMap;

/**
 * 调用微信支付
 */
@FeignClient(value = "PAYMENT", fallback = ProxyPayRpcServiceImpl.class)
public interface ProxyPayRpcService {


    @RequestMapping(value = "pay/wx/createPreOrder")
    PreOrderResult createPreOrder(@RequestParam(value = "amount")BigDecimal amount,
                                         @RequestParam(value = "title")String title,
                                         @RequestParam(value = "out_trade_no")String out_trade_no,
                                         @RequestParam(value = "attach") String attach,
                                         @RequestParam(value = "ip") String ip,
                                         @RequestParam(value = "openid") String openid);

}
