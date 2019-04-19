package com.jiuy.core.service.share;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuy.core.dao.ShareServiceDao;
import com.jiuyuan.entity.query.PageQuery;

@Service
public class ShareServiceImpl implements ShareService {
	@Autowired
	private ShareServiceDao shareServiceDao;

	@Override
	public Map<String, Object> getDayReportData(long dayTime, long endTime) {
		return shareServiceDao.getDayReportData(dayTime, endTime);
	}

	@Override
	public Map<String, Object> getTotayData(long startTime, long endTime) {
		return shareServiceDao.getTotayData(startTime, endTime);
	}

	@Override
	public int getRecordCount(long yjjNumber, long productId, int type, long startTime, long endTime, long phone,
			String clothesNumber, long startJiuBi, long endJiuBi) {

		return shareServiceDao.getRecordCount(yjjNumber, productId, type, startTime, endTime, phone, clothesNumber,
				startJiuBi, endJiuBi);
	}

	@Override
	public List<Map<String, String>> searchShareRecord(PageQuery pageQuery, long yjjNumber, long productId, int type,
			long startTime, long endTime, long phone, String clothesNumber, long startJiuBi, long endJiuBi) {

		return shareServiceDao.searchShareRecord(pageQuery, yjjNumber, productId, type, startTime, endTime, phone,
				clothesNumber, startJiuBi, endJiuBi);
	}

	@Override
	public int getClickRecordCount(long shareYjjNumber, long yjjNumber, int type, long startTime, long endTime,
			long phone,long sharePhone, long shareId) {
		return shareServiceDao.getClickRecordCount(shareYjjNumber, yjjNumber, type, startTime, endTime, phone,sharePhone, shareId);
	}

	@Override
	public List<Map<String, String>> searchClickShareRecord(PageQuery pageQuery, long shareYjjNumber, long yjjNumber,
			int type, long startTime, long endTime, long phone,long sharePhone, long shareId) {
		return shareServiceDao.searchClickShareRecord(pageQuery, shareYjjNumber, yjjNumber, type, startTime, endTime,
				phone,sharePhone, shareId);
	}

}
