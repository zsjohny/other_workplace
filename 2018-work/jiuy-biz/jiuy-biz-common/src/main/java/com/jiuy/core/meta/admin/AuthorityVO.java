package com.jiuy.core.meta.admin;
/**
 * @author jeff.zhan
 * @version 2016年11月15日 下午1:42:47
 * 
 */

public class AuthorityVO extends Authority {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7328333948408238707L;
	
	private String parentModuleName;

	public String getParentModuleName() {
		return parentModuleName;
	}

	public void setParentModuleName(String parentModuleName) {
		this.parentModuleName = parentModuleName;
	}

	
}
