package com.jiuyuan.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jiuy.web.controller.util.DateUtil;
import com.store.entity.ProductPropVO;

public class ProductSKU implements Serializable {

    private static final long serialVersionUID = -9149120648055610720L;
  
    private long id;

    private long productId;
  //商品SKU属性值聚合，PropertyNameId:PropertyValueId形式，多个以英文,隔开
    private String propertyIds;

    // 价格，人民币以分为单位，玖币以1为单位
    private long price;
  //库存
    private int remainCount;
    
    private String specificImage;

 // 状态:-3废弃，-2停用，-1下架，0正常，1定时上架
    private int status;

    private long createTime;

    private long updateTime;
    
    private long skuNo;
    
    private double cash;

    //重量
    private double weight;

    private String name;
    
    private double marketPrice;
    
    private double costPrice;

    private String clothesNumber;
    
    private long lOWarehouseId;
    //库存保留时间
    private int remainKeepTime;
    
    private long BrandId;
    
//  上架时间
    private long saleStartTime;
//  下架时间
    private long saleEndTime;
    
    private int sort;
    
    private long parentCategoryId;
    
    private long categoryId;
    
    private String childCategoryName;
    
    private String parentCategoryName;

	private String brandName;
	
	private int type;

    private int remainCountLock;

    private long remainCountStartTime;

    private long remainCountEndTime;
    
    private int isRemainCountLock;
    
    private long pushTime;
    
    private int promotionSaleCount;
    
    private int promotionVisitCount;

    // added by Dongzhong 2016-11-24
    private long lOWarehouseId2;	//副仓id
    private long setLOWarehouseId2;	//设置副仓 0:不设  1：设置
    private int remainCount2;		//副仓库存    

    private String position;		//货架号
    
    private double wholeSaleCash;	//批发价
    

  

    public void setPrice(int price) {
        this.price = price;
    }

   
    
    
   
	
	

	public long getSaleStartTime() {
		return saleStartTime;
	}

	public void setSaleStartTime(long saleStartTime) {
		this.saleStartTime = saleStartTime;
	}

	public long getSaleEndTime() {
		return saleEndTime;
	}

	public void setSaleEndTime(long saleEndTime) {
		this.saleEndTime = saleEndTime;
	}

	public long getlOWarehouseId() {
		return lOWarehouseId;
	}

	public void setlOWarehouseId(long lOWarehouseId) {
		this.lOWarehouseId = lOWarehouseId;
	}

	public long getlOWarehouseId2() {
		return lOWarehouseId2;
	}

	

	

	

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    public String getClothesNumber() {
        return clothesNumber;
	}

	public void setClothesNumber(String clothesNumber) {
        this.clothesNumber = clothesNumber;
	}

	public long getLOWarehouseId() {
        return lOWarehouseId;
	}

	public void setLOWarehouseId(long lOWarehouseId) {
        this.lOWarehouseId = lOWarehouseId;
	}

	public int getRemainKeepTime() {
        return remainKeepTime;
	}

	public void setRemainKeepTime(int remainKeepTime) {
        this.remainKeepTime = remainKeepTime;
	}

	public long getBrandId() {
		return BrandId;
	}

	public void setBrandId(long brandId) {
		BrandId = brandId;
	}

	

	public void setSaleStartTime(Long saleStartTime) {
        this.saleStartTime = saleStartTime;
	}

	

	public void setSaleEndTime(Long saleEndTime) {
        this.saleEndTime = saleEndTime;
	}
    
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public String getPropertyIds() {
        return propertyIds;
    }

    public void setPropertyIds(String propertyIds) {
        this.propertyIds = propertyIds;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public int getRemainCount() {
        return remainCount;
    }

    public void setRemainCount(int remainCount) {
        this.remainCount = remainCount;
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

    public String getSpecificImage() {
        return specificImage;
    }

    public void setSpecificImage(String specificImage) {
        this.specificImage = specificImage;
    }

	public long getSkuNo() {
		return skuNo;
	}

	public void setSkuNo(long skuNo) {
		this.skuNo = skuNo;
	}
	
	public double getCash() {
		return cash;
	}

	public void setCash(double cash) {
		this.cash = cash;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getMarketPrice() {
		return marketPrice;
	}

	public void setMarketPrice(double marketPrice) {
		this.marketPrice = marketPrice;
	}
	
	/**
	 * 
	 * @return
	 * 返回格式: Map<PropertyNameId, PropertyValueId>
	 */
	@JsonIgnore
	public Map<String, Long> getPropertyMap() {
		Map<String, Long> map = new HashMap<String, Long>();
		String[] propPairs = StringUtils.split(getPropertyIds(), ";");
		for (String propPair : propPairs) {
			String[] parts = StringUtils.split(propPair, ":");
			map.put(Long.parseLong(parts[0])+"", Long.parseLong(parts[1]));
		}
		
		return map;
	}
	
	/**
	 * 
	 * @return
	 * 返回格式: Map<PropertyNameId, PropertyValueId>
	 */
	@JsonIgnore
	public Map<String, Long> getPropertyNameMap() {
		Map<String, Long> map = new HashMap<String, Long>();
		String[] propPairs = StringUtils.split(getPropertyIds(), ";");
		for (String propPair : propPairs) {
			String[] parts = StringUtils.split(propPair, ":");
			map.put(Long.parseLong(parts[0]) == 7 ? "color" : "size", Long.parseLong(parts[1]));
		}
		
		return map;
	}
	@JsonIgnore
    public List<ProductPropVO> getProductProps() {
        String[] propPairs = StringUtils.split(getPropertyIds(), ";");
        List<ProductPropVO> result = new ArrayList<ProductPropVO>();
        for (String propPair : propPairs) {
            String[] parts = StringUtils.split(propPair, ":");
            ProductPropVO prop = new ProductPropVO();
            prop.setProductId(getProductId());
            prop.setPropertyNameId(Long.parseLong(parts[0]));
            prop.setPropertyValueId(Long.parseLong(parts[1]));
            result.add(prop);
        }
        return result;
    }

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

	public String getChildCategoryName() {
		return childCategoryName;
	}

	public void setChildCategoryName(String childCategoryName) {
		this.childCategoryName = childCategoryName;
	}

	public String getParentCategoryName() {
		return parentCategoryName;
	}

	public void setParentCategoryName(String parentCategoryName) {
		this.parentCategoryName = parentCategoryName;
	}

	public long getParentCategoryId() {
		return parentCategoryId;
	}

	public void setParentCategoryId(long parentCategoryId) {
		this.parentCategoryId = parentCategoryId;
	}

	public long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(long categoryId) {
		this.categoryId = categoryId;
	}
	
	@JsonIgnore
    public int getOnSalingInt() {
    	if(status < 0) {
    		return status;
    	}
    	if(saleEndTime == 0 || saleStartTime == 0) {
    		return -1;
    	}
    	long time = System.currentTimeMillis();
        if (time > saleEndTime && saleEndTime != 0) {
        	return -1;
        } else if (status > -1 && time > saleStartTime && (time < saleEndTime || saleEndTime == 0)) {
            return 0;
        } else if (status > -1 && time <= saleStartTime) {
            return 1;
        }
        return -1;
    }
	
	/**
	 * 是否上架
	 *  * 说明：
	 * 1、SKUstatus状态:-3废弃，-2停用，-1下架，0正常，1定时上架
	 * 2、但是要结合下架时间和商家时间进行判断最终状态，具体参考本方法代码
	 * 
 	 *  相关逻辑SQL：sku.Status >= 0 and sku.SaleStartTime < unix_timestamp()*1000 and (sku.SaleEndTime = 0 or sku.SaleEndTime > unix_timestamp()*1000
	 * @return true已上架、false已下架   
	 * 	@JsonIgnore
	 */
    public boolean getOnSaling() {
        long time = System.currentTimeMillis();
        if(status <= -1) {
        	return false;
        }
    //  getSaleStartTime上架时间    getSaleEndTime下架时间
        if (getSaleStartTime() > 0 && getSaleEndTime() > 0) {
            return getSaleStartTime() <= time && time <= getSaleEndTime();
        }

        if (getSaleEndTime() <= 0) {
            return getSaleStartTime() <= time;
        }
        return getSaleEndTime() >= time;
    }
	
	/**
	 * 是否在架
	 *  * 说明：
	 * 1、SKUstatus状态:-3废弃，-2停用，-1下架，0正常，1定时上架
	 * 2、但是要结合下架时间和商家时间进行判断最终状态，具体参考本方法代码
	 * 
 	 *  相关逻辑SQL：sku.Status >= 0 and sku.SaleStartTime < unix_timestamp()*1000 and (sku.SaleEndTime = 0 or sku.SaleEndTime > unix_timestamp()*1000
	 * @return true已上架、false已下架   
	 * 	@JsonIgnore
	 */
    public boolean isOnShelf() {
        long time = System.currentTimeMillis();
        if(status <= -1) {
        	return false;
        }
    //  getSaleStartTime上架时间    getSaleEndTime下架时间
        if (getSaleStartTime() > 0 && getSaleEndTime() > 0) {
            return getSaleStartTime() <= time && time <= getSaleEndTime();
        }

        if (getSaleEndTime() <= 0) {
            return getSaleStartTime() <= time;
        }
        return getSaleEndTime() >= time;
    }
	
	public String getSkuNoStr() {
		return String.format("%07d", skuNo);
	}

	public String getBrandName() {
		return this.brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

    public int getRemainCountLock() {
        return remainCountLock;
    }

    public void setRemainCountLock(int remainCountLock) {
        this.remainCountLock = remainCountLock;
    }

    public long getRemainCountStartTime() {
        return remainCountStartTime;
    }

    public void setRemainCountStartTime(long remainCountStartTime) {
        this.remainCountStartTime = remainCountStartTime;
    }

    public long getRemainCountEndTime() {
        return remainCountEndTime;
    }

    public void setRemainCountEndTime(long remainCountEndTime) {
        this.remainCountEndTime = remainCountEndTime;
    }

    public String getRemainCountStartTimeString() {
    	if(remainCountStartTime == 0 ) {
    		return "";
    	}
        return DateUtil.convertMSEL(remainCountStartTime);
    }

    public String getRemainCountEndTimeString() {
    	if(remainCountEndTime == 0 ) {
    		return "";
    	}
        return DateUtil.convertMSEL(remainCountEndTime);
    }
    
    public int getIsRemainCountLock() {
		return isRemainCountLock;
	}

	public void setIsRemainCountLock(int isRemainCountLock) {
		this.isRemainCountLock = isRemainCountLock;
	}

	public long getPushTime() {
		return pushTime;
	}

	public void setPushTime(long pushTime) {
		this.pushTime = pushTime;
	}
	
	public boolean getIsPushed() {
		if(pushTime == 0) {
			return false;
		}
		
		return true;
	}

	public boolean isLocked() {
        if(isRemainCountLock == 0) {
            return false;
        }

        return true;
    }

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getPromotionSaleCount() {
		return promotionSaleCount;
	}

	public void setPromotionSaleCount(int promotionSaleCount) {
		this.promotionSaleCount = promotionSaleCount;
	}

	public int getPromotionVisitCount() {
		return promotionVisitCount;
	}

	public void setPromotionVisitCount(int promotionVisitCount) {
		this.promotionVisitCount = promotionVisitCount;
	}

	public double getCostPrice() {
		return costPrice;
	}

	public void setCostPrice(double costPrice) {
		this.costPrice = costPrice;
	}

	

	public void setlOWarehouseId2(long lOWarehouseId2) {
		this.lOWarehouseId2 = lOWarehouseId2;
	}

	public long getSetLOWarehouseId2() {
		return setLOWarehouseId2;
	}

	public void setSetLOWarehouseId2(long setLOWarehouseId2) {
		this.setLOWarehouseId2 = setLOWarehouseId2;
	}

	public int getRemainCount2() {
		return remainCount2;
	}

	public void setRemainCount2(int remainCount2) {
		this.remainCount2 = remainCount2;
	}

	

	

	public double getWholeSaleCash() {
		return wholeSaleCash;
	}

	public void setWholeSaleCash(double wholeSaleCash) {
		this.wholeSaleCash = wholeSaleCash;
	}
	
	/**
	 * 是否上架
	 *  * 说明：
	 * 1、SKUstatus状态:-3废弃，-2停用，-1下架，0正常，1定时上架
	 * 2、但是要结合下架时间和商家时间进行判断最终状态，具体参考本方法代码
	 * 
 	 *  相关逻辑SQL：sku.Status >= 0 and sku.SaleStartTime < unix_timestamp()*1000 and (sku.SaleEndTime = 0 or sku.SaleEndTime > unix_timestamp()*1000
	 * @return true已上架、false已下架 
	 */
	@JsonIgnore
    public boolean isSKUOnSaling() {
        long time = System.currentTimeMillis();
        if(status <= -1) {
        	return false;
        }
    //  getSaleStartTime上架时间    getSaleEndTime下架时间
        if (getSaleStartTime() > 0 && getSaleEndTime() > 0) {
            return getSaleStartTime() <= time && time <= getSaleEndTime();
        }

        if (getSaleEndTime() <= 0) {
            return getSaleStartTime() <= time;
        }
        return getSaleEndTime() >= time;
    }

	@Override
	public String toString() {
		return "ProductSKU [id=" + id + ", productId=" + productId + ", propertyIds=" + propertyIds + ", price=" + price
				+ ", remainCount=" + remainCount + ", specificImage=" + specificImage + ", status=" + status
				+ ", createTime=" + createTime + ", updateTime=" + updateTime + ", skuNo=" + skuNo + ", cash=" + cash
				+ ", weight=" + weight + ", name=" + name + ", marketPrice=" + marketPrice + ", costPrice=" + costPrice
				+ ", clothesNumber=" + clothesNumber + ", lOWarehouseId=" + lOWarehouseId + ", remainKeepTime="
				+ remainKeepTime + ", BrandId=" + BrandId + ", saleStartTime=" + saleStartTime + ", saleEndTime="
				+ saleEndTime + ", sort=" + sort + ", parentCategoryId=" + parentCategoryId + ", categoryId="
				+ categoryId + ", childCategoryName=" + childCategoryName + ", parentCategoryName=" + parentCategoryName
				+ ", brandName=" + brandName + ", type=" + type + ", remainCountLock=" + remainCountLock
				+ ", remainCountStartTime=" + remainCountStartTime + ", remainCountEndTime=" + remainCountEndTime
				+ ", isRemainCountLock=" + isRemainCountLock + ", pushTime=" + pushTime + ", promotionSaleCount="
				+ promotionSaleCount + ", promotionVisitCount=" + promotionVisitCount + ", lOWarehouseId2="
				+ lOWarehouseId2 + ", setLOWarehouseId2=" + setLOWarehouseId2 + ", remainCount2=" + remainCount2
				+ ", position=" + position + ", wholeSaleCash=" + wholeSaleCash + "]";
	}
	
}