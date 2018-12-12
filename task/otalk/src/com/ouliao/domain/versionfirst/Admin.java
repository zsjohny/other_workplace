/**
 * 
 */
package com.ouliao.domain.versionfirst;

import javax.persistence.*;

/**
 * 
 * 
 * @author xiaoluo
 * @version $Id: Admin.java, 2016年2月22日 下午3:57:14
 */
@Entity
@Table(name = "admin")
public class Admin {
	private Integer id;
	private String username;
	private String password;
	private String role;
	private String enabled;

	@GeneratedValue
	@Id
	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	@Column(length = 10)
	/**
	 * @return the role
	 */
	public String getRole() {
		return role;
	}

	/**
	 * @param role
	 *            the role to set
	 */
	public void setRole(String role) {
		this.role = role;
	}

	@Column(length = 14)
	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	@Column(length = 14)
	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	@Column(length = 6)
	/**
	 * @return the enabled
	 */
	public String getEnabled() {
		return enabled;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @param username
	 *            the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @param enabled
	 *            the enabled to set
	 */
	public void setEnabled(String enabled) {
		this.enabled = enabled;
	}

}
