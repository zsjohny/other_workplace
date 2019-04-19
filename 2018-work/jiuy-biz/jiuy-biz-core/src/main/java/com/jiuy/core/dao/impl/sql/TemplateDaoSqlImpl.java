package com.jiuy.core.dao.impl.sql;

import com.jiuy.core.dao.TemplateDao;
import com.jiuy.core.dao.support.DomainDaoSqlSupport;
import com.jiuyuan.entity.template.Template;

public class TemplateDaoSqlImpl extends DomainDaoSqlSupport<Template, Long> implements TemplateDao {

	@Override
	public Template addElem(Template template) {
		getSqlSession().insert("com.jiuy.core.dao.impl.sql.TemplateDaoSqlImpl.addElem", template);
		return template;
	}

}
