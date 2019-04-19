package com.store.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.jiuyuan.entity.newentity.ShopTagNavigation;
import com.store.dao.mapper.ShopTagNavigationMapper;

/**
 * <p>
 * 小程序标签导航表 服务实现类
 * </p>
 *
 * @author 赵兴林
 * @since 2018-02-11
 */
@Service
public class ShopTagNavigationService {
	
	@Autowired
	private ShopTagNavigationMapper shopTagNavigationMapper;
	
	@Autowired
	private ShopTagProductService shopTagProductService;
	
	/**
	 * 判断导航名称是否重名
	 * @param navigationId 
	 * @param navigationName
	 * @param storeId
	 * @return
	 */
	public boolean validate(long navigationId, String navigationName, long storeId) {
		Wrapper<ShopTagNavigation> wrapper = new EntityWrapper<ShopTagNavigation>().eq("store_id", storeId).eq("navigation_name",
				navigationName);
		if (navigationId != 0) {
			ShopTagNavigation shopTagNavigation = shopTagNavigationMapper.selectById(navigationId);
			if (navigationName.equals(shopTagNavigation.getNavigationName())) {
				return false;
			}
		}
		Integer count = shopTagNavigationMapper.selectCount(wrapper);
		if (count > 0) {
			return true;
		}
		return false;
	}
	/**
	 * 添加标签导航
	 * @param navigationName
	 * @param navigationImage
	 * @param storeId
	 * @param tagId 
	 */
	@Transactional(rollbackFor = Exception.class)
	public void addTagNavigation(ShopTagNavigation shopTagNavigation) {
		boolean result =validate(0,shopTagNavigation.getNavigationImage(), shopTagNavigation.getStoreId());
		if (result) {
			throw new RuntimeException("导航名称已存在!");
		}else{
			shopTagNavigationMapper.insert(shopTagNavigation);
		}
	}
	/**
	 * 修改标签导航
	 * @param navigationId
	 * @param navigationName
	 * @param navigationImage
	 * @param tagId
	 * @param storeId
	 */
	@Transactional(rollbackFor = Exception.class)
	public void updateTagNavigation(ShopTagNavigation shopTagNavigation) {
		shopTagNavigationMapper.updateById(shopTagNavigation);
	}
	
	
	/**
	 * 获取标签导航列表
	 * @param storeId
	 * @return
	 */
	public List<ShopTagNavigation> getShopNavigationList(long storeId) {
		Wrapper<ShopTagNavigation> wrapper = new EntityWrapper<ShopTagNavigation>();
		wrapper.eq("store_id", storeId);
		wrapper.eq("status", 0);
		wrapper.orderBy("weight", true);
		return shopTagNavigationMapper.selectList(wrapper);
	}
	/**
	 * 删除标签导航
	 * @param navigationId
	 * @param storeId
	 */
	public void deleteNavigation(long navigationId, long storeId) {
		shopTagNavigationMapper.deleteById(navigationId);//物理删除
	}
	/**
	 * 保存标签排序
	 * @param navigationIds
	 */
	public void orderSave(String navigationIds) {
		int weight = 0;
		if (StringUtils.isEmpty(navigationIds)) {
			return;
		}
		String[] navigationArr = navigationIds.split(",");
		for (int i = 0; i < navigationArr.length; i++) {
			ShopTagNavigation shopTagNavigation = new ShopTagNavigation();
			shopTagNavigation.setId(Long.valueOf(navigationArr[i]));
			// 设置顺序
			shopTagNavigation.setWeight(++weight);
			updateTagNavigation(shopTagNavigation);
		}
	}

	public List<ShopTagNavigation> findShopNavigationListById(long storeId) {
		return shopTagNavigationMapper.findShopNavigationListById(storeId);
	}
}
