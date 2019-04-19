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
 * @date 2018年06月05日 下午 04:04:18
 */
@Data
public class ProductSku extends Model {  
 
	/**
	 * id
	 */  
	@FieldName(name = "id")  
	@PrimaryKey  
	private Long id;  
 
	/**
	 * 商品id
	 */  
	@FieldName(name = "商品id")  
	private Long productid;  
 
	/**
	 * 商品SKU属性值聚合，PropertyNameId:PropertyValueId形式，多个以英文,隔开
	 */  
	@FieldName(name = "商品SKU属性值聚合，PropertyNameId")  
	private String propertyids;  
 
	/**
	 * 价格，人民币以分为单位，玖币以1为单位
	 */  
	@FieldName(name = "价格，人民币以分为单位，玖币以1为单位")  
	private Integer price;  
 
	/**
	 * 库存
	 */  
	@FieldName(name = "库存")  
	private Integer remaincount;  
 
	/**
	 * 对应SKU的图片信息
	 */  
	@FieldName(name = "对应SKU的图片信息")  
	private String specificimage;  
 
	/**
	 * 状态:-3废弃，-2停用，-1下架，0正常，1定时上架
	 */  
	@FieldName(name = "状态")  
	private Byte status;  
 
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
	 * sku编码
	 */  
	@FieldName(name = "sku编码")  
	private Long skuno;  
 
	private BigDecimal cash;  
 
	/**
	 * 重量
	 */  
	@FieldName(name = "重量")  
	private BigDecimal weight;  
 
	/**
	 * 

货品名称

	 */  
	@FieldName(name = "货品名称")  
	private String name;  
 
	/**
	 * 市场价（吊牌价）
	 */  
	@FieldName(name = "市场价（吊牌价）")  
	private BigDecimal marketprice;  
 
	/**
	 * 成本价
	 */  
	@FieldName(name = "成本价")  
	private BigDecimal costprice;  
 
	/**
	 * 'sku款号'
	 */  
	@FieldName(name = "'sku款号'")  
	private String clothesnumber;  
 
	/**
	 * 主仓库
	 */  
	@FieldName(name = "主仓库")  
	private Long lowarehouseid;  
 
	/**
	 * '库存保留时间' 天
	 */  
	@FieldName(name = "'库存保留时间' 天")  
	private Integer remainkeeptime;  
 
	/**
	 * 品牌id
	 */  
	@FieldName(name = "品牌id")  
	private Long brandid;  
 
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
 
	private Integer sort;  
 
	/**
	 * 库存锁定量
	 */  
	@FieldName(name = "库存锁定量")  
	private Integer remaincountlock;  
 
	/**
	 * 库存锁定开始时间
	 */  
	@FieldName(name = "库存锁定开始时间")  
	private Long remaincountstarttime;  
 
	/**
	 * 库存锁定结束时间
	 */  
	@FieldName(name = "库存锁定结束时间")  
	private Long remaincountendtime;  
 
	/**
	 * 是否锁库存
	 */  
	@FieldName(name = "是否锁库存")  
	private Byte isremaincountlock;  
 
	/**
	 * 推送erp时间
	 */  
	@FieldName(name = "推送erp时间")  
	private Long pushtime;  
 
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
	 * 副仓库库存
	 */  
	@FieldName(name = "副仓库库存")  
	private Integer remaincount2;  
 
	/**
	 * 副仓库
	 */  
	@FieldName(name = "副仓库")  
	private Long lowarehouseid2;  
 
	private Byte setlowarehouseid2;  
 
	/**
	 * 货架位置格式  1--2（表示1号2排）

	 */  
	@FieldName(name = "货架位置格式  1--2（表示1号2排）")  
	private String position;  
 
	/**
	 * 颜色ID
	 */  
	@FieldName(name = "颜色ID")  
	private Long colorid;  
 
	/**
	 * 颜色名称
	 */  
	@FieldName(name = "颜色名称")  
	private String colorname;  
 
	/**
	 * 尺码ID
	 */  
	@FieldName(name = "尺码ID")  
	private Long sizeid;  
 
	/**
	 * 尺码名称
	 */  
	@FieldName(name = "尺码名称")  
	private String sizename;  
 
	//请不要手动拓展此类 此类为基础类不允许拓展 如果需要拓展请继承后拓展 
  
 }