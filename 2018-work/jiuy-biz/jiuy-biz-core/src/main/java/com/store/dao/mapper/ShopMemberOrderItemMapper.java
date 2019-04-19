package com.store.dao.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.jiuyuan.dao.annotation.DBMaster;
import com.jiuyuan.entity.order.ShopMemberOrderItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
  * 会员订单明细表 Mapper 接口
 * </p>
 *
 * @author 赵兴林
 * @since 2017-09-05
 */
@DBMaster
public interface ShopMemberOrderItemMapper extends BaseMapper<ShopMemberOrderItem> {

    /**
     * 根据订单号查询
     * @param orderId
     * @return
     */
    List<ShopMemberOrderItem> findOrderItemByOrderId(@Param("orderId") long orderId);
    List<ShopMemberOrderItem> selectShopMemberOrderItem(@Param("orderId")Long orderId);
}