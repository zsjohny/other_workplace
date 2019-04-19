package com.jiuyuan.dao.mapper.supplier;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.plugins.Page;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.jiuyuan.dao.annotation.DBMaster;
import com.jiuyuan.entity.newentity.ProductSkuNew;


@DBMaster
public interface ProductSkuNewMapper extends BaseMapper<ProductSkuNew> {
	/**
	 * 减少库存
	 * @param skuId
	 * @param buyCount
	 */
	public void reduceRemainCount(@Param("skuId") long skuId,@Param("buyCount") int buyCount);
//	  int insertProductSkuBatch(@Param("skus") List<ProductSkuNew> skus);
    /**
     * 获取该商品的所有SKU
     * @param productId
     * @return
     */
	public List<ProductSkuNew> getAllProductSKUsOfProduct(@Param("productId") long productId);
	
	/**
	 * 获取该商品的sku
	 * @param productIdList
	 * @return
	 */
	public List<Map<String, String>> getSkuMapListByProductIds(@Param("productIdList") Collection<String> productIdList);


	/**
	 * 获取小程序商品的sku
	 * @param productIdList
	 * @return
	 */
	public List<Map<String, String>> getSkuMapListByWxaProductIds(@Param("productId") Long productId);

	/**
	 * 查询sku报表总数
	 * <p>
	 * 上架时间在 startTime, 和endTime之间的sku报表信息
	 *
	 * @param startTime
	 * @param endTime
	 * @return int
	 * @auther Charlie(唐静)
	 * @date 2018/6/5 17:34
	 */
    int countSkuHistory(
			@Param("page") Page<Map<String,Object>> page,
			@Param("startTime") Long startTime,
			@Param("endTime")Long endTime,
			@Param("auditStartTime") Long auditStartTime,
			@Param("auditEndTime")Long auditEndTime
	);

	/**
	 * 获取sku信息统计列表
	 * <p>
	 * 上架时间在 startTime, 和endTime之间的sku报表信息
	 *
	 * @param page 分页参数
	 * @param startTime 查询过滤条件 上架时间 > startTime
	 * @param endTime 查询过滤条件 上架时间 < endTime
	 * @return int
	 * @auther Charlie(唐静)
	 * @date 2018/6/5 17:34
	 */
	List<Map<String,Object>> findSkuHistory(
			@Param("page") Page<Map<String,Object>> page,
			@Param("startTime") Long startTime,
			@Param("endTime")Long endTime,
			@Param("auditStartTime") Long auditStartTime,
			@Param("auditEndTime")Long auditEndTime
	);


	/**
	 * 根据供应商id, skuId 查询sku
	 * <p>
	 *     可以达到校验功能, 只查询供应商自己的sku
	 * @param supplierId
	 * @param skuIds
	 * @return java.util.List<com.jiuyuan.entity.newentity.ProductSkuNew>
	 * @auther Charlie(唐静)
	 * @date 2018/6/8 10:05
	 */
    List<ProductSkuNew> findSkuBySupplierAndSkuIds(@Param("supplierId")Long supplierId, @Param("skuIds")Collection<Long> skuIds);

	/**
	 * 查询尺寸颜色库存
	 * @param skuId
	 * @return
	 */
    ProductSkuNew findColorSizeRemainById(@Param("skuId") String skuId);

	/**
	 * 根据sku查询 库存
	 * @param skuId
	 * @return
	 */
	Integer findRemainCountById(@Param("skuId") long skuId);
	/**
	 * 平台商品的sku 颜色,尺码,库存信息(根据日期查询)
	 *
	 * @param productId productId
	 * @return java.util.List<com.jiuyuan.entity.newentity.ProductSkuNew>
	 * @author Charlie
	 * @date 2019/1/2 20:40
	 */
    List<ProductSkuNew> getSkuColorSizeAndCountByProductId(@Param("productId") Long productId, @Param("current") long current);

	/**
	 * 小程序商品的sku 颜色,尺码,库存信息
	 *
	 * @param wxaProductId wxaProductId
	 * @return java.util.List<com.jiuyuan.entity.newentity.ProductSkuNew>
	 * @author Charlie
	 * @date 2019/1/2 20:40
	 */
	List<ProductSkuNew> listSkuColorSizeAndCountByWxaProductId(@Param("wxaProductId") Long wxaProductId);

	/**
	 * 平台商品的sku 颜色,尺码,库存信息
	 *
	 * @param productId productId
	 * @return java.util.List<com.jiuyuan.entity.newentity.ProductSkuNew>
	 * @author Charlie
	 * @date 2019/1/2 20:40
	 */
	List<ProductSkuNew> listSkuColorSizeAndCountByProductId(@Param("productId") Long productId);
}
