package com.jiuyuan.entity;

import java.io.Serializable;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.annotations.TableField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jiuy.web.controller.util.DateUtil;
import com.jiuyuan.constant.order.OrderConstants;
import com.jiuyuan.entity.newentity.ProductNew;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;
import com.yujj.web.helper.VersionControl;
/**
 * CREATE TABLE `yjj_Product` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '商品id',
  `Name` varchar(200) NOT NULL COMMENT '商品名',
  `CategoryId` bigint(20) NOT NULL DEFAULT '0' COMMENT '所属分类id',
  `DetailImages` varchar(4096) NOT NULL DEFAULT '[]' COMMENT '橱窗图片:JSON 格式数组',
  `SummaryImages` varchar(4096) NOT NULL DEFAULT '[]' COMMENT '详情图 json数组',
  `Status` tinyint(4) DEFAULT '0' COMMENT '状态:-1删除，0正常',
  `SaleStartTime` bigint(20) NOT NULL COMMENT '上架时间',
  `SaleEndTime` bigint(20) NOT NULL COMMENT '下架时间',
  `SaleCurrencyType` tinyint(4) NOT NULL DEFAULT '0' COMMENT '销售类型：0-人民币 1-玖币',
  `SaleTotalCount` int(11) DEFAULT '0' COMMENT '总销量',
  `SaleMonthlyMaxCount` int(11) DEFAULT '0' COMMENT '月销量最大值，每月初重新计算',
  `Price` int(11) NOT NULL DEFAULT '0' COMMENT '价格冗余字段，用于列表显示，添加SKU的时候更新一个最小的价格到这个字段',
  `Favorite` bigint(20) DEFAULT '0' COMMENT '收藏数',
  `AssessmentCount` bigint(20) DEFAULT '0' COMMENT '评论数',
  `ExpressFree` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否免邮 0-免邮 1-不免邮',
  `ExpressDetails` varchar(1024) DEFAULT NULL COMMENT 'JSON 格式的邮费说明',
  `CreateTime` bigint(20) NOT NULL COMMENT '创建时间',
  `UpdateTime` bigint(20) NOT NULL COMMENT '更新时间',
  `vip` tinyint(4) DEFAULT '0',
  `ProductSeq` varchar(50) DEFAULT NULL COMMENT '款号',
  `remark` text COMMENT '文本描述的产品信息   商品规格',
  `MarketPrice` decimal(10,2) DEFAULT NULL COMMENT '市场价，不是玖币价格',
  `SizeTableImage` varchar(1024) DEFAULT NULL,
  `ClothesNumber` varchar(45) DEFAULT NULL COMMENT '商品款号 和 ProductSeq 重复了.',
  `PromotionImage` varchar(256) DEFAULT NULL COMMENT '推广图片存储路径',
  `Weight` int(11) DEFAULT '0' COMMENT '商品权重',
  `BrandId` bigint(20) unsigned DEFAULT '0' COMMENT '商品的品牌id',
  `ShowStatus` int(4) NOT NULL DEFAULT '0' COMMENT '0:默认全局显示，其他:自定义显示',
  `BottomPrice` decimal(10,2) DEFAULT '0.00',
  `MarketPriceMin` int(10) DEFAULT '0',
  `MarketPriceMax` int(10) DEFAULT '0',
  `Type` tinyint(4) DEFAULT '1' COMMENT '0: 零售 1:零售/批发 2:批发',
  `WholeSaleCash` decimal(10,2) DEFAULT '0.00' COMMENT '批发价',
  `Cash` decimal(10,2) DEFAULT NULL COMMENT '消费人民币\n',
  `JiuCoin` int(11) DEFAULT NULL COMMENT '消费玖币',
  `RestrictHistoryBuy` int(11) NOT NULL DEFAULT '-1' COMMENT '历史限购 -1:不限 ',
  `RestrictDayBuy` int(11) NOT NULL DEFAULT '-1' COMMENT '单日限购 -1:不限 ',
  `PromotionSetting` tinyint(4) NOT NULL DEFAULT '0' COMMENT '价格促销设置 0-无 1-定义\n',
  `PromotionCash` decimal(10,2) DEFAULT NULL COMMENT '促销“消费人民币”价',
  `PromotionJiuCoin` int(11) DEFAULT NULL COMMENT '促销“消费玖币”价',
  `PromotionStartTime` bigint(20) DEFAULT NULL COMMENT '价格促销开始时间',
  `PromotionEndTime` bigint(20) DEFAULT NULL COMMENT '价格促销结束时间',
  `RestrictCycle` int(11) NOT NULL DEFAULT '0' COMMENT '限购范围（天） 默认为0，代表无限购周期，',
  `RestrictHistoryBuyTime` bigint(20) NOT NULL DEFAULT '0',
  `RestrictDayBuyTime` bigint(20) NOT NULL DEFAULT '0',
  `LOWarehouseId` bigint(20) DEFAULT NULL COMMENT '主仓库',
  `Description` text,
  `RestrictId` bigint(20) DEFAULT NULL COMMENT '组合限购id',
  `VCategoryId` bigint(20) DEFAULT NULL COMMENT '虚拟分类Id',
  `Together` varchar(200) DEFAULT NULL COMMENT '搭配推荐',
  `CartSttstcs` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '购物车推荐统计',
  `HotSttstcs` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '热卖推荐统计',
  `SubscriptId` bigint(20) DEFAULT NULL COMMENT '角标Id',
  `BuyType` tinyint(4) DEFAULT '0' COMMENT '购买方式，0：原价或者折扣价，1：原价，2：折扣',
  `DisplayType` tinyint(4) DEFAULT '0' COMMENT '橱窗价格展示，0：原价或者折扣价，1：原价，2：折扣',
  `PromotionBuyType` tinyint(4) DEFAULT '0' COMMENT '促销购买方式，0：原价或者折扣价，1：原价，2：折扣',
  `PromotionDisplayType` tinyint(4) DEFAULT '0' COMMENT '促销橱窗价格展示，0：原价或者折扣价，1：原价，2：折扣\n',
  `PromotionSaleCount` int(11) DEFAULT '0' COMMENT '推广销量',
  `PromotionVisitCount` int(11) DEFAULT '0' COMMENT '推广访问量',
  `LOWarehouseId2` bigint(20) NOT NULL DEFAULT '0' COMMENT '副仓库',
  `SetLOWarehouseId2` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0:不设置副仓 1:设置副仓',
  `DeductPercent` decimal(10,2) DEFAULT '0.00',
  `videoUrl` varchar(256) DEFAULT NULL COMMENT '商品视频url',
  `videoName` varchar(256) DEFAULT '' COMMENT '视频名称',
  `videoFileId` bigint(20) DEFAULT '0' COMMENT '商品视频fileId',
  `ladderPriceJson` varchar(50) DEFAULT NULL COMMENT '阶梯价格JSON',
  `maxLadderPrice` decimal(10,2) DEFAULT '0.00' COMMENT '最大阶梯价格',
  `supplierId` bigint(20) DEFAULT '0' COMMENT '供应商ID',
  `minLadderPrice` decimal(10,2) DEFAULT '0.00' COMMENT '最小阶梯价格',
  PRIMARY KEY (`Id`),
  UNIQUE KEY `Id_UNIQUE` (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=2698 DEFAULT CHARSET=utf8;


 * @author Administrator
 *
 */
@VersionControl("2.0.0.0")
public class Product implements Serializable {

	private static final long serialVersionUID = 166676858413714226L;

	private String classification;

	private String season;

	private String brandName;

	private int showStatus;

	private double cartSttstcs;

	private double hotSttstcs;

	private int buyType;

	private int displayType;

	private int promotionBuyType;

	private int promotionDisplayType;

	private int promotionVisitCount;

	private long code;// 统计识别码

	private String shareTitle;

	private String shareImg;

	private String shareDesc;

	// added by wwj 2017-04-20
	private double deductPercent; // 玖币抵扣百分比

	private String videoName;

	private long videoFileId;

	private long id;

	private String name;

	private long categoryId;

	private long partnerId;
	private String mainImg;
	/**
	 * 款号
	 */
	private String productSeq;

	/**
	 * 概要图json数组
	 */
	private String detailImages;

	/**
	 * 详情图 json数组
	 */
	private String summaryImages;

	/**
	 * 推广头图
	 */
	private String promotionImage;

	private int status;

	private long saleStartTime;

	private long saleEndTime;

	private int saleCurrencyType;

	private int saleTotalCount;

	private int saleMonthlyMaxCount;

	private int price;

	private long favorite;

	private long assessmentCount;

	private int expressFree;

	private String expressDetails;

	private long createTime;

	private long updateTime;

	/************ add by LiuWeisheng ***************/
	private String remark;

	private int marketPrice;

	private String sizeTableImage;

	/************ add by Jeff.Zhan ******************/
	private long brandId;

	private double bottomPrice;

	private int marketPriceMin;

	private int marketPriceMax;

	private int weight;

	private double cash;

	private int jiuCoin;

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

	private long lOWarehouseId2;

	private int setLOWarehouseId2;

	private Map<String, Object> brand;
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

	private int type;
	/**
	 * VIP商品 0不是vip商品 1是vip商品
	 */
	private int vip;

	private double wholeSaleCash;

	private String subscriptLogo;

	private List<Map<String, Object>> skuList;

	private String videoUrl;
	private double minLadderPrice;//最小阶梯价格
	private double maxLadderPrice;//最大阶梯价格
	private String ladderPriceJson;//阶梯价格JSON
	private long supplierId;//供应商ID
	/**
	 * 角标URL
	 */
	private String badgeImage;
	
	
	private double currentLadderPriceByBuyCount = 0;
	private int state;//最新的商品状态字段
	private int delState;//最新的商品是否删除
	private int memberLevel;//会员商品等级(add by (黄杨烽))

	private String memberLadderPriceJson;//会员阶梯价格json (add by (黄杨烽))
	private double memberLadderPriceMin;//会员最小价格json (add by (黄杨烽))
	private double memberLadderPriceMax;//会员最大价格 (add by (黄杨烽))

	public void setMemberLevel(int memberLevel) {
		this.memberLevel = memberLevel;
	}

	public double getMemberLadderPriceMin() {
		return memberLadderPriceMin;
	}

	public void setMemberLadderPriceMin(double memberLadderPriceMin) {
		this.memberLadderPriceMin = memberLadderPriceMin;
	}

	public double getMemberLadderPriceMax() {
		return memberLadderPriceMax;
	}

	public void setMemberLadderPriceMax(double memberLadderPriceMax) {
		this.memberLadderPriceMax = memberLadderPriceMax;
	}

	public String getMemberLadderPriceJson() {
		return memberLadderPriceJson;
	}

	public void setMemberLadderPriceJson(String memberLadderPriceJson) {
		this.memberLadderPriceJson = memberLadderPriceJson;
	}

	public Integer getMemberLevel() {
		return memberLevel;
	}

	public void setMemberLevel(Integer memberLevel) {
		this.memberLevel = memberLevel;
	}

	/**
	 * @see: 商品的橱窗视频地址
	 */
	@TableField("vedio_main")
	private String vedioMain;

	public String getVedioMain() {
		return vedioMain;
	}

	public void setVedioMain(String vedioMain) {
		this.vedioMain = vedioMain;
	}

	private static final Log logger = LogFactory.get();
	
	public void setCurrentLadderPriceByBuyCount(double currentLadderPriceByBuyCount) {
		this.currentLadderPriceByBuyCount = currentLadderPriceByBuyCount;
	}
	
	public double getCurrentLadderPriceByBuyCount( ) {
		return currentLadderPriceByBuyCount;
	}



	@Override
	public String toString() {
		return "Product [classification=" + classification + ", season=" + season + ", brandName=" + brandName
				+ ", showStatus=" + showStatus + ", cartSttstcs=" + cartSttstcs + ", hotSttstcs=" + hotSttstcs
				+ ", buyType=" + buyType + ", displayType=" + displayType + ", promotionBuyType=" + promotionBuyType
				+ ", promotionDisplayType=" + promotionDisplayType + ", promotionVisitCount=" + promotionVisitCount
				+ ", code=" + code + ", shareTitle=" + shareTitle + ", shareImg=" + shareImg + ", shareDesc="
				+ shareDesc + ", deductPercent=" + deductPercent + ", videoName=" + videoName + ", videoFileId="
				+ videoFileId + ", id=" + id + ", name=" + name + ", categoryId=" + categoryId + ", partnerId="
				+ partnerId + ", productSeq=" + productSeq + ", detailImages=" + detailImages + ", summaryImages="
				+ summaryImages + ", promotionImage=" + promotionImage + ", status=" + status + ", saleStartTime="
				+ saleStartTime + ", saleEndTime=" + saleEndTime + ", saleCurrencyType=" + saleCurrencyType
				+ ", saleTotalCount=" + saleTotalCount + ", saleMonthlyMaxCount=" + saleMonthlyMaxCount + ", price="
				+ price + ", favorite=" + favorite + ", assessmentCount=" + assessmentCount + ", expressFree="
				+ expressFree + ", expressDetails=" + expressDetails + ", createTime=" + createTime + ", updateTime="
				+ updateTime + ", remark=" + remark + ", marketPrice=" + marketPrice + ", sizeTableImage="
				+ sizeTableImage + ", brandId=" + brandId + ", bottomPrice=" + bottomPrice + ", marketPriceMin="
				+ marketPriceMin + ", marketPriceMax=" + marketPriceMax + ", weight=" + weight + ", cash=" + cash
				+ ", jiuCoin=" + jiuCoin + ", restrictHistoryBuy=" + restrictHistoryBuy + ", restrictCycle="
				+ restrictCycle + ", restrictDayBuy=" + restrictDayBuy + ", promotionSetting=" + promotionSetting
				+ ", promotionCash=" + promotionCash + ", promotionJiuCoin=" + promotionJiuCoin
				+ ", promotionStartTime=" + promotionStartTime + ", promotionEndTime=" + promotionEndTime
				+ ", restrictHistoryBuyTime=" + restrictHistoryBuyTime + ", restrictDayBuyTime=" + restrictDayBuyTime
				+ ", lOWarehouseId=" + lOWarehouseId + ", lOWarehouseId2=" + lOWarehouseId2 + ", setLOWarehouseId2="
				+ setLOWarehouseId2 + ", brand=" + brand + ", description=" + description + ", restrictId=" + restrictId
				+ ", vCategoryId=" + vCategoryId + ", subscriptId=" + subscriptId + ", together=" + together
				+ ", clothesNumber=" + clothesNumber + ", promotionSaleCount=" + promotionSaleCount
				+ ", PromotionVisitCount=" + PromotionVisitCount + ", skuOnSaleNum=" + skuOnSaleNum + ", type=" + type
				+ ", vip=" + vip + ", wholeSaleCash=" + wholeSaleCash + ", subscriptLogo=" + subscriptLogo
				+ ", skuList=" + skuList + ", videoUrl=" + videoUrl + "]";
	}

	public long getCode() {
		return code;
	}

	public void setCode(long code) {
		this.code = code;
	}

	public String[] getSizeTableImageArray() {
		JSONArray array = JSON.parseArray(getSizeTableImage());
		if (array == null) {
			return new String[] {};
		}
		return array.toArray(new String[] {});
	}

	// public String[] getSizeTableImageArray() {
	// String[] sizeTableImageArray = null;
	// try{
	// JSONArray retArray = JSON.parseArray(getSizeTableImage().equals("")?
	// "":getSizeTableImage());
	// sizeTableImageArray = retArray.toArray(new String[] {});
	// } catch(Exception e) {
	// sizeTableImageArray = new String[] {""};
	// }
	// return sizeTableImageArray;
	// }

	// public String[] getDetailImageArray() {
	// if (StringUtils.equals(null, detailImages) || StringUtils.equals("",
	// detailImages)) {
	// return new String[0];
	// }
	// JSONArray retArray = JSON.parseArray(getDetailImages());
	// return retArray.toArray(new String[] {});
	// }

	public String[] getDetailImageArray() {
		try {

			JSONArray array = JSON.parseArray(getDetailImages());
			if (array == null) {
				return new String[] {};
			}
			return array.toArray(new String[] {});
		} catch (Exception e) {
			return null;
		}
	}
	
	public String getDisplayPicture() {
		JSONArray retArray = JSON.parseArray(getDetailImages());
		if (retArray == null) {
			return "";
		}
		String[] pics = retArray.toArray(new String[] {});

		if (pics.length < 1) {
			return "";
		}
		return pics[0];
	}

	public String getClassification() {
		return classification;
	}

	public void setClassification(String classification) {
		this.classification = classification;
	}

	public String getSeason() {
		return season;
	}

	public void setSeason(String season) {
		this.season = season;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	/**
	 * @return the promotionImage
	 */
	public String getPromotionImage() {
		return promotionImage == null ? "" : promotionImage;
	}

	public long getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(long partnerId) {
		this.partnerId = partnerId;
	}

	public int getShowStatus() {
		return showStatus;
	}

	public void setShowStatus(int showStatus) {
		this.showStatus = showStatus;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public long getPayAmountInCents() {
		return price * OrderConstants.PAY_CENTS_PER_UNIT;
	}

	public String getPromotionStartTimeDate() {
		return DateUtil.format(promotionStartTime, "yyyy-MM-dd");
	}

	public String getPromotionStartTimeHMS() {
		return DateUtil.format(promotionStartTime, "HH:mm:ss");
	}

	public String getPromotionEndTimeDate() {
		return DateUtil.format(promotionEndTime, "yyyy-MM-dd");
	}

	public String getPromotionEndTimeHMS() {
		return DateUtil.format(promotionEndTime, "HH:mm:ss");
	}

	

	// public boolean getIsOnSaling() {
	// long time = System.currentTimeMillis();
	// if(status == -1) {
	// return false;
	// }
	// if (getSaleStartTime() > 0 && getSaleEndTime() > 0) {
	// return getSaleStartTime() <= time && time <= getSaleEndTime();
	// }
	//
	// if (getSaleEndTime() <= 0) {
	// return getSaleStartTime() <= time;
	// }
	// return getSaleEndTime() >= time;
	// }

	public String getBadgeImage() {
		return badgeImage;
	}

	public void setBadgeImage(String badgeImage) {
		this.badgeImage = badgeImage;
	}

	// 这里要改！ 即将上架也为ture了，暂时先用
	public boolean getIsOnPromotion() {
		long time = System.currentTimeMillis();
		if (promotionSetting == 1 && time >= promotionStartTime
				&& (time <= promotionEndTime || promotionEndTime == 0)) {
			return true;
		}
		if (promotionSetting == 1 && time <= promotionStartTime) {
			return true;
		}

		return false;
	}

	public int getPromotionStatus() {
		if (getIsOnPromotion()) {
			return 1;
		}

		return 0;
	}

	public double getCartSttstcs() {
		return cartSttstcs;
	}

	public void setCartSttstcs(double cartSttstcs) {
		this.cartSttstcs = cartSttstcs;
	}

	public double getHotSttstcs() {
		return hotSttstcs;
	}

	public void setHotSttstcs(double hotSttstcs) {
		this.hotSttstcs = hotSttstcs;
	}

	public int getBuyType() {
		return buyType;
	}

	public void setBuyType(int buyType) {
		this.buyType = buyType;
	}

	public int getDisplayType() {
		return displayType;
	}

	public void setDisplayType(int displayType) {
		this.displayType = displayType;
	}

	public int getPromotionBuyType() {
		return promotionBuyType;
	}

	public void setPromotionBuyType(int promotionBuyType) {
		this.promotionBuyType = promotionBuyType;
	}

	public int getPromotionDisplayType() {
		return promotionDisplayType;
	}

	public void setPromotionDisplayType(int promotionDisplayType) {
		this.promotionDisplayType = promotionDisplayType;
	}

	public String getMainImage() {
		return getFirstImage();
	}

	@JsonIgnore
	public String getFirstImage() {
		if (!StringUtils.equals("", promotionImage) && !StringUtils.equals(null, promotionImage)) {
			return promotionImage;
		}
		String[] array = getDetailImageArray();
		if (array.length > 0) {
			return array[0];
		}
		String[] array2 = getSummaryImageArray();
		if (array2.length > 0) {
			return array2[0];
		}
		return null;
	}

	public String getShareTitle() {
		return shareTitle;
	}

	public void setShareTitle(String shareTitle) {
		this.shareTitle = shareTitle;
	}

	public String getShareImg() {
		return shareImg;
	}

	public void setShareImg(String shareImg) {
		this.shareImg = shareImg;
	}

	public String getShareDesc() {
		return shareDesc;
	}

	public void setShareDesc(String shareDesc) {
		this.shareDesc = shareDesc;
	}

	public double getDeductPercent() {
		return deductPercent;
	}

	public void setDeductPercent(double deductPercent) {
		this.deductPercent = deductPercent;
	}

	public int getDeductCoinNum() {
		return (int) (cash * deductPercent * 0.01);
	}

	public String getVideoName() {
		return videoName;
	}

	public void setVideoName(String videoName) {
		this.videoName = videoName;
	}

	public long getVideoFileId() {
		return videoFileId;
	}

	public void setVideoFileId(long videoFileId) {
		this.videoFileId = videoFileId;
	}

	public String getPromotionStatusName() {
		long time = System.currentTimeMillis();
		if (time < promotionStartTime) {
			return "即将开始";
		} else if (time < promotionEndTime && time >= promotionStartTime) {
			return "进行中";
		} else if (time >= promotionEndTime) {
			return "已结束";
		} else
			return "";

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
		if (time >= promotionEndTime) {
			return futureTime;
		} else {
			return promotionStartTime;
		}
	}

	public String getVideoUrl() {
		return videoUrl;
	}

	public void setVideoUrl(String videoUrl) {
		this.videoUrl = videoUrl;
	}

	public int getSetLOWarehouseId2() {
		return setLOWarehouseId2;
	}

	public void setSetLOWarehouseId2(int setLOWarehouseId2) {
		this.setLOWarehouseId2 = setLOWarehouseId2;
	}

	public long getlOWarehouseId2() {
		return lOWarehouseId2;
	}

	public void setlOWarehouseId2(long lOWarehouseId2) {
		this.lOWarehouseId2 = lOWarehouseId2;
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
			return new String[] {};
		}
		return array.toArray(new String[] {});
	}

	public String getDetailImages() {
		return detailImages;
	}

	public void setDetailImages(String detailImages) {
		this.detailImages = detailImages;
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
		if (isPromotion()) {
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
		if (this.skuOnSaleNum > 0) {
			return true;

		} else {
			return false;
		}
		// long time = System.currentTimeMillis();
		// if(status == -1) {
		// return false;
		// }
		// if (getSaleStartTime() > 0 && getSaleEndTime() > 0) {
		// return getSaleStartTime() <= time && time <= getSaleEndTime();
		// }
		//
		// if (getSaleEndTime() <= 0) {
		// return getSaleStartTime() <= time;
		// }
		// return getSaleEndTime() >= time;
	}

	/**
	 * @return the remark
	 */
	public String getRemark() {
		return remark;
	}

	/**
	 * @param remark
	 *            the remark to set
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
		if (this.getMarketPrice() > 0) {
			return 0;
		}
		return marketPriceMin;
	}

	public void setMarketPriceMin(int marketPriceMin) {
		this.marketPriceMin = marketPriceMin;
	}

	public int getMarketPriceMax() {
		if (this.getMarketPrice() > 0) {
			return 0;
		}
		return marketPriceMax;
	}

	public void setMarketPriceMax(int marketPriceMax) {
		this.marketPriceMax = marketPriceMax;
	}

	public double getCash() {
		return cash;
	}

	public void setCash(double cash) {
		this.cash = cash;
	}

	public int getJiuCoin() {
		if (isPromotion()) {
			return promotionJiuCoin;
		}
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

	public Map<String, Object> getBrand() {
		return brand;
	}

	public void setBrand(Map<String, Object> brand) {
		this.brand = brand;
	}

	public void setlOWarehouseId(long lOWarehouseId) {
		this.lOWarehouseId = lOWarehouseId;
	}

	private boolean isPromotion() {
		return getPromotionSetting() == 1 && getPromotionStartTime() < System.currentTimeMillis()
				&& getPromotionEndTime() > System.currentTimeMillis();
	}

	public boolean getIsPromotion() {
		return isPromotion();
	}

	@JsonIgnore
	public double getCurrenCash() {
		// if(isPromotion()) {
		// return promotionCash;
		// }
		return wholeSaleCash;
	}

	@JsonIgnore
	public int getCurrentJiuCoin() {
		if (isPromotion()) {
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

		map.put("image", image);
		map.put("price", getPrice());
		map.put("marketPrice", getMarketPrice());
		map.put("bottomPrice", getBottomPrice());
		map.put("marketPriceMin", getMarketPriceMin());
		map.put("marketPriceMax", getMarketPriceMax());

		map.put("onSaling", isOnSaling());
		map.put("saleTotalCount", getSaleTotalCount());
		map.put("onSaling", isOnSaling());
		map.put("weight", weight);
		map.put("promotionSaleCount", promotionSaleCount);
		map.put("promotionSaleCountStr", "销量：" + promotionSaleCount + "件");

		map.put("jiuCoin", getCurrentJiuCoin());
		map.put("subscriptLogo", subscriptLogo);
		map.put("currenCash", getCurrenCash());
		map.put("wholeSaleCash", getWholeSaleCash());
		map.put("badgeImage", getBadgeImage());
		map.put("minLadderPrice", getMinLadderPrice());//最小阶梯价格
		map.put("maxLadderPrice", getMaxLadderPrice());//最大阶梯价格
		map.put("ladderPriceJson", getLadderPriceJson());//阶梯价格JSON
		map.put("supplierId", getSupplierId());//供应商ID
		map.put("cash", getCash());
		// map.put("income", );
		map.put("loWarehouseId", lOWarehouseId);
		map.put("vedioMain", getVedioMain());
		map.put("memberLadderPriceMax", getMemberLadderPriceMax());
		map.put("memberLadderPriceMin", getMemberLadderPriceMin());
		map.put("memberLevel", getMemberLevel());
		return map;
	}

	public Map<String, Object> toSimpleMapIndex(boolean promotionImage,int buyCount) {
		Map<String, Object> map = new HashMap<String, Object>();
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
		// map.put("price", getPrice());
		map.put("marketPrice", getMarketPrice());
		map.put("bottomPrice", getBottomPrice());

		map.put("onSaling", isOnSaling());
		map.put("saleTotalCount", getSaleTotalCount());
		map.put("onSaling", isOnSaling());

		map.put("promotionSaleCount", promotionSaleCount);
		map.put("promotionSaleCountStr", "销量：" + promotionSaleCount + "件");

		map.put("subscriptLogo", subscriptLogo);
		map.put("currenCash", getCurrenCash());
		map.put("wholeSaleCash", getWholeSaleCash());
		if(buyCount != -1){
			map.put("currentLadderPriceByBuyCount", ProductNew.buildCurrentLadderPriceByBuyCount(getLadderPriceJson(),buyCount));
		}
		map.put("minLadderPrice", getMinLadderPrice());//最小阶梯价格
		map.put("maxLadderPrice", getMaxLadderPrice());//最大阶梯价格
		map.put("ladderPriceJson", getLadderPriceJson());//阶梯价格JSON
		map.put("supplierId", getSupplierId());//供应商ID
		// map.put("cash", getCash());
		// map.put("income", );

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
		return "销量：" + promotionSaleCount + "件";
	}

	public void setSubscriptLogo(String subscriptLogo) {
		this.subscriptLogo = subscriptLogo;
	}

	public List<Map<String, Object>> getSkuList() {
		return skuList;
	}

	public void setSkuList(List<Map<String, Object>> skuList) {
		this.skuList = skuList;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public double getWholeSaleCash() {
		return wholeSaleCash;
	}

	public void setWholeSaleCash(double wholeSaleCash) {
		this.wholeSaleCash = wholeSaleCash;
	}

	/**
	 * 得到概要图的第一张
	 * 
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

	public int getVip() {
		return vip;
	}

	public void setVip(int vip) {
		this.vip = vip;
	}

	public double getMaxLadderPrice() {
		return maxLadderPrice;
	}

	public void setMaxLadderPrice(double maxLadderPrice) {
		this.maxLadderPrice = maxLadderPrice;
	}

	public double getMinLadderPrice() {
		return minLadderPrice;
	}

	public void setMinLadderPrice(double minLadderPrice) {
		this.minLadderPrice = minLadderPrice;
	}

	public String getLadderPriceJson() {
		return ladderPriceJson;
	}

	public void setLadderPriceJson(String ladderPriceJson) {
		this.ladderPriceJson = ladderPriceJson;
	}

	public long getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(long supplierId) {
		this.supplierId = supplierId;
	}

	public String getMainImg() {
		return mainImg;
	}

	public void setMainImg(String mainImg) {
		this.mainImg = mainImg;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public int getDelState() {
		return delState;
	}

	public void setDelState(int delState) {
		this.delState = delState;
	}

}
//=======
//package com.jiuyuan.entity;
//
//import java.io.Serializable;
//import java.text.ParseException;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import org.apache.commons.lang3.StringUtils;
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONArray;
//import com.fasterxml.jackson.annotation.JsonIgnore;
//import com.jiuy.web.controller.util.DateUtil;
//import com.jiuyuan.constant.order.OrderConstants;
//import com.yujj.web.helper.VersionControl;
//
//@VersionControl("2.0.0.0")
//public class Product implements Serializable {
//
//	private static final long serialVersionUID = 166676858413714226L;
//
//	private String classification;
//
//	private String season;
//
//	private String brandName;
//
//	private int showStatus;
//
//	private double cartSttstcs;
//
//	private double hotSttstcs;
//
//	private int buyType;
//
//	private int displayType;
//
//	private int promotionBuyType;
//
//	private int promotionDisplayType;
//
//	private int promotionVisitCount;
//
//	private long code;// 统计识别码
//
//	private String shareTitle;
//
//	private String shareImg;
//
//	private String shareDesc;
//
//	// added by wwj 2017-04-20
//	private double deductPercent; // 玖币抵扣百分比
//
//	private String videoName;
//
//	private long videoFileId;
//
//	private long id;
//
//	private String name;
//
//	private long categoryId;
//
//	private long partnerId;
//
//	/**
//	 * 款号
//	 */
////	@JsonIgnore
//	private String productSeq;
//
//	/**
//	 * 概要图json数组
//	 */
////	@JsonIgnore
//	private String detailImages;
//
//	/**
//	 * 详情图 json数组
//	 */
////	@JsonIgnore
//	private String summaryImages;
//
//	/**
//	 * 推广头图
//	 */
//	private String promotionImage;
//
////	@JsonIgnore
//	private int status;
//
//	private long saleStartTime;
//
//	private long saleEndTime;
//
//	private int saleCurrencyType;
//
//	private int saleTotalCount;
//
//	private int saleMonthlyMaxCount;
//
//	private int price;
//
//	private long favorite;
//
//	private long assessmentCount;
//
////	@JsonIgnore
//	private int expressFree;
//
////	@JsonIgnore
//	private String expressDetails;
//
////	@JsonIgnore
//	private long createTime;
//
////	@JsonIgnore
//	private long updateTime;
//
//	/************ add by LiuWeisheng ***************/
//	private String remark;
//
//	private int marketPrice;
//
////	@JsonIgnore
//	private String sizeTableImage;
//
//	/************ add by Jeff.Zhan ******************/
//	private long brandId;
//
//	private double bottomPrice;
//
//	private int marketPriceMin;
//
//	private int marketPriceMax;
//
//	private int weight;
//
//	private double cash;
//
//	private int jiuCoin;
//
//	private int restrictHistoryBuy;
//
//	private int restrictCycle;
//
//	private int restrictDayBuy;
//
//	private int promotionSetting;
//
//	private double promotionCash;
//
//	private int promotionJiuCoin;
//
//	private long promotionStartTime;
//
//	private long promotionEndTime;
//
//	private long restrictHistoryBuyTime;
//
//	private long restrictDayBuyTime;
//
//	private long lOWarehouseId;
//
//	private long lOWarehouseId2;
//
//	private int setLOWarehouseId2;
//
//	private Map<String, Object> brand;
//	// added by Dongzhong 2016-07-09
//	private String description;
//
//	private long restrictId;
//
//	private long vCategoryId;
//
//	private long subscriptId;
//
//	private String together;
//
//	private String clothesNumber;
//
//	private int promotionSaleCount;
//
//	private int PromotionVisitCount;
//
//	private int skuOnSaleNum;
//
//	private int type;
//	/**
//	 * VIP商品 0不是vip商品 1是vip商品
//	 */
//	private int vip;
//
//	private double wholeSaleCash;
//
//	private String subscriptLogo;
//
//	private List<Map<String, Object>> skuList;
//
//	private String videoUrl;
//
//	@Override
//	public String toString() {
//		return "Product [classification=" + classification + ", season=" + season + ", brandName=" + brandName
//				+ ", showStatus=" + showStatus + ", cartSttstcs=" + cartSttstcs + ", hotSttstcs=" + hotSttstcs
//				+ ", buyType=" + buyType + ", displayType=" + displayType + ", promotionBuyType=" + promotionBuyType
//				+ ", promotionDisplayType=" + promotionDisplayType + ", promotionVisitCount=" + promotionVisitCount
//				+ ", code=" + code + ", shareTitle=" + shareTitle + ", shareImg=" + shareImg + ", shareDesc="
//				+ shareDesc + ", deductPercent=" + deductPercent + ", videoName=" + videoName + ", videoFileId="
//				+ videoFileId + ", id=" + id + ", name=" + name + ", categoryId=" + categoryId + ", partnerId="
//				+ partnerId + ", productSeq=" + productSeq + ", detailImages=" + detailImages + ", summaryImages="
//				+ summaryImages + ", promotionImage=" + promotionImage + ", status=" + status + ", saleStartTime="
//				+ saleStartTime + ", saleEndTime=" + saleEndTime + ", saleCurrencyType=" + saleCurrencyType
//				+ ", saleTotalCount=" + saleTotalCount + ", saleMonthlyMaxCount=" + saleMonthlyMaxCount + ", price="
//				+ price + ", favorite=" + favorite + ", assessmentCount=" + assessmentCount + ", expressFree="
//				+ expressFree + ", expressDetails=" + expressDetails + ", createTime=" + createTime + ", updateTime="
//				+ updateTime + ", remark=" + remark + ", marketPrice=" + marketPrice + ", sizeTableImage="
//				+ sizeTableImage + ", brandId=" + brandId + ", bottomPrice=" + bottomPrice + ", marketPriceMin="
//				+ marketPriceMin + ", marketPriceMax=" + marketPriceMax + ", weight=" + weight + ", cash=" + cash
//				+ ", jiuCoin=" + jiuCoin + ", restrictHistoryBuy=" + restrictHistoryBuy + ", restrictCycle="
//				+ restrictCycle + ", restrictDayBuy=" + restrictDayBuy + ", promotionSetting=" + promotionSetting
//				+ ", promotionCash=" + promotionCash + ", promotionJiuCoin=" + promotionJiuCoin
//				+ ", promotionStartTime=" + promotionStartTime + ", promotionEndTime=" + promotionEndTime
//				+ ", restrictHistoryBuyTime=" + restrictHistoryBuyTime + ", restrictDayBuyTime=" + restrictDayBuyTime
//				+ ", lOWarehouseId=" + lOWarehouseId + ", lOWarehouseId2=" + lOWarehouseId2 + ", setLOWarehouseId2="
//				+ setLOWarehouseId2 + ", brand=" + brand + ", description=" + description + ", restrictId=" + restrictId
//				+ ", vCategoryId=" + vCategoryId + ", subscriptId=" + subscriptId + ", together=" + together
//				+ ", clothesNumber=" + clothesNumber + ", promotionSaleCount=" + promotionSaleCount
//				+ ", PromotionVisitCount=" + PromotionVisitCount + ", skuOnSaleNum=" + skuOnSaleNum + ", type=" + type
//				+ ", vip=" + vip + ", wholeSaleCash=" + wholeSaleCash + ", subscriptLogo=" + subscriptLogo
//				+ ", skuList=" + skuList + ", videoUrl=" + videoUrl + "]";
//	}
//
//	public long getCode() {
//		return code;
//	}
//
//	public void setCode(long code) {
//		this.code = code;
//	}
//
//	public String[] getSizeTableImageArray() {
//		JSONArray array = JSON.parseArray(getSizeTableImage());
//		if (array == null) {
//			return new String[] {};
//		}
//		return array.toArray(new String[] {});
//	}
//
//	// public String[] getSizeTableImageArray() {
//	// String[] sizeTableImageArray = null;
//	// try{
//	// JSONArray retArray = JSON.parseArray(getSizeTableImage().equals("")?
//	// "":getSizeTableImage());
//	// sizeTableImageArray = retArray.toArray(new String[] {});
//	// } catch(Exception e) {
//	// sizeTableImageArray = new String[] {""};
//	// }
//	// return sizeTableImageArray;
//	// }
//
//	// public String[] getDetailImageArray() {
//	// if (StringUtils.equals(null, detailImages) || StringUtils.equals("",
//	// detailImages)) {
//	// return new String[0];
//	// }
//	// JSONArray retArray = JSON.parseArray(getDetailImages());
//	// return retArray.toArray(new String[] {});
//	// }
//
//	public String[] getDetailImageArray() {
//		try {
//
//			JSONArray array = JSON.parseArray(getDetailImages());
//			if (array == null) {
//				return new String[] {};
//			}
//			return array.toArray(new String[] {});
//		} catch (Exception e) {
//			return null;
//		}
//	}
//	
//	public String getDisplayPicture() {
//		JSONArray retArray = JSON.parseArray(getDetailImages());
//		if (retArray == null) {
//			return "";
//		}
//		String[] pics = retArray.toArray(new String[] {});
//
//		if (pics.length < 1) {
//			return "";
//		}
//		return pics[0];
//	}
//
//	public String getClassification() {
//		return classification;
//	}
//
//	public void setClassification(String classification) {
//		this.classification = classification;
//	}
//
//	public String getSeason() {
//		return season;
//	}
//
//	public void setSeason(String season) {
//		this.season = season;
//	}
//
//	public String getBrandName() {
//		return brandName;
//	}
//
//	public void setBrandName(String brandName) {
//		this.brandName = brandName;
//	}
//
//	/**
//	 * @return the promotionImage
//	 */
//	public String getPromotionImage() {
//		return promotionImage == null ? "" : promotionImage;
//	}
//
//	public long getPartnerId() {
//		return partnerId;
//	}
//
//	public void setPartnerId(long partnerId) {
//		this.partnerId = partnerId;
//	}
//
//	public int getShowStatus() {
//		return showStatus;
//	}
//
//	public void setShowStatus(int showStatus) {
//		this.showStatus = showStatus;
//	}
//
//	public static long getSerialversionuid() {
//		return serialVersionUID;
//	}
//
//	public long getPayAmountInCents() {
//		return price * OrderConstants.PAY_CENTS_PER_UNIT;
//	}
//
//	public String getPromotionStartTimeDate() {
//		return DateUtil.format(promotionStartTime, "yyyy-MM-dd");
//	}
//
//	public String getPromotionStartTimeHMS() {
//		return DateUtil.format(promotionStartTime, "HH:mm:ss");
//	}
//
//	public String getPromotionEndTimeDate() {
//		return DateUtil.format(promotionEndTime, "yyyy-MM-dd");
//	}
//
//	public String getPromotionEndTimeHMS() {
//		return DateUtil.format(promotionEndTime, "HH:mm:ss");
//	}
//
//	
//
//	// public boolean getIsOnSaling() {
//	// long time = System.currentTimeMillis();
//	// if(status == -1) {
//	// return false;
//	// }
//	// if (getSaleStartTime() > 0 && getSaleEndTime() > 0) {
//	// return getSaleStartTime() <= time && time <= getSaleEndTime();
//	// }
//	//
//	// if (getSaleEndTime() <= 0) {
//	// return getSaleStartTime() <= time;
//	// }
//	// return getSaleEndTime() >= time;
//	// }
//
//	// 这里要改！ 即将上架也为ture了，暂时先用
//	public boolean getIsOnPromotion() {
//		long time = System.currentTimeMillis();
//		if (promotionSetting == 1 && time >= promotionStartTime
//				&& (time <= promotionEndTime || promotionEndTime == 0)) {
//			return true;
//		}
//		if (promotionSetting == 1 && time <= promotionStartTime) {
//			return true;
//		}
//
//		return false;
//	}
//
//	public int getPromotionStatus() {
//		if (getIsOnPromotion()) {
//			return 1;
//		}
//
//		return 0;
//	}
//
//	public double getCartSttstcs() {
//		return cartSttstcs;
//	}
//
//	public void setCartSttstcs(double cartSttstcs) {
//		this.cartSttstcs = cartSttstcs;
//	}
//
//	public double getHotSttstcs() {
//		return hotSttstcs;
//	}
//
//	public void setHotSttstcs(double hotSttstcs) {
//		this.hotSttstcs = hotSttstcs;
//	}
//
//	public int getBuyType() {
//		return buyType;
//	}
//
//	public void setBuyType(int buyType) {
//		this.buyType = buyType;
//	}
//
//	public int getDisplayType() {
//		return displayType;
//	}
//
//	public void setDisplayType(int displayType) {
//		this.displayType = displayType;
//	}
//
//	public int getPromotionBuyType() {
//		return promotionBuyType;
//	}
//
//	public void setPromotionBuyType(int promotionBuyType) {
//		this.promotionBuyType = promotionBuyType;
//	}
//
//	public int getPromotionDisplayType() {
//		return promotionDisplayType;
//	}
//
//	public void setPromotionDisplayType(int promotionDisplayType) {
//		this.promotionDisplayType = promotionDisplayType;
//	}
//
//	public String getMainImage() {
//		return getFirstImage();
//	}
//
//	@JsonIgnore
//	public String getFirstImage() {
//		if (!StringUtils.equals("", promotionImage) && !StringUtils.equals(null, promotionImage)) {
//			return promotionImage;
//		}
//		String[] array = getDetailImageArray();
//		if (array.length > 0) {
//			return array[0];
//		}
//		String[] array2 = getSummaryImageArray();
//		if (array2.length > 0) {
//			return array2[0];
//		}
//		return null;
//	}
//
//	public String getShareTitle() {
//		return shareTitle;
//	}
//
//	public void setShareTitle(String shareTitle) {
//		this.shareTitle = shareTitle;
//	}
//
//	public String getShareImg() {
//		return shareImg;
//	}
//
//	public void setShareImg(String shareImg) {
//		this.shareImg = shareImg;
//	}
//
//	public String getShareDesc() {
//		return shareDesc;
//	}
//
//	public void setShareDesc(String shareDesc) {
//		this.shareDesc = shareDesc;
//	}
//
//	public double getDeductPercent() {
//		return deductPercent;
//	}
//
//	public void setDeductPercent(double deductPercent) {
//		this.deductPercent = deductPercent;
//	}
//
//	public int getDeductCoinNum() {
//		return (int) (cash * deductPercent * 0.01);
//	}
//
//	public String getVideoName() {
//		return videoName;
//	}
//
//	public void setVideoName(String videoName) {
//		this.videoName = videoName;
//	}
//
//	public long getVideoFileId() {
//		return videoFileId;
//	}
//
//	public void setVideoFileId(long videoFileId) {
//		this.videoFileId = videoFileId;
//	}
//
//	public String getPromotionStatusName() {
//		long time = System.currentTimeMillis();
//		if (time < promotionStartTime) {
//			return "即将开始";
//		} else if (time < promotionEndTime && time >= promotionStartTime) {
//			return "进行中";
//		} else if (time >= promotionEndTime) {
//			return "已结束";
//		} else
//			return "";
//
//	}
//
//	public long getPromotionSortTime() {
//		long time = System.currentTimeMillis();
//		long futureTime = 0;
//		try {
//			futureTime = DateUtil.convertToMSEL("2117-01-01 23:59:59");
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		if (time >= promotionEndTime) {
//			return futureTime;
//		} else {
//			return promotionStartTime;
//		}
//	}
//
//	public String getVideoUrl() {
//		return videoUrl;
//	}
//
//	public void setVideoUrl(String videoUrl) {
//		this.videoUrl = videoUrl;
//	}
//
//	public int getSetLOWarehouseId2() {
//		return setLOWarehouseId2;
//	}
//
//	public void setSetLOWarehouseId2(int setLOWarehouseId2) {
//		this.setLOWarehouseId2 = setLOWarehouseId2;
//	}
//
//	public long getlOWarehouseId2() {
//		return lOWarehouseId2;
//	}
//
//	public void setlOWarehouseId2(long lOWarehouseId2) {
//		this.lOWarehouseId2 = lOWarehouseId2;
//	}
//
//	public int getPromotionSaleCount() {
//		return promotionSaleCount;
//	}
//
//	public void setPromotionSaleCount(int promotionSaleCount) {
//		this.promotionSaleCount = promotionSaleCount;
//	}
//
//	public int getPromotionVisitCount() {
//		return PromotionVisitCount;
//	}
//
//	public void setPromotionVisitCount(int promotionVisitCount) {
//		PromotionVisitCount = promotionVisitCount;
//	}
//
//	public String getClothesNumber() {
//		return clothesNumber;
//	}
//
//	public void setClothesNumber(String clothesNumber) {
//		this.clothesNumber = clothesNumber;
//	}
//
//	public long getId() {
//		return id;
//	}
//
//	public void setId(long id) {
//		this.id = id;
//	}
//
//	public String getName() {
//		return name;
//	}
//
//	public void setName(String name) {
//		this.name = name;
//	}
//
//	public long getCategoryId() {
//		return categoryId;
//	}
//
//	public void setCategoryId(long categoryId) {
//		this.categoryId = categoryId;
//	}
//
//	public String getProductSeq() {
//		return productSeq;
//	}
//
//	public void setProductSeq(String productSeq) {
//		this.productSeq = productSeq;
//	}
//
//	public String getSummaryImages() {
//		return summaryImages;
//	}
//
//	public void setSummaryImages(String summaryImages) {
//		this.summaryImages = summaryImages;
//	}
//
//	public String[] getSummaryImageArray() {
//		JSONArray array = JSON.parseArray(getSummaryImages());
//		if (array == null) {
//			return new String[] {};
//		}
//		return array.toArray(new String[] {});
//	}
//
//	public String getDetailImages() {
//		return detailImages;
//	}
//
//	public void setDetailImages(String detailImages) {
//		this.detailImages = detailImages;
//	}
//
//	public void setPromotionImage(String promotionImage) {
//		this.promotionImage = promotionImage;
//	}
//
//	public int getStatus() {
//		return status;
//	}
//
//	public void setStatus(int status) {
//		this.status = status;
//	}
//
//	public long getSaleStartTime() {
//		return saleStartTime;
//	}
//
//	public void setSaleStartTime(long saleStartTime) {
//		this.saleStartTime = saleStartTime;
//	}
//
//	public long getSaleEndTime() {
//		return saleEndTime;
//	}
//
//	public void setSaleEndTime(long saleEndTime) {
//		this.saleEndTime = saleEndTime;
//	}
//
//	public int getSaleCurrencyType() {
//		return saleCurrencyType;
//	}
//
//	public void setSaleCurrencyType(int saleCurrencyType) {
//		this.saleCurrencyType = saleCurrencyType;
//	}
//
//	public int getSaleTotalCount() {
//		if (isPromotion()) {
//			return saleTotalCount + promotionSaleCount;
//		}
//		return saleTotalCount;
//	}
//
//	public void setSaleTotalCount(int saleTotalCount) {
//		this.saleTotalCount = saleTotalCount;
//	}
//
//	public int getSaleMonthlyMaxCount() {
//		return saleMonthlyMaxCount;
//	}
//
//	public void setSaleMonthlyMaxCount(int saleMonthlyMaxCount) {
//		this.saleMonthlyMaxCount = saleMonthlyMaxCount;
//	}
//
//	public int getPrice() {
//		return price;
//	}
//
//	public void setPrice(int price) {
//		this.price = price;
//	}
//
//	public long getFavorite() {
//		return favorite;
//	}
//
//	public void setFavorite(long favorite) {
//		this.favorite = favorite;
//	}
//
//	public long getAssessmentCount() {
//		return assessmentCount;
//	}
//
//	public void setAssessmentCount(long assessmentCount) {
//		this.assessmentCount = assessmentCount;
//	}
//
//	public int getExpressFree() {
//		return expressFree;
//	}
//
//	public void setExpressFree(int expressFree) {
//		this.expressFree = expressFree;
//	}
//
//	public String getExpressDetails() {
//		return expressDetails;
//	}
//
//	public void setExpressDetails(String expressDetails) {
//		this.expressDetails = expressDetails;
//	}
//
//	public long getCreateTime() {
//		return createTime;
//	}
//
//	public void setCreateTime(long createTime) {
//		this.createTime = createTime;
//	}
//
//	public long getUpdateTime() {
//		return updateTime;
//	}
//
//	public void setUpdateTime(long updateTime) {
//		this.updateTime = updateTime;
//	}
//
//	public int getSkuOnSaleNum() {
//		return skuOnSaleNum;
//	}
//
//	public void setSkuOnSaleNum(int skuOnSaleNum) {
//		this.skuOnSaleNum = skuOnSaleNum;
//	}
//
//	public boolean isOnSaling() {
//		if (this.skuOnSaleNum > 0) {
//			return true;
//
//		} else {
//			return false;
//		}
//		// long time = System.currentTimeMillis();
//		// if(status == -1) {
//		// return false;
//		// }
//		// if (getSaleStartTime() > 0 && getSaleEndTime() > 0) {
//		// return getSaleStartTime() <= time && time <= getSaleEndTime();
//		// }
//		//
//		// if (getSaleEndTime() <= 0) {
//		// return getSaleStartTime() <= time;
//		// }
//		// return getSaleEndTime() >= time;
//	}
//
//	/**
//	 * @return the remark
//	 */
//	public String getRemark() {
//		return remark;
//	}
//
//	/**
//	 * @param remark
//	 *            the remark to set
//	 */
//	public void setRemark(String remark) {
//		this.remark = remark;
//	}
//
//	public int getMarketPrice() {
//		return marketPrice;
//	}
//
//	public void setMarketPrice(int marketPrice) {
//		this.marketPrice = marketPrice;
//	}
//
//	public String getSizeTableImage() {
//		return sizeTableImage;
//	}
//
//	public void setSizeTableImage(String sizeTableImage) {
//		this.sizeTableImage = sizeTableImage;
//	}
//
//	public Map<String, Object> toSimpleMap() {
//		return toSimpleMap(false);
//	}
//
//	public Map<String, Object> toSimpleMap15() {
//		return toSimpleMap15(false);
//	}
//
//	public long getBrandId() {
//		return brandId;
//	}
//
//	public void setBrandId(long brandId) {
//		this.brandId = brandId;
//	}
//
//	public String getImage() {
//		String image = StringUtils.defaultString(getPromotionImage());
//
//		if (StringUtils.isBlank(image)) {
//			String[] detailImageArray = getDetailImageArray();
//			if (detailImageArray.length > 0) {
//				image = detailImageArray[0];
//			}
//		}
//
//		return image;
//	}
//
//	public Map<String, Object> toSimpleMap(boolean promotionImage) {
//		Map<String, Object> map = new HashMap<String, Object>();
//		map.put("id", getId());
//		map.put("name", getName());
//		String image = promotionImage ? StringUtils.defaultString(getPromotionImage()) : "";
//		if (StringUtils.isBlank(image)) {
//			String[] detailImageArray = getDetailImageArray();
//			if (detailImageArray.length > 0) {
//				image = detailImageArray[0];
//			}
//		}
//		map.put("image", image);
//		map.put("price", getPrice());
//		map.put("marketPrice", getMarketPrice());
//		map.put("saleTotalCount", getSaleTotalCount());
//		map.put("onSaling", isOnSaling());
//
//		return map;
//	}
//
//	public int getWeight() {
//		return weight;
//	}
//
//	public void setWeight(int weight) {
//		this.weight = weight;
//	}
//
//	public double getBottomPrice() {
//		return bottomPrice;
//	}
//
//	public void setBottomPrice(double bottomPrice) {
//		this.bottomPrice = bottomPrice;
//	}
//
//	public int getMarketPriceMin() {
//		if (this.getMarketPrice() > 0) {
//			return 0;
//		}
//		return marketPriceMin;
//	}
//
//	public void setMarketPriceMin(int marketPriceMin) {
//		this.marketPriceMin = marketPriceMin;
//	}
//
//	public int getMarketPriceMax() {
//		if (this.getMarketPrice() > 0) {
//			return 0;
//		}
//		return marketPriceMax;
//	}
//
//	public void setMarketPriceMax(int marketPriceMax) {
//		this.marketPriceMax = marketPriceMax;
//	}
//
//	public double getCash() {
//		return cash;
//	}
//
//	public void setCash(double cash) {
//		this.cash = cash;
//	}
//
//	public int getJiuCoin() {
//		if (isPromotion()) {
//			return promotionJiuCoin;
//		}
//		return jiuCoin;
//	}
//
//	public void setJiuCoin(int jiuCoin) {
//		this.jiuCoin = jiuCoin;
//	}
//
//	public int getRestrictHistoryBuy() {
//		return restrictHistoryBuy;
//	}
//
//	public void setRestrictHistoryBuy(int restrictHistoryBuy) {
//		this.restrictHistoryBuy = restrictHistoryBuy;
//	}
//
//	public int getRestrictDayBuy() {
//		return restrictDayBuy;
//	}
//
//	public void setRestrictDayBuy(int restrictDayBuy) {
//		this.restrictDayBuy = restrictDayBuy;
//	}
//
//	public int getPromotionSetting() {
//		return promotionSetting;
//	}
//
//	public void setPromotionSetting(int promotionSetting) {
//		this.promotionSetting = promotionSetting;
//	}
//
//	public double getPromotionCash() {
//		return promotionCash;
//	}
//
//	public void setPromotionCash(double promotionCash) {
//		this.promotionCash = promotionCash;
//	}
//
//	public int getPromotionJiuCoin() {
//		return promotionJiuCoin;
//	}
//
//	public void setPromotionJiuCoin(int promotionJiuCoin) {
//		this.promotionJiuCoin = promotionJiuCoin;
//	}
//
//	public long getPromotionStartTime() {
//		return promotionStartTime;
//	}
//
//	public void setPromotionStartTime(long promotionStartTime) {
//		this.promotionStartTime = promotionStartTime;
//	}
//
//	public long getPromotionEndTime() {
//		return promotionEndTime;
//	}
//
//	public void setPromotionEndTime(long promotionEndTime) {
//		this.promotionEndTime = promotionEndTime;
//	}
//
//	public int getRestrictCycle() {
//		return restrictCycle;
//	}
//
//	public void setRestrictCycle(int restrictCycle) {
//		this.restrictCycle = restrictCycle;
//	}
//
//	public long getRestrictHistoryBuyTime() {
//		return restrictHistoryBuyTime;
//	}
//
//	public void setRestrictHistoryBuyTime(long restrictHistoryBuyTime) {
//		this.restrictHistoryBuyTime = restrictHistoryBuyTime;
//	}
//
//	public long getRestrictDayBuyTime() {
//		return restrictDayBuyTime;
//	}
//
//	public void setRestrictDayBuyTime(long restrictDayBuyTime) {
//		this.restrictDayBuyTime = restrictDayBuyTime;
//	}
//
//	public long getlOWarehouseId() {
//		return lOWarehouseId;
//	}
//
//	public Map<String, Object> getBrand() {
//		return brand;
//	}
//
//	public void setBrand(Map<String, Object> brand) {
//		this.brand = brand;
//	}
//
//	public void setlOWarehouseId(long lOWarehouseId) {
//		this.lOWarehouseId = lOWarehouseId;
//	}
//
//	private boolean isPromotion() {
//		return getPromotionSetting() == 1 && getPromotionStartTime() < System.currentTimeMillis()
//				&& getPromotionEndTime() > System.currentTimeMillis();
//	}
//
//	public boolean getIsPromotion() {
//		return isPromotion();
//	}
//
//	@JsonIgnore
//	public double getCurrenCash() {
//		// if(isPromotion()) {
//		// return promotionCash;
//		// }
//		return wholeSaleCash;
//	}
//
//	@JsonIgnore
//	public int getCurrentJiuCoin() {
//		if (isPromotion()) {
//			return promotionJiuCoin;
//		}
//		return jiuCoin;
//	}
//
//	public Map<String, Object> toSimpleMap15(boolean promotionImage) {
//		Map<String, Object> map = new HashMap<String, Object>();
//		map.put("id", getId());
//		map.put("name", getName());
//		String image = promotionImage ? StringUtils.defaultString(getPromotionImage()) : "";
//		if (StringUtils.isBlank(image)) {
//			String[] detailImageArray = getDetailImageArray();
//			if (detailImageArray.length > 0) {
//				image = detailImageArray[0];
//			}
//		}
//
//		map.put("image", image);
//		map.put("price", getPrice());
//		map.put("marketPrice", getMarketPrice());
//		map.put("bottomPrice", getBottomPrice());
//		map.put("marketPriceMin", getMarketPriceMin());
//		map.put("marketPriceMax", getMarketPriceMax());
//
//		map.put("onSaling", isOnSaling());
//		map.put("saleTotalCount", getSaleTotalCount());
//		map.put("onSaling", isOnSaling());
//		map.put("weight", weight);
//		map.put("promotionSaleCount", promotionSaleCount);
//		map.put("promotionSaleCountStr", "销量：" + promotionSaleCount + "件");
//
//		map.put("jiuCoin", getCurrentJiuCoin());
//		map.put("subscriptLogo", subscriptLogo);
//		map.put("currenCash", getCurrenCash());
//		map.put("wholeSaleCash", getWholeSaleCash());
//		map.put("cash", getCash());
//		// map.put("income", );
//		map.put("loWarehouseId", lOWarehouseId);
//		return map;
//	}
//
//	public Map<String, Object> toSimpleMapIndex(boolean promotionImage) {
//		Map<String, Object> map = new HashMap<String, Object>();
//		map.put("name", getName());
//		String image = promotionImage ? StringUtils.defaultString(getPromotionImage()) : "";
//		if (StringUtils.isBlank(image)) {
//			String[] detailImageArray = getDetailImageArray();
//			if (detailImageArray.length > 0) {
//				image = detailImageArray[0];
//			}
//		}
//		map.put("id", id);
//		map.put("image", image);
//		// map.put("price", getPrice());
//		map.put("marketPrice", getMarketPrice());
//		map.put("bottomPrice", getBottomPrice());
//
//		map.put("onSaling", isOnSaling());
//		map.put("saleTotalCount", getSaleTotalCount());
//		map.put("onSaling", isOnSaling());
//
//		map.put("promotionSaleCount", promotionSaleCount);
//		map.put("promotionSaleCountStr", "销量：" + promotionSaleCount + "件");
//
//		map.put("subscriptLogo", subscriptLogo);
//		map.put("currenCash", getCurrenCash());
//		map.put("wholeSaleCash", getWholeSaleCash());
//		// map.put("cash", getCash());
//		// map.put("income", );
//
//		return map;
//	}
//
//	public String getDescription() {
//		return description;
//	}
//
//	public void setDescription(String description) {
//		this.description = description;
//	}
//
//	public long getRestrictId() {
//		return restrictId;
//	}
//
//	public void setRestrictId(long restrictId) {
//		this.restrictId = restrictId;
//	}
//
//	public long getvCategoryId() {
//		return vCategoryId;
//	}
//
//	public void setvCategoryId(long vCategoryId) {
//		this.vCategoryId = vCategoryId;
//	}
//
//	public String getTogether() {
//		return together;
//	}
//
//	public void setTogether(String together) {
//		this.together = together;
//	}
//
//	public long getSubscriptId() {
//		return subscriptId;
//	}
//
//	public void setSubscriptId(long subscriptId) {
//		this.subscriptId = subscriptId;
//	}
//
//	public String getSubscriptLogo() {
//		return subscriptLogo;
//	}
//
//	public String getPromotionSaleCountStr() {
//		return "销量：" + promotionSaleCount + "件";
//	}
//
//	public void setSubscriptLogo(String subscriptLogo) {
//		this.subscriptLogo = subscriptLogo;
//	}
//
//	public List<Map<String, Object>> getSkuList() {
//		return skuList;
//	}
//
//	public void setSkuList(List<Map<String, Object>> skuList) {
//		this.skuList = skuList;
//	}
//
//	public int getType() {
//		return type;
//	}
//
//	public void setType(int type) {
//		this.type = type;
//	}
//
//	public double getWholeSaleCash() {
//		return wholeSaleCash;
//	}
//
//	public void setWholeSaleCash(double wholeSaleCash) {
//		this.wholeSaleCash = wholeSaleCash;
//	}
//
//	/**
//	 * 得到概要图的第一张
//	 * 
//	 * @return
//	 */
//	public String getFirstDetailImage() {
//		String image = "";
//		String[] detailImageArray = getDetailImageArray();
//		if (detailImageArray.length > 0) {
//			image = detailImageArray[0];
//		}
//		return image;
//	}
//
//	public int getVip() {
//		return vip;
//	}
//
//	public void setVip(int vip) {
//		this.vip = vip;
//	}
//
//	//
//	//
//	// CREATE TABLE `yjj_Product` (
//	// `Id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '商品id',
//	// `Name` varchar(200) NOT NULL COMMENT '商品名',
//	// `CategoryId` bigint(20) NOT NULL DEFAULT '0' COMMENT '所属分类id',
//	// `DetailImages` varchar(4096) NOT NULL DEFAULT '[]' COMMENT '橱窗图片:JSON
//	// 格式数组',
//	// `SummaryImages` varchar(4096) NOT NULL DEFAULT '[]' COMMENT '详情图 json数组',
//	// `Status` tinyint(4) DEFAULT '0' COMMENT '状态:-1删除，0正常',
//	// `SaleStartTime` bigint(20) NOT NULL COMMENT '上架时间',
//	// `SaleEndTime` bigint(20) NOT NULL COMMENT '下架时间',
//	// `SaleCurrencyType` tinyint(4) NOT NULL DEFAULT '0' COMMENT '销售类型：0-人民币
//	// 1-玖币',
//	// `SaleTotalCount` int(11) DEFAULT '0' COMMENT '总销量',
//	// `SaleMonthlyMaxCount` int(11) DEFAULT '0' COMMENT '月销量最大值，每月初重新计算',
//	// `Price` int(11) NOT NULL DEFAULT '0' COMMENT
//	// '价格冗余字段，用于列表显示，添加SKU的时候更新一个最小的价格到这个字段',
//	// `Favorite` bigint(20) DEFAULT '0' COMMENT '收藏数',
//	// `AssessmentCount` bigint(20) DEFAULT '0' COMMENT '评论数',
//	// `ExpressFree` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否免邮 0-免邮 1-不免邮',
//	// `ExpressDetails` varchar(1024) DEFAULT NULL COMMENT 'JSON 格式的邮费说明',
//	// `CreateTime` bigint(20) NOT NULL COMMENT '创建时间',
//	// `UpdateTime` bigint(20) NOT NULL COMMENT '更新时间',
//	// `vip` tinyint(4) DEFAULT '0',
//	// `ProductSeq` varchar(50) DEFAULT NULL COMMENT '款号',
//	// `remark` text COMMENT '文本描述的产品信息 商品规格',
//	// `MarketPrice` decimal(10,2) DEFAULT NULL COMMENT '市场价，不是玖币价格',
//	// `SizeTableImage` varchar(1024) DEFAULT NULL,
//	// `ClothesNumber` varchar(45) DEFAULT NULL COMMENT '商品款号 和 ProductSeq
//	// 重复了.',
//	// `PromotionImage` varchar(256) DEFAULT NULL COMMENT '推广图片存储路径',
//	// `Weight` int(11) DEFAULT '0' COMMENT '商品权重',
//	// `BrandId` bigint(20) unsigned DEFAULT '0' COMMENT '商品的品牌id',
//	// `ShowStatus` int(4) NOT NULL DEFAULT '0' COMMENT '0:默认全局显示，其他:自定义显示',
//	// `BottomPrice` decimal(10,2) DEFAULT '0.00',
//	// `MarketPriceMin` int(10) DEFAULT '0',
//	// `MarketPriceMax` int(10) DEFAULT '0',
//	// `Type` tinyint(4) DEFAULT '1' COMMENT '0: 零售 1:零售/批发 2:批发',
//	// `WholeSaleCash` decimal(10,2) DEFAULT '0.00' COMMENT '批发价',
//	// `Cash` decimal(10,2) DEFAULT NULL COMMENT '消费人民币\n',
//	// `JiuCoin` int(11) DEFAULT NULL COMMENT '消费玖币',
//	// `RestrictHistoryBuy` int(11) NOT NULL DEFAULT '-1' COMMENT '历史限购 -1:不限 ',
//	// `RestrictDayBuy` int(11) NOT NULL DEFAULT '-1' COMMENT '单日限购 -1:不限 ',
//	// `PromotionSetting` tinyint(4) NOT NULL DEFAULT '0' COMMENT '价格促销设置 0-无
//	// 1-定义\n',
//	// `PromotionCash` decimal(10,2) DEFAULT NULL COMMENT '促销“消费人民币”价',
//	// `PromotionJiuCoin` int(11) DEFAULT NULL COMMENT '促销“消费玖币”价',
//	// `PromotionStartTime` bigint(20) DEFAULT NULL COMMENT '价格促销开始时间',
//	// `PromotionEndTime` bigint(20) DEFAULT NULL COMMENT '价格促销结束时间',
//	// `RestrictCycle` int(11) NOT NULL DEFAULT '0' COMMENT '限购范围（天）
//	// 默认为0，代表无限购周期，',
//	// `RestrictHistoryBuyTime` bigint(20) NOT NULL DEFAULT '0',
//	// `RestrictDayBuyTime` bigint(20) NOT NULL DEFAULT '0',
//	// `LOWarehouseId` bigint(20) DEFAULT NULL COMMENT '主仓库',
//	// `Description` text,
//	// `RestrictId` bigint(20) DEFAULT NULL COMMENT '组合限购id',
//	// `VCategoryId` bigint(20) DEFAULT NULL COMMENT '虚拟分类Id',
//	// `Together` varchar(200) DEFAULT NULL COMMENT '搭配推荐',
//	// `CartSttstcs` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '购物车推荐统计',
//	// `HotSttstcs` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '热卖推荐统计',
//	// `SubscriptId` bigint(20) DEFAULT NULL COMMENT '角标Id',
//	// `BuyType` tinyint(4) DEFAULT '0' COMMENT '购买方式，0：原价或者折扣价，1：原价，2：折扣',
//	// `DisplayType` tinyint(4) DEFAULT '0' COMMENT
//	// '橱窗价格展示，0：原价或者折扣价，1：原价，2：折扣',
//	// `PromotionBuyType` tinyint(4) DEFAULT '0' COMMENT
//	// '促销购买方式，0：原价或者折扣价，1：原价，2：折扣',
//	// `PromotionDisplayType` tinyint(4) DEFAULT '0' COMMENT
//	// '促销橱窗价格展示，0：原价或者折扣价，1：原价，2：折扣\n',
//	// `PromotionSaleCount` int(11) DEFAULT '0' COMMENT '推广销量',
//	// `PromotionVisitCount` int(11) DEFAULT '0' COMMENT '推广访问量',
//	// `LOWarehouseId2` bigint(20) NOT NULL DEFAULT '0' COMMENT '副仓库',
//	// `SetLOWarehouseId2` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0:不设置副仓
//	// 1:设置副仓',
//	// `DeductPercent` decimal(10,2) DEFAULT '0.00',
//	// `videoUrl` varchar(256) DEFAULT NULL COMMENT '商品视频url',
//	// `videoName` varchar(256) DEFAULT '' COMMENT '视频名称',
//	// `videoFileId` bigint(20) DEFAULT '0' COMMENT '商品视频fileId',
//	// PRIMARY KEY (`Id`),
//	// UNIQUE KEY `Id_UNIQUE` (`Id`)
//	// ) ENGINE=InnoDB AUTO_INCREMENT=2698 DEFAULT CHARSET=utf8;
//
//}
//>>>>>>> refs/remotes/origin/master
