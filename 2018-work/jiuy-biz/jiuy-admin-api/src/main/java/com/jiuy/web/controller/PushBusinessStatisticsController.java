package com.jiuy.web.controller;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jiuy.core.service.PushBusinessStatisticsService;
import com.jiuy.web.controller.util.ExcelUtil;
import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.util.anno.AdminOperationLog;
import com.jiuyuan.util.anno.Login;
import com.jiuyuan.web.help.JsonResponse;

/**
* @author WuWanjian
* @version 创建时间: 2016年11月7日 下午3:37:14
*/

@Controller
@Login
@RequestMapping("/storeorder")
public class PushBusinessStatisticsController {
	
	@Resource
	private PushBusinessStatisticsService pushBusinessStatisticsService;	
	
    /**
     * 导出推荐人统计EXCEL
     * @param response
     * @param startTime
     * @param endTime
     * @throws IOException
     * @throws ParseException 
     */
    @RequestMapping("/getReferrerExcel")
    @AdminOperationLog
    @ResponseBody
	public JsonResponse getReferrerExcel(HttpServletResponse response,
                         @RequestParam(value = "starttime", required = false, defaultValue = "") String startTime,
                         @RequestParam(value = "endtime", required = false, defaultValue = "") String endTime) throws IOException, ParseException {
    	JsonResponse jsonResponse = new JsonResponse();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat simpleDateFormatToday = new SimpleDateFormat("MM-dd");
        long startTimeLong = simpleDateFormat.parse(startTime).getTime();
        long endTimeLong = 0;
        if(StringUtils.isEmpty(endTime)){
        	Calendar calendar = Calendar.getInstance();  
        	//将小时至0  
        	calendar.set(Calendar.HOUR_OF_DAY, 0);  
        	//将分钟至0  
        	calendar.set(Calendar.MINUTE, 0);  
        	//将秒至0  
        	calendar.set(Calendar.SECOND,0);  
        	//将毫秒至0  
        	calendar.set(Calendar.MILLISECOND, 0);  
        	endTimeLong = calendar.getTimeInMillis();
        }else if(startTime.equals(endTime) || startTimeLong>=(System.currentTimeMillis()-60 * 60 * 24 * 1000)){
        	endTimeLong = startTimeLong + 60 * 60 * 24 * 1000;
        }else{
        	endTimeLong = simpleDateFormat.parse(endTime).getTime();
        }
        if(startTimeLong>endTimeLong){
        	return jsonResponse.setError("时间不正确,请确认");
        }
        
        List<Map<String, Object>> referrerList = pushBusinessStatisticsService.getReferrerList(startTimeLong, endTimeLong);
//		System.out.println(referrerList);
        List<String> columnNamesList = new ArrayList<String>();
        List<String> keysList = new ArrayList<String>();
        columnNamesList.add("推荐人手机号码");
        columnNamesList.add("总注册数");
        columnNamesList.add("总激活数");
        keysList.add("phone");
        keysList.add("allRegisterCount");
        keysList.add("allRctivateCount");
//        int index = 1;
        while(startTimeLong<endTimeLong){
			long time = startTimeLong + 60 * 60 * 24 * 1000;
			String today = simpleDateFormatToday.format(new Date(startTimeLong));
			columnNamesList.add(today+"新增注册数");
			columnNamesList.add(today+"新增激活数");
			keysList.add(today + "RegisterCount");
			keysList.add(today + "ActivateCount");
			startTimeLong = time;
//			index ++;
		}
//        System.out.println(columnNamesList);
//        System.out.println(keysList);
        String columnNames[] = columnNamesList.toArray(new String[columnNamesList.size()]);//列名
        String keys[] = keysList.toArray(new String[keysList.size()]);//map中的key
        
        ExcelUtil.exportExcel(response, referrerList, keys, columnNames, "推荐人统计表");
        
        return jsonResponse.setResultCode(ResultCode.COMMON_SUCCESS);
	}
    
    /**
     * 导出地区统计EXCEL
     * @param response
     * @param startTime
     * @param endTime
     * @throws IOException
     * @throws ParseException 
     */
    @RequestMapping("/getAreaExcel")
    @AdminOperationLog
    @ResponseBody
	public JsonResponse getAreaExcel(HttpServletResponse response,
                         @RequestParam(value = "starttime", required = false, defaultValue = "") String startTime,
                         @RequestParam(value = "endtime", required = false, defaultValue = "") String endTime) throws IOException, ParseException {
    	JsonResponse jsonResponse = new JsonResponse();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat simpleDateFormatToday = new SimpleDateFormat("MM-dd");
        long startTimeLong = simpleDateFormat.parse(startTime).getTime();
        long endTimeLong = 0;
        if(StringUtils.isEmpty(endTime)){
        	Calendar calendar = Calendar.getInstance();  
        	//将小时至0  
        	calendar.set(Calendar.HOUR_OF_DAY, 0);  
        	//将分钟至0  
        	calendar.set(Calendar.MINUTE, 0);  
        	//将秒至0  
        	calendar.set(Calendar.SECOND,0);  
        	//将毫秒至0  
        	calendar.set(Calendar.MILLISECOND, 0);  
        	endTimeLong = calendar.getTimeInMillis();
        }else if(startTime.equals(endTime) || startTimeLong>=(System.currentTimeMillis()-60 * 60 * 24 * 1000)){
        	endTimeLong = startTimeLong + 60 * 60 * 24 * 1000;
        }else{
        	endTimeLong = simpleDateFormat.parse(endTime).getTime();
        }
        if(startTimeLong>endTimeLong){
        	return jsonResponse.setError("时间不正确,请确认");
        }
        
        List<Map<String, Object>> areaList = pushBusinessStatisticsService.getAreaList(startTimeLong, endTimeLong);
		
        List<String> columnNamesList = new ArrayList<String>();
        List<String> keysList = new ArrayList<String>();
        columnNamesList.add("推荐人所在省");
        columnNamesList.add("总注册数");
        columnNamesList.add("总激活数");
        keysList.add("province");
        keysList.add("allRegisterCount");
        keysList.add("allRctivateCount");
//        int index = 1;
        while(startTimeLong<endTimeLong){
			long time = startTimeLong + 60 * 60 * 24 * 1000;
			String today = simpleDateFormatToday.format(new Date(startTimeLong));
			columnNamesList.add(today+"新增注册数");
			columnNamesList.add(today+"新增激活数");
			keysList.add(today + "RegisterCount");
			keysList.add(today + "ActivateCount");
			startTimeLong = time;
//			index++;
		}
        String columnNames[] = columnNamesList.toArray(new String[columnNamesList.size()]);//列名
        String keys[] = keysList.toArray(new String[keysList.size()]);//map中的key
        
        ExcelUtil.exportExcel(response, areaList, keys, columnNames, "地区统计表");
        
        return jsonResponse.setResultCode(ResultCode.COMMON_SUCCESS);
	}
}
