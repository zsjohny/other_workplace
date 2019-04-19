package com.jiuy.core.dao;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.jiuy.core.meta.homepage.HomeTemplate;

public interface HomeTemplateDao {

	List<HomeTemplate> loadTemplates(String name);

	HomeTemplate addTemplate(HomeTemplate homeTemplate);

	int updateTemplate(HomeTemplate ht);

	HomeTemplate loadTemplate(Long homeTemplateId);

	int removeDirtyData(long activityPlaceId);

	Map<Long, HomeTemplate> templateOfIds(Collection<Long> ids);

	List<HomeTemplate> getByName(String template_name);

}
