/**
 * 
 */
package com.jiuyuan.entity.log;

import java.io.Serializable;

/**
* @author czy
* @version 创建时间: 2017年2月16日 上午9:49:53
*/
public class RelatedOrderLog implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6837314702677473320L;
	
	private long id;

	private long srcPageId;
	private String srcRelatedId;
	private long pageId;
	private long orderNo;

	private long createTime;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getSrcPageId() {
		return srcPageId;
	}

	public void setSrcPageId(long srcPageId) {
		this.srcPageId = srcPageId;
	}

	public String getSrcRelatedId() {
		return srcRelatedId;
	}

	public void setSrcRelatedId(String srcRelatedId) {
		this.srcRelatedId = srcRelatedId;
	}

	public long getPageId() {
		return pageId;
	}

	public void setPageId(long pageId) {
		this.pageId = pageId;
	}

	public long getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(long orderNo) {
		this.orderNo = orderNo;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	
	
	

//
//	@Override
//	public String toString() {
//		return "Log [logId=" + logId + ", userId=" + userId + ", ip=" + ip + ", cid=" + cid + ", platform=" + platform
//				+ ", version=" + version + ", net=" + net + ", status=" + status + ", srcId=" + srcId + ", srcLogId="
//				+ srcLogId + ", srcRelatedId=" + srcRelatedId + ", pageId=" + pageId + ", enterTime=" + enterTime
//				+ ", loadFinishTime=" + loadFinishTime + ", exitTime=" + exitTime + ", duration=" + duration
//				+ ", createTime=" + createTime + "]";
//	}
	
}
