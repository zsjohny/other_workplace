package com.e_commerce.miscroservice.publicaccount.service.proxy;


import com.e_commerce.miscroservice.commons.entity.application.proxy.ProxyReferee;
import com.e_commerce.miscroservice.commons.entity.proxy.MyProxyQueryVo;
import com.e_commerce.miscroservice.commons.helper.util.service.Response;
import com.e_commerce.miscroservice.publicaccount.entity.vo.ProxyRefereeUserInfo;
import com.github.pagehelper.PageInfo;
import com.e_commerce.miscroservice.publicaccount.entity.vo.MyProxyCustomerInfo;
import org.apache.ibatis.annotations.Param;

import java.util.Map;


/**
 * 代理关系
 * @author Charlie
 * @version V1.0
 * @date 2018/9/20 19:42
 * @Copyright 玖远网络
 */
public interface ProxyRefereeService{
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
    ProxyReferee bindReferee(Long refereeUserId, Long recommonUserId, Long refereParentId, Integer refereeUserType);




    /**
     * 根据id解绑
     *
     * @param refereeId 绑定关系id
     * @param operUserId 操作人
     * @return int
     * @author Charlie
     * @date 2018/9/25 17:30
     */
    int unBindById(Long refereeId, Long operUserId);


    /**
     * 根据id解绑
     * <p>这是安全的,用户只能解绑自己的</p>
     * @param refereeId 绑定关系id
     * @param userId 当前用户id(绑定关系拥有者)
     * @param operUserId 操作人
     * @return int
     * @author Charlie
     * @date 2018/9/25 17:30
     */
    int unBindSafeById(Long refereeId, Long userId, Long operUserId);

    /**
     * 今日代理数量
     *
     * @param type 1客户,2代理
     * @param publicAccountUserId publicAccountUserId
     * @return long
     * @author Charlie
     * @date 2018/9/26 7:10
     */
    long todayCreateCount(Integer type, Long publicAccountUserId);


    /**
     * 用户所有的下级代理/客户数量
     *
     * @param type 1客户,2代理
     * @param publicAccountUserId publicAccountUserId
     * @return long
     * @author Charlie
     * @date 2018/9/26 7:12
     */
    long allRefereeCount(Integer type, Long publicAccountUserId);

    /**
     * 查找他和上级的关系
     *
     * @param userId userId
     * @return com.e_commerce.miscroservice.publicaccount.po.proxy.ProxyReferee
     * @author Charlie
     * @date 2018/9/27 15:14
     */
    ProxyReferee findSuperior(Long userId);
    /**
     * 解绑用户以前的县级关系
     *
     * @param userId userId
     * @param operUserId 操作人id
     * @author Charlie
     * @date 2018/9/27 15:35
     */
    int unBindCustomer2CountyReferee(Long userId, Long operUserId);

    /**
     * 解绑用户所有
     *
     * @param userId userId
     * @author Charlie
     * @date 2018/9/27 15:35
     */
    int unBindAllReferee(Long userId);



    /**
     * 我的客户/代理
     *
     * @param query queryVo
     * @return java.util.List<com.e_commerce.miscroservice.publicaccount.entity.vo.ProxyRefereeUserInfo>
     * @author Charlie
     * @date 2018/9/26 21:40
     */
    MyProxyCustomerInfo myProxyCustomer(MyProxyQueryVo query);


    int orderNumByType(long userId,int type);


    /**
     * 描述 获取 userid的客户/代理的销售额  订单金额
     * @param userId
    * @param type
     * @author hyq
     * @date 2018/10/15 14:53
     * @return int
     */
    Map orderMoneyByType(long userId, int type);

    Map orderMoneyBySelf(long userId);

    /**
     * 描述 获取 userid的收益
     * @param userId
    * @param type  0 未发  1已发
     * @author hyq
     * @date 2018/10/15 14:53
     * @return int
     */
    int rewardMoneyIsGrant(long userId,int type);
}
