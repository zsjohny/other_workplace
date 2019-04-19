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
 * @date 2018年08月10日 下午 04:44:54
 */
@Data
@ModelName(name = "", tableName = "store_coupon")
public class StoreCoupon extends Model {  
 
	@PrimaryKey  
	private Long id;  
 
	private Long orderNo;  
 
	/**
	 * 兑换码(NULL代表未填写兑换码)
	 */  
	@FieldName(name = "兑换码(NULL代表未填写兑换码)")  
	private String code;  
 
	/**
	 * 优惠券模板id
	 */  
	@FieldName(name = "优惠券模板id")  
	private Long couponTemplateId;  
 
	/**
	 * 模板名称
	 */  
	@FieldName(name = "模板名称")  
	private String templateName;  
 
	/**
	 * 0 :指定用户的代金券,1：所有用户的代金券,3：指定注册手机号的代金券
	 */  
	@FieldName(name = "0 ")  
	private Integer type;  
 
	/**
	 * 面值
	 */  
	@FieldName(name = "面值")  
	private BigDecimal money;  
 
	/**
	 * 范围类型 0: 通用, 1:分类, 2:限额订单, 3:限定商品, 4:邮费, 5:品牌
	 */  
	@FieldName(name = "范围类型 0")  
	private Integer rangeType;  
 
	/**
	 * 范围 格式：例如{ "productIds":[2,3]}
	 */  
	@FieldName(name = "范围 格式：例如")
	private String rangeContent;  
 
	/**
	 * 有效开始时间
	 */  
	@FieldName(name = "有效开始时间")  
	private Long validityStartTime;  
 
	/**
	 * 有效结束时间 0:无限制
	 */  
	@FieldName(name = "有效结束时间 0")  
	private Long validityEndTime;  
 
	/**
	 * 优惠限制 0:无 1:有
	 */  
	@FieldName(name = "优惠限制 0")  
	private Integer isLimit;  
 
	/**
	 * 优惠券共用性 0:不可共用 1：可共用
	 */  
	@FieldName(name = "优惠券共用性 0")  
	private Integer coexist;  
 
	/**
	 * 门店ID
	 */  
	@FieldName(name = "门店ID")  
	private Long storeId;  
 
	/**
	 * 商家号
	 */  
	@FieldName(name = "商家号")  
	private Long businessNumber;  
 
	/**
	 * -2删除 -1:作废  0:未用 1:已分配 or 已使用
	 */  
	@FieldName(name = "-2删除 -1")  
	private Integer status;  
 
	private Long createTime;  
 
	private Long updateTime;  
 
	/**
	 * -1:不需要推，0：待推送，1：已推送
	 */  
	@FieldName(name = "-1")  
	private Integer pushStatus;  
 
	/**
	 * 推送标题
	 */  
	@FieldName(name = "推送标题")  
	private String pushTitle;  
 
	/**
	 * 推送描述
	 */  
	@FieldName(name = "推送描述")  
	private String pushDescription;  
 
	/**
	 * 推送url，若不填写URL，提交发放，默认为“我的代金券”界面
	 */  
	@FieldName(name = "推送url，若不填写URL，提交发放，默认为“我的代金券”界面")  
	private String pushUrl;  
 
	/**
	 * 推送图片
	 */  
	@FieldName(name = "推送图片")  
	private String pushImage;  
 
	/**
	 * 发行操作的管理员
	 */  
	@FieldName(name = "发行操作的管理员")  
	private Long publishAdminId;  
 
	/**
	 * 发放操作的管理员
	 */  
	@FieldName(name = "发放操作的管理员")  
	private Long grantAdminId;  
 
	/**
	 * 获取方式 0：发放 1：领取 2：邀请
	 */  
	@FieldName(name = "获取方式 0")  
	private Integer getWay;  
 
	/**
	 * 订单限额
	 */  
	@FieldName(name = "订单限额")  
	private BigDecimal limitMoney;  
 
	/**
	 * 范围类型ID集合.格式:,id,id,id,
	 */  
	@FieldName(name = "范围类型ID集合.格式")  
	private String rangeTypeIds;  
 
	/**
	 * 范围类型名称集合.格式:,name,name,
	 */  
	@FieldName(name = "范围类型名称集合.格式")  
	private String rangeTypeNames;  
 
	/**
	 * 发放优惠券的供应商ID
	 */  
	@FieldName(name = "发放优惠券的供应商ID")  
	private Long supplierId;  
 
	/**
	 * 领取时间
	 */  
	@FieldName(name = "领取时间")  
	private Long drawTime;  
 
	/**
	 * 领取开始时间
	 */  
	@FieldName(name = "领取开始时间")  
	private Long drawStartTime;  
 
	/**
	 * 领取结束时间
	 */  
	@FieldName(name = "领取结束时间")  
	private Long drawEndTime;  
 
	/**
	 * 发行方
	 */  
	@FieldName(name = "发行方")  
	private String publisher;  
 
	//请不要手动拓展此类 此类为基础类不允许拓展 如果需要拓展请继承后拓展 
  
 }