package com.jiuyuan.entity;

import com.jiuyuan.util.anno.VersionControl;

/**
 * Created by Jinwu Zhan on 15/6/2. Modified by LWS on 2015/6/9 Modifications: 1. 增加收藏数、品牌、货号、风格、上架季节信息
 */
@VersionControl("1.0.0.0")
public class ProductV1 extends BaseMeta<Long> {

    /**
	 * 
	 */
    public static final long serialVersionUID = -3948558096146912007L;

    @Override
    public Long getCacheId() {
        return id;
    }

    public long id;

    public int saleState;

    public long saleStartTime;

    public long saleEndTime;

    public int saleCurrencyType;

    public int saleTotalCount;

    public int saleMonthlyMaxCount;

    public String title;

    public String classification;

    public String description;

    public int price;

    public String detailImages;

    public String locationProvince;

    public String locationCity;

    public String locationArea;

    public int expressFree;

    public String expressDetails;

    public String specifications;

    public String style;

    public String format;

    public String element;

    public String material;

    public long favorite;

    public String brand;

    public long serialNum;

    public String season;
    
    public int marketPrice;
    
    public long newModelID;
    
    public String attributeComment;
    
    /****************** add by Jeff.Zhan ****************/
    
    public String sizeTableImage;
    
    public String clothesNumber;
    
    private int status;
    
    private int brandId;
    
    private int showStatus;
    
    private double BottomPrice;
    
    private int MarketPriceMin;
    
    private int MarketPriceMax;
    
    /****************add by LWS at 2016/01/14**************/
    /**
     * 推广图片
     */
    public String promotionImage;
    
    public int weight;
    
    /*app 1.7*/
    private double cash;    
    private int jiuCoin;    
    private int restrictHistoryBuy;    
    private int restrictDayBuy;    
    private int restrictCycle;
    private int promotionSetting;    
    private double promotionCash;    
    private int promotionJiuCoin;
    private long promotionStartTime;    
    private long promotionEndTime;    
    private long restrictHistoryBuyTime;
    private long restrictDayBuyTime;
    private long lOWarehouseId;
    
    public String getPromotionImage() {
		return promotionImage;
	}

	public void setPromotionImage(String promotionImage) {
		this.promotionImage = promotionImage;
	}

    public String getClothesNumber() {
		return clothesNumber;
	}

	public void setClothesNumber(String clothesNumber) {
		this.clothesNumber = clothesNumber;
	}

	public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getSaleState() {
        return saleState;
    }

    public void setSaleState(int saleState) {
        this.saleState = saleState;
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

    public int getSaleCurrencyType() {
        return saleCurrencyType;
    }

    public void setSaleCurrencyType(int saleCurrencyType) {
        this.saleCurrencyType = saleCurrencyType;
    }

    public int getSaleTotalCount() {
        return saleTotalCount;
    }

    public void setSaleTotalCount(int saleTotalCount) {
        this.saleTotalCount = saleTotalCount;
    }

    public int getSaleMonthlyMaxCount() {
        return saleMonthlyMaxCount;
    }

    public void setSaleMonthlyMaxCount(int saleMonthlyMaxCount) {
        this.saleMonthlyMaxCount = saleMonthlyMaxCount;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getDetailImages() {
        return detailImages;
    }

    public void setDetailImages(String detailImages) {
        this.detailImages = detailImages;
    }

    public String getLocationProvince() {
        return locationProvince;
    }

    public void setLocationProvince(String locationProvince) {
        this.locationProvince = locationProvince;
    }

    public String getLocationCity() {
        return locationCity;
    }

    public void setLocationCity(String locationCity) {
        this.locationCity = locationCity;
    }

    public int getExpressFree() {
        return expressFree;
    }

    public void setExpressFree(int expressFree) {
        this.expressFree = expressFree;
    }

    public String getExpressDetails() {
        return expressDetails;
    }

    public void setExpressDetails(String expressDetails) {
        this.expressDetails = expressDetails;
    }

    public String getSpecifications() {
        return specifications;
    }

    public void setSpecifications(String specifications) {
        this.specifications = specifications;
    }

    public long getFavorite() {
        return favorite;
    }

    public void setFavorite(long favorite) {
        this.favorite = favorite;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getElement() {
        return element;
    }

    public void setElement(String element) {
        this.element = element;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public long getSerialNum() {
        return serialNum;
    }

    public void setSerialNum(long serialNum) {
        this.serialNum = serialNum;
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getLocationArea() {
        return locationArea;
    }

    public void setLocationArea(String locationArea) {
        this.locationArea = locationArea;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public long getNewModelID() {
        return newModelID;
    }

    public void setNewModelID(long newModelId) {
        this.newModelID = newModelId;
    }

    public String getAttributeComment() {
        return attributeComment;
    }

    public void setAttributeComment(String attributeComment) {
        this.attributeComment = attributeComment;
    }
    
    public String getSizeTableImage() {
		return sizeTableImage;
	}

	public void setSizeTableImage(String sizeTableImage) {
		this.sizeTableImage = sizeTableImage;
	}

	/**
     * @return the marketPrice
     */
    public int getMarketPrice() {
        return marketPrice;
    }

    /**
     * @param marketPrice the marketPrice to set
     */
    public void setMarketPrice(int marketPrice) {
        this.marketPrice = marketPrice;
    }

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getBrandId() {
		return brandId;
	}

	public void setBrandId(int brandId) {
		this.brandId = brandId;
	}
	
	public int getShowStatus() {
		return showStatus;
	}

	public void setShowStatus(int showStatus) {
		this.showStatus = showStatus;
	}

	public double getBottomPrice() {
		return BottomPrice;
	}

	public void setBottomPrice(double bottomPrice) {
		BottomPrice = bottomPrice;
	}

	public int getMarketPriceMin() {
		return MarketPriceMin;
	}

	public void setMarketPriceMin(int marketPriceMin) {
		MarketPriceMin = marketPriceMin;
	}

	public int getMarketPriceMax() {
		return MarketPriceMax;
	}

	public void setMarketPriceMax(int marketPriceMax) {
		MarketPriceMax = marketPriceMax;
	}

	public double getCash() {
		return cash;
	}

	public void setCash(double cash) {
		this.cash = cash;
	}

	public int getJiuCoin() {
		return jiuCoin;
	}

	public void setJiuCoin(int jiuCoin) {
		this.jiuCoin = jiuCoin;
	}

	public int getRestrictHistoryBuy() {
		return restrictHistoryBuy;
	}

	public void setRestrictHistoryBuy(int restrictHistoryBuy) {
		this.restrictHistoryBuy = restrictHistoryBuy;
	}

	public int getRestrictDayBuy() {
		return restrictDayBuy;
	}

	public void setRestrictDayBuy(int restrictDayBuy) {
		this.restrictDayBuy = restrictDayBuy;
	}

	public int getPromotionSetting() {
		return promotionSetting;
	}

	public void setPromotionSetting(int promotionSetting) {
		this.promotionSetting = promotionSetting;
	}

	public double getPromotionCash() {
		return promotionCash;
	}

	public void setPromotionCash(double promotionCash) {
		this.promotionCash = promotionCash;
	}

	public int getPromotionJiuCoin() {
		return promotionJiuCoin;
	}

	public void setPromotionJiuCoin(int promotionJiuCoin) {
		this.promotionJiuCoin = promotionJiuCoin;
	}

	public long getPromotionStartTime() {
		return promotionStartTime;
	}

	public void setPromotionStartTime(long promotionStartTime) {
		this.promotionStartTime = promotionStartTime;
	}

	public long getPromotionEndTime() {
		return promotionEndTime;
	}

	public void setPromotionEndTime(long promotionEndTime) {
		this.promotionEndTime = promotionEndTime;
	}

	public int getRestrictCycle() {
		return restrictCycle;
	}

	public void setRestrictCycle(int restrictCycle) {
		this.restrictCycle = restrictCycle;
	}

	public long getRestrictHistoryBuyTime() {
		return restrictHistoryBuyTime;
	}

	public void setRestrictHistoryBuyTime(long restrictHistoryBuyTime) {
		this.restrictHistoryBuyTime = restrictHistoryBuyTime;
	}

	public long getRestrictDayBuyTime() {
		return restrictDayBuyTime;
	}

	public void setRestrictDayBuyTime(long restrictDayBuyTime) {
		this.restrictDayBuyTime = restrictDayBuyTime;
	}

	public long getlOWarehouseId() {
		return lOWarehouseId;
	}

	public void setlOWarehouseId(long lOWarehouseId) {
		this.lOWarehouseId = lOWarehouseId;
	}

}
