package com.jiuy.store.tool.controller.mobile;

import java.text.SimpleDateFormat;
import java.util.*;

import com.jiuy.base.util.Biz;
import com.jiuyuan.service.common.ISalesVolumeProductNewService;
import com.store.entity.SalesVolumeProduct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baomidou.mybatisplus.plugins.Page;
import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.entity.UserDetail;
import com.jiuyuan.entity.newentity.SecondBuyActivity;
import com.jiuyuan.entity.newentity.StoreBusiness;
import com.jiuyuan.entity.newentity.TeamBuyActivity;
import com.jiuyuan.service.common.IMyStoreActivityService;
import com.jiuyuan.service.common.StoreSecondBuyActivityService;
import com.jiuyuan.util.anno.Login;
import com.jiuyuan.web.help.JsonResponse;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;

/**
 * 商家
 * @author Administrator
 *
 */
@Controller
@Login
@RequestMapping("/store/second/buy/activity")
public class StoreSecondBuyActivityController {
	
	private static final Logger logger = LoggerFactory.getLogger(StoreSecondBuyActivityController.class);
	
    Log log = LogFactory.get();
	@Autowired
	private ISalesVolumeProductNewService salesVolumeProductNewService;
	@Autowired
	private StoreSecondBuyActivityService storeSecondBuyActivityService;
	
	@Autowired
	private IMyStoreActivityService myStoreActivityService;
	
	 /**
     * 秒杀管理列表
     * @return
     */
    @RequestMapping("/getList/auth")
    @ResponseBody
    public JsonResponse getStoreSecondBuyActivityList(UserDetail<StoreBusiness> userDetail,Page<SecondBuyActivity> page) {
    	JsonResponse jsonResponse = new JsonResponse();
    	try {
    		long storeId = userDetail.getId();
       	 	if(storeId == 0){
       	 		logger.info("商家信息不能为空，该接口需要登陆，请排除问题");
       	 		return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_NOT_LOGGED_IN);
       	 	}
       	 	
       	 	//秒杀管理列表
       	 	logger.info("秒杀管理列表storeId:"+storeId);
       	 	List<SecondBuyActivity> secondList = storeSecondBuyActivityService.getStoreSecondBuyActivityList(storeId,page);
			Map<String, Object> data = buildSecondActInfo (secondList);
			data.put("page", page);
       	   
       	 	//判断当前活动该用户是否有未结束的，如果有就不能创建
			List<SecondBuyActivity> list = myStoreActivityService.searchRunningSecondBuyActivity(storeId);
			if(list.size() >0){
				data.put("isActivityOngoing", "1");
		    }else{
		    	data.put("isActivityOngoing", "0");
		    }
			
       	 	//返回数据
       	 	return jsonResponse.setSuccessful().setData(data);
		} catch (Exception e) {
			logger.error("获取秒杀管理列表exception:"+e.getMessage());
			return jsonResponse.setError("获取秒杀管理列表有误");
		}
    	
    }

	/**
	 * 拼装商品信息,和销量信息
	 *
	 * @param secondList secondList
	 * @return java.util.Map
	 * @author Charlie
	 * @date 2018/7/31 17:40
	 */
	private Map<String, Object> buildSecondActInfo(List<SecondBuyActivity> secondList) {
		//商品销量
		List<SalesVolumeProduct> svList = null;
		if (! secondList.isEmpty ()) {
			ArrayList<Long> shopProductIds = new ArrayList<> (secondList.size ());
			for (SecondBuyActivity activity : secondList) {
				shopProductIds.add (activity.getId ());
			}
			svList = salesVolumeProductNewService.listSalesVolumeService(52, shopProductIds);
		}
		//拼装
		return this.setStoreSecondBuyActivityData(secondList, svList == null? new ArrayList<> (): svList);
	}

	/**
     * 秒杀历史列表
     * @return
     */
    @RequestMapping("/getHistoryList/auth")
    @ResponseBody
    public JsonResponse getHistoryList(UserDetail<StoreBusiness> userDetail,Page<SecondBuyActivity> page) {
    	JsonResponse jsonResponse = new JsonResponse();
    	try {
    		long storeId = userDetail.getId();
       	 	if(storeId == 0){
       	 		logger.info("商家信息不能为空，该接口需要登陆，请排除问题");
       	 		return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_NOT_LOGGED_IN);
       	 	}
       	 	
       	 	//秒杀历史列表
       	 	logger.info("秒杀历史列表storeId:"+storeId);
       	 	List<SecondBuyActivity> secondList = storeSecondBuyActivityService.getHistoryList(storeId,page);

			//商品销量
			Map<String, Object> data = buildSecondActInfo (secondList);
       	 	data.put("page", page);
       	   
       	 	//返回数据
       	 	return jsonResponse.setSuccessful().setData(data);
		} catch (Exception e) {
			logger.error("获取秒杀历史列表exception:"+e.getMessage());
			return jsonResponse.setError("获取秒杀历史列表有误");
		}
    	
    }
    
    /**
     * 秒杀活动详情
     * @return
     */
    @RequestMapping("/getStoreSecondBuyActivityItem/auth")
    @ResponseBody
    public JsonResponse getStoreSecondBuyActivityItem(@RequestParam(value="activityId")Long activityId,
    		UserDetail<StoreBusiness> userDetail,Page<SecondBuyActivity> page) {
    	JsonResponse jsonResponse = new JsonResponse();
    	try {
    		long storeId = userDetail.getId();
       	 	if(storeId == 0){
       	 		logger.info("商家信息不能为空，该接口需要登陆，请排除问题");
       	 		return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_NOT_LOGGED_IN);
       	 	}
       	 	
       	 	//秒杀活动详情
       	 	logger.info("秒杀活动详情storeId:"+storeId+";activityId:"+activityId);
       	 	SecondBuyActivity secondBuyActivity = storeSecondBuyActivityService.getStoreSecondBuyActivityItem(activityId);
       	 	if(secondBuyActivity.getDelState()==-1){//该活动已删除
       	 		return jsonResponse.setError("该活动已删除");
       	 	}
       	 	Map<String,Object> data = this.setStoreSecondBuyActivityItemData(secondBuyActivity);
       	   
       	 	//返回数据
       	 	return jsonResponse.setSuccessful().setData(data);
		} catch (Exception e) {
			logger.error("获取秒杀活动详情exception:"+e.getMessage());
			return jsonResponse.setError("获取秒杀活动详情有误");
		}
    	
    }

    /**
     * 封装秒杀活动详情
     * @param secondBuyActivity
     * @return
     */
    private Map<String, Object> setStoreSecondBuyActivityItemData(SecondBuyActivity secondBuyActivity) {
    	SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	Map<String,Object> secondBuyActivityData = new HashMap<String,Object>();
		secondBuyActivityData.put("id", secondBuyActivity.getId());
		secondBuyActivityData.put("shopProductId", secondBuyActivity.getShopProductId());
		secondBuyActivityData.put("shopProductMainimg", secondBuyActivity.getShopProductMainimg());
		secondBuyActivityData.put("shopProductName", secondBuyActivity.getShopProductName());
		secondBuyActivityData.put("activityProductPrice", secondBuyActivity.getActivityProductPrice()+"");
		secondBuyActivityData.put("activityTitle", secondBuyActivity.getActivityTitle());
		secondBuyActivityData.put("activityStartTime", simpleDateFormat.format(new Date(secondBuyActivity.getActivityStartTime())));
		secondBuyActivityData.put("activityEndTime", simpleDateFormat.format(new Date(secondBuyActivity.getActivityEndTime())));
		secondBuyActivityData.put("activityPrice", secondBuyActivity.getActivityPrice());
		secondBuyActivityData.put("activityProductCount", secondBuyActivity.getActivityProductCount());
		secondBuyActivityData.put("activityMemberCount", secondBuyActivity.getActivityMemberCount());
		secondBuyActivityData.put("activityStatus", secondBuyActivity.haveActivityStatus());
		secondBuyActivityData.put("activityStatusInt", secondBuyActivity.haveActivityStatusInt());
		Long activityHandEndTime = secondBuyActivity.getActivityHandEndTime();
		if(activityHandEndTime==null || activityHandEndTime==0){
			secondBuyActivityData.put("activityHandEndTime", "");
		}else{
			secondBuyActivityData.put("activityHandEndTime", simpleDateFormat.format(new Date(activityHandEndTime)));
		}
		return secondBuyActivityData;
	}

	/**
     * 封装秒杀活动数据
     * @param storeSecondBuyActivityList
     * @return
     */
	private Map<String, Object> setStoreSecondBuyActivityData(List<SecondBuyActivity> storeSecondBuyActivityList, List<SalesVolumeProduct> svList) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Map<String,Object> data = new HashMap<String,Object>();
		List<Map<String,Object>> storeSecondBuyActivityDataList = new ArrayList<Map<String,Object>>();
		for (SecondBuyActivity secondBuyActivity : storeSecondBuyActivityList) {
			Map<String,Object> secondBuyActivityData = new HashMap<String,Object>();
			secondBuyActivityData.put("id", secondBuyActivity.getId());
			secondBuyActivityData.put("activityStatus", secondBuyActivity.haveActivityStatus());
			secondBuyActivityData.put("activityStatusInt", secondBuyActivity.haveActivityStatusInt());
			secondBuyActivityData.put("shopProductMainimg", secondBuyActivity.getShopProductMainimg());
			secondBuyActivityData.put("shopProductName", Biz.replaceStr (secondBuyActivity.getShopProductName(), 20, "..."));
			secondBuyActivityData.put("activityPrice", secondBuyActivity.getActivityPrice());
			secondBuyActivityData.put ("activityProductPrice", secondBuyActivity.getActivityProductPrice ());
			secondBuyActivityData.put("activityProductCount", secondBuyActivity.getActivityProductCount());
			secondBuyActivityData.put("activityStartTime", simpleDateFormat.format(new Date(secondBuyActivity.getActivityStartTime())));
			secondBuyActivityData.put("activityEndTime", simpleDateFormat.format(new Date(secondBuyActivity.getActivityEndTime())));

			//销量
			secondBuyActivityData.put ("salesVolume", 0);
			Iterator<SalesVolumeProduct> it = svList.iterator ();
			while (it.hasNext ()) {
				SalesVolumeProduct next = it.next ();
				if (ObjectUtils.nullSafeEquals (next.getProductId (), secondBuyActivity.getId ())) {
					secondBuyActivityData.put ("salesVolume", next.getOrderSuccessCount ());
					it.remove ();
					break;
				}
			}
			storeSecondBuyActivityDataList.add(secondBuyActivityData);
		}
		data.put("storeSecondBuyActivityDataList", storeSecondBuyActivityDataList);
		return data;
	}

}