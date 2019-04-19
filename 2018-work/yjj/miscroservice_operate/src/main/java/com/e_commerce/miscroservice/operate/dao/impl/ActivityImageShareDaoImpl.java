package com.e_commerce.miscroservice.operate.dao.impl;

import com.e_commerce.miscroservice.commons.entity.activity.ActivityImageShare;
import com.e_commerce.miscroservice.commons.enums.StateEnum;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisOperaterUtil;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisSqlWhereBuild;
import com.e_commerce.miscroservice.operate.dao.ActivityImageShareDao;
import com.e_commerce.miscroservice.operate.mapper.ActivityImageShareMapper;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author hyf
 * @Date 2019/1/8 16:38
 */
@Repository
public class ActivityImageShareDaoImpl implements ActivityImageShareDao {

    @Resource
    private ActivityImageShareMapper activityImageShareMapper;

    /**
     * 根据筛选条件 查找
     *
     * @param type
     * @param shareType
     * @param timeStar
     * @param timeEnd
     * @param pageNumber
     * @param pageSize
     * @return
     */
    @Override
    public List<ActivityImageShare> findAllActivityImageShare(Integer type, Integer shareType, Long timeStar, Long timeEnd, Integer pageNumber, Integer pageSize) {
        PageHelper.startPage(pageNumber,pageSize);
        List<ActivityImageShare> list = activityImageShareMapper.findAllActivityImageShare(type,shareType,timeStar,timeEnd);
        return list;
    }

    @Override
    public ActivityImageShare findById(Long id) {
        ActivityImageShare activityImageShare =  MybatisOperaterUtil.getInstance().findOne(new ActivityImageShare(), new MybatisSqlWhereBuild(ActivityImageShare.class).eq(ActivityImageShare::getId, id).eq(ActivityImageShare::getDelStatus, StateEnum.NORMAL));
        return activityImageShare;
    }

    /**
     * 添加
     *
     * @param request
     * @return
     */
    @Override
    public Integer addShareImage(ActivityImageShare request) {
        Integer in = MybatisOperaterUtil.getInstance().save(request);
        return in;
    }

    /**
     * 更新
     *
     * @param request
     * @return
     */
    @Override
    public Integer shareImageUpd(ActivityImageShare request) {
        Integer in = MybatisOperaterUtil.getInstance().update(request,new MybatisSqlWhereBuild(ActivityImageShare.class).eq(ActivityImageShare::getId,request.getId()));
        return in;
    }
}
