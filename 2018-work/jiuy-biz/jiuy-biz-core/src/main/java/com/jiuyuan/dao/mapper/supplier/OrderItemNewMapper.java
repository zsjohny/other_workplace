package com.jiuyuan.dao.mapper.supplier;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.jiuyuan.dao.annotation.DBMaster;
import com.jiuyuan.entity.newentity.StoreOrderItemNew;

/**
 * <p>
  *  Mapper 接口
 * </p>
 *
 * @author 赵兴林
 * @since 2017-10-11
 */
@DBMaster
public interface OrderItemNewMapper extends BaseMapper<StoreOrderItemNew> {

	List<StoreOrderItemNew> orderItemsOfOrderNos(@Param("orderNos")Collection<Long> orderNos);

	List<Map<String, Object>> getOrederItemIdAndTotalPayByOrderNo(@Param("orderNo")Long orderNo);

    int salvageHistoryOrderItem(StoreOrderItemNew orderItem);
}