package com.jiuy.rb.model.product; 

import com.jiuy.base.annotation.FieldName; 
import com.jiuy.base.annotation.ModelName; 
import com.jiuy.base.annotation.PrimaryKey; 
import com.jiuy.base.model.Model; 
import lombok.Data; 
import java.math.BigDecimal;

/**
 * 
 * <p> 
 * 请不要再映射对象内添加自定义属性 如果需要添加属性请继承后在子类中添加属性
 * 
 * @author Aison
 * @version V1.0  
 * @date 2018年06月13日 下午 07:36:57
 */
@Data
@ModelName(name = "", tableName = "yjj_product")
public class ProductRb extends Model {  
 
	/**
	 * 商品id
	 */  
	@FieldName(name = "商品id")  
	@PrimaryKey  
	private Long id;  
 
	/**
	 * 商品名
	 */  
	@FieldName(name = "商品名")  
	private String name;  
 
	/**
	 * 所属分类id
	 */  
	@FieldName(name = "所属分类id")  
	private Long categoryId;  
 
	/**
	 * 橱窗图片:JSON 格式数组
	 */  
	@FieldName(name = "橱窗图片")  
	private String detailImages;  
 
	/**
	 * 详情图 json数组
	 */  
	@FieldName(name = "详情图 json数组")  
	private String summaryImages;  
 
	/**
	 * (已经废弃不用， 参考delState)状态:-1删除，0正常
	 */  
	@FieldName(name = "(已经废弃不用， 参考delState)状态")  
	private Integer status;  
 
	/**
	 * 上架时间
	 */  
	@FieldName(name = "上架时间")  
	private Long saleStartTime;  
 
	/**
	 * 下架时间
	 */  
	@FieldName(name = "下架时间")  
	private Long saleEndTime;  
 
	/**
	 * 销售类型：0-人民币 1-玖币
	 */  
	@FieldName(name = "销售类型：0-人民币 1-玖币")  
	private Integer saleCurrencyType;  
 
	/**
	 * 总销量
	 */  
	@FieldName(name = "总销量")  
	private Integer saleTotalCount;  
 
	/**
	 * 月销量最大值，每月初重新计算
	 */  
	@FieldName(name = "月销量最大值，每月初重新计算")  
	private Integer saleMonthlyMaxCount;  
 
	/**
	 * 价格冗余字段，用于列表显示，添加SKU的时候更新一个最小的价格到这个字段
	 */  
	@FieldName(name = "价格冗余字段，用于列表显示，添加SKU的时候更新一个最小的价格到这个字段")  
	private Integer price;  
 
	/**
	 * 收藏数
	 */  
	@FieldName(name = "收藏数")  
	private Long favorite;  
 
	/**
	 * 评论数
	 */  
	@FieldName(name = "评论数")  
	private Long assessmentCount;  
 
	/**
	 * 是否免邮 0-免邮 1-不免邮
	 */  
	@FieldName(name = "是否免邮 0-免邮 1-不免邮")  
	private Integer expressFree;  
 
	/**
	 * JSON 格式的邮费说明
	 */  
	@FieldName(name = "JSON 格式的邮费说明")  
	private String expressDetails;  
 
	/**
	 * 创建时间
	 */  
	@FieldName(name = "创建时间")  
	private Long createTime;  
 
	/**
	 * 更新时间
	 */  
	@FieldName(name = "更新时间")  
	private Long updateTime;  
 
	/**
	 * 款号
	 */  
	@FieldName(name = "款号")  
	private String productSeq;  
 
	/**
	 * 文本描述的产品信息   商品规格
	 */  
	@FieldName(name = "文本描述的产品信息   商品规格")  
	private String remark;  
 
	/**
	 * 市场价，不是玖币价格
	 */  
	@FieldName(name = "市场价，不是玖币价格")  
	private BigDecimal marketPrice;  
 
	private String sizeTableImage;  
 
	/**
	 * 商品款号 和 ProductSeq 重复了.
	 */  
	@FieldName(name = "商品款号 和 ProductSeq 重复了.")  
	private String clothesNumber;  
 
	/**
	 * 推广图片存储路径
	 */  
	@FieldName(name = "推广图片存储路径")  
	private String promotionImage;  
 
	/**
	 * 商品权重
	 */  
	@FieldName(name = "商品权重")  
	private Integer weight;  
 
	/**
	 * 商品的品牌id
	 */  
	@FieldName(name = "商品的品牌id")  
	private Long brandId;  
 
	/**
	 * 0:默认全局显示，其他:自定义显示
	 */  
	@FieldName(name = "0")  
	private Integer showStatus;  
 
	private BigDecimal bottomPrice;  
 
	private Integer marketPriceMin;  
 
	private Integer marketPriceMax;  
 
	/**
	 * 0: 零售 1:零售/批发 2:批发
	 */  
	@FieldName(name = "0")  
	private Integer type;  
 
	/**
	 * 批发价
	 */  
	@FieldName(name = "批发价")  
	private BigDecimal wholeSaleCash;  
 
	/**
	 * 消费人民币

	 */  
	@FieldName(name = "消费人民币")  
	private BigDecimal cash;  
 
	/**
	 * 消费玖币
	 */  
	@FieldName(name = "消费玖币")  
	private Integer jiuCoin;  
 
	/**
	 * 历史限购 -1:不限 
	 */  
	@FieldName(name = "历史限购 -1")  
	private Integer restrictHistoryBuy;  
 
	/**
	 * 单日限购 -1:不限 
	 */  
	@FieldName(name = "单日限购 -1")  
	private Integer restrictDayBuy;  
 
	/**
	 * 价格促销设置 0-无 1-定义

	 */  
	@FieldName(name = "价格促销设置 0-无 1-定义")  
	private Integer promotionSetting;  
 
	/**
	 * 促销“消费人民币”价
	 */  
	@FieldName(name = "促销“消费人民币”价")  
	private BigDecimal promotionCash;  
 
	/**
	 * 促销“消费玖币”价
	 */  
	@FieldName(name = "促销“消费玖币”价")  
	private Integer promotionJiuCoin;  
 
	/**
	 * 价格促销开始时间
	 */  
	@FieldName(name = "价格促销开始时间")  
	private Long promotionStartTime;  
 
	/**
	 * 价格促销结束时间
	 */  
	@FieldName(name = "价格促销结束时间")  
	private Long promotionEndTime;  
 
	/**
	 * 限购范围（天） 默认为0，代表无限购周期，
	 */  
	@FieldName(name = "限购范围（天） 默认为0，代表无限购周期，")  
	private Integer restrictCycle;  
 
	private Long restrictHistoryBuyTime;  
 
	private Long restrictDayBuyTime;  
 
	/**
	 * 主仓库
	 */  
	@FieldName(name = "主仓库")  
	private Long lOWarehouseId;  
 
	private String description;  
 
	/**
	 * 组合限购id
	 */  
	@FieldName(name = "组合限购id")  
	private Long restrictId;  
 
	/**
	 * 虚拟分类Id
	 */  
	@FieldName(name = "虚拟分类Id")  
	private Long vCategoryId;  
 
	/**
	 * 搭配推荐
	 */  
	@FieldName(name = "搭配推荐")  
	private String together;  
 
	/**
	 * 购物车推荐统计
	 */  
	@FieldName(name = "购物车推荐统计")  
	private BigDecimal cartSttstcs;  
 
	/**
	 * 热卖推荐统计
	 */  
	@FieldName(name = "热卖推荐统计")  
	private BigDecimal hotSttstcs;  
 
	/**
	 * 角标Id
	 */  
	@FieldName(name = "角标Id")  
	private Long subscriptId;  
 
	/**
	 * 购买方式，0：原价或者折扣价，1：原价，2：折扣
	 */  
	@FieldName(name = "购买方式，0：原价或者折扣价，1：原价，2：折扣")  
	private Integer buyType;  
 
	/**
	 * 橱窗价格展示，0：原价或者折扣价，1：原价，2：折扣
	 */  
	@FieldName(name = "橱窗价格展示，0：原价或者折扣价，1：原价，2：折扣")  
	private Integer displayType;  
 
	/**
	 * 促销购买方式，0：原价或者折扣价，1：原价，2：折扣
	 */  
	@FieldName(name = "促销购买方式，0：原价或者折扣价，1：原价，2：折扣")  
	private Integer promotionBuyType;  
 
	/**
	 * 促销橱窗价格展示，0：原价或者折扣价，1：原价，2：折扣

	 */  
	@FieldName(name = "促销橱窗价格展示，0：原价或者折扣价，1：原价，2：折扣")  
	private Integer promotionDisplayType;  
 
	/**
	 * 推广销量
	 */  
	@FieldName(name = "推广销量")  
	private Integer promotionSaleCount;  
 
	/**
	 * 推广访问量
	 */  
	@FieldName(name = "推广访问量")  
	private Integer promotionVisitCount;  
 
	/**
	 * 副仓库
	 */  
	@FieldName(name = "副仓库")  
	private Long lOWarehouseId2;  
 
	/**
	 * 0:不设置副仓 1:设置副仓
	 */  
	@FieldName(name = "0")  
	private Integer setLOWarehouseId2;  
 
	/**
	 * 玖币抵扣字段
	 */  
	@FieldName(name = "玖币抵扣字段")  
	private BigDecimal deductPercent;  
 
	/**
	 * 商品视频url
	 */  
	@FieldName(name = "商品视频url")  
	private String videoUrl;  
 
	/**
	 * 视频名称
	 */  
	@FieldName(name = "视频名称")  
	private String videoName;  
 
	/**
	 * VIP商品：0不是Vip商品，1是Vip商品
	 */  
	@FieldName(name = "VIP商品：0不是Vip商品，1是Vip商品")  
	private Integer vip;  
 
	/**
	 * 商品视频fileId
	 */  
	@FieldName(name = "商品视频fileId")  
	private Long videoFileId;  
 
	/**
	 * 商品状态： 0（编辑中，未完成编辑）、 1（新建，编辑完成、待提审）、2（待提审）、3（待审核，审核中）、4（审核不通过）、 5（待上架，审核通过、待上架）、 6（上架，审核通过、已上架）、 7（下架，审核通过、已下架）
	 */  
	@FieldName(name = "商品状态： 0（编辑中，未完成编辑）、 1（新建，编辑完成、待提审）、2（待提审）、3（待审核，审核中）、4（审核不通过）、 5（待上架，审核通过、待上架）、 6（上架，审核通过、已上架）、 7（下架，审核通过、已下架）")  
	private Integer state;  
 
	/**
	 * 删除状态： 0（未删除）、 1（已删除）
	 */  
	@FieldName(name = "删除状态： 0（未删除）、 1（已删除）")  
	private Integer delState;  
 
	/**
	 * 提交审核时间
	 */  
	@FieldName(name = "提交审核时间")  
	private Long submitAuditTime;  
 
	/**
	 * 最终审核时间
	 */  
	@FieldName(name = "最终审核时间")  
	private Long auditTime;  
 
	/**
	 * 审核拒绝理由
	 */  
	@FieldName(name = "审核拒绝理由")  
	private String auditNoPassReason;  
 
	/**
	 * 上架时间
	 */  
	@FieldName(name = "上架时间")  
	private Long upSoldTime;  
 
	/**
	 * 上架时间
	 */  
	@FieldName(name = "上架时间")  
	private Long downSoldTime;  
 
	/**
	 * 新建时间
	 */  
	@FieldName(name = "新建时间")  
	private Long newTime;  
 
	/**
	 * SKU数量
	 */  
	@FieldName(name = "SKU数量")  
	private Integer skuCount;  
 
	/**
	 * 商品主图
	 */  
	@FieldName(name = "商品主图")  
	private String mainImg;  
 
	/**
	 * 所属一级分类ID
	 */  
	@FieldName(name = "所属一级分类ID")  
	private Long oneCategoryId;  
 
	/**
	 * 所属一级分类名称
	 */  
	@FieldName(name = "所属一级分类名称")  
	private String oneCategoryName;  
 
	/**
	 * 所属二级分类ID
	 */  
	@FieldName(name = "所属二级分类ID")  
	private Long twoCategoryId;  
 
	/**
	 * 所属二级分类名称
	 */  
	@FieldName(name = "所属二级分类名称")  
	private String twoCategoryName;  
 
	/**
	 * 所属三级分类ID
	 */  
	@FieldName(name = "所属三级分类ID")  
	private Long threeCategoryId;  
 
	/**
	 * 所属三级分类名称
	 */  
	@FieldName(name = "所属三级分类名称")  
	private String threeCategoryName;  
 
	/**
	 * 品牌名称
	 */  
	@FieldName(name = "品牌名称")  
	private String brandName;  
 
	/**
	 * 品牌Logo
	 */  
	@FieldName(name = "品牌Logo")  
	private String brandLogo;  
 
	/**
	 * 是否需要审核： 0（不需要审核）、 1（需要审核）
	 */  
	@FieldName(name = "是否需要审核： 0（不需要审核）、 1（需要审核）")  
	private Integer needAudit;  
 
	/**
	 * 阶梯价格JSON
	 */  
	@FieldName(name = "阶梯价格JSON")  
	private String ladderPriceJson;  
 
	/**
	 * 最大阶梯价格
	 */  
	@FieldName(name = "最大阶梯价格")  
	private BigDecimal maxLadderPrice;  
 
	/**
	 * 最小阶梯价格
	 */  
	@FieldName(name = "最小阶梯价格")  
	private BigDecimal minLadderPrice;  
 
	/**
	 * 供应商ID
	 */  
	@FieldName(name = "供应商ID")  
	private Long supplierId;  
 
	/**
	 * 分类ID集合,逗号分隔，如：一级分类ID,二级分类ID,三级分类ID
	 */  
	@FieldName(name = "分类ID集合,逗号分隔，如：一级分类ID,二级分类ID,三级分类ID")  
	private String categoryIds;  
 
	/**
	 * 角标id：0：无角标  id:有角标
	 */  
	@FieldName(name = "角标id：0：无角标  id")  
	private Long badgeId;  
 
	/**
	 * 角标名称
	 */  
	@FieldName(name = "角标名称")  
	private String badgeName;  
 
	/**
	 * 角标图片地址
	 */  
	@FieldName(name = "角标图片地址")  
	private String badgeImage;  
 
	/**
	 * 排名
	 */  
	@FieldName(name = "排名")  
	private Integer rank;  
 
	/**
	 * 品牌类型：1(高档)，2(中档)
	 */  
	@FieldName(name = "品牌类型：1(高档)，2(中档)")  
	private Integer brandType;  
 
	/**
	 * 最后上架时间
	 */  
	@FieldName(name = "最后上架时间")  
	private Long lastPutonTime;  
 
	/**
	 * 商品橱窗视频
	 */  
	@FieldName(name = "商品橱窗视频")  
	private String vedioMain;
	/**
	 * 商品橱窗视频
	 */
	@FieldName(name = "商品橱窗视频")
	private Integer memberLevel;

	/**
	 * 会员阶梯价格JSON
	 */
	@FieldName(name = "会员阶梯价格JSON")
	private String memberLadderPriceJson;

	/**
	 * 会员最大阶梯价格
	 */
	@FieldName(name = "会员最大阶梯价格")
	private BigDecimal memberLadderPriceMax;

	/**
	 * 会员最小阶梯价格
	 */
	@FieldName(name = "会员最小阶梯价格")
	private BigDecimal memberLadderPriceMin;
	//请不要手动拓展此类 此类为基础类不允许拓展 如果需要拓展请继承后拓展 
  
 }