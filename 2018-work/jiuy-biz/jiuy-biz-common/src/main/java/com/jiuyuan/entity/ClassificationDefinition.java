/**
 * 
 */
package com.jiuyuan.entity;

import java.util.List;

/**
 * @author LWS
 *
 */
public class ClassificationDefinition extends BaseMeta<Integer> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8077743642738814491L;
	/* (non-Javadoc)
	 * @see com.jiuy.core.meta.BaseMeta#getCacheId()
	 */
	@Override
	public Integer getCacheId() {
		return id;
	}

	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getClassificationBelong() {
		return classificationBelong;
	}
	public void setClassificationBelong(String classificationBelong) {
		this.classificationBelong = classificationBelong;
	}
	public String getClassificationName() {
		return classificationName;
	}
	public void setClassificationName(String classificationName) {
		this.classificationName = classificationName;
	}
	public List<String> getSubClassification() {
		return subClassification;
	}
	public void setSubClassification(List<String> subClassification) {
		this.subClassification = subClassification;
	}



	private int id;
	private String classificationBelong;
	private String classificationName;
	private List<String> subClassification;
	
}
