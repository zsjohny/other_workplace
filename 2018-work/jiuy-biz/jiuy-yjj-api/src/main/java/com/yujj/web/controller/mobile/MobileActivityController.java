/**
 * 
 */
package com.yujj.web.controller.mobile;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.time.DateUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.constant.product.SortType;
import com.jiuyuan.entity.ClientPlatform;
import com.jiuyuan.entity.account.UserCoin;
import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.entity.query.PageQueryResult;
import com.jiuyuan.util.DateUtil;
import com.jiuyuan.util.anno.Login;
import com.jiuyuan.web.help.JsonResponse;
import com.yujj.business.facade.ActivityFacade;
import com.yujj.business.service.CategoryService;
import com.yujj.business.service.ProductService;
import com.yujj.business.service.UserCoinService;
import com.yujj.dao.mapper.DrawLotteryLogMapper;
import com.yujj.entity.account.UserDetail;
import com.yujj.entity.product.Category;
import com.yujj.entity.product.Product;

/**
 * @author LWS
 *
 * 产品活动相关接口
 */
@Controller
@RequestMapping("/mobile/activity")
public class MobileActivityController {
	
	@Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;
    
    @Autowired
    private ActivityFacade activityFacade; 
   
    @Autowired
    private UserCoinService userCoinService;
    
    @Autowired
    private DrawLotteryLogMapper drawLotteryLogMapper;
    
	/**
     * 加载指定活动产品列表，临时解决方案，后期迁移到单独的模块中，
     * 本次用于“圣诞节活动”专用
     * 
     * @author LWS
     * 
     * @return
     */
	@RequestMapping(value = "/getactivityproduct", method = RequestMethod.GET, produces = {
			"application/json;charset=UTF-8" })
	@ResponseBody
	public String loadActivityProduct(@RequestParam("activityid") long activityId,
			@RequestParam(value = "pagesize", defaultValue = "40", required = false) int pagesize) {
		final int _START_PAGE_ACTIVITY = 1;
		final int _PAGE_SIZE_ACTIVITY = pagesize;
		Collection<Long> categoryIds = new ArrayList<Long>();
		categoryIds.add(activityId);
		PageQuery pageQuery = new PageQuery(_START_PAGE_ACTIVITY, _PAGE_SIZE_ACTIVITY);
		List<Product> productList = productService.getProductOfCategory(categoryIds, SortType.CREATE_TIME_DESC,
				pageQuery);
		String retString = JSON.toJSONString(productList, true);
		return retString;
	}

    @RequestMapping("/flashsale")
    @ResponseBody
    public JsonResponse flashSale() {
        JsonResponse jsonResponse = new JsonResponse();

        long now = System.currentTimeMillis();
        long startTimeBegin = DateUtil.getDayZeroTime(now);
        long startTimeEnd = startTimeBegin + DateUtils.MILLIS_PER_DAY;
        Category category = categoryService.getCategoryById(-3);

        List<Product> productList =
            productService.getProductBySaleTime(category.getCategoryIds(), startTimeBegin, startTimeEnd,
                SortType.WEIGHT_DESC);
        Map<Long, List<Map<String, Object>>> timeProductMap = new HashMap<Long, List<Map<String, Object>>>();
        for (Product product : productList) {
            DateTime dateTime = new DateTime(product.getSaleStartTime());
            dateTime = dateTime.secondOfMinute().withMinimumValue();
            dateTime = dateTime.minuteOfHour().withMinimumValue();
            List<Map<String, Object>> products = timeProductMap.get(dateTime.getMillis());
            if (products == null) {
                products = new ArrayList<Map<String, Object>>();
                timeProductMap.put(dateTime.getMillis(), products);
            }
            products.add(product.toSimpleMap15());
        }

        List<Long> times = new ArrayList<Long>(timeProductMap.keySet());
        Collections.sort(times);

        List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
        for (Long time : times) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("time", time);
            map.put("products", timeProductMap.get(time));

            dataList.add(map);
        }

        Map<String, Object> data = new HashMap<String, Object>();
        data.put("nowTime", now);
        data.put("dataList", dataList);
        return jsonResponse.setSuccessful().setData(data);
    }
    
    @RequestMapping("/lottery/info")
    @ResponseBody
    @Login
    public JsonResponse lotteryInfo(UserDetail userDetail) {
    	JsonResponse jsonResponse = new JsonResponse();
    	Map<String, Object> data = new HashMap<>();
    	UserCoin userCoin = userCoinService.getUserCoin(userDetail.getUserId());
    	data.put("coin", userCoin == null ? 0 : userCoin.getUnavalCoins());
//    	data.put("lottery_info", globalSettingService.getJsonObject(GlobalSettingName.DRAW_LOTTERY));
    	data.put("lottery_info", activityFacade.getLotteryInfo());
    	data.put("lottery_list", activityFacade.getLotteryList());
    	
    	return jsonResponse.setResultCode(ResultCode.COMMON_SUCCESS).setData(data);
    }
    
    @RequestMapping("/draw/lottery")
    @ResponseBody
    @Login
    public JsonResponse drawLottery(UserDetail userDetail, ClientPlatform clientPlatform) {
    	JsonResponse jsonResponse = new JsonResponse();
    	Map<String, Object> data = new HashMap<>();
    	
    	Map<String, Object> lottery_info = null;
		try {
			lottery_info = activityFacade.drawLottery(userDetail, clientPlatform);
		} catch (ParseException e) {
			e.printStackTrace();
		}
    	
    	data.put("lottery_info", lottery_info);
    	
    	return jsonResponse.setResultCode(ResultCode.COMMON_SUCCESS).setData(data);
    }
    
    @RequestMapping("/draw/record")
    @ResponseBody
    @Login
    public JsonResponse drawRecord(UserDetail userDetail, PageQuery pageQuery) {
    	JsonResponse jsonResponse = new JsonResponse();
    	Map<String, Object> data = new HashMap<>();
    	
    	Integer sumJiuCoin = drawLotteryLogMapper.getTotalJiuCoinByUser(userDetail.getUserId());
    	int recordCount = drawLotteryLogMapper.getByUserCount(userDetail.getUserId(), 0, 1);
    	Integer times = drawLotteryLogMapper.getTotalLuckyTimes(userDetail.getUserId());
    	
    	PageQueryResult pageQueryResult = PageQueryResult.copyFrom(pageQuery, recordCount);
    	
    	data.put("total", pageQueryResult);
    	data.put("list", activityFacade.getDrawRecordByUser(userDetail.getUserId(), pageQuery, 0, 1));
    	data.put("sumJiuCoin", sumJiuCoin);
    	data.put("times", times);
    	
    	return jsonResponse.setResultCode(ResultCode.COMMON_SUCCESS).setData(data);
    }
    
    
    @RequestMapping("/lottery/announcement")
    @ResponseBody
    public JsonResponse lotteryAnnouncement(UserDetail userDetail, PageQuery pageQuery) {
    	JsonResponse jsonResponse = new JsonResponse();
    	Map<String, Object> data = new HashMap<>();
    	
    	int recordCount = drawLotteryLogMapper.getAllUserCount(0, 1);
    	List<Map<String, Object>> results = activityFacade.getLotteryUsersLogs(pageQuery, 0, 1);
    	
    	PageQueryResult pageQueryResult = PageQueryResult.copyFrom(pageQuery, recordCount);
    	
    	data.put("total", pageQueryResult);
    	data.put("list", results);
    	
    	return jsonResponse.setResultCode(ResultCode.COMMON_SUCCESS).setData(data);
    }
}
