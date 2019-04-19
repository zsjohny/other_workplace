/**
 * 
 */
package com.jiuyuan.entity;

import org.apache.commons.lang.StringUtils;

/**
 * @author LWS
 *
 */
public class TreeDictionary extends BaseMeta<String> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4546439617707501698L;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jiuy.core.meta.BaseMeta#getCacheId()
	 */
	@Override
	public String getCacheId() {
		return StringUtils.join(new Object[] { groupId, dictId });
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getDictId() {
		return dictId;
	}

	public void setDictId(String dictId) {
		this.dictId = dictId;
	}

	public String getDictName() {
		return dictName;
	}

	public void setDictName(String dictName) {
		this.dictName = dictName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public int getDictLevel() {
		return dictLevel;
	}

	public void setDictLevel(int dictLevel) {
		this.dictLevel = dictLevel;
	}

	private String groupId;
	private String groupName;
	private String dictId;
	private String dictName;
	private String parentId;
	private int dictLevel;
	private String status;
}
