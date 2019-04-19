package com.qianmi.open.api.domain.cloudshop;

import com.qianmi.open.api.QianmiObject;
import com.qianmi.open.api.tool.mapping.ApiField;

/**
 * 区域信息
 *
 * @author auto
 * @since 2.0
 */
public class Area extends QianmiObject {

	private static final long serialVersionUID = 1L;

	/**
	 * 区域ID
	 */
	@ApiField("ID")
	private String ID;

	/**
	 * 区域名称
	 */
	@ApiField("name")
	private String name;

	/**
	 * 父级地域编号
	 */
	@ApiField("parent_id")
	private String parentId;

	public String getID() {
		return this.ID;
	}
	public void setID(String ID) {
		this.ID = ID;
	}

	public String getName() {
		return this.name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getParentId() {
		return this.parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

}