package com.store.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.jiuyuan.entity.newentity.ShopTag;
import com.jiuyuan.entity.newentity.ShopTagNavigation;
import com.store.dao.mapper.ShopTagMapper;
import com.store.dao.mapper.ShopTagNavigationMapper;

/**
 * <p>
 * 门店标签表 服务实现类
 * </p>
 *
 * @author 赵兴林
 * @since 2018-02-09
 */
@Service
public class ShopTagService {

	@Autowired
	private ShopTagMapper shopTagMapper;

	@Autowired
	private ShopTagProductService shopTagProductService;

	@Autowired
	private ShopTagNavigationMapper shopTagNavigationMapper;

	/**
	 * 判断标签名称是否重复
	 * 
	 * @param tagName
	 * @param storeId
	 * @return
	 */
	public boolean validate(String tagName, long storeId) {
		Wrapper<ShopTag> wrapper = new EntityWrapper<ShopTag>().eq("store_id", storeId).eq("status", 0).eq("tag_name",
				tagName);
		Integer count = shopTagMapper.selectCount(wrapper);
		if (count > 0) {
			return false;
		}
		return true;
	}

	/**
	 * 添加标签
	 * 
	 * @param tagName
	 * @param storeId
	 */
	@Transactional(rollbackFor = Exception.class)
	public void addTag(String tagName, long storeId) {
		ShopTag shopTag = new ShopTag();
		shopTag.setCreateTime(System.currentTimeMillis());
		shopTag.setUpdateTime(System.currentTimeMillis());
		shopTag.setStoreId(storeId);
		shopTag.setTagName(tagName);
		shopTagMapper.insert(shopTag);
	}

	/**
	 * 修改标签
	 * 
	 * @param tagId
	 * @param tagName
	 * @param storeId
	 */
	@Transactional(rollbackFor = Exception.class)
	public void updateTag(long tagId, String tagName, long storeId) {
		ShopTag shopTag = new ShopTag();
		shopTag.setId(tagId);
		shopTag.setTagName(tagName);
		shopTag.setUpdateTime(System.currentTimeMillis());
		shopTagMapper.updateById(shopTag);

		// 修改标签商品中间表中标签名称
		shopTagProductService.updateShopTagProduct(storeId, tagId, tagName);
	}

	/**
	 * 根据标签id获取标签
	 * 
	 * @param tagId
	 * @return
	 */
	public ShopTag getTagById(Long tagId) {
		return shopTagMapper.selectById(tagId);
	}

	/**
	 * 获取标签列表
	 * 
	 * @param storeId
	 * @return
	 */
	public List<Map<String, Object>> getTagMap(long storeId) {
		List<Map<String, Object>> selectMaps  = shopTagMapper.selectShopTagMap(storeId);
		return selectMaps;

	}

	/**
	 * 删除标签
	 * 
	 * @param tagId
	 * @param storeId
	 */
	@Transactional(rollbackFor = Exception.class)
	public void deleteTag(long tagId, long storeId) {
		Wrapper<ShopTag> wrapper = new EntityWrapper<ShopTag>().eq("store_id", storeId).eq("id", tagId);
		shopTagMapper.delete(wrapper);
		// 删除标签商品关联关系数据
		shopTagProductService.deleteShopTagProduct(tagId, storeId);
	}
	/**
	 * 标签列表
	 * @param storeId
	 * @return
	 */
	public List<Map<String, Object>>  getTagList(long storeId) {
		Wrapper<ShopTag> wrapper = new EntityWrapper<ShopTag>().eq("store_id", storeId).eq("status", 0).orderBy("create_time",true);
		List<Map<String, Object>> selectMaps = shopTagMapper.selectMaps(wrapper);
		return selectMaps;
	}
	/**
	 * 判断标签是否与导航绑定
	 * @param tagId
	 * @param storeId 
	 * @return
	 */
	public boolean bindNavigation(long tagId, long storeId) {
		Wrapper<ShopTagNavigation> wrapper = new EntityWrapper<ShopTagNavigation>().eq("tag_id", tagId).eq("store_id", storeId);
		Integer selectCount = shopTagNavigationMapper.selectCount(wrapper );
		if (selectCount > 0) {
			return true;
		}
		return false;
	}



	/**
	 * 根据id批量查询
	 *
	 * @param storeId 门店id
	 * @param tagIdList tagId
	 * @return java.util.List<com.jiuyuan.entity.newentity.ShopTag>
	 * @author Charlie(唐静)
	 * @date 2018/7/4 17:23
	 */
    public List<ShopTag> listByIds(Long storeId, List<Long> tagIdList) {
		if (tagIdList.isEmpty()) {
			return new ArrayList<>(0);
		}

		Wrapper<ShopTag> wrapper = new EntityWrapper<ShopTag>()
				.in("id", tagIdList)
				.eq("status", 0)
				.eq("store_id", storeId);
		return shopTagMapper.selectList(wrapper);
	}



	/**
	 * 获取用户标签信息,包括统计信息
	 *
	 * @param storeId 用户id
	 * @param soldOuts 商品的状态 0：草稿，1：上架， 2：下架
	 * @return java.util.List
	 * @author Charlie(唐静)
	 * @date 2018/7/10 16:46
	 */
	public List<Map<String,Object>> getTagMapV377(long storeId, List<Integer> soldOuts) {
		return shopTagMapper.getTagMapV377(storeId, soldOuts);
	}
}
