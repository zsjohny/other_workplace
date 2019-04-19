package com.jiuy.core.meta.aftersale;

import java.io.Serializable;

import com.jiuy.web.controller.util.DateUtil;
import com.jiuyuan.entity.ServiceTicket;

public class ServiceTicketVO extends ServiceTicket implements Serializable {
	
	public ServiceTicketVO() {
	}
	
	public ServiceTicketVO(long id2, long orderNo2, long skuNo2, long yjjNumber2, String userRealName2, String userTelephone2, int status2, int type2, long startApplyTimeL,  long endApplyTimeL, long startExamineTimeL, long endExamineTimeL) {
		this.setId(id2);
		this.setOrderNo(orderNo2);
		this.setSkuNo(skuNo2);
		this.setYjjNumber(yjjNumber2);
		this.setUserRealName(userRealName2);
		this.setUserTelephone(userTelephone2);
		this.setStatus(status2);
		this.setType(type2);
		this.setStartApplyTime(startApplyTimeL);
		this.setEndApplyTime(endApplyTimeL);
		this.setStartExamineTime(startExamineTimeL);
		this.setEndExamineTime(endExamineTimeL);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -5002266083946054881L;
	
	private long startApplyTime;
	
	private long endApplyTime;
	
	private long startExamineTime;
	
	private long endExamineTime;

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
