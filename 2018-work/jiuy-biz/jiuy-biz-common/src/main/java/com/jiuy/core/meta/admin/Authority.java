package com.jiuy.core.meta.admin;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author jeff.zhan
 * @version 2016年9月27日下午7:17:04
 * 
 */
public class Authority implements Serializable, Comparable<Authority> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7771808009086468703L;

	private Long id;
	
	private String moduleName;
	
	private String menuName;
	
	private String description;

	private long parentId;
	
	private String url;
	
	private int weight;
	
	private int status;
	
	private long createTime;
	
	private long updateTime;
	
	/**
	 * 不是MYSQL里的字段,额外字段
	 * 选中的菜单
	 */
	private int selected;
	
	/**
	 * 需要显示的菜单
	 */
	private int displayed;
	
	private List<Authority> authorities = new ArrayList<>();

	public Authority() {
	}
	
	public Authority(Long id, String moduleName, Long parentId, String url, String menuName, String description, int weight) {
		this.id = id;
		this.moduleName = moduleName;
		this.parentId = parentId;
		this.url = url;
		this.menuName = menuName;
		this.description = description;
		this.weight = weight;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public long getParentId() {
		return parentId;
	}

	public void setParentId(long parentId) {
		this.parentId = parentId;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}

	public List<Authority> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(List<Authority> authorities) {
		this.authorities = authorities;
	}

	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public int getSelected() {
		return selected;
	}

	public void setSelected(int selected) {
		this.selected = selected;
	}

	@Override
	public int compareTo(Authority o) {
		return o.getWeight() - getWeight();
	}

	public int getDisplayed() {
		return displayed;
	}

	public void setDisplayed(int displayed) {
		this.displayed = displayed;
	}

}
