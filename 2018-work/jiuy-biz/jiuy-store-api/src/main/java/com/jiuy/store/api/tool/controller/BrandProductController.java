/**
 * 
 */
package com.jiuy.store.api.tool.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jiuyuan.service.common.monitor.IProductMonitorService;
import com.store.entity.DefaultStoreUserDetail;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baomidou.mybatisplus.plugins.Page;
import com.jiuyuan.entity.UserDetail;
import com.jiuyuan.entity.newentity.BrandNew;
import com.jiuyuan.entity.newentity.ProductNew;
import com.jiuyuan.entity.newentity.ShopProduct;
import com.jiuyuan.entity.newentity.StoreBusiness;
import com.jiuyuan.entity.newentity.UserNew;
import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.entity.query.PageQueryResult;
import com.jiuyuan.service.common.IBrandNewService;
import com.jiuyuan.service.common.IProductNewService;
import com.jiuyuan.service.common.IUserNewService;
import com.jiuyuan.service.common.ShopProductNewService;
import com.jiuyuan.web.help.JsonResponse;

import static com.jiuyuan.entity.newentity.StoreBusiness.defaultStoreBusiness;

/**
 * @author LWS
 *
 */
@Controller
@RequestMapping("/brand/product")
public class BrandProductController {
	private static final Logger logger = Logger.getLogger(BrandProductController.class);

	@Autowired
	private IProductNewService productNewService;
	// @Autowired
	// private IUserNewService userNewService;

	@Autowired
	private IBrandNewService brandNewService;

	@Autowired
	private ShopProductNewService shopProductNewService;
	@Autowired
	private IUserNewService userNewService;

	@Autowired
	private IProductMonitorService productMonitorService;

	/**
	 * 获取品牌首页商品列表
	 * 
	 * @param brandId
	 * @param type
	 *            1：推荐；2：销量；3：最新升序；4：最新降序；5：价格升序；6：价格降序；
	 * @param pageQuery
	 * @param userDetail
	 * @return
	 */
	@RequestMapping({"/homepageList/auth","/homepageList"})
	@ResponseBody
	@Cacheable("cache")
	public JsonResponse getBrandProductHomepageList(@RequestParam(value = "brandId") long brandId,
			@RequestParam(value = "type", required = false, defaultValue = "1") int type, // 1：推荐；2：销量；3：最新升序；4：最新降序；5：价格升序；6：价格降序；
			@RequestParam(value = "keyWord", required = false, defaultValue = "") String keyWord,
			// Page<ProductNew> page,
			PageQuery pageQuery, UserDetail<StoreBusiness> userDetail) {
		if (userDetail==null){
			userDetail = defaultStoreBusiness();
		}
		JsonResponse jsonResponse = new JsonResponse();
		try {
			long storeId = userDetail.getId();
			logger.info("获取品牌首页商品列表storeId:" + storeId + ";brandId:" + brandId + ";type:" + type);

			Map<String, Object> data = new HashMap<String, Object>();
			Page<ProductNew> page = new Page<ProductNew>(pageQuery.getPage(), pageQuery.getPageSize());
			List<ProductNew> prodcutList = productNewService.getBrandProductList(storeId, brandId, type, keyWord, page);
			List<Map<String, Object>> productListData = this.encapsulatedProductListDataData(prodcutList, storeId);

			productMonitorService.fillProductMonitor(productListData,"id");

			data.put("productListData", productListData);
			int productListCount = productNewService.getBrandProductListCount(storeId, brandId, type, keyWord);
			PageQueryResult pageQueryResult = PageQueryResult.copyFrom(pageQuery, productListCount);
			data.put("pageQuery", pageQueryResult);
			BrandNew brand = brandNewService.getBrandNew(brandId);
			if (brand == null) {
				logger.error("获取品牌首页商品列表有误没有该品牌brandId:" + brandId);
				throw new RuntimeException("获取品牌首页商品列表有误没有该品牌brandId:" + brandId);
			} else {
				String brandPromotionImg = brand.getBrandPromotionImg();
				if (StringUtils.isEmpty(brandPromotionImg)) {
					// 品牌列表头图
					data.put("supplierStoreImage", "");
				} else {
					// 品牌列表头图
					data.put("supplierStoreImage", brandPromotionImg);
				}
				// 品牌列表文字
				data.put("supplierStoreName", brand.getBrandName());
			}
			
			UserNew userNew = userNewService.getSupplierByBrandId(brandId);
			String campaignImage = userNew.getCampaignImage();
			data.put("campaignImage", campaignImage);
			
			//批发限制状态（即混批）（0不混批、1混批）
			int wholesaleLimitState = userNew.buildWholesaleLimitState();
			//批发限制提示文本1, 例子：10件、￥1000元 起订
			String wholesaleLimitTip1 = userNew.buildWholesaleLimitTip1();
			//批发限制提示文本2, 例子：全店满50件且满50.00元订单总价可混批采购、全店满50件可混批采购、全店满50.00元订单总价可混批采购
			String wholesaleLimitTip2 = userNew.buildWholesaleLimitTip2();
			data.put("wholesaleLimitState", wholesaleLimitState);
			data.put("wholesaleLimitTip1", wholesaleLimitTip1);
			data.put("wholesaleLimitTip2",wholesaleLimitTip2 );
			//根据用户权限显示商品信息
			Map<String, Object> checkBrowsePermission = new HashMap<String,Object>();
			if(storeId == 0){
				checkBrowsePermission = brandNewService.checkBrowsePermission("",brandId,data);
			}else{
				String phoneNumber = userDetail.getUserDetail().getPhoneNumber();
				checkBrowsePermission = brandNewService.checkBrowsePermission(phoneNumber,brandId,data);
			}
			return jsonResponse.setData(checkBrowsePermission).setSuccessful();
			
		} catch (Exception e) {
			logger.error("获取品牌首页商品列表有误:" + e.getMessage());
			return jsonResponse.setError("获取品牌首页商品列表有误");
		}
	}
	
	
	
	/**
	 * 封装品牌商品列表数据
	 * 
	 * @param prodcutList
	 * @param storeId
	 * @return
	 */
	private List<Map<String, Object>> encapsulatedProductListDataData(List<ProductNew> prodcutList, long storeId) {
		List<Map<String, Object>> productListData = new ArrayList<Map<String, Object>>();
		for (ProductNew productNew : prodcutList) {
			Map<String, Object> productNewData = new HashMap<String, Object>();
			productNewData.put("id", productNew.getId());// 商品ID
			productNewData.put("mainImg", productNew.getMainImg());// 商品主图
			productNewData.put("name", productNew.getName());// 商品名称
			productNewData.put("badgeImage", productNew.getBadgeImage());// 商品角标
			// 最大阶梯价
			productNewData.put("minLadderPrice", productNew.getMaxLadderPrice());// 商品价格
			// 商品橱窗视频
			productNewData.put("vedioMain", productNew.getVedioMain());
			productNewData.put("saleTotalCount",productNew.getSaleTotalCount());
			// UserNew userNew =
			// userNewService.getSupplierUserInfo(productNew.getSupplierId());
			// //起批件数
			// productNewData.put("wholesaleCount",
			// userNew.getWholesaleCount());//商品起批件数
			// 填充店家上传该商品数量
			long productId = productNew.getId();
			List<ShopProduct> shopProductList = shopProductNewService.selectList(storeId, productId);
			productNewData.put("uploadNum", shopProductList.size());
			productNewData.put("memberLevel",productNew.getMemberLevel());//会员商品等级
			productNewData.put("memberLadderPriceMin",productNew.getMemberLadderPriceMin());//会员商品最小价格
			productNewData.put("memberLadderPriceJson",productNew.getMemberLadderPriceJson());//会员商品阶梯价格
			productListData.add(productNewData);
		}
		return productListData;
	}
}