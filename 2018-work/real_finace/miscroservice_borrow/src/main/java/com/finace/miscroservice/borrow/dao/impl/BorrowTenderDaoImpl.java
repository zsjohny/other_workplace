package com.finace.miscroservice.borrow.dao.impl;

import com.finace.miscroservice.borrow.dao.BorrowTenderDao;
import com.finace.miscroservice.borrow.mapper.BorrowTenderMapper;
import com.finace.miscroservice.commons.entity.BorrowTender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *汇付投资记录dao层实现类
 */
@Component
public class BorrowTenderDaoImpl implements BorrowTenderDao {

    @Autowired
    private BorrowTenderMapper borrowTenderMapper;

    @Override
    public BorrowTender getUserTotalPast(String userId) {
        return borrowTenderMapper.getUserTotalPast(userId);
    }

	@Override
	public int getTenderNowDay() {
		return borrowTenderMapper.getTenderNowDay();
	}

	@Override
	public double getTenderNowDaySum() {
		return borrowTenderMapper.getTenderNowDaySum();
	}

	@Override
	public int getUserNowDayNum() {
		return borrowTenderMapper.getUserNowDayNum();
	}

	@Override
	public int tenderFirstNum() {
		return borrowTenderMapper.tenderFirstNum();
	}

	@Override
	public int getTenderRepayNowDay() {
		return borrowTenderMapper.getTenderRepayNowDay();
	}

	@Override
	public double getTendeRepayrNowDaySum() {
		return borrowTenderMapper.getTendeRepayrNowDaySum();
	}
	/**
	 * 查询已完成回款的投标累计本金和累计收益
	 */
	public BorrowTender getRepayInfoOfToday() {
		return borrowTenderMapper.getRepayInfoOfToday();
	}

	@Override
	public int getTenderofNewuser() {
		return borrowTenderMapper.getTenderofNewuser();
	}

	@Override
	public double getAllCollectSum() {
		return borrowTenderMapper.getAllCollectSum();
	}

	@Override
	public double getAllCollectFidSum() {
		return borrowTenderMapper.getAllCollectFidSum();
	}

	@Override
	public int getTenderNum() {
		return borrowTenderMapper.getTenderNum();
	}

	@Override
	public Double getAllTenderByUserId(Integer userId) {
		return borrowTenderMapper.getAllTenderByUserId(userId);
	}
}
