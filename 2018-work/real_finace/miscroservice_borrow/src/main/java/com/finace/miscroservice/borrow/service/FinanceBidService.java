package com.finace.miscroservice.borrow.service;

import com.finace.miscroservice.borrow.po.FinanceBidPO;
import com.finace.miscroservice.commons.entity.FinanceMoney;
import com.finace.miscroservice.commons.entity.InvestRecords;
import com.finace.miscroservice.commons.utils.Response;

import java.util.List;
import java.util.Map;

public interface FinanceBidService {

    /**
     * 根据用户id获取用户富有投资总数
     * @param userId
     * @return
     */
    public FinanceMoney getFinanceMoneyInfo(String userId);


    /**
     * 用户待回款笔数
     * @param userId
     * @return
     */
    public Integer getUserFidCount(String userId);

    /**
     * 获取用户汇付和富有投资记录
     * @param map
     * @return
     */
    public List<InvestRecords> getUserInvestRecords(Map<String, Object> map, int pageNum);

    /**
     *获取用户本月还款情况
     * @param userId
     * @param tmonth
     * @return
     */
    public FinanceMoney getFinanceMonthInfo(String userId, String tmonth);

    /**
     * 获取用户每天的回款信息
     * @param userId
     * @param tday
     * @return
     */
    public Map<String, Object> getReturnCalendar(String userId, String tday, String tmonth);

    /**
     * 获取用户每月回款日期
     * @param userId
     * @param userId
     * @return
     */
    public List<String> getDayByMonth(String userId);

    /**
     * 根据id获取投资记录
     * @param fbid
     * @return
     */
    public FinanceBidPO getFidById(int fbid);

    /**
     * 获取投资记录
     * @param id
     * @return
     */
    public Map<String, Object> getInvestmentRecordByBorrowId(int id,int page);

    /**
     * 获取用户投资总额
     * @param userId
     * @return
     */
    public double getAllFinaceByUserId(int userId);

    /**
     * 根据订单号获取订单信息
     * @param orderId
     * @return
     */
    public FinanceBidPO getFidByOrderId( String orderId);

    /**
     *修改投资记录的支付状态
     * @param map
     * @return
     */
    public int updatePayFinanceBidByOrderId(Map<String, Object> map);


    /**
     * 获取用户第一次投资金额
     * @param userId
     * @return
     */
    public Double getUserFirstBuyAmt(int userId);

    /**
     * 订单失败处理
     * @param orderId
     */
    public String closeOrder(String orderId, String goToFuiouHtml, String version);

    /**
     * 显示合同信息
     * @return
     */
    public Map<String, Object> showHt(String fbid);

    /**
     * 累计成交数据
     * @return
     */
    public Map<String, Object> getCumulativeData();

    /**
     * 修改云合同的合同id
     * @return
     */
    public void updateYunContractIdById(String contractId, String orderId);


    /**
     * 根据标的id获取投资记录最后一条投资记录
     *
     * @param borrowId
     * @param timeout
     * @return
     */
    public String getFinanceBidByDesc(int borrowId, String timeout);


    /**
     * 根据订单号获取订单信息 并且是初始订单
     * @param orderId
     * @return
     */
    public FinanceBidPO getFidByOrderIdAndFail(String orderId);


    /**
     * 购买判断是否可以支付
     * @param orderId
     * @return
     */
    public Response canBePay(String userId, String orderId);

    /**
     * 购买判断是否可以支付
     * @param borrowId
     * @return
     */
    public Response agreeCanBePay(String userId,String borrowId, Double buyAmt);


    /**
     * 恢复可购买金额
     * @param orderId
     * @return
     */
    public Response recoveryAmt(String userId, String orderId);


}
