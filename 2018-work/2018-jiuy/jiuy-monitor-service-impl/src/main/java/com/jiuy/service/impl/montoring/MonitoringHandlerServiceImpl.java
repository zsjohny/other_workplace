package com.jiuy.service.impl.montoring;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jiuy.model.common.DataDictionary;
import com.jiuy.service.common.ICacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.jiuy.base.enums.GlobalsEnums;
import com.jiuy.base.exception.BizException;
import com.jiuy.base.util.Biz;
import com.jiuy.base.util.Globals;
import com.jiuy.mapper.monitoring.MonitoringDeviceMapper;
import com.jiuy.mapper.monitoring.MonitoringMetadataMapper;
import com.jiuy.mapper.monitoring.MonitoringPageMapper;
import com.jiuy.mapper.monitoring.MonitoringPageReportMapper;
import com.jiuy.mapper.monitoring.MonitoringTotalReportMapper;
import com.jiuy.model.monitoring.MetadataTemp;
import com.jiuy.model.monitoring.MonitoringDevice;
import com.jiuy.model.monitoring.MonitoringPage;
import com.jiuy.model.monitoring.MonitoringPageReport;
import com.jiuy.model.monitoring.MonitoringTotalReport;
import com.jiuy.model.monitoring.MonitoringMetadata;
import com.jiuy.service.monitoring.IMonitoringHandlerService;

/**   
 * @ClassName:  DeviceMonitoringServiceImpl   
 * @Description: 处理监控数据的service
 * @author: Aison 
 * @date:   2018年4月18日 下午2:56:17    
 * @Copyright: 玖远网络
 */  
@Service("monitoringHandlerService")
public class MonitoringHandlerServiceImpl implements IMonitoringHandlerService{

	
	@Autowired
	private MonitoringDeviceMapper monitoringDeviceMapper;
	@Autowired
	private MonitoringPageMapper monitoringPageMapper;
	@Autowired
	private MonitoringMetadataMapper monitoringMetadataMapper;
	@Autowired
	private MonitoringTotalReportMapper monitoringTotalReportMapper;
	@Autowired
	private MonitoringPageReportMapper monitoringPageReportMapper;

	@Autowired
	private ICacheService cacheService;


	
	/**   
	 * <p>Title: doDataParse</p>   
	 * <p>Description: 解析出来数据插入到对应的表里面..修改元数据的状态..需要事务支持 </p>   
	 * @param metadata   
	 * @see IMonitoringHandlerService#doDataParse(com.jiuy.model.monitoring.MonitoringMetadata)
	 */  
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void doDataParse(MonitoringMetadata metadata) {
		String data = metadata.getData();
		@SuppressWarnings("unchecked")
		List<MetadataTemp> metadataTemps = Biz.jsonStrToListObject(data, List.class, MetadataTemp.class);
		List<MonitoringPage> pages = new ArrayList<>();
		List<MonitoringDevice> devices = new ArrayList<>();

		metadataTemps.forEach(action->{
			Integer type = action.getType();
			// 设备打开的记录
			if(type == 1) {

				action.getData().forEach(dataItem ->{

					MonitoringDevice device = new MonitoringDevice();
					device.setDeviceId(dataItem.getDeviceId());
					device.setOpenTime(Biz.timestamp2Date(dataItem.getOpenTime()));
					device.setCloseTime(Biz.timestamp2Date(dataItem.getCloseTime()));
					device.setPhone(dataItem.getPhone());
					device.setOsName(dataItem.getOsName());
					device.setVersion(dataItem.getVersion());
					device.setCreateTime(new Date());
					Long stayTime = Biz.subDate(device.getOpenTime(), device.getCloseTime());
					device.setAppStayTime(stayTime/1000);
					devices.add(device);
				});

			} else if(type == 2 ) {
				action.getData().forEach(dataItem ->{
					MonitoringPage page = new MonitoringPage();
					String pageCode = dataItem.getPageCode();
					DataDictionary dd = cacheService.getByCode(pageCode);

					page.setDeviceId(dataItem.getDeviceId());
					page.setEnterTime(Biz.timestamp2Date(dataItem.getEnterTime()));
					page.setLeaveTime(Biz.timestamp2Date(dataItem.getLeaveTime()));
					page.setPhone(dataItem.getPhone());
					page.setOsName(dataItem.getOsName());
					page.setVersion(dataItem.getVersion());
					page.setCreateTime(new Date());
					page.setEventCount(dataItem.getEventCount());
					page.setPageName(dd.getVal());
					page.setPageCode(dataItem.getPageCode());
					Long stayTime = Biz.subDate(page.getEnterTime(), page.getLeaveTime());
					page.setPageStayTime(stayTime/1000);
					pages.add(page);
				});

			}
		});

		if(devices.size()>0) {
			monitoringDeviceMapper.insertBach(devices);
		}
		if(pages.size()>0) {
			monitoringPageMapper.insertBach(pages);
		}
		// 更新源数据状态
		metadata.setStatus(Globals.ENABLE);
		metadata.setParseCount(metadata.getParseCount()+1);
		metadata.setParseResult("成功");
		int rs = monitoringMetadataMapper.updateWithVersion(metadata);
		if(rs == 0) {
			throw new BizException(GlobalsEnums.DATA_IS_CHANGED);
		}
	}

	/**
	 * @Title:  reportDevice
	 * @Description:
	 * @param date 统计那天的数据 如果为空则统计当天的数据
	 * @date:   2018/4/19 10:30
	 * @author: Aison
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void reportDevice(Date date) {

		Date today = date == null ? new Date() : date;
		monitoringTotalReportMapper.delteOneDayReport(today);
		long stayCount = monitoringDeviceMapper.appStayTimeCount(today);
		long openAppCount = monitoringDeviceMapper.openAppCount(today);
		long openDeviceCount = monitoringDeviceMapper.opendDeviceCount(today);

		// 访问页面总数
		Long pvTotal = monitoringTotalReportMapper.oneDayPVTotal(today);
		MonitoringTotalReport totalReport = new MonitoringTotalReport();
		totalReport.setOpenAppCount(openAppCount);
		totalReport.setOpenAppDeviceCount(openDeviceCount);
		totalReport.setAppStayTimeCount(stayCount);
		totalReport.setReportTime(new Date());
		totalReport.setPageQueryCount(pvTotal);
		totalReport.setLastReportTime(new Date());
		totalReport.setReportDay(today);
		
		monitoringTotalReportMapper.insertSelective(totalReport);
	}


	/**
	 * @Title:  reportPage
	 * @Description: 页面访问统计
	 * @param date 需要统计那天的页面访问报表 如果为空则统计当天的
	 * @date:   2018/4/19 10:31
	 * @author: Aison
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void reportPage(Date date) {

		//首先删除历史的统计记录
		Date today = date == null ? new Date() : date;
		monitoringPageReportMapper.deleteByDate(today);

		List<Map<String,String>> pagePVs = monitoringPageMapper.pagePV(today);
		List<Map<String,String>> pageUVs = monitoringPageMapper.pageUV(today);
		List<Map<String,String>> pageStayTimes =  monitoringPageMapper.pageStayTimeCount(today);
		
		Map<String,Map<String,String>> uvMap = new HashMap<>();
		Map<String,Map<String,String>> stayTimesMap = new HashMap<>();
		pageUVs.forEach(action->{
			uvMap.put(action.get("pageCode"), action);
		});
		pageStayTimes.forEach(action->{
			stayTimesMap.put(action.get("pageCode"), action);
		});
		List<MonitoringPageReport> pageReports = new ArrayList<>();
		pagePVs.forEach(action->{
			String pageCode = action.get("pageCode");
			MonitoringPageReport pageReport = new MonitoringPageReport();
			Object pagePV = action.get("pagePV");
			pageReport.setPagePv(Long.valueOf(pagePV.toString()));
			pageReport.setPageCode(pageCode);
			pageReport.setPageName(action.get("pageName"));
			Object eventCount = action.get("eventCount");
			pageReport.setEventCount(Long.valueOf(eventCount.toString()));
			Map<String,String> mapUV  = uvMap.get(pageCode);
			Map<String,String> mapTimeCount  = stayTimesMap.get(pageCode);
			Object pageUV  = mapUV.get("pageUV");
			pageReport.setPageUv(Long.valueOf(pageUV.toString()));
			Object pageStayTimeCount = mapTimeCount.get("pageStayTimeCount");
			pageReport.setPageStayTotal(Long.valueOf(pageStayTimeCount.toString()));
			pageReport.setReportTime(new Date());
			pageReport.setLastReportTime(new Date());
			pageReport.setReportDay(today);
			pageReports.add(pageReport);
		});
		if(pageReports.size()>0) {
			monitoringPageReportMapper.insertBach(pageReports);
		}
	}
	
}	
