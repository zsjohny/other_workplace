package com.e_commerce.miscroservice.activity.dao;

import com.e_commerce.miscroservice.activity.entity.WxaShareLog;

/**
 * 分享记录表
 * <p> 
 * 请不要再映射对象内添加自定义属性 如果需要添加属性请继承后在子类中添加属性
 * 
 * @author Aison
 * @version V1.0  
 * @date 2018年07月05日 下午 05:37:31
 */
public interface WxaShareLogDao{

    /**
     * 插入一个对象
     *
     * @param wxaShareLog wxaShareLog
     * @return int
     * @author Charlie
     * @date 2018/11/21 19:36
     */
    int insert(WxaShareLog wxaShareLog);


    /**
     * 今日分享商品的数量
     *
     * @param userId userId
     * @return java.lang.Long
     * @author Charlie
     * @date 2018/11/22 16:46
     */
    Long todayhasEarningsShareCount(Long userId);



}