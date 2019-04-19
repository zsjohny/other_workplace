package com.yujj.business.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.entity.visit.UserVisitHistory;
import com.yujj.dao.mapper.UserVisitMapper;
import com.yujj.entity.product.Product;

@Service
public class VisitService {

    @Autowired
    private UserVisitMapper visitMapper;

    public void addVisitHistory(long userId, Long[] ids) {
        long time = System.currentTimeMillis();
        visitMapper.addVisitHistory(userId, ids, time);
    }

	/**
	 * 获取用户的浏览记录，按照时间倒序排序
	 * 
	 * @param userId
	 * @param pageQuery
	 * @return
	 */
	public List<UserVisitHistory> getVisitList(Long userId, PageQuery pageQuery) {

		return visitMapper.getUserVisitList(userId, pageQuery);
	}

	/**
	 * 清空浏览记录
	 * 
	 * @param userId
	 * @return
	 */
	public int deleteVisitList(Long userId) {
		return visitMapper.deleteAll(userId);
	}

	public int getVisitListCount(Long userId) {

		return visitMapper.getUserVisitListCount(userId);
	}

	public List<Product> getBuyGuessProduct(long userId, PageQuery pageQuery, int count) {
		long currentTime = System.currentTimeMillis();
		return visitMapper.getBuyGuessProduct(userId, pageQuery, count, currentTime);
	}

	public List<Product> getSeeAgainProduct(long userId, PageQuery pageQuery, int count) {
		long currentTime = System.currentTimeMillis();
		return visitMapper.getSeeAgainProduct(userId, pageQuery, count, currentTime);
	}

	/**
	 * @return
	 */
	public Map<Long, UserVisitHistory> getVisits() {
		return visitMapper.getVisits();
	}

}
