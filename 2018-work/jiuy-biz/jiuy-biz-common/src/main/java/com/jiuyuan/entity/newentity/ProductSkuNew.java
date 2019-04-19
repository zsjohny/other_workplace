package com.jiuyuan.entity.newentity;

import com.baomidou.mybatisplus.enums.IdType;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author 赵兴林
 * @since 2017-10-09
 */
@TableName("yjj_ProductSKU")
public class ProductSkuNew extends Model<ProductSkuNew> {
	
	
//	大于等于-1  状态:-3废弃，-2停用，-1下架，0正常，1定时上架
	public static final int up_sold = 0;//正常，已上架
	public static final int down_sold = -1;//下架，已下架
	

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
	@TableId(value="Id", type= IdType.AUTO)
	private Long Id;
	/**
     * 商品id
     */
	private Long ProductId;
	/**
     * 颜色ID
     */
	private Long colorId;
	/**
     * 颜色名称
     */
	private String colorName;
	/**
     * 尺码ID
     */
	private Long sizeId;
	/**
     * 尺码名称
     */
	private String sizeName;
	
    /**
     * 商品SKU属性值聚合，PropertyNameId:PropertyValueId形式，多个以英文,隔开
     */
	private String PropertyIds;
    /**
     * 价格，人民币以分为单位，玖币以1为单位
     */
	private Integer Price;
    /**
     * 库存量
     */
	private Integer RemainCount;
    /**
     * 对应SKU的图片信息
     */
	private String SpecificImage;
    /**
     * 状态:-3废弃，-2停用，-1下架，0正常，1定时上架
     */
	private Integer Status;
    /**
     * 创建时间
     */
	private Long CreateTime;
    /**
     * 更新时间
     */
	private Long UpdateTime;
    /**
     * sku编码
     */
	private Long SkuNo;
	private Double Cash;
    /**
     * 重量
     */
	private Double Weight;
    /**
     * 

货品名称

     */
	private String Name;
    /**
     * 市场价（吊牌价）
     */
	private Double MarketPrice;
    /**
     * 成本价
     */
	private Double CostPrice;
    /**
     * 'sku款号'
     */
	private String ClothesNumber;
    /**
     * 主仓库
     */
	private Long LOWarehouseId;
    /**
     * '库存保留时间' 天
     */
	private Integer RemainKeepTime;
    /**
     * 品牌id
     */
	private Long BrandId;
    /**
     * 上架时间
     */
	private Long SaleStartTime;
    /**
     * 下架时间
     */
	private Long SaleEndTime;
	private Integer Sort;
    /**
     * 库存锁定量
     */
	private Integer RemainCountLock;
    /**
     * 库存锁定开始时间
     */
	private Long RemainCountStartTime;
    /**
     * 库存锁定结束时间
     */
	private Long RemainCountEndTime;
    /**
     * 是否锁库存
     */
	private Integer IsRemainCountLock;
    /**
     * 推送erp时间
     */
	private Long PushTime;
    /**
     * 推广销量

     */
	private Integer PromotionSaleCount;
    /**
     * 推广访问量

     */
	private Integer PromotionVisitCount;
    /**
     * 副仓库库存
     */
	private Integer RemainCount2;
    /**
     * 副仓库
     */
	private Long LOWarehouseId2;
	private Integer SetLOWarehouseId2;
    /**
     * 货架位置格式  1--2（表示1号2排）

     */
	private String Position;
	/**
	 * 定时更新库存时间, 时间为上架后N天 或者 指定日期时间戳(根据auto_set_type判断),默认值0
	 */
	@TableField("timing_set_remain_count_time")
	private Long timingSetRemainCountTime;
	/**
	 * 定时更新库存类型, 0关闭(不更新), 1指定日期更新 ,2上架后N天更新, 默认值0
	 */
	@TableField("timing_set_type")
	private Integer timingSetType;
	/**
	 * 定时更新库存数,默认值0
	 */
	@TableField("timing_set_count")
	private Integer timingSetCount;

	/**
	 * 小程序ID
	 */
	@TableField("wxa_product_id")
	private Integer wxaProductId;

	/**
	 * sku类别 1:品牌商sku,2:门店
	 */
	@TableField("own_type")
	private Integer ownType;

	public Integer getOwnType() {
		return ownType;
	}

	public void setOwnType(Integer ownType) {
		this.ownType = ownType;
	}

	public Integer getWxaProductId() {
		return wxaProductId;
	}

	public void setWxaProductId(Integer wxaProductId) {
		this.wxaProductId = wxaProductId;
	}

	public Long getTimingSetRemainCountTime() {
		return timingSetRemainCountTime;
	}

	public void setTimingSetRemainCountTime(Long timingSetRemainCountTime) {
		this.timingSetRemainCountTime = timingSetRemainCountTime;
	}

	public Integer getTimingSetType() {
		return timingSetType;
	}

	public void setTimingSetType(Integer timingSetType) {
		this.timingSetType = timingSetType;
	}

	public Integer getTimingSetCount() {
		return timingSetCount;
	}

	public void setTimingSetCount(Integer timingSetCount) {
		this.timingSetCount = timingSetCount;
	}

	public Long getId() {
		return Id;
	}

	public void setId(Long Id) {
		this.Id = Id;
	}

	public Long getProductId() {
		return ProductId;
	}

	public void setProductId(Long ProductId) {
		this.ProductId = ProductId;
	}

	public String getPropertyIds() {
		return PropertyIds;
	}

	public void setPropertyIds(String PropertyIds) {
		this.PropertyIds = PropertyIds;
	}

	public Integer getPrice() {
		return Price;
	}

	public void setPrice(Integer Price) {
		this.Price = Price;
	}

	public Integer getRemainCount() {
		return RemainCount;
	}

	public void setRemainCount(Integer RemainCount) {
		this.RemainCount = RemainCount;
	}

	public String getSpecificImage() {
		return SpecificImage;
	}

	public void setSpecificImage(String SpecificImage) {
		this.SpecificImage = SpecificImage;
	}

	public Integer getStatus() {
		return Status;
	}

	public void setStatus(Integer Status) {
		this.Status = Status;
	}

	public Long getCreateTime() {
		return CreateTime;
	}

	public void setCreateTime(Long CreateTime) {
		this.CreateTime = CreateTime;
	}

	public Long getUpdateTime() {
		return UpdateTime;
	}

	public void setUpdateTime(Long UpdateTime) {
		this.UpdateTime = UpdateTime;
	}

	public Long getSkuNo() {
		return SkuNo;
	}

	public void setSkuNo(Long SkuNo) {
		this.SkuNo = SkuNo;
	}

	

	public Double getCash() {
		return Cash;
	}

	public void setCash(Double cash) {
		Cash = cash;
	}

	

	public Double getWeight() {
		return Weight;
	}

	public void setWeight(Double weight) {
		Weight = weight;
	}

	public String getName() {
		return Name;
	}

	public void setName(String Name) {
		this.Name = Name;
	}

	public Double getMarketPrice() {
		return MarketPrice;
	}

	public void setMarketPrice(Double MarketPrice) {
		this.MarketPrice = MarketPrice;
	}

	public Double getCostPrice() {
		return CostPrice;
	}

	public void setCostPrice(Double CostPrice) {
		this.CostPrice = CostPrice;
	}

	public String getClothesNumber() {
		return ClothesNumber;
	}

	public void setClothesNumber(String ClothesNumber) {
		this.ClothesNumber = ClothesNumber;
	}

	public Long getLOWarehouseId() {
		return LOWarehouseId;
	}

	public void setLOWarehouseId(Long LOWarehouseId) {
		this.LOWarehouseId = LOWarehouseId;
	}

	public Integer getRemainKeepTime() {
		return RemainKeepTime;
	}

	public void setRemainKeepTime(Integer RemainKeepTime) {
		this.RemainKeepTime = RemainKeepTime;
	}

	public Long getBrandId() {
		return BrandId;
	}

	public void setBrandId(Long BrandId) {
		this.BrandId = BrandId;
	}

	public Long getSaleStartTime() {
		return SaleStartTime;
	}

	public void setSaleStartTime(Long SaleStartTime) {
		this.SaleStartTime = SaleStartTime;
	}

	public Long getSaleEndTime() {
		return SaleEndTime;
	}

	public void setSaleEndTime(Long SaleEndTime) {
		this.SaleEndTime = SaleEndTime;
	}

	public Integer getSort() {
		return Sort;
	}

	public void setSort(Integer Sort) {
		this.Sort = Sort;
	}

	public Integer getRemainCountLock() {
		return RemainCountLock;
	}

	public void setRemainCountLock(Integer RemainCountLock) {
		this.RemainCountLock = RemainCountLock;
	}

	public Long getRemainCountStartTime() {
		return RemainCountStartTime;
	}

	public void setRemainCountStartTime(Long RemainCountStartTime) {
		this.RemainCountStartTime = RemainCountStartTime;
	}

	public Long getRemainCountEndTime() {
		return RemainCountEndTime;
	}

	public void setRemainCountEndTime(Long RemainCountEndTime) {
		this.RemainCountEndTime = RemainCountEndTime;
	}

	public Integer getIsRemainCountLock() {
		return IsRemainCountLock;
	}

	public void setIsRemainCountLock(Integer IsRemainCountLock) {
		this.IsRemainCountLock = IsRemainCountLock;
	}

	public Long getPushTime() {
		return PushTime;
	}

	public void setPushTime(Long PushTime) {
		this.PushTime = PushTime;
	}

	public Integer getPromotionSaleCount() {
		return PromotionSaleCount;
	}

	public void setPromotionSaleCount(Integer PromotionSaleCount) {
		this.PromotionSaleCount = PromotionSaleCount;
	}

	public Integer getPromotionVisitCount() {
		return PromotionVisitCount;
	}

	public void setPromotionVisitCount(Integer PromotionVisitCount) {
		this.PromotionVisitCount = PromotionVisitCount;
	}

	public Integer getRemainCount2() {
		return RemainCount2;
	}

	public void setRemainCount2(Integer RemainCount2) {
		this.RemainCount2 = RemainCount2;
	}

	public Long getLOWarehouseId2() {
		return LOWarehouseId2;
	}

	public void setLOWarehouseId2(Long LOWarehouseId2) {
		this.LOWarehouseId2 = LOWarehouseId2;
	}

	public Integer getSetLOWarehouseId2() {
		return SetLOWarehouseId2;
	}

	public void setSetLOWarehouseId2(Integer SetLOWarehouseId2) {
		this.SetLOWarehouseId2 = SetLOWarehouseId2;
	}

	public String getPosition() {
		return Position;
	}

	public void setPosition(String Position) {
		this.Position = Position;
	}

	public Long getColorId() {
		return colorId;
	}

	public void setColorId(Long colorId) {
		this.colorId = colorId;
	}

	public String getColorName() {
		return colorName;
	}

	public void setColorName(String colorName) {
		this.colorName = colorName;
	}

	public Long getSizeId() {
		return sizeId;
	}

	public void setSizeId(Long sizeId) {
		this.sizeId = sizeId;
	}

	public String getSizeName() {
		return sizeName;
	}

	public void setSizeName(String sizeName) {
		this.sizeName = sizeName;
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
        if(getStatus() <= -1) {
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
	protected Serializable pkVal() {
		return this.Id;
	}

}
