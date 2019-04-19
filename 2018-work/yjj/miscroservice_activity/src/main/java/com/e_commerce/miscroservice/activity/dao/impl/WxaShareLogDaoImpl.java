package com.e_commerce.miscroservice.activity.dao.impl;

import com.e_commerce.miscroservice.activity.dao.WxaShareLogDao;
import com.e_commerce.miscroservice.activity.entity.WxaShareLog;
import com.e_commerce.miscroservice.commons.enums.activity.ShareTypeEnum;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisOperaterUtil;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisSqlWhereBuild;
import com.e_commerce.miscroservice.commons.utils.TimeUtils;
import org.springframework.stereotype.Component;

import java.util.Calendar;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/11/21 19:32
 * @Copyright 玖远网络
 */
@Component
public class WxaShareLogDaoImpl implements WxaShareLogDao{


    /**
     * 插入一个对象
     *
     * @param wxaShareLog wxaShareLog
     * @return int
     * @author Charlie
     * @date 2018/11/21 19:36
     */
    @Override
    public int insert(WxaShareLog wxaShareLog) {
        return MybatisOperaterUtil.getInstance ().save (wxaShareLog);
    }

    /**
     * 今日分享商品的数量
     *
     * @param userId userId
     * @return java.lang.Long
     * @author Charlie
     * @date 2018/11/22 16:46
     */
    @Override
    public Long todayhasEarningsShareCount(Long userId) {
        Calendar startOfToday = TimeUtils.startOfToday ();
        String timeStr = TimeUtils.sdf ().format (startOfToday.getTime ());
        return MybatisOperaterUtil.getInstance ().count (
                new MybatisSqlWhereBuild (WxaShareLog.class)
                        .eq (WxaShareLog::getMemberId, userId)
                        .eq (WxaShareLog::getEarningsType, 1)
                        .gte (WxaShareLog::getCreateTime, timeStr)
        );
    }





}
