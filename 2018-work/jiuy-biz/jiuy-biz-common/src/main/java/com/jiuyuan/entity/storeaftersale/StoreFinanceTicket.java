package com.jiuyuan.entity.storeaftersale;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.jiuy.web.controller.util.DateUtil;
import com.jiuyuan.entity.BaseMeta;

public class StoreFinanceTicket extends BaseMeta<Long> implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = 5824474550886891823L;
	
	private StoreServiceTicket serviceTicket;
	
	private long id;
		
	private long serviceId;
	
	private long createTime;
	
	private int status;
	
	private long returnTime;
	
	private double returnMoney;
	
	private String returnNo;
	
	private int returnType;
	
	private String returnMemo;
	
	private int returnSource;
	
	private String returnUser;
	
	public StoreFinanceTicket() {
	}

	public StoreFinanceTicket(long id, long serviceId, int returnType, double returnMoney, String returnNo,
			String message, int status, long returnTime) {
		this.id = id;
		this.serviceId = serviceId;
		this.returnType = returnType;
		this.returnMoney = returnMoney;
		this.returnNo = returnNo;
		this.returnMemo = message;
		this.status = status;
		this.returnTime = returnTime;
	}
	
	public StoreFinanceTicket(long serviceId, long nowTime, int status, int paymentType, String remark, int returnSource,
			String userName) {
		this.serviceId = serviceId;
		this.createTime = nowTime;
		this.status =status;
		this.returnType = paymentType;
		this.returnMemo = remark;
		this.returnSource =returnSource;
		this.returnUser = userName;
	}

	public String getCreateTimeStr() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(new Date(this.getCreateTime()));
	}
	
	public String getReturnTimeStr() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(new Date(this.getReturnTime()));
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getServiceId() {
		return serviceId;
	}

	public void setServiceId(long serviceId) {
		this.serviceId = serviceId;
	}

	public long getCreateTime() {
		return createTime;
	}
	
	public String getCreateTimeString() {
		return DateUtil.convertMSEL(createTime);
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public long getReturnTime() {
		return returnTime;
	}
	
	public String getReturnTimeString() {
		return DateUtil.convertMSEL(returnTime);
	}

	public void setReturnTime(long returnTime) {
		this.returnTime = returnTime;
	}

	public double getReturnMoney() {
		return returnMoney;
	}

	public void setReturnMoney(double returnMoney) {
		this.returnMoney = returnMoney;
	}

	public String getReturnNo() {
		return returnNo;
	}

	public void setReturnNo(String returnNo) {
		this.returnNo = returnNo;
	}

	public int getReturnType() {
		return returnType;
	}

	public void setReturnType(int returnType) {
		this.returnType = returnType;
	}

	@Override
	public Long getCacheId() {
		return null;
	}

	public StoreServiceTicket getServiceTicket() {
		return serviceTicket;
	}

	public void setServiceTicket(StoreServiceTicket serviceTicket) {
		this.serviceTicket = serviceTicket;
	}

	public String getReturnMemo() {
		return returnMemo;
	}

	public void setReturnMemo(String returnMemo) {
		this.returnMemo = returnMemo;
	}

	public int getReturnSource() {
		return returnSource;
	}

	public void setReturnSource(int returnSource) {
		this.returnSource = returnSource;
	}

	public String getReturnUser() {
		return returnUser;
	}

	public void setReturnUser(String returnUser) {
		this.returnUser = returnUser;
	}
	
}
