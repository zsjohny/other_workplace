package com.finace.miscroservice.borrow.mapper;

import com.finace.miscroservice.borrow.entity.response.EyeDataResponse;
import com.finace.miscroservice.borrow.po.FinanceBidPO;
import com.finace.miscroservice.commons.entity.FinanceMoney;
import com.finace.miscroservice.commons.entity.InvestRecords;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface FinanceBidMapper {

    /**
     * 修改投资记录信息
     * @param financeBidPO
     */
    void updateFinanceBid(FinanceBidPO financeBidPO);

    /**
     * 根据订单号获取订单信息
     * @param orderId
     * @return
     */
    FinanceBidPO getFidByOrderId(@Param("orderId") String orderId);

    /**
     * 根据标的id查询标的的有效投资记录
     * @param borrowId
     * @return
     */
    List<FinanceBidPO> getRealFidByBorrowId(@Param("borrowId") String borrowId);

    /**
     * 根据订单号获取未完成投资记录
     * @param orderId
     * @return
     */
    FinanceBidPO getFidByNoOrderId(@Param("orderId") String orderId);


    /**
     * 新增富有投资记录
     * @param financeBidPO
     */
    int  addFinanceBid(FinanceBidPO financeBidPO);

    /**
     * 根据用户id获取用户富有投资总数
     *
     * @param userId
     * @return
     */
    FinanceMoney getFinanceMoneyInfo(@Param("userId") String userId);

    /**
     *用户待回款笔数
     * @param userId
     * @return
     */
    Integer getUserFidCount(@Param("userId") String userId);

    /**
     * 获取用户汇付和富有投资记录
     * @param map
     * @return
     */
    List<InvestRecords> getUserInvestRecords(Map<String, Object> map);

    /**
     *获取用户本月还款情况
     * @param userId
     * @param tmonth
     * @return
     */
    FinanceMoney getFinanceMonthInfo(@Param("userId") String userId, @Param("tmonth") String tmonth);

    /**
     * 获取用户每天的回款信息
     * @param userId
     * @param tday
     * @return
     */
    List<FinanceMoney> getFinanceDayInfo(@Param("userId") String userId, @Param("tday") String tday);

    /**
     * 获取用户每月回款日期
     * @param userId
     * @return
     */
    List<String> getDayByMonth(@Param("userId") String userId);

    /**
     * 根据id获取投资记录
     * @param fbid
     * @return
     */
    FinanceBidPO getFidById(@Param("fbid") int fbid);

    /**
     *
     * @param id
     * @return
     */
    List<FinanceBidPO> getInvestmentRecordByBorrowId(@Param("id") int id);

    /**
     * 获取用户投资总额
     * @param userId
     * @return
     */
    double getAllFinaceByUserId(@Param("userId") int userId);

    /**
     * 修改投资记录的支付状态
     * @return
     */
    int updatePayFinanceBidByOrderId(Map<String, Object> map);

    /**
     * 获取用户第一次投资金额
     * @param userId
     * @return
     */
    double getUserFirstBuyAmt(@Param("userId") int userId);


    /**
     * 累计成交数据
     * @return
     */
    FinanceBidPO getCumulativeData();

    /**
     * 修改云合同的合同id
     * @return
     */
    int updateYunContractIdById(Map<String, Object> map);

    /**
     * 根据标的id获取投资记录最后一条投资记录
     * @return
     */
    FinanceBidPO getFinanceBidByDesc(@Param("borrowId") int borrowId);


    /**
     * 根据订单号获取订单信息
     * @param orderId
     * @return
     */
    FinanceBidPO getFidByOrderIdAndFail(@Param("orderId") String orderId);

    /**
     * 天眼查询需要的信息
     * @param map
     * @return
     */
    List<EyeDataResponse> getEyeInfoByData(Map<String, Object> map);
    /**
     * 该用户投资金额>10000的次数
     * @param user_id
     * @param starttime
     * @param endtime
     * @return
     */
    Integer findInvitationDistanct(@Param("user_id")int user_id, @Param("starttime")String starttime, @Param("endtime")String endtime);

    /**
     * 根据用户id查找 累计投资金额
     * @param user_id
     * @return
     */
    String findAmountMoneyByUserId(@Param("user_id")int user_id,@Param("starttime")String starttime,@Param("endtime")String endtime);

    /**
     * 修改标的记录
     * @param financeBidPO
     * @return
     */
    int updateFinance(FinanceBidPO financeBidPO);
}
