package com.jiuyuan.service.common;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.plugins.Page;
import com.jiuyuan.entity.newentity.RestrictionActivityProduct;
import com.jiuyuan.entity.newentity.RestrictionActivityProductSku;

public interface IActivityProductService {

	List<RestrictionActivityProduct> getAllActivityProductList(long restrictionActivityProductId,
			String restrictionActivityProductName, String clothesNumber, int productStatus,
			int restrictionActivityStatus, int remainCountMin, int remainCountMax, long activityProductShelfTimeBegin,
			long activityProductShelfTimeEnd, double activityProductPriceMin, double activityProductPriceMax,
			int saleCountMin, int saleCountMax, Page<Map<String, Object>> page);

	List<RestrictionActivityProductSku> getAllActivityProductSkuList(long restrictionActivityProductId);

	int getActivityProductListAllCount();

	int getActivityProductListProcessingCount();

	int getActivityProductListSoldOutCount();

	int shelfActivityProduct(long restrictionActivityProductId);

	int deleteActivityProduct(long restrictionActivityProductId);

	int soldOutActivityProduct(long restrictionActivityProductId);

	RestrictionActivityProduct getActivityProductById(long restrictionActivityProductId);

	/**
	 * 保存一个活动商品
	 * update by Charlie(唐静) 2018-5-28 10:54:33 : version 3.7.5 新增字段 "miniPurchaseCount"
	 * @param productIds
	 * @param restrictionActivityProductId
	 * @param promotionImage
	 * @param activityProductName
	 * @param activityProductPrice
	 * @param activityPricePercentage
	 * @param productPrice
	 * @param skuInfo
	 * @param activityBeginTime
	 * @param activityEndTime
	 * @param restrictionCount 最大购买量/限购量
	 * @param miniPurchaseCount 最小购买量/起订量
	 * @return int
	 * @auther Charlie(唐静)
	 * @date 2018/5/28 10:52
	 */
	int saveActivityProductInfo(String productIds, long restrictionActivityProductId, String promotionImage, String activityProductName,
			double activityProductPrice, int activityPricePercentage, double productPrice, String skuInfo, long activityBeginTime, long activityEndTime,
			int restrictionCount, int miniPurchaseCount);

	Map<String, Object> getNewActivityProductInfo(String clothesNumbers);

	void updateActivityProductSkuRemainCount(long restrictionActivityProductSkuId, int remainCount);

	List<RestrictionActivityProduct> getRestrictionActivityProductListList(int activityStatus, Page page);

	RestrictionActivityProduct getRestrictionActivityProductInfo(long activityProductId);

	void saveActivityTitle(String activityTitle);

	String getActivityTitle();

	/**
	 * 保存多个活动商品
	 * update by Charlie(唐静) 2018-5-28 10:54:33 : version 3.7.5 新增字段 "miniPurchaseCount"
	 * @param productIdArr 商品数组
	 * @param activityProductPrice
	 * @param activityPricePercentage
	 * @param activityBeginTime
	 * @param activityEndTime
	 * @param restrictionCount 最大购买量/限购量
	 * @return int
	 * @auther Charlie(唐静)
	 * @date 2018/5/28 10:52
	 */
	int saveActivityProducts(String[] productIdArr, double activityProductPrice, int activityPricePercentage,
			long activityBeginTime, long activityEndTime, int restrictionCount);

}