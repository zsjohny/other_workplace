package com.e_commerce.miscroservice.distribution.service;

import com.alibaba.fastjson.JSONObject;
import com.e_commerce.miscroservice.commons.entity.application.distributionSystem.ShopMemberAccount;
import com.e_commerce.miscroservice.commons.entity.application.distributionSystem.ShopMemberAccountCashOutIn;
import com.e_commerce.miscroservice.commons.entity.distribution.ShopMemAcctCashOutInQuery;
import com.e_commerce.miscroservice.commons.entity.order.ShopMemberOrderVo;
import com.e_commerce.miscroservice.commons.helper.util.service.Response;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Map;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/10/10 16:34
 * @Copyright 玖远网络
 */
public interface ShopMemberAccountService{


    /**
     * 个人中心收益统计
     * @param userId
     * @return
     */
    JSONObject getUserIncomeStatistics(Long userId);


    /**
     * 分销成功
     *
     * @param vo 订单信息
     * @return void
     * @author Charlie
     * @date 2018/10/10 20:45
     */
    void dstbSuccess(ShopMemberOrderVo vo);


    /**
     * 团队收益待结算到可用
     *
     * @author Charlie
     * @date 2018/10/10 21:44
     */
    Map<String, Integer> teamInWait2Alive();


    @Transactional(rollbackFor = Exception.class) /*synchronized */void doWaitTeamIn2Alive(ShopMemberAccountCashOutIn cashOutIn);

    /**
     * 佣金账单统计
     * @param userId
     * @return
     */
    Response countBill(Long userId);

    /**
     * 现金账单明细
     *
     * @param choose
     * @param type
     * @param inOutType
     * @param status
     * @param userId
     * @param page
     * @return
     */
    Response findBillDetails(Integer choose, Integer type, Integer inOutType, Integer status, Long userId, Integer page);

    /**
     * 金币账单信息
     * @param userId
     * @return
     */
    JSONObject countGoldBill(Long userId);



    /**
     * 记录下单后的分销返利
     *
     * @param vo 订单信息
     * @author Charlie
     * @date 2018/10/11 17:00
     */
    void addDstbFromOrder(ShopMemberOrderVo vo);



    /**
     * 提现
     *
     * @param userId 用户id
     * @param operMoney 提现金额
     * @return com.e_commerce.miscroservice.commons.helper.util.service.Response
     * @author Charlie
     * @date 2018/10/13 16:41
     */
    Map<String, Object> cashOut(Long userId, BigDecimal operMoney, HttpServletRequest request);



    /**
     * 初始化一个提现的流水记录
     *
     * @param userId 用户id
     * @param operMoney 操作的金额
     * @return 流水id
     * @author Charlie
     * @date 2018/10/13 16:40
     */
    Long preCashOut(Long userId, BigDecimal operMoney);

    void doCashOutCallback(ShopMemberAccountCashOutIn cashOutIn, Map<String, String> wxResMap);
    /**
     * 团队订单列表信息
     * @param userId
     * @param page
     * @param orderNo
     * @return
     */
    Response teamOrderList(Long userId, Integer page, String orderNo);
    /**
     * 团队订单信息
     * @param userId
     * @return
     */
    Response teamOrderCount(Long userId);
    /**
     * 团队订单信息详情
     * @param userId
     * @param orderNo
     * @return
     */
    Response teamOrderDetail(Long userId, String orderNo);

    /**
     * 根据用户id查询
     * @param userId
     * @return
     */
    ShopMemberAccount findByUser(Long userId);

    /**
     * 根据用户id更新
     * @param shopMemberAccount
     * @param userId
     */
    void updateShopMemberAccountByUserId(ShopMemberAccount shopMemberAccount, Long userId);

    /**
     * 保存
     * @param userId
     * @param coin
     */
    void saveShopMemberAccount(Long userId,BigDecimal coin);

    /**
     * 账户金额-详情-收支详情
     * @param id id
     * @param userId
     * @return
     */
    Response findOrderAccountDetails(Long id, Long userId);


    /**
     * 提现申请审核
     * @param cashOutId 流水id
     * @param isPass 1通过,0不通过
     * @param ip ip
     */
    void cashOutAudit(Long cashOutId, Integer isPass, String ip);

    /**
     * 提现
     *
     * @param ip ip
     * @param cashOutIn cashOutIn
     * @param userId userId
     * @param operMoney operMoney
     * @return java.util.Map
     * @author Charlie
     * @date 2018/11/19 9:07
     */
    Map<String, String> startCashOut(String ip, ShopMemberAccountCashOutIn cashOutIn, Long userId, BigDecimal operMoney);


    /**
     * 增加收益流水,根据流水类型
     *
     * @param addInfo addInfo
     * @return java.util.Map
     * @author Charlie
     * @date 2018/11/22 16:55
     */
    Map<String, Object> addCashOutInByType(ShopMemAcctCashOutInQuery addInfo);



}
