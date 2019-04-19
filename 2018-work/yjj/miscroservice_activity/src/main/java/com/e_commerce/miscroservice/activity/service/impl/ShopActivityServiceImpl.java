package com.e_commerce.miscroservice.activity.service.impl;

import com.e_commerce.miscroservice.activity.entity.SecondBuyActivity;
import com.e_commerce.miscroservice.activity.entity.TeamBuyActivity;
import com.e_commerce.miscroservice.activity.mapper.SecondBuyActivityMapper;
import com.e_commerce.miscroservice.activity.mapper.TeamBuyActivityMapper;
import com.e_commerce.miscroservice.activity.service.ShopActivityService;
import com.e_commerce.miscroservice.commons.entity.activity.ShopActivityQuery;
import com.e_commerce.miscroservice.commons.exception.ErrorHelper;
import com.e_commerce.miscroservice.commons.helper.log.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/11/27 10:02
 * @Copyright 玖远网络
 */
@Service
public class ShopActivityServiceImpl implements ShopActivityService{


    private Log logger = Log.getInstance (ShopActivityServiceImpl.class);

    @Autowired
    private TeamBuyActivityMapper teamBuyActivityMapper;
    @Autowired
    private SecondBuyActivityMapper secondBuyActivityMapper;


    /**
     * 最近的一个(正在进行或待开始)的活动
     * <p>
     *     type = -1 没有活动
     *     type = 1 团购
     *     type = 2 秒杀
     *
     *     data: 活动
     * </p>
     *
     * @param query query
     * @return java.lang.Object
     * @author Charlie
     * @date 2018/11/27 10:35
     */
    @Override
    public Map<String, Object> recentlyShopProductActivity(ShopActivityQuery query) {

        Map<String, Object> result = new HashMap<> (2);

        Integer type = query.getActivityType ();
        logger.info ("查询商品最近的一个活动 shopProduct={},activityId={},activityType={}", query.getShopProductId (), query.getId (), type);

        if (type == null) {


            //查询所有活动,
            TeamBuyActivity teamBuy = teamBuyActivityMapper.recentlyShopProductActivity (
                    query.getShopProductId (),
                    System.currentTimeMillis (),
                    query.getId ());

            SecondBuyActivity secondBuy = secondBuyActivityMapper.recentlyShopProductActivity (
                    query.getShopProductId (),
                    System.currentTimeMillis (),
                    query.getId ());

            if (teamBuy == null && secondBuy == null) {
                result.put ("type", - 1);
            }
            else if (secondBuy == null) {
                //团购
                result.put ("data", teamBuy);
                result.put ("type", 1);
            }
            else if (teamBuy == null) {
                //秒杀
                result.put ("data", secondBuy);
                result.put ("type", 2);
            }
            else {
                //都不是空,返回开始时间较早的
                int resType = teamBuy.getActivityStartTime () < secondBuy.getActivityStartTime () ? 1 : 2;
                result.put ("type", resType);
                result.put ("data", resType == 1 ? teamBuy : secondBuy);
            }

        }
        else if (type == 1) {


            //查询团购
            TeamBuyActivity teamBuy = teamBuyActivityMapper.recentlyShopProductActivity (
                    query.getShopProductId (),
                    System.currentTimeMillis (),
                    query.getId ());
            if (teamBuy != null) {
                result.put ("data", teamBuy);
                result.put ("type", 1);
            }
            else {
                result.put ("type", -1);
            }
        }
        else if (type == 2) {


            //查询秒杀
            SecondBuyActivity secondBuy = secondBuyActivityMapper.recentlyShopProductActivity (
                    query.getShopProductId (),
                    System.currentTimeMillis (),
                    query.getId ());
            if (secondBuy != null) {
                result.put ("data", secondBuy);
                result.put ("type", 2);
            }
            else {
                result.put ("type", -1);
            }
        }
        else {
            logger.error ("未知的查询类型");
            ErrorHelper.declare (Boolean.FALSE, "未知的查询类型");
        }

        return result;
    }
}
