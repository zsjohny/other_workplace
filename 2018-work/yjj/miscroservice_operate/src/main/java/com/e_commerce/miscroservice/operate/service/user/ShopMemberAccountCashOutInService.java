package com.e_commerce.miscroservice.operate.service.user;

import com.e_commerce.miscroservice.commons.entity.distribution.OperUserDstbRequest;
import com.e_commerce.miscroservice.commons.entity.distribution.ShopMemAcctCashOutInQuery;
import com.github.pagehelper.PageInfo;

import java.util.Map;


/**
 * @author Charlie
 * @version V1.0
 * @date 2018/10/10 20:01
 * @Copyright 玖远网络
 */
public interface ShopMemberAccountCashOutInService{


    /**
     * 现金收支明细
     *
     * @param query query
     * @return java.util.List<java.util.Map   <   java.lang.String   ,   java.lang.Object>>
     * @author Charlie
     * @date 2018/10/19 9:21
     */
    Map<String, Object> listDetail(OperUserDstbRequest query);

    /**
     * 提现详情
     *
     * @param cashOutInId 流水id
     * @return java.lang.Object
     * @author Charlie
     * @date 2018/10/23 11:00
     */
    Map<String, Object> cashOutDetail(Long cashOutInId);


    /**
     * 小程序收益业绩统计
     *
     * @param query query
     * @return java.util.Map<java.lang.String   ,   java.lang.Object>
     * @author Charlie
     * @date 2018/11/12 17:42
     */
    Map<String, Object> performanceListAll(ShopMemAcctCashOutInQuery query);



    /**
     * 用户分销业绩订单返佣明细
     *
     * @param query query
     * @return java.util.Map
     * @author Charlie
     * @date 2018/11/13 11:08
     */
    Map<String, Object> performanceListCommission(ShopMemAcctCashOutInQuery query);




    /**
     * 业绩统计,管理收益明细查询
     *
     * @param query query
     * @return java.util.Map
     * @author Charlie
     * @date 2018/11/13 14:44
     */
    Map<String, Object> performanceListManager(ShopMemAcctCashOutInQuery query);

    /**
     * 团队订单返佣明细
     *
     * @param query query
     * @return java.util.Map
     * @author Charlie
     * @date 2018/11/13 19:21
     */
    Map<String, Object> performanceListManagerTeamEarnings(ShopMemAcctCashOutInQuery query);


    Map<String, Object> listCashOut(ShopMemAcctCashOutInQuery query);
}
