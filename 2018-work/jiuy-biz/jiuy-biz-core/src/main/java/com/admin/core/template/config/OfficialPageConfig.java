package com.admin.core.template.config;

import java.util.List;

import com.baomidou.mybatisplus.plugins.Page;
import com.jiuyuan.entity.newentity.OperatorArticle;
import com.jiuyuan.entity.newentity.OperatorRelationUrl;
import com.jiuyuan.entity.newentity.OperatorSeo;

public class OfficialPageConfig {
	
	public static final int PAGE_TYPE_HOME = 0;//首页
	public static final int PAGE_TYPE_FRIENDLY_LINK = 1;//友情链接
	public static final int PAGE_TYPE_ARTICLE_DETAIL = 2;//文章详情
	public static final int PAGE_TYPE_ARTICLE_LIST = 3;//文章列表
	public static final int PAGE_TYPE_RELATIVE = 4;//文章相关页面
	// 首页小程序生成
	public static final int PAGE_TYPE_WXA = 5;
	// 首页小程序生成
	public static final int PAGE_TYPE_WXA_AGENT = 6;
	
	
	private OfficialContextConfig officialContextConfig;


    private String pageHomeTemplate;
    private String pageFriendlyLinkTemplate;
    private String pageArticleDetailTemplate;
    private String pageArticleListTemplate;
    private String pageAboutCompanyTemplate;
    private String pageServerTemplate;
    private String pageJoinInTemplate;
    private String pagePrivacyTemplate;
    private String pageJoinUsTemplate;
    private String pageWxaTemplate;
	private String pageWxaAgentTemplate;
    
    private OperatorSeo operatorSeo;
    private List<OperatorArticle> operatorArticles;
    private Page<OperatorArticle> operatorArticlePage;
    private List<OperatorRelationUrl> operatorRelationUrls;
    private OperatorArticle operatorArticle;
    

	public void init(){
    	pageHomeTemplate = "\\WebContent\\index.html";
    	pageFriendlyLinkTemplate = "\\WebContent\\footer.html";
    	pageArticleDetailTemplate = "\\WebContent\\articleDetail\\{}.html";
    	pageArticleListTemplate = "\\WebContent\\news\\list{}.html";
    	pageAboutCompanyTemplate = "\\WebContent\\aboutCompany.html";
    	pageServerTemplate = "\\WebContent\\server.html";
		pageWxaTemplate = "\\WebContent\\wxa.html";
    	pagePrivacyTemplate = "\\WebContent\\privacy.html";
    	pageJoinUsTemplate = "\\WebContent\\joinUs.html";
		pageWxaAgentTemplate = "\\WebContent\\wxaAgent.html";
    	
    }

	public OfficialContextConfig getOfficialContextConfig() {
		return officialContextConfig;
	}

	public void setOfficialContextConfig(OfficialContextConfig officialContextConfig) {
		this.officialContextConfig = officialContextConfig;
	}

	public OperatorSeo getOperatorSeo() {
		return operatorSeo;
	}

	public void setOperatorSeo(OperatorSeo operatorSeo) {
		this.operatorSeo = operatorSeo;
	}

	public String getPageHomeTemplate() {
		return pageHomeTemplate;
	}

	public void setPageHomeTemplate(String pageHomeTemplate) {
		this.pageHomeTemplate = pageHomeTemplate;
	}

	public String getPageFriendlyLinkTemplate() {
		return pageFriendlyLinkTemplate;
	}

	public void setPageFriendlyLinkTemplate(String pageFriendlyLinkTemplate) {
		this.pageFriendlyLinkTemplate = pageFriendlyLinkTemplate;
	}

	public List<OperatorRelationUrl> getOperatorRelationUrls() {
		return operatorRelationUrls;
	}
	
	public void setOperatorRelationUrls(List<OperatorRelationUrl> operatorRelationUrls) {
		this.operatorRelationUrls = operatorRelationUrls;
	}

	public String getPageArticleDetailTemplate() {
		return pageArticleDetailTemplate;
	}

	public void setPageArticleDetailTemplate(String pageArticleDetailTemplate) {
		this.pageArticleDetailTemplate = pageArticleDetailTemplate;
	}

	public OperatorArticle getOperatorArticle() {
		return operatorArticle;
	}

	public void setOperatorArticle(OperatorArticle operatorArticle) {
		this.operatorArticle = operatorArticle;
	}

	public List<OperatorArticle> getOperatorArticles() {
		return operatorArticles;
	}

	public void setOperatorArticles(List<OperatorArticle> operatorArticles) {
		this.operatorArticles = operatorArticles;
	}

	public String getPageArticleListTemplate() {
		return pageArticleListTemplate;
	}

	public void setPageArticleListTemplate(String pageArticleListTemplate) {
		this.pageArticleListTemplate = pageArticleListTemplate;
	}

	public Page<OperatorArticle> getOperatorArticlePage() {
		return operatorArticlePage;
	}

	public void setOperatorArticlePage(Page<OperatorArticle> operatorArticlePage) {
		this.operatorArticlePage = operatorArticlePage;
	}

	public String getPageAboutCompanyTemplate() {
		return pageAboutCompanyTemplate;
	}

	public void setPageAboutCompanyTemplate(String pageAboutCompanyTemplate) {
		this.pageAboutCompanyTemplate = pageAboutCompanyTemplate;
	}

	public String getPageServerTemplate() {
		return pageServerTemplate;
	}

	public void setPageServerTemplate(String pageServerTemplate) {
		this.pageServerTemplate = pageServerTemplate;
	}

	public String getPageJoinInTemplate() {
		return pageJoinInTemplate;
	}

	public void setPageJoinInTemplate(String pageJoinInTemplate) {
		this.pageJoinInTemplate = pageJoinInTemplate;
	}

	public String getPagePrivacyTemplate() {
		return pagePrivacyTemplate;
	}

	public void setPagePrivacyTemplate(String pagePrivacyTemplate) {
		this.pagePrivacyTemplate = pagePrivacyTemplate;
	}

	public String getPageJoinUsTemplate() {
		return pageJoinUsTemplate;
	}

	public void setPageJoinUsTemplate(String pageJoinUsTemplate) {
		this.pageJoinUsTemplate = pageJoinUsTemplate;
	}


	public String getWpageWxaTemplate() {
		return pageWxaTemplate;
	}


	public String getWxaProxy() {
		return pageWxaAgentTemplate;
	}

	
	
    
    

}
