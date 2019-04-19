package com.e_commerce.miscroservice.order.mapper;

import com.e_commerce.miscroservice.commons.entity.application.proxy.ProxyReward;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 描述
 * @date 2018/9/18 17:25
 * @return
 */
@Mapper
public interface RewardMapper extends BaseMapper<ProxyReward> {

    @Select("SELECT cus.type from yjj_proxy_customer cus where cus.user_id=#{userId}")
    Integer proxyType(@Param("userId")long userId);

    @Select("SELECT * from yjj_proxy_reward rew where rew.user_id=#{userId} and rew.order_no=#{orderNo}")
    ProxyReward getRewardMoney(@Param("userId")long userId,@Param("orderNo")String orderNo);

}
