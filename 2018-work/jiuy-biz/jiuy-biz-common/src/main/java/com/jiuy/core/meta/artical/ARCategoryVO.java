package com.jiuy.core.meta.artical;

import java.io.Serializable;

import com.jiuyuan.entity.BaseMeta;

public class ARCategoryVO extends BaseMeta<Long> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -688312490418932001L;

	private long id;
	private String name;
	private long parentId;
	private int weight;
	private String description;
	private int status;
	private String parentName;
	private int articalNum;
	
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



	public long getParentId() {
		return parentId;
	}



	public void setParentId(long parentId) {
		this.parentId = parentId;
	}



	public int getWeight() {
		return weight;
	}



	public void setWeight(int weight) {
		this.weight = weight;
	}



	public String getDescription() {
		return description;
	}



	public void setDescription(String description) {
		this.description = description;
	}



	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}


	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}



	@Override
	public String toString() {
		return "ARCategoryVO [id=" + id + ", name=" + name + ", parentId=" + parentId + ", weight=" + weight
				+ ", description=" + description + ", status=" + status + ", parentName=" + parentName + ", articalNum="
				+ articalNum + "]";
	}



	public int getArticalNum() {
		return articalNum;
	}



	public void setArticalNum(int articalNum) {
		this.articalNum = articalNum;
	}



	@Override
	public Long getCacheId() {
		return null;
	}

}
