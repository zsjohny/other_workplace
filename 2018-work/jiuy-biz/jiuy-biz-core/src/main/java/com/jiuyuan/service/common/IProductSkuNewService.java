package com.jiuyuan.service.common;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.plugins.Page;

import com.jiuyuan.entity.newentity.ProductNew;
import com.jiuyuan.entity.newentity.ProductSkuNew;

public interface IProductSkuNewService {
	public void updSkuRemainCount(long productId, long skuId,int remainCount,Double weight) ;

	List<ProductSkuNew> getValidSkuListByProductId(long productId);

	/* (non-Javadoc)
	 * @see com.jiuyuan.service.common.IProductNewService#insertProductSku(long, com.jiuyuan.entity.newentity.ProductNew, long, long, java.lang.String, java.lang.String, int, long, long)
	 */
	void insertProductSku(long productId, ProductNew product, long sizeId, long colorId, String sizeName,
			String colorName, int remainCount, long supplierId, long lowarehouseId,Double weight);

	/**
	 * 只获取可用sku，封装map list
	 * @return
	 */
	List<Map<String, Object>> buildSkuListMap(long productId);

	ProductSkuNew buildProductSku(long productId, ProductNew product, long sizeId, long colorId, String sizeName,
			String colorName, int remainCount, long supplierId, long lowarehouseId);

	public Map<String, Object> getSkuList(Long id);

	public void editProductSkuClothesNumber(long productId, String clothesNumber);

	public List<ProductSkuNew> getSkuListByProductIds(List<String> productIdList);
	public List<Map<String,String>> getSkuMapListByProductIds(List<String> productIdList);

	/**
	 * 获取sku信息查询列表
	 *
	 * @param page 分页参数
	 * @param startTime 查询条件, 查询上架时间在startTime之后
	 * @param endTime 查询条件, 查询上架时间在endTime之前
	 * @return java.lang.Object
	 * @auther Charlie(唐静)
	 * @date 2018/6/5 17:09
	 */
	List<Map<String, Object>> findSkuHistory(Page<Map<String,Object>> page, Long startTime, Long endTime, Long auditStartTime, Long auditEndTime);


	/**
	 * 编辑 定时修改库存任务
	 * @param supplierId 供应商id
	 * @param query
	 *      ProductSkuNew#Id    skuId
	 *      ProductSkuNew#timingSetRemainCountTime 定时更新库存时间
	 *      ProductSkuNew#timingSetType 定时更新库存类型, 0关闭(不更新), 1指定日期更新 ,2上架后N天更新
	 *      ProductSkuNew#timingSetCount 定时更新库存数
	 * @return void
	 * @auther Charlie(唐静)
	 * @date 2018/6/8 9:33
	 */
    List<Map<String, Object>> timingSetRemainCountUpd(List<ProductSkuNew> query, Long supplierId);


	/**
	 * 首次上架后, 启动上架后定时修改库存任务
	 *
	 * @param product
	 * @param skus    product的sku列表,从数据库取出的sku
	 * @return void
	 * @auther Charlie(唐静)
	 * @date 2018/6/10 8:14
	 */
	void startTimingSetRemainCountAfterOnShelves(ProductNew product, Collection<ProductSkuNew> skus, Long supplierId);


	/**
	 * 更新sku库存
	 * @param skuId skuId
	 * @param supplierId 供应商id
	 * @param count 更新的数量
	 * @return void
	 * @auther Charlie(唐静)
	 * @date 2018/6/10 15:24
	 */
    void updateRemainCount(Long skuId, Long supplierId, Integer count, String token);


	/**
	 * 根据商品查询sku
	 *
	 * @param productId productId
	 * @param ownType ownType
	 * @param status '状态:-3废弃，-2停用，-1下架，0正常，1定时上架' 字符串拼接
	 * @return java.util.List<com.jiuyuan.entity.newentity.ProductSkuNew>
	 * @author Charlie
	 * @date 2018/9/7 17:44
	 */
    List<ProductSkuNew> listSkuByProduct(Long productId, Integer ownType, String status);
}