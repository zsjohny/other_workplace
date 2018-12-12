package com.finace.miscroservice.activity.po;

import java.io.Serializable;

public class CreditLogPO implements Serializable {
	
	private int id;
	private long user_id;
	private int type_id;
	private long op;
	private int value;
	private String remark;
	private long op_user;
	private long addtime;
	private String addip;
	private String total;
	private Integer activity_id;//活动id(参考hongbao_activity表，红包/金豆合二为一)
	private Integer status;//0无有效期，1未失效/消费，2已失效，3已消费
	
	////关联user表字段
	private String addtime1;
	private String username;
	 

	public CreditLogPO() {
		super();
	}
	
	public CreditLogPO(long user_id, long op, long addtime, String addip) {
		super();
		this.user_id = user_id;
		this.op = op;
		this.addtime=addtime;
		this.addip=addip;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getType_id() {
		return type_id;
	}

	public void setType_id(int type_id) {
		this.type_id = type_id;
	}

	public long getOp() {
		return op;
	}

	public void setOp(long op) {
		this.op = op;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public long getUser_id() {
		return user_id;
	}

	public void setUser_id(long user_id) {
		this.user_id = user_id;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public long getOp_user() {
		return op_user;
	}

	public void setOp_user(long op_user) {
		this.op_user = op_user;
	}

	public long getAddtime() {
		return addtime;
	}

	public void setAddtime(long addtime) {
		this.addtime = addtime;
	}

	public String getAddip() {
		return addip;
	}

	public void setAddip(String addip) {
		this.addip = addip;
	}


	public Integer getActivity_id() {
		return activity_id;
	}

	public void setActivity_id(Integer activity_id) {
		this.activity_id = activity_id;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getAddtime1() {
		return addtime1;
	}

	public void setAddtime1(String addtime1) {
		this.addtime1 = addtime1;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}



}
