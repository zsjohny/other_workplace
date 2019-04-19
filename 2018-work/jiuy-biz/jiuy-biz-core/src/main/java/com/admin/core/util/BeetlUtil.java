package com.admin.core.util;

import java.util.List;

import org.springframework.web.filter.GenericFilterBean;

import com.admin.core.template.config.OfficialContextConfig;
import com.admin.core.template.config.OfficialPageConfig;
import com.admin.core.template.engine.OfficialCommonTemplateEngine;
import com.admin.core.template.engine.base.OfficialTemplateEngine;
import com.baomidou.mybatisplus.plugins.Page;
import com.jiuyuan.constant.Constants;
import com.jiuyuan.entity.newentity.OperatorArticle;
import com.jiuyuan.entity.newentity.OperatorRelationUrl;
import com.jiuyuan.entity.newentity.OperatorSeo;

public class BeetlUtil {
	
	public static void generateHomeFile(OperatorSeo operatorSeo, List<OperatorArticle> list){
		/* 配置 */
		OfficialPageConfig officialPageConfig = new OfficialPageConfig();
		officialPageConfig.setOperatorSeo(operatorSeo);
		officialPageConfig.setOperatorArticles(list);

		/* 上下文 */
		OfficialContextConfig officialContextConfig = new OfficialContextConfig();
		officialContextConfig.setProjectPath(Constants.OFFICIAL_PROJECT_PATH);

		/* 组装 配置和上下文 */
		OfficialTemplateEngine officialTemplateEngine = new OfficialCommonTemplateEngine();
		officialTemplateEngine.setOfficialContextConfig(officialContextConfig);
		officialTemplateEngine.setOfficialPageConfig(officialPageConfig);

		//启动
		officialTemplateEngine.start(OfficialPageConfig.PAGE_TYPE_HOME);
	}
	
	public static void generateFooterFile(List<OperatorRelationUrl> list){
		OfficialPageConfig officialPageConfig = new OfficialPageConfig();
		officialPageConfig.setOperatorRelationUrls(list);
		OfficialContextConfig officialContextConfig = new OfficialContextConfig();
		officialContextConfig.setProjectPath(Constants.OFFICIAL_PROJECT_PATH);
		OfficialTemplateEngine officialTemplateEngine = new OfficialCommonTemplateEngine();
		officialTemplateEngine.setOfficialContextConfig(officialContextConfig);
		officialTemplateEngine.setOfficialPageConfig(officialPageConfig);
		officialTemplateEngine.start(OfficialPageConfig.PAGE_TYPE_FRIENDLY_LINK);
	}
	
	public static void generateArticleDetailFile(OperatorArticle operatorArticle, OperatorSeo operatorSeo){
		OfficialPageConfig officialPageConfig = new OfficialPageConfig();
		officialPageConfig.setOperatorArticle(operatorArticle);
		officialPageConfig.setOperatorSeo(operatorSeo);
		OfficialContextConfig officialContextConfig = new OfficialContextConfig();
		officialContextConfig.setProjectPath(Constants.OFFICIAL_PROJECT_PATH);
		officialContextConfig.setArticleDetailId(String.valueOf(operatorArticle.getId()));
		OfficialTemplateEngine officialTemplateEngine = new OfficialCommonTemplateEngine();
		officialTemplateEngine.setOfficialContextConfig(officialContextConfig);
		officialTemplateEngine.setOfficialPageConfig(officialPageConfig);
		officialTemplateEngine.start(OfficialPageConfig.PAGE_TYPE_ARTICLE_DETAIL);
	}
	
	public static void generateArticleListFile(Page<OperatorArticle> page, OperatorSeo operatorSeo){
		OfficialPageConfig officialPageConfig = new OfficialPageConfig();
		officialPageConfig.setOperatorArticles(page.getRecords());
		officialPageConfig.setOperatorArticlePage(page);
		officialPageConfig.setOperatorSeo(operatorSeo);
		OfficialContextConfig officialContextConfig = new OfficialContextConfig();
		officialContextConfig.setProjectPath(Constants.OFFICIAL_PROJECT_PATH);
		officialContextConfig.setArticleListIndex(String.valueOf(page.getCurrent()));
		OfficialTemplateEngine officialTemplateEngine = new OfficialCommonTemplateEngine();
		officialTemplateEngine.setOfficialContextConfig(officialContextConfig);
		officialTemplateEngine.setOfficialPageConfig(officialPageConfig);
		officialTemplateEngine.start(OfficialPageConfig.PAGE_TYPE_ARTICLE_LIST);
	}
	
	public static void generateRelativeFile(OperatorSeo operatorSeo){
		OfficialPageConfig officialPageConfig = new OfficialPageConfig();
		officialPageConfig.setOperatorSeo(operatorSeo);
		OfficialContextConfig officialContextConfig = new OfficialContextConfig();
		officialContextConfig.setProjectPath(Constants.OFFICIAL_PROJECT_PATH);
		OfficialTemplateEngine officialTemplateEngine = new OfficialCommonTemplateEngine();
		officialTemplateEngine.setOfficialContextConfig(officialContextConfig);
		officialTemplateEngine.setOfficialPageConfig(officialPageConfig);
		officialTemplateEngine.start(OfficialPageConfig.PAGE_TYPE_RELATIVE);
	}

	public static void generatePages(OperatorSeo operatorSeo,Integer type) {

		OfficialPageConfig officialPageConfig = new OfficialPageConfig();
		officialPageConfig.setOperatorSeo(operatorSeo);
		OfficialContextConfig officialContextConfig = new OfficialContextConfig();
		officialContextConfig.setProjectPath(Constants.OFFICIAL_PROJECT_PATH);
		OfficialTemplateEngine officialTemplateEngine = new OfficialCommonTemplateEngine();
		officialTemplateEngine.setOfficialContextConfig(officialContextConfig);
		officialTemplateEngine.setOfficialPageConfig(officialPageConfig);
		officialTemplateEngine.start(type);
	}

	

}
