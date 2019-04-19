package com.jiuy.core.business.facade;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuy.core.dao.HomeTemplateDao;
import com.jiuy.core.meta.clickstatistics.ClickStatistics;
import com.jiuy.core.meta.clickstatistics.ClickStatisticsSearch;
import com.jiuy.core.meta.clickstatistics.ClickStatisticsVO;
import com.jiuy.core.meta.homepage.HomeTemplate;
import com.jiuy.core.service.ClickStatisticsServiceImpl;
import com.jiuyuan.entity.query.PageQuery;

@Service
public class ClickStatisticsFacade {
	
	@Autowired
	private ClickStatisticsServiceImpl clickStatisticsService;
	
	@Autowired
	private HomeTemplateDao homeTemplateDao;
	
	public List<ClickStatisticsVO> search(ClickStatisticsSearch clickStatisticsSearch,long startTime,long endTime,PageQuery pageQuery,int sort){
		//根据楼层名称搜索出楼层id
		if(!StringUtils.equals("", clickStatisticsSearch.getFloorName())){
			List<Long> idsOfFloorName = clickStatisticsService.getIdsOfFloorName(clickStatisticsSearch.getFloorName());
			if(idsOfFloorName.size()>0){
				clickStatisticsSearch.setFloorIds(idsOfFloorName);
			}
		}
		
		String template_name = clickStatisticsSearch.getTemplateId();
		if (!StringUtils.equals("", template_name)) {
			List<HomeTemplate> homeTemplates = homeTemplateDao.getByName(template_name);
			if (homeTemplates.size() > 0) {
				StringBuilder builder = new StringBuilder();
				for (HomeTemplate homeTemplate : homeTemplates) {
					builder.append(homeTemplate.getId() + "|");
				}
				builder.deleteCharAt(builder.length() - 1);
				clickStatisticsSearch.setTemplateId(builder.toString());
			}
		}
		
		List<ClickStatistics> clickStatisticsList = clickStatisticsService.search(clickStatisticsSearch, startTime, endTime, pageQuery,sort);
		
		ArrayList<ClickStatisticsVO> clickStatisticsVOs = new ArrayList<ClickStatisticsVO>();
		for(ClickStatistics clickStatistics : clickStatisticsList){
			ClickStatisticsVO clickStatisticsVO = new ClickStatisticsVO();
			clickStatisticsVOs.add(clickStatisticsVO);
			
			BeanUtils.copyProperties(clickStatistics, clickStatisticsVO);
			
			String[] parsingCode = parsingCode(clickStatistics.getCode());
			clickStatisticsVO.setFloorId(parsingCode[1]);
			clickStatisticsVO.setTemplateId(parsingCode[2]);
			clickStatisticsVO.setSerialNumber(parsingCode[3]);
			clickStatisticsVO.setElementId(parsingCode[4]);
			
			String floorName = clickStatisticsService.searchFloorName(parsingCode[1]);
			clickStatisticsVO.setFloorName(floorName);
			String templateImgUrl = clickStatisticsService.searchTemplateImgUrl(parsingCode[2]);
			clickStatisticsVO.setTemplateImgUrl(templateImgUrl);
		}
		
		return clickStatisticsVOs;
	}
	
	private String[] parsingCode(String code){
		String[] codeArray = code.split("L|M|N|ID");
		return codeArray;
	}
	
}
