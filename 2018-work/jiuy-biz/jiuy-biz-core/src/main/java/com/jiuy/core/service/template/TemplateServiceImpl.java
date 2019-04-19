package com.jiuy.core.service.template;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.jiuy.core.dao.TemplateDao;
import com.jiuyuan.entity.template.Template;

@Service
public class TemplateServiceImpl implements TemplateService {
	
	@Resource 
	private TemplateDao templateDaoSqlImpl;

	@Override
	public Template add(long id) {
		Template template = new Template();
		template.setPartnerId(id);
		
		return templateDaoSqlImpl.addElem(template);
	}
}
