package com.admin.core.template.engine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.admin.core.template.engine.base.OfficialTemplateEngine;
import com.admin.core.util.ToolUtil;

public class OfficialCommonTemplateEngine extends OfficialTemplateEngine{
	private static final Logger logger = LoggerFactory.getLogger(OfficialCommonTemplateEngine.class);

	@Override
	protected void generateHomePageHtml() {
		String path = ToolUtil.format(super.getOfficialContextConfig().getProjectPath() + getOfficialPageConfig().getPageHomeTemplate());
        generateFile("gunsTemplate/index.html.btl", path);
	}

	@Override
	protected void generateFooterPageHtml() {
		String path = ToolUtil.format(super.getOfficialContextConfig().getProjectPath() + getOfficialPageConfig().getPageFriendlyLinkTemplate());
        generateFile("gunsTemplate/footer.html.btl", path);
        logger.info("生成官网友情链接1成功!");
	}

	@Override
	protected void generateArticleDetailHtml() {
		String path = ToolUtil.format(super.getOfficialContextConfig().getProjectPath() + getOfficialPageConfig().getPageArticleDetailTemplate(),
				super.getOfficialContextConfig().getArticleDetailId());
        generateFile("gunsTemplate/newsDetail.html.btl", path);
        logger.info("生成官网文章详情成功!");
	}

	@Override
	protected void generateArticleListHtml() {
		String path = ToolUtil.format(super.getOfficialContextConfig().getProjectPath() + getOfficialPageConfig().getPageArticleListTemplate(),
				super.getOfficialContextConfig().getArticleListIndex());
        generateFile("gunsTemplate/news.html.btl", path);
        logger.info("生成官网文章列表"+super.getOfficialContextConfig().getArticleDetailId()+"成功!");
		
	}





	@Override
	protected void generateRelativeHtml() {
		//生成关于公司页面
		String path = ToolUtil.format(super.getOfficialContextConfig().getProjectPath() + getOfficialPageConfig().getPageAboutCompanyTemplate());
        generateFile("gunsTemplate/aboutCompany.html.btl", path);
        logger.info("生成官网关于公司页面成功!");
		//生成服务页面
		path = ToolUtil.format(super.getOfficialContextConfig().getProjectPath() + getOfficialPageConfig().getPageServerTemplate());
        generateFile("gunsTemplate/server.html.btl", path);
        logger.info("生成官网服务页面成功!");
        //生成隐私页面
        path = ToolUtil.format(super.getOfficialContextConfig().getProjectPath() + getOfficialPageConfig().getPagePrivacyTemplate());
        generateFile("gunsTemplate/privacy.html.btl", path);
        logger.info("生成官网隐私政策页面成功!");
        //生成小程序页面
        path = ToolUtil.format(super.getOfficialContextConfig().getProjectPath() + getOfficialPageConfig().getPageJoinInTemplate());
        generateFile("gunsTemplate/joinIn.html.btl", path);
        logger.info("生成官网小程序页面成功!");
        //生成加入我们页面
        path = ToolUtil.format(super.getOfficialContextConfig().getProjectPath() + getOfficialPageConfig().getPageJoinUsTemplate());
        generateFile("gunsTemplate/joinUs.html.btl", path);
        logger.info("生成官网加入我们页面成功!");
       
	}

	@Override
	protected void generateWxaHtml() {
		String path = ToolUtil.format(super.getOfficialContextConfig().getProjectPath() + getOfficialPageConfig().getWpageWxaTemplate());
		generateFile("gunsTemplate/wxa.html.btl", path);
	}

	@Override
	protected void generateWxaProxyHtml() {
		String path = ToolUtil.format(super.getOfficialContextConfig().getProjectPath() + getOfficialPageConfig().getWxaProxy());
		generateFile("gunsTemplate/wxaAgent.html.btl", path);
	}


}
