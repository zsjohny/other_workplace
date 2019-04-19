package com.jiuyuan.dao.mapper.supplier;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.jiuyuan.dao.annotation.DBMaster;
import com.jiuyuan.entity.newentity.ShopMemberOrder;

/**
 * <p>
  * 会员订单表 Mapper 接口
 * </p>
 *
 * @author nijin
 * @since 2018-01-11
 */
@DBMaster
public interface ShopMemberOrderNewMapper extends BaseMapper<ShopMemberOrder> {

	List<ShopMemberOrder> stopMemberTeamOrderOvertime(@Param("nowTime")long nowTime);

	List<ShopMemberOrder> getMemberSecondOvertimeOrder(@Param("nowTime")long nowTime);

}