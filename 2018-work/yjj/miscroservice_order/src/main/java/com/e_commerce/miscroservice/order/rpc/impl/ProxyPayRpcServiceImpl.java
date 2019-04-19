package com.e_commerce.miscroservice.order.rpc.impl;

import com.e_commerce.miscroservice.commons.entity.proxy.PreOrderResult;
import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.e_commerce.miscroservice.order.rpc.proxy.ProxyPayRpcService;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.SortedMap;


@Component
public class ProxyPayRpcServiceImpl implements ProxyPayRpcService {

    private Log logger = Log.getInstance(ProxyPayRpcServiceImpl.class);

    @Override
    public PreOrderResult createPreOrder(BigDecimal amount, String title, String out_trade_no, String attach,String ip, String openid) {
        return null;
    }
}
