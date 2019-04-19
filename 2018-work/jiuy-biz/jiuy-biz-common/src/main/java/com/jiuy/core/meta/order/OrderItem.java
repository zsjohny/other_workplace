package com.jiuy.core.meta.order;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jiuyuan.entity.BaseMeta;

public class OrderItem extends BaseMeta<Long> implements Serializable {

    private static final long serialVersionUID = 4733818106411309129L;

    private long id;
    
    private long orderId;
    
    private long userId;
    
    private long productId;
    
    private long skuId;
    
    private double totalMoney;
    
    private double totalExpressMoney;
    
    private int totalUnavalCoinUsed;
    
    private double money;
    
    private double expressMoney;

    private int buyCount;

    private String skuSnapshot;

    @JsonIgnore
    private int status;

    @JsonIgnore
    private long createTime;

    @JsonIgnore
    private long updateTime;

    private long brandId;
    
    private long lOWarehouseId;
    
    private long groupId;
    
	private double totalPay;
	
	private long orderNo;
	
	private long statisticsId;
	
	private String position;
	
	//added by chenyuan  2017/5/25
	private double totalCommission;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public long getSkuId() {
        return skuId;
    }

    public void setSkuId(long skuId) {
        this.skuId = skuId;
    }

    public double getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(double totalMoney) {
        this.totalMoney = totalMoney;
    }

    public double getTotalExpressMoney() {
        return totalExpressMoney;
    }

    public void setTotalExpressMoney(double totalExpressMoney) {
        this.totalExpressMoney = totalExpressMoney;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public double getExpressMoney() {
        return expressMoney;
    }

    public void setExpressMoney(double expressMoney) {
        this.expressMoney = expressMoney;
    }

    public int getBuyCount() {
        return buyCount;
    }

    public void setBuyCount(int buyCount) {
        this.buyCount = buyCount;
    }

    public String getSkuSnapshot() {
        return skuSnapshot;
    }

    public void setSkuSnapshot(String skuSnapshot) {
        this.skuSnapshot = skuSnapshot;
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

    public long getBrandId() {
        return brandId;
    }

    public void setBrandId(long brandId) {
        this.brandId = brandId;
    }
    
    public long getGroupId() {
		return groupId;
	}

	public void setGroupId(long groupId) {
		this.groupId = groupId;
	}
	
	public int getTotalUnavalCoinUsed() {
		return totalUnavalCoinUsed;
	}

	public void setTotalUnavalCoinUsed(int totalUnavalCoinUsed) {
		this.totalUnavalCoinUsed = totalUnavalCoinUsed;
	}
	
	public long getlOWarehouseId() {
		return lOWarehouseId;
	}

	public void setlOWarehouseId(long lOWarehouseId) {
		this.lOWarehouseId = lOWarehouseId;
	}

	public double getTotalPay() {
		return totalPay;
	}

	public void setTotalPay(double totalPay) {
		this.totalPay = totalPay;
	}
	
	public long getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(long orderNo) {
		this.orderNo = orderNo;
	}
	
	public double getTotalConsume() {
		return this.totalPay + this.totalExpressMoney;
	}

	public long getStatisticsId() {
		return statisticsId;
	}

	public void setStatisticsId(long statisticsId) {
		this.statisticsId = statisticsId;
	}

	@Override
    public Long getCacheId() {
        return null;
    }

	@Override
	public String toString() {
		return "OrderItem [id=" + id + ", orderId=" + orderId + ", userId=" + userId + ", productId=" + productId
				+ ", skuId=" + skuId + ", totalMoney=" + totalMoney + ", totalExpressMoney=" + totalExpressMoney
				+ ", totalUnavalCoinUsed=" + totalUnavalCoinUsed + ", money=" + money + ", expressMoney=" + expressMoney
				+ ", buyCount=" + buyCount + ", skuSnapshot=" + skuSnapshot + ", status=" + status + ", createTime="
				+ createTime + ", updateTime=" + updateTime + ", brandId=" + brandId + ", lOWarehouseId="
				+ lOWarehouseId + ", groupId=" + groupId + ", totalPay=" + totalPay + ", orderNo=" + orderNo + "]";
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public double getTotalCommission() {
		return totalCommission;
	}

	public void setTotalCommission(double totalCommission) {
		this.totalCommission = totalCommission;
	}

	
//	CREATE TABLE `yjj_OrderItem` (
//			  `Id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
//			  `OrderId` bigint(20) NOT NULL DEFAULT '0' COMMENT '对应Order表的id',
//			  `UserId` bigint(20) NOT NULL COMMENT '用户id',
//			  `ProductId` bigint(20) NOT NULL COMMENT '对应Product表的id',
//			  `SkuId` bigint(20) NOT NULL COMMENT '对应ProductSKU的id',
//			  `TotalMoney` decimal(10,2) NOT NULL COMMENT '订单金额总价，不包含邮费',
//			  `TotalExpressMoney` decimal(10,2) NOT NULL COMMENT '邮费总价',
//			  `Money` decimal(10,2) NOT NULL COMMENT '订单细目单价，不包含邮费',
//			  `ExpressMoney` decimal(10,2) NOT NULL COMMENT '邮费单价',
//			  `TotalUnavalCoinUsed` int(11) DEFAULT '0' COMMENT '总玖币',
//			  `UnavalCoinUsed` int(11) DEFAULT '0' COMMENT '玖币',
//			  `BuyCount` int(11) NOT NULL COMMENT '订购数量',
//			  `SkuSnapshot` varchar(1024) NOT NULL COMMENT 'sku快照，json',
//			  `Status` tinyint(4) DEFAULT '0' COMMENT '状态:-1删除，0正常',
//			  `CreateTime` bigint(20) NOT NULL COMMENT '创建时间',
//			  `UpdateTime` bigint(20) NOT NULL COMMENT '更新时间',
//			  `BrandId` bigint(20) DEFAULT '0' COMMENT '关联的品牌id',
//			  `LOWarehouseId` bigint(20) NOT NULL DEFAULT '0' COMMENT '仓库id',
//			  `GroupId` bigint(20) NOT NULL DEFAULT '0',
//			  `TotalPay` decimal(10,2) DEFAULT NULL COMMENT '实付价',
//			  `OrderNo` bigint(20) DEFAULT NULL COMMENT '新订单表OrderNo',
//			  `TotalMarketPrice` decimal(10,2) DEFAULT '0.00' COMMENT '市场价',
//			  `MarketPrice` decimal(10,2) DEFAULT '0.00',
//			  `StatisticsId` bigint(20) DEFAULT NULL COMMENT '统计识别码Id',
//			  `TotalCommission` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '总提成金额',
//			  `ParentId` bigint(20) DEFAULT NULL COMMENT '父订单id',
//			  `Position` varchar(45) DEFAULT NULL COMMENT 'sku 货架位置',
//			  `wholesaleType` tinyint(4) DEFAULT '0' COMMENT '零售批发类型， 0: 零售 1:零售/批发 2:批发',
//			  PRIMARY KEY (`Id`),
//			  KEY `idx_userid` (`UserId`)
//			) ENGINE=InnoDB AUTO_INCREMENT=29318 DEFAULT CHARSET=utf8 COMMENT='用户订单细目表';


}
