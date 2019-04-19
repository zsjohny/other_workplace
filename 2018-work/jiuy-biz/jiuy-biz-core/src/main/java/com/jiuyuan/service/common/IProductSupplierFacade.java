package com.jiuyuan.service.common;

import java.util.Map;

import com.jiuyuan.entity.Product;
import com.jiuyuan.entity.newentity.ProductNew;
import org.springframework.transaction.annotation.Transactional;

public interface IProductSupplierFacade {

	/**
	 * 编辑商品SKU信息（编辑第二步）
	 * @param productId
	 * @param supplierId
	 * @param updSkuListJson 编辑商品SKU列表JSON（skuId、库存数量）
	 * @param addSkuListJson 添加商品SKU列表JSON（库存数量、颜色值、颜色组Id、尺码值、尺码组Id），说明：如果颜色值不存在则会新增
	 */
	ProductNew updProductSkuInfo(long productId, long supplierId, long lowarehouseId, String updSkuListJson,
								 String addSkuListJson);

	/**
	 * 编辑或新建基本信息商品（编辑第一步）
	 * long productId,// 商品ID，新建时为0
			long supplierId,//供应商ID
	 		long oneCategoryId,// 所属一级分类ID
			String oneCategoryName,//所属一级分类名称
			long twoCategoryId,//所属二级分类ID
			String twoCategoryName,//所属二级分类名称
			long threeCategoryId,//所属三级分类ID
			String threeCategoryName,//所属三级分类名称
			String name,//商品名称
			String mainImg,//商品主图
			String showcaseImgs,//商品橱窗图
			String clothesNumber, //商品款号 拼接上改品牌的款号前缀
			String ladderPriceJson 
			long lOWarehouseId主仓库ID
	 		String vedioMain 主橱窗视频地址
	 * @return
	 */
	long updProductBasicInfo(long productId, long supplierId, long oneCategoryId, String oneCategoryName,
			long twoCategoryId, String twoCategoryName, long threeCategoryId, String threeCategoryName, String name,
			String mainImg, String showcaseImgs, String clothesNumber, String ladderPriceJson, long lOWarehouseId,String vedioMain,Integer express);


	/**
	 * @param productId 商品ID
	 * @param togetherProducts 搭配推荐商品Id集合，英文逗号分隔
	 * @param detailImgs 商品详情图集合JSON
	 * @param videoUrl 商品视频
	 * @param putawayType 上架类型 1:审核通过后立即上架, 2:定时上架, 3:暂不上架
	 * @param timingPutwayTime 定时上架时间
	 */
	void updProductDetailInfo(long productId, String togetherProducts, String detailImgs, String videoUrl,
			String videoName, long videoFileId, int putawayType, long timingPutwayTime);

	/**
	 * 获取颜色列表和尺码列表
	 * 说明，
	 * @return
	 */
	Map<String, Object> getColorListAndSizeList(long supplierId);


	/**
	 * 商品手动上架
	 *
	 * @param productId 待上架商品id
	 * @param supplierId 供应商id
	 * @return void
	 * @auther Charlie(唐静)
	 * @date 2018/6/14 15:57
	 */
	void manualOnShelvesV375(Long productId, Long supplierId);
}