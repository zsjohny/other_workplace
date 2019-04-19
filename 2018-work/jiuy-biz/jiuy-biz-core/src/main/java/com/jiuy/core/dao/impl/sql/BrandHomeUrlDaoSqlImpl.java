package com.jiuy.core.dao.impl.sql;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


import com.jiuy.core.dao.BrandHomeUrlDao;
import com.jiuy.core.dao.support.DomainDaoSqlSupport;
import com.jiuyuan.entity.brandcategory.PartnerCategory;


public class BrandHomeUrlDaoSqlImpl extends DomainDaoSqlSupport<PartnerCategory, Long> implements BrandHomeUrlDao {

	@Override
	public int addBrandUrl(long partnerId, String brandUrl, String template) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("partnerId", partnerId);
		map.put("brandUrl", brandUrl);
		map.put("template", template);
		int count = getSqlSession().insert("HomePageSetting.insertBrandUrl", map);
		return count;
	}

	@Override
	public String getTemplateUrl(String brandUrl) {
		String template = getSqlSession().selectOne("HomePageSetting.getTemplateUrl", brandUrl);
		if(template == null) {
			template = "";
		}
		return template;
	}

	@Override
	public List<Map<String, Object>> getHomeSettingInfo(long partnerId) {
		return getSqlSession().selectList("HomePageSetting.getHomeSettingInfo", partnerId);
	}

	@Override
	public int activeBrandUrl(long partnerId) {
		return getSqlSession().update("HomePageSetting.activeBrandUrl", partnerId);
	}

	@Override
	public int deactiveBrandUrl(long partnerId) {
		return getSqlSession().update("HomePageSetting.deactiveBrandUrl", partnerId);
	}

}
