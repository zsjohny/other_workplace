/**
 * created on 2015/06/20
 * 1、登录注册相关逻辑控制器，处理首页数据加载、用户登录、注册等控制
 * 2、增加用户信息修改功能
 * 
 * updated on 2015/09/06
 * 1. 按照新的用户模型进行校验处理
 * 2. 支持多种验证方式
 */
package com.jiuy.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jiuy.core.service.actionlog.ActionLogService;
import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.entity.query.PageQueryResult;
import com.jiuyuan.entity.store.ProductActionLog;
import com.jiuyuan.entity.store.StoreActionLog;
import com.jiuyuan.util.anno.Login;
import com.jiuyuan.web.help.JsonResponse;

/**
 *
 */
@Controller
@Login
@RequestMapping("/actionLog")
public class ActionLogController extends AbstractController {

	
	private static Logger logger = Logger.getLogger(ActionLogController.class);
	
	
	@Resource
	private ActionLogService actionLogService;	

//	@RequestMapping("/getProductActionLogList")
    
	
	/**
	 * 获取商品操作日志列表
	 * http://dev.yujiejie.com:33080/actionLog/getProductActionLogList.json
	 * @return 操作:0设置商品VIP、1取消商品VIP
	 * 	 * |current |string   |否|当前是第几页  |
|size |string   |否|每页显示条数  |
	 */
	@RequestMapping(value = "/getProductActionLogList")
    @ResponseBody
    public JsonResponse getProductActionLogList(
    		@RequestParam(required = false, defaultValue = "")String actionUserAccount,
    		@RequestParam(required = false, defaultValue = "")String actionUserName,
    		@RequestParam(required = false, defaultValue = "")String actionType,
    		@RequestParam(required = false, defaultValue = "")String actionContent,
    		@RequestParam(required = false, defaultValue = "0")long startTime,
    		@RequestParam(required = false, defaultValue = "0")long endTime,
    		@RequestParam(value = "page", required = false, defaultValue = "1")Integer page, 
			@RequestParam(value = "pageSize", required = false, defaultValue = "10")Integer pageSize
			) {
		
//		int count = storeAuthService.getAuthListCount(authState,authType,keyWord);
		
		PageQuery  pageQuery = new PageQuery(page,pageSize);
		PageQueryResult pageQueryResult = new PageQueryResult();
		BeanUtils.copyProperties(pageQuery, pageQueryResult);
		
       List<ProductActionLog> productActionLogList =  actionLogService.getProductActionLogList( actionUserAccount, actionUserName, actionType, actionContent, startTime,  endTime,pageQuery);
       
       Map<String,Object> data = new HashMap<String,Object>();
       data.put("productActionLogList", productActionLogList);
       int count =  actionLogService.getProductActionLogListCount( actionUserAccount, actionUserName, actionType, actionContent, startTime,  endTime);
       pageQueryResult.setRecordCount(count);
       data.put("total", pageQueryResult);
//		Map<String,String> pageInfo = new HashMap<String,String>();
//		pageInfo.put("current",String.valueOf(pageQuery.getPage()));
//		pageInfo.put("size",String.valueOf(pageQuery.getPageSize()));
//		data.put("page", pageInfo);
		
//		data.put("count", count+"");
       JsonResponse jsonResponse = new JsonResponse();
       return jsonResponse.setSuccessful().setData(data);
    }
	
	/**
	 * 获取商家操作日志列表
	 * http://dev.yujiejie.com:33080/actionLog/getStoreActionLogList.json
	 * @return 操作:0设置商品VIP、1取消商品VIP
	 * |current |string   |否|当前是第几页  |
|size |string   |否|每页显示条数  |
	 */
	@RequestMapping(value = "/getStoreActionLogList")
    @ResponseBody
    public JsonResponse getStoreActionLogList(
    		@RequestParam(required = false, defaultValue = "")String actionUserAccount,
    		@RequestParam(required = false, defaultValue = "")String actionUserName,
    		@RequestParam(required = false, defaultValue = "")String actionType,
    		@RequestParam(required = false, defaultValue = "")String actionContent,
    		@RequestParam(required = false, defaultValue = "0")long startTime,
    		@RequestParam(required = false, defaultValue = "0")long endTime,
    		@RequestParam(value = "page", required = false, defaultValue = "1")Integer page, 
			@RequestParam(value = "pageSize", required = false, defaultValue = "10")Integer pageSize
			) {
		
		PageQuery  pageQuery = new PageQuery(page,pageSize);
		PageQueryResult pageQueryResult = new PageQueryResult();
		BeanUtils.copyProperties(pageQuery, pageQueryResult);
		
       List<StoreActionLog> storeActionLogList =  actionLogService.getStoreActionLogList( actionUserAccount, actionUserName, actionType, actionContent, startTime,  endTime,pageQuery);
       
       Map<String,Object> data = new HashMap<String,Object>();
       data.put("storeActionLogList", storeActionLogList);
       
       int count =  actionLogService.getStoreActionLogListCount( actionUserAccount, actionUserName, actionType, actionContent, startTime,  endTime);
       pageQueryResult.setRecordCount(count);
       data.put("total", pageQueryResult);
       
//		Map<String,String> pageInfo = new HashMap<String,String>();
//		pageInfo.put("current",String.valueOf(pageQuery.getPage()));
//		pageInfo.put("size",String.valueOf(pageQuery.getPageSize()));
//		data.put("page", pageInfo);
//		data.put("count", count+"");
       JsonResponse jsonResponse = new JsonResponse();
       return jsonResponse.setSuccessful().setData(data);
    }
}
