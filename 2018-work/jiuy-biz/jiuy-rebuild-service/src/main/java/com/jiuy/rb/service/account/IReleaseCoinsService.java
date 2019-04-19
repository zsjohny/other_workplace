package com.jiuy.rb.service.account;

import com.jiuy.base.model.UserSession;
import com.jiuy.rb.model.account.CoinsCashOut;
import com.jiuy.rb.model.account.CoinsDetail;

import java.util.Map;

/**
 * 释放玖币的接口
 *
 * @author Aison
 * @version V1.0
 * @date 2018/7/18 14:05
 * @Copyright 玖远网络
 */
public interface IReleaseCoinsService {

    /**
     * 将购买玖币从待入账到已入账
     *
     * @param coinsDetail coinsDetail
     * @author Aison
     * @date 2018/7/18 14:12
     *
     */
    void releaseOrder(CoinsDetail coinsDetail);

    Map<String,String> doCashOut(CoinsCashOut cashOut,  UserSession userSession, Map<String,String> rsMap);
}
