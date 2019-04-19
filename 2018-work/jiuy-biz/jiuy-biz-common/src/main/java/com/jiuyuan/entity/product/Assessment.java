/**
 * 
 */
package com.jiuyuan.entity.product;

import java.io.Serializable;

/**
 * @author LWS
 *
 */
public class Assessment  implements Serializable  {

	private long id;
	private long userid;
	private long productid;
	private long assessTime;
	private short assessLevel;
	private String assessContent;
	private String itemAssessContent;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -434517233609185438L;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getUserid() {
		return userid;
	}

	public void setUserid(long userid) {
		this.userid = userid;
	}

	public long getProductid() {
		return productid;
	}

	public void setProductid(long productid) {
		this.productid = productid;
	}

	public long getAssessTime() {
		return assessTime;
	}

	public void setAssessTime(long assessTime) {
		this.assessTime = assessTime;
	}

	public short getAssessLevel() {
		return assessLevel;
	}

	public void setAssessLevel(short assessLevel) {
		this.assessLevel = assessLevel;
	}

	public String getAssessContent() {
		return assessContent;
	}

	public void setAssessContent(String assessContent) {
		this.assessContent = assessContent;
	}

	public String getItemAssessContent() {
		return itemAssessContent;
	}

	public void setItemAssessContent(String itemAssessContent) {
		this.itemAssessContent = itemAssessContent;
	}

}
