package com.store.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jiuyuan.dao.mapper.supplier.ShopProductMapper;
import com.jiuyuan.entity.newentity.ProductNew;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.jiuyuan.entity.newentity.ShopProduct;
import com.jiuyuan.util.PriceUtil;
import com.store.dao.mapper.WxaMemberFavoriteMapper;
import com.store.entity.ShopMemberFavorite;
import com.store.entity.ShopMemberVisit;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;

/**
 * @author QiuYuefan
 * @version 创建时间: 2017年6月19日 14:24:27
 */

@Service
public class WxaMemberFavoriteService {
	private static final Log logger = LogFactory.get();

	@Autowired
	private WxaMemberFavoriteMapper shopMemberFavoriteMapper;
	@Autowired
	private ShopProductMapper shopProductMapper;
	// @Autowired
	// private ShopProductMapper shopProductMapper;

	@Autowired
	private ShopProductService shopProductService;

	/**
	 * 添加或者取消会员收藏
	 * 
	 * @param memberId
	 * @param type
	 * @param status
	 * @return
	 */
	public int addMemberFavoriteRecord(Long memberId, Long shopProductId, Integer type, Integer status, Long storeId) {
		if (memberId == null || memberId < 1) {
			throw new RuntimeException("会员信息有误,请确认");
		}
		ShopMemberFavorite favoriteOld = getFavorite(memberId, storeId, shopProductId, type);
		int ret = 0;
		ShopMemberFavorite favorite = null;
		if (favoriteOld == null) {// 无收藏记录则添加
			if (status != 0) {
				throw new RuntimeException("未收藏不能取消收藏");
			}
			favorite = new ShopMemberFavorite();
			favorite.setStatus(0);
			favorite.setMemberId(memberId);
			favorite.setStoreId(storeId);
			favorite.setRelatedId(shopProductId);
			favorite.setType(type);
			favorite.setCreateTime(System.currentTimeMillis());
			favorite.setUpdateTime(System.currentTimeMillis());
			ret = shopMemberFavoriteMapper.insert(favorite);
		} else if(favoriteOld.getStatus()-status==0){
//			throw new RuntimeException("请勿重复提交收藏或者取消收藏商品");
			return status;
		} else {//有收藏记录则修改
			favorite = new ShopMemberFavorite();
			favorite.setId(favoriteOld.getId());
			if(favoriteOld.getStatus()==0){
				favorite.setStatus(-1);
			}else{
				favorite.setStatus(0);
			}
			favorite.setStatus(status);
			favorite.setUpdateTime(System.currentTimeMillis());
			ret = shopMemberFavoriteMapper.updateById(favorite);
		}
		if(type == 0){//商品收藏
			if (ret == 1) {//成功
				if (favorite.getStatus() == 0) {// 收藏增加商品收藏数量
					shopProductService.increaseFavoriteCount(shopProductId);
				} else {// 取消收藏减少商品收藏数量
					shopProductService.reduceFavoriteCount(shopProductId);
				}
			}
		}
		return favorite.getStatus();
	}

	private ShopMemberFavorite getFavorite(Long memberId, Long storeId, Long productId, Integer type) {
		ShopMemberFavorite favorite = new ShopMemberFavorite();
		favorite.setMemberId(memberId);
		favorite.setStoreId(storeId);
		favorite.setRelatedId(productId);
		favorite.setType(type);
		return shopMemberFavoriteMapper.selectOne(favorite);
	}
	
	public int getMyFavoriteCount(long memberId, long storeId) {
		/*
//		logger.info("获取收藏商品数量：memberId："+memberId+",storeId:"+storeId);
		Wrapper<ShopMemberFavorite> wrapper = new EntityWrapper<ShopMemberFavorite>().eq("member_id", memberId)
				.eq("store_id", storeId).eq("status", 0).eq("type", 0);
//		return shopMemberFavoriteMapper.selectCount(wrapper);
		List<ShopMemberFavorite> shopMemberFavoriteList = shopMemberFavoriteMapper.selectList( wrapper);
		 */
		Integer count = shopMemberFavoriteMapper.findMyFavoriteCount(memberId,storeId);
		return count;

		//无需处理已经下架和删除的商品，在收藏商品列表中会做状态处理
//		List<ShopMemberFavorite> selectList = shopMemberFavoriteMapper.selectList(wrapper);
//		for (ShopMemberFavorite shopMemberFavorite : selectList) {
//			ShopProduct shopProduct = shopProductMapper.selectById(shopMemberFavorite.getRelatedId());
//			//状态:-1：删除，0：正常          0：草稿，1：上架， 2：下架
//			if(shopProduct==null || shopProduct.getStatus()==-1 || shopProduct.getSoldOut()!=1){
//				selectList.remove(shopMemberFavorite);
//			}
//		}
		
	}

	/**
	 * 获取会员收藏列表
	 * 
	 * @param memberId
	 * @param page
	 * @param appId
	 * @return
	 */
	public Map<String, Object> getMemberFavoriteList(Long memberId, Page<ShopMemberVisit> page, Long storeId,
			String appId) {
		if (memberId == null || memberId < 1) {
			throw new RuntimeException("会员信息有误,请确认");
		}
		Wrapper<ShopMemberFavorite> wrapper = new EntityWrapper<ShopMemberFavorite>().eq("member_id", memberId).eq("store_id", storeId).eq("status", 0).eq("type", 0).orderBy("create_time", false);
		List<ShopMemberFavorite> shopMemberFavoriteList = shopMemberFavoriteMapper.selectPage(page, wrapper);
		Map<String, Object> data = new HashMap<>(2);
		if (shopMemberFavoriteList.isEmpty()) {
			logger.info("没有收藏");
			data.put("isNoProduct", 0);
			data.put("text", "没有任何内容");
			return data;
		}

		List<Long> shopProductIds = new ArrayList<>(shopMemberFavoriteList.size());
		shopMemberFavoriteList.forEach(fav-> shopProductIds.add(fav.getRelatedId()));
		if (shopProductIds.isEmpty()) {
			logger.info("商品id");
			data.put("isNoProduct", 0);
			data.put("text", "没有任何内容");
            return data;
        }
		List<ShopProduct> shopProductList = shopProductMapper.listProductForFavoriteByIds(shopProductIds);
		if (shopProductList.isEmpty()) {
			logger.error("没有商品");
			data.put("isNoProduct", 0);
			data.put("text", "没有任何内容");
            return data;
        }

		//供应商平台商品id
		List<Long> supplierProductIds = new ArrayList<>();
		shopProductList.forEach(shopProduct -> {
			if (shopProduct != null && shopProduct.getOwn() == 0) {
				supplierProductIds.add(shopProduct.getProductId());
			}
		});

		//利用供应商商品id查询商品5图片
		if (! supplierProductIds.isEmpty()) {
			List<ProductNew> supplierProducts = shopProductMapper.listImgs(supplierProductIds);
			logger.info("查询供应商商品  size={}", supplierProducts.size());
			supplierProducts.forEach(splProduct->{
				for (ShopProduct shopProduct : shopProductList) {
					if (shopProduct.getOwn() == 0 && shopProduct.getProductId() - splProduct.getId() == 0) {
						shopProduct.setSummaryImages(splProduct.getDetailImages());
						break;
					}
				}
			});
		}

		//老版本兼容,大于0表示有商品
		data.put("isNoProduct", 1);

		List<Map<String, String>> productResList = new ArrayList<>(shopProductList.size());
		shopProductList.forEach(shopProduct -> {
			Map<String, String> product = new HashMap<>(6);
			product.put("productId", String.valueOf(shopProduct.getId()));
			product.put("name", shopProduct.getName());
			product.put("price", PriceUtil.getPriceString(shopProduct.getPrice()) + "");
			product.put("image", shopProduct.getFirstImage());

			if (shopProduct.getStockTime() == 0 || shopProduct.getTabType() == 1) {
				product.put("isStock", "0");
			} else {
				product.put("isStock", "1");
			}

			//状态:-1：删除，0：正常
			int status = shopProduct.getStatus();
			//上架状态：0草稿，1上架， 2下架
			int soldOut = shopProduct.getSoldOut();
			//商家商品状态:0已上架、1已下架、2已删除
			String shopProductState;
			if (status == -1) {
				shopProductState = "2";
			} else if (soldOut == ShopProduct.sold_out_up) {
				shopProductState = "0";
			} else {
				shopProductState = "1";
			}
			product.put("shopProductState", shopProductState);
			productResList.add(product);
		});
		data.put("productList", productResList);

		//老的
//		if (shopMemberFavoriteList.size() > 0) {
//			List<Map<String, String>> productList = new ArrayList<Map<String, String>>();
//			for (ShopMemberFavorite shopMemberFavorite : shopMemberFavoriteList) {
//				//填充商品信息
//				long shopProductId = shopMemberFavorite.getRelatedId();
////						ShopProduct shopProduct = shopProductService.selectById(shopProductId);
//				ShopProduct shopProduct = shopProductService.findProductForFavoriteById(shopProductId);
//				if (shopProduct == null) {
//					logger.info("收藏的商品为空，请排查问题,shopProductId:"+shopProductId);
//				}else{
//					Map<String, String> product = new HashMap<>();
//					product.put("productId", String.valueOf(shopProduct.getId()));
//					product.put("name", shopProduct.getName());
//
//					product.put("price", PriceUtil.getPriceString(shopProduct.getPrice()) + "");
//
//					product.put("image", shopProduct.getFirstImage());
//					if (shopProduct.getStockTime() == 0 || shopProduct.getTabType() == 1) {
//						product.put("isStock", "0");
//					} else {
//						product.put("isStock", "1");
//					}
//
//					product.put("shopProductState", shopProductService.getShopProductState(shopProductId));
//					productList.add(product);
//				}
//			}
//			data.put("productList", productList);
//			data.put("isNoProduct", 1);
//		} else {
//			data.put("isNoProduct", 0);
//			data.put("text", "没有任何内容");
//		}
		data.put("memberId", memberId);
		return data;
	}

	// /**
	// * 取消会员收藏记录
	// * @param id
	// * @return
	// */
	// public JsonResponse deleteMemberFavoriteRecord(Long id) {
	// JsonResponse jsonResponse = new JsonResponse();
	// try {
	// if(id==null || id<1){
	// return jsonResponse.setError("会员收藏记录有误,请重新再操作");
	// }
	// ShopMemberFavorite shopMemberFavorite = new ShopMemberFavorite();
	// shopMemberFavorite.setId(id);
	// shopMemberFavorite.setStatus(-1);
	// shopMemberFavorite.setUpdateTime(System.currentTimeMillis());
	// Integer result = shopMemberFavoriteMapper.updateById(shopMemberFavorite);
	// if(result!=1){
	// return jsonResponse.setError("会员收藏记录删除失败,请重新再操作");
	// }
	// return jsonResponse.setSuccessful();
	// } catch (Exception e) {
	// return jsonResponse.setError("会员收藏记录删除失败,请重新再操作");
	// }
	// }

}
