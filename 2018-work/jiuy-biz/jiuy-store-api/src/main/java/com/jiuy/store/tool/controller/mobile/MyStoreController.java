package com.jiuy.store.tool.controller.mobile;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baomidou.mybatisplus.plugins.Page;
import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.dao.mapper.supplier.SecondBuyActivityMapper;
import com.jiuyuan.dao.mapper.supplier.TeamBuyActivityMapper;
import com.jiuyuan.entity.UserDetail;
import com.jiuyuan.entity.newentity.SecondBuyActivity;
import com.jiuyuan.entity.newentity.ShopProduct;
import com.jiuyuan.entity.newentity.StoreBusiness;
import com.jiuyuan.entity.newentity.TeamBuyActivity;
import com.jiuyuan.service.common.IMyStoreActivityService;
import com.jiuyuan.util.ResultCodeException;
import com.jiuyuan.util.TipsMessageException;
import com.jiuyuan.util.anno.AdminOperationLog;
import com.jiuyuan.web.help.JsonResponse;

@Controller
@RequestMapping("/store/mystore")
public class MyStoreController {
	private static final Logger logger = LoggerFactory.getLogger(MyStoreController.class);
	
	private static final int TEAM_ACTIVITY = 1;
	
	private static final int SECOND_ACTIVITY = 2;
	
	@Autowired
	private IMyStoreActivityService myStoreActivityService;
	
	@Autowired
	private TeamBuyActivityMapper teamBuyActivityMapper;
	
	@Autowired
	private SecondBuyActivityMapper secondBuyActivityMapper;
	
	@RequestMapping("/addTeamActivity/auth")
	@ResponseBody
	@AdminOperationLog
	public JsonResponse addTeamActivity(@RequestParam(value = "activityTitle",required=false,defaultValue="") String activityTitle,//活动标题
										@RequestParam(value="userCount", defaultValue = "0", required = false) Integer userCount,//成团人数
										@RequestParam("activityStartTime") long activityStartTime,//活动开始时间
			                            @RequestParam("activityEndTime") long activityEndTime,//活动结束时间
			                            @RequestParam("activityPrice") double activityPrice ,//活动价格
			                            @RequestParam("activityProductCount") int activityProductCount,//活动商品数量
			                            @RequestParam("shopProductId") long shopProductId,//门店商品ID
			                            @RequestParam(value = "conditionType", defaultValue = "1", required = false) Integer conditionType,//成团条件类型 1:人数成团(3.7.9以前版本),2:件数成团
			                            @RequestParam(value = "meetProductCount", defaultValue = "0", required = false) Integer meetProductCount,//成团件数
			                            UserDetail<StoreBusiness> userDetail
			                            ){
		JsonResponse jsonResponse = new JsonResponse();
		long storeId = userDetail.getId();
   	 	if(storeId == 0){
   	 		logger.info("商家信息不能为空，该接口需要登陆，请排除问题");
   	 		return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_NOT_LOGGED_IN);
   	 	}
   	 	try {
   	 	    myStoreActivityService.addTeamActivity(activityTitle,
   	 	    	                                   userCount,
   	 	    	                                   activityStartTime,
   	 	    	                                   activityEndTime,
   	 	    	                                   activityPrice,
   	 	    	                                   activityProductCount,
   	 	    	                                   shopProductId,
					                               conditionType, meetProductCount,
   	 	    	                                   storeId);
   	 		return jsonResponse.setSuccessful();
   	 } catch(ResultCodeException e){
			return jsonResponse.setResultCode(e.getCode(),e.getMessage());
   	 } catch (Exception e) {
			logger.info(e.getMessage());
			return jsonResponse.setError("服务器开小差了，请稍后再试");
	 }
	}
	
	@RequestMapping("/addSecondActivity/auth")
	@ResponseBody
	@AdminOperationLog
	public JsonResponse addSecondActivity(@RequestParam(value = "activityTitle",required=false,defaultValue="") String activityTitle,//活动标题
                                          @RequestParam("activityStartTime") long activityStartTime,//活动开始时间
                                          @RequestParam("activityEndTime") long activityEndTime,//活动结束时间
                                          @RequestParam("activityPrice") double activityPrice ,//活动价格
                                          @RequestParam("activityProductCount") int activityProductCount,//活动商品数量
                                          @RequestParam("shopProductId") long shopProductId,//门店商品ID
                                          UserDetail<StoreBusiness> userDetail
                                          ){ 
		JsonResponse jsonResponse = new JsonResponse();
		long storeId = userDetail.getId();
   	 	if(storeId == 0){
   	 		logger.info("商家信息不能为空，该接口需要登陆，请排除问题");
   	 		return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_NOT_LOGGED_IN);
   	 	}
   	 	try {
   	 	    myStoreActivityService.addSecondActivity(activityTitle,
                                                   activityStartTime,
                                                   activityEndTime,
                                                   activityPrice,
                                                   activityProductCount,
                                                   shopProductId,
                                                   storeId);
   	 		return jsonResponse.setSuccessful();
		} catch(ResultCodeException e){
			return jsonResponse.setResultCode(e.getCode(),e.getMessage());
		} catch (Exception e) {
			logger.info(e.getMessage());
			return jsonResponse.setError("服务器开小差了，请稍后再试");
		}
	}
	
	@RequestMapping("/editTeamActivity/auth")
	@ResponseBody
	@AdminOperationLog
	public JsonResponse editTeamActivity(@RequestParam(value = "activityTitle",required=false,defaultValue="") String activityTitle,//活动标题
										 @RequestParam(value="userCount", defaultValue = "0", required = false) Integer userCount,//成团人数
										 @RequestParam("activityStartTime") long activityStartTime,//活动开始时间
	                                     @RequestParam("activityEndTime") long activityEndTime,//活动结束时间
	                                     @RequestParam("activityPrice") double activityPrice ,//活动价格
	                                     @RequestParam("activityProductCount") int activityProductCount,//活动商品数量
	                                     @RequestParam("shopProductId") long shopProductId,//门店商品ID
	                                     @RequestParam("teamActivityId") long teamActivityId,//团购活动ID
										 @RequestParam(value = "conditionType", defaultValue = "1", required = false) Integer conditionType,//成团条件类型 1:人数成团(3.7.9以前版本),2:件数成团
										 @RequestParam(value = "meetProductCount", defaultValue = "0", required = false) Integer meetProductCount,//成团件数
										 UserDetail<StoreBusiness> userDetail){
		JsonResponse jsonResponse = new JsonResponse();
		long storeId = userDetail.getId();
	 	if(storeId == 0){
	 		logger.info("商家信息不能为空，该接口需要登陆，请排除问题");
	 		return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_NOT_LOGGED_IN);
	 	}
	 	try {
	 	    myStoreActivityService.editTeamActivity(activityTitle,
	                                                userCount,
	                                                activityStartTime,
	                                                activityEndTime,
	                                                activityPrice,
	                                                activityProductCount,
	                                                shopProductId,
	                                                teamActivityId,
	                                                storeId,conditionType, meetProductCount);
	 		return jsonResponse.setSuccessful();
		} catch(ResultCodeException e){
			return jsonResponse.setResultCode(e.getCode(),e.getMessage());
		} catch (Exception e) {
			logger.info(e.getMessage());
			return jsonResponse.setError("服务器开小差了，请稍后再试");
		}
	}

	@RequestMapping("/editSecondActivity/auth")
	@ResponseBody
	@AdminOperationLog
	public JsonResponse editSecondActivity(@RequestParam(value = "activityTitle",required=false,defaultValue="") String activityTitle,//活动标题
                                           @RequestParam("activityStartTime") long activityStartTime,//活动开始时间
                                           @RequestParam("activityEndTime") long activityEndTime,//活动结束时间
                                           @RequestParam("activityPrice") double activityPrice ,//活动价格
                                           @RequestParam("activityProductCount") int activityProductCount,//活动商品数量
                                           @RequestParam("shopProductId") long shopProductId,//门店商品ID
                                           @RequestParam("secondActivityId") long secondActivityId,//秒杀活动ID
                                           UserDetail<StoreBusiness> userDetail
			                              ){
		JsonResponse jsonResponse = new JsonResponse();
		long storeId = userDetail.getId();
   	 	if(storeId == 0){
   	 		logger.info("商家信息不能为空，该接口需要登陆，请排除问题");
   	 		return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_NOT_LOGGED_IN);
   	 	}
   	 	try {
   	 	    myStoreActivityService.editSecondActivity(activityTitle,
                                                      activityStartTime,
                                                      activityEndTime,
                                                      activityPrice,
                                                      activityProductCount,
                                                      shopProductId,
                                                      secondActivityId,
                                                      storeId
   	 	    		                                 );
   	 		return jsonResponse.setSuccessful();
		} catch(ResultCodeException e){
			return jsonResponse.setResultCode(e.getCode(),e.getMessage());
		} catch (Exception e) {
			logger.info(e.getMessage());
			return jsonResponse.setError("服务器开小差了，请稍后再试");
		}
	}
	
	@RequestMapping("/getShopProductList/auth")
	@ResponseBody
	public JsonResponse getShopProductList(@RequestParam(value="keywords",required=false,defaultValue="") String keywords,
			                               @RequestParam(value="current",required=true) int current,
			                               @RequestParam(value="size",required=false,defaultValue="20") int size,
			                               UserDetail<StoreBusiness> userDetail){
		JsonResponse jsonResponse = new JsonResponse();
		long storeId = userDetail.getId();
	 	if(storeId == 0){
	 		logger.info("商家信息不能为空，该接口需要登陆，请排除问题");
	 		return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_NOT_LOGGED_IN);
	 	}
	 	try {
	 		Page<Map<String,Object>> page = new Page<Map<String,Object>>(current,size);
	 		List<Map<String,Object>> data = myStoreActivityService.getShopProductList(keywords,storeId,page);
	 	    page.setRecords(data);
	 		return jsonResponse.setSuccessful().setData(page);
		} catch(ResultCodeException e){
			return jsonResponse.setResultCode(e.getCode(),e.getMessage());
		} catch (Exception e) {
			logger.info(e.getMessage());
			return jsonResponse.setError("服务器开小差了，请稍后再试");
		}
	}

	@RequestMapping("/checkProductChoice/auth")
	@ResponseBody
	public JsonResponse checkProductChoice(@RequestParam(value = "activityId",required = false,defaultValue ="0") long activityId,//活动Id
			                               @RequestParam("type") int type,//活动类型 1:团购 2:秒杀
			                               @RequestParam(value = "currentProductId") long currentProductId,//获取的当前商品ID
			                               UserDetail<StoreBusiness> userDetail){
		JsonResponse jsonResponse = new JsonResponse();
		long storeId = userDetail.getId();
	 	if(storeId == 0){
	 		logger.info("商家信息不能为空，该接口需要登陆，请排除问题");
	 		return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_NOT_LOGGED_IN);
	 	}
	 	try {
	 		long editProductId = 0L;
	 		//团购
	 		if(type == TEAM_ACTIVITY){
	 			if(activityId != 0L){
	 			TeamBuyActivity teamBuyActivity = teamBuyActivityMapper.selectById(activityId);
	 			editProductId =teamBuyActivity.getShopProductId();
	 			}
	 		}
	 		//秒杀
	 		if(type == SECOND_ACTIVITY){
	 			if(activityId != 0L){
	 				SecondBuyActivity secondBuyActivity = secondBuyActivityMapper.selectById(activityId);
	 				editProductId = secondBuyActivity.getShopProductId();
	 			}
	 		}
	 	    myStoreActivityService.checkProductChoice(editProductId,currentProductId);
	 	    return jsonResponse.setSuccessful();
		} catch(ResultCodeException e){
			return jsonResponse.setResultCode(e.getCode(),e.getMessage());
		} catch (Exception e) {
			logger.info(e.getMessage());
			return jsonResponse.setError("服务器开小差了，请稍后再试");
		}
	}
	
	@RequestMapping("/deleteAvtivity/auth")
	@ResponseBody
	@AdminOperationLog
	public JsonResponse deleteAvtivity(@RequestParam("activityId") long activityId,//活动ID
			                           @RequestParam("type") int type,//活动类型 1：团购 2：秒杀
			                           UserDetail<StoreBusiness> userDetail){
		JsonResponse jsonResponse = new JsonResponse();
		long storeId = userDetail.getId();
	 	if(storeId == 0){
	 		logger.info("商家信息不能为空，该接口需要登陆，请排除问题");
	 		return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_NOT_LOGGED_IN);
	 	}
	 	try {
	 		myStoreActivityService.deleteAvtivity(activityId,type,storeId);
	 		return jsonResponse.setSuccessful();
		} catch(ResultCodeException e){
			return jsonResponse.setResultCode(e.getCode(),e.getMessage());
		} catch (Exception e) {
			logger.info(e.getMessage());
			return jsonResponse.setError("服务器开小差了，请稍后再试");
		}
	}
	
	@RequestMapping("/handCloseActivity/auth")
	@ResponseBody
	@AdminOperationLog
	public JsonResponse handCloseActivity(@RequestParam("activityId") long activityId,//活动ID
                                          @RequestParam("type") int type,//活动类型 1：团购 2：秒杀
                                          UserDetail<StoreBusiness> userDetail){
		JsonResponse jsonResponse = new JsonResponse();
		long storeId = userDetail.getId();
	 	if(storeId == 0){
	 		logger.info("商家信息不能为空，该接口需要登陆，请排除问题");
	 		return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_NOT_LOGGED_IN);
	 	}
	 	try {
	 		myStoreActivityService.handCloseActivity(activityId,type,storeId);
	 		return jsonResponse.setSuccessful();
		} catch(ResultCodeException e){
			return jsonResponse.setResultCode(e.getCode(),e.getMessage());
		} catch (Exception e) {
			logger.info(e.getMessage());
			return jsonResponse.setError("服务器开小差了，请稍后再试");
		}
		
	}
	
	/**
	 * 开启或关闭客服热线
	 */
	@RequestMapping("/updateHotlineStatus/auth")
	@ResponseBody
	@AdminOperationLog
	public JsonResponse updateHotlineStatus(@RequestParam("hasHotonline") int hasHotonline,//是否开启客服热线 0：关闭 1：开启
			                                 UserDetail<StoreBusiness> userDetail){
		JsonResponse jsonResponse = new JsonResponse();
		
		try {
			long storeId = userDetail.getId();
			checkStore(storeId);
			myStoreActivityService.updateHotlineStatus(hasHotonline, storeId);
			return jsonResponse.setSuccessful();
		} catch(TipsMessageException e){
			logger.info(e.getFriendlyMsg());
			return jsonResponse.setError(e.getFriendlyMsg());
		} catch(ResultCodeException e){
			return jsonResponse.setResultCode(e.getCode(),e.getMessage());
		} catch (Exception e) {
			logger.info(e.getMessage());
			return jsonResponse.setError("服务器开小差了，请稍后再试");
		}
		
	}
	
	/**
	 * 保存客服热线
	 */
	@RequestMapping("/saveHotonline/auth")
	@ResponseBody
	@AdminOperationLog
	public JsonResponse saveHotonline(@RequestParam("hotonline") String hotonline,//热线电话
			UserDetail<StoreBusiness> userDetail){
		JsonResponse jsonResponse = new JsonResponse();
		
		try {
			long storeId = userDetail.getId();
			checkStore(storeId);
			myStoreActivityService.saveHotonline(hotonline, storeId);
			return jsonResponse.setSuccessful();
		} catch(TipsMessageException e){
			logger.info(e.getFriendlyMsg());
			return jsonResponse.setError(e.getFriendlyMsg());
		} catch(ResultCodeException e){
			return jsonResponse.setResultCode(e.getCode(),e.getMessage());
		} catch (Exception e) {
			logger.info(e.getMessage());
			return jsonResponse.setError("服务器开小差了，请稍后再试");
		}
		
	}

	private void checkStore(long storeId) {
		//检测门店
		if(storeId == 0){
			logger.info("商家信息不能为空，该接口需要登陆，请排除问题");
	 		throw new ResultCodeException(ResultCode.COMMON_ERROR_NOT_LOGGED_IN);
		}
		
	}


}
