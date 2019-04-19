package com.jiuyuan.service.common;

import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.jiuyuan.entity.newentity.Badge;

public interface IBadgeService {

	/**
	 * 获取绑定角标的商品数量
	 * @param badgeId
	 * @return
	 */
	int getBindPruductCount(long badgeId);
	/**
	 * 根据商品IDS获取绑定了角标的商品数量,并清空绑定角标信息
	 * @param productIds
	 * @return
	 */
	int getBindBadgeProductCount(String productIds);
	/**
	 * 修改商品角标信息
	 * @param badgeId
	 * @param badgeName
	 * @param badgeImage
	 * @param list 
	 * @return
	 */
	int updateProductBadge(long badgeId, String badgeName, String badgeImage);

}