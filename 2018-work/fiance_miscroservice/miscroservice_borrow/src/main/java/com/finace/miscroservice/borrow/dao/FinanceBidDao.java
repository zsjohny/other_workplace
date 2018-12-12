package com.finace.miscroservice.borrow.dao;

import com.finace.miscroservice.borrow.entity.response.EyeDataResponse;
import com.finace.miscroservice.borrow.po.FinanceBidPO;
import com.finace.miscroservice.commons.entity.FinanceMoney;
import com.finace.miscroservice.commons.entity.InvestRecords;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface FinanceBidDao {

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
     * 根据标的id查询标的的有效投资记录
     * @param borrowId
     * @return
     */
    public List<FinanceBidPO> getRealFidByBorrowId(String borrowId);

    /**
     *获取用户汇付和富有投资记录
     * @param map
     * @return
     */
    public List<InvestRecords> getUserInvestRecords(Map<String, Object> map, int pageNum);

    /**
     * 获取用户本月还款情况
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
    public List<FinanceMoney> getFinanceDayInfo(String userId, String tday);

    /**
     * 获取用户每月回款日期
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
    public List<FinanceBidPO> getInvestmentRecordByBorrowId(int id);

    /**
     * 获取用户投资总额
     * @param userId
     * @return
     */
    public double getAllFinaceByUserId(int userId);

    /**
     * 新增富有投资记录
     * @param financeBidPO
     */
    public int  addFinanceBid(FinanceBidPO financeBidPO);

    /**
     * 根据订单号获取订单信息
     * @param orderId
     * @return
     */
    public FinanceBidPO getFidByOrderId( String orderId);

    /**
     * 根据订单号获取未完成投资记录
     * @param orderId
     * @return
     */
    FinanceBidPO getFidByNoOrderId(String orderId);

    /**
     *
     * @param financeBidPO
     */
    public void updateFinanceBid(FinanceBidPO financeBidPO);

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
     * 累计成交数据
     * @return
     */
    public FinanceBidPO getCumulativeData();

    /**
     * 修改云合同的合同id
     * @return
     */
    public int updateYunContractIdById(Map<String, Object> map);

    /**
     * 根据标的id获取投资记录最后一条投资记录
     * @return
     */
    public FinanceBidPO getFinanceBidByDesc(int borrowId);


    /**
     * 根据订单号获取订单信息 并且是初始订单
     * @param orderId
     * @return
     */
    public FinanceBidPO getFidByOrderIdAndFail(String orderId);

    /**
     * 天眼查询需要的信息
     * @param map
     * @return
     */
    public List<EyeDataResponse> getEyeInfoByData(Map<String, Object> map);


    /**
     * 该用户投资金额>10000的次数
     * @param user_id
     * @param starttime
     * @param endtime
     * @return
     */
    Integer findInvitationDistanct(int user_id, String starttime, String endtime);

    /**
     * 根据用户id查找 累计投资金额
     * @param user_id
     * @return
     */
    String findAmountMoneyByUserId(int user_id,String starttime,String endtime);

    /**
     * 修改标的记录
     * @param financeBidPO
     * @return
     */
    int updateFinance(FinanceBidPO financeBidPO);
}














