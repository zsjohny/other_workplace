package com.yujj.entity.product;

import java.io.Serializable;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jiuyuan.constant.order.OrderConstants;
import com.jiuyuan.util.DateUtil;
import com.jiuyuan.util.VersionUtil;
import com.yujj.entity.Brand;
import com.yujj.web.helper.VersionControl;

@VersionControl("2.0.0.0")
public class Product implements Serializable {

    private static final long serialVersionUID = 166676858413714226L;

    private long id;

    private String name;

    private long categoryId;

    /**
     * 款号
     */
    @JsonIgnore
    private String productSeq;

    /**
     * 概要图json数组
     */
    @JsonIgnore
    private String detailImages;

    /**
     * 详情图 json数组
     */
    @JsonIgnore
    private String summaryImages;

    /**
     * 推广头图
     */
    private String promotionImage;

    @JsonIgnore
    private int status;

    private long saleStartTime;

    private long saleEndTime;

    private int saleCurrencyType;

    private int saleTotalCount;

    private int saleMonthlyMaxCount;

    private int price;

    private long favorite;

    private long assessmentCount;

    @JsonIgnore
    private int expressFree;

    @JsonIgnore
    private String expressDetails;

    @JsonIgnore
    private long createTime;

    @JsonIgnore
    private long updateTime;

    /************ add by LiuWeisheng ***************/
    private String remark;

    private int marketPrice;

    @JsonIgnore
    private String sizeTableImage;

    /************ add by Jeff.Zhan ******************/
    private long brandId;
    
    private double bottomPrice;
    
    private int marketPriceMin;
    
    private int marketPriceMax;
    
    private int weight;
    
    private double cash;    
    
    private int jiuCoin;    
    
    private int type;    
    
    private int restrictHistoryBuy;    
    
    private int restrictCycle;
    
    private int restrictDayBuy;    
    
    private int promotionSetting;    
    
    private double promotionCash;    
    

    
    private int promotionJiuCoin;
    
    private long promotionStartTime;    
    
    private long promotionEndTime;
    
    private long restrictHistoryBuyTime;
    
    private long restrictDayBuyTime;
    
    private long lOWarehouseId;
    
    private Brand brand;
 // added by Dongzhong 2016-07-09
    private String description;

	private long restrictId;
	
	private long vCategoryId;
	
	private long subscriptId;
	
	private String together;
	
	private String clothesNumber;

	private int promotionSaleCount;
	
	private int PromotionVisitCount;
	
	private int skuOnSaleNum;
	
	private long code;//统计识别码
	
//	private String brandIdentity;
	
	private String subscriptLogo;
	
	public static String appVersion = "1.8.11";
	
	
    private long lOWarehouseId2;
    
    private int setLOWarehouseId2;
    
    private double deductPercent;
    
    private String deductDesc;

    private int memberLevel;//会员商品等级(add by (黄杨烽))

    private String ladderPriceJson;//阶梯价格json (add by (黄杨烽))
    private String memberLadderPriceJson;//会员阶梯价格json (add by (黄杨烽))

    public String getLadderPriceJson() {
        return ladderPriceJson;
    }

    public void setLadderPriceJson(String ladderPriceJson) {
        this.ladderPriceJson = ladderPriceJson;
    }

    public String getMemberLadderPriceJson() {
        return memberLadderPriceJson;
    }

    public void setMemberLadderPriceJson(String memberLadderPriceJson) {
        this.memberLadderPriceJson = memberLadderPriceJson;
    }

    public int getMemberLevel() {
        return memberLevel;
    }

    public void setMemberLevel(int memberLevel) {
        this.memberLevel = memberLevel;
    }

    public int getPromotionSaleCount() {
		return promotionSaleCount;
	}

	public void setPromotionSaleCount(int promotionSaleCount) {
		this.promotionSaleCount = promotionSaleCount;
	}

	public int getPromotionVisitCount() {
		return PromotionVisitCount;
	}

	public void setPromotionVisitCount(int promotionVisitCount) {
		PromotionVisitCount = promotionVisitCount;
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
	
	public long getProductId() {
		return id;
	}

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    public String getProductSeq() {
        return productSeq;
    }

    public void setProductSeq(String productSeq) {
        this.productSeq = productSeq;
    }

    public String getSummaryImages() {
        return summaryImages;
    }

    public void setSummaryImages(String summaryImages) {
        this.summaryImages = summaryImages;
    }

    public String[] getSummaryImageArray() {
        JSONArray array = JSON.parseArray(getSummaryImages());
        if (array == null) {
            return new String[]{};
        }
        return array.toArray(new String[]{});
    }

    public String getDetailImages() {
        return detailImages;
    }

    public void setDetailImages(String detailImages) {
        this.detailImages = detailImages;
    }

    public String[] getDetailImageArray() {
    	try {

            JSONArray array = JSON.parseArray(getDetailImages());
            if (array == null) {
                return new String[]{};
            }
            return array.toArray(new String[]{});
		} catch (Exception e) {
			return null;
		}
    }
    /**
     * 得到概要图的第一张
     * @return
     */
    public String getFirstDetailImage() {
    	String image = "";
    	 	String[] detailImageArray = getDetailImageArray();
    	    if (detailImageArray.length > 0) {
    	        image = detailImageArray[0];
    	    }
    	    return image;
    }
    
   

    public String getPromotionImage() {
        return promotionImage;
    }

    public void setPromotionImage(String promotionImage) {
        this.promotionImage = promotionImage;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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
		if(isPromotion()) {
			return saleTotalCount + promotionSaleCount;
		} 
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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public long getFavorite() {
        return favorite;
    }

    public void setFavorite(long favorite) {
        this.favorite = favorite;
    }

    public long getAssessmentCount() {
        return assessmentCount;
    }

    public void setAssessmentCount(long assessmentCount) {
        this.assessmentCount = assessmentCount;
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
    

    public int getSkuOnSaleNum() {
		return skuOnSaleNum;
	}

	public void setSkuOnSaleNum(int skuOnSaleNum) {
		this.skuOnSaleNum = skuOnSaleNum;
	}

	public boolean isOnSaling() {
    	if(this.skuOnSaleNum > 0){
    		return true;
    		
    	}else{
    		return false;
    	}
//        long time = System.currentTimeMillis();
//        if(status == -1) {
//        	return false;
//        }
//        if (getSaleStartTime() > 0 && getSaleEndTime() > 0) {
//            return getSaleStartTime() <= time && time <= getSaleEndTime();
//        }
//
//        if (getSaleEndTime() <= 0) {
//            return getSaleStartTime() <= time;
//        }
//        return getSaleEndTime() >= time;
    }

    /**
     * @return the remark
     */
    public String getRemark() {
        return remark;
    }

    /**
     * @param remark the remark to set
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(int marketPrice) {
        this.marketPrice = marketPrice;
    }

    public String getSizeTableImage() {
        return sizeTableImage;
    }

    public void setSizeTableImage(String sizeTableImage) {
        this.sizeTableImage = sizeTableImage;
    }

    public String[] getSizeTableImageArray() {
        JSONArray array = JSON.parseArray(getSizeTableImage());
        if (array == null) {
            return new String[]{};
        }
        return array.toArray(new String[]{});
    }

    public Map<String, Object> toSimpleMap() {
        return toSimpleMap(false);
    }
    
    public Map<String, Object> toSimpleMap15() {
        return toSimpleMap15(false);
    }
    
    public long getBrandId() {
		return brandId;
	}

	public void setBrandId(long brandId) {
		this.brandId = brandId;
	}
	
	public String getImage() {
		String image = StringUtils.defaultString(getPromotionImage());

		
		if (StringUtils.isBlank(image)) {
	        String[] detailImageArray = getDetailImageArray();
	        if (detailImageArray.length > 0) {
	            image = detailImageArray[0];
	        }
		}

		return image;
	}

	public Map<String, Object> toSimpleMap(boolean promotionImage) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", getId());
        map.put("name", getName());
        String image = promotionImage ? StringUtils.defaultString(getPromotionImage()) : "";
        if (StringUtils.isBlank(image)) {
            String[] detailImageArray = getDetailImageArray();
            if (detailImageArray.length > 0) {
                image = detailImageArray[0];
            }
        }
        map.put("image", image);
        map.put("price", getPrice());
        map.put("marketPrice", getMarketPrice());
        map.put("saleTotalCount", getSaleTotalCount());
        map.put("onSaling", isOnSaling());

        return map;
    }
	
	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public double getBottomPrice() {
		return bottomPrice;
	}

	public void setBottomPrice(double bottomPrice) {
		this.bottomPrice = bottomPrice;
	}

	public int getMarketPriceMin() {
		if(this.getMarketPrice() > 0){
			return 0;
		}
		return marketPriceMin;
	}

	public void setMarketPriceMin(int marketPriceMin) {
		this.marketPriceMin = marketPriceMin;
	}

	public int getMarketPriceMax() {
		if(this.getMarketPrice() > 0){
			return 0;
		}
		return marketPriceMax;
	}

	public void setMarketPriceMax(int marketPriceMax) {
		this.marketPriceMax = marketPriceMax;
	}

	public int getPayAmountInCents() {
		return price * OrderConstants.PAY_CENTS_PER_UNIT;
	}

	public double getCash() {
		if(VersionUtil.compareVersion(appVersion , "1.8.11") < 0){
			return getCurrenCash();
        } else{
        	return cash;
        }
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
	
	

	public Brand getBrand() {
		return brand;
	}

	public void setBrand(Brand brand) {
		this.brand = brand;
	}

	public void setlOWarehouseId(long lOWarehouseId) {
		this.lOWarehouseId = lOWarehouseId;
	}

	private boolean isPromotion() {
		return getPromotionSetting() == 1 && 
				getPromotionStartTime() < System.currentTimeMillis() && 
				getPromotionEndTime() > System.currentTimeMillis();
	}
	public boolean getIsPromotion() {
		return isPromotion();
	}


	public double getCurrenCash() {
		if(isPromotion()) {
			return promotionCash;
		} 
		return cash;
	}
	

	public int getCurrentJiuCoin() {
		if(isPromotion()) {
			return promotionJiuCoin;
		}
		return jiuCoin;
	}
	
	public Map<String, Object> toSimpleMap15(boolean promotionImage) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", getId());
        map.put("name", getName());
        String image = promotionImage ? StringUtils.defaultString(getPromotionImage()) : "";
        if (StringUtils.isBlank(image)) {
            String[] detailImageArray = getDetailImageArray();
            if (detailImageArray.length > 0) {
                image = detailImageArray[0];
            }
        }
        map.put("id", id);
        map.put("image", image);
        map.put("price", getPrice());
        map.put("marketPrice", getMarketPrice());
        map.put("bottomPrice", getBottomPrice());
        map.put("marketPriceMin", getMarketPriceMin());
        map.put("marketPriceMax", getMarketPriceMax());
        map.put("payAmountInCents", getPayAmountInCents());
        map.put("onSaling", isOnSaling());
        map.put("saleTotalCount", getSaleTotalCount());
        map.put("onSaling", isOnSaling());
        map.put("weight", weight);
        map.put("deductDesc", getDeductDesc());
        map.put("promotionSaleCount", promotionSaleCount);
        map.put("promotionSaleCountStr", "销量：" + (promotionSaleCount + saleTotalCount) + "件");
        map.put("deductPercent", getDeductPercent());
        map.put("subscriptLogo", subscriptLogo);
        map.put("jiuCoin", getJiuCoin());
        if(VersionUtil.compareVersion(appVersion , "1.8.11") < 0){
        	map.put("cash", getCurrenCash());
        } else{
        	map.put("cash", cash);
        }
//    	map.put("cash",getCurrenCash());
     
        map.put("promotionCash", getPromotionCash());
        map.put("currenCash", getCurrenCash());
        map.put("isPromotion", isPromotion());
        map.put("deductPercent", deductPercent);
        map.put("loWarehouseId", lOWarehouseId);
        return map;
    }

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public long getRestrictId() {
		return restrictId;
	}

	public void setRestrictId(long restrictId) {
		this.restrictId = restrictId;
	}

	public long getvCategoryId() {
		return vCategoryId;
	}

	public void setvCategoryId(long vCategoryId) {
		this.vCategoryId = vCategoryId;
	}

	public String getTogether() {
		return together;
	}

	public void setTogether(String together) {
		this.together = together;
	}

	public long getSubscriptId() {
		return subscriptId;
	}

	public void setSubscriptId(long subscriptId) {
		this.subscriptId = subscriptId;
	}

	public String getSubscriptLogo() {
		return subscriptLogo;
	}
	
	public String getPromotionSaleCountStr() {
		return "销量：" + (promotionSaleCount + saleTotalCount) + "件";
	}
	
	public String getPromotionStatus() {
		long time = System.currentTimeMillis();
		if(time < promotionStartTime){
			return "即将开始";
		}else if(time < promotionEndTime && time >= promotionStartTime){
			return "进行中";
		}else if(time >= promotionEndTime ){
			return "已结束";
		}else return "";
		
	}

	public void setSubscriptLogo(String subscriptLogo) {
		this.subscriptLogo = subscriptLogo;
	}

	public long getlOWarehouseId2() {
		return lOWarehouseId2;
	}

	public void setlOWarehouseId2(long lOWarehouseId2) {
		this.lOWarehouseId2 = lOWarehouseId2;
	}

	public int getSetLOWarehouseId2() {
		return setLOWarehouseId2;
	}

	public void setSetLOWarehouseId2(int setLOWarehouseId2) {
		this.setLOWarehouseId2 = setLOWarehouseId2;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	public long getPromotionLastTime() {
		long time = System.currentTimeMillis();
		return promotionEndTime - time;
	}


	public double getDeductPercent() {
		return deductPercent;
	}
	
//	public String getDeductPercentDesc() {
//		if(deductPercent > 0){
//			return "玖币抵" + getDeductPercent() + "%";	
//		}else{
//			return "";	
//		}
//	}

	public void setDeductPercent(double deductPercent) {
		this.deductPercent = deductPercent;
	}

	public String getDeductDesc() {
		return deductDesc;
	}

	public void setDeductDesc(String deductDesc) {
		this.deductDesc = deductDesc;
	}


	public long getPromotionSortTime() {
		long time = System.currentTimeMillis();
		long futureTime = 0;
		try {
			futureTime = DateUtil.convertToMSEL("2117-01-01 23:59:59");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 if(time >= promotionEndTime ){
			return futureTime;
		}else{
			return promotionStartTime;
		}
		
	}


	public long getCode() {
		return code;
	}

	public void setCode(long code) {
		this.code = code;
	}

	
	
	
}
