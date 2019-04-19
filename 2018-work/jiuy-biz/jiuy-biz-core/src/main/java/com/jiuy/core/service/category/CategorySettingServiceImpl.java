package com.jiuy.core.service.category;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.jiuy.core.dao.mapper.CategorySettingDao;
import com.jiuy.core.exception.ParameterErrorException;
import com.jiuy.core.service.GlobalSettingService;
import com.jiuyuan.constant.GlobalSettingName;
import com.jiuyuan.entity.CategorySetting;
import com.jiuyuan.entity.query.PageQuery;

@Service
public class CategorySettingServiceImpl implements CategorySettingService{

	@Autowired
	private GlobalSettingService globalSettingService;
	
	@Autowired
	private CategorySettingDao categorySettingDao;
	
	@Override
	public int searchCount(String name) {
		return categorySettingDao.searchCount(name);
	}

	@Override
	public List<CategorySetting> search(PageQuery pageQuery, String name) {
		return categorySettingDao.search(pageQuery, name);
	}

	@Override
	public int add(CategorySetting categorySetting) {
		return categorySettingDao.add(categorySetting);
	}

	@Override
	public int remove(Long id) {
		JSONObject jsonObject = globalSettingService.getJsonObject(GlobalSettingName.CATEGORY_FIRST_NAVIGATION);
		Long categorySettingId = jsonObject.getLong("id");
		if (id.equals(categorySettingId)) {
			throw new ParameterErrorException("该分类配置为默认分类配置,不可删除！");
		}
		return categorySettingDao.remove(id);
	}

	@Override
	public int update(CategorySetting categorySetting) {
		JSONObject jsonObject = globalSettingService.getJsonObject(GlobalSettingName.CATEGORY_FIRST_NAVIGATION);
		Long categorySettingId = jsonObject.getLong("id");
		if (categorySetting.getId().equals(categorySettingId)) {
			if (categorySetting.getLinkType() == 0) {
				throw new ParameterErrorException("该分类配置为默认分类配置,不可修改跳转类型为内容");
			}
		}
		return categorySettingDao.update(categorySetting);
	}

	@Override
	public int update(Long id, String content) {
		return categorySettingDao.update(id, content);
	}

	@Override
	public CategorySetting search(Long id) {
		return categorySettingDao.search(id);
	}

	@Override
	public List<CategorySetting> search(Integer linkType) {
		return categorySettingDao.search(linkType);
	}
	
	
}
