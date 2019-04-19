package com.jiuy.core.meta.order;

import java.io.Serializable;

import com.jiuy.web.controller.util.DateUtil;

public class OrderMessageBoard implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4691601063581859584L;

	private long id;
	
	private long orderNo;
	
	private long adminId;
	
	private String adminName;
	
	private int type;
	
	private String operation;
	
	private String message;
	
	private int status;
	
	private long createTime;

    public OrderMessageBoard() {
        super();
    }

    public OrderMessageBoard(long orderNo, long adminId, String adminName, int type, String operation, String message) {
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
	
    public String getCreateTimeString() {
        if (createTime == 0) {
            return "";
        }

        return DateUtil.convertMSEL(createTime);
    }
    
    
    
    
    
    
    
//    
//    CREATE TABLE `yjj_OrderMessageBoard` (
//    		  `Id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
//    		  `OrderNo` bigint(20) NOT NULL,
//    		  `AdminId` bigint(20) DEFAULT '0' COMMENT '管理员Id',
//    		  `AdminName` varchar(45) DEFAULT NULL,
//    		  `Type` tinyint(4) NOT NULL COMMENT '0:, 业务处理 1:业务操作 :2:品牌发单',
//    		  `Operation` text COMMENT 'Type:1 业务操作下有效',
//    		  `Message` text COMMENT '留言',
//    		  `Status` tinyint(4) NOT NULL DEFAULT '0',
//    		  `CreateTime` bigint(20) NOT NULL DEFAULT '0' COMMENT '创建时间',
//    		  `BrandBusinessId` bigint(20) DEFAULT '0' COMMENT '品牌商家ID',
//    		  PRIMARY KEY (`Id`)
//    		) ENGINE=InnoDB AUTO_INCREMENT=3665 DEFAULT CHARSET=utf8 COMMENT='订单留言板表';
}
