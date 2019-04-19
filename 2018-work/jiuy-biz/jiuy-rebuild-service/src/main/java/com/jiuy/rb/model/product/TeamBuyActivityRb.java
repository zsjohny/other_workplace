package com.jiuy.rb.model.product; 

import com.jiuy.base.annotation.FieldName; 
import com.jiuy.base.annotation.ModelName; 
import com.jiuy.base.annotation.PrimaryKey; 
import com.jiuy.base.model.Model; 
import lombok.Data; 
import java.math.BigDecimal;

/**
 * 门店团购活动
 * <p> 
 * 请不要再映射对象内添加自定义属性 如果需要添加属性请继承后在子类中添加属性
 * 
 * @author Aison
 * @version V1.0  
 * @date 2018年07月11日 上午 11:48:50
 */
@Data
@ModelName(name = "门店团购活动", tableName = "store_team_buy_activity")
public class TeamBuyActivityRb extends Model {  
 
	/**
	 * id
	 */  
	@FieldName(name = "id")  
	@PrimaryKey  
	private Long id;  
 
	/**
	 * 活动标题
	 */  
	@FieldName(name = "活动标题")  
	private String activityTitle;  
 
	/**
	 * 商家号ID
	 */  
	@FieldName(name = "商家号ID")  
	private Long storeId;  
 
	/**
	 * 活动商品ID
	 */  
	@FieldName(name = "活动商品ID")  
	private Long shopProductId;  
 
	/**
	 * 活动商品名称
	 */  
	@FieldName(name = "活动商品名称")  
	private String shopProductName;  
 
	/**
	 * 活动商品款号
	 */  
	@FieldName(name = "活动商品款号")  
	private String clothesNumber;  
 
	/**
	 * 活动商品主图
	 */  
	@FieldName(name = "活动商品主图")  
	private String shopProductMainimg;  
 
	/**
	 * 活动商品原价格
	 */  
	@FieldName(name = "活动商品原价格")  
	private BigDecimal activityProductPrice;  
 
	/**
	 * 活动价格
	 */  
	@FieldName(name = "活动价格")  
	private BigDecimal activityPrice;  
 
	/**
	 * 成团人数
	 */  
	@FieldName(name = "成团人数")  
	private Integer userCount;  
 
	/**
	 * 活动商品数量
	 */  
	@FieldName(name = "活动商品数量")  
	private Integer activityProductCount;  
 
	/**
	 * 活动有效开始时间
	 */  
	@FieldName(name = "活动有效开始时间")  
	private Long activityStartTime;  
 
	/**
	 * 活动有效截止时间
	 */  
	@FieldName(name = "活动有效截止时间")  
	private Long activityEndTime;  
 
	/**
	 * 活动手工结束时间：0表示未手工结束
	 */  
	@FieldName(name = "活动手工结束时间")  
	private Long activityHandEndTime;  
 
	/**
	 * 参与人数
	 */  
	@FieldName(name = "参与人数")  
	private Integer activityMemberCount;  
 
	/**
	 * 删除状态：-1删除、0正常
	 */  
	@FieldName(name = "删除状态")  
	private Integer delState;  
 
	/**
	 * 创建时间
	 */  
	@FieldName(name = "创建时间")  
	private Long createTime;  
 
	/**
	 * 修改时间
	 */  
	@FieldName(name = "修改时间")  
	private Long updateTime;  
 
	/**
	 * 活动商品橱窗图
	 */  
	@FieldName(name = "活动商品橱窗图")  
	private String shopProductShowcaseImgs;

	/**
	 * 成团件数
	 */
	@FieldName(name = "成团件数")
	private Integer meetProductCount;

	/**
	 * 已下单件数
	 */
	@FieldName(name = "已下单件数")
	private Integer orderedProductCount;

	/**
	 * 成团条件类型 1:人数成团(3.7.9以前版本),2:件数成团
	 */
	@FieldName(name = "成团条件类型")
	private Integer conditionType;


    //请不要手动拓展此类 此类为基础类不允许拓展 如果需要拓展请继承后拓展 

 }