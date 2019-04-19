/**
 * 
 */
package com.jiuy.store.api.tool.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.admin.common.constant.factory.PageFactory;
import com.alibaba.fastjson.JSONObject;
import com.jiuy.rb.enums.ShopProductOwnEnum;
import com.jiuy.rb.model.product.ShopProductRb;
import com.jiuy.rb.model.product.ShopProductRbQuery;
import com.jiuy.rb.service.coupon.ICouponServerNew;

import com.baomidou.mybatisplus.plugins.Page;
import com.jiuy.rb.service.product.IShopProductService;
import com.jiuyuan.constant.SystemPlatform;
import com.jiuyuan.entity.newentity.*;
import com.jiuyuan.ext.spring.web.method.ClientIp;
import com.jiuyuan.service.common.*;
import com.jiuyuan.service.common.express.IExpressModelService;
import com.jiuyuan.service.common.monitor.IProductMonitorService;
import com.jiuyuan.service.common.IYjjMemberService;
import com.jiuyuan.util.*;
import com.store.entity.DefaultStoreUserDetail;
import com.store.entity.SalesVolumeProduct;
import com.util.LocalMapUtil;
import com.yujj.entity.product.YjjMember;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jiuyuan.constant.Constants;
import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.entity.ClientPlatform;
import com.jiuyuan.entity.Product;
import com.jiuyuan.entity.UserDetail;
import com.jiuyuan.entity.newentity.dynamicproperty.DynamicPropertyCategory;
import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.service.common.IActivityProductService;
import com.jiuyuan.service.common.IBrandNewService;
import com.jiuyuan.service.common.IDynamicPropertyCategoryService;
import com.jiuyuan.service.common.IDynamicPropertyProductService;
import com.jiuyuan.service.common.IProductNewService;
import com.jiuyuan.service.common.IStoreShareCommandRecordService;
import com.jiuyuan.service.common.IUserNewService;
import com.jiuyuan.util.QRCodeUtil;
import com.jiuyuan.util.ResultCodeException;
import com.jiuyuan.util.anno.Login;
import com.jiuyuan.web.help.JsonResponse;
import com.store.service.ProductFacadeShop;
import com.store.service.ProductServiceShop;

import static com.jiuyuan.entity.newentity.StoreBusiness.defaultStoreBusiness;

/**
 * @author LWS
 * @since 2015/07/31
 */
@Controller
@RequestMapping("/mobile/product")
public class MobileProductController {

	private static final Logger logger = Logger.getLogger(MobileProductController.class);
	
	private static final long THREE_DAY=3L*24*60*60*1000;
	@Autowired
	private IProductNewService productNewService;
	
	@Autowired
	private IActivityProductService activityProductService;
	
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private IBrandNewService brandNewService;
	@Autowired
	private ProductServiceShop productService;

	@Autowired
	private ProductFacadeShop productFacade;

	@Autowired
	private IYjjMemberService memberService;
	@Autowired
	private IStoreShareCommandRecordService storeShareCommandRecordService;
	
	@Autowired
	private IDynamicPropertyProductService dynamicPropertyPrpductService;
	@Autowired
	private IDynamicPropertyCategoryService dynamicPropertyCategoryService;
	@Autowired
	private IUserNewService userNewService;

	@Autowired
	private IExpressModelService expressModelService;

	@Autowired
	private IProductMonitorService productMonitorService;
	@Autowired
	private IProductSkuNewService productSkuNewService;
	@Autowired
	private IShopProductService shopProductService;
	@Autowired
	private ICouponServerNew couponServerNew;

	/**
     * 网页商品详情接口
     */
	@RequestMapping(value = "/productDetailOfWeb")
	@ResponseBody
    public JsonResponse productDetailOfWeb(long productId){
		JsonResponse jsonResponse = new JsonResponse();
	    try {
	    	ProductNew product = productNewService.getProductById(productId);
	    	ProductDetail productDetail = productNewService.getProductDatailByProductId(productId);
	    	
	    	//判断商品是否为空
	    	if (product == null) {
				throw new RuntimeException("未找到该商品");
			}
	    	if (productDetail == null) {
				throw new RuntimeException("未找到该商品详情");
			}
			logger.info("根据id获取产品详情，productNew："+JSON.toJSONString(product));
			Map<String,Object> data = new HashMap<String,Object>();
			data.put("productId", product.getId() );//商品ID
			data.put("productName", product.getName());//商品名称
			data.put("clothesNumber", product.getClothesNumber());//款号
			data.put("brandName", product.getBrandName());//品牌名称
			data.put("brandLogo", product.getBrandLogo());//品牌LOGO
			data.put("showcaseImgs", productDetail.getShowcaseImgs());//橱窗图集合
			data.put("videoUrl", productDetail.getVideoUrl());//视频URL
			data.put("detailImgs", productDetail.getDetailImgs());//商品详情图
	        return jsonResponse.setSuccessful().setData(data);
		} catch (Exception e) {
			e.printStackTrace();
			return jsonResponse.setError(e.getMessage());
		}
	}

	/**
	 * 根据产品id获取当前产品详情 示例：/mobile/product/test.json?req=12345678909876
	 * @param productId
	 * @return
	 */
	@RequestMapping(value = "/test")
	@ResponseBody
	public JsonResponse test(long productId, UserDetail userDetail) {
		JsonResponse jsonResponse = new JsonResponse();
		ProductNew product = productNewService.getProductById(productId);
		return jsonResponse.setSuccessful().setData(product);
	}

	/**
	 * 根据产品id获取当前产品详情 示例：/mobile/product/2.json?req=12345678909876
	 * 
	 * @param productId
	 * @return
	 */
	@RequestMapping(value = "/checkProductShowVipAuth/auth")
	@Login
	@ResponseBody
	public JsonResponse checkProductShowVipAuth(long productId, UserDetail<StoreBusiness> userDetail) {
		JsonResponse jsonResponse = new JsonResponse();
		try {
//			logger.info("检查商品VIP返回数据，productId："+productId);
			if(productId == 0){
					throw new RuntimeException("平台商品ID还不能为0");
			}
			StoreBusiness storeBusiness = userDetail.getUserDetail();
			Product product = productService.getProductById(productId);
			if (product == null) {
				logger.info("检查商品VIP获取平商品信息为空productId："+productId);
				return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_UNKNOWN);
			}
			long storeBusinessId = userDetail.getId();
			if (storeBusinessId == 0) {
				logger.info("商家信息不能为空，该接口需要登陆，请排除问题");
				return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_NOT_LOGGED_IN);
			}
//			logger.info("检查商品VIP返回数据，product.getVip()："+product.getVip()+",storeBusiness.getVip()："+storeBusiness.getVip());	
			boolean showAuth = checkProductShowVipAuth(storeBusiness.getVip(), product.getVip());
	
			Map<String, String> data = new HashMap<String, String>();
			data.put("showAuth", showAuth ? "1" : "0");// String 是否有权限查看 0无权限 1有权限
			return jsonResponse.setData(data).setSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
			return jsonResponse.setError(e.getMessage());
		}
	}

	private boolean checkProductShowVipAuth(int storeBusinessVip, int productVip) {
		// 检测VIP商品浏览权限（商品为VIP商品商家不是VIP商家则提示“您不是VIP客户”）
		boolean showAuth = true;
		if (productVip == 1 && storeBusinessVip != 1) {
			showAuth = false;
		}
		return showAuth;
	}
	/**
	 * 根据产品id获取当前产品详情 示例：/mobile/product/2.json?req=12345678909876
	 * 
	 * @param productId
	 * @return
	 */
	@RequestMapping({"/new/{productId}/auth","/new/{productId}"})
	@ResponseBody
	public JsonResponse getProduct18(@RequestParam(value="restrictionActivityProductId",required = false,defaultValue = "0") long restrictionActivityProductId,
			@RequestParam(value = "guide_flag", required = false, defaultValue = "0") int guideFlag,
			@PathVariable("productId") long productId, UserDetail<StoreBusiness> userDetail,
			ClientPlatform clientPlatform) {
		// logger.info("根据产品id获取当前产品详情productId:"+productId);
		if (userDetail==null){
			userDetail = defaultStoreBusiness();
		}
		JsonResponse jsonResponse = new JsonResponse();
		SalesVolumeProduct salesVolumeProduct = null;
		try {
			//如果限购活动商品id大于0，取限购活动商品id里面的商品id覆盖当前商品id
			RestrictionActivityProduct restrictionActivityProductInfo = null;
			if(restrictionActivityProductId>0){
				restrictionActivityProductInfo =
						activityProductService.getRestrictionActivityProductInfo(restrictionActivityProductId);
				productId = restrictionActivityProductInfo.getProductId();
				int productStatus = restrictionActivityProductInfo.getProductStatus();
				// 该活动商品已经下架
//				if (productStatus == 2) {
//					return jsonResponse.setResultCode(ResultCode.ORDER_ERROR_RESTRICTION_ACTIVITY_PRODUCT_IS_REMOVED);
//				}
				//该活动商品已经删除
				if (productStatus == 3) {
					return jsonResponse.setResultCode(ResultCode.ORDER_ERROR_RESTRICTION_ACTIVITY_PRODUCT_IS_DELETE);
				}
				salesVolumeProduct = productMonitorService.getByProductIdAndType(restrictionActivityProductId,2);
			} else {
				salesVolumeProduct = productMonitorService.getByProductIdAndType(productId,1);
			}
			// logger.info("根据产品id获取当前产品详情storeBusiness:"+storeBusiness);
			Product product = productService.getProductById(productId);
			if (product == null) {
				return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_UNKNOWN);
			}
			// logger.info("根据产品id获取当前产品详情product:"+product);
			// logger.info("大于2.0.0检查VIP，正常，clientPlatform："+JSONObject.toJSONString(clientPlatform));
			// 检测VIP商品浏览权限（商品为VIP商品商家不是VIP商家则提示“您不是VIP客户”）
			StoreBusiness storeBusiness = userDetail.getUserDetail();
			/*if (storeBusiness != null) {
				boolean showAuth = checkProductShowVipAuth(storeBusiness.getVip(), product.getVip());
				// logger.info("根据产品id获取当前产品详情showAuth:"+showAuth);
				if (!showAuth) {
					return jsonResponse.setResultCode(ResultCode.VIP_PRODYCT_SHOW_NO_AUTHORITY);
				}
			}*/
			Map<String, Object> data = productFacade.getProduct18(product, userDetail, guideFlag,restrictionActivityProductInfo);
			// 填充销量信息
			productMonitorService.fillProMonitorSingle(data,salesVolumeProduct,restrictionActivityProductId);
			// 获取最低运费
			BigDecimal miniExpress = expressModelService.mininExpress(product.getSupplierId());
			data.put("miniExpressMoney",miniExpress);

			try {
				// ProductFacade中ObjectMapper无法autowired，在此处理一下数据
				data.put("skuMap", objectMapper.writeValueAsString(data.get("skuMap")));
			} catch (JsonProcessingException e) {
				e.printStackTrace();
				logger.error("parsing sku map error!");
			}
			//判断是否显示产品参数入口
			if(dynamicPropertyPrpductService.show(productId)){
				data.put("haveDyna", 1);
			}else{
				data.put("haveDyna", 0);
			}
			//判断商品权限 UserNew:供应商用户
			UserNew userNew = userNewService.getSupplierByBrandId(product.getBrandId());
			
			int wholesaleLimitState = userNew.buildWholesaleLimitState();//批发限制（即混批）状态（0不混批、1混批）
			String wholesaleLimitTip1 = userNew.buildWholesaleLimitTip1();//批发限制提示文本1, 例子：10件、￥1000元 起订
			String wholesaleLimitTip2 = userNew.buildWholesaleLimitTip2();//批发限制提示文本2, 例子：全店满50件且满50.00元订单总价可混批采购、全店满50件可混批采购、全店满50.00元订单总价可混批采购
			data.put("wholesaleLimitState", wholesaleLimitState);
			data.put("wholesaleLimitTip1", wholesaleLimitTip1);
			data.put("wholesaleLimitTip2",wholesaleLimitTip2 );
			BrandNew brandNew = userNewService.getSupplierBrandInfo(product.getBrandId());
			data.put("brandType",brandNew.getBrandType() );//品牌类型：1：高档，2：中档
			
			String campaignImage = userNew.getCampaignImage();
			data.put("campaignImage", campaignImage);
			long storeId = userDetail.getId();
			Map<String, Object> checkBrowsePermission = new HashMap<String,Object>();
			if(storeId == 0){
				checkBrowsePermission = brandNewService.checkBrowsePermission("",product.getBrandId(),data);
			}else{
				String phoneNumber = userDetail.getUserDetail().getPhoneNumber();
				checkBrowsePermission = brandNewService.checkBrowsePermission(phoneNumber,product.getBrandId(),data);
			}
			return jsonResponse.setData(checkBrowsePermission).setSuccessful();
//			return jsonResponse.setData(data).setSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
			return jsonResponse.setError(e.getMessage());
		}
	}
	
	/**
	 * 获取分享口令
	 * @param
	 * @param productId
	 * @return
	 */
	@RequestMapping("/getShareCommand/auth")
	@ResponseBody
	public JsonResponse getShareCommand(@RequestParam("productId") long productId,
			                            UserDetail<StoreBusiness> userDetail){
		JsonResponse jsonResponse = new JsonResponse();
		long storeId = userDetail.getId();
		try {
			String data = storeShareCommandRecordService.getShareCommand(storeId,productId,StoreShareCommandRecord.VALID_TIME);
			return jsonResponse.setSuccessful().setData(data);
		}catch(ResultCodeException e){
			return jsonResponse.setResultCode(e.getCode(),e.getMessage());
		}catch (Exception e) {
			logger.info(e.getMessage());
			return jsonResponse.setError("服务器开小差了，请稍后再试");
		}
	}
	
	/**
	 * 解析分享口令
	 * @param key
	 * @return
	 */
    @RequestMapping({"/parseShareCommand","/parseShareCommand/auth"})
    @ResponseBody
    public JsonResponse parseShareCommand(@RequestParam("key") String key){
    	JsonResponse jsonResponse = new JsonResponse();
    	try {
    		Map<String,Object> data = storeShareCommandRecordService.parseShareCommand(key);
    		return jsonResponse.setSuccessful().setData(data);
		} catch(ResultCodeException e){
			return jsonResponse.setResultCode(e.getCode(),e.getMessage());
		}catch (Exception e) {
			logger.info(e.getMessage());
			return jsonResponse.setError("服务器开小差了，请稍后再试");
		}
    }
    /**
     * 插入打开者的日志
     * @param shareCommandId
     * @param userDetail
     * @return
     */
    @RequestMapping("/insertShareCommandRecord/auth")
    @ResponseBody
    public JsonResponse insertShareCommandRecord(@RequestParam("shareCommandId") long shareCommandId,
    		                                     UserDetail<StoreBusiness> userDetail){
    	JsonResponse jsonResponse = new JsonResponse();
    	long storeId = userDetail.getId();
    	try {
    		storeShareCommandRecordService.insertShareCommandLog(shareCommandId,storeId);			
    		return jsonResponse.setSuccessful();
		} catch(ResultCodeException e){
			return jsonResponse.setResultCode(e.getCode(),e.getMessage());
		} catch (Exception e) {
			logger.info(e.getMessage());
			return jsonResponse.setError("服务器开小差了，请稍后再试");
		}
    }
    


	@RequestMapping("/qrcode/src/auth")
	@ResponseBody
	public void qrcode(HttpServletResponse response, HttpServletRequest request, UserDetail userDetail,
			@RequestParam("product_id") long productId, @RequestParam("width") int width,
			@RequestParam("height") int height) {
		String content = Constants.SERVER_URL + "/static/app/ProductDetailsUser.html" + "?product_id=" + productId;
		if (userDetail.getId() > 0) {
			content += "&store_id=" + userDetail.getId();
		}
		QRCodeUtil.getFile(request, response, content, "IMG" + userDetail.getId() + "_" + productId, width, height);
	}

	/**
	 * 获取门店品牌列表和对应的商品列表
	 * @param type 0：热卖 1：推荐 2：最近浏览 3：采购记录
	 * @param searchBrand
	 * @param userDetail
	 * @param pageQuery
	 * @return
	 */
	@RequestMapping(value = "/brand/listback/auth", method = RequestMethod.GET)
	@ResponseBody
	@Cacheable("cache")
	public JsonResponse getProductList(
			@RequestParam(value="type", required=false, defaultValue = "0") int type,//0:销量倒序;1:权重倒序;2:最近浏览历史倒序;3:最近购买时间倒序
			@RequestParam(value = "search_brand", required = false) String searchBrand,
			@RequestParam(value = "brandType", required = false, defaultValue = "-1") int brandType,  //品牌类型：1(高档)，2(中档)
			UserDetail<StoreBusiness> userDetail, PageQuery pageQuery,
			HttpServletRequest request) {
		JsonResponse jsonResponse = new JsonResponse();

		Map<String,Object> date = new HashMap<>();
		date.put("brandList",productFacade.brandList(searchBrand, userDetail, pageQuery, type,brandType, true));
		return jsonResponse.setData(date).setSuccessful();
	}


	/**
	 * 获取门店品牌列表和对应的商品列表
	 * @param type 0：热卖 1：推荐 2：最近浏览 3：采购记录
	 * @param searchBrand
	 * @param userDetail
	 * @param pageQuery
	 * @return
	 */
    @RequestMapping({"/brand/list/auth","/brand/list"})
    @ResponseBody
	@Cacheable("cache")
    public JsonResponse getProductList(
    		@RequestParam(value="type", required=false, defaultValue = "0") int type,//0:销量倒序;1:权重倒序;2:最近浏览历史倒序;3:最近购买时间倒序
    		@RequestParam(value = "search_brand", required = false) String searchBrand,
    		@RequestParam(value = "brandType", required = false, defaultValue = "-1") int brandType,  //品牌类型：1(高档)，2(中档)
    		UserDetail<StoreBusiness> userDetail, PageQuery pageQuery) {
		if (userDetail==null){
			userDetail = defaultStoreBusiness();
		}

    	JsonResponse jsonResponse = new JsonResponse();
		Map<String, Object> data = productFacade.getBrandList(searchBrand, userDetail, pageQuery, type,brandType, true);

		couponServerNew.hasCoupon(data);

    	return jsonResponse.setData(data).setSuccessful();
    }



	/**
	 * 商品列表
	 *
	 * @param userDetail userDetail
	 * @param userOwn userOwn 是否用户可拥有
	 * @param queryCondition 查询条件
	 * @return com.jiuyuan.web.help.JsonResponse
	 * @author Charlie
	 * @date 2018/9/7 15:55
	 */
	@RequestMapping( value = "/productList/auth" )
	@ResponseBody
	@Login
	@Cacheable("cache")
	public JsonResponse productList(UserDetail<StoreBusiness> userDetail,
									@RequestParam(value = "userOwn", defaultValue = "false", required = false) Boolean userOwn,
									ProductNew queryCondition
	) {
		JsonResponse response = JsonResponse.getInstance ();
		Long storeId = userDetail.getId ();
		List<Long> productIdList = null;
		if (userOwn != null && userOwn && storeId > 0)  {
			YjjMember validMember = memberService.findValidMemberByUserId (SystemPlatform.STORE, storeId);
			queryCondition.setMemberLevel (validMember == null ? 0 : validMember.getMemberLevel ());
			//已有的供应商同款不展示
			ShopProductRbQuery shopProductRbQuery = new ShopProductRbQuery ();
			shopProductRbQuery.setStoreId (storeId);
			shopProductRbQuery.setStatus (0);
			shopProductRbQuery.setOwn (ShopProductOwnEnum.SELF_SAMPLE_STYLE.getCode ());
			List<ShopProductRb> shopProductRbList = shopProductService.selectList (shopProductRbQuery);
			if (!shopProductRbList.isEmpty ()) {
				productIdList = new ArrayList<> (shopProductRbList.size ());
				for (ShopProductRb shopProductRb : shopProductRbList) {
					if (shopProductRb.getProductId () != null) {
						productIdList.add (shopProductRb.getProductId ());
					}
				}
			}
		}
		Page<Map<String, Object>> page = new PageFactory<Map<String, Object>> ().defaultPage();
		List<ProductNew> productList = productService.productList (page, queryCondition, productIdList);
		List<Map<String, Object>> result = new ArrayList<> (productList.size ());
		for (ProductNew product : productList) {
			Map<String, Object> productMap = new HashMap<> ();
			productMap.put ("id", product.getId ());
			productMap.put ("name", product.getName ());
			productMap.put ("clothesNumber", product.getClothesNumber ());
			productMap.put ("firstDetailImage", product.getFirstDetailImage ());
			result.add (productMap);
		}
		page.setRecords (result);
		return response.setSuccessful ().setData (page);
	}

	/**
	 * SKU信息
	 *
	 * @param productId 商品id
	 * @param ownType sku类别 1:品牌商sku,2:门店sku 3小程序
	 * @param status '状态:-3废弃，-2停用，-1下架，0正常，1定时上架' 字符串拼接
	 * @return com.jiuyuan.web.help.JsonResponse
	 * @author Charlie
	 * @date 2018/9/7 15:55
	 */
	@RequestMapping( value = "/listSkuByProduct" )
	@ResponseBody
	@Login
	public JsonResponse listSkuByProduct(Long productId, Integer ownType,
									@RequestParam(value = "status", required = false, defaultValue = "0") String status) {
		JsonResponse response = JsonResponse.getInstance ();
		List<ProductSkuNew> skuList = productSkuNewService.listSkuByProduct (productId, ownType, status);
		return response.setSuccessful ().setData (skuList);
	}





	/**
	 * 将品牌列表刷到redis缓存中
	 *
	 * @param token
	 * @param type 0：热卖 1：推荐
	 * @param pageQuery 查询分页参数
	 * @param seconds 秒 0永久存活
	 * @return com.jiuyuan.web.help.JsonResponse
	 * @author Charlie(唐静)
	 * @date 2018/6/22 18:49
	 */
	@RequestMapping( "brushBrandListCache" )
	@ResponseBody
	public JsonResponse brushBrandListCache(String token, Integer type, PageQuery pageQuery, int seconds) {
		try {
			return JsonResponse.getInstance().setSuccessful().setData(productFacade.brushBrandListCache(token, type, pageQuery, seconds));
		} catch (Exception e) {
			return BizUtil.exceptionHandler(e);
		}
	}



    /**
     * 获取商品属性详情
     * @param productId
     * @return
     */
    @RequestMapping("/getProductDynaProp")
	@ResponseBody
	public JsonResponse getProductDynaProp(@RequestParam("productId") long productId) {
    	JsonResponse jsonResponse = new JsonResponse();
    	try {
    		List<Map<String,Object>> resultList=new ArrayList<>();
    		
    		List<Map<String,Object>> list = dynamicPropertyPrpductService.getDynaPropAndValue(productId);
    		ProductNew product = productNewService.getProductById(productId);
    		Long categoryId = product.getThreeCategoryId();
    		Wrapper<DynamicPropertyCategory> wrapper = new EntityWrapper<DynamicPropertyCategory>()
    				.eq("cate_id",categoryId)
    				.eq("status", DynamicPropertyCategory.DYNA_PROP_CATEGORY_STATUS_ON).orderBy("weight", true);
			List<DynamicPropertyCategory> dynamicPropertyCategoryList = dynamicPropertyCategoryService.selectList(wrapper );
			if (dynamicPropertyCategoryList.size()>0) {
				for (DynamicPropertyCategory dynamicPropertyCategory : dynamicPropertyCategoryList) {
					for (Map<String,Object> dynamicPropertyPrpduct : list) {
						if (dynamicPropertyCategory.getDynaPropId()==dynamicPropertyPrpduct.get("dynaPropId")) {
							Map<String,Object> map= new HashMap<>();
							map.put("dynaPropId", dynamicPropertyPrpduct.get("dynaPropId"));
							map.put("dynaPropName", dynamicPropertyPrpduct.get("dynaPropName"));
							map.put("value", dynamicPropertyPrpduct.get("value"));
							resultList.add(map);
						}
					}
				}
			}
    		return jsonResponse.setData(resultList).setSuccessful();
		} catch (Exception e) {
			return jsonResponse.setError("查看商品动态属性参数e:"+e.getMessage());
		}
    }


	/**
	 * 活动专场页
	 *
	 * @param current 分页
	 * @param size 分页
	 * @return com.jiuyuan.web.help.JsonResponse
	 * @author Charlie
	 * @date 2018/8/13 16:02
	 */
	@RequestMapping( {"/memberProductSpecial","/memberProductSpecial/auth"} )
	@ResponseBody
	public JsonResponse memberProductSpecial(	@RequestParam(value="current",required=false,defaultValue="1")Integer current, @RequestParam(value="size",required=false,defaultValue="10")Integer size) {
		JsonResponse jsonResponse = JsonResponse.getInstance ();
		try {
			Page<ProductNew> page = new Page<> (current, size);
			List<ProductNew> productList = productNewService.memberProductSpecial (page);
			SmallPage result = new SmallPage (page);
			result.setRecords (productList);
			return jsonResponse.setData(result).setSuccessful();
		} catch (Exception e) {
			return jsonResponse.setError("查看商品动态属性参数e:"+e.getMessage());
		}
	}


	/**
	 *会员特价商品  根据产品id获取当前产品详情
	 * @param productId 产品id
	 * @param userDetail 用户
	 * @param ip ip
	 * @return com.jiuyuan.web.help.JsonResponse
	 * @author hyf
	 * @date 2018/8/14 11:55
	 */
	@RequestMapping(value = "/member/{productId}/auth", method = RequestMethod.GET)
	@ResponseBody
	public JsonResponse getMemberProduct(@PathVariable("productId") long productId, UserDetail userDetail, @ClientIp String ip,
										 @RequestParam(value = "guide_flag", required = false, defaultValue = "0") int guideFlag) {

		return getProduct18(0,guideFlag,productId,userDetail,null);
//		JsonResponse jsonResponse = new JsonResponse();
//		Long userId = userDetail.getId();
//
//		// get product main information
//		Product product = productService.getProductById(productId);
//		if (product == null) {
//			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_UNKNOWN);
//		}
//		if (product.getMemberLevel()==0){
//			logger.info("普通商品");
//			return jsonResponse.setError("非会员商品");
//		}
//		YjjMember yjjMember = null;
//		if (userId!=0){
//			yjjMember = yjjMemberService.findValidMemberByUserId (SystemPlatform.STORE, userId);
//		}
//
//		Integer memberLevel = 0;
//		if (yjjMember!=null){
//			memberLevel = yjjMember.getMemberLevel();
//		}
////		Map<String, Object> data = new HashMap<String, Object>();
//		RestrictionActivityProduct restrictionActivityProduct = null;
//		Map<String, Object> data = productFacade.getProduct18(product, userDetail, guideFlag,restrictionActivityProduct);
//		try {
//			// ProductFacade中ObjectMapper无法autowired，在此处理一下数据
//			data.put("skuMap", objectMapper.writeValueAsString(data.get("skuMap")));
//		} catch (JsonProcessingException e) {
//			e.printStackTrace();
//			logger.error("parsing sku map error!");
//		}
//		//会员等级
//		data.put("member",memberLevel);
//		//阶梯价格
//		data.put("ladderPriceJson",product.getLadderPriceJson());
//		//会员阶梯价格
//		data.put("memberLadderPriceJson",product.getMemberLadderPriceJson());
//		return jsonResponse.setData(data).setSuccessful();
	}


	/**
	 * 用于删除 会员存储状态
	 * @param storeId
	 * @return
	 */
	@RequestMapping("clear")
	@ResponseBody
	public JsonResponse clearMember(Long storeId){
		logger.info("会员状态清除="+storeId);
		LocalMapUtil.invalidate(String.format("member:%d",storeId));

		String type = null;
		type = LocalMapUtil.get(String.format("member:%d",storeId));
		logger.info("会员状态="+type);
		return JsonResponse.getInstance().setSuccessful();
	}




}
