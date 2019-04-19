package com.store.service;

import com.jiuyuan.entity.Product;
import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.entity.visit.UserVisitHistory;
import com.store.dao.mapper.UserVisitMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class VisitService {

    @Autowired
    private UserVisitMapper visitMapper;

    public void addVisitHistory(long userId, int guideFlag, Long[] ids, int type) {
        if (ids.length == 0) return;

        long time = System.currentTimeMillis();
        //TODO:废弃
//        visitMapper.addVisitHistory(userId, guideFlag, ids, time, type);
    }

    /**
     * 获取用户的浏览记录，按照时间倒序排序
     *
     * @param userId
     * @param pageQuery
     * @return
     */
    public List<UserVisitHistory> getVisitList(Long userId, int guideFlag, PageQuery pageQuery) {

//		return visitMapper.getUserVisitList(userId, guideFlag, pageQuery);
        return new ArrayList<>();
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

    public int getVisitListCount(Long userId, int guideFlag) {

        return visitMapper.getUserVisitListCount(userId, guideFlag);
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
        // TODO Auto-generated method stub
//		return visitMapper.getVisits();
        return new HashMap<>();
    }

}
