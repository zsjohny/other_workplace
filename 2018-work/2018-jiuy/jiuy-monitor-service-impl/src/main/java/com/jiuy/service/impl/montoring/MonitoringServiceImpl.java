package com.jiuy.service.impl.montoring;
import java.util.*;
import com.jiuy.base.enums.GlobalsEnums;
import com.jiuy.base.exception.BizException;
import com.jiuy.base.model.MyPage;
import com.jiuy.base.util.Biz;
import com.jiuy.model.brand.Brand;
import com.jiuy.model.common.DataDictionary;
import com.jiuy.service.brand.IBrandService;
import com.jiuy.service.common.IDictionaryService;
import com.jiuy.mapper.monitoring.MonitoringPageReportMapper;
import com.jiuy.mapper.monitoring.MonitoringSmsMapper;
import com.jiuy.mapper.monitoring.MonitoringTotalReportMapper;
import com.jiuy.model.monitoring.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.jiuy.base.util.Globals;
import com.jiuy.mapper.monitoring.MonitoringMetadataMapper;
import com.jiuy.service.monitoring.IMonitoringHandlerService;
import com.jiuy.service.monitoring.IMonitoringService;

/**
 * 统计接口的实现类
 * @author Aison
 * @date  2018年4月18日 下午2:05:14
 * @Copyright 玖远网络
 */
@Service("monitoringService")
@Log4j2
public class MonitoringServiceImpl implements IMonitoringService {

	private final MonitoringMetadataMapper monitoringMetadataMapper;
	private final IMonitoringHandlerService monitoringHandlerService;
	private final MonitoringTotalReportMapper monitoringTotalReportMapper;
	private final MonitoringPageReportMapper monitoringPageReportMapper;
	private final MonitoringSmsMapper monitoringSmsMapper;
	private final IDictionaryService dictionaryService;
	private final IBrandService brandService;

	@Autowired
	public MonitoringServiceImpl(MonitoringMetadataMapper monitoringMetadataMapper, IMonitoringHandlerService monitoringHandlerService, MonitoringTotalReportMapper monitoringTotalReportMapper, MonitoringPageReportMapper monitoringPageReportMapper, MonitoringSmsMapper monitoringSmsMapper, IDictionaryService dictionaryService, IBrandService brandService) {
		this.monitoringMetadataMapper = monitoringMetadataMapper;
		this.monitoringHandlerService = monitoringHandlerService;
		this.monitoringTotalReportMapper = monitoringTotalReportMapper;
		this.monitoringPageReportMapper = monitoringPageReportMapper;
		this.monitoringSmsMapper = monitoringSmsMapper;
		this.dictionaryService = dictionaryService;
		this.brandService = brandService;
	}

	/**
	 * 存储一个元数据
	 * @param monitoringMetadata 元素据封装
	 */
	@Override
	public void acceptData(MonitoringMetadata monitoringMetadata) {
		if(!Biz.isNotEmpty(monitoringMetadata.getData())) {
			throw new BizException(GlobalsEnums.PARAM_ERROR);
		}
		monitoringMetadata.setCreateTime(new Date());
		monitoringMetadata.setParseCount(0);
		monitoringMetadata.setStatus(Globals.DISABLED);
		monitoringMetadata.setParseResult("");
		monitoringMetadataMapper.insertSelective(monitoringMetadata);
	}

	/**
	 * 数据解析
	 */
	@Override
	public void doParseData() {
		MonitoringMetadataQuery metadata = new MonitoringMetadataQuery();
		metadata.setStatus(Globals.DISABLED);
		metadata.setRetryCount(Globals.RETRY);
		metadata.setOffset(1);
		//一次查询1000条进行处理
		metadata.setLimit(1000);
		List<MonitoringMetadata> metadatas =  monitoringMetadataMapper.selectList(metadata);
		metadatas.forEach(action->{
			try {
				monitoringHandlerService.doDataParse(action);
			} catch (Exception e) {
				e.printStackTrace();
				// 这里只有失败的才会更新 也就是status = 0的
				action.setParseCount(action.getParseCount()+1);
				action.setParseResult(Biz.getFullException(e));
				monitoringMetadataMapper.updateWithVersion(action);
			}
		});
	}

	/**
	 * 接收短信推广打开数据
	 * @param monitoringSms 需要接收的数据封装
	 * @date   2018/4/24 15:20
	 * @author Aison
	 */
	@Override
	public void acceptSmsLog(MonitoringSms monitoringSms,String brandId,String pageCode) {

		if(Biz.hasEmpty(monitoringSms.getUrl(),monitoringSms.getPageCode())) {
			throw BizException.def().msg("推广地址,视频地址不能为空");
		}
		if(Biz.hasEmpty(monitoringSms.getOpenTime())) {
			monitoringSms.setOpenTime(new Date());
		}

		String pageName ;
		// 如果是品牌页面则判断品牌是否存在
		String brandmian = "brandMain";
		if(brandmian.equals(pageCode)) {
			Brand brand = brandService.getByBrandId(Long.valueOf(brandId));
			if(brand == null) {
				throw BizException.def().msg("品牌获取失败");
			}
			pageName = brand.getBrandName();
		} else {
			// 通过页面编码查询页面名称
			DataDictionary dictionary = dictionaryService.getByCode(monitoringSms.getPageCode(),"page_group");
			if(dictionary== null) {
				throw BizException.def().msg("页面不存在");
			}
			pageName = dictionary.getVal();
		}
		monitoringSms.setPageName(pageName);
		monitoringSms.setCreateTime(new Date());
		monitoringSmsMapper.insertSelective(monitoringSms);
	}

	/**
	 * 查询短信统计列表
	 * @param param 参数封装
	 * @date  2018/4/24 16:17
	 * @author Aison
	 */
	@Override
	public MyPage<Map<String,Object>> smsReport(Map<String,Object> param) {
		return new MyPage<>(monitoringSmsMapper.selectSmsReport(Biz.getPageMap(param)));
	}

	/**
	 * 打开设备统计报表
	 * @param query 查询实体
	 * @date 2018/4/24 15:19
	 * @author Aison
	 */
	@Override
	public Map<String, Object> deviceReportPage(MonitoringTotalReportQuery query) {
		Map<String,Object> deviceReports = new HashMap<>(20);
		deviceReports.put("rows",monitoringTotalReportMapper.selectList(query));
		MonitoringTotalReport totalReport = monitoringTotalReportMapper.sumTotalReport(query.getBeginDate(),query.getEndDate());
		deviceReports.put("sum",totalReport);
		return deviceReports;
	}

	/**
	 * 页面统计报表列表
	 * @param query 查询实体
	 * @date 2018/4/24 15:19
	 * @author Aison
	 */
	@Override
	public Map<String,Object> pageReportPage(MonitoringPageReportQuery query) {

		Map<String,Object> pageReports = new HashMap<>(20);
		pageReports.put("rows",monitoringPageReportMapper.selectList(query));
		List<MonitoringPageReport> pageReport = monitoringPageReportMapper.sumPageReport(query.getBeginDate(),query.getEndDate());
		pageReports.put("sum",pageReport);
		return pageReports;

	}

	/**
	 * 自动解析数据定时器
	 * @date 2018/4/24 15:18
	 * @author Aison
	 */
	@Override
	public String autoParseData() {

		StringBuilder stringBuilder = new StringBuilder();
		log.info("开始执行数据解析定时器=====>");
		try{
			doParseData();
			log.info("结束执行数据解析定时器=====>");
			stringBuilder.append("结束执行数据解析定时器");
		}catch (Exception e) {
			log.info("执行数据解析定时器失败=====>");
		    e.printStackTrace();
			stringBuilder.append(Biz.getFullException(e));
		}
		return stringBuilder.toString();
	}

	/**
	 * 自动统计报表
	 * @date 2018/4/24 15:18
	 * @author Aison
	 */
	@Override
	public String autoReport() {

		StringBuilder stringBuilder = new StringBuilder();
		log.info("开始执行报表统计定时器");
		try {
			monitoringHandlerService.reportPage(null);
			log.info("页面访问统计完成");
			stringBuilder.append("页面访问统计完成 \r\n");
		} catch (Exception e) {
			e.printStackTrace();
			log.info("页面访问统计失败");
			stringBuilder.append(Biz.getFullException(e));
		}
		try {
			monitoringHandlerService.reportDevice(null);
			log.info("设备统计完成");
			stringBuilder.append("设备统计完成 \r\n");
		} catch (Exception e) {
			e.printStackTrace();
			log.info("设备统计失败");
			stringBuilder.append(Biz.getFullException(e));
		}

		log.info("结束执行报表统计定时器");
		return stringBuilder.toString();
	}

}

