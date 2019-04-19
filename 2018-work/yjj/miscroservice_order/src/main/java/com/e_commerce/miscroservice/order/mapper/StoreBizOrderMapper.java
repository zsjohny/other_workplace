package com.e_commerce.miscroservice.order.mapper;

import com.e_commerce.miscroservice.order.entity.ShopMemberDeliveryAddress;
import com.e_commerce.miscroservice.order.entity.StoreBizOrder;
import com.e_commerce.miscroservice.order.entity.StoreBusiness;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/12/11 16:40
 * @Copyright 玖远网络
 */
@Mapper
public interface StoreBizOrderMapper{


    /**
     * 查找还未支付的会员订单
     *
     * @param storeId storeId
     * @param memberType 会员类型
     * @param totalFee 支付金额
     * @param canal 支付来源
     * @return com.e_commerce.miscroservice.order.entity.StoreBizOrder
     * @author Charlie
     * @date 2018/12/11 16:39
     */
    List<StoreBizOrder> findNoPayMemberOrder(@Param ("storeId") Long storeId,
                                             @Param ("memberType") Integer memberType,
                                             @Param ("totalFee") BigDecimal totalFee,
                                             @Param ("canal") Integer canal,
                                             @Param ("lastTime") String lastTime

    );


    /**
     * 添加默认小程序收货地址
     */
    int chooseDefault(@Param("id") Long id);
    /**
     * 在添加默认收货地址之前将之前默认地址清除
     */
    int updateAdress(@Param("memberId")Long memberId);

    /**
     * 查询店家地址
     * @param storeId
     * @return
     */
    StoreBusiness  selectBusiness(@Param("storeId")Long storeId);

    /**
     * 查询收货地址
     * @param memberId
     * @return
     */
    List<ShopMemberDeliveryAddress> selectMemberAddress(@Param("memberId")Long memberId);
    /**
     * 根据id查询收货地址
     */
    ShopMemberDeliveryAddress selectAddressById(@Param("memberId")Long memberId,@Param("id")Long id);
    /**
     * 根据查询时间和用户id查询地址did
     */
    Long selectAddressId(@Param("time") Long time,@Param("memberId") Long memberId);

    Long selectCount();
}
