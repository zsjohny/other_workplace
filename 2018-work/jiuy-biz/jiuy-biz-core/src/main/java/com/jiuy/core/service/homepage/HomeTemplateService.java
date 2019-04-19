package com.jiuy.core.service.homepage;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.jiuy.core.meta.homepage.HomeTemplate;
import com.jiuyuan.constant.ResultCode;

public interface HomeTemplateService {

	List<HomeTemplate> loadTemplates(String string);

	HomeTemplate addTemplate(long floorId, HomeTemplate nextHomeTemplateId);

	ResultCode updateTemplate(HomeTemplate ht);

	HomeTemplate loadTemplate(Long homeTemplateId);

	HomeTemplate add(long floorId, HomeTemplate ht);

	Map<Long, HomeTemplate> templateOfIds(Collection<Long> allTemplateIds);

}
