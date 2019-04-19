package com.jiuy.store.tool.controller.mobile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jiuyuan.service.common.monitor.IProductMonitorService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.plugins.Page;
import com.jiuyuan.entity.UserDetail;
import com.jiuyuan.entity.newentity.RestrictionActivityProduct;
import com.jiuyuan.entity.newentity.StoreBusiness;
import com.jiuyuan.service.common.IActivityProductService;
import com.jiuyuan.util.SmallPage;
import com.jiuyuan.web.help.JsonResponse;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;

/**
* 商品Controller
* @author Qiuyuefan
*/
@Controller
@RequestMapping("/restrictionActivityProduct")
public class ShopRestrictionActivityProductController {

	private static final Log logger = LogFactory.get("ShopRestrictionActivityProductController");
	
    @Autowired
    private IActivityProductService activityProductService;

    @Autowired
    private IProductMonitorService productMonitorService;

     /**
     * 获取限购活动商品列表
     * @return
     */
    @RequestMapping({"/getList","/getList/auth"})
    @ResponseBody
    public JsonResponse getListaa(@RequestParam("activityStatus") int activityStatus,Page page,
			UserDetail<StoreBusiness> userDetail) {
		JsonResponse jsonResponse = new JsonResponse();
		try {
			Map<String,Object> data = new HashMap<String,Object>();
			List<RestrictionActivityProduct> restrictionActivityProductList = activityProductService.getRestrictionActivityProductListList(activityStatus,page);
			List<Map<String,Object>> restrictionActivityProductInfoList = this.packagingRestrictionActivityProductInfoList(restrictionActivityProductList,activityStatus);


			productMonitorService.fillActivityProduct(restrictionActivityProductInfoList,"restrictionActivityProductId");

			page.setRecords(restrictionActivityProductInfoList);
			SmallPage smallPage = new SmallPage(page);
			data.put("smallPage", smallPage);
			//获取限购活动标题
			String activityTitle = activityProductService.getActivityTitle();
			data.put("activityTitle", activityTitle);
			return jsonResponse.setSuccessful().setData(data);
		} catch (Exception e) {
			e.printStackTrace();
			return jsonResponse.setError(e.getMessage());
		}
    }

    /**
     * 封装限购活动商品列表信息
     * @param activityStatus 
     */
	private List<Map<String,Object>> packagingRestrictionActivityProductInfoList(
			List<RestrictionActivityProduct> restrictionActivityProductList, int activityStatus) {
		List<Map<String,Object>> restrictionActivityProductInfoList = new ArrayList<Map<String,Object>>();
		for (RestrictionActivityProduct restrictionActivityProduct : restrictionActivityProductList) {
			Map<String,Object> restrictionActivityProductInfo = new HashMap<String,Object>();
			restrictionActivityProductInfo.put("restrictionActivityProductId", restrictionActivityProduct.getId());//限购活动id
			restrictionActivityProductInfo.put("restrictionActivityProductName", restrictionActivityProduct.getProductName());//限购活动商品名称
			String promotionImage = restrictionActivityProduct.getPromotionImage();
			if(StringUtils.isEmpty(promotionImage)){
				String showcaseImage = restrictionActivityProduct.getShowcaseImage();
				JSONArray jsonArray = JSONArray.parseArray(showcaseImage);
				restrictionActivityProductInfo.put("promotionImage", jsonArray);//活动商品推广图
			}else{
				JSONArray jsonArray = new JSONArray();
				jsonArray.add(promotionImage);
				restrictionActivityProductInfo.put("promotionImage", jsonArray);//活动商品推广图
			}
			restrictionActivityProductInfo.put("activityProductPrice", restrictionActivityProduct.getActivityProductPrice());//活动价格
			restrictionActivityProductInfo.put("productPrice", restrictionActivityProduct.getProductPrice());//商品原价格
			restrictionActivityProductInfo.put("remainCount", restrictionActivityProduct.getRemainCount());//限购活动商品剩余库存
			if(activityStatus==1){//正进行中
				long activityEndTime = restrictionActivityProduct.getActivityEndTime();
				restrictionActivityProductInfo.put("timeMillis", activityEndTime-System.currentTimeMillis());//限购活动结束剩余毫秒
			}else if(activityStatus==2){//即将开始
				long activityBeginTime = restrictionActivityProduct.getActivityBeginTime();
				restrictionActivityProductInfo.put("timeMillis", activityBeginTime-System.currentTimeMillis());//限购活动开始剩余毫秒
			}else{
				logger.error("获取限购活动商品列表活动状态有误:activityStatus:"+activityStatus);
				throw new RuntimeException("获取限购活动商品列表活动状态有误");
			}
			restrictionActivityProductInfoList.add(restrictionActivityProductInfo);
		}
		return restrictionActivityProductInfoList;
	} 
	
//	/**
//     * 获取限购活动商品信息
//     * @return
//     */
//    @RequestMapping("/getRestrictionActivityProductInfo")
//    @ResponseBody
//    public JsonResponse getRestrictionActivityProductInfo(@RequestParam("activityProductId") long activityProductId,
//			UserDetail<StoreBusiness> userDetail) {
//		JsonResponse jsonResponse = new JsonResponse();
//		long storeId = userDetail.getId();
//   	 	if(storeId == 0){
//   	 		logger.info("商家信息不能为空，该接口需要登陆，请排除问题");
//   	 		return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_NOT_LOGGED_IN);
//   	 	}
//		try {
//			RestrictionActivityProduct restrictionActivityProduct = activityProductService.getRestrictionActivityProductInfo(activityProductId);
//			Map<String,Object> data = this.packagingRestrictionActivityProductInfo(restrictionActivityProduct);
//			return jsonResponse.setSuccessful().setData(data);
//		} catch (Exception e) {
//			e.printStackTrace();
//			return jsonResponse.setError(e.getMessage());
//		}
//    }
//
//    /**
//     * 封装限购活动商品信息
//     * @param restrictionActivityProduct
//     * @return
//     */
//	private Map<String, Object> packagingRestrictionActivityProductInfo(
//			RestrictionActivityProduct restrictionActivityProduct) {
//		Map<String,Object> data = new HashMap<String,Object>();
//		Map<String,Object> restrictionActivityProductInfo = new HashMap<String,Object>();
//		restrictionActivityProductInfo.put("restrictionActivityProductId", restrictionActivityProduct.getId());//限购活动id
//		String promotionImage = restrictionActivityProduct.getPromotionImage();
//		restrictionActivityProductInfo
//		.put("promotionImage", StringUtils.isEmpty(promotionImage)?restrictionActivityProduct.getShowcaseImage():promotionImage);//活动商品推广图
//		restrictionActivityProductInfo.put("productName", restrictionActivityProduct.getProductName());//活动商品名称
//		restrictionActivityProductInfo.put("clothesNumber", restrictionActivityProduct.getClothesNumber());//活动商品款号
//		restrictionActivityProductInfo.put("activityProductPrice", restrictionActivityProduct.getActivityProductPrice());//活动价格
//		restrictionActivityProductInfo.put("productPrice", restrictionActivityProduct.getProductPrice());//商品原价格
//		restrictionActivityProductInfo.put("remainCount", restrictionActivityProduct.getRemainCount());//限购活动商品剩余库存
//		long activityBeginTime = restrictionActivityProduct.getActivityBeginTime();
//		long activityEndTime = restrictionActivityProduct.getActivityEndTime();
//		if(activityBeginTime<System.currentTimeMillis() && activityEndTime>System.currentTimeMillis()){//正进行中
//			restrictionActivityProductInfo.put("timeMillis", activityEndTime-System.currentTimeMillis());//限购活动结束剩余毫秒
//		}else if(activityBeginTime>System.currentTimeMillis()){//即将开始
//			restrictionActivityProductInfo.put("timeMillis", activityBeginTime-System.currentTimeMillis());//限购活动开始剩余毫秒
//		}
//		data.put("restrictionActivityProductInfo", restrictionActivityProductInfo);
//		return data;
//	}
}