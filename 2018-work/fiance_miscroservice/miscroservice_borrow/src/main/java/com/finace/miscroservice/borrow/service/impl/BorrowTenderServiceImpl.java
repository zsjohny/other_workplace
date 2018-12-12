package com.finace.miscroservice.borrow.service.impl;

import com.finace.miscroservice.borrow.dao.BorrowTenderDao;
import com.finace.miscroservice.borrow.service.BorrowTenderService;
import com.finace.miscroservice.commons.entity.BorrowTender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 汇付投资记录service实现层
 */
@Service
public class BorrowTenderServiceImpl implements BorrowTenderService {

	@Autowired
	private BorrowTenderDao borrowTenderDao;

	@Override
	public BorrowTender getUserTotalPast(String userId) {
		return borrowTenderDao.getUserTotalPast(userId);
	}

	@Autowired
	public int getTenderNowDay() {
		return borrowTenderDao.getTenderNowDay();
	}

	@Autowired
	public double getTenderNowDaySum() {
		return borrowTenderDao.getTenderNowDaySum();
	}

	@Override
	public int getUserNowDayNum() {
		return borrowTenderDao.getUserNowDayNum();
	}

	@Override
	public int getTenderRepayNowDay() {
		return borrowTenderDao.getTenderRepayNowDay();
	}

	@Override
	public double getTendeRepayrNowDaySum() {
		return borrowTenderDao.getTendeRepayrNowDaySum();
	}

	@Override
	public double getAllCollectSum() {
		return borrowTenderDao.getAllCollectSum();
	}

	@Override
	public double getAllCollectFidSum() {
		return borrowTenderDao.getAllCollectFidSum();
	}

	@Override
	public int getTenderofNewuser() {
		return borrowTenderDao.getTenderofNewuser();
	}

	@Override
	public int getTenderNum() {
		return borrowTenderDao.getTenderNum();
	}

	@Override
	public int tenderFirstNum() {
		return borrowTenderDao.tenderFirstNum();
	}
	@Override
	public BorrowTender getRepayInfoOfToday(){
		return borrowTenderDao.getRepayInfoOfToday() ;}

}
