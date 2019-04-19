package com.jiuy.operator.modular.limitActivityManage.controller;

import java.io.IOException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jiuyuan.service.common.monitor.IProductMonitorService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.admin.common.constant.factory.PageFactory;
import com.admin.core.base.controller.BaseController;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.plugins.Page;
import com.jiuyuan.constant.Client;
import com.jiuyuan.entity.newentity.RestrictionActivityProduct;
import com.jiuyuan.entity.newentity.RestrictionActivityProductSku;
import com.jiuyuan.service.common.IActivityProductService;
import com.jiuyuan.util.UrlUtil;
import com.jiuyuan.util.anno.AdminOperationLog;
import com.jiuyuan.web.help.JsonResponse;
import com.yujj.util.file.OSSFileUtil;

/**
 * 活动商品控制器
 *
 * @author fengshuonan
 * @Date 2018-03-14 09:26:51
 */
@Controller
@RequestMapping("/activityProduct")
public class ActivityProductController extends BaseController {
	
	private static final Logger logger = LoggerFactory.getLogger(ActivityProductController.class);

    private String PREFIX = "/limitActivityManage/activityProduct/";
    
    private final String DEFAULT_BASEPATH_NAME = Client.OSS_DEFAULT_BASEPATH_NAME;

    @Autowired
    private IProductMonitorService productMonitorService;

    @Autowired
	private OSSFileUtil ossFileUtil;
    
    @Resource
    private IActivityProductService activityProductService;
    
    /**
     * 获取所有活动商品列表
     * @return
     */
    @RequestMapping("/getAllActivityProductList")
    @ResponseBody
    @AdminOperationLog
    public Object getAllActivityProductList(
    		@RequestParam(value="restrictionActivityProductId",required=false,defaultValue="0")long restrictionActivityProductId,//活动商品ID
    		@RequestParam(value="restrictionActivityProductName",required=false,defaultValue="")String restrictionActivityProductName,//活动商品名称
    		@RequestParam(value="clothesNumber",required=false,defaultValue="")String clothesNumber,//款号
    		@RequestParam(value="productStatus",required=false,defaultValue="-1")int productStatus,//商品状态
    		@RequestParam(value="restrictionActivityStatus",required=false,defaultValue="-1")int restrictionActivityStatus,//活动状态
    		@RequestParam(value="remainCountMin",required=false,defaultValue="0")int remainCountMin,//活动当前库存量
    		@RequestParam(value="remainCountMax",required=false,defaultValue="0")int remainCountMax,//活动当前库存量
    		@RequestParam(value="activityProductShelfTimeBegin",required=false,defaultValue="-1")long activityProductShelfTimeBegin,//上架时间
    		@RequestParam(value="activityProductShelfTimeEnd",required=false,defaultValue="-1")long activityProductShelfTimeEnd,//上架时间
    		@RequestParam(value="activityProductPriceMin",required=false,defaultValue="0")double activityProductPriceMin,//活动价
    		@RequestParam(value="activityProductPriceMax",required=false,defaultValue="0")double activityProductPriceMax,//活动价
    		@RequestParam(value="saleCountMin",required=false,defaultValue="0")int saleCountMin,//销量
    		@RequestParam(value="saleCountMax",required=false,defaultValue="0")int saleCountMax//销量
    		) {
    	JsonResponse jsonResponse = new JsonResponse();
    	clothesNumber = UrlUtil.unescape(clothesNumber);
    	try {
    		Page<Map<String, Object>> page = new PageFactory<Map<String, Object>>().defaultPage();
    		List<RestrictionActivityProduct> activityProductList = activityProductService.getAllActivityProductList(restrictionActivityProductId,
    				restrictionActivityProductName,clothesNumber,productStatus,
    				restrictionActivityStatus,remainCountMin,remainCountMax,activityProductShelfTimeBegin,activityProductShelfTimeEnd,
    				activityProductPriceMin,activityProductPriceMax,saleCountMin,saleCountMax,page);
    		List<Map<String,Object>> result = this.packagingAllActivityProductList(activityProductList);
			productMonitorService.fillActivityProduct(result,"activityProductId");
    		page.setRecords(result);
			return super.packForBT(page);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取所有活动商品列表:"+e.getMessage());
			return jsonResponse.setError(e.getMessage());
		}
    }

    /**
     * 封装所有活动商品列表数据
     * @param activityProductList
     * @return
     */
    private List<Map<String, Object>> packagingAllActivityProductList(
			List<RestrictionActivityProduct> activityProductList) {
    	SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
		for (RestrictionActivityProduct restrictionActivityProduct : activityProductList) {
			Map<String,Object> restrictionActivityProductMap = new HashMap<String,Object>();
			long restrictionActivityProductId = restrictionActivityProduct.getId();
			restrictionActivityProductMap.put("activityProductId", restrictionActivityProductId);//活动商品id
			restrictionActivityProductMap.put("clothesNumber", restrictionActivityProduct.getClothesNumber());//活动商品款号
			//获取活动商品对应的sku
			List<RestrictionActivityProductSku> restrictionActivityProductSkuList = 
					activityProductService.getAllActivityProductSkuList(restrictionActivityProductId);
			restrictionActivityProductMap.put("skuCount", restrictionActivityProductSkuList.size());//活动商品sku个数
			restrictionActivityProductMap.put("mainImage", restrictionActivityProduct.getMainImage());//活动商品预览图片
			restrictionActivityProductMap.put("activityProductName", restrictionActivityProduct.getProductName());//活动商品名称
			restrictionActivityProductMap.put("remainCount", restrictionActivityProduct.getRemainCount());//活动商品当前库存
			restrictionActivityProductMap.put("saleCount", restrictionActivityProduct.getSaleCount());//活动商品销量
			restrictionActivityProductMap.put("activityProductPrice", restrictionActivityProduct.getActivityProductPrice());//活动价格
			restrictionActivityProductMap.put("activityStatus", this.getActivityStatus(restrictionActivityProduct));//活动价格
			//活动开始时间
			long activityBeginTime = restrictionActivityProduct.getActivityBeginTime();
			restrictionActivityProductMap.put("activityBeginTime", activityBeginTime>0?simpleDateFormat.format(new Date(activityBeginTime)):activityBeginTime);
			//活动结束时间
			long activityEndTime = restrictionActivityProduct.getActivityEndTime();
			restrictionActivityProductMap.put("activityEndTime", activityEndTime>0?simpleDateFormat.format(new Date(activityEndTime)):activityEndTime);
			//活动创建时间
			restrictionActivityProductMap.put("createTime", simpleDateFormat.format(new Date(restrictionActivityProduct.getCreateTime())));
			//活动商品状态
			restrictionActivityProductMap.put("productStatus", restrictionActivityProduct.getProductStatus());
			restrictionActivityProductMap.put("productStatusStr", this.getProductStatusStr(restrictionActivityProduct));
			result.add(restrictionActivityProductMap);
		}
		return result;
	}

    /**
     * 获取当前活动商品状态
     * @param restrictionActivityProduct
     * @return
     */
    private String getProductStatusStr(RestrictionActivityProduct restrictionActivityProduct) {
    	int productStatus = restrictionActivityProduct.getProductStatus();
    	//0:待上架;1:已上架;2:已下架;3:已删除
		if(productStatus==0){
			return "待上架";
		}else if(productStatus==1){
			return "已上架";
		}else if(productStatus==2){
			return "已下架";
		}else if(productStatus==3){
			return "已删除";
		}
		logger.error("获取当前活动商品状态:该活动商品状态有误id:"+restrictionActivityProduct.getId()+";productStatus:"+restrictionActivityProduct);
		return "";
	}

	/**
     * 获取当前活动状态
     * @param restrictionActivityProduct
     * @return
     */
	private String getActivityStatus(RestrictionActivityProduct restrictionActivityProduct) {
		if(restrictionActivityProduct.getRemainCount()<1){
			return "已售罄";
		}else if(restrictionActivityProduct.getActivityBeginTime()>System.currentTimeMillis()){
			return "即将开始";
		}else if(restrictionActivityProduct.getActivityEndTime()>=System.currentTimeMillis()){
			return "进行中";
		}else if(restrictionActivityProduct.getActivityEndTime()<System.currentTimeMillis()){
			return "已结束";
		}
		logger.error("获取当前活动状态:该活动状态有误id:"+restrictionActivityProduct.getId());
		return "";
	}
	
	/**
     * 获取所有活动商品数量
     * @return
     */
    @RequestMapping("/getAllActivityProductListCount")
    @ResponseBody
    @AdminOperationLog
    public Object getAllActivityProductListCount() {
    	JsonResponse jsonResponse = new JsonResponse();
    	try {
    		Map<String,Object> data = new HashMap<String,Object>();
    		int activityProductListAllCount = activityProductService.getActivityProductListAllCount();
    		int activityProductListProcessingCount = activityProductService.getActivityProductListProcessingCount();
    		int activityProductListSoldOutCount = activityProductService.getActivityProductListSoldOutCount();
    		data.put("activityProductListAllCount", activityProductListAllCount);
    		data.put("activityProductListProcessingCount", activityProductListProcessingCount);
    		data.put("activityProductListSoldOutCount", activityProductListSoldOutCount);
    		return jsonResponse.setSuccessful().setData(data);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取所有活动商品数量:"+e.getMessage());
			return jsonResponse.setError(e.getMessage());
		}
    }
    
    /**
     * 上架活动商品
     * @return
     */
    @RequestMapping("/shelfActivityProduct")
    @ResponseBody
    @AdminOperationLog
    public Object shelfActivityProduct(@RequestParam(value="restrictionActivityProductId")long restrictionActivityProductId) {
    	JsonResponse jsonResponse = new JsonResponse();
    	try {
    		int result = activityProductService.shelfActivityProduct(restrictionActivityProductId);
    		if(result!=1){
    			return jsonResponse.setError("上架活动商品有误");
    		}
    		return jsonResponse.setSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("上架活动商品:"+e.getMessage());
			return jsonResponse.setError(e.getMessage());
		}
    }
    
    /**
     * 删除活动商品
     * @return
     */
    @RequestMapping("/deleteActivityProduct")
    @ResponseBody
    @AdminOperationLog
    public Object deleteActivityProduct(@RequestParam(value="restrictionActivityProductId")long restrictionActivityProductId) {
    	JsonResponse jsonResponse = new JsonResponse();
    	try {
    		int result = activityProductService.deleteActivityProduct(restrictionActivityProductId);
    		if(result!=1){
    			return jsonResponse.setError("删除活动商品有误");
    		}
    		return jsonResponse.setSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("删除活动商品:"+e.getMessage());
			return jsonResponse.setError(e.getMessage());
		}
    }
    
    /**
     * 下架活动商品
     * @return
     */
    @RequestMapping("/soldOutActivityProduct")
    @ResponseBody
    @AdminOperationLog
    public Object soldOutActivityProduct(@RequestParam(value="restrictionActivityProductId")long restrictionActivityProductId) {
    	JsonResponse jsonResponse = new JsonResponse();
    	try {
    		int result = activityProductService.soldOutActivityProduct(restrictionActivityProductId);
    		if(result!=1){
    			return jsonResponse.setError("下架活动商品有误");
    		}
    		return jsonResponse.setSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("下架活动商品:"+e.getMessage());
			return jsonResponse.setError(e.getMessage());
		}
    }
    
    /**
     * 回显活动商品信息
     * @return
     */
    @RequestMapping("/getActivityProductInfo")
    @ResponseBody
    @AdminOperationLog
    public Object getActivityProductInfo(@RequestParam(value="restrictionActivityProductId")long restrictionActivityProductId) {
    	JsonResponse jsonResponse = new JsonResponse();
    	try {
    		RestrictionActivityProduct restrictionActivityProduct = activityProductService.getActivityProductById(restrictionActivityProductId);
    		Map<String,Object> data = this.packagingAllActivityProductInfo(restrictionActivityProduct);
    		return jsonResponse.setSuccessful().setData(data);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("上架活动商品:"+e.getMessage());
			return jsonResponse.setError(e.getMessage());
		}
    }
    
    /**
     * 更新活动商品sku库存
     * @return
     */
    @RequestMapping("/updateActivityProductSkuRemainCount")
    @ResponseBody
    @AdminOperationLog
    public Object updateActivityProductSkuRemainCount(@RequestParam(value="restrictionActivityProductSkuId")long restrictionActivityProductSkuId,
    		@RequestParam(value="remainCount")int remainCount) {
    	JsonResponse jsonResponse = new JsonResponse();
    	try {
    		activityProductService.updateActivityProductSkuRemainCount(restrictionActivityProductSkuId,remainCount);
    		return jsonResponse.setSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("上架活动商品:"+e.getMessage());
			return jsonResponse.setError(e.getMessage());
		}
    }

    /**
     * 封装活动商品数据
     * @param restrictionActivityProduct
     * @return
     */
	private Map<String, Object> packagingAllActivityProductInfo(RestrictionActivityProduct restrictionActivityProduct) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Map<String,Object> data = new HashMap<String,Object>();
		long restrictionActivityProductId = restrictionActivityProduct.getId();
		data.put("activityProductId", restrictionActivityProductId);//活动商品id
		String promotionImage = restrictionActivityProduct.getPromotionImage();
		if(StringUtils.isEmpty(promotionImage)){
			String showcaseImage = restrictionActivityProduct.getShowcaseImage();
			JSONArray jsonArray = JSONArray.parseArray(showcaseImage);
			data.put("promotionImage", jsonArray);//活动商品推广图
		}else{
			JSONArray jsonArray = new JSONArray();
			jsonArray.add(promotionImage);
			data.put("promotionImage", jsonArray);//活动商品推广图
		}
		
		data.put("activityProductName", restrictionActivityProduct.getProductName());//活动商品名称
		data.put("activityProductPrice", restrictionActivityProduct.getActivityProductPrice());//活动价格
		data.put("productPrice", restrictionActivityProduct.getProductPrice());//活动价格
		//获取活动商品对应的sku
		List<RestrictionActivityProductSku> restrictionActivityProductSkuList = 
				activityProductService.getAllActivityProductSkuList(restrictionActivityProductId);
		List<Map<String,Object>> restrictionActivityProductSkuInfoList = new ArrayList<Map<String,Object>>();
		for (RestrictionActivityProductSku restrictionActivityProductSku : restrictionActivityProductSkuList) {
			Map<String,Object> restrictionActivityProductSkuInfo = new HashMap<String,Object>();
			restrictionActivityProductSkuInfo.put("id", restrictionActivityProductSku.getId());//活动商品skuId
			restrictionActivityProductSkuInfo.put("productSkuId", restrictionActivityProductSku.getProductSkuId());//商品skuId
			restrictionActivityProductSkuInfo.put("color", restrictionActivityProductSku.getColorName());//颜色
			restrictionActivityProductSkuInfo.put("size", restrictionActivityProductSku.getSizeName());//尺码
			restrictionActivityProductSkuInfo.put("remainCount", restrictionActivityProductSku.getRemainCount());//库存
			restrictionActivityProductSkuInfoList.add(restrictionActivityProductSkuInfo);
		}
		data.put("restrictionActivityProductSkuInfoList", restrictionActivityProductSkuInfoList);
		//活动开始时间
		long activityBeginTime = restrictionActivityProduct.getActivityBeginTime();
		data.put("activityBeginTime", activityBeginTime>0?simpleDateFormat.format(new Date(activityBeginTime)):activityBeginTime);
		//活动结束时间
		long activityEndTime = restrictionActivityProduct.getActivityEndTime();
		data.put("activityEndTime", activityEndTime>0?simpleDateFormat.format(new Date(activityEndTime)):activityEndTime);
		//限购数量
		data.put("restrictionCount", restrictionActivityProduct.getRestrictionCount());
		//最小起订量
        data.put("miniPurchaseCount", restrictionActivityProduct.getMiniPurchaseCount());
		return data;
	}
	
	/**
     * 保存/修改 单个活动商品信息
     * @return
     */
    @RequestMapping("/saveActivityProductInfo")
    @ResponseBody
    @AdminOperationLog
    public Object saveActivityProductInfo(
    		@RequestParam(value="productIds",required=false,defaultValue="")String productIds,//活动商品id
    		@RequestParam(value="restrictionActivityProductId",required=false,defaultValue="0")long restrictionActivityProductId,//活动商品id
    		@RequestParam(value="promotionImage",required=false,defaultValue="")String promotionImage,//活动商品推广图片
    		@RequestParam(value="activityProductName",required=false,defaultValue="")String activityProductName,//活动商品名称
    		@RequestParam(value="activityProductPrice",required=false,defaultValue="0")double activityProductPrice,//活动价格
    		@RequestParam(value="activityPricePercentage",required=false,defaultValue="0")int activityPricePercentage,//活动价格百分比
    		@RequestParam(value="productPrice",required=false,defaultValue="0")double productPrice,//活动商品原价
    		@RequestParam(value="skuInfo",required=false,defaultValue="")String skuInfo,//sku信息
    		@RequestParam(value="activityBeginTime")long activityBeginTime,//活动开始时间
    		@RequestParam(value="activityEndTime")long activityEndTime,//活动结束时间
    		@RequestParam(value="restrictionCount")int restrictionCount,//限购数量
    		@RequestParam(value="miniPurchaseCount",defaultValue="1")int miniPurchaseCount)//最小购买量/起订量
	{
    	JsonResponse jsonResponse = new JsonResponse();
    	try {
    		Map<String,Object> data = new HashMap<String,Object>();
    		int record = activityProductService.saveActivityProductInfo(productIds,restrictionActivityProductId,promotionImage,activityProductName,activityProductPrice,activityPricePercentage,
    				productPrice,skuInfo,activityBeginTime,activityEndTime,restrictionCount,miniPurchaseCount);
    		data.put("record", record);
    		return jsonResponse.setSuccessful().setData(data);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("保存活动商品信息:"+e.getMessage());
			return jsonResponse.setError(e.getMessage());
		}
    }
    
    /**
     * 保存多个活动商品信息
     * @return
     */
    @RequestMapping("/saveActivityProductsInfo")
    @ResponseBody
    @AdminOperationLog
    public Object saveActivityProductsInfo(
    		@RequestParam(value="productIds",required=false,defaultValue="")String productIds,//活动商品id
    		@RequestParam(value="activityProductPrice",required=false,defaultValue="0")double activityProductPrice,//活动价格
    		@RequestParam(value="activityPricePercentage",required=false,defaultValue="0")int activityPricePercentage,//活动价格百分比
    		@RequestParam(value="activityBeginTime")long activityBeginTime,//活动开始时间
    		@RequestParam(value="activityEndTime")long activityEndTime,//活动结束时间
    		@RequestParam(value="restrictionCount")int restrictionCount//限购数量
	){

    	JsonResponse jsonResponse = new JsonResponse();
    	try {
    		String[] productIdArr = productIds.split(",");
    		Map<String,Object> data = new HashMap<>();
    		int record = activityProductService.saveActivityProducts(productIdArr,activityProductPrice,activityPricePercentage,
					activityBeginTime,activityEndTime,restrictionCount);
    		data.put("record", record);
    		return jsonResponse.setSuccessful().setData(data);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("保存多个活动商品信息:"+e.getMessage());
			return jsonResponse.setError(e.getMessage());
		}
    }
    
    /**
     * 保存限购活动标题
     * @return
     */
    @RequestMapping("/saveActivityTitle")
    @ResponseBody
    @AdminOperationLog
    public Object saveActivityTitle(@RequestParam(value="activityTitle",required=false,defaultValue="")String activityTitle) {//限购活动标题
    	JsonResponse jsonResponse = new JsonResponse();
    	try {
    		activityProductService.saveActivityTitle(activityTitle);
    		return jsonResponse.setSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("保存限购活动标题:"+e.getMessage());
			return jsonResponse.setError(e.getMessage());
		}
    }
    
    /**
     * 获得限购活动标题
     * @return
     */
    @RequestMapping("/getActivityTitle")
    @ResponseBody
    @AdminOperationLog
    public Object getActivityTitle() {//限购活动标题
    	JsonResponse jsonResponse = new JsonResponse();
    	try {
    		Map<String,String> data = new HashMap<String,String>();
    		String activityTitle =  activityProductService.getActivityTitle();
    		data.put("activityTitle",activityTitle);
    		return jsonResponse.setSuccessful().setData(data);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取限购活动标题:"+e.getMessage());
			return jsonResponse.setError(e.getMessage());
		}
    }
    
//    /**
//     * 保存活动商品信息(多个)
//     * @return
//     */
//    @RequestMapping("/saveActivityProductsInfo")
//    @ResponseBody
//    @AdminOperationLog
//    public Object saveActivityProductsInfo(
//    		@RequestParam(value="restrictionActivityProductIds")String restrictionActivityProductIds,//活动商品id
//    		@RequestParam(value="activityPrice",required=false,defaultValue="0")double activityPrice,//活动价格
//    		@RequestParam(value="activityPricePercentage",required=false,defaultValue="0")int activityPricePercentage,//活动价格百分比
//    		@RequestParam(value="activityBeginTime")long activityBeginTime,//活动开始时间
//    		@RequestParam(value="activityEndTime")long activityEndTime,//活动结束时间
//    		@RequestParam(value="restrictionCount")int restrictionCount) {//限购数量
//    	JsonResponse jsonResponse = new JsonResponse();
//    	try {
//    		activityProductService.saveActivityProductsInfo(restrictionActivityProductIds,
//    				activityPrice,activityPricePercentage,activityBeginTime,activityEndTime,restrictionCount);
//    		return jsonResponse.setSuccessful();
//		} catch (Exception e) {
//			e.printStackTrace();
//			logger.error("保存活动商品信息(多个):"+e.getMessage());
//			return jsonResponse.setError(e.getMessage());
//		}
//    }
    
    /**
     * 根据款号获取商品的信息
     * @return
     */
    @RequestMapping("/getNewActivityProductInfo")
    @ResponseBody
    @AdminOperationLog
    public Object getNewActivityProductInfo(@RequestParam(value="clothesNumbers")String clothesNumbers) {
    	JsonResponse jsonResponse = new JsonResponse();
    	clothesNumbers = UrlUtil.unescape(clothesNumbers);
    	try {
    		Map<String,Object> restrictionActivityProductList = activityProductService.getNewActivityProductInfo(clothesNumbers);
    		return jsonResponse.setSuccessful().setData(restrictionActivityProductList);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("根据款号获取商品的信息:"+e.getMessage());
			return jsonResponse.setError(e.getMessage());
		}
    }
    
    
    /**
	 * 上传商品推广图自定义图片
	 *  创建内容: 1）上传文件到OSS中 
	 *  2） 将文件名存储到session中
	 *
	 * @param request
	 * @param response
	 * @return 例子：{"filename":https://yjj-img-www.oss-cn-hangzhou.aliyuncs.com/15087254452311508725445231.jpg}
	 *  method = RequestMethod.POST
	 */
	@RequestMapping(value = "/ossUpload")
	@ResponseBody
	public Map<String, String> ossUpload(@RequestPart("file") MultipartFile file, ModelMap modelMap,HttpServletRequest request, HttpServletResponse response) {
		String path = null;
		String oldPath = request.getParameter("oldPath");
		Map<String, String> result = new HashMap<String, String>();
		try {
			if (request instanceof MultipartHttpServletRequest) {
				logger.debug("yes you are!");
				if (file == null) {
					logger.error("请求中没有file对象，请排查问题" );
					logger.error("request file null oldPath:" + oldPath);
					return result;
				}
				logger.debug("request file name :" + file.getName());
				path = ossFileUtil.uploadFile(DEFAULT_BASEPATH_NAME, file);
				
				//覆盖旧路径则删除
				if(StringUtils.isNotEmpty(StringUtils.trim(oldPath))){
					String key = oldPath.split("/")[oldPath.split("/").length - 1];
					ossFileUtil.removeFile(DEFAULT_BASEPATH_NAME, key);
				}
			} else {
				logger.debug("no wrong request!");
			}
			modelMap.addAttribute("images", path);
			result.put("filename", path);
			logger.info("上传商品推广图自定义图片接口返回数据，result:"+result.toString());
			return result;
		} catch (IOException e) {
			logger.error("上传商品推广图自定义图片出现异常", e);
		}
		return result;
	}

	/**
     * 跳转到活动商品首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "activityProduct.html";
    }

    /**
     * 跳转到添加活动商品
     */
    @RequestMapping("/activityProduct_add")
    public String activityProductAdd() {
        return PREFIX + "activityProduct_add.html";
    }

    /**
     * 跳转到修改活动商品
     */
    @RequestMapping("/activityProduct_update")
    public String activityProductUpdate() {
        return PREFIX + "activityProduct_edit.html";
    }

    /**
     * 获取活动商品列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        return null;
    }

    /**
     * 新增活动商品
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add() {
        return super.SUCCESS_TIP;
    }

    /**
     * 删除活动商品
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete() {
        return SUCCESS_TIP;
    }


    /**
     * 修改活动商品
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update() {
        return super.SUCCESS_TIP;
    }

    /**
     * 活动商品详情
     */
    @RequestMapping(value = "/detail")
    @ResponseBody
    public Object detail() {
        return null;
    }
}
