package com.e_commerce.miscroservice.publicaccount.service.impl;

import com.e_commerce.miscroservice.commons.entity.application.proxy.ProxyCustomer;
import com.e_commerce.miscroservice.commons.entity.application.proxy.ProxyReferee;
import com.e_commerce.miscroservice.commons.entity.proxy.MyProxyQueryVo;
import com.e_commerce.miscroservice.commons.enums.StateEnum;
import com.e_commerce.miscroservice.commons.exception.ErrorHelper;
import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisOperaterUtil;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisSqlWhereBuild;
import com.e_commerce.miscroservice.commons.helper.util.service.Response;
import com.e_commerce.miscroservice.commons.utils.ResponseHelper;
import com.e_commerce.miscroservice.commons.utils.TimeUtils;
import com.e_commerce.miscroservice.publicaccount.dao.ProxyCustomerDao;
import com.e_commerce.miscroservice.publicaccount.entity.vo.MyProxyCustomerInfo;
import com.e_commerce.miscroservice.publicaccount.entity.vo.ProxyRefereeUserInfo;
import com.e_commerce.miscroservice.publicaccount.mapper.ProxyRefereeMapper;
import com.e_commerce.miscroservice.publicaccount.service.proxy.ProxyRefereeService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.el.lang.ELArithmetic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/9/20 19:53
 * @Copyright 玖远网络
 */
@Service
public class ProxyRefereeServiceImpl implements ProxyRefereeService{
    private static final int NORMAL = 0;
    private static final int UNBIND = 1;
    private static final int BIND = 0;
    private Log logger = Log.getInstance (ProxyRefereeServiceImpl.class);

    @Autowired
    private ProxyCustomerDao proxyCustomerDao;
    @Autowired
    private ProxyRefereeMapper proxyRefereeMapper;

    /**
     * 绑定代理关系
     *
     * @param recommonUserId   被推荐人
     * @param refereeUserId   推荐人
     * @param refereParentId 推荐人上级
     * @param refereeUserType 被推荐人身份1:客户,2:县级代理商
     * @return com.e_commerce.miscroservice.operate.po.proxy.ProxyReferee
     * @author Charlie
     * @date 2018/9/20 20:03
     */
    @Override
    public ProxyReferee bindReferee(Long refereeUserId, Long recommonUserId, Long refereParentId, Integer refereeUserType) {
        //校验一个用户只能是一个代理的可户,或是一个代理的下级
        ProxyReferee history = MybatisOperaterUtil.getInstance ().findOne (
                new ProxyReferee (),
                new MybatisSqlWhereBuild (ProxyReferee.class)
                        .eq (ProxyReferee::getRecommonUserId, recommonUserId)
                        .eq (ProxyReferee::getDelStatus, StateEnum.NORMAL)
                        .eq (ProxyReferee::getStatus, BIND)
                        .eq (ProxyReferee::getRecommonUserType, refereeUserType)
        );
        if (history != null) {
            logger.info ("绑定代理关系,用户已有所属关系 refereeUserId:{},refereeUserType:{}", refereeUserId, refereeUserType);
            return history;
        }

        ProxyReferee referee = new ProxyReferee ();
        referee.setRefereeUserId (refereeUserId);
        referee.setRecommonUserId (recommonUserId);
        referee.setRefereParentId (refereParentId);
        referee.setRecommonUserType (refereeUserType);
        int rec = MybatisOperaterUtil.getInstance ().save (referee);
        logger.info ("绑定代理商关系 storeId:{},recommonUserId:{},refereParentId:{},rec:{}", refereeUserId, recommonUserId, refereParentId, rec);
        ErrorHelper.declare (rec == 1, "绑定代理商关系失败");
        return referee;
    }





    /**
     * 根据id解绑
     * <p>这是不安全的,不建议暴露在服务端</p>
     *
     * @param refereeId  绑定关系id
     * @param operUserId 操作人
     * @return int
     * @author Charlie
     * @date 2018/9/25 17:30
     */
    @Override
    public int unBindById(Long refereeId, Long operUserId) {
        ProxyReferee updInfo = new ProxyReferee ();
        updInfo.setStatus (1);
        updInfo.setUpdateUserId (operUserId);
        return MybatisOperaterUtil.getInstance ().update (
                updInfo,
                new MybatisSqlWhereBuild (ProxyReferee.class)
                        .eq (ProxyReferee::getDelStatus, NORMAL)
                        .eq (ProxyReferee::getStatus, 0)
                        .eq (ProxyReferee::getId, refereeId)
        );
    }


    /**
     * 根据id解绑
     * <p>这是安全的,用户只能解绑自己的</p>
     *
     * @param refereeId  绑定关系id
     * @param userId     当前用户id(绑定关系拥有者)
     * @param operUserId 操作人
     * @return int
     * @author Charlie
     * @date 2018/9/25 17:30
     */
    @Override
    public int unBindSafeById(Long refereeId, Long userId, Long operUserId) {
        ProxyReferee updInfo = new ProxyReferee ();
        updInfo.setStatus (1);
        updInfo.setUpdateUserId (operUserId);
        return MybatisOperaterUtil.getInstance ().update (
                updInfo,
                new MybatisSqlWhereBuild (ProxyReferee.class)
                        .eq (ProxyReferee::getDelStatus, NORMAL)
                        .eq (ProxyReferee::getStatus, 0)
                        .eq (ProxyReferee::getId, refereeId)
                        .eq (ProxyReferee::getRefereeUserId, userId)
        );
    }



    /**
     * 今日代理数量
     *
     * @param type 1客户,2代理
     * @param publicAccountUserId publicAccountUserId
     * @return long
     * @author Charlie
     * @date 2018/9/26 7:10
     */
    @Override
    public long todayCreateCount(Integer type, Long publicAccountUserId) {
        Calendar calendar = Calendar.getInstance ();
        calendar.setTime (new Date ());
        calendar.set (Calendar.HOUR_OF_DAY, 0);
        calendar.set (Calendar.MINUTE, 0);
        calendar.set (Calendar.SECOND, 0);
        String beginTime = TimeUtils.cal2Str (calendar);
        calendar.add (Calendar.DATE, 1);
        String endTime = TimeUtils.cal2Str (calendar);

        //直接关系
        long count = MybatisOperaterUtil.getInstance ().count (
                new MybatisSqlWhereBuild (ProxyReferee.class)
                        .eq (ProxyReferee::getRefereeUserId, publicAccountUserId)
                        .eq (ProxyReferee::getRecommonUserType, type)
                        .eq (ProxyReferee::getDelStatus, StateEnum.NORMAL)
                        .eq (ProxyReferee::getStatus, 0)
                        .gte (ProxyReferee::getCreateTime, beginTime)
                        .lt (ProxyReferee::getCreateTime, endTime)
        );
        return count;
    }



    /**
     * 用户所有的下级代理数量
     *
     * @param type type
     * @param publicAccountUserId publicAccountUserId
     * @return long
     * @author Charlie
     * @date 2018/9/26 7:12
     */
    @Override
    public long allRefereeCount(Integer type, Long publicAccountUserId) {
        //直接关系
        long count = MybatisOperaterUtil.getInstance ().count (
                new MybatisSqlWhereBuild (ProxyReferee.class)
                        .eq (ProxyReferee::getRefereeUserId, publicAccountUserId)
                        .eq (ProxyReferee::getRecommonUserType, type)
                        .eq (ProxyReferee::getDelStatus, StateEnum.NORMAL)
                        .eq (ProxyReferee::getStatus, 0)
        );
        return count;
    }



    /**
     * 查找他和上级的关系
     *
     * @param userId userId
     * @return com.e_commerce.miscroservice.publicaccount.po.proxy.ProxyReferee
     * @author Charlie
     * @date 2018/9/27 15:14
     */
    @Override
    public ProxyReferee findSuperior(Long userId) {
        return  MybatisOperaterUtil.getInstance ().findOne (
                new ProxyReferee (),
                new MybatisSqlWhereBuild (ProxyReferee.class)
                        .eq (ProxyReferee::getRecommonUserId, userId)
                        .eq (ProxyReferee::getDelStatus, StateEnum.NORMAL)
                        .eq (ProxyReferee::getStatus, 0)
        );
    }




    /**
     * 解绑用户以前的县级关系
     *
     * @param userId userId
     * @param operUserId 操作人id
     * @author Charlie
     * @date 2018/9/27 15:35
     */
    @Override
    public int unBindCustomer2CountyReferee(Long userId, Long operUserId) {
        ProxyReferee updInfo = new ProxyReferee ();
        updInfo.setStatus (1);
        updInfo.setUpdateUserId (operUserId);
        return MybatisOperaterUtil.getInstance ().update (
                updInfo,
                new MybatisSqlWhereBuild (ProxyReferee.class)
                        .eq (ProxyReferee::getDelStatus, NORMAL)
                        .eq (ProxyReferee::getStatus, 0)
                        .eq (ProxyReferee::getRecommonUserId, userId)
                        .eq (ProxyReferee::getRecommonUserType, 1)
        );

    }


    /**
     * 解绑用户所有
     *
     * @param userId userId
     * @author Charlie
     * @date 2018/9/27 15:35
     */
    @Override
    public int unBindAllReferee(Long userId) {
        ProxyReferee updInfo = new ProxyReferee ();
        updInfo.setStatus (1);
        return MybatisOperaterUtil.getInstance ().update (
                updInfo,
                new MybatisSqlWhereBuild (ProxyReferee.class)
                        .eq (ProxyReferee::getDelStatus, NORMAL)
                        .eq (ProxyReferee::getStatus, 0)
                        .eq (ProxyReferee::getRecommonUserId, userId)
        );
    }



    /**
     * 我的客户/代理
     *
     * @param query queryVo
     * @return java.util.List<com.e_commerce.miscroservice.publicaccount.entity.vo.ProxyRefereeUserInfo>
     * @author Charlie
     * @date 2018/9/26 21:40
     */
    @Override
    public MyProxyCustomerInfo myProxyCustomer(MyProxyQueryVo query) {

        MyProxyCustomerInfo myProxyCustomer = new MyProxyCustomerInfo ();
        //进日新增
        long todayCreateCount = todayCreateCount (query.getType (), query.getPublicAccountUserId ());
        myProxyCustomer.setTodayCreateCount (todayCreateCount);
        //我的代理
        List<ProxyRefereeUserInfo> resultList;
        if (query.getType () == 1) {
            PageHelper.startPage(query.getPageNumber (), query.getPageSize ());
            resultList=  proxyRefereeMapper.myCustomer(query);
        }
        else if (query.getType () == 2) {
            PageHelper.startPage(query.getPageNumber (), query.getPageSize ());
            resultList = proxyRefereeMapper.myProxy(query);
        }
        else {
            throw ErrorHelper.me ("未知的查询类型");
        }
        PageInfo<ProxyRefereeUserInfo> userList = new PageInfo<> (resultList);
        myProxyCustomer.setUserList (userList);

        ProxyCustomer proxyCustomer = proxyCustomerDao.selectByUserId (query.getPublicAccountUserId ());
        myProxyCustomer.setProxyCustomer (proxyCustomer);
        return myProxyCustomer;

    }

    @Override
    public int orderNumByType(long userId, int type) {
        return proxyRefereeMapper.orderNumByType(userId, type);
    }

    @Override
    public Map orderMoneyByType(long userId, int type) {
        return proxyRefereeMapper.orderMoneyByType(userId, type);
    }

    @Override
    public Map orderMoneyBySelf(long userId) {
        return proxyRefereeMapper.orderMoneyBySelf(userId);
    }

    @Override
    public int rewardMoneyIsGrant(long userId, int type) {
        return proxyRefereeMapper.rewardMoneyIsGrant(userId, type);
    }


}
