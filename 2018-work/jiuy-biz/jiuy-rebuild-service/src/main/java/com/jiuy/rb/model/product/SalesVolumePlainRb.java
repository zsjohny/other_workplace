package com.jiuy.rb.model.product; 

import com.jiuy.base.annotation.FieldName; 
import com.jiuy.base.annotation.ModelName; 
import com.jiuy.base.annotation.PrimaryKey; 
import com.jiuy.base.model.Model; 
import lombok.Data; 
import java.util.Date;

/**
 * 策略主表
 * <p> 
 * 请不要再映射对象内添加自定义属性 如果需要添加属性请继承后在子类中添加属性
 * 
 * @author Aison
 * @version V1.0  
 * @date 2018年06月19日 下午 02:28:25
 */
@Data
@ModelName(name = "策略主表", tableName = "yjj_sales_volume_plain")
public class SalesVolumePlainRb extends Model {  
 
	/**
	 * 主键
	 */  
	@FieldName(name = "主键")  
	@PrimaryKey  
	private Long id;  
 
	/**
	 * 策略名称
	 */  
	@FieldName(name = "策略名称")  
	private String name;  
 
	/**
	 * 策略状态:0:停止,1:进行中
	 */  
	@FieldName(name = "策略状态")  
	private Integer status;  
 
	/**
	 * 策略描述
	 */  
	@FieldName(name = "策略描述")  
	private String comment;  
 
	/**
	 * 刷量商品总数量
	 */  
	@FieldName(name = "刷量商品总数量")  
	private Long productCount;  
 
	/**
	 * 添加数量类型:0固定了,1随机量
	 */  
	@FieldName(name = "添加数量类型")  
	private Integer countType;  
 
	/**
	 * 随机最小数
	 */  
	@FieldName(name = "随机最小数")  
	private Long countRandomMini;  
 
	/**
	 * 随机最大数
	 */  
	@FieldName(name = "随机最大数")  
	private Long countRandomMax;  
 
	/**
	 * 每个商品要加多少
	 */  
	@FieldName(name = "每个商品要加多少")  
	private Long eachAddCount;  
 
	/**
	 * 今日预计总共刷多少
	 */  
	@FieldName(name = "今日预计总共刷多少")  
	private Long todayExpectAllCount;  
 
	/**
	 * 今日总共已刷了多少
	 */  
	@FieldName(name = "今日总共已刷了多少")  
	private Long todayAddedAllCount;  
 
	/**
	 * 刷量类型:0 轮询,1 仅此一次
	 */  
	@FieldName(name = "刷量类型")  
	private Integer executeType;  
 
	/**
	 * 策略开始时间
	 */  
	@FieldName(name = "策略开始时间")  
	private String executeTimeBegin;  
 
	/**
	 * 策略结束时间
	 */  
	@FieldName(name = "策略结束时间")  
	private String executeTimeEnd;  
 
	/**
	 * 执行间隔
	 */  
	@FieldName(name = "执行间隔")  
	private Integer eachTime;  
 
	/**
	 * 商品查询类型：1指定商品,2查询条件
	 */  
	@FieldName(name = "商品查询类型")  
	private Integer productQueryType;  
 
	/**
	 * 查询条件/或者ids
	 */  
	@FieldName(name = "查询条件/或者ids")  
	private String productQueryDetail;  
 
	/**
	 * 预计总刷量
	 */  
	@FieldName(name = "预计总刷量")  
	private Long expectAllCount;  
 
	/**
	 * 当前总刷量
	 */  
	@FieldName(name = "当前总刷量")  
	private Long addedAllCount;  
 
	/**
	 * 创建时间
	 */  
	@FieldName(name = "创建时间")  
	private Date createTime;  
 
	/**
	 * 修改时间
	 */  
	@FieldName(name = "修改时间")  
	private Date updateTime;  
 
	/**
	 * 初始化时间
	 */  
	@FieldName(name = "初始化时间")  
	private Date initDate;  
 
	/**
	 * 定时任务的jobName
	 */  
	@FieldName(name = "定时任务的jobName")  
	private String jobName;  
 
	//请不要手动拓展此类 此类为基础类不允许拓展 如果需要拓展请继承后拓展 
  
 }