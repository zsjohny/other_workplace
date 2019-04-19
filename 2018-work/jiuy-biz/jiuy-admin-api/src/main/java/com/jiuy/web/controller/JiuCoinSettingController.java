package com.jiuy.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.util.TextUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jiuy.core.dao.GrantJiuCoinLogDao;
import com.jiuy.core.dao.mapper.UserDao;
import com.jiuy.core.dao.modelv2.ProductMapper;
import com.jiuy.core.meta.account.User;
import com.jiuy.core.service.GlobalSettingService;
import com.jiuy.core.service.JiuCoinService;
import com.jiuyuan.constant.GlobalSettingName;
import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.constant.product.SaleStatus;
import com.jiuyuan.entity.GlobalSetting;
import com.jiuyuan.entity.GrantJiuCoinLog;
import com.jiuyuan.entity.JiuCoinDeductStatisticsDayBean;
import com.jiuyuan.entity.Product;
import com.jiuyuan.entity.product.DeductProductVO;
import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.entity.query.PageQueryResult;
import com.jiuyuan.util.anno.AdminOperationLog;
import com.jiuyuan.util.anno.Login;
import com.jiuyuan.web.help.JsonResponse;

/**
* @author WuWanjian
* @version 创建时间: 2016年11月22日 上午10:02:46
*/
@RequestMapping("/jiucoin")
@Controller
@Login
public class JiuCoinSettingController {
	
	@Autowired
	private GlobalSettingService globalSettingService;
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private ProductMapper productMapper;
	
	@Resource
	private JiuCoinService jiuCoinService;
	
	@Resource
	private GrantJiuCoinLogDao grantJiuCoinLogDao;

	@RequestMapping(value = "/setting/update")
	@ResponseBody
	@AdminOperationLog
	public JsonResponse add(@RequestParam(value = "property_name") String propertyName, 
			@RequestParam(value = "property_value") String propertyValue,
			@RequestParam(value = "group_id", required = false, defaultValue = "0") int groupId,
			@RequestParam(value = "group_value", required = false, defaultValue = "") String groupName,
			@RequestParam(value = "description", required = false, defaultValue = "") String description){
		JsonResponse jsonResponse = new JsonResponse();
		
		globalSettingService.addJiuCoinSetting(propertyName, propertyValue, groupId, groupName, description);
		
		return jsonResponse.setSuccessful();
	}
	
	@RequestMapping(value = "/setting/load")
	@ResponseBody
	@AdminOperationLog
	public JsonResponse load(){
		JsonResponse jsonResponse = new JsonResponse();
		HashMap<String, Object> data = new HashMap<String,Object>();
		
		String setting = globalSettingService.getSetting(GlobalSettingName.getByStringValue(GlobalSettingName.JIUCOIN_GLOBAL_SETTING.getStringValue()));
		JSONObject parseObject = JSON.parseObject(setting);
		data.put("setting", parseObject);
		
		return jsonResponse.setSuccessful().setData(data);
	}
	
	@RequestMapping(value = "/grant" , method = RequestMethod.POST)
	@ResponseBody
	@AdminOperationLog
	public JsonResponse grantJiuCoin(@RequestParam(value = "type")int type,
			@RequestParam(value = "users")String usersStr,
			@RequestParam(value = "coins")int coins){
		JsonResponse jsonResponse = new JsonResponse();
		
		List<Long> userList = new ArrayList<>();
		if(type!=2){
			userList = transToUsers(usersStr);
			if(userList.size()<=0 && type!=2){
				return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER).setError("未指定发放用户");
			}
		}
		
		int result = jiuCoinService.updateJiuCoin(type,coins,userList,usersStr);
		
		return result == 1 ? jsonResponse.setSuccessful() : jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER).setError("输入的用户信息有误");
	}
	
	@RequestMapping(value = "/grantlog")
	@ResponseBody
	@AdminOperationLog
	public JsonResponse getGrantJiuCoinLog(@RequestParam(value = "page", required = false, defaultValue = "1")int page,
			@RequestParam(value = "page_size", required = false, defaultValue="20")int pageSize){
		JsonResponse jsonResponse = new JsonResponse();
		Map<String, Object> data = new HashMap<String, Object>();
		PageQuery query = new PageQuery(page, pageSize);
		PageQueryResult queryResult = new PageQueryResult();
		BeanUtils.copyProperties(query, queryResult);
		
		List<GrantJiuCoinLog> logList = grantJiuCoinLogDao.getGrantJiuCoinLog(queryResult);
		int count = grantJiuCoinLogDao.getGrantJiuCoinLogCount();
		queryResult.setRecordCount(count);
		
		data.put("logs", logList);
		data.put("total", queryResult);
		
		return jsonResponse.setSuccessful().setData(data);
	}
	
	@RequestMapping(value = "/grantcount")
	@ResponseBody
	@AdminOperationLog
	public JsonResponse getGrantCount(@RequestParam(value = "type")int type,
			@RequestParam(value = "users")String usersStr){
		JsonResponse jsonResponse = new JsonResponse();
		Map<String, Object> data = new HashMap<String, Object>();
		int count = 0;
		
		List<Long> usersList = new ArrayList<>();
		if(type!=2){
			usersList = transToUsers(usersStr);
			if(usersList.size()==0){
				data.put("count", count);
				return jsonResponse.setSuccessful().setData(data);
			}
		}
		
		List<User> users = new ArrayList<>();
		
		switch (type) {
		case 1:			//指定用户
			count = userDao.searchCount(usersList);
			break;
			
		case 2:
			count = (int)userDao.getUserCount(null, null);
			break;
			
		case 3:
			count = userDao.excludeSearchCount(usersList);
			break;
		case 4:
			users = userDao.usersOfPhones(usersList);
			break;
		}
		
		if(users!=null && users.size()>0){
			count = users.size();
		}
		
		data.put("count", count);
		
		return jsonResponse.setSuccessful().setData(data);
	}
	
	private List<Long> transToUsers(String usersStr){
		List<Long> users = new ArrayList<Long>();
		if(StringUtils.isBlank(usersStr)){
			return users;
		}
		String[] split = usersStr.split(",");
		for(String item : split){
			try { 
				users.add(Long.parseLong(item));
			}catch (Exception e) {
				continue;
			}
		}
		return users;
	}
	
	@RequestMapping(value = "/deduction/product")
	@ResponseBody
	@AdminOperationLog
	public JsonResponse searchDeductProduct(PageQuery pageQuery,
			@RequestParam(value = "id", required = false, defaultValue = "-1") long id,
            @RequestParam(value = "season", required = false, defaultValue = "-1") long season,
            @RequestParam(value = "year", required = false, defaultValue = "-1") long year,
    		@RequestParam(value = "name", required = false, defaultValue ="") String name,
    		@RequestParam(value = "clothes_num", required = false, defaultValue = "") String clothesNumber,
			@RequestParam(value = "brand_name", required = false, defaultValue = "")String brandName, 
			@RequestParam(value = "sale_status", required = false, defaultValue = "0")int saleStatus,
			@RequestParam(value = "parent_categoryid", required = false, defaultValue = "-1")int parentCategoryId,
			@RequestParam(value = "categoryid", required = false, defaultValue = "-1")long categoryId,
			@RequestParam(value = "sku_status", required = false, defaultValue = "0")int skuStatus){
		Map<String, Object> data = new HashMap<String, Object>();
		JsonResponse jsonResponse = new JsonResponse();
		
		List<DeductProductVO> list = productMapper.searchJiuCoinDeductProduct(pageQuery, id, season, year, name, clothesNumber, brandName, SaleStatus.getSql(saleStatus), parentCategoryId, categoryId, skuStatus);
		int count = productMapper.searchJiuCoinDeductProductCount(id, season, year, name, clothesNumber, brandName, SaleStatus.getSql(saleStatus), parentCategoryId, categoryId, skuStatus);
		data.put("list", list);
		PageQueryResult pageQueryResult = PageQueryResult.copyFrom(pageQuery, count);
		data.put("total", pageQueryResult);
		
		return jsonResponse.setSuccessful().setData(data);
	}
	
	/**
	 * 批量修改符合条件的商品的玖币抵扣百分比
	 * @return
	 */
	@RequestMapping(value = "/deduction/batchupdate",method = RequestMethod.POST)
	@ResponseBody
	@AdminOperationLog
	public JsonResponse batchUpdateJiuCoinDeduction(PageQuery pageQuery,
			@RequestParam(value = "product_ids", required = false, defaultValue = "")String productIdsStr,
			@RequestParam(value = "id", required = false, defaultValue = "-1") long id,
            @RequestParam(value = "season", required = false, defaultValue = "-1") long season,
            @RequestParam(value = "year", required = false, defaultValue = "-1") long year,
    		@RequestParam(value = "name", required = false, defaultValue ="") String name,
    		@RequestParam(value = "clothes_num", required = false, defaultValue = "") String clothesNumber,
			@RequestParam(value = "brand_name", required = false, defaultValue = "")String brandName, 
			@RequestParam(value = "sale_status", required = false, defaultValue = "0")int saleStatus,
			@RequestParam(value = "parent_categoryid", required = false, defaultValue = "-1")int parentCategoryId,
			@RequestParam(value = "categoryid", required = false, defaultValue = "-1")long categoryId,
			@RequestParam(value = "sku_status", required = false, defaultValue = "0")int skuStatus,
			@RequestParam(value = "deduct_percent", required = true)double deductPercent){
		Map<String, Object> data = new HashMap<String, Object>();
		JsonResponse jsonResponse = new JsonResponse();
		
		List productIdList =  new ArrayList<Long>();
		//分情况
		if(!TextUtils.isEmpty(productIdsStr)){
			//根据Id
			productIdList = transToProductIds(productIdsStr);
		}else {
			//查询符合条件的需要商品
			if(pageQuery.getPage() == 0){		//需要修改符合条件的所有商品
				pageQuery = null;
			}
			List<DeductProductVO> productList = productMapper.searchJiuCoinDeductProduct(pageQuery, id, season, year, name, clothesNumber, brandName, SaleStatus.getSql(saleStatus), parentCategoryId, categoryId, skuStatus);
			for (Product product : productList) {
				productIdList.add(product.getId());
			}
		}
		
		try{
			if(productIdList.size() == 0){
				return jsonResponse.setError("未找到符合条件的商品");
			}
			jiuCoinService.batchUpdateJiuCoinDeduction(productIdList,deductPercent);
			return jsonResponse.setSuccessful();
		}catch (Exception e) {
			return jsonResponse.setError("修改玖币抵扣失败");
		}
	}
	
	private List<Long> transToProductIds(String productIdsStr){
		List<Long> productIds = new ArrayList<Long>();
		if(StringUtils.isBlank(productIdsStr)){
			return productIds;
		}
		String[] split = productIdsStr.split(",");
		for(String item : split){
			try { 
				productIds.add(Long.parseLong(item));
			}catch (Exception e) {
				continue;
			}
		}
		return productIds;
	}
	
	/**
	 * 玖币抵钱明细记录
	 * @return
	 */
	@RequestMapping(value = "/deduction/detailrecord")
	@ResponseBody
	@AdminOperationLog
	public JsonResponse searchDeductDetailRecord(PageQuery pageQuery,
			@RequestParam(value = "product_id",required = false, defaultValue="0")long productId,
			@RequestParam(value = "product_name",required = false, defaultValue="")String productName,
			@RequestParam(value = "yjjnumber", required = false, defaultValue="0")long yjjNumber,
			@RequestParam(value = "start_time", required = false, defaultValue="0")long startTime,
			@RequestParam(value = "end_time", required = false, defaultValue="0")long endTime){
		Map<String, Object> data = new HashMap<String, Object>();
		JsonResponse jsonResponse = new JsonResponse();
		
		if(endTime == 0){
			endTime = System.currentTimeMillis();
		}
		
		Map<String, Object> resultData = jiuCoinService.searchDeductDetailRecord(pageQuery,productId, productName, yjjNumber, startTime, endTime);
		
		return jsonResponse.setSuccessful().setData(resultData);
	}
	
	@RequestMapping(value = "/deduction/setting/update",method = RequestMethod.POST)
	@ResponseBody
	@AdminOperationLog
	public JsonResponse add(@RequestParam(value = "property_value") String propertyValue,
			@RequestParam(value = "group_id", required = false, defaultValue = "0") int groupId,
			@RequestParam(value = "group_value", required = false, defaultValue = "") String groupName,
			@RequestParam(value = "description", required = false, defaultValue = "") String description){
		JsonResponse jsonResponse = new JsonResponse();
		
		GlobalSetting globalSetting = new GlobalSetting();
		globalSetting.setPropertyName(GlobalSettingName.JIUCOIN_DEDUCTION_SETTING.getStringValue());
		globalSetting.setPropertyValue(propertyValue);
		globalSetting.setGroupId(groupId);
		globalSetting.setGroupName(groupName);
		globalSetting.setDescription(description);
		
		globalSettingService.add(globalSetting);
		
		return jsonResponse.setSuccessful();
	}
	
	@RequestMapping(value = "/deduction/setting/load")
	@ResponseBody
	@AdminOperationLog
	public JsonResponse settingLoad(){
		JsonResponse jsonResponse = new JsonResponse();
		HashMap<String, Object> data = new HashMap<String,Object>();
		
		String setting = globalSettingService.getSetting(GlobalSettingName.getByStringValue(GlobalSettingName.JIUCOIN_DEDUCTION_SETTING.getStringValue()));
		JSONObject parseObject = JSON.parseObject(setting);
		data.put("setting", parseObject);
		
		return jsonResponse.setSuccessful().setData(data);
	}
	
	@RequestMapping(value="/deduction/dayStatistics")
	@ResponseBody	
	public JsonResponse dayStatistics(){
		Map<String, Object> data = new HashMap<String, Object>();

		JsonResponse jsonResponse = new JsonResponse();
		
		List<JiuCoinDeductStatisticsDayBean> list = jiuCoinService.sumDayStatistics();
		data.put("list", list);
		
		return jsonResponse.setSuccessful().setData(data);
	}
	
	//时间区间查询
	@RequestMapping(value="/deduction/timeIntervalStatistics")
	@ResponseBody
	public JsonResponse timeIntervalStatistics(@RequestParam(value="start_time",required = false,defaultValue="0")long startTime,
			@RequestParam(value="end_time",required = false,defaultValue="0")long endTime){
		Map<String, Object> data = new HashMap<String, Object>();
		JsonResponse jsonResponse = new JsonResponse();
		
		if(endTime == 0){
			endTime = System.currentTimeMillis();
		}
		
		JiuCoinDeductStatisticsDayBean resultData = jiuCoinService.timeIntervalStatistics(startTime, endTime);
		data.put("data", resultData);
		
		return jsonResponse.setSuccessful().setData(data);
	}
}

