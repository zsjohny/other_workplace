package com.jiuyuan.entity.storeaftersale;

import java.io.Serializable;
import java.util.List;

import com.jiuy.web.controller.util.DateUtil;

public class StoreServiceTicketVO extends StoreServiceTicket implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3291387223136131437L;
	private List<Long> orderList;

	public StoreServiceTicketVO() {
	}
	
	public StoreServiceTicketVO(long id2, List<Long> orderList, long skuNo2, long businessNumber, String userRealName2, String userTelephone2, int status2, int type2, long startApplyTimeL,  long endApplyTimeL, long startExamineTimeL, long endExamineTimeL) {
		this.setId(id2);
		this.orderList=orderList;
		this.setSkuNo(skuNo2);
		this.setBusinessNumber(businessNumber);
		this.setUserRealName(userRealName2);
		this.setUserTelephone(userTelephone2);
		this.setStatus(status2);
		this.setType(type2);
		this.setStartApplyTime(startApplyTimeL);
		this.setEndApplyTime(endApplyTimeL);
		this.setStartExamineTime(startExamineTimeL);
		this.setEndExamineTime(endExamineTimeL);
	}

	
	private long startApplyTime;
	
	private long endApplyTime;
	
	private long startExamineTime;
	
	private long endExamineTime;

	public List<Long> getOrderList() {
		return orderList;
	}

	public void setOrderList(List<Long> orderList) {
		this.orderList = orderList;
	}

	public long getStartApplyTime() {
		return startApplyTime;
	}

	public void setStartApplyTime(long startApplyTime) {
		this.startApplyTime = startApplyTime;
	}

    public String getStartApplyTimeString() {
        return DateUtil.convertMSEL(startApplyTime);
    }

	public long getEndApplyTime() {
		return endApplyTime;
	}

	public void setEndApplyTime(long endApplyTime) {
		this.endApplyTime = endApplyTime;
	}

    public String getEndApplyTimeString() {
        return DateUtil.convertMSEL(endApplyTime);
    }

	public long getStartExamineTime() {
		return startExamineTime;
	}

	public void setStartExamineTime(long startExamineTime) {
		this.startExamineTime = startExamineTime;
	}

    public String getStartExamineTimeString() {
        return DateUtil.convertMSEL(startExamineTime);
    }

	public long getEndExamineTime() {
		return endExamineTime;
	}

	public void setEndExamineTime(long endExamineTime) {
		this.endExamineTime = endExamineTime;
	}

    public String getEndExamineTimeString() {
        return DateUtil.convertMSEL(endExamineTime);
    }
}
