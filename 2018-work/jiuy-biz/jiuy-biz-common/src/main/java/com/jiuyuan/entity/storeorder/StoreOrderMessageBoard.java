package com.jiuyuan.entity.storeorder;

import java.io.Serializable;

/**
* @author WuWanjian
* @version 创建时间: 2016年11月11日 下午4:26:47
*/
public class StoreOrderMessageBoard implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5519039107884418799L;

	private long id;
	
	private long orderNo;
	
	private long adminId;
	
	private String adminName;
	
	private int type;
	
	private String operation;
	
	private String message;
	
	private int status;
	
	private long createTime;
	
	public StoreOrderMessageBoard(){
		super();
	}
	
	public StoreOrderMessageBoard(long orderNo, long adminId, String adminName, int type, String operation, String message) {
        super();
        this.orderNo = orderNo;
        this.adminId = adminId;
        this.adminName = adminName;
        this.type = type;
        this.operation = operation;
        this.message = message;
    }

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(long orderNo) {
		this.orderNo = orderNo;
	}

	public long getAdminId() {
		return adminId;
	}

	public void setAdminId(long adminId) {
		this.adminId = adminId;
	}

	public String getAdminName() {
		return adminName;
	}

	public void setAdminName(String adminName) {
		this.adminName = adminName;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
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
	
	
}
