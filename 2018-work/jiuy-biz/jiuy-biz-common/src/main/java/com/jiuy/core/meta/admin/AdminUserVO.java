package com.jiuy.core.meta.admin;
/**
 * @author jeff.zhan
 * @version 2016年9月27日下午7:17:04
 * 
 */
public class AdminUserVO extends AdminUser {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6316212846758353360L;
	
	private Role role;

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

}
