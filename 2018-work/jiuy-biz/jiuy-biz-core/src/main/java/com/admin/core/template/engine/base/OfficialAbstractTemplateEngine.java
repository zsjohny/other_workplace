package com.admin.core.template.engine.base;

import com.admin.core.template.config.OfficialContextConfig;
import com.admin.core.template.config.OfficialPageConfig;

public class OfficialAbstractTemplateEngine {
	
	protected OfficialContextConfig officialContextConfig;                //全局配置
	protected OfficialPageConfig officialPageConfig;                      //页面的控制器
	
	public void initConfig(){
		if(this.officialContextConfig == null){
			this.officialContextConfig = new OfficialContextConfig();
		}
		if(this.officialPageConfig == null){
			this.officialPageConfig = new OfficialPageConfig();
		}
		this.officialPageConfig.setOfficialContextConfig(officialContextConfig);
		this.officialPageConfig.init();
	}

	public OfficialContextConfig getOfficialContextConfig() {
		return officialContextConfig;
	}

	public void setOfficialContextConfig(OfficialContextConfig officialContextConfig) {
		this.officialContextConfig = officialContextConfig;
	}

	public OfficialPageConfig getOfficialPageConfig() {
		return officialPageConfig;
	}

	public void setOfficialPageConfig(OfficialPageConfig officialPageConfig) {
		this.officialPageConfig = officialPageConfig;
	}
	
	
}
