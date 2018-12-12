package com.finace.miscroservice.borrow.dao.impl;

import com.finace.miscroservice.borrow.controller.AppEyeSkyController;
import com.finace.miscroservice.borrow.dao.FinanceBidDao;
import com.finace.miscroservice.borrow.entity.response.EyeDataResponse;
import com.finace.miscroservice.borrow.mapper.FinanceBidMapper;
import com.finace.miscroservice.borrow.po.FinanceBidPO;
import com.finace.miscroservice.commons.entity.BasePage;
import com.finace.miscroservice.commons.entity.FinanceMoney;
import com.finace.miscroservice.commons.entity.InvestRecords;
import com.finace.miscroservice.commons.log.Log;
import com.finace.miscroservice.commons.utils.tools.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Component
public class FinanceBidDaoImpl implements FinanceBidDao {

    private Log logger = Log.getInstance(FinanceBidDaoImpl.class);

    @Autowired
    private FinanceBidMapper financeBidMapper;

    @Override
    public FinanceMoney getFinanceMoneyInfo(String userId) {

        return financeBidMapper.getFinanceMoneyInfo(userId);
    }

    @Override
    public Integer getUserFidCount(String userId) {
        return financeBidMapper.getUserFidCount(userId);
    }

    @Override
    public List<FinanceBidPO> getRealFidByBorrowId(String borrowId) {
        return financeBidMapper.getRealFidByBorrowId(borrowId);
    }

    @Override
    public List<InvestRecords> getUserInvestRecords(Map<String, Object> map, int pageNum) {
        BasePage basePage = new BasePage();
        basePage.setPageNum(pageNum);
        List<InvestRecords> list = financeBidMapper.getUserInvestRecords(map);
        basePage.getTotal(list);
        return list;
    }

    @Override
    public FinanceMoney getFinanceMonthInfo(String userId, String tmonth) {
        return financeBidMapper.getFinanceMonthInfo(userId, tmonth);
    }

    @Override
    public List<FinanceMoney> getFinanceDayInfo(String userId, String tday) {
        return financeBidMapper.getFinanceDayInfo(userId, tday);
    }

    @Override
    public List<String> getDayByMonth(String userId) {
        return financeBidMapper.getDayByMonth(userId);
    }

    @Override
    public FinanceBidPO getFidById(int fbid) {
        return financeBidMapper.getFidById(fbid);
    }

    @Override
    public List<FinanceBidPO> getInvestmentRecordByBorrowId(int id) {
        return financeBidMapper.getInvestmentRecordByBorrowId(id);
    }

    @Override
    public double getAllFinaceByUserId(int userId) {
        return financeBidMapper.getAllFinaceByUserId(userId);
    }

    @Override
    public int addFinanceBid(FinanceBidPO financeBidPO) {
        return financeBidMapper.addFinanceBid(financeBidPO);
    }

    @Override
    public FinanceBidPO getFidByOrderId(String orderId) {
        return financeBidMapper.getFidByOrderId(orderId);
    }

    @Override
    public FinanceBidPO getFidByNoOrderId(String orderId) {
        return financeBidMapper.getFidByNoOrderId(orderId);
    }

    @Override
    public void updateFinanceBid(FinanceBidPO financeBidPO) {
        financeBidMapper.updateFinanceBid(financeBidPO);
    }

    @Override
    public int updatePayFinanceBidByOrderId(Map<String, Object> map) {
        return financeBidMapper.updatePayFinanceBidByOrderId(map);
    }

    @Override
    public Double getUserFirstBuyAmt(int userId) {
        return financeBidMapper.getUserFirstBuyAmt(userId);
    }

    @Override
    public FinanceBidPO getCumulativeData() {
        return financeBidMapper.getCumulativeData();
    }

    @Override
    public int updateYunContractIdById(Map<String, Object> map) {
        return financeBidMapper.updateYunContractIdById(map);
    }

    @Override
    public FinanceBidPO getFinanceBidByDesc(int borrowId) {
        return financeBidMapper.getFinanceBidByDesc(borrowId);
    }

    @Override
    public FinanceBidPO getFidByOrderIdAndFail(String orderId) {

        return financeBidMapper.getFidByOrderIdAndFail(orderId);
    }

    @Override
    public List<EyeDataResponse> getEyeInfoByData(Map<String, Object> map) {
        List<EyeDataResponse> list = financeBidMapper.getEyeInfoByData(map);
        Iterator<EyeDataResponse> it = list.iterator();
        try {
            while (it.hasNext()) {
                EyeDataResponse x = it.next();
                //移除不在查询时间范围内的
                if (map.get("startTime") != null && map.get("endTime") != null) {
                    Long sTime = Long.valueOf(DateUtils.dateToStamp(map.get("startTime").toString())) / 1000;
                    Long eTime = Long.valueOf(DateUtils.dateToStamp(map.get("endTime").toString())) / 1000;
                    Long time = x.getTrade_time();
                    if (time < sTime || time > eTime) {
                        it.remove();
                    }
                }
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("天眼数据查询异常：{}", e);
        }
        return null;
    }

    @Override
    public Integer findInvitationDistanct(int user_id, String starttime, String endtime) {
        return financeBidMapper.findInvitationDistanct(user_id, starttime, endtime);
    }

    @Override
    public String findAmountMoneyByUserId(int user_id, String starttime, String endtime) {
        return financeBidMapper.findAmountMoneyByUserId(user_id, starttime, endtime);
    }

    @Override
    public int updateFinance(FinanceBidPO financeBidPO) {
        return financeBidMapper.updateFinance(financeBidPO);
    }
}














