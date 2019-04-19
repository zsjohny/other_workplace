package com.jiuy.core.service.homepage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.jiuy.core.dao.HomeFloorDao;
import com.jiuy.core.dao.HomeTemplateDao;
import com.jiuy.core.dao.mapper.StatisticsDao;
import com.jiuy.core.exception.ParameterErrorException;
//import com.jiuy.core.dao.mapper.StatisticsDao;
import com.jiuy.core.meta.homepage.HomeFloor;
import com.jiuy.core.meta.homepage.HomeFloorVO;
import com.jiuy.core.meta.homepage.HomeTemplate;
import com.jiuyuan.constant.AdminConstants;
import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.entity.query.PageQuery;

@Service
public class HomeFloorServiceImpl implements HomeFloorService {
	
	@Resource
	private HomeFloorDao homeFloorDao;
	
	@Resource
	private HomeTemplateDao homeTemplateDao;
	
	@Resource
	private HomeTemplateService homeTemplateService;
	
	@Autowired
	private StatisticsDao statisticsDao;
	

	@Override
	public List<HomeFloorVO> search(PageQuery query, String name, int sequence, long activityPlaceId) {
		
		return homeFloorDao.search(query, name, sequence, activityPlaceId, AdminConstants.EXCLUDE_TEMPLATE_NAMES);
	}

	@Override
	public int searchCount(String name, long activityPlaceId) {
		return homeFloorDao.searchCount(name, activityPlaceId, AdminConstants.EXCLUDE_TEMPLATE_NAMES);
	}

	@Override
	public ResultCode addHomeFloor(HomeFloor homeFloor) {
		long createTime = System.currentTimeMillis();
		
		homeFloor.setCreateTime(createTime);
		homeFloor.setUpdateTime(createTime);
		
		homeFloorDao.addHomeFloor(homeFloor);
		
		return ResultCode.COMMON_SUCCESS;
	}

	@Override
	public ResultCode removeHomeFloor(long id) {
		homeFloorDao.removeHomeFloor(id);
		
		return ResultCode.COMMON_SUCCESS;
	}

	@Override
	public ResultCode updateHomeFloor(HomeFloor homeFloor) {
		homeFloorDao.updateHomeFloor(homeFloor);
		
		return ResultCode.COMMON_SUCCESS;
	}

	@Override
	public HomeFloorVO searchById(long floorId) {
		return homeFloorDao.searchById(floorId);
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public ResultCode publishHomeFloor(long activityPlaceId) {
		
		homeTemplateDao.removeDirtyData(activityPlaceId);
		homeFloorDao.publishHomeFloor(activityPlaceId);
		
		return ResultCode.COMMON_SUCCESS;
	}

	@Override
	public List<Map<String, Object>> preview(long activityPlaceId) {
		List<Map<String, Object>> floors = homeFloorDao.preview(activityPlaceId);
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		
		for(Map<String, Object> floor : floors) {
			if(floor == null) {
				continue;
			}
			
			String content = (String)floor.get("content");
			String name = (String)floor.get("name");
			
			if(StringUtils.equals("", content)) {
				continue;
			}
			
			Map<String, Object> map = new HashMap<String, Object>();
			JSONArray jsonArray = JSONArray.parseArray(content);
			
			map.put("content", jsonArray);
			map.put("name", name);
			
			list.add(map);
		}
		
		return list;
	}

	@Override
	public List<HomeFloorVO> search(PageQuery pageQuery, String name, Integer sequence, Integer type, Long relatedId) {
		return homeFloorDao.search(pageQuery, name, sequence, type, relatedId);
	}

	@Override
	public int searchCount(String name, Integer type, Long relatedId) {
		return homeFloorDao.searchCount(name, type, relatedId);
	}

	@Override
	public int add(HomeFloor homeFloor2) {
		return homeFloorDao.add(homeFloor2);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public int remove(Long id) {
		statisticsDao.closeCodeByRemove("L" + id, System.currentTimeMillis());
		return homeFloorDao.remove(id);
	}

	@Override
	public int update(HomeFloor homeFloor2) {
		return homeFloorDao.update(homeFloor2);
	}

	@Override
	public HomeFloorVO search(Long id) {
		return homeFloorDao.search(id);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void publish(Integer type, Long relatedId) {
		Set<Long> unpublishTemplateIds = new HashSet<>();
		Set<Long> publishedTemplateIds = new HashSet<>();
		List<HomeFloorVO> hfvos = homeFloorDao.search(null, null, null, type, relatedId);
		for (HomeFloorVO homeFloorVO : hfvos) {
			String htName = homeFloorVO.getHomeTemplateName();
			if (htName == null) {
				throw new ParameterErrorException(homeFloorVO.getId() + "模板名为空!");
			}
			unpublishTemplateIds.add(homeFloorVO.getNextHomeTemplateId());
			publishedTemplateIds.add(homeFloorVO.getHomeTemplateId());
		}
		
		//去掉交集(已发布的集合模板)
		Set<Long> intersection = new HashSet<>(unpublishTemplateIds);
		intersection.retainAll(publishedTemplateIds);
		unpublishTemplateIds.removeAll(intersection);
		publishedTemplateIds.removeAll(intersection);
		
		Set<Long> allTemplateIds = new HashSet<>(unpublishTemplateIds);
		allTemplateIds.addAll(publishedTemplateIds);
		
		Map<Long, HomeTemplate> hoMap = homeTemplateService.templateOfIds(allTemplateIds);
				
    	Set<Long> unpublishCodeSet = gatherCodeSet(unpublishTemplateIds, hoMap);
		Set<Long> publishedCodeSet = gatherCodeSet(publishedTemplateIds, hoMap);
		
		long time = System.currentTimeMillis();
		if (publishedCodeSet.size() > 0) {
			statisticsDao.closeCode(publishedCodeSet, time);
		}
		if (unpublishCodeSet.size() > 0) {
			statisticsDao.startCode(unpublishCodeSet, time);
		}
		
		homeFloorDao.publish(type, relatedId);
	}
	
	
	
	private Set<Long> gatherCodeSet(Set<Long> unpublishTemplateIds, Map<Long, HomeTemplate> hoMap) {
		String regEx = "\"code\":\"([A-Z a-z 0-9_]*)\"";
    	Pattern pat = Pattern.compile(regEx);
    	Matcher mat;
    	Set<Long> set = new HashSet<>();
		for (Long unpublishTemplateId : unpublishTemplateIds) {
			HomeTemplate ht = hoMap.get(unpublishTemplateId);
			if (ht == null) {
				continue;
			}
			String content = ht.getContent();
			if (StringUtils.equals(null, content) || StringUtils.equals("", content)) {
				continue;
			}
			mat = pat.matcher(content);
			while (mat.find()) {
				if (!StringUtils.equals("", mat.group(1)) && isInteger(mat.group(1))) {
					set.add(Long.parseLong(mat.group(1)));       			
				}
    		}
		}
		return set;
	}

	public static boolean isInteger(String str) {    
	    Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");    
	    return pattern.matcher(str).matches();    
	}  

	@Override
	public List<Map<String, Object>> preview(Integer type, Long relatedId) {
		return homeFloorDao.preview(type, relatedId);
	}
}
