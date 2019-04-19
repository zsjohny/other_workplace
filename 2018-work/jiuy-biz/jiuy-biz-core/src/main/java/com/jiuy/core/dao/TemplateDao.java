package com.jiuy.core.dao;

import com.jiuy.core.dao.support.DomainDao;
import com.jiuyuan.entity.template.Template;

public interface TemplateDao extends DomainDao<Template, Long>{


	Template addElem(Template template);
	

}
