/**
 * 
 */
package com.yujj.web.controller.mobile;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.entity.ClientPlatform;
import com.yujj.business.service.IdService;
import com.yujj.business.service.LogService;
import com.yujj.entity.account.UserDetail;
import com.jiuyuan.ext.spring.web.method.ClientIp;
import com.jiuyuan.web.help.JsonResponse;

/**
* @author DongZhong
* @version 创建时间: 2017年2月8日 上午9:38:25
*/
@Controller
@RequestMapping("/mobile/log")
public class MobileLogController {
	
    private static final Logger logger = LoggerFactory.getLogger(MobileLogController.class);

    @Autowired
    private IdService idService;
    
    @Autowired
    private LogService logService;
    
	/*
	 * @brief app推送日志信息到服务器
	 * @param[in] cid 客户设备唯一id
	 * @param[in] src_id 来源页面Id
	 * @param[in] src_related_id 来源页面相关Id
	 * @param[in] page_id 本页面Id
	 * @param[in] enter_time 页面进入时间
	 * @param[in] live_time 页面离开时间
	 * @param[in] duration 页面停留时间
	 */
    @RequestMapping("/pushLog")
    @ResponseBody
    @Deprecated
    public JsonResponse pushLog(@RequestParam("cid") String cid, @RequestParam("src_id") long srcId, 
    		@RequestParam("src_related_id") long SrcRelateId, @RequestParam("page_id") String pageId, 
    		@RequestParam("enter_time") long enterTime, @RequestParam("live_time") String liveTime, 
    		@RequestParam("duration") long duration, 
    		ClientPlatform clientPlatform, @ClientIp String ip, UserDetail userDetail) {
    	

    	logger.info("log pushLog:");
    	return new JsonResponse().setSuccessful();
    }
    
	/*
	 * @brief app推送日志信息到服务器
	 * @param[in] logs json对象数组 [{"logId":"","userId":"","ip":"","platform":"","version":"","Net":"","cid":"","srcId":"","srcLogId":"","srcRelatedId":"","pageId":"","enterTime":"","loadFinishTime":"","exitTime":"","duration":""}]
	 */
    @RequestMapping("/pushLogs")
    @ResponseBody
    public JsonResponse pushLogs(@RequestParam("logs") String logs,
    		ClientPlatform clientPlatform, @ClientIp String ip, UserDetail userDetail) {
    	    	
    	logService.addLogs(logs);
    	
    	return new JsonResponse().setSuccessful();
    }
    
	/*
	 * @brief 从服务器批量获取日志id
	 * @param[in] count id数量
	 * @return baseId： 连续数量id的第一个
	 */
    @RequestMapping("/getBaseId")
    @ResponseBody
    public JsonResponse getBaseId(@RequestParam("count") int count,
    		ClientPlatform clientPlatform, @ClientIp String ip, UserDetail userDetail) {
    	
    	Map<String, Object> map = new HashMap<String, Object>();
    	    	
    	map.put("baseId", idService.getLastLogId(count));
    	
    	return new JsonResponse().setSuccessful().setData(map);
    }
    
	/*
	 * @brief app推送相关订单日志信息到服务器
	 * @param[in] logs json对象数组 [{"SrcPageId":"","SrcRelateId":"","PageId":"","OrderNo":""}]
	 */
    @RequestMapping("/pushRelatedOrderLogs")
    @ResponseBody
    public JsonResponse pushRelatedOrderLogs(@RequestParam("logs") String logs,
    		ClientPlatform clientPlatform, @ClientIp String ip, UserDetail userDetail) {
    	if(logs==null || "".equals(logs)){
    		return new JsonResponse().setResultCode(ResultCode.COMMON_ERROR_PARAMETER_MISSING).setError("参数为空");
    	}
    	logService.addRelatedOrderLogs(logs);
    	return new JsonResponse().setSuccessful();
    }
    
	/*
	 * @brief app推送用户启动日志信息到服务器
	 * @param[in] logs json对象数组 {"userId":"","ip":"","platform":"","version":"","Net":"","cid":""}
	 */
    @RequestMapping("/pushUserStartLog")
    @ResponseBody
    public JsonResponse pushUserStartLog(@RequestParam("logs") String logs,
    		ClientPlatform clientPlatform, @ClientIp String ip, UserDetail userDetail) {

    	logService.addUserStartLog(logs);
    	
    	return new JsonResponse().setSuccessful();
    }
}
