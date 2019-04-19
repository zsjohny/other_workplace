package com.jiuyuan.entity.product;

import java.io.Serializable;
import java.util.List;

import com.jiuyuan.entity.ProductFilterVO;
import com.yujj.entity.product.ProductPropValue;

public class BrandFilter implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7879185807824318949L;

	private long id;
	
	private long categoryId;
	
	private int type;

	private int weight;
	
	private long relatedId;
	
	
	private int quickSetting;
	
	private int status;
	
	private long createTime;
	
	private long updateTime;
	
	private String relatedName;  //数据库中没有 筛选分类名称
	
	private int selectNum;  //数据库中没有 可选数量

	private List<ProductPropValue> propValueList;
	
	private List<ProductFilterVO> productFilterVOList;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(long categoryId) {
		this.categoryId = categoryId;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public long getRelatedId() {
		return relatedId;
	}

	public void setRelatedId(long relatedId) {
		this.relatedId = relatedId;
	}

	public int getQuickSetting() {
		return quickSetting;
	}

	public void setQuickSetting(int quickSetting) {
		this.quickSetting = quickSetting;
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

	public String getRelatedName() {
		return relatedName;
	}

	public void setRelatedName(String relatedName) {
		this.relatedName = relatedName;
	}

	public int getSelectNum() {
		return selectNum;
	}

	public void setSelectNum(int selectNum) {
		this.selectNum = selectNum;
	}

	public List<ProductPropValue> getPropValueList() {
		return propValueList;
	}

	public void setPropValueList(List<ProductPropValue> propValueList) {
		this.propValueList = propValueList;
	}

	public List<ProductFilterVO> getProductFilterVOList() {
		return productFilterVOList;
	}

	public void setProductFilterVOList(List<ProductFilterVO> productFilterVOList) {
		this.productFilterVOList = productFilterVOList;
	}

	
}
