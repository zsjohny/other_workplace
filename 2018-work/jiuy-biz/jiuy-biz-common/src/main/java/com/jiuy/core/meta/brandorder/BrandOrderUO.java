package com.jiuy.core.meta.brandorder;

import java.util.List;

/**
 * @author jeff.zhan
 * @version 2017年1月3日 下午7:26:57
 * 
 */

public class BrandOrderUO {
	
	private Integer status;
	
	private List<Long> orderNos;
	
	private Long updateTime;
	
	private Integer orderStatus;

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public List<Long> getOrderNos() {
		return orderNos;
	}

	public void setOrderNos(List<Long> orderNos) {
		this.orderNos = orderNos;
	}

	public Long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Long updateTime) {
		this.updateTime = updateTime;
	}

	public Integer getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(Integer orderStatus) {
		this.orderStatus = orderStatus;
	}
	
}
