package com.jiuy.monitoring.controller;

import com.jiuy.base.model.MyPage;
import com.jiuy.base.util.ResponseResult;
import com.jiuy.model.monitoring.MonitoringPageReportQuery;
import com.jiuy.model.monitoring.MonitoringSms;
import com.jiuy.model.monitoring.MonitoringTotalReportQuery;
import com.jiuy.service.common.ICacheService;
import com.jiuy.service.monitoring.IMonitoringService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.jiuy.model.monitoring.MonitoringMetadata;
import com.jiuy.service.monitoring.IMonitoringHandlerService;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**   
 * 处理统计的controller
 * @author Aison
 * @date   2018年4月18日 下午6:38:59
 * @Copyright 玖远网络
 */  
@Controller
public class MonitoringController {
	
	private final IMonitoringService monitoringService;
	private final IMonitoringHandlerService monitoringHandlerService;
	private final ICacheService cacheService;

	@Autowired
	public MonitoringController(IMonitoringHandlerService monitoringHandlerService, IMonitoringService monitoringService, ICacheService cacheService) {
		this.monitoringHandlerService = monitoringHandlerService;
		this.monitoringService = monitoringService;
		this.cacheService = cacheService;
	}

	/**
	 * 首页跳转
	 * @author Aison
	 * @date 2018/6/8 13:47
	 */
	@RequestMapping("/")
	public String main() {
		return "index";
	}

	/**
	 * 短信统计 类似 http://localhost/index 这样的地址
	 * @param uri 地址uri
	 * @param request request对象
	 * @author Aison
	 * @date 2018/6/6 15:19
	 */
	@RequestMapping("/{uri}.htm")
	public String index(@PathVariable("uri") String uri, HttpServletRequest request) {

		addSms(uri,request.getRequestURL().toString(),null);
		return "shortMessage";
	}
	
	/**
	 * 短信品牌统计 类似 http://localhost/brandMain/1 这样的页面
	 * @param id id
	 * @param request request
	 * @author Aison
	 * @date 2018/6/6 15:20
	 */
	@RequestMapping("/brandMain/{id}")
	public String indexId(@PathVariable("id") String id, HttpServletRequest request) {

		addSms("brandMain",request.getRequestURL().toString(),id);
		return "shortMessage";
	}

	/**
	 * 添加短信设备统计
	 * @param uri uri
	 * @param url url
	 * @param id  id
	 * @author Aison
	 * @date 2018/6/6 15:21
	 */
	private void addSms(String uri,String url,String id) {
		try{
			MonitoringSms monitoringSms = new MonitoringSms();
			if(id==null) {
				monitoringSms.setPageCode(uri);
			} else {
				monitoringSms.setPageCode(uri+"-"+id);
			}
			monitoringSms.setCreateTime(new Date());
			monitoringSms.setOpenTime(new Date());
			monitoringSms.setUrl(url);
			monitoringService.acceptSmsLog(monitoringSms,id,uri);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 接收数据
	 * @param monitoringMetadata 元素据封装
	 * @return String
	 */
	@ResponseBody
	@RequestMapping("acceptData")
	public ResponseResult acceptData(MonitoringMetadata monitoringMetadata) {
		monitoringService.acceptData(monitoringMetadata);
		return ResponseResult.SUCCESS;
	}
	
	
	/**
	 * 接收数据
	 *
	 * @return String
	 */
	@ResponseBody
	@RequestMapping("parseData")
	public ResponseResult parseData() {
		monitoringService.doParseData();
		return ResponseResult.SUCCESS;
	}

	/**
	 * 统计某天的设备报表
	 * @param date 数据
	 * @date   2018/4/19 14:08
	 * @author Aison
	 */
	@ResponseBody
	@RequestMapping("/reportDevice")
	public ResponseResult reportDevice(Date date) {
		monitoringHandlerService.reportDevice(date);
		return ResponseResult.SUCCESS;
	}

	/**
	 * 统计某天的页面打开报表
	 * @param date 日期
	 * @date  2018/4/19 14:08
	 * @author Aison
	 */
	@ResponseBody
	@RequestMapping("/reportPage")
	public ResponseResult reportPage(Date date) {
		monitoringHandlerService.reportPage(date);
		return ResponseResult.SUCCESS;
	}

	/**
	 * 总览的分页
	 * @param query 查询实体
	 * @date   2018/4/19 14:24
	 * @author Aison
	 */
	@ResponseBody
	@RequestMapping("/totalReportList")
	public ResponseResult totalReportList(MonitoringTotalReportQuery query) {
		return new ResponseResult().success().setData(monitoringService.deviceReportPage(query));
	}

	/**
	 * 页面统计的分页
	 * @param query 查询实体
	 * @date  2018/4/19 14:24
	 * @author Aison
	 */
	@ResponseBody
	@RequestMapping("/pageReportList")
	public ResponseResult pageReportList(MonitoringPageReportQuery query) {
		return new ResponseResult().success().setData(monitoringService.pageReportPage(query));
	}

	/**
	 * 实时统计短信打开记录
	 * @param request begintTime  endTime  pageCode pageName
	 * @date  2018/4/24 16:23
	 * @author Aison
	 */
	@RequestMapping("/smsReport")
	@ResponseBody
	public MyPage<Map<String,Object>> smsReport(HttpServletRequest request) {

		return monitoringService.smsReport(WebUtil.getRequestMap(request));
	}


	/**
	 * 自动解析数据接口
	 * 提供给定时项目调用的接口
	 * @author Aison
	 * @date 2018/6/6 15:37
	 */
	@RequestMapping("/admin/qrtzParseData")
	@ResponseBody
	public ResponseResult qrtzParseData() {

		return ResponseResult.instance().success(monitoringService.autoParseData());
	}

	/**
	 * 自动统计数据接口
	 * 提供给定时项目调用的接口
	 * @author Aison
	 * @date 2018/6/6 15:37
	 */
	@RequestMapping("/admin/qrtzDoReport")
	@ResponseBody
	public ResponseResult qrtzDoReport() {
		String fields = monitoringService.getClass().getResource("").getFile();
		return ResponseResult.instance().success(monitoringService.autoReport());
	}


	/**
	 * 字符串日期转util日期
	 * @param binder binder
	 * @author Aison
	 * @date 2018/6/6 15:23
	 */
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));
	}
	
}
