package com.jiuy.store.tool.controller.mobile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;


import com.jiuy.base.exception.BizException;
import com.jiuy.base.util.Biz;
import com.jiuy.rb.model.product.ShopProductRb;
import com.jiuy.rb.model.product.ShopProductRbQuery;
import com.jiuy.rb.service.product.IShopProductService;
import com.jiuyuan.util.anno.Login;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.baomidou.mybatisplus.plugins.Page;
import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.constant.ThirdPartService;
import com.jiuyuan.entity.ClientPlatform;
import com.jiuyuan.entity.UserDetail;
import com.jiuyuan.entity.newentity.ShopProduct;
import com.jiuyuan.entity.newentity.StoreBusiness;
import com.jiuyuan.ext.spring.web.method.ClientIp;
import com.jiuyuan.service.common.IProductNewService;
import com.jiuyuan.service.common.ShopProductNewService;
import com.jiuyuan.util.TipsMessageException;
import com.jiuyuan.util.VideoSignatureUtil;
import com.jiuyuan.web.help.JsonResponse;
import com.store.service.ShopProductFacade;
import com.store.service.ShopProductService;
import com.store.service.StoreWxaService;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;
import com.yujj.util.file.OSSFileUtil;

import javax.annotation.Resource;


/**
* 商品Controller
* @author Qiuyuefan
*/
@Controller
@RequestMapping("/product")
public class ShopProductController {

	private static final Log logger = LogFactory.get("ShopProductController");
	
	private final String DEFAULT_BASEPATH_NAME = ThirdPartService.OSS_DEFAULT_BASEPATH_NAME;

	
    
    @Autowired
    private ShopProductService shopProductService;
    
    @Autowired
    private ShopProductFacade shopProductFacade;
    
    @Autowired
    private IProductNewService productNewService;
    
    @Autowired
    private ShopProductNewService shopProductNewService;
    
    @Autowired
    private OSSFileUtil ossFileUtil;
    
    @Autowired
 	private StoreWxaService storeWxaService;
    
    @Resource( name = "shopProductServiceRb" )
	private IShopProductService shopProductRbService;
 
	
	
	
	
    /**
     * 商品列表(筛选)接口
     * @return
     */
    @RequestMapping("/getListaa/auth")
    @ResponseBody
    public JsonResponse getListaa(@RequestParam("tabType") Integer tabType,
    									@RequestParam(value="sortType", required=false, defaultValue = "0") Integer sortType,
    									@RequestParam(value="isOrder", required=false, defaultValue = "1") Integer isOrder,
    									@RequestParam(value="own", required=false, defaultValue = "0") Integer own,
								        @RequestParam(value="owns", required=false, defaultValue = "") String owns,
								        @RequestParam(value="isStock", required=false, defaultValue = "0") Integer isStock,
    									@RequestParam(value="keyWords",required=false,defaultValue = "") String keyWords,
    									@RequestParam("current")int current,
    									@RequestParam("size")int size,
			UserDetail<StoreBusiness> userDetail) {
		JsonResponse jsonResponse = new JsonResponse();
		long storeId = userDetail.getId();
   	 	if(storeId == 0){
   	 		logger.info("商家信息不能为空，该接口需要登陆，请排除问题");
   	 		return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_NOT_LOGGED_IN);
   	 	}
		try {
			Map<String,Object> data = shopProductService.getShopProductList(tabType,sortType,new Page<ShopProduct>(current,size),userDetail.getUserDetail(),isOrder,own,isStock,keyWords,0, owns);
			return jsonResponse.setSuccessful().setData(data);
		} catch (Exception e) {
			e.printStackTrace();
			return jsonResponse.setError(e.getMessage());
		}
    } 
   
    /**
     * 从2.2版本开始的新商品列表接口
     * 获取商品列表
     * /product/productlist.json?tabType=0&sortType=0&current=1&size=10
     * http://dev.yujiejie.com:32080/product/productList.json?tabType=0&sortType=1&current=1&size=10&storeId=1
     * tabType  状态:1：在售商品、2：已下架、0：草稿 默认是1：在售商品(上架)
     * isOrder  是否已下单 ：1：未下单、0：已下单 默认是1：未下单
     * own  是否是自有商品：1：是自有商品，0：平台商品 默认是0：平台商品
     * isStock 是否有现货：0：无现货、1：有现货 默认0：无现货
     * sortType   排序方式：0：上架时间(最新)、1：想要、2：浏览量(热度) 默认0：上架时间(最新)
     * keyWords  关键字搜索 默认无
     * tabType  状态:1:在售商品,2:已下架,0:草稿’
     * isOrder   是否下单 ：0已下单、1未下单
     * own  是否是自有商品 1自有商品  0平台商品
     * isStock 是否有现货  0无现货  1有现货
     * sortType   排序方式 ：0上架时间、1想要、浏览量
	 * owns '是否是自有商品：0平台供应商商品, 1是用户自定义款，2用户自营平台同款'
     */
    @RequestMapping("/productList/auth")
    @ResponseBody
    public JsonResponse productList(@RequestParam("tabType") Integer tabType,
    									@RequestParam(value="sortType", required=false, defaultValue = "0") Integer sortType,
    									@RequestParam(value="isOrder", required=false, defaultValue = "1") Integer isOrder,
    									@RequestParam(value="own", required=false, defaultValue = "0") Integer own,
    									@RequestParam(value="owns", required=false, defaultValue = "") String owns,
    									@RequestParam(value="isStock", required=false, defaultValue = "0") Integer isStock,
    									@RequestParam(value="keyWords",required=false,defaultValue = "") String keyWords,
    									@RequestParam(value="tagId",required=false,defaultValue = "0") long tagId,
    									@RequestParam("current")int current,
    									@RequestParam("size")int size,
			UserDetail<StoreBusiness> userDetail) {
		JsonResponse jsonResponse = new JsonResponse();
		long storeId = userDetail.getId();
   	 	if(storeId == 0){
   	 		logger.info("商家信息不能为空，该接口需要登陆，请排除问题");
   	 		return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_NOT_LOGGED_IN);
   	 	}
		try {
			logger.info("商品列表接口入参，：tabType："+tabType+",sortType:"+sortType+",isOrder:"+isOrder+",own:"+own+",isStock"+isStock+",keyWords:"+keyWords+",current:"+current+",size:"+size);
			Map<String,Object> data = shopProductService.getShopProductList(tabType,sortType,new Page<ShopProduct>(current,size),userDetail.getUserDetail(),isOrder,own,isStock,keyWords,tagId,owns);
//			logger.info("商品列表接口返回数据。data："+JSON.toJSONString(data));
			return jsonResponse.setSuccessful().setData(data);
		} catch (Exception e) {
			e.printStackTrace();
			return new JsonResponse().setError(e.getMessage());
		}
    } 
    
    /**
     * 修改商家自定义描述
     */
    @RequestMapping("/updateShopOwnDetail/auth")
    @ResponseBody
    public JsonResponse updateShopOwnDetail(@RequestParam(required = true) String shopOwnDetail,
    		@RequestParam(value="name",required = false,defaultValue="") String name,
    		@RequestParam(required = true) long shopProductId,
    		UserDetail userDetail,@ClientIp String ip, ClientPlatform client) {
    	JsonResponse jsonResponse = new JsonResponse();
    	
    	long storeId = userDetail.getId();
   	 	if(storeId == 0){
   	 		logger.info("商家信息不能为空，该接口需要登陆，请排除问题");
   	 		return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_NOT_LOGGED_IN);
   	 	}
   	 	
    	shopProductService.updateShopOwnDetail(shopOwnDetail,name,shopProductId);
    	
    	return jsonResponse.setSuccessful();
    } 
    
    /**
     * 获取商家商品自定义描述
     */
    @RequestMapping("/getShopOwnDetail/auth")
    @ResponseBody
    public JsonResponse getShopOwnDetail(@RequestParam(required = true) long shopProductId,
    		UserDetail userDetail,@ClientIp String ip, ClientPlatform client) {
    	JsonResponse jsonResponse = new JsonResponse();
    	long storeId = userDetail.getId();
   	 	if(storeId == 0){
   	 		logger.info("商家信息不能为空，该接口需要登陆，请排除问题");
   	 		return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_NOT_LOGGED_IN);
   	 	}
   	 	ShopProduct shopProduct = shopProductService.selectById(shopProductId);
   	 	if(shopProduct == null){
   	 	logger.info("商家商品为空请排查问题，shopProductId："+shopProductId+",storeId:"+storeId);
	 		return jsonResponse.setError("商家商品为空");
   	 	}
    	
    	Map<String,String> data = new HashMap<String,String>();
    	data.put("shopProductId", String.valueOf(shopProduct.getId()));
    	data.put("shopOwnDetail", shopProduct.getShopOwnDetail());
    	return jsonResponse.setSuccessful().setData(data);
    } 
    
  
    
   
    
    /**
     * 商品推荐/取消推荐
     * @param shopProductId
     * @param isTop
     * @return
     */
    @RequestMapping("/updatetop/auth")
    @ResponseBody
    public JsonResponse updateTop(@RequestParam("id")Long shopProductId,@RequestParam("isTop")Integer isTop,@ClientIp String ip, ClientPlatform client
    		,UserDetail userDetail) {
		JsonResponse jsonResponse = new JsonResponse();
		try {
			shopProductService.updateTop(shopProductId,isTop,ip,client,userDetail.getId());
			return jsonResponse.setSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
			return new JsonResponse().setError(e.getMessage());
		}
    } 
    
    /**
     * 商品设为现货/取消现货
     * @param shopProductId
     * @param isStock
     * @return
     */
    @RequestMapping("/updatestock/auth")
    @ResponseBody
    public JsonResponse updateStock(@RequestParam("id")Long shopProductId,@RequestParam("isStock")Integer isStock,@ClientIp String ip, ClientPlatform client
    		,UserDetail userDetail) {
		JsonResponse jsonResponse = new JsonResponse();
		try {
			shopProductService.updateStock(shopProductId,isStock,ip,client,userDetail.getId());
			return jsonResponse.setSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
			return new JsonResponse().setError(e.getMessage());
		}
    } 
    
    /**
     * 商品上架/下架
     * @param shopProductId
     * @param soldOut
     * @return
     */
    @RequestMapping("/updatesoldout/auth")
    @ResponseBody
    public JsonResponse updateSoldOut(@RequestParam("id")Long shopProductId,@RequestParam("soldOut")Integer soldOut,@ClientIp String ip, ClientPlatform client
    		,UserDetail userDetail) {
		JsonResponse jsonResponse = new JsonResponse();
		try {
			shopProductService.updateSoldOut(shopProductId,soldOut,ip,client,userDetail.getId());
			return jsonResponse.setSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
			return new JsonResponse().setError(e.getMessage());
		}
    } 
    
    /**
     * 商品改零售价
     * @param shopProductId
     * @param price
     * @return
     */
    @RequestMapping("/updateprice/auth")
    @ResponseBody
    public JsonResponse updatePrice(@RequestParam("id")Long shopProductId,
    		@RequestParam("price")Double price,@ClientIp String ip, ClientPlatform client
    		,UserDetail userDetail) {
		JsonResponse jsonResponse = new JsonResponse();
		try {
			shopProductService.updatePrice(shopProductId,price,ip,client,userDetail.getId());
			return jsonResponse.setSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
			return new JsonResponse().setError(e.getMessage());
		}
    } 
    
    /**
     * 商品删除
     * @param shopProductId
     * @return
     */
    @RequestMapping("/delete/auth")
    @ResponseBody
    public JsonResponse delete(@RequestParam("id")Long shopProductId,@ClientIp String ip, ClientPlatform client,UserDetail userDetail) {
		JsonResponse jsonResponse = new JsonResponse();
		try {
			shopProductService.delete(shopProductId,ip,client,userDetail.getId());
			return jsonResponse.setSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
			return new JsonResponse().setError(e.getMessage());
		}
    } 
    
    /**
     * 编辑前获取数据并回显
     * @param shopProductId
     * @return
	 * @see  ShopProductController#shopProductDetail(java.lang.Long, com.jiuyuan.entity.UserDetail, java.lang.Integer)
     */
    @RequestMapping("/toupdate/auth")
    @ResponseBody
	@Deprecated
    public JsonResponse getUpdPrdocut(@RequestParam(value="shopProductId",required=false,defaultValue="0")Long shopProductId,UserDetail userDetail) {
    	JsonResponse jsonResponse = new JsonResponse();
    	Map<String,Object> data = shopProductFacade.getUpdPrdocut(shopProductId,userDetail.getId());
    	return jsonResponse.setSuccessful().setData(data);
    } 


	/**
     * 编辑前获取数据并回显
	 *
	 * @param productId 可以是小程序商品id,也可以是供应商商品id
	 * @param userDetail userDetail
	 * @return com.jiuyuan.web.help.JsonResponse
	 * @author Charlie
	 * @date 2018/9/8 21:39
	 */
    @RequestMapping("/shopProductDetail/auth")
    @ResponseBody
	@Login
    public JsonResponse shopProductDetail(Long productId,UserDetail userDetail, @RequestParam(value = "type", defaultValue = "1", required = false) Integer type) {
    	JsonResponse jsonResponse = JsonResponse.getInstance ();
		ShopProductRbQuery shopProductRbQuery = null;
		try {
			if (type == 1) {
				//查询小程序商品
				//IOS兼容
				if (productId == null || productId == 0) {
					return jsonResponse.setSuccessful ().setData (null);
				}

				boolean isJoin = shopProductFacade.isJoinAcitivity(productId,userDetail.getId());
				if (isJoin) {
					return jsonResponse.setError ("该商品正在参加活动,不允许编辑");
				}
				shopProductRbQuery = shopProductRbService.queryProductDetail (productId,userDetail.getId());
			}
			else if (type == 2){
				//利用供应商商品组装小程序商品格式的vo
				shopProductRbQuery = shopProductRbService.supplierProduct2ShopProduct (productId);
			}
		} catch (Exception e) {
			e.printStackTrace ();
			if (e instanceof BizException) {
				return jsonResponse.setError (((BizException) e).getMsg ());
			}
			else {
                throw e;
            }
		}
		return jsonResponse.setSuccessful().setData(shopProductRbQuery);
    }





//    @RequestMapping("/joker")
//    @ResponseBody
//    public JsonResponse a(ModelMap modelMap,HttpServletRequest request, HttpServletResponse response){
//    	try {
//    		Map<String,Object> data = WxaqrCodeImageUtil.getWxaqrCodeImage(new String[0]);
//			MultipartFile file = (MultipartFile) data.get("multipartFile");
//				String path = null;
//				String oldPath = request.getParameter("oldPath");
////				String needWaterMark = request.getParameter("need_water_mark");
//				Map<String, String> result = new HashMap<String, String>();
//				
////					if (request instanceof MultipartHttpServletRequest) {
//						logger.debug("yes you are!");
////						MultipartHttpServletRequest multiservlet = (MultipartHttpServletRequest) request;
////						MultipartFile file = multiservlet.getFile("file");
//						if (file == null) {
//							logger.error("请求中没有file对象，请排查问题" );
//							logger.error("request file null oldPath:" + oldPath);
//							return new JsonResponse();
//						}
//						logger.debug("request file name :" + file.getName());
//						path = ossFileUtil.uploadFile(DEFAULT_BASEPATH_NAME, file);
//						
//						//覆盖旧路径则删除
//						if(StringUtils.isNotEmpty(StringUtils.trim(oldPath))){
//							String key = oldPath.split("/")[oldPath.split("/").length - 1];
//							ossFileUtil.removeFile(DEFAULT_BASEPATH_NAME, key);
//						}
////					} else {
////						logger.debug("no wrong request!");
////					}
//					modelMap.addAttribute("images", path);
//					result.put("filename", path);
//					logger.info("上传文件接口返回数据，result:"+result.toString());
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return new JsonResponse();
//    }
    
    
    /**
     * 新建或者编辑商品时保存商品
     * @param id
     * @param name
     * @param price
     * @param clothesNumber
     * @param isStock
     * @param categoryId
     * @param summaryImages
     * @param shopOwnDetail
     * @param soldOut
     * @param videoDisplayUrl
     * @param videoFileId
     * @param videoImage
     * @param ip
     * @param tagIds 标签的属性值id,字符串以逗号拼接
	 * @param colorId 颜色的属性值id
	 * @param sizeId  尺码的属性值id
     */
    @RequestMapping("/saveproduct/auth")
    @ResponseBody
	@Login
    public JsonResponse saveProduct(
    		@RequestParam(value="id",required=false,defaultValue="0")Long id,
    		@RequestParam(value="name",required=false,defaultValue="")String name,
    		@RequestParam(value="price",required=false,defaultValue="0")Double price,
    		@RequestParam(value="clothesNumber",required=false,defaultValue="")String clothesNumber,
    		@RequestParam(value="isStock",required=false,defaultValue="0")Integer isStock,
    		@RequestParam(value="categoryId",required=false,defaultValue="0")Long categoryId,
    		@RequestParam(value="summaryImages",required=false,defaultValue="")String summaryImages,
    		@RequestParam(value="shopOwnDetail",required = true,defaultValue="") String shopOwnDetail,
    		@RequestParam(value="soldOut")Integer soldOut,
    		@RequestParam(value="videoUrl",required = true,defaultValue="")String videoDisplayUrl,
    		@RequestParam(value="videoFileId",required = true,defaultValue="0")Long videoFileId,
    		@RequestParam(value="videoImage",required = true,defaultValue="")String videoImage,@ClientIp String ip,
    		@RequestParam(value = "tagIds", required = false, defaultValue="")String tagIds,
    		@RequestParam(value = "colorId", required = false, defaultValue= "0" )Long colorId,
    		@RequestParam(value = "sizeId", required = false, defaultValue= "0" )Long sizeId,
    		@RequestParam(value = "skuJsonArray", required = false, defaultValue= "" )String skuJsonArray,
    		@RequestParam(value = "own", required = false, defaultValue= "0" )Integer own,//
    		ClientPlatform client,UserDetail userDetail) {
		JsonResponse jsonResponse = new JsonResponse();
    	try {
    		long storeId = userDetail.getId();
    		Map<String, Object> data = new HashMap<String, Object>();
    		logger.info("新建或者编辑商品时保存商品:saveproduct:id-"+id+",name-"+name+",price-"+price+",clothesNumber-"+clothesNumber
    				+",isStock-"+isStock+",categoryId-"+categoryId+",summaryImages-"+summaryImages
    				+",shopOwnDetail-"+shopOwnDetail+",status-"+soldOut);
        	long shopProductId =  shopProductService.save(id,name,price,clothesNumber
					,categoryId,isStock,summaryImages,shopOwnDetail,soldOut,videoDisplayUrl
					,videoFileId, videoImage,ip,client,storeId,tagIds,colorId, sizeId);
        	data.put("id", String.valueOf(shopProductId));
			return jsonResponse.setSuccessful().setData(data);
		} catch (Exception e) {
			logger.error("新建或者编辑商品时保存商品:错误:"+e.getMessage());
			e.printStackTrace();
			return jsonResponse.setError(e.getMessage());
		}
    	
    }



	/**
	 * 新建或者编辑商品时保存商品
	 *
	 * @param id 商品id, 没有是是新增
	 * @param name 商品名称
	 * @param price 商品价格
	 * @param clothesNumber 款号
	 * @param tagIds 标签(必填)
	 * @param skuJsonArray sku对象json
	 * @param summaryImages 主图最多4张
	 * @param shopOwnDetail 商家自定义描述
	 * @param videoImage 商品橱窗视频显示时候使用的图片
	 * @param videoUrl 商品橱窗视频url
	 * @param videoFileId 商品橱窗视频fileId
	 * @param own 是否是自有商品：0平台供应商商品, 1是用户自定义款，2用户自营平台同款
	 * @param soldOut 0：草稿，1：上架， 2：下架
	 * @param userDetail userDetail
	 * @return com.jiuyuan.web.help.JsonResponse
	 * @author Charlie
	 * @date 2018/9/6 9:48
	 */
    @RequestMapping("/addEditShopProduct/auth")
    @ResponseBody
	@Login
    public JsonResponse addEditShopProduct(Long id, String name, Double price, String clothesNumber, String tagIds,
			String skuJsonArray, String summaryImages, String shopOwnDetail, String videoImage, String videoUrl,
			Long videoFileId, Integer own, Integer soldOut, /*String sizeIds, String colorIds,*/
			Long productId, UserDetail userDetail
	) {
		JsonResponse jsonResponse = JsonResponse.getInstance ();
    	try {
    		Map<String, Object> data = new HashMap<> (2);
			ShopProductRbQuery param = ShopProductRbQuery.me (
					id, name, price, clothesNumber, summaryImages,
					shopOwnDetail, soldOut, videoUrl, videoFileId, videoImage,
					tagIds, skuJsonArray, own, userDetail.getId(), null,
					null, productId
			);
			if (!Biz.isEmpty (param.getId ()) && param.getId () > 0) {
				//编辑
				shopProductRbService.editShopProduct (param);
				data.put ("id", param.getId ());
			}
			else {
				//新增
				ShopProductRb shopProduct = shopProductRbService.createShopProduct (param);
				data.put ("id", shopProduct.getId ());
			}
			return jsonResponse.setSuccessful().setData(data);
		} catch (Exception e) {
			logger.error("新建或者编辑商品时保存商品:错误:"+e.getMessage());
			e.printStackTrace();
			if (e instanceof BizException) {
				return jsonResponse.setError (((BizException) e).getMsg ());
			}
			return jsonResponse.setError(e.getMessage());
		}

    }



	/**
     * 搭配商品列表
     */
    @RequestMapping("/collocationList/auth")
    @ResponseBody
    public JsonResponse collocationList(@RequestParam("productIds") String productIds,
    		                            UserDetail<StoreBusiness> userDetail){
    	JsonResponse jsonResponse = JsonResponse.getInstance ();
    	long storeId = userDetail.getId();
   	 	if(storeId == 0){
   	 		logger.info("商家信息不能为空，该接口需要登陆，请排除问题");
   	 		return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_NOT_LOGGED_IN);
   	 	}
   	 	try {
   	 		List<Map<String,Object>> list = productNewService.collocationList(productIds,storeId);
   	 		return jsonResponse.setSuccessful().setData(list);
		} catch (TipsMessageException e) {
			logger.info(e.getFriendlyMsg());
			return jsonResponse.setError(e.getFriendlyMsg());
		} catch (Exception e) {
			logger.info(e.getMessage());
			return jsonResponse.setError(ResultCode.SYSTEM_OPERATION_ERROR.getDesc());
		}
    }
    
//    /**
//     * 获取sku选择列表
//     */
//    @RequestMapping("/getSkuList")
//    @ResponseBody
//    public JsonResponse getSkuList(@RequestParam("productId") long productId,
//    		                       UserDetail<StoreBusiness> userDetail){
//    	JsonResponse jsonResponse = new JsonResponse();
//    	long storeId = userDetail.getId();
//   	 	if(storeId == 0){
//   	 		logger.info("商家信息不能为空，该接口需要登陆，请排除问题");
//   	 		return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_NOT_LOGGED_IN);
//   	 	}
//   	    Map<String,Object> data = productNewService.getSkuList(productId);
//    	return jsonResponse.setSuccessful().setData(data);
//    }
    
    /**
     * 查询商品详情
     * @param id
     * @return
     */
    @RequestMapping("/productitem/auth")
    @ResponseBody
    public JsonResponse getProductItem(@RequestParam(value="id")Long id,UserDetail userDetail) {
    	JsonResponse jsonResponse = new JsonResponse();
    	try {
    		Map<String,Object> data = shopProductService.getProductItem(id,userDetail.getId());
    		return jsonResponse.setSuccessful().setData(data);
		} catch (Exception e) {
			e.printStackTrace();
			return new JsonResponse().setError(e.getMessage());
		}
    }
    
    /**
     * 商品上传
     * @param
     * @return
     */
    @RequestMapping("/uploadproduct/auth")
    @ResponseBody
    public JsonResponse uploadProduct(@RequestParam(value="productId")Long productId,
    		@RequestParam(value="price",required=false)Double price,
    		@RequestParam(value="shopProductName",required=false,defaultValue="")String shopProductName,
    		UserDetail userDetail,@ClientIp String ip, ClientPlatform client) {
    	JsonResponse jsonResponse = new JsonResponse();
    	try { 
    		logger.info("商品上传productId:"+productId+",price:"+price+",shopProductName:"+shopProductName);
    		shopProductService.uploadProduct(productId,userDetail.getId(),ip,client,price,shopProductName);
    		return jsonResponse.setSuccessful();
		} catch (Exception e) {
			logger.warn("商品上传:"+e.getMessage());
			return new JsonResponse().setError(e.getMessage());
		}
    }
    
    /**
	 * 获取上传视频签名
	 * @return
	 */
    @RequestMapping(value = "/getsign")
    @ResponseBody
    public JsonResponse getsign() {
    	JsonResponse jsonResponse = new JsonResponse();

    	VideoSignatureUtil sign = new VideoSignatureUtil();
        sign.m_strSecId = "AKIDtb6MN980dz5uTl6X5MN8cqnTt3xb99OO";
        sign.m_strSecKey = "tYx72iAMJ0RfdMp2ye4E9pFQM3VhjW3J";
        sign.m_qwNowTime = System.currentTimeMillis() / 1000;
        sign.m_iRandom = new Random().nextInt(java.lang.Integer.MAX_VALUE);
        sign.m_iSignValidDuration = 3600 * 24 * 2;

    	Map<String,Object> data = new HashMap<String,Object>();
    	data.put("sign", sign.getUploadSignature());
        return jsonResponse.setSuccessful().setData(data);
    }
	
    /**
     * 删除视频
     * @param fileId
     * @param priority
     * @return
     */
    @RequestMapping(value = "/deletevideo")
    @ResponseBody
    public JsonResponse deleteVideo(@RequestParam("shopProductId")long shopProductId,@RequestParam("fileId")long fileId,
    		@RequestParam(value="priority",required=false,defaultValue="1")int priority) {
    	JsonResponse jsonResponse = new JsonResponse();
    	try {
    		
    		logger.info("删除视频失败:shopProductId:"+shopProductId+";fileId:"+fileId+";priority:"+priority);
        	shopProductService.deleteVideo(shopProductId,fileId,priority);
            return jsonResponse.setSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("删除视频失败"+e.getMessage());
	        return jsonResponse.setError("删除视频失败"+e.getMessage());
		}
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
//===================================================================================================================================================================
//    /**
//     * 将分享二维码上传到阿里云服务器
//     * @param file
//     * @param modelMap
//     * @param request
//     * @return
//     */
//    private String uploadWxaqrcode(MultipartFile file,HttpServletRequest request){
//    	try {
//    		String path = null;
//			String oldPath = request.getParameter("oldPath");
//			Map<String, String> result = new HashMap<String, String>();
//			logger.debug("yes you are!");
//			if (file == null) {
//				logger.error("请求中没有file对象，请排查问题" );
//				logger.error("request file null oldPath:" + oldPath);
//				return "";
//			}
//			logger.debug("request file name :" + file.getName());
//			path = ossFileUtil.uploadFile(DEFAULT_BASEPATH_NAME, file);
//			
//			//覆盖旧路径则删除
//			if(StringUtils.isNotEmpty(StringUtils.trim(oldPath))){
//				String key = oldPath.split("/")[oldPath.split("/").length - 1];
//				ossFileUtil.removeFile(DEFAULT_BASEPATH_NAME, key);
//			}
//			logger.info("上传文件接口返回数据，result:"+result.toString());
//			return path;
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return "";
//    }
    
    
    
    
    
    
    
//==========一下为废弃接口==========================================================================================================================================================
    /**
     * 2.2版本之前的商品列表接口，从2.2版本之后不再使用
     * 获取商品列表
     * /product/productlist.json?tabType=0&sortType=0&current=1&size=10
     * http://dev.yujiejie.com:32080/product/productlist.json?tabType=0&sortType=1&current=1&size=10&storeId=1
     * tabType  状态:’0：店主精选，1：买手推荐，2：已下架’
     * sortType   排序方式 ：0上架时间、1想要、浏览量
     * @return
     */
    @RequestMapping("/productlist/auth")
    @ResponseBody
    public JsonResponse getProductList(@RequestParam("tabType") Integer tabType,
    									@RequestParam(value="sortType", required=false, defaultValue = "0") Integer sortType,
									    @RequestParam(value="owns", required=false, defaultValue = "") String owns,
									    @RequestParam("current")int current,
    									@RequestParam("size")int size,
			UserDetail<StoreBusiness> userDetail) {
		JsonResponse jsonResponse = new JsonResponse();
		try {
			Map<String,Object> data = shopProductService.getShopProductList(tabType,sortType,new Page<ShopProduct>(current,size),userDetail.getUserDetail(),-1,0,0,"",0, owns);
			return jsonResponse.setSuccessful().setData(data);
		} catch (Exception e) {
			e.printStackTrace();
			return new JsonResponse().setError(e.getMessage());
		}
    } 
}


