package com.e_commerce.miscroservice.publicaccount.mapper;

import com.e_commerce.miscroservice.commons.entity.proxy.MyProxyQueryVo;
import com.e_commerce.miscroservice.publicaccount.entity.vo.ProxyRefereeUserInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/9/27 16:58
 * @Copyright 玖远网络
 */
@Mapper
public interface ProxyRefereeMapper{

    /**
     * 我的客户
     * @param query query
     * @return java.util.List<com.e_commerce.miscroservice.publicaccount.entity.vo.ProxyRefereeUserInfo>
     * @author Charlie
     * @date 2018/9/27 17:00
     */
    List<ProxyRefereeUserInfo> myCustomer(MyProxyQueryVo query);

    /**
     * 我的代理
     *
     * @param query query
     * @return java.util.List<com.e_commerce.miscroservice.publicaccount.entity.vo.ProxyRefereeUserInfo>
     * @author Charlie
     * @date 2018/9/27 17:00
     */
    List<ProxyRefereeUserInfo> myProxy(MyProxyQueryVo query);

    /**
     * 描述 获取 userid的客户/代理的下单数量
     * @param userId
     * @param type
     * @author hyq
     * @date 2018/10/15 14:53
     * @return int
     */
    int orderNumByType(@Param("userId")long userId, @Param("type")int type);

    /**
     * 描述 获取 userid的客户/代理的销售额  订单金额
     * @param userId
     * @param type
     * @author hyq
     * @date 2018/10/15 14:53
     * @return int
     */
    Map orderMoneyByType(@Param("userId")long userId, @Param("type")int type);

    /**
     * 描述 收益
     * @param userId
    * @param type
     * @author hyq
     * @date 2018/10/15 14:53
     * @return int
     */
    Map orderMoneyBySelf(@Param("userId")long userId);

    /**
     * 描述 获取 userid的收益
     * @param userId
     * @param type  0 未发  1已发
     * @author hyq
     * @date 2018/10/15 14:53
     * @return int
     */
    int rewardMoneyIsGrant(@Param("userId")long userId,@Param("type")int type);

}
