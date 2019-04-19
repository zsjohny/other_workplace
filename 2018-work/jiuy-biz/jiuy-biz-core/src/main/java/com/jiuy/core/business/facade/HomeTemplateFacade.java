package com.jiuy.core.business.facade;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.jiuy.core.dao.mapper.StatisticsDao;
import com.jiuy.core.meta.homepage.HomeTemplate;
import com.jiuy.core.service.homepage.HomeTemplateService;
import com.jiuyuan.constant.StatisticsPageId;
import com.jiuyuan.constant.StatisticsType;
import com.jiuyuan.entity.Statistics;

@Service
public class HomeTemplateFacade {

	@Autowired
	private HomeTemplateService homeTemplateService;
	
	@Autowired
	private StatisticsDao statisticsDao;
	
	@Transactional
	public void add(int pageId,long floorId, HomeTemplate ht) {
		homeTemplateService.addTemplate(floorId, ht);
		StringBuilder builder = new StringBuilder("P" + pageId + "L" + floorId);
		String name = ht.getName();
		String m = name.substring(name.lastIndexOf("-"));
		builder.append("M" + m + "N");
//		String code = "P" + pageId + "L" + floorId + "M" + templateId + "N" + sort + "ID" + id;
		String content = ht.getContent();
		JSONArray jsonArray = JSON.parseArray(content);
		
		List<Statistics> list = new ArrayList<>();
		Long id = ht.getId();
		long time = System.currentTimeMillis();
		for (int i = 1; i <= jsonArray.size(); i++) {
			String code = builder.toString() + i + "ID" + id;
			Statistics statistics = new Statistics();
			statistics.setCode(code);
			statistics.setType(StatisticsType.HOME_PAGE.getIntValue());
			statistics.setPageId(StatisticsPageId.HOME_PAGE.getIntValue());
			statistics.setCreateTime(time);
			statistics.setUpdateTime(time);
			
			list.add(statistics);
			
			i ++;
		}
		
		statisticsDao.add(list);
		
	}
	
	
	
}
