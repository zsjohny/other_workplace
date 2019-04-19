package com.store.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.jiuyuan.entity.newentity.ShopProduct;
import com.jiuyuan.util.PriceUtil;
import com.store.dao.mapper.WxaMemberVisitMapper;
import com.store.entity.MiniappMemberVisit;
import com.store.entity.ShopMemberVisit;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;

/**
* @author QiuYuefan
* @version 创建时间: 2017年6月19日 14:24:27
*/

@Service
public class WxaMemberVisitService{
	 private static final Log logger = LogFactory.get();

	@Autowired
	private WxaMemberVisitMapper shopMemberVisitMapper;
	
	@Autowired
	private ShopProductService shopProductService;
	
	/**
	 * 添加或者修改会员访问记录
	 * @param memberId
	 * @param productId
	 * @param type
	 * @param appId 
	 * @return
	 */
	public void addUpdateMemberVisitRecord(Long memberId, Long shopProductId, Integer type,Long storeId, String appId) {
		if(memberId==null || memberId<1){
			throw new RuntimeException("会员信息有误,请确认");
		}
//		Wrapper<ShopMemberVisit> wrapper = new EntityWrapper<ShopMemberVisit>().eq("member_id", memberId).eq("related_id ", productId).eq("type", type)
//				.eq("app_id", appId).eq("store_id", storeId);
		//1、添加自己记录
//		ShopMemberVisit entity = new ShopMemberVisit();
//		entity.setMemberId(memberId);
//		entity.setRelatedId(shopProductId);
//		entity.setType(type);
//		entity.setStoreId(storeId);
//		ShopMemberVisit shopMemberVisit = shopMemberVisitMapper.selectOne(entity);
		Wrapper<ShopMemberVisit> wrapper = new EntityWrapper<ShopMemberVisit>().eq("member_id", memberId).eq("related_id", shopProductId).eq("type", type).eq("store_id", storeId);
		List<ShopMemberVisit> shopMemberVisitList = shopMemberVisitMapper.selectList(wrapper);
		if(shopMemberVisitList.size()>1){
			throw new RuntimeException("该商品存在多条记录");
		}
		int record = 0;
		if(shopMemberVisitList.size()<1){//添加足迹记录
			ShopMemberVisit visit = new ShopMemberVisit();
			visit.setCreateTime(System.currentTimeMillis());
			visit.setMemberId(memberId);
			visit.setRelatedId(shopProductId);
			visit.setType(type);
			visit.setMemberId(memberId);
			visit.setStoreId(storeId);
			visit.setRelatedId(shopProductId);
			visit.setType(type);
			visit.setAppId(appId);
			visit.setCount(1);
			record = shopMemberVisitMapper.insert(visit);
		}else{//修改自己数量
			ShopMemberVisit shopMemberVisit = shopMemberVisitList.get(0);
//			ShopMemberVisit visit = new ShopMemberVisit();
//			int count = shopMemberVisit.getCount();
//			visit.setCount(count+1);
//			visit.setId(shopMemberVisit.getId());
//			record = shopMemberVisitMapper.updateById(visit);
			record = shopMemberVisitMapper.increaseShopMemberVisitCount(shopMemberVisit.getId());
		}
		if(record<=0){
			throw new RuntimeException("添加或者修改会员访问记录有误");
		}
		
		//2、增加商品浏览量productId
		shopProductService.increaseShowCount(shopProductId);
		
		
	}

	/**
	 * 获取会员访问列表
	 * @param memberId
	 * @param page
	 * @param appId 
	 * @return
	 */
	public Map<String,Object> getMemberVisitList(Long memberId,Page<MiniappMemberVisit> page,Long storeId, String appId) {
		if(memberId==null || memberId<1){
			throw new RuntimeException("会员信息有误,请确认");
		}
		Wrapper<ShopMemberVisit> wrapper = new EntityWrapper<ShopMemberVisit>().eq("member_id", memberId).eq("store_id", storeId)
				.eq("app_id", appId).orderBy("create_time",false);
		List<ShopMemberVisit> shopMemberVisitList = shopMemberVisitMapper.selectPage(page, wrapper);
		Map<String,Object> data = new HashMap<String,Object>();
		if(shopMemberVisitList.size()>0){
			List<Map<String,String>> productList = new ArrayList<Map<String,String>>();
			for (ShopMemberVisit shopMemberVisit : shopMemberVisitList) {
				if(shopMemberVisit.getType()==0){
					//填充商品信息
					long shopProductId = shopMemberVisit.getRelatedId();
					ShopProduct shopProduct = shopProductService.getShopProductInfoNoStock(shopProductId);
					if(shopProduct==null || shopProduct.getStatus()==-1 || shopProduct.getSoldOut()!=1){
						continue;
					}
					Map<String,String> product = new HashMap<String,String>();
					product.put("productId",shopProduct.getId()+"");
					product.put("name", shopProduct.getName());
					
//					String priceNew = "null";
//					Double priceOld = shopProduct.getPrice();
//					if(priceOld!=null && !("".equals(priceOld))){
//						priceNew = String.format("%.2f",priceOld);
//					}
					product.put("price",PriceUtil.getPriceString(shopProduct.getPrice())+"");
					
					product.put("image", shopProduct.getFirstImage());
					if(shopProduct.getStockTime()==0 || shopProduct.getTabType()==1){
						product.put("isStock", "0");
					}else{
						product.put("isStock", "1");
					}
					
					
					product.put("shopProductState", shopProductService.getShopProductState(shopProductId));
					productList.add(product);
				}
			}
			data.put("productList", productList);
			data.put("isNoProduct", 1);
		}else{
			data.put("isNoProduct", 0);
			data.put("text", "没有任何内容");
		}
		data.put("memberId", memberId);
		
		return data;
	}

	/**
	 * 删除会员访问记录
	 * @param id
	 * @return
	 */
	public void deleteMemberVisitRecord(Long id) {
		if(id==null || id<1){
			throw new RuntimeException("会员访问记录有误,请重新再操作");
		}
		Integer result = shopMemberVisitMapper.deleteById(id);
		if(result!=1){
			throw new RuntimeException("会员访问记录删除失败,请重新再操作");
		}
	}
	
}
