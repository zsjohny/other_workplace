package com.jiuyuan.entity.newentity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * <p>
 * 商品表
 * 
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
  `DeductPercent` decimal(10,2) DEFAULT '0.00' COMMENT '玖币抵扣字段',
  `videoUrl` varchar(256) DEFAULT NULL COMMENT '商品视频url',
  `videoName` varchar(256) DEFAULT '' COMMENT '视频名称',
  `vip` tinyint(4) DEFAULT '0' COMMENT 'VIP商品：0不是Vip商品，1是Vip商品',
  `videoFileId` bigint(20) DEFAULT '0' COMMENT '商品视频fileId',
  `state` tinyint(4) DEFAULT NULL COMMENT '商品状态： 0（编辑中，未完成编辑）、 1（新建，编辑完成、待提审）、2（待提审）、3（待审核，审核中）、4（审核不通过）、 5（待上架，审核通过、待上架）、 6（上架，审核通过、已上架）、 7（下架，审核通过、已下架）',
  `delState` tinyint(4) DEFAULT '0' COMMENT '删除状态： 0（未删除）、 1（已删除）',
  `submitAuditTime` bigint(20) DEFAULT '0' COMMENT '提交审核时间',
  `auditTime` bigint(20) DEFAULT '0' COMMENT '最终审核时间',
  `auditNoPassReason` varchar(200) DEFAULT NULL COMMENT '审核拒绝理由',
  `upSoldTime` bigint(20) DEFAULT '0' COMMENT '上架时间',
  `downSoldTime` bigint(20) DEFAULT '0' COMMENT '下架时间',
  `newTime` bigint(20) DEFAULT '0' COMMENT '新建时间',
  `skuCount` int(11) DEFAULT '0' COMMENT 'SKU数量',
  `mainImg` varchar(200) DEFAULT NULL COMMENT '商品主图',
  `oneCategoryId` bigint(20) DEFAULT '0' COMMENT '所属一级分类ID',
  `oneCategoryName` varchar(30) DEFAULT NULL COMMENT '所属一级分类名称',
  `twoCategoryId` bigint(20) DEFAULT '0' COMMENT '所属二级分类ID',
  `twoCategoryName` varchar(30) DEFAULT NULL COMMENT '所属二级分类名称',
  `threeCategoryId` bigint(20) DEFAULT '0' COMMENT '所属三级分类ID',
  `threeCategoryName` varchar(30) DEFAULT NULL COMMENT '所属三级分类名称',
  `brandName` varchar(50) DEFAULT NULL COMMENT '品牌名称',
  `brandLogo` varchar(500) DEFAULT NULL COMMENT '品牌Logo',
  `needAudit` tinyint(4) DEFAULT '0' COMMENT '是否需要审核： 0（不需要审核）、 1（需要审核）',
  `ladderPriceJson` varchar(200) DEFAULT NULL COMMENT '阶梯价格JSON',
  `maxLadderPrice` decimal(10,2) DEFAULT '0.00' COMMENT '最大阶梯价格',
  `minLadderPrice` decimal(10,2) DEFAULT '0.00' COMMENT '最小阶梯价格',
  `supplierId` bigint(20) DEFAULT '0' COMMENT '供应商ID',
  `categoryIds` varchar(30) DEFAULT NULL COMMENT '分类ID集合,逗号分隔，如：一级分类ID,二级分类ID,三级分类ID',
  `badge_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '角标id：0：无角标  id:有角标',
  `badge_name` varchar(200) DEFAULT NULL COMMENT '角标名称',
  `badge_image` varchar(4096) DEFAULT NULL COMMENT '角标图片地址',
  `rank` int(11) DEFAULT '9999' COMMENT '排名',
  `grade_type` int(11) DEFAULT '0' COMMENT '品牌档次类型：0未知、1高档、2低档',
  `brand_type` int(11) DEFAULT '0' COMMENT '品牌类型：1(高档)，2(中档)',
  PRIMARY KEY (`Id`),
  UNIQUE KEY `Id_UNIQUE` (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=7120 DEFAULT CHARSET=utf8;


 * </p>
 * @author 赵兴林
 * @since 2017-10-12
 */
@TableName("yjj_Product")
public class ProductNew extends Model<ProductNew> {

    private static final long serialVersionUID = 1L;
//    删除状态： 0（未删除）、 1（已删除）
    public static final int delState_true = 1;//已删除
    public static final int delState_false = 0;//未删除
////设置商品需要提交审核，用来判断是否出现提审按钮    
//    是否需要审核： 0（不需要审核）、 1（需要审核）
    public static final int needAudit_true = 1;//1（需要审核）
    public static final int needAudit_false = 0;//0（不需要审核）
    
    /**
     * 商品id
     */
	@TableId(value="Id", type= IdType.AUTO)
	private Long Id;
    /**
     * 商品名
     */
	private String Name;
    /**
     * 所属分类id
     */
	private Long CategoryId;
    /**
     * 橱窗图片:JSON 格式数组
     */
	private String DetailImages;
    /**
     * 详情图 json数组
     */
	private String SummaryImages;
    /**
     * 状态:-1删除，0正常
     */
	private Integer Status;
    /**
     * 上架时间
     */
	private Long SaleStartTime;
    /**
     * 下架时间
     */
	private Long SaleEndTime;
    /**
     * 销售类型：0-人民币 1-玖币
     */
	private Integer SaleCurrencyType;
    /**
     * 总销量
     */
	private Integer SaleTotalCount;
    /**
     * 月销量最大值，每月初重新计算
     */
	private Integer SaleMonthlyMaxCount;
    /**
     * 价格冗余字段，用于列表显示，添加SKU的时候更新一个最小的价格到这个字段
     */
	private Integer Price;
    /**
     * 收藏数
     */
	private Long Favorite;
    /**
     * 评论数
     */
	private Long AssessmentCount;
    /**
     * 是否免邮 0-免邮 1-不免邮
     */
	private Integer ExpressFree;
    /**
     * JSON 格式的邮费说明
     */
	private String ExpressDetails;
    /**
     * 创建时间
     */
	private Long CreateTime;
    /**
     * 更新时间
     */
	private Long UpdateTime;
	private Integer vip;
    /**
     * 款号(已废弃)
     */
	private String ProductSeq;
    /**
     * 文本描述的产品信息   商品规格
     */
	private String remark;
    /**
     * 市场价，不是玖币价格
     */
	private BigDecimal MarketPrice;
	private String SizeTableImage;
    /**
     * 商品款号 （正在使用）
     */
	private String ClothesNumber;
    /**
     * 推广图片存储路径
     */
	private String PromotionImage;
    /**
     * 商品权重
     */
	private Integer Weight;
    /**
     * 商品的品牌id
     */
	private Long BrandId;
    /**
     * 0:默认全局显示，其他:自定义显示
     */
	private Integer ShowStatus;
	private Double BottomPrice;
	private Integer MarketPriceMin;
	private Integer MarketPriceMax;
    /**
     * 0: 零售 1:零售/批发 2:批发
     */
	private Integer Type;
    /**
     * 批发价（该字段已经废弃，已经被阶梯价代替）
     */
	private Double WholeSaleCash;
    /**
     * 消费人民币

     */
	private Double Cash;
    /**
     * 消费玖币
     */
	private Integer JiuCoin;
    /**
     * 历史限购 -1:不限 
     */
	private Integer RestrictHistoryBuy;
    /**
     * 单日限购 -1:不限 
     */
	private Integer RestrictDayBuy;
    /**
     * 价格促销设置 0-无 1-定义

     */
	private Integer PromotionSetting;
    /**
     * 促销“消费人民币”价
     */
	private Double PromotionCash;
    /**
     * 促销“消费玖币”价
     */
	private Integer PromotionJiuCoin;
    /**
     * 价格促销开始时间
     */
	private Long PromotionStartTime;
    /**
     * 价格促销结束时间
     */
	private Long PromotionEndTime;
    /**
     * 限购范围（天） 默认为0，代表无限购周期，
     */
	private Integer RestrictCycle;
	private Long RestrictHistoryBuyTime;
	private Long RestrictDayBuyTime;
    /**
     * 主仓库
     */
	private Long LOWarehouseId;
	private String Description;
    /**
     * 组合限购id
     */
	private Long RestrictId;
    /**
     * 虚拟分类Id
     */
	private Long VCategoryId;
    /**
     * 搭配推荐
     */
	private String Together;
    /**
     * 购物车推荐统计
     */
	private BigDecimal CartSttstcs;
    /**
     * 热卖推荐统计
     */
	private BigDecimal HotSttstcs;
    /**
     * 
     */
	private Long SubscriptId;
    /**
     * 购买方式，0：原价或者折扣价，1：原价，2：折扣
     */
	private Integer BuyType;
    /**
     * 橱窗价格展示，0：原价或者折扣价，1：原价，2：折扣
     */
	private Integer DisplayType;
    /**
     * 促销购买方式，0：原价或者折扣价，1：原价，2：折扣
     */
	private Integer PromotionBuyType;
    /**
     * 促销橱窗价格展示，0：原价或者折扣价，1：原价，2：折扣

     */
	private Integer PromotionDisplayType;
    /**
     * 推广销量
     */
	private Integer PromotionSaleCount;
    /**
     * 推广访问量
     */
	private Integer PromotionVisitCount;
    /**
     * 副仓库
     */
	private Long LOWarehouseId2;
    /**
     * 0:不设置副仓 1:设置副仓
     */
	private Integer SetLOWarehouseId2;
	
	
	private Double DeductPercent;
    /**
     * 商品视频url
     */
	private String videoUrl;
    /**
     * 视频名称
     */
	private String videoName;
    /**
     * 商品视频fileId
     */
	private Long videoFileId;
    /**
     * 阶梯价格JSON
     */
	private String ladderPriceJson;
    /**
     * 最大阶梯价格
     */
	private Double maxLadderPrice;
    /**
     * 供应商ID
     */
	private Long supplierId;
    /**
     * 最小阶梯价格
     */
	private Double minLadderPrice;
	
	 /**
     * 商品状态： 0（编辑中，未完成编辑）、 1（新建，编辑完成、待提审）、2（待提审）、3（待审核，审核中）、4（审核不通过）、 5（待上架，审核通过、待上架）、 6（上架，审核通过、已上架）、 7（下架，审核通过、已下架）
     */
	private Integer state;
	 /**
//     * 是否需要审核： 0（不需要审核）、 1（需要审核）
//     */
//	private Integer needAudit;
	 /**
     * 删除状态： 0（未删除）、 1（已删除）
     */
	private Integer delState;
	/**
     * 提交审核时间
     */
	private Long submitAuditTime;
	/**
     * 审核时间
     */
	private Long auditTime;
	/**
     * 审核拒绝理由
     */
	private String auditNoPassReason;
	/**
     * 上架时间
     */
	private Long upSoldTime;
	/**
     * 下架时间
     */
	private Long downSoldTime;
	/**
     * 新建时间
     */
	private Long newTime;
	/**
     * SKU数量
     */
	private Integer skuCount;
	/**
     * 商品主图
     */
	private String mainImg;
	
	/**
     * 分类ID集合,逗号分隔，如：一级分类ID,二级分类ID,三级分类ID
     */
	private String categoryIds;
	/**
     * 所属一级分类ID
     */
	private Long oneCategoryId;
	/**
     * 所属一级分类名称
     */
	private  String oneCategoryName;
	/**
     * 所属二级分类ID
     */
	private Long twoCategoryId ;
	/**
     * 所属二级分类名称
     */
	private String twoCategoryName ;
	/**
     * 所属三级分类ID
     */
	private Long threeCategoryId;
	/**
     * 所属三级分类名称
     */
	private String threeCategoryName ;
	/**
     * 品牌名称
     */
	private String brandName ;
	/**
     * 品牌Logo
     */
	private String brandLogo;

	/**
	 * 角标id
	 */
	@TableField("badge_id")
	private Long badgeId;
	/**
	 * 角标名称
	 */
	@TableField("badge_name")
	private String badgeName;
	/**
	 * 角标URL
	 */
	@TableField("badge_image")
	private String badgeImage;
	/**
	 * 排名
	 */
	@TableField("rank")
	private Integer rank;
	
	/**
	 * 品牌类型：1(高档)，2(中档)
	 */
	@TableField("brand_type")
	private Integer brandType;

	/**
	 * @see: 商品的橱窗视频地址
	 */
	@TableField("vedio_main")
	private String vedioMain;

	/**
	 * @see: 最后上架时间
	 */
	@TableField("last_puton_time")
	private Long lastPutonTime;


	/**
	 * 会员等级 默认（普通）：0,一级会员商品：1 ...类推
	 */
	@TableField("memberLevel")
	private Integer memberLevel;


	/**
	 * 会员阶梯价格JSON
	 */
	@TableField("member_ladder_price_json")
	private String memberLadderPriceJson;


	/**
	 * 会员最大阶梯价格
	 */
	@TableField("member_ladder_price_max")
	private BigDecimal memberLadderPriceMax;


	/**
	 * 会员最小阶梯价格
	 */
	@TableField("member_ladder_price_min")
	private BigDecimal memberLadderPriceMin;


	public String getMemberLadderPriceJson() {
		return memberLadderPriceJson;
	}

	public void setMemberLadderPriceJson(String memberLadderPriceJson) {
		this.memberLadderPriceJson = memberLadderPriceJson;
	}

	public BigDecimal getMemberLadderPriceMax() {
		return memberLadderPriceMax;
	}

	public void setMemberLadderPriceMax(BigDecimal memberLadderPriceMax) {
		this.memberLadderPriceMax = memberLadderPriceMax;
	}

	public BigDecimal getMemberLadderPriceMin() {
		return memberLadderPriceMin;
	}

	public void setMemberLadderPriceMin(BigDecimal memberLadderPriceMin) {
		this.memberLadderPriceMin = memberLadderPriceMin;
	}

	public Integer getMemberLevel() {
		return memberLevel;
	}

	public void setMemberLevel(Integer memberLevel) {
		this.memberLevel = memberLevel;
	}

	public Long getLastPutonTime() {
		return lastPutonTime;
	}

	public void setLastPutonTime(Long lastPutonTime) {
		this.lastPutonTime = lastPutonTime;
	}

	public String getVedioMain() {
		return vedioMain;
	}

	public void setVedioMain(String vedioMain) {
		this.vedioMain = vedioMain;
	}

	public Integer getBrandType() {
		return brandType;
	}

	public void setBrandType(Integer brandType) {
		this.brandType = brandType;
	}

	public String getCategoryIds() {
		return categoryIds;
	}

	public void setCategoryIds(String categoryIds) {
		this.categoryIds = categoryIds;
	}

	public Long getId() {
		return Id;
	}

	public void setId(Long Id) {
		this.Id = Id;
	}

	public String getName() {
		return Name;
	}

	public void setName(String Name) {
		this.Name = Name;
	}

	public Long getCategoryId() {
		return CategoryId;
	}

	public void setCategoryId(Long CategoryId) {
		this.CategoryId = CategoryId;
	}

	public String getDetailImages() {
		return DetailImages;
	}

	public void setDetailImages(String DetailImages) {
		this.DetailImages = DetailImages;
	}

	public String getSummaryImages() {
		return SummaryImages;
	}

	public void setSummaryImages(String SummaryImages) {
		this.SummaryImages = SummaryImages;
	}

	public Integer getStatus() {
		return Status;
	}

	public void setStatus(Integer Status) {
		this.Status = Status;
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

	public Integer getSaleCurrencyType() {
		return SaleCurrencyType;
	}

	public void setSaleCurrencyType(Integer SaleCurrencyType) {
		this.SaleCurrencyType = SaleCurrencyType;
	}

	public Integer getSaleTotalCount() {
		return SaleTotalCount;
	}

	public void setSaleTotalCount(Integer SaleTotalCount) {
		this.SaleTotalCount = SaleTotalCount;
	}

	public Integer getSaleMonthlyMaxCount() {
		return SaleMonthlyMaxCount;
	}

	public void setSaleMonthlyMaxCount(Integer SaleMonthlyMaxCount) {
		this.SaleMonthlyMaxCount = SaleMonthlyMaxCount;
	}

	public Integer getPrice() {
		return Price;
	}

	public void setPrice(Integer Price) {
		this.Price = Price;
	}

	public Long getFavorite() {
		return Favorite;
	}

	public void setFavorite(Long Favorite) {
		this.Favorite = Favorite;
	}

	public Long getAssessmentCount() {
		return AssessmentCount;
	}

	public void setAssessmentCount(Long AssessmentCount) {
		this.AssessmentCount = AssessmentCount;
	}

	public Integer getExpressFree() {
		return ExpressFree;
	}

	public void setExpressFree(Integer ExpressFree) {
		this.ExpressFree = ExpressFree;
	}

	public String getExpressDetails() {
		return ExpressDetails;
	}

	public void setExpressDetails(String ExpressDetails) {
		this.ExpressDetails = ExpressDetails;
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

	public Integer getVip() {
		return vip;
	}

	public void setVip(Integer vip) {
		this.vip = vip;
	}

	public String getProductSeq() {
		return ProductSeq;
	}

	public void setProductSeq(String ProductSeq) {
		this.ProductSeq = ProductSeq;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public BigDecimal getMarketPrice() {
		return MarketPrice;
	}

	public void setMarketPrice(BigDecimal MarketPrice) {
		this.MarketPrice = MarketPrice;
	}

	public String getSizeTableImage() {
		return SizeTableImage;
	}

	public void setSizeTableImage(String SizeTableImage) {
		this.SizeTableImage = SizeTableImage;
	}

	public String getClothesNumber() {
		return ClothesNumber;
	}

	public void setClothesNumber(String ClothesNumber) {
		this.ClothesNumber = ClothesNumber;
	}

	public String getPromotionImage() {
		return PromotionImage;
	}

	public void setPromotionImage(String PromotionImage) {
		this.PromotionImage = PromotionImage;
	}

	public Integer getWeight() {
		return Weight;
	}

	public void setWeight(Integer Weight) {
		this.Weight = Weight;
	}

	public Long getBrandId() {
		return BrandId;
	}

	public void setBrandId(Long BrandId) {
		this.BrandId = BrandId;
	}

	public Integer getShowStatus() {
		return ShowStatus;
	}

	public void setShowStatus(Integer ShowStatus) {
		this.ShowStatus = ShowStatus;
	}

	

	public Integer getMarketPriceMin() {
		return MarketPriceMin;
	}

	public void setMarketPriceMin(Integer MarketPriceMin) {
		this.MarketPriceMin = MarketPriceMin;
	}

	public Integer getMarketPriceMax() {
		return MarketPriceMax;
	}

	public void setMarketPriceMax(Integer MarketPriceMax) {
		this.MarketPriceMax = MarketPriceMax;
	}

	public Integer getType() {
		return Type;
	}

	public void setType(Integer Type) {
		this.Type = Type;
	}



	public Integer getJiuCoin() {
		return JiuCoin;
	}

	public void setJiuCoin(Integer JiuCoin) {
		this.JiuCoin = JiuCoin;
	}

	public Integer getRestrictHistoryBuy() {
		return RestrictHistoryBuy;
	}

	public void setRestrictHistoryBuy(Integer RestrictHistoryBuy) {
		this.RestrictHistoryBuy = RestrictHistoryBuy;
	}

	public Integer getRestrictDayBuy() {
		return RestrictDayBuy;
	}

	public void setRestrictDayBuy(Integer RestrictDayBuy) {
		this.RestrictDayBuy = RestrictDayBuy;
	}

	public Integer getPromotionSetting() {
		return PromotionSetting;
	}

	public void setPromotionSetting(Integer PromotionSetting) {
		this.PromotionSetting = PromotionSetting;
	}

	

	public Integer getPromotionJiuCoin() {
		return PromotionJiuCoin;
	}

	public void setPromotionJiuCoin(Integer PromotionJiuCoin) {
		this.PromotionJiuCoin = PromotionJiuCoin;
	}

	public Long getPromotionStartTime() {
		return PromotionStartTime;
	}

	public void setPromotionStartTime(Long PromotionStartTime) {
		this.PromotionStartTime = PromotionStartTime;
	}

	public Long getPromotionEndTime() {
		return PromotionEndTime;
	}

	public void setPromotionEndTime(Long PromotionEndTime) {
		this.PromotionEndTime = PromotionEndTime;
	}

	public Integer getRestrictCycle() {
		return RestrictCycle;
	}

	public void setRestrictCycle(Integer RestrictCycle) {
		this.RestrictCycle = RestrictCycle;
	}

	public Long getRestrictHistoryBuyTime() {
		return RestrictHistoryBuyTime;
	}

	public void setRestrictHistoryBuyTime(Long RestrictHistoryBuyTime) {
		this.RestrictHistoryBuyTime = RestrictHistoryBuyTime;
	}

	public Long getRestrictDayBuyTime() {
		return RestrictDayBuyTime;
	}

	public void setRestrictDayBuyTime(Long RestrictDayBuyTime) {
		this.RestrictDayBuyTime = RestrictDayBuyTime;
	}

	public Long getLOWarehouseId() {
		return LOWarehouseId;
	}

	public void setLOWarehouseId(Long LOWarehouseId) {
		this.LOWarehouseId = LOWarehouseId;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String Description) {
		this.Description = Description;
	}

	public Long getRestrictId() {
		return RestrictId;
	}

	public void setRestrictId(Long RestrictId) {
		this.RestrictId = RestrictId;
	}

	public Long getVCategoryId() {
		return VCategoryId;
	}

	public void setVCategoryId(Long VCategoryId) {
		this.VCategoryId = VCategoryId;
	}

	public String getTogether() {
		return Together;
	}

	public void setTogether(String Together) {
		this.Together = Together;
	}

	public BigDecimal getCartSttstcs() {
		return CartSttstcs;
	}

	public void setCartSttstcs(BigDecimal CartSttstcs) {
		this.CartSttstcs = CartSttstcs;
	}

	public BigDecimal getHotSttstcs() {
		return HotSttstcs;
	}

	public void setHotSttstcs(BigDecimal HotSttstcs) {
		this.HotSttstcs = HotSttstcs;
	}

	public Long getSubscriptId() {
		return SubscriptId;
	}

	public void setSubscriptId(Long SubscriptId) {
		this.SubscriptId = SubscriptId;
	}

	public Integer getBuyType() {
		return BuyType;
	}

	public void setBuyType(Integer BuyType) {
		this.BuyType = BuyType;
	}

	public Integer getDisplayType() {
		return DisplayType;
	}

	public void setDisplayType(Integer DisplayType) {
		this.DisplayType = DisplayType;
	}

	public Integer getPromotionBuyType() {
		return PromotionBuyType;
	}

	public void setPromotionBuyType(Integer PromotionBuyType) {
		this.PromotionBuyType = PromotionBuyType;
	}

	public Integer getPromotionDisplayType() {
		return PromotionDisplayType;
	}

	public void setPromotionDisplayType(Integer PromotionDisplayType) {
		this.PromotionDisplayType = PromotionDisplayType;
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

	
	public String getVideoUrl() {
		return videoUrl;
	}

	public void setVideoUrl(String videoUrl) {
		this.videoUrl = videoUrl;
	}

	public String getVideoName() {
		return videoName;
	}

	public void setVideoName(String videoName) {
		this.videoName = videoName;
	}

	public Long getVideoFileId() {
		return videoFileId;
	}

	public void setVideoFileId(Long videoFileId) {
		this.videoFileId = videoFileId;
	}

	public String getLadderPriceJson() {
		return ladderPriceJson;
	}

	public void setLadderPriceJson(String ladderPriceJson) {
		this.ladderPriceJson = ladderPriceJson;
	}






	@Override
	protected Serializable pkVal() {
		return this.Id;
	}
	/**
	 * 转换商品款号编辑值
	 * 将款号中去除看好前缀
	 * @param clothesNumberPrefix
	 * @return
	 */
	  public  String buildClothesNumberEditValue(String clothesNumberPrefix){
		  String clothesNumberEditValue = ""; 
	//    String a = this.ClothesNumber.toLowerCase();
	//    String pre = clothesNumberPrefix.toLowerCase();
	    if(this.ClothesNumber.startsWith(clothesNumberPrefix)){////已款号前缀开头，则截取款号前缀
	    	clothesNumberEditValue = this.ClothesNumber.substring(clothesNumberPrefix.length());
	    }else{
	    	clothesNumberEditValue = this.ClothesNumber;
	    }
	    return clothesNumberEditValue;
	  }
	/**
	 * 商品品类名称，只用于显示，格式例：裙装 -> 连衣裙
	 * @return
	 */
	public String getCategoryNames() {
		String categoryNames = "";
		if(StringUtils.isEmpty(oneCategoryName)){
			categoryNames = "无";
		}else if(StringUtils.isEmpty(twoCategoryName)){
			categoryNames = oneCategoryName;
			return categoryNames;
		}else if(StringUtils.isEmpty(threeCategoryName)){
			categoryNames = oneCategoryName + ">" + twoCategoryName;
			return categoryNames;
		}else{
			categoryNames = oneCategoryName + ">" + twoCategoryName + ">" +threeCategoryName;
		}
		return categoryNames;
	}
	/**
	 * 获得主图
	 * 
	 * @return
	 */
	@JsonIgnore
	public String getFirstImage() {
		if (!StringUtils.equals("", getPromotionImage()) && !StringUtils.equals(null, getPromotionImage())) {
			return getPromotionImage();
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
	@JsonIgnore
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
	@JsonIgnore
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
	@JsonIgnore
	public String[] getSizeTableImageArray() {
		JSONArray array = JSON.parseArray(getSizeTableImage());
		if (array == null) {
			return new String[] {};
		}
		return array.toArray(new String[] {});
	}
	@JsonIgnore
	public String[] getSummaryImageArray() {
		JSONArray array = JSON.parseArray(getSummaryImages());
		if (array == null) {
			return new String[] {};
		}
		return array.toArray(new String[] {});
	}

	public Integer getDelState() {
		return delState;
	}

	public void setDelState(Integer delState) {
		this.delState = delState;
	}

	public Long getSubmitAuditTime() {
		return submitAuditTime;
	}

	public void setSubmitAuditTime(Long submitAuditTime) {
		this.submitAuditTime = submitAuditTime;
	}

	public Long getAuditTime() {
		return auditTime;
	}

	public void setAuditTime(Long auditTime) {
		this.auditTime = auditTime;
	}

	public String getAuditNoPassReason() {
		return auditNoPassReason;
	}

	public void setAuditNoPassReason(String auditNoPassReason) {
		this.auditNoPassReason = auditNoPassReason;
	}

	public Long getUpSoldTime() {
		return upSoldTime;
	}

	public void setUpSoldTime(Long upSoldTime) {
		this.upSoldTime = upSoldTime;
	}

	public Long getDownSoldTime() {
		return downSoldTime;
	}

	public void setDownSoldTime(Long downSoldTime) {
		this.downSoldTime = downSoldTime;
	}

	public Long getNewTime() {
		return newTime;
	}

	public void setNewTime(Long newTime) {
		this.newTime = newTime;
	}

	public Integer getSkuCount() {
		return skuCount;
	}

	public void setSkuCount(Integer skuCount) {
		this.skuCount = skuCount;
	}

	public String getMainImg() {
		return mainImg;
	}

	public void setMainImg(String mainImg) {
		this.mainImg = mainImg;
	}

	public void setMaxLadderPrice(Double maxLadderPrice) {
		this.maxLadderPrice = maxLadderPrice;
	}

	public void setMinLadderPrice(Double minLadderPrice) {
		this.minLadderPrice = minLadderPrice;
	}

	public Long getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Long supplierId) {
		this.supplierId = supplierId;
	}

	public Double getBottomPrice() {
		return BottomPrice;
	}

	public void setBottomPrice(Double bottomPrice) {
		BottomPrice = bottomPrice;
	}

	public Double getWholeSaleCash() {
		return WholeSaleCash;
	}

	public void setWholeSaleCash(Double wholeSaleCash) {
		WholeSaleCash = wholeSaleCash;
	}

	public Double getCash() {
		return Cash;
	}

	public void setCash(Double cash) {
		Cash = cash;
	}

	public Double getPromotionCash() {
		return PromotionCash;
	}

	public void setPromotionCash(Double promotionCash) {
		PromotionCash = promotionCash;
	}

	public Double getDeductPercent() {
		return DeductPercent;
	}

	public void setDeductPercent(Double deductPercent) {
		DeductPercent = deductPercent;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Long getOneCategoryId() {
		return oneCategoryId;
	}

	public void setOneCategoryId(Long oneCategoryId) {
		this.oneCategoryId = oneCategoryId;
	}

	public String getOneCategoryName() {
		return oneCategoryName;
	}

	public void setOneCategoryName(String oneCategoryName) {
		this.oneCategoryName = oneCategoryName;
	}

	public Long getTwoCategoryId() {
		return twoCategoryId;
	}

	public void setTwoCategoryId(Long twoCategoryId) {
		this.twoCategoryId = twoCategoryId;
	}

	public String getTwoCategoryName() {
		return twoCategoryName;
	}

	public void setTwoCategoryName(String twoCategoryName) {
		this.twoCategoryName = twoCategoryName;
	}

	public Long getThreeCategoryId() {
		return threeCategoryId;
	}

	public void setThreeCategoryId(Long threeCategoryId) {
		this.threeCategoryId = threeCategoryId;
	}

	public String getThreeCategoryName() {
		return threeCategoryName;
	}

	public void setThreeCategoryName(String threeCategoryName) {
		this.threeCategoryName = threeCategoryName;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getBrandLogo() {
		return brandLogo;
	}

	public void setBrandLogo(String brandLogo) {
		this.brandLogo = brandLogo;
	}

	public Double getMaxLadderPrice() {
		return maxLadderPrice;
	}

	public Double getMinLadderPrice() {
		return minLadderPrice;
	}

	public Long getBadgeId() {
		return badgeId;
	}

	public void setBadgeId(Long badgeId) {
		this.badgeId = badgeId;
	}

	public String getBadgeName() {
		return badgeName;
	}

	public void setBadgeName(String badgeName) {
		this.badgeName = badgeName;
	}

	public String getBadgeImage() {
		return badgeImage;
	}

	public void setBadgeImage(String badgeImage) {
		this.badgeImage = badgeImage;
	}

	public Integer getRank() {
		return rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}

//	public Integer getNeedAudit() {
//		return needAudit;
//	}
//
//	public void setNeedAudit(Integer needAudit) {
//		this.needAudit = needAudit;
//	}
	/**
	 * 根据阶梯价JSON解析出最大价格
	 * @param
	 * @return
	 */
	public static Double buildMaxLadderPrice(String ladderPriceJson) {
		double maxLadderPrice = 0;
		if(StringUtils.isNotEmpty(ladderPriceJson)){
			JSONArray ladderPriceArr = JSONArray.parseArray(ladderPriceJson);
			if(ladderPriceArr.size() > 0 ){
				JSONObject ladderPrice = ladderPriceArr.getJSONObject(0);
				String maxLadderPriceStr =ladderPrice.getString("price");
				maxLadderPrice = Double.parseDouble(maxLadderPriceStr);
			}
		}
		return maxLadderPrice;
	}
	
	/**
	 * 根据阶梯价JSON解析出最小价格
	 * @param
	 * @return
	 */
	public static Double buildMinLadderPrice(String ladderPriceJson) {
		double minLadderPrice = 0;
		if(StringUtils.isNotEmpty(ladderPriceJson)){
			JSONArray ladderPriceArr = JSONArray.parseArray(ladderPriceJson);
			if(ladderPriceArr.size() > 0 ){
				JSONObject ladderPrice = ladderPriceArr.getJSONObject(ladderPriceArr.size()-1);
				String minLadderPriceStr =ladderPrice.getString("price");
				minLadderPrice = Double.parseDouble(minLadderPriceStr);
			}
		}
		return minLadderPrice;
	}
//	/**
//	 * 根据购买数量获取对应阶梯价中的订单单价
//	 * @param buyCount
//	 * @return
//	 */
//	public double getUnitPriceByBuyCount(int buyCount) {
//		double unitPrice = buildCurrentLadderPriceByBuyCount(ladderPriceJson ,buyCount);//订单商品单价
//		return unitPrice;
//	}


	/**
	 * 获取阶梯价格中, 最小起批量
	 * @param ladderPriceJson
	 * @return int 最小值为1, 默认1
	 * @auther Charlie(唐静)
	 * @date 2018/5/28 16:13
	 */
	public static int getMinLadderCount(String ladderPriceJson) {
	    /* 默认1 */
		int defaultMin = 1;
		Map<Integer, Double> ladderPriceMap = buildLadderPriceMap(ladderPriceJson);
		if (null == ladderPriceMap || ladderPriceMap.isEmpty()) {
			return defaultMin;
		}


		/* 有阶梯价时, 返回最小起批值 */
		Set<Integer> ladderSet = ladderPriceMap.keySet();
		int i = 0;
		int temp = 1;
		for (int ladder : ladderSet) {
			if (i++ == 0) {
				temp = ladder;
			} else {
				if (temp > ladder) {
					temp = ladder;
				}
			}
		}
		return temp < defaultMin ? defaultMin : temp;
	}



	/**
	 * 将阶梯价JSON解析成Map,key(购买数量)、value(价格)
	 * @param
	 * @return
	 */
	public static Map<Integer, Double> buildLadderPriceMap(String ladderPriceJson) {
		Map<Integer, Double> ladderPriceMap = new HashMap<Integer, Double>();
		JSONArray ladderPriceJsonJSONArray = JSON.parseArray(ladderPriceJson);  
		for(Object ladderPrice : ladderPriceJsonJSONArray){
			JSONObject ladderPriceJSONObject = (JSONObject)ladderPrice;
			int count = Integer.parseInt(ladderPriceJSONObject.get("count").toString());
			double price = ladderPriceJSONObject.getDoubleValue("price");
			ladderPriceMap.put(count, price);
		}
		return ladderPriceMap;
	}

	/**
	 * 根据购买数量获取对应阶梯价格
	 * @param buyCount
	 * @return 阶梯价格单价
	 */
	public static double buildCurrentLadderPriceByBuyCount(String ladderPriceJson,int buyCount){
		JSONArray ladderPriceJsonJSONArray = JSON.parseArray(ladderPriceJson);  
		
//		for(Object ladderPrice : ladderPriceJsonJSONArray){
//			JSONObject ladderPriceJSONObject = (JSONObject)ladderPrice;
//			System.out.println(ladderPriceJSONObject);
//			System.out.println(ladderPriceJSONObject.get("count"));
//			System.out.println(ladderPriceJSONObject.get("price"));
//		}
		
		//阶梯价数量为1    数量之前已经做了校验
		if(ladderPriceJsonJSONArray.size() == 1){
			Object ladderPrice1 = ladderPriceJsonJSONArray.get(0);
			JSONObject ladderPriceJSONObject1 = (JSONObject)ladderPrice1;
			double price = ladderPriceJSONObject1.getDoubleValue("price");
//			logger.info("计算阶梯价格：price:"+price+",buyCount:"+buyCount+",ladderPriceJson"+ladderPriceJson);
			return price;
		}
		//阶梯价数量为2
		if(ladderPriceJsonJSONArray.size() == 2){
			Object ladderPrice2 = ladderPriceJsonJSONArray.get(1);
			JSONObject ladderPriceJSONObject2 = (JSONObject)ladderPrice2;
			int count2 = Integer.parseInt(ladderPriceJSONObject2.get("count").toString()) ;
			if(buyCount >= count2){
				double price = ladderPriceJSONObject2.getDoubleValue("price");
//				logger.info("计算阶梯价格：price:"+price+",buyCount:"+buyCount+",ladderPriceJson"+ladderPriceJson);
				return  price;
			}else{
				Object ladderPrice1 = ladderPriceJsonJSONArray.get(0);
				JSONObject ladderPriceJSONObject1 = (JSONObject)ladderPrice1;
				double price = ladderPriceJSONObject1.getDoubleValue("price");
//				logger.info("计算阶梯价格：price:"+price+",buyCount:"+buyCount+",ladderPriceJson"+ladderPriceJson);
				return  price;
			}
		}
		//阶梯价数量为3
		if(ladderPriceJsonJSONArray.size() == 3){
			Object ladderPrice3 = ladderPriceJsonJSONArray.get(2);
			JSONObject ladderPriceJSONObject3 = (JSONObject)ladderPrice3;
			int count3 = Integer.parseInt(ladderPriceJSONObject3.get("count").toString());

			if(buyCount >= count3){
				return ladderPriceJSONObject3.getDoubleValue("price");
			}else {
				Object ladderPrice2 = ladderPriceJsonJSONArray.get(1);
				JSONObject ladderPriceJSONObject2 = (JSONObject)ladderPrice2;
				int count2 = Integer.parseInt(ladderPriceJSONObject2.get("count").toString());
				if(buyCount >= count2){
					double price = ladderPriceJSONObject2.getDoubleValue("price");
//					logger.info("计算阶梯价格：price:"+price+",buyCount:"+buyCount+",ladderPriceJson"+ladderPriceJson);
					return  price;
				}else{
					Object ladderPrice1 = ladderPriceJsonJSONArray.get(0);
					JSONObject ladderPriceJSONObject1 = (JSONObject)ladderPrice1;
					double price = ladderPriceJSONObject1.getDoubleValue("price");
//					logger.info("计算阶梯价格：price:"+price+",buyCount:"+buyCount+",ladderPriceJson"+ladderPriceJson);
					return  price;
				}
			}
		}
//		logger.info("获取商品阶梯价格出现错误，请尽快排查问题！！！！！！！！！！！！！！！！！！！！！！！！！！！！");
		return 0;
	}

	public static void main(String[] args) {
		String ladderPriceJson = "[{count:1,price:14.49}]";
		int buyCount = 1;
		JSONArray ladderPriceJsonJSONArray = JSON.parseArray(ladderPriceJson);  
		
		for(Object ladderPrice : ladderPriceJsonJSONArray){
			JSONObject ladderPriceJSONObject = (JSONObject)ladderPrice;
			System.out.println(ladderPriceJSONObject);
			System.out.println(ladderPriceJSONObject.get("count"));
			System.out.println(ladderPriceJSONObject.get("price"));
		}
	}

	
}
