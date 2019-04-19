package com.jiuyuan.entity.brand;

import java.io.Serializable;

public class PartnerVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8198040778849819975L;
	
    private long id;
    private String name;
    private String engName;
    private String summary;
    private String description;
    private long createTime;
    private long updateTime;
    private String iconUrl;
    private String partnerCatManageName;
    private long partnerCatManageId;
    private int weight;
    private String url;
    private long templateId;
    
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEngName() {
		return engName;
	}
	public void setEngName(String engName) {
		this.engName = engName;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
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
	public String getIconUrl() {
		return iconUrl;
	}
	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}
	
	public int getWeight() {
		return weight;
	}
	public void setWeight(int weight) {
		this.weight = weight;
	}
	public long getPartnerCatManageId() {
		return partnerCatManageId;
	}
	public void setPartnerCatManageId(long partnerCatManageId) {
		this.partnerCatManageId = partnerCatManageId;
	}
	public String getPartnerCatManageName() {
		return partnerCatManageName;
	}
	public void setPartnerCatManageName(String partnerCatManageName) {
		this.partnerCatManageName = partnerCatManageName;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public long getTemplateId() {
		return templateId;
	}
	public void setTemplateId(long templateId) {
		this.templateId = templateId;
	}
	@Override
	public String toString() {
		return "PartnerVO [id=" + id + ", name=" + name + ", engName=" + engName + ", summary=" + summary
				+ ", description=" + description + ", createTime=" + createTime + ", updateTime=" + updateTime
				+ ", iconUrl=" + iconUrl + ", partnerCatManageName=" + partnerCatManageName + ", partnerCatManageId="
				+ partnerCatManageId + ", weight=" + weight + ", url=" + url + ", templateId=" + templateId + "]";
	}
	
	
}
