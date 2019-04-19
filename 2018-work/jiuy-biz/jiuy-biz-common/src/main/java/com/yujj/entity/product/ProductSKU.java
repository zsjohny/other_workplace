package com.yujj.entity.product;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yujj.entity.product.ProductPropVO;

public class ProductSKU implements Serializable {

    private static final long serialVersionUID = -9149120648055610720L;

    private long id;

    /**
     * 商品id
     */
    @JsonIgnore
    private long productId;

    @JsonIgnore
    private String propertyIds;

    private int price;

    private int remainCount;
    
    private int remainKeepTime;
    
    private int remainCountLock;
    
    private long skuNo;
    
    private int isRemainCountLock;
    
    private long remainCountStartTime;
    
    private long remainCountEndTime;
    
    private String specificImage;
    

    private long saleStartTime;

    private long saleEndTime;

//    @JsonIgnore
    private int status;

//    @JsonIgnore
    private long createTime;

//    @JsonIgnore
    private long updateTime;
    
    
    private long lOWarehouseId;
    
    private long lOWarehouseId2;
    
    private int remainCount2;
    
    private String position;

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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
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

    public String getSpecificImage() {
        return specificImage;
    }

    public void setSpecificImage(String specificImage) {
        this.specificImage = specificImage;
    }

	public int getRemainKeepTime() {
		return remainKeepTime;
	}

	public void setRemainKeepTime(int remainKeepTime) {
		this.remainKeepTime = remainKeepTime;
	}

	public int getRemainCountLock() {
		return remainCountLock;
	}

	public void setRemainCountLock(int remainCountLock) {
		this.remainCountLock = remainCountLock;
	}

	public int getIsRemainCountLock() {
		return isRemainCountLock;
	}

	public void setIsRemainCountLock(int isRemainCountLock) {
		this.isRemainCountLock = isRemainCountLock;
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

	public long getSkuNo() {
		return skuNo;
	}

	public void setSkuNo(long skuNo) {
		this.skuNo = skuNo;
	}
	
    public boolean isOnSaling() {
        long time = System.currentTimeMillis();
        if(status <= -1) {
        	return false;
        }
        if (getSaleStartTime() > 0 && getSaleEndTime() > 0) {
            return getSaleStartTime() <= time && time <= getSaleEndTime();
        }

        if (getSaleEndTime() <= 0) {
            return getSaleStartTime() <= time;
        }
        return getSaleEndTime() >= time;
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

	public void setlOWarehouseId2(long lOWarehouseId2) {
		this.lOWarehouseId2 = lOWarehouseId2;
	}

	public int getRemainCount2() {
		return remainCount2;
	}

	public void setRemainCount2(int remainCount2) {
		this.remainCount2 = remainCount2;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}
/*
	CREATE TABLE `yjj_ProductSKU` (
			  `Id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
			  `ProductId` bigint(20) NOT NULL COMMENT '商品id',
			  `PropertyIds` varchar(255) NOT NULL COMMENT '商品SKU属性值聚合，PropertyNameId:PropertyValueId形式，多个以英文,隔开',
			  `Price` int(11) NOT NULL DEFAULT '0' COMMENT '价格，人民币以分为单位，玖币以1为单位',
			  `RemainCount` int(11) NOT NULL COMMENT '库存',
			  `SpecificImage` varchar(256) DEFAULT NULL COMMENT '对应SKU的图片信息',
			  `Status` tinyint(4) DEFAULT '0' COMMENT '状态:-3废弃，-2停用，-1下架，0正常，1定时上架',
			  `CreateTime` bigint(20) NOT NULL COMMENT '创建时间',
			  `UpdateTime` bigint(20) NOT NULL COMMENT '更新时间',
			  `SkuNo` bigint(20) NOT NULL DEFAULT '0' COMMENT 'sku编码',
			  `Cash` decimal(10,2) DEFAULT '0.00',
			  `Weight` decimal(10,2) DEFAULT '0.00' COMMENT '重量',
			  `Name` varchar(45) DEFAULT '' COMMENT '\n\n货品名称\n',
			  `MarketPrice` decimal(10,2) DEFAULT '0.00' COMMENT '市场价（吊牌价）',
			  `CostPrice` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '成本价',
			  `ClothesNumber` varchar(45) DEFAULT NULL COMMENT '''sku款号''',
			  `LOWarehouseId` bigint(20) DEFAULT NULL COMMENT '主仓库',
			  `RemainKeepTime` int(11) NOT NULL DEFAULT '1440' COMMENT '''库存保留时间'' 天',
			  `BrandId` bigint(20) DEFAULT NULL COMMENT '品牌id',
			  `SaleStartTime` bigint(20) DEFAULT '0' COMMENT '上架时间',
			  `SaleEndTime` bigint(20) DEFAULT '0' COMMENT '下架时间',
			  `Sort` int(11) DEFAULT '0',
			  `RemainCountLock` int(11) NOT NULL DEFAULT '0' COMMENT '库存锁定量',
			  `RemainCountStartTime` bigint(20) NOT NULL DEFAULT '0' COMMENT '库存锁定开始时间',
			  `RemainCountEndTime` bigint(20) NOT NULL DEFAULT '0' COMMENT '库存锁定结束时间',
			  `IsRemainCountLock` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否锁库存',
			  `PushTime` bigint(20) NOT NULL DEFAULT '0' COMMENT '推送erp时间',
			  `PromotionSaleCount` int(11) DEFAULT '0' COMMENT '推广销量\n',
			  `PromotionVisitCount` int(11) DEFAULT '0' COMMENT '推广访问量\n',
			  `RemainCount2` int(11) NOT NULL DEFAULT '0' COMMENT '副仓库库存',
			  `LOWarehouseId2` bigint(20) NOT NULL DEFAULT '0' COMMENT '副仓库',
			  `SetLOWarehouseId2` tinyint(4) NOT NULL DEFAULT '0',
			  `Position` varchar(45) DEFAULT NULL COMMENT '货架位置格式  1--2（表示1号2排）\n',
			  PRIMARY KEY (`Id`),
			  UNIQUE KEY `Id_UNIQUE` (`PropertyIds`,`BrandId`,`ClothesNumber`)
			) ENGINE=InnoDB AUTO_INCREMENT=12069 DEFAULT CHARSET=utf8;
*/

	
}
