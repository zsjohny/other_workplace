package com.yujj.entity.order;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yujj.entity.product.ProductSKU;

public class OrderItem implements Serializable {

    private static final long serialVersionUID = 4733818106411309129L;

    private long id;
    
    private long orderId;
    
    private long userId;
    
    private long productId;
    
    private long skuId;
    
    private double totalMoney;
    
    private double totalExpressMoney;
    
    private double money;
    
    private double expressMoney;

    private int buyCount;

    private String skuSnapshot;
    
    /** 玖币 **/
    private int totalUnavalCoinUsed;

    private int unavalCoinUsed;
    
    @JsonIgnore
    private int status;

    @JsonIgnore
    private long createTime;

    @JsonIgnore
    private long updateTime;

    private long brandId;
    
    private long lOWarehouseId;

    private long groupId;

    private long orderNo;
    
    private long parentId;

    private double totalPay;
    
    private double totalMarketPrice;
    
    private double marketPrice;
    
    private int afterSaleFlag;
    
    private int wholesaleType;
    
    private int deductCoinNum;
    
    private String statisticsId;
    
    private String position;
    
    private ProductSKU productSKU;
    
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

	public int getTotalUnavalCoinUsed() {
		return totalUnavalCoinUsed;
	}

	public void setTotalUnavalCoinUsed(int totalUnavalCoinUsed) {
		this.totalUnavalCoinUsed = totalUnavalCoinUsed;
	}

	public int getUnavalCoinUsed() {
		return unavalCoinUsed;
	}

	public void setUnavalCoinUsed(int unavalCoinUsed) {
		this.unavalCoinUsed = unavalCoinUsed;
	}

	public long getlOWarehouseId() {
		return lOWarehouseId;
	}

	public void setlOWarehouseId(long lOWarehouseId) {
		this.lOWarehouseId = lOWarehouseId;
	}

	public long getGroupId() {
		return groupId;
	}

	public void setGroupId(long groupId) {
		this.groupId = groupId;
	}


	public long getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(long orderNo) {
		this.orderNo = orderNo;
	}

	public double getTotalPay() {
		return totalPay;
	}

	public void setTotalPay(double totalPay) {
		this.totalPay = totalPay;

	}

	public double getTotalMarketPrice() {
		return totalMarketPrice;
	}

	public void setTotalMarketPrice(double totalMarketPrice) {
		this.totalMarketPrice = totalMarketPrice;
	}

	public double getMarketPrice() {
		return marketPrice;
	}

	public void setMarketPrice(double marketPrice) {
		this.marketPrice = marketPrice;
	}

	public int getAfterSaleFlag() {
		return afterSaleFlag;
	}

	public void setAfterSaleFlag(int afterSaleFlag) {
		this.afterSaleFlag = afterSaleFlag;
	}

	public ProductSKU getProductSKU() {
		return productSKU;
	}

	public void setProductSKU(ProductSKU productSKU) {
		this.productSKU = productSKU;
	}

	public String getStatisticsId() {
		return statisticsId;
	}

	public void setStatisticsId(String statisticsId) {
		this.statisticsId = statisticsId;
	}

	public long getParentId() {
		return parentId;
	}

	public void setParentId(long parentId) {
		this.parentId = parentId;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public int getWholesaleType() {
		return wholesaleType;
	}

	public void setWholesaleType(int wholesaleType) {
		this.wholesaleType = wholesaleType;
	}

	public int getDeductCoinNum() {
		return deductCoinNum;
	}

	public void setDeductCoinNum(int deductCoinNum) {
		this.deductCoinNum = deductCoinNum;
	}


	
	
	
	
}
