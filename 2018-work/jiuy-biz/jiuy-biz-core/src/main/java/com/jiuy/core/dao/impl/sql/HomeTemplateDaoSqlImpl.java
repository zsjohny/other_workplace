package com.jiuy.core.dao.impl.sql;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jiuy.core.dao.HomeTemplateDao;
import com.jiuy.core.dao.support.DomainDaoSqlSupport;
import com.jiuy.core.meta.homepage.HomeTemplate;

public class HomeTemplateDaoSqlImpl extends DomainDaoSqlSupport<HomeTemplate, Long>  implements HomeTemplateDao {

	@Override
	public List<HomeTemplate> loadTemplates(String name) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("name", name);
		
		return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.HomeTemplateDaoSqlImpl.loadTemplates", params);
	}
	
	@Override
	public HomeTemplate loadTemplate(Long homeTemplateId) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("id", homeTemplateId);
		
		return getSqlSession().selectOne("com.jiuy.core.dao.impl.sql.HomeTemplateDaoSqlImpl.loadTemplateById", params);
	}

	@Override
	public HomeTemplate addTemplate(HomeTemplate ht) {
		getSqlSession().insert("com.jiuy.core.dao.impl.sql.HomeTemplateDaoSqlImpl.addTemplate", ht);
		return ht;
	}

	@Override
	public int updateTemplate(HomeTemplate ht) {
		return getSqlSession().update("com.jiuy.core.dao.impl.sql.HomeTemplateDaoSqlImpl.updateTemplate", ht);
	}

	@Override
	public int removeDirtyData(long activityPlaceId) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("activityPlaceId", activityPlaceId);
		
		return getSqlSession().update("com.jiuy.core.dao.impl.sql.HomeTemplateDaoSqlImpl.removeDirtyData", params);
	}

	@Override
	public Map<Long, HomeTemplate> templateOfIds(Collection<Long> ids) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("ids", ids);
		
		return getSqlSession().selectMap("com.jiuy.core.dao.impl.sql.HomeTemplateDaoSqlImpl.templateOfIds", params, "id");
	}

	@Override
	public List<HomeTemplate> getByName(String name) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("name", name);
		
		return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.HomeTemplateDaoSqlImpl.getByName", params);
	}

}
