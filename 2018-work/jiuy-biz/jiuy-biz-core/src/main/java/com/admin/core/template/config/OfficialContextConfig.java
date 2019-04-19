package com.admin.core.template.config;

public class OfficialContextConfig {
	
    private String projectPath = "D:\\ideaSpace\\guns";//模板输出的项目目录
    private String articleDetailId = "";
    private String articleListIndex = "";
    private String moduleName = "system";  //模块名称
    
    
	public String getProjectPath() {
		return projectPath;
	}
	public void setProjectPath(String projectPath) {
		this.projectPath = projectPath;
	}
	public String getModuleName() {
		return moduleName;
	}
	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}
	public String getArticleDetailId() {
		return articleDetailId;
	}
	public void setArticleDetailId(String articleDetailId) {
		this.articleDetailId = articleDetailId;
	}
	public String getArticleListIndex() {
		return articleListIndex;
	}
	public void setArticleListIndex(String articleListIndex) {
		this.articleListIndex = articleListIndex;
	}
    
    

}
