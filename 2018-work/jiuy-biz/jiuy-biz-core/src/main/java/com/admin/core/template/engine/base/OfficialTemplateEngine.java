package com.admin.core.template.engine.base;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.beetl.core.Configuration;
import org.beetl.core.GroupTemplate;
import org.beetl.core.Template;
import org.beetl.core.resource.ClasspathResourceLoader;

import com.admin.core.template.config.OfficialPageConfig;
import com.admin.core.util.ToolUtil;

public abstract class OfficialTemplateEngine extends OfficialAbstractTemplateEngine {

	protected GroupTemplate groupTemplate;

	public OfficialTemplateEngine() {
		initBeetlEngine();
	}

	public void initBeetlEngine() {
		Properties properties = new Properties();
		properties.put("RESOURCE.root", "");
		properties.put("DELIMITER_STATEMENT_START", "<%");
		properties.put("DELIMITER_STATEMENT_END", "%>");
		properties.put("HTML_TAG_FLAG", "##");
		Configuration cfg = null;
		try {
			cfg = new Configuration(properties);
		} catch (IOException e) {
			e.printStackTrace();
		}
		ClasspathResourceLoader resourceLoader = new ClasspathResourceLoader();
		groupTemplate = new GroupTemplate(resourceLoader, cfg);
		groupTemplate.registerFunctionPackage("tool", new ToolUtil());
	}

	public void configTemplate(Template template) {
		template.binding("page", super.getOfficialPageConfig());
		template.binding("context", super.getOfficialContextConfig());
	}

	public void generateFile(String template, String filePath) {
		Template pageTemplate = groupTemplate.getTemplate(template);
		configTemplate(pageTemplate);
		if (ToolUtil.isWinOs()) {
			filePath = filePath.replaceAll("/+|\\\\+", "\\\\");
		} else {
			filePath = filePath.replaceAll("/+|\\\\+", "/");
		}
		File file = new File(filePath);
		File parentFile = file.getParentFile();
		if (!parentFile.exists()) {
			parentFile.mkdirs();
		}
		try {
			//渲染,输出文本
			pageTemplate.renderTo(new FileOutputStream(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void start(int pageTypeHome) {
		// 配置之间的相互依赖
		super.initConfig();

		// 生成模板
		switch (pageTypeHome) {
		case OfficialPageConfig.PAGE_TYPE_HOME:
			generateHomePageHtml();
			break;
		case OfficialPageConfig.PAGE_TYPE_FRIENDLY_LINK:
			generateFooterPageHtml();
			break;
		case OfficialPageConfig.PAGE_TYPE_ARTICLE_DETAIL:
			generateArticleDetailHtml();
			break;
		case OfficialPageConfig.PAGE_TYPE_ARTICLE_LIST:
			generateArticleListHtml();
			break;
		case OfficialPageConfig.PAGE_TYPE_RELATIVE:
			generateRelativeHtml();
			break;
		case OfficialPageConfig.PAGE_TYPE_WXA:
				generateWxaHtml();
			break;
		case OfficialPageConfig.PAGE_TYPE_WXA_AGENT:
				generateWxaProxyHtml();
			break;
		default:
			break;
		}

	}

	protected abstract void generateHomePageHtml();
	
	protected abstract void generateFooterPageHtml();
	
	protected abstract void generateArticleDetailHtml();
	
	protected abstract void generateArticleListHtml();
	
	protected abstract void generateRelativeHtml();

	protected abstract void generateWxaHtml();


	protected abstract void generateWxaProxyHtml();

}
