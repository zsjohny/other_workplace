package com.jiuy.core.dao.impl.sql;

import java.util.HashMap;
import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.jiuy.core.dao.ShopMemberOrderDao;
import com.jiuyuan.entity.newentity.ShopMemberOrder;
import com.store.entity.coupon.ShopMemberCoupon;

@Repository
public class ShopMemberOrderDaoSqlImpl implements ShopMemberOrderDao {

	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;
	@Override
	public List<ShopMemberOrder> getWaitPayTipOrderList(long twentyThreeBefore) {
		HashMap<String, Object> params = new HashMap<String,Object>(); 
		params.put("twentyThreeBefore", twentyThreeBefore);
		return sqlSessionTemplate.selectList("com.jiuy.core.dao.impl.sql.ShopMemberOrderDaoSqlImpl.getWaitPayTipOrderList", params);
	}

	@Override
	public List<ShopMemberOrder> getUnPaidMemberOrderList(long outTime) {
		HashMap<String, Object> params = new HashMap<String,Object>(); 
		params.put("outTime", outTime);
		return sqlSessionTemplate.selectList("com.jiuy.core.dao.impl.sql.ShopMemberOrderDaoSqlImpl.getUnPaidMemberOrderList", params);
	}

	@Override
	public int storeOrderByOrderId(ShopMemberOrder shopMemberOrder) {
		HashMap<String, Object> params = new HashMap<String,Object>(); 
		params.put("shopMemberOrder", shopMemberOrder);
		return sqlSessionTemplate.update("com.jiuy.core.dao.impl.sql.ShopMemberOrderDaoSqlImpl.storeOrderByOrderId", params);
	}

	@Override
	public int updateOrderSendMessage(Long orderId) {
		HashMap<String, Object> params = new HashMap<String,Object>(); 
		params.put("orderId", orderId);
		return sqlSessionTemplate.update("com.jiuy.core.dao.impl.sql.ShopMemberOrderDaoSqlImpl.updateOrderSendMessage", params);
	}

	@Override
	public int updateShopMemberCouponStatus(ShopMemberCoupon shopMemberCoupon) {
		HashMap<String, Object> params = new HashMap<String,Object>(); 
		params.put("shopMemberCoupon", shopMemberCoupon);
		return sqlSessionTemplate.update("com.jiuy.core.dao.impl.sql.ShopMemberOrderDaoSqlImpl.updateShopMemberCouponStatus", params);
	}
}