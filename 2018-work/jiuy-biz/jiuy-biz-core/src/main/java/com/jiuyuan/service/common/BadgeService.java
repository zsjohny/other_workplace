package com.jiuyuan.service.common;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.jiuyuan.dao.mapper.operator.BadgeMapper;
import com.jiuyuan.dao.mapper.supplier.ProductNewMapper;
import com.jiuyuan.entity.newentity.Badge;
import com.jiuyuan.entity.newentity.ProductNew;

/**
 * <p>
 * 角标表 服务实现类
 * </p>
 *
 * @author 赵兴林
 * @since 2018-01-22
 */
@Service
public class BadgeService implements IBadgeService  {
	@Autowired
	private ProductNewMapper productNewMapper;
	
	@Autowired
	private BadgeMapper badgeMapper;
	
	/*
	 * (non-Javadoc)
	 * @see com.jiuyuan.service.common.IBadgeService#getBindPruductCount(long)
	 */
	@Override
	public int getBindPruductCount(long badgeId) {
		Wrapper<ProductNew> wrapper = new EntityWrapper<ProductNew>().eq("badge_id", badgeId).eq("delState", 0);
		return  productNewMapper.selectCount(wrapper);

	}
	/*
	 * (non-Javadoc)
	 * @see com.jiuyuan.service.common.IBadgeService#getBindBadgeProductCount(java.lang.String)
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public int getBindBadgeProductCount(String productIds) {
		Wrapper<ProductNew> wrapper = new EntityWrapper<ProductNew>().in("id", productIds).ne("badge_id", 0).eq("delState", 0);
		List<ProductNew> productNewList = productNewMapper.selectList(wrapper);
		ProductNew newProductNew = null;
		int count = 0;
		for (ProductNew productNew : productNewList) {
			newProductNew.setId(productNew.getId());
			newProductNew.setBadgeId(0L);//0：未绑定角标
			newProductNew.setBadgeName(null);
			newProductNew.setBadgeImage(null);
			productNewMapper.updateById(newProductNew);
			count ++;
		}
		return count;
	}
	/*
	 * (non-Javadoc)
	 * @see com.jiuyuan.service.common.IBadgeService#updateProductBadge(long, java.lang.String, java.lang.String)
	 */
	@SuppressWarnings("null")
	@Transactional(rollbackFor = Exception.class)
	public int updateProductBadge(long badgeId, String badgeName, String badgeImage) {
		Wrapper<ProductNew> wrapper = new EntityWrapper<ProductNew>().eq("badge_id", badgeId).eq("delState", 0);
		List<ProductNew> productNewList = productNewMapper.selectList(wrapper);
		int count = 0;
		if (productNewList.size()<1) {
			return count;
		}
		ProductNew newProductNew = new ProductNew();
		for (ProductNew productNew : productNewList) {
			newProductNew.setId(productNew.getId());
			newProductNew.setBadgeName(badgeName);
			newProductNew.setBadgeImage(badgeImage);
			productNewMapper.updateById(newProductNew);
			count ++;
		}
		return count;
	}
}
