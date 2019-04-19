package com.store.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.jiuyuan.entity.newentity.ShopTag;
import com.jiuyuan.entity.newentity.ShopTagProduct;
import com.store.dao.mapper.ShopTagProductMapper;

/**
 * <p>
 * 标签商品中间表 服务实现类
 * </p>
 *
 * @author 赵兴林
 * @since 2018-02-09
 */
@Service
public class ShopTagProductService {
	 static Logger logger = LoggerFactory.getLogger(ShopTagProductService.class);

	@Autowired
	private ShopTagProductMapper shopTagProductMapper;

	@Autowired
	private ShopTagService shopTagService;

	@Deprecated
	public int getProductCountByTagId(long tagId, long storeId) {
		Wrapper<ShopTagProduct> wrapper = new EntityWrapper<ShopTagProduct>()
				.eq("store_id", storeId)
				.eq("tag_id",tagId);
		int productCount = shopTagProductMapper.selectCount(wrapper);
		return productCount;
	}

	/**
	 * 清除商品绑定标签
	 * 
	 * @param productId
	 * @param storeId
	 */
	@Transactional(rollbackFor = Exception.class)
	public void clearTag(long productId, long storeId) {
		Wrapper<ShopTagProduct> wrapper = new EntityWrapper<ShopTagProduct>().eq("store_id", storeId).eq("shop_product_id",
				productId);
		shopTagProductMapper.delete(wrapper);// 物理删除
	}

	/**
	 * 商品绑定标签
	 * 
	 * @param tagId
	 * @param shopProductId
	 * @param storeId
	 */
	@Transactional(rollbackFor = Exception.class)
	public void bindTag(Long tagId, long shopProductId, long storeId) {
		ShopTagProduct shopTagProduct = new ShopTagProduct();
		shopTagProduct.setCreateTime(System.currentTimeMillis());
		shopTagProduct.setUpdateTime(System.currentTimeMillis());
		shopTagProduct.setShopProductId(shopProductId);
		shopTagProduct.setStoreId(storeId);
		shopTagProduct.setTagId(tagId);
		// 根据标签id 获取标签
		ShopTag shoptag = shopTagService.getTagById(tagId);
		shopTagProduct.setTagName(shoptag.getTagName());
		shopTagProductMapper.insert(shopTagProduct);
	}

	/**
	 * 获取商品绑定的标签列表
	 * 
	 * @param productId
	 * @param storeId
	 * @return
	 */
	public List<ShopTagProduct> getBindProductTagList(long productId, long storeId) {
		Wrapper<ShopTagProduct> wrapper = new EntityWrapper<ShopTagProduct>().eq("store_id", storeId).eq("shop_product_id",productId);
		List<ShopTagProduct> selectList = shopTagProductMapper.selectList(wrapper);
		return selectList;
	}

	/**
	 * 删除标签商品关联关系
	 * 
	 * @param tagId
	 * @param storeId
	 */
	@Transactional(rollbackFor = Exception.class)
	public void deleteShopTagProduct(long tagId, long storeId) {
		Wrapper<ShopTagProduct> wrapper = new EntityWrapper<ShopTagProduct>().eq("store_id", storeId).eq("tag_id",tagId);
		shopTagProductMapper.delete(wrapper);

	}

	/**
	 * 修改标签名称
	 * 
	 * @param storeId
	 * @param tagId
	 * @param tagName
	 */
	@Transactional(rollbackFor = Exception.class)
	public void updateShopTagProduct(long storeId, long tagId, String tagName) {
		Wrapper<ShopTagProduct> wrapper = new EntityWrapper<ShopTagProduct>().eq("store_id", storeId).eq("tag_id",tagId);
		List<ShopTagProduct> shopTagProducts = shopTagProductMapper.selectList(wrapper);
		for (ShopTagProduct shopTagProduct : shopTagProducts) {
			ShopTagProduct newShopTagProduct = new ShopTagProduct();
			newShopTagProduct.setUpdateTime(System.currentTimeMillis());
			newShopTagProduct.setId(shopTagProduct.getId());
			newShopTagProduct.setTagName(tagName);	
			shopTagProductMapper.updateById(newShopTagProduct);
		}
	}



	/**
	 * 同步商品标签信息
	 *
	 * @param storeId 用户门店id
	 * @param shopPduId 小程序商品id
	 * @param tagIds 新增/修改的tagId
	 * @author Charlie(唐静)
	 * @date 2018/7/4 16:55
	 */
	@Transactional(rollbackFor = Exception.class)
	public void synchronizePduTagInfo(Long storeId, Long shopPduId, List<Long> tagIds) {
		logger.info("开始同步商品标签信息 shopPduId:"+shopPduId+", tagIds:"+tagIds);
		// 删除原来的
		clearTag(shopPduId, storeId);
		logger.info("删除已有的标签 shopPduId:"+shopPduId+", tagIds:"+tagIds);
		// 新增现在的
		tagIds.forEach(tagId-> bindTag(tagId, shopPduId, storeId));
		logger.info("新增新的标签 shopPduId:"+shopPduId+", tagIds:"+tagIds);
	}



	/**
	 * 根据标签获取上架商品数量
	 *
	 * @param tagId tagId
	 * @param storeId storeId
	 * @return java.lang.Integer
	 * @author Charlie
	 * @date 2018/7/18 15:31
	 */
	public Integer getSoldOutProductCount(Long tagId, Long storeId) {
		return shopTagProductMapper.getSoldOutProductCount (tagId, storeId);
	}
}
