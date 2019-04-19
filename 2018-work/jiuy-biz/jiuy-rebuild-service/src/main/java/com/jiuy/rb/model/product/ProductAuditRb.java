package com.jiuy.rb.model.product; 

import com.jiuy.base.annotation.FieldName; 
import com.jiuy.base.annotation.ModelName; 
import com.jiuy.base.annotation.PrimaryKey; 
import com.jiuy.base.model.Model; 
import lombok.Data;
import java.math.BigDecimal;

/**
 * 商品审核信息表
 * <p> 
 * 请不要再映射对象内添加自定义属性 如果需要添加属性请继承后在子类中添加属性
 * 
 * @author Aison
 * @version V1.0  
 * @date 2018年06月14日 上午 08:59:24
 */
@Data
@ModelName(name = "商品审核信息表", tableName = "yjj_productaudit")
public class ProductAuditRb extends Model {  
 
	@PrimaryKey  
	private Long id;  
 
	/**
	 * 审核状态：0客服待审核、1客服已通过、2客服已拒绝、3买手待审核、4买手已通过、5买手已拒绝
	 */  
	@FieldName(name = "审核状态")  
	private Integer auditState;  
 
	/**
	 * 审核不通过理由
	 */  
	@FieldName(name = "审核不通过理由")  
	private String noPassReason;  
 
	/**
	 * 审核时间
	 */  
	@FieldName(name = "审核时间")  
	private Long auditTime;  
 
	/**
	 * 提交审核时间
	 */  
	@FieldName(name = "提交审核时间")  
	private Long submitAuditTime;  
 
	/**
	 * 商家ID
	 */  
	@FieldName(name = "商家ID")  
	private Long supplierId;  
 
	/**
	 * 品牌ID
	 */  
	@FieldName(name = "品牌ID")  
	private Long brandId;  
 
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
	 * 商品ID
	 */  
	@FieldName(name = "商品ID")  
	private Long productId;  
 
	/**
	 * 商品名称
	 */  
	@FieldName(name = "商品名称")  
	private String productName;  
 
	/**
	 * 商品款号
	 */  
	@FieldName(name = "商品款号")  
	private String clothesNumber;  
 
	/**
	 * 橱窗图片集合
	 */  
	@FieldName(name = "橱窗图片集合")  
	private String showcaseImgs;  
 
	/**
	 * 详情图片集合
	 */  
	@FieldName(name = "详情图片集合")  
	private String detailImgs;  
 
	/**
	 * 商品视频url
	 */  
	@FieldName(name = "商品视频url")  
	private String videoUrl;  
 
	/**
	 * 商品主图
	 */  
	@FieldName(name = "商品主图")  
	private String mainImg;  
 
	/**
	 * 商品品类名称，只用于显示，格式例：裙装 -> 连衣裙
	 */  
	@FieldName(name = "商品品类名称，只用于显示，格式例")  
	private String categoryName;  
 
	/**
	 * 商品SKUJSON
	 */  
	@FieldName(name = "商品SKUJSON")  
	private String skuJSON;  
 
	/**
	 * 搭配商品ID集合，英文逗号分隔
	 */  
	@FieldName(name = "搭配商品ID集合，英文逗号分隔")  
	private String matchProductIds;  
 
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
	 * 创建时间
	 */  
	@FieldName(name = "创建时间")  
	private Long createTime;  
 
	/**
	 * 更新时间
	 */  
	@FieldName(name = "更新时间")  
	private Long updateTime;  
 
	//请不要手动拓展此类 此类为基础类不允许拓展 如果需要拓展请继承后拓展 
  
 }