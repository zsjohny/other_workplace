package com.jiuy.rb.model.product; 

import com.jiuy.base.annotation.FieldName; 
import com.jiuy.base.annotation.ModelName; 
import com.jiuy.base.annotation.PrimaryKey; 
import com.jiuy.base.model.Model; 
import lombok.Data; 
import java.math.BigDecimal;

/**
 * 商品分类表
 * <p> 
 * 请不要再映射对象内添加自定义属性 如果需要添加属性请继承后在子类中添加属性
 * 
 * @author Aison
 * @version V1.0  
 * @date 2018年06月14日 下午 03:54:04
 */
@Data
@ModelName(name = "商品分类表", tableName = "yjj_category")
public class CategoryRb extends Model {  
 
	/**
	 * 分类id
	 */  
	@FieldName(name = "分类id")  
	@PrimaryKey  
	private Long id;  
 
	/**
	 * 分类名称
	 */  
	@FieldName(name = "分类名称")  
	private String categoryName;  
 
	/**
	 * 分类父id，0表示顶级分类
	 */  
	@FieldName(name = "分类父id，0表示顶级分类")  
	private Long parentId;  
 
	/**
	 * 状态:-1删除，0正常，1隐藏
	 */  
	@FieldName(name = "状态")  
	private Integer status;  
 
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
	 * 分类类型：0产品分类，1商家分类，2：虚拟分类
	 */  
	@FieldName(name = "分类类型")  
	private Integer categoryType;  
 
	private String description;  
 
	/**
	 * 分类权重,相同的parentId情况下,根据权重来排序
	 */  
	@FieldName(name = "分类权重,相同的parentId情况下,根据权重来排序")  
	private Integer weight;  
 
	/**
	 * 分类图标
	 */  
	@FieldName(name = "分类图标")  
	private String iconUrl;  
 
	/**
	 * 分类选中图标
	 */  
	@FieldName(name = "分类选中图标")  
	private String iconOnUrl;  
 
	/**
	 * 分类首页URL
	 */  
	@FieldName(name = "分类首页URL")  
	private String categoryUrl;  
 
	/**
	 * 是否优惠 0:否 1：是
	 */  
	@FieldName(name = "是否优惠 0")  
	private Integer isDiscount;  
 
	/**
	 * 满额立减条件
	 */  
	@FieldName(name = "满额立减条件")  
	private BigDecimal exceedMoney;  
 
	/**
	 * 满额立减数
	 */  
	@FieldName(name = "满额立减数")  
	private BigDecimal minusMoney;  
 
	/**
	 * 分类等级:0:未知;1:一级;2:二级;3:三级;
	 */  
	@FieldName(name = "分类等级")  
	private Integer categoryLevel;  
 
	/**
	 * 类目编码
	 */  
	@FieldName(name = "类目编码")  
	private String code;  
 
	//请不要手动拓展此类 此类为基础类不允许拓展 如果需要拓展请继承后拓展 
  
 }