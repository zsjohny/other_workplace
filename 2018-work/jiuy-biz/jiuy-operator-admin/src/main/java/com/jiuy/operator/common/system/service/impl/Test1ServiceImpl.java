package com.jiuy.operator.common.system.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.admin.common.constant.DSEnum;
import com.admin.core.mutidatasource.annotion.DataSource;
import com.jiuy.operator.common.system.service.ITest1Service;
import com.store.dao.mapper.coupon.ShopMemberCouponMapper;
import com.store.entity.coupon.ShopMemberCoupon;

@Service
@Transactional()
public class Test1ServiceImpl implements ITest1Service {

	@Resource
	ShopMemberCouponMapper shopMemberCouponMapper;

	@Override
	@DataSource(name = DSEnum.DATA_SOURCE_BIZ)
	public Object list(Integer id) {
		ShopMemberCoupon shopMemberCoupon = shopMemberCouponMapper.selectById(142);
		return shopMemberCoupon;
	}
}
