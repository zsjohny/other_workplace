package com.store.dao.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.jiuyuan.dao.annotation.DBMaster;
import com.jiuyuan.entity.newentity.ShopMemberDeliveryAddress;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * <p>
 * 小程序会员收货地址表 Mapper 接口
 * </p>
 *
 * @author 赵兴林
 * @since 2018-01-11
 */
@DBMaster
public interface ShopMemberDeliveryAddressMapper extends BaseMapper<ShopMemberDeliveryAddress> {
    Map<String, Object> selectLastUsedAddress(@Param(value = "memberId") Long memberId, @Param(value = "storeId") Long storeId);
    Map<String, Object> selectLastUsedAddress(@Param(value="memberId")Long memberId, @Param(value="storeId")Long storeId,@Param("time")Long time);
	List<ShopMemberDeliveryAddress> selectAddress(@Param("memberId")Long memberId);



    int findExistById(@Param("id")Long id);
    Long selectTime(@Param("memberId")Long memberId,@Param("storeId")Long storeId);
    long addDeliveryAddress(@Param("memberId")long memberId, @Param("storeId")long storeId, @Param("linkmanName")String linkmanName, @Param("phoneNumber")String phoneNumber, @Param("location")String location,
                            @Param("address")String address, @Param("shopMemberDeliveryAddress")ShopMemberDeliveryAddress shopMemberDeliveryAddress);

	/**
	 * 查询订单信息
	 * @param deliveryAddressId
	 * @return
	 */
    ShopMemberDeliveryAddress findDeliveryAddressById(@Param("deliveryAddressId") long deliveryAddressId);

	/**
	 * 	地址信息
	 * @param storeId
	 * @param memberId
	 * @return
	 */
	List<ShopMemberDeliveryAddress> findDeliveryAddressByStoreMemberId(@Param("storeId") Long storeId, @Param("memberId") Long memberId);
    ShopMemberDeliveryAddress selectShopMemberDeliveryAddress(@Param("id") Long id);
}