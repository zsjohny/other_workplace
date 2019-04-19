package com.jiuy.core.service.homepage;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jiuy.core.dao.HomeFloorDao;
import com.jiuy.core.dao.HomeTemplateDao;
import com.jiuy.core.meta.homepage.HomeTemplate;
import com.jiuyuan.constant.ResultCode;

@Service
public class HomeTemplateServiceImpl implements HomeTemplateService {
	
	@Resource
	private HomeTemplateDao homeTemplateDaoImpl;
	
	@Resource
	private HomeFloorDao homeFloorDaoSqlImpl;
	
	@Override
	public List<HomeTemplate> loadTemplates(String name) {
		return homeTemplateDaoImpl.loadTemplates(name);
	}

	@Override
	public HomeTemplate loadTemplate(Long homeTemplateId) {
		return homeTemplateDaoImpl.loadTemplate(homeTemplateId);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public HomeTemplate addTemplate(long floorId, HomeTemplate ht) {
		long createTime = System.currentTimeMillis();
		
		ht.setUpdateTime(createTime);
		ht.setCreateTime(createTime);
		
		HomeTemplate newHT = homeTemplateDaoImpl.addTemplate(ht);
		homeFloorDaoSqlImpl.updateHomeTemplateId(floorId, newHT.getId());
		
		return ht;
	}

	@Override
	public ResultCode updateTemplate(HomeTemplate ht) {
		long updateTime = System.currentTimeMillis();
		
		ht.setUpdateTime(updateTime);
		homeTemplateDaoImpl.updateTemplate(ht);
		
		return ResultCode.COMMON_SUCCESS;
	}

	@Override
	public HomeTemplate add(long floorId, HomeTemplate ht) {
		addTemplate(floorId, ht);
		
		return ht;
	}

	@Override
	public Map<Long, HomeTemplate> templateOfIds(Collection<Long> ids) {
		if (ids.size() < 1) {
			return new HashMap<>();
		}
		return homeTemplateDaoImpl.templateOfIds(ids);
	}
	

}
