package com.jiuy.store.tool.controller.mobile;

import java.text.SimpleDateFormat;
import java.util.*;

import com.jiuy.base.util.Biz;
import com.jiuy.rb.service.product.ISalesVolumeService;
import com.jiuy.rb.service.product.IShopProductService;
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
import com.jiuyuan.entity.newentity.StoreBusiness;
import com.jiuyuan.entity.newentity.TeamBuyActivity;
import com.jiuyuan.service.common.IMyStoreActivityService;
import com.jiuyuan.service.common.StoreTeamBuyActivityService;
import com.jiuyuan.util.anno.Login;
import com.jiuyuan.web.help.JsonResponse;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;

import javax.annotation.Resource;

/**
 * 商家
 * @author Administrator
 *
 */
@Controller
@Login
@RequestMapping("/store/team/buy/activity")
public class StoreTeamBuyActivityController {
	
	private static final Logger logger = LoggerFactory.getLogger(StoreTeamBuyActivityController.class);
	
    Log log = LogFactory.get();
    
    @Autowired
    private StoreTeamBuyActivityService storeTeamBuyActivityService;
    
    @Autowired
    private IMyStoreActivityService myStoreActivityService;

    @Autowired
	private ISalesVolumeProductNewService salesVolumeProductNewService;

    /**
     * 团购管理列表
     * @return
     */
    @RequestMapping("/getList/auth")
    @ResponseBody
    public JsonResponse getStoreTeamBuyActivityList(UserDetail<StoreBusiness> userDetail,Page<TeamBuyActivity> page) {
    	JsonResponse jsonResponse = new JsonResponse();
    	try {
    		long storeId = userDetail.getId();
       	 	if(storeId == 0){
       	 		logger.info("商家信息不能为空，该接口需要登陆，请排除问题");
       	 		return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_NOT_LOGGED_IN);
       	 	}
       	 	
       	 	//团购管理列表
       	 	logger.info("团购管理列表storeId:"+storeId);
       	 	List<TeamBuyActivity> teamList = storeTeamBuyActivityService.getStoreTeamBuyActivityList(storeId,page);
       	 	//商品销量
			Map<String, Object> data = buildTeamActInfo (teamList);
       	 	data.put("page", page);
       	 	
       	 	//判断当前活动该用户是否有未结束的，如果有就不能创建
			/*List<TeamBuyActivity> list = myStoreActivityService.searchRunningTeamBuyActivity(storeId);
			if(list.size() >0){
				data.put("isActivityOngoing", "1");
		    }else{
		    	data.put("isActivityOngoing", "0");
		    }*/
       	   
       	 	//返回数据
       	 	return jsonResponse.setSuccessful().setData(data);
		} catch (Exception e) {
			logger.error("获取团购管理列表exception:"+e.getMessage());
			return jsonResponse.setError("获取团购管理列表有误");
		}
    	
    }
    
    /**
     * 团购历史列表
     * @return
     */
    @RequestMapping("/getHistoryList/auth")
    @ResponseBody
    public JsonResponse getHistoryList(UserDetail<StoreBusiness> userDetail,Page<TeamBuyActivity> page) {
    	JsonResponse jsonResponse = new JsonResponse();
    	try {
    		long storeId = userDetail.getId();
       	 	if(storeId == 0){
       	 		logger.info("商家信息不能为空，该接口需要登陆，请排除问题");
       	 		return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_NOT_LOGGED_IN);
       	 	}
       	 	
       	 	//团购历史列表
       	 	logger.info("团购历史列表storeId:"+storeId);
       	 	List<TeamBuyActivity> teamList = storeTeamBuyActivityService.getHistoryList(storeId,page);
			Map<String, Object> data = buildTeamActInfo (teamList);


			data.put("page", page);
       	   
       	 	//返回数据
       	 	return jsonResponse.setSuccessful().setData(data);
		} catch (Exception e) {
			logger.error("获取团购历史列表exception:"+e.getMessage());
			return jsonResponse.setError("获取团购历史列表有误");
		}
    	
    }



	/**
	 * 拼装信息(销量)
	 *
	 * @param teamList teamList
	 * @return java.util.Map
	 * @author Charlie
	 * @date 2018/7/31 17:42
	 */
	private Map<String, Object> buildTeamActInfo(List<TeamBuyActivity> teamList) {
		//商品销量
		List<SalesVolumeProduct> svList = null;
		if (! teamList.isEmpty ()) {
			ArrayList<Long> shopProductIds = new ArrayList<> (teamList.size ());
			for (TeamBuyActivity activity : teamList) {
				shopProductIds.add (activity.getId ());
			}
			svList = salesVolumeProductNewService.listSalesVolumeService(51, shopProductIds);
		}
		//拼装
		return this.setStoreTeamBuyActivityData(teamList, svList == null? new ArrayList<> (): svList);
	}

	/**
     * 团购活动详情
     * @return
     */
    @RequestMapping("/getStoreTeamBuyActivityItem/auth")
    @ResponseBody
    public JsonResponse getStoreTeamBuyActivityItem(@RequestParam(value="activityId")Long activityId,
    		UserDetail<StoreBusiness> userDetail,Page<TeamBuyActivity> page) {
    	JsonResponse jsonResponse = new JsonResponse();
    	try {
    		long storeId = userDetail.getId();
       	 	if(storeId == 0){
       	 		logger.info("商家信息不能为空，该接口需要登陆，请排除问题");
       	 		return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_NOT_LOGGED_IN);
       	 	}
       	 	
       	 	//团购活动详情
       	 	logger.info("团购活动详情storeId:"+storeId+";activityId:"+activityId);
       	 	TeamBuyActivity TeamBuyActivity = storeTeamBuyActivityService.getStoreTeamBuyActivityItem(activityId);
       	 	Map<String,Object> data = this.setStoreTeamBuyActivityItemData(TeamBuyActivity);
       	   
       	 	//返回数据
       	 	return jsonResponse.setSuccessful().setData(data);
		} catch (Exception e) {
			logger.error("获取团购活动详情exception:"+e.getMessage());
			return jsonResponse.setError("获取团购活动详情有误");
		}
    	
    }

    /**
     * 封装团购活动详情
     * @return
     */
    private Map<String, Object> setStoreTeamBuyActivityItemData(TeamBuyActivity teamBuyActivity) {
    	SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	Map<String,Object> teamBuyActivityData = new HashMap<String,Object>();
    	teamBuyActivityData.put("id", teamBuyActivity.getId());
    	teamBuyActivityData.put("shopProductId", teamBuyActivity.getShopProductId());
    	teamBuyActivityData.put("shopProductMainimg", teamBuyActivity.getShopProductMainimg());
    	teamBuyActivityData.put("shopProductName", teamBuyActivity.getShopProductName());
		teamBuyActivityData.put("activityProductPrice", teamBuyActivity.getActivityProductPrice()+"");
		teamBuyActivityData.put("activityTitle", teamBuyActivity.getActivityTitle());
		teamBuyActivityData.put("userCount", teamBuyActivity.getUserCount());
		teamBuyActivityData.put("activityStartTime", simpleDateFormat.format(new Date(teamBuyActivity.getActivityStartTime())));
		teamBuyActivityData.put("activityEndTime", simpleDateFormat.format(new Date(teamBuyActivity.getActivityEndTime())));
		teamBuyActivityData.put("activityPrice", teamBuyActivity.getActivityPrice());
		teamBuyActivityData.put("activityProductCount", teamBuyActivity.getActivityProductCount());
		teamBuyActivityData.put("activityMemberCount", teamBuyActivity.getActivityMemberCount());
		teamBuyActivityData.put("activityStatus", teamBuyActivity.haveActivityStatus());
		teamBuyActivityData.put("activityStatusInt", teamBuyActivity.haveActivityStatusInt());
		teamBuyActivityData.put("conditionType", teamBuyActivity.getConditionType ());
		teamBuyActivityData.put("meetProductCount", teamBuyActivity.getMeetProductCount ());
		teamBuyActivityData.put("orderedProductCount", teamBuyActivity.getOrderedProductCount ());
		Long activityHandEndTime = teamBuyActivity.getActivityHandEndTime();
		if(activityHandEndTime==null || activityHandEndTime==0){
			teamBuyActivityData.put("activityHandEndTime", "");
		}else{
			teamBuyActivityData.put("activityHandEndTime", simpleDateFormat.format(new Date(activityHandEndTime)));
		}
		return teamBuyActivityData;
	}

	/**
     * 封装团购活动数据
     */
	private Map<String, Object> setStoreTeamBuyActivityData(List<TeamBuyActivity> teamList, List<SalesVolumeProduct> svList) {

		List<Map<String,Object>> finalResult = new ArrayList<> (teamList.size ());
		if (! teamList.isEmpty ()) {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			for (TeamBuyActivity team : teamList) {
				Map<String,Object> teamBuyActivityData = new HashMap<String,Object>();
				Long teamId = team.getId ();
				teamBuyActivityData.put("id", teamId);
				teamBuyActivityData.put("activityProductPrice", team.getActivityProductPrice ());
				teamBuyActivityData.put("activityStatus", team.haveActivityStatus());
				teamBuyActivityData.put("activityStatusInt", team.haveActivityStatusInt());
				teamBuyActivityData.put("shopProductMainimg", team.getShopProductMainimg());
				teamBuyActivityData.put("shopProductName", Biz.replaceStr (team.getShopProductName(), 20, "..."));
				teamBuyActivityData.put("activityPrice", team.getActivityPrice());
				teamBuyActivityData.put("activityProductCount", team.getActivityProductCount());
				teamBuyActivityData.put("userCount", team.getUserCount());
				teamBuyActivityData.put("conditionType", team.getConditionType ());
				teamBuyActivityData.put("meetProductCount", team.getMeetProductCount ());
				teamBuyActivityData.put("orderedProductCount", team.getOrderedProductCount ());
				teamBuyActivityData.put("activityMemberCount", team.getActivityMemberCount ());
				teamBuyActivityData.put("activityStartTime", simpleDateFormat.format(new Date(team.getActivityStartTime())));
				teamBuyActivityData.put("activityEndTime", simpleDateFormat.format(new Date(team.getActivityEndTime())));
				//销量
				teamBuyActivityData.put ("salesVolume", 0);
				Iterator<SalesVolumeProduct> it = svList.iterator ();
				while (it.hasNext ()) {
					SalesVolumeProduct next = it.next ();
					if (ObjectUtils.nullSafeEquals (next.getProductId (), team.getId ())) {
						teamBuyActivityData.put ("salesVolume", next.getOrderSuccessCount ());
						it.remove ();
						break;
					}
				}
				//add
				finalResult.add(teamBuyActivityData);
			}
		}


		Map<String,Object> data = new HashMap<> (2);
		data.put("storeTeamBuyActivityDataList", finalResult);
		return data;
	}

}