/**
 * 
 */
package com.jiuyuan.entity.product;

import java.io.Serializable;

/**
 * @author LWS
 *
 */
public class PurchaseDetail  implements Serializable  {
	
	private long id;
	private long userid;
	private long productid;
	private long pruchaseTime;
	private String purchaseDetailContent;

	/**
	 * 
	 */
	private static final long serialVersionUID = -4500892716284384502L;

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

	public long getPruchaseTime() {
		return pruchaseTime;
	}

	public void setPruchaseTime(long pruchaseTime) {
		this.pruchaseTime = pruchaseTime;
	}

	public String getPurchaseDetailContent() {
		return purchaseDetailContent;
	}

	public void setPurchaseDetailContent(String purchaseDetailContent) {
		this.purchaseDetailContent = purchaseDetailContent;
	}

	
}
