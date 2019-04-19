package com.store.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.toolkit.StringUtils;
import com.jiuyuan.constant.GlobalSettingName;
import com.jiuyuan.service.common.IMyStoreActivityService;
import com.jiuyuan.service.common.ShopGlobalSettingService;
import com.store.entity.ShopCategory;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;

@Service
public class ShopProductFacade {
	
	private static final Log logger = LogFactory.get("ShopProductFacade");
    
    @Autowired
    private ShopProductService shopProductService;
    @Autowired
    private ShopGlobalSettingService globalSettingService;
    @Autowired
    private ShopCategoryService shopCategoryService;
    

	@Autowired
	private IMyStoreActivityService myStoreActivityService;
    
    public Map<String, Object> getUpdPrdocut(long shopProductId, long storeId) {
    	logger.info("ShopProductFacade编辑商品数据回显getUpdPrdocut:shopProductId-"+shopProductId+",storeId-"+storeId);
    	
    	//商品当前参与活动：0未参与活动、1表示参与团购活动、2表示参与秒杀活动
    	int intoActivity = myStoreActivityService.getShopProductActivityState(shopProductId,storeId);
    	if(intoActivity != 0){
    		throw new RuntimeException("不可操作！该商品正在活动中");
    	}
    			
		Map<String,Object> data = new HashMap<String,Object>();
		Map<String, Object> shopProduct = null;
		if(shopProductId>0){
			shopProduct = shopProductService.toUpdate(shopProductId);
		}
		data.put("shopProduct", shopProduct);
		
		String ratio = globalSettingService.getSetting(GlobalSettingName.OWN_PLATFORM_PRODUCT_RATIO);
		int ratioCount = 0;
		String[] split = null;
		if(!(StringUtils.isEmpty(ratio))){
			split = ratio.split(":");
			double ratioDouble = Integer.parseInt(split[0])/Integer.parseInt(split[1]);
			int platformProductCount = shopProductService.getPlatformProductCount(storeId);
			int ownProductCount = shopProductService.getOwnProductCount(storeId);
			ratioCount = (int) (platformProductCount*ratioDouble-ownProductCount);
		}
		if(ratioCount>0){
			data.put("text1", "");
			data.put("text2", "");
			data.put("text3", "");

		}else{
			data.put("text1", "剩余可发布商品数为0！");
			data.put("text2", "您可通过上传平台商品，获得更多的可发布商品数量。");
			if(split.length>=2){
				data.put("text3", "每上传"+split[1]+"个平台商品，可获得"+split[0]+"个可发布商品数。");
			}else{
				data.put("text3", "");
			}
		}

		data.put("ratioCount", 10000);
		
		List<ShopCategory> parentCategories = shopCategoryService.getParentCategories();
		List<Object> parentCategoriesList = new ArrayList<Object>();
		for (ShopCategory shopCategory : parentCategories) {
			List<Object> parentCategory = new ArrayList<Object>();
			parentCategory.add(shopCategory.getId());
			parentCategory.add(shopCategory.getCategoryName());
			List<ShopCategory> childCategories = shopCategoryService.getChildCategoryByParentId(shopCategory.getId());
			List<Object> childCategoriesList = new ArrayList<Object>();
			for (ShopCategory shopCategory2 : childCategories) {
				List<String> childCategoriy = new ArrayList<String>();
				childCategoriy.add(shopCategory2.getId()+"");
				childCategoriy.add(shopCategory2.getCategoryName());
				childCategoriesList.add(childCategoriy);
			}
			parentCategory.add(childCategoriesList);
			parentCategoriesList.add(parentCategory);
//			shopCategory.setChildCategories(childCategories);
		}
		data.put("categories", parentCategoriesList);
		return data;
	}


	/**
	 * 商品是否有参加活动
	 *
	 * @param shopProductId shopProductId
	 * @param storeId storeId
	 * @return boolean
	 * @author Charlie
	 * @date 2018/9/8 21:42
	 */
	public boolean isJoinAcitivity(Long shopProductId, long storeId) {
		return myStoreActivityService.getShopProductActivityState(shopProductId,storeId) != 0;
	}
}