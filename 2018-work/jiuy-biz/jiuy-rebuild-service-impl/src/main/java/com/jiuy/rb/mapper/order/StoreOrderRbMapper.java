package com.jiuy.rb.mapper.order; 
 
import com.jiuy.rb.model.order.StoreOrderRb; 
import com.jiuy.base.mapper.BaseMapper;
import com.jiuy.rb.model.order.StoreOrderRbQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;
import java.util.Set;

/** 
 * 批发订单表 的mapper 文件
 
 * @author Aison
 * @version V1.0 
 * @date 2018年08月03日 下午 06:23:39
 * @Copyright 玖远网络 
 */
public interface StoreOrderRbMapper extends BaseMapper<StoreOrderRb>{  

	// @Costom 
	// 从上一行注释以上为系统生成 从这一行往下为用户自定义 请将自定义的sql对应的接口放在此行下面

    /**
     * 获取订单列表
     *
     * @param query query
     * @author Aison
     * @date 2018/6/29 16:34
     * @return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
     */
    List<Map<String,Object>> selectOrderList(StoreOrderRbQuery query);


    /**
     * 获取被合并的订单
     *
     * @param mergedIds mergedIds
     * @author Aison
     * @date 2018/6/29 16:33
     * @return java.util.List<com.jiuy.rb.model.order.StoreOrderRb>
     */
    List<StoreOrderRb> childOrders(@Param("mergedIds") Set<Long> mergedIds);

    /**
     * 修改订单的优惠券发放状态
     *
     * @param orderNo orderNo
     * @author Aison
     * @date 2018/8/3 18:27
     * @return int
     */
    int updateSendCoupon(String orderNo);

}
