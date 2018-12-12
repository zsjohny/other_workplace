package com.finace.miscroservice.borrow.dao.impl;

import com.finace.miscroservice.borrow.dao.BorrowDao;
import com.finace.miscroservice.borrow.entity.response.DataCollectionResponse;
import com.finace.miscroservice.borrow.entity.response.UserProportion;
import com.finace.miscroservice.borrow.mapper.BorrowMapper;
import com.finace.miscroservice.borrow.po.BorrowPO;
import com.finace.miscroservice.commons.entity.BasePage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 标的 Dao层实现类
 */
@Component
public class BorrowDaoImpl implements BorrowDao {

    @Autowired
    private BorrowMapper borrowMapper;


    @Override
    public BorrowPO getBorrowByUserId(int userId) {

        return borrowMapper.getBorrowByUserId(userId);
    }


    @Override
    public BorrowPO getBorrowById(int id) {
        return borrowMapper.getBorrowById(id);
    }

    @Override
    public BorrowPO getBorrowByStatusId(String borrowId, String status) {
        Map<String, String> map = new HashMap<>();
        map.put("borrowId", borrowId);
        map.put("status", status);

        return borrowMapper.getBorrowByStatusId(map);
    }

    @Override
    public BorrowPO getBorrowUserById(int id) {
        return borrowMapper.getBorrowUserById(id);
    }

    @Override
    public void updateBorrowStatusById(Map<String, String> map) {
        borrowMapper.updateBorrowStatusById(map);
    }


    @Override
    public List<BorrowPO> getShowFinaceList(Map<String, Object> map, int page) {
        BasePage.setPage(page);
        return borrowMapper.getShowFinaceList(map);
    }

    @Override
    public BorrowPO getShowFinaceById(int id) {
        return borrowMapper.getShowFinaceById(id);
    }

    @Override
    public void updateBorrow(BorrowPO borrowPO) {
        borrowMapper.updateBorrow(borrowPO);
    }

    @Override
    public void updateAllBorrow(BorrowPO borrowPO) {
        borrowMapper.updateAllBorrow(borrowPO);
    }

    @Override
    public List<BorrowPO> getBorrowByIndex(String type,String tab) {
        return borrowMapper.getBorrowByIndex(type,tab);
    }


    @Override
    public List<BorrowPO> getBorrowPOBySellOut(String borrowGroup) {
        return borrowMapper.getBorrowPOBySellOut(borrowGroup);
    }


    @Override
    public BorrowPO getBorrowPOByAutoAdd(String borrowGroup) {
        return borrowMapper.getBorrowPOByAutoAdd(borrowGroup);
    }


    @Override
    public int updateAutoCheckBorrow(BorrowPO borrowPO) {
        return borrowMapper.updateAutoCheckBorrow(borrowPO);
    }

    @Override
    public DataCollectionResponse getDatas() {
        return borrowMapper.getDatas();
    }

    @Override
    public Integer getLjcjbs() {
        return borrowMapper.getLjcjbs();
    }

    @Override
    public Integer getLjcjrzs() {
        return borrowMapper.getLjcjrzs();
    }

    @Override
    public Double getBndcjje(String date) {
        return borrowMapper.getBndcjje(date);
    }

    @Override
    public Double dhje() {
        return borrowMapper.dhje();
    }

    @Override
    public Double getZddhcjyezb() {
        return borrowMapper.getZddhcjyezb();
    }

    @Override
    public Double getZdshtzcjzb() {
        return borrowMapper.getZdshtzcjzb();
    }

    @Override
    public UserProportion userProportion() {
        return borrowMapper.userProportion();
    }

    @Override
    public Integer dhjebs() {
        return borrowMapper.dhjebs();
    }

    @Override
    public Integer ljjkrzs() {
        return borrowMapper.ljjkrzs();
    }

    @Override
    public Integer dqjkrsl() {
        return borrowMapper.dqjkrsl();
    }

    @Override
    public Integer dqcjrsl() {
        return borrowMapper.dqcjrsl();
    }

    @Override
    public Double getUserAmtMoneyInTime(Integer userId, String starttime, String endtime) {
        return borrowMapper.getUserAmtMoneyInTime(userId, starttime, endtime);
    }

    @Override
    public List<BorrowPO> getBorrowNinetyByIndex() {
        return borrowMapper.getBorrowNinetyByIndex();
    }

    @Override
    public BorrowPO getBorrowAgreeByAutoAdd(String borrowGroup) {
            return borrowMapper.getBorrowAgreeByAutoAdd(borrowGroup);
    }

    @Override
    public List<BorrowPO> getBorrowByTab(String tab) {
        return borrowMapper.getBorrowByTab(tab);
    }

    @Override
    public BorrowPO getBorrowByAutoAdd(String borrowGroup) {
        return borrowMapper.getBorrowByAutoAdd(borrowGroup);
    }
}


