package com.jiuy.product.model; 

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
 * @date 2018年06月05日 下午 04:01:25
 */
@Data
public class Product extends Model {  
 
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
	private Long categoryid;  
 
	/**
	 * 橱窗图片:JSON 格式数组
	 */  
	@FieldName(name = "橱窗图片")  
	private String detailimages;  
 
	/**
	 * 详情图 json数组
	 */  
	@FieldName(name = "详情图 json数组")  
	private String summaryimages;  
 
	/**
	 * (已经废弃不用， 参考delState)状态:-1删除，0正常
	 */  
	@FieldName(name = "(已经废弃不用， 参考delState)状态")  
	private Byte status;  
 
	/**
	 * 上架时间
	 */  
	@FieldName(name = "上架时间")  
	private Long salestarttime;  
 
	/**
	 * 下架时间
	 */  
	@FieldName(name = "下架时间")  
	private Long saleendtime;  
 
	/**
	 * 销售类型：0-人民币 1-玖币
	 */  
	@FieldName(name = "销售类型：0-人民币 1-玖币")  
	private Byte salecurrencytype;  
 
	/**
	 * 总销量
	 */  
	@FieldName(name = "总销量")  
	private Integer saletotalcount;  
 
	/**
	 * 月销量最大值，每月初重新计算
	 */  
	@FieldName(name = "月销量最大值，每月初重新计算")  
	private Integer salemonthlymaxcount;  
 
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
	private Long assessmentcount;  
 
	/**
	 * 是否免邮 0-免邮 1-不免邮
	 */  
	@FieldName(name = "是否免邮 0-免邮 1-不免邮")  
	private Byte expressfree;  
 
	/**
	 * JSON 格式的邮费说明
	 */  
	@FieldName(name = "JSON 格式的邮费说明")  
	private String expressdetails;  
 
	/**
	 * 创建时间
	 */  
	@FieldName(name = "创建时间")  
	private Long createtime;  
 
	/**
	 * 更新时间
	 */  
	@FieldName(name = "更新时间")  
	private Long updatetime;  
 
	/**
	 * 款号
	 */  
	@FieldName(name = "款号")  
	private String productseq;  
 
	/**
	 * 文本描述的产品信息   商品规格
	 */  
	@FieldName(name = "文本描述的产品信息   商品规格")  
	private String remark;  
 
	/**
	 * 市场价，不是玖币价格
	 */  
	@FieldName(name = "市场价，不是玖币价格")  
	private BigDecimal marketprice;  
 
	private String sizetableimage;  
 
	/**
	 * 商品款号 和 ProductSeq 重复了.
	 */  
	@FieldName(name = "商品款号 和 ProductSeq 重复了.")  
	private String clothesnumber;  
 
	/**
	 * 推广图片存储路径
	 */  
	@FieldName(name = "推广图片存储路径")  
	private String promotionimage;  
 
	/**
	 * 商品权重
	 */  
	@FieldName(name = "商品权重")  
	private Integer weight;  
 
	/**
	 * 商品的品牌id
	 */  
	@FieldName(name = "商品的品牌id")  
	private Long brandid;  
 
	/**
	 * 0:默认全局显示，其他:自定义显示
	 */  
	@FieldName(name = "0")  
	private Integer showstatus;  
 
	private BigDecimal bottomprice;  
 
	private Integer marketpricemin;  
 
	private Integer marketpricemax;  
 
	/**
	 * 0: 零售 1:零售/批发 2:批发
	 */  
	@FieldName(name = "0")  
	private Byte type;  
 
	/**
	 * 批发价
	 */  
	@FieldName(name = "批发价")  
	private BigDecimal wholesalecash;  
 
	/**
	 * 消费人民币

	 */  
	@FieldName(name = "消费人民币")  
	private BigDecimal cash;  
 
	/**
	 * 消费玖币
	 */  
	@FieldName(name = "消费玖币")  
	private Integer jiucoin;  
 
	/**
	 * 历史限购 -1:不限 
	 */  
	@FieldName(name = "历史限购 -1")  
	private Integer restricthistorybuy;  
 
	/**
	 * 单日限购 -1:不限 
	 */  
	@FieldName(name = "单日限购 -1")  
	private Integer restrictdaybuy;  
 
	/**
	 * 价格促销设置 0-无 1-定义

	 */  
	@FieldName(name = "价格促销设置 0-无 1-定义")  
	private Byte promotionsetting;  
 
	/**
	 * 促销“消费人民币”价
	 */  
	@FieldName(name = "促销“消费人民币”价")  
	private BigDecimal promotioncash;  
 
	/**
	 * 促销“消费玖币”价
	 */  
	@FieldName(name = "促销“消费玖币”价")  
	private Integer promotionjiucoin;  
 
	/**
	 * 价格促销开始时间
	 */  
	@FieldName(name = "价格促销开始时间")  
	private Long promotionstarttime;  
 
	/**
	 * 价格促销结束时间
	 */  
	@FieldName(name = "价格促销结束时间")  
	private Long promotionendtime;  
 
	/**
	 * 限购范围（天） 默认为0，代表无限购周期，
	 */  
	@FieldName(name = "限购范围（天） 默认为0，代表无限购周期，")  
	private Integer restrictcycle;  
 
	private Long restricthistorybuytime;  
 
	private Long restrictdaybuytime;  
 
	/**
	 * 主仓库
	 */  
	@FieldName(name = "主仓库")  
	private Long lowarehouseid;  
 
	private String description;  
 
	/**
	 * 组合限购id
	 */  
	@FieldName(name = "组合限购id")  
	private Long restrictid;  
 
	/**
	 * 虚拟分类Id
	 */  
	@FieldName(name = "虚拟分类Id")  
	private Long vcategoryid;  
 
	/**
	 * 搭配推荐
	 */  
	@FieldName(name = "搭配推荐")  
	private String together;  
 
	/**
	 * 购物车推荐统计
	 */  
	@FieldName(name = "购物车推荐统计")  
	private BigDecimal cartsttstcs;  
 
	/**
	 * 热卖推荐统计
	 */  
	@FieldName(name = "热卖推荐统计")  
	private BigDecimal hotsttstcs;  
 
	/**
	 * 角标Id
	 */  
	@FieldName(name = "角标Id")  
	private Long subscriptid;  
 
	/**
	 * 购买方式，0：原价或者折扣价，1：原价，2：折扣
	 */  
	@FieldName(name = "购买方式，0：原价或者折扣价，1：原价，2：折扣")  
	private Byte buytype;  
 
	/**
	 * 橱窗价格展示，0：原价或者折扣价，1：原价，2：折扣
	 */  
	@FieldName(name = "橱窗价格展示，0：原价或者折扣价，1：原价，2：折扣")  
	private Byte displaytype;  
 
	/**
	 * 促销购买方式，0：原价或者折扣价，1：原价，2：折扣
	 */  
	@FieldName(name = "促销购买方式，0：原价或者折扣价，1：原价，2：折扣")  
	private Byte promotionbuytype;  
 
	/**
	 * 促销橱窗价格展示，0：原价或者折扣价，1：原价，2：折扣

	 */  
	@FieldName(name = "促销橱窗价格展示，0：原价或者折扣价，1：原价，2：折扣")  
	private Byte promotiondisplaytype;  
 
	/**
	 * 推广销量
	 */  
	@FieldName(name = "推广销量")  
	private Integer promotionsalecount;  
 
	/**
	 * 推广访问量
	 */  
	@FieldName(name = "推广访问量")  
	private Integer promotionvisitcount;  
 
	/**
	 * 副仓库
	 */  
	@FieldName(name = "副仓库")  
	private Long lowarehouseid2;  
 
	/**
	 * 0:不设置副仓 1:设置副仓
	 */  
	@FieldName(name = "0")  
	private Byte setlowarehouseid2;  
 
	/**
	 * 玖币抵扣字段
	 */  
	@FieldName(name = "玖币抵扣字段")  
	private BigDecimal deductpercent;  
 
	/**
	 * 商品视频url
	 */  
	@FieldName(name = "商品视频url")  
	private String videourl;  
 
	/**
	 * 视频名称
	 */  
	@FieldName(name = "视频名称")  
	private String videoname;  
 
	/**
	 * VIP商品：0不是Vip商品，1是Vip商品
	 */  
	@FieldName(name = "VIP商品：0不是Vip商品，1是Vip商品")  
	private Byte vip;  
 
	/**
	 * 商品视频fileId
	 */  
	@FieldName(name = "商品视频fileId")  
	private Long videofileid;  
 
	/**
	 * 商品状态： 0（编辑中，未完成编辑）、 1（新建，编辑完成、待提审）、2（待提审）、3（待审核，审核中）、4（审核不通过）、 5（待上架，审核通过、待上架）、 6（上架，审核通过、已上架）、 7（下架，审核通过、已下架）
	 */  
	@FieldName(name = "商品状态： 0（编辑中，未完成编辑）、 1（新建，编辑完成、待提审）、2（待提审）、3（待审核，审核中）、4（审核不通过）、 5（待上架，审核通过、待上架）、 6（上架，审核通过、已上架）、 7（下架，审核通过、已下架）")  
	private Byte state;  
 
	/**
	 * 删除状态： 0（未删除）、 1（已删除）
	 */  
	@FieldName(name = "删除状态： 0（未删除）、 1（已删除）")  
	private Byte delstate;  
 
	/**
	 * 提交审核时间
	 */  
	@FieldName(name = "提交审核时间")  
	private Long submitaudittime;  
 
	/**
	 * 最终审核时间
	 */  
	@FieldName(name = "最终审核时间")  
	private Long audittime;  
 
	/**
	 * 审核拒绝理由
	 */  
	@FieldName(name = "审核拒绝理由")  
	private String auditnopassreason;  
 
	/**
	 * 上架时间
	 */  
	@FieldName(name = "上架时间")  
	private Long upsoldtime;  
 
	/**
	 * 上架时间
	 */  
	@FieldName(name = "上架时间")  
	private Long downsoldtime;  
 
	/**
	 * 新建时间
	 */  
	@FieldName(name = "新建时间")  
	private Long newtime;  
 
	/**
	 * SKU数量
	 */  
	@FieldName(name = "SKU数量")  
	private Integer skucount;  
 
	/**
	 * 商品主图
	 */  
	@FieldName(name = "商品主图")  
	private String mainimg;  
 
	/**
	 * 所属一级分类ID
	 */  
	@FieldName(name = "所属一级分类ID")  
	private Long onecategoryid;  
 
	/**
	 * 所属一级分类名称
	 */  
	@FieldName(name = "所属一级分类名称")  
	private String onecategoryname;  
 
	/**
	 * 所属二级分类ID
	 */  
	@FieldName(name = "所属二级分类ID")  
	private Long twocategoryid;  
 
	/**
	 * 所属二级分类名称
	 */  
	@FieldName(name = "所属二级分类名称")  
	private String twocategoryname;  
 
	/**
	 * 所属三级分类ID
	 */  
	@FieldName(name = "所属三级分类ID")  
	private Long threecategoryid;  
 
	/**
	 * 所属三级分类名称
	 */  
	@FieldName(name = "所属三级分类名称")  
	private String threecategoryname;  
 
	/**
	 * 品牌名称
	 */  
	@FieldName(name = "品牌名称")  
	private String brandname;  
 
	/**
	 * 品牌Logo
	 */  
	@FieldName(name = "品牌Logo")  
	private String brandlogo;  
 
	/**
	 * 是否需要审核： 0（不需要审核）、 1（需要审核）
	 */  
	@FieldName(name = "是否需要审核： 0（不需要审核）、 1（需要审核）")  
	private Byte needaudit;  
 
	/**
	 * 阶梯价格JSON
	 */  
	@FieldName(name = "阶梯价格JSON")  
	private String ladderpricejson;  
 
	/**
	 * 最大阶梯价格
	 */  
	@FieldName(name = "最大阶梯价格")  
	private BigDecimal maxladderprice;  
 
	/**
	 * 最小阶梯价格
	 */  
	@FieldName(name = "最小阶梯价格")  
	private BigDecimal minladderprice;  
 
	/**
	 * 供应商ID
	 */  
	@FieldName(name = "供应商ID")  
	private Long supplierid;  
 
	/**
	 * 分类ID集合,逗号分隔，如：一级分类ID,二级分类ID,三级分类ID
	 */  
	@FieldName(name = "分类ID集合,逗号分隔，如：一级分类ID,二级分类ID,三级分类ID")  
	private String categoryids;  
 
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
 
	//请不要手动拓展此类 此类为基础类不允许拓展 如果需要拓展请继承后拓展 
  
 }