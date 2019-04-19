/**
 * 
 */
package com.jiuyuan.entity.visit;

import java.io.Serializable;
/**
 * @author Dongzhong
 */
public class AccessLog implements Serializable {

    private static final long serialVersionUID = 116542063818076543L;


	private long accessId;

    private String fromIp;

    private String accessUrl;

    private String accessMemo;

    private long createDate;
    
    public long getAccessId() {
		return accessId;
	}

	public void setAccessId(long accessId) {
		this.accessId = accessId;
	}

	public String getFromIp() {
		return fromIp;
	}

	public void setFromIp(String fromIp) {
		this.fromIp = fromIp;
	}

	public String getAccessUrl() {
		return accessUrl;
	}

	public void setAccessUrl(String accessUrl) {
		this.accessUrl = accessUrl;
	}

	public String getAccessMemo() {
		return accessMemo;
	}

	public void setAccessMemo(String accessMemo) {
		this.accessMemo = accessMemo;
	}

	public long getCreateDate() {
		return createDate;
	}

	public void setCreateDate(long createDate) {
		this.createDate = createDate;
	}
}
