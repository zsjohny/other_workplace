package com.yujj.entity.afterSale;

import java.io.Serializable;

import com.jiuyuan.constant.order.PaymentTypeDetail;
import com.jiuyuan.entity.newentity.alipay.direct.UtilDate;

public class FinanceTicket implements Serializable {

    private static final long serialVersionUID = -2960266889540167523L;

    private long id;

    private long serviceId;

    private long createTime;
    
    private int status;
    
    private long returnTime;
    
    private double returnMoney;
    
    private String returnNo;
    
    private int returnType;
    
    private String returnMemo;
    
  

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

	public String getReturnMemo() {
		return returnMemo;
	}

	public void setReturnMemo(String returnMemo) {
		this.returnMemo = returnMemo;
	}
	
	public String getReturnTypeName() {
		return PaymentTypeDetail.getNameByValue(returnType).getShowName();
	}
	
	
	public String getCreateTimeStr() {
		if(createTime <= 0){
			return "";
		}
		return UtilDate.getDateStrFromMillis(createTime, UtilDate.simple);
	}
	
	
	public String getReturnTimeStr() {
		if(returnTime <= 0){
			return "";
		}
		return UtilDate.getDateStrFromMillis(returnTime, UtilDate.simple);
	}



}
