package com.jiuy.core.meta.order;

import java.io.Serializable;
import java.math.BigDecimal;

import com.jiuy.web.controller.util.DateUtil;
import com.jiuyuan.constant.order.OrderStatus;
import com.jiuyuan.constant.order.PaymentTypeDetail;

public class OrderNew implements Serializable{

    /**
     * 
     */
    private static final long serialVersionUID = 8540253452870796324L;
    
    private long orderNo;
    
    private long userId;
    
    private int orderStatus;
    
    private double totalMoney;
    
    private double totalPay;
    
    private double totalExpressMoney;
    
    private String expressInfo;
    
    private int coinUsed;
    
    private String remark;
    
    private String platform;
    
    private String platformVersion;
    
    private String ip;
    
    private String paymentNo;
    
    private int paymentType;
    
    private long parentId;
    
    private long mergedId;
    
    private int status;
    
    private long createTime;

    private long updateTime;
    
    private long lOWarehouseId;
    
    private long expiredTime;
    
    private int orderType;
    
    private double commission;
    
    private double availableCommission;
    
    private int afterSellNum;
    
    private double returnMoney;
    
    private long payTime;
    
    private long yjjNumber;
    
    private long belongBusinessId;
    
    private long brandOrder;
    
    private String day;
    
    private String buyCounts; 
    
    private String provinceName;
    
    //added by chenyuan 2017/5/25
    private String dividedCommission;
    
    private double returnCommission;
    
	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	public String getBuyCounts() {
		return buyCounts;
	}

	public void setBuyCounts(String buyCounts) {
		this.buyCounts = buyCounts;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public long getBelongBusinessId() {
		return belongBusinessId;
	}

	public void setBelongBusinessId(long belongBusinessId) {
		this.belongBusinessId = belongBusinessId;
	}

	public long getYjjNumber() {
		return yjjNumber;
	}

	public void setYjjNumber(long yjjNumber) {
		this.yjjNumber = yjjNumber;
	}

	public long getPayTime() {
		return payTime;
	}

	public void setPayTime(long payTime) {
		this.payTime = payTime;
	}

	public double getAvailableCommission() {
		return availableCommission;
	}

	public void setAvailableCommission(double availableCommission) {
		this.availableCommission = availableCommission;
	}

	public int getAfterSellNum() {
		return afterSellNum;
	}

	public void setAfterSellNum(int afterSellNum) {
		this.afterSellNum = afterSellNum;
	}

	public double getReturnMoney() {
		return returnMoney;
	}

	public void setReturnMoney(double returnMoney) {
		this.returnMoney = returnMoney;
	}

	public double getCommission() {
		return commission;
	}

	public void setCommission(double commission) {
		this.commission = commission;
	}

	public long getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(long orderNo) {
        this.orderNo = orderNo;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }

    public double getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(double totalMoney) {
        this.totalMoney = totalMoney;
    }

    public double getTotalPay() {
        return totalPay;
    }

    public void setTotalPay(double totalPay) {
        this.totalPay = totalPay;
    }

    public double getTotalExpressMoney() {
        return totalExpressMoney;
    }

    public void setTotalExpressMoney(double totalExpressMoney) {
        this.totalExpressMoney = totalExpressMoney;
    }

    public String getExpressInfo() {
        return expressInfo;
    }

    public void setExpressInfo(String expressInfo) {
        this.expressInfo = expressInfo;
    }

    public int getCoinUsed() {
        return coinUsed;
    }

    public void setCoinUsed(int coinUsed) {
        this.coinUsed = coinUsed;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getPlatformVersion() {
        return platformVersion;
    }

    public void setPlatformVersion(String platformVersion) {
        this.platformVersion = platformVersion;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPaymentNo() {
        return paymentNo;
    }

    public void setPaymentNo(String paymentNo) {
        this.paymentNo = paymentNo;
    }

    public int getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(int paymentType) {
        this.paymentType = paymentType;
    }

    public long getParentId() {
        return parentId;
    }

    public void setParentId(long parentId) {
        this.parentId = parentId;
    }

    public long getMergedId() {
        return mergedId;
    }

    public void setMergedId(long mergedId) {
        this.mergedId = mergedId;
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

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public long getlOWarehouseId() {
        return lOWarehouseId;
    }

    public void setlOWarehouseId(long lOWarehouseId) {
        this.lOWarehouseId = lOWarehouseId;
    }
    
    public long getExpiredTime() {
        return expiredTime;
    }

    public void setExpiredTime(long expiredTime) {
        this.expiredTime = expiredTime;
    }
    
	public void setOrderType(int orderType) {
		this.orderType = orderType;
	}
	
	public int getOrderType() {
		return orderType;
	}

	public String getOrderSeq() {
    	return String.format("%09d", orderNo);
    }
    
    public String getOrderNewType() {
    	if(parentId == -1) {
    		return "拆分";
    	} else if (mergedId == -1) {
    		return "组合";
    	}
        return "普通";
    }
    
    public String getCreateTimeString() {
    	if(createTime == 0) {
    		return "";
    	}
    	
    	return DateUtil.convertMSEL(createTime);
    }
    
    public String getUpdateTimeString() {
    	if(updateTime == 0) {
    		return "";
    	}
    	
    	return DateUtil.convertMSEL(updateTime);
    }
    
    public String getPayType() {
    	if (PaymentTypeDetail.getByIntValue(paymentType) != null && PaymentTypeDetail.getByIntValue(paymentType).getShowName() != null){
    		
    		return PaymentTypeDetail.getByIntValue(paymentType).getShowName();
    	}else{
    		return "";
    	}
    }

    public double getPay8Postage() {
    	return new BigDecimal(totalPay + totalExpressMoney).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
    }
    
    public boolean canCancel() {
       // return this.orderStatus == 0;
        return this.orderStatus == OrderStatus.UNPAID.getIntValue();
    }

	public long getBrandOrder() {
		return brandOrder;
	}

	public void setBrandOrder(long brandOrder) {
		this.brandOrder = brandOrder;
	}

	public String getDividedCommission() {
		return dividedCommission;
	}

	public void setDividedCommission(String dividedCommission) {
		this.dividedCommission = dividedCommission;
	}

	public double getReturnCommission() {
		return returnCommission;
	}

	public void setReturnCommission(double returnCommission) {
		this.returnCommission = returnCommission;
	}
    
	
	
	
	
//	CREATE TABLE `yjj_OrderNew` (
//			  `OrderNo` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '订单编号，前台展示',
//			  `UserId` bigint(20) NOT NULL COMMENT '用户id',
//			  `OrderType` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0: 普通订单 1:售后订单 2:当面付订单',
//			  `OrderStatus` tinyint(4) NOT NULL COMMENT '订单付款状态：参照参照OrderStatus',
//			  `TotalMoney` decimal(10,2) NOT NULL COMMENT '订单金额原价总价，不包含邮费',
//			  `TotalPay` decimal(10,2) NOT NULL COMMENT '订单金额折后总价，不包含邮费',
//			  `TotalExpressMoney` decimal(10,2) NOT NULL COMMENT '邮费总价',
//			  `ExpressInfo` varchar(1024) NOT NULL COMMENT '邮寄信息',
//			  `CoinUsed` int(11) NOT NULL COMMENT '使用的玖币',
//			  `Remark` varchar(1024) DEFAULT NULL COMMENT '用户订单备注',
//			  `Platform` varchar(30) DEFAULT NULL COMMENT '生成订单平台',
//			  `PlatformVersion` varchar(45) DEFAULT NULL COMMENT '生成订单平台版本号',
//			  `Ip` varchar(50) DEFAULT NULL COMMENT '客户端ip',
//			  `PaymentNo` varchar(50) DEFAULT NULL COMMENT '第三方的支付订单号',
//			  `PaymentType` tinyint(4) DEFAULT '0' COMMENT '使用的第三方支付方式，参考常量PaymentType',
//			  `ParentId` bigint(20) NOT NULL DEFAULT '0' COMMENT '母订单id 0:其他  -1:已拆分订单, OrderNo:没有子订单',
//			  `MergedId` bigint(20) NOT NULL DEFAULT '0' COMMENT '0:其他, 需合并订单：自身改为-1并把相应的被合并的订单此字段设为合好的新订单OrderNo, 不需要合并订单：值与自身OrderNo相等',
//			  `LOWarehouseId` bigint(20) NOT NULL DEFAULT '0' COMMENT '仓库id',
//			  `Status` tinyint(4) DEFAULT '0' COMMENT '状态:-1删除，0正常',
//			  `CreateTime` bigint(20) NOT NULL COMMENT '创建时间',
//			  `UpdateTime` bigint(20) NOT NULL COMMENT '更新时间',
//			  `TotalMarketPrice` decimal(10,2) DEFAULT '0.00' COMMENT '市场价',
//			  `CancelReason` varchar(45) DEFAULT NULL COMMENT '取消原因',
//			  `PushTime` bigint(20) NOT NULL DEFAULT '0' COMMENT '推送时间',
//			  `ExpiredTime` bigint(20) DEFAULT '0' COMMENT '订单过期时间',
//			  `Commission` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '提成金额',
//			  `AvailableCommission` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '可提现金额',
//			  `BelongBusinessId` bigint(20) DEFAULT NULL COMMENT '会员归属商家号',
//			  `DistributionStatus` tinyint(4) NOT NULL DEFAULT '0' COMMENT '分销状态 0：正常 1：用户分销状态禁用、门店账号状态禁用、门店分销状态禁用、用户没绑定',
//			  `AfterSellNum` tinyint(4) NOT NULL DEFAULT '0' COMMENT '售后个数',
//			  `AfterSellStatus` tinyint(4) NOT NULL DEFAULT '0' COMMENT '售后状态 0：可申请售后、1：正在申请售后、2：不可申请售后',
//			  `ReturnMoney` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '退款总计',
//			  `PayTime` bigint(20) NOT NULL DEFAULT '0' COMMENT '付款时间',
//			  `BrandOrder` bigint(20) DEFAULT NULL COMMENT '品牌批发订单编号',
//			  `TotalBuyCount` int(11) DEFAULT '0' COMMENT '总购买件数',
//			  PRIMARY KEY (`OrderNo`),
//			  KEY `idx_orderno` (`OrderNo`),
//			  KEY `idx_uid_orderno` (`UserId`,`OrderNo`)
//			) ENGINE=InnoDB AUTO_INCREMENT=20738 DEFAULT CHARSET=utf8 COMMENT='新订单表';


	
	
	
	
	
	
	
}
