package com.jiuy.rb.model.user; 

import com.jiuy.base.annotation.FieldName; 
import com.jiuy.base.annotation.ModelName; 
import com.jiuy.base.annotation.PrimaryKey; 
import com.jiuy.base.model.Model; 
import lombok.Data; 

/**
 * 供应商客户表
 * <p> 
 * 请不要再映射对象内添加自定义属性 如果需要添加属性请继承后在子类中添加属性
 * 
 * @author Aison
 * @version V1.0  
 * @date 2018年06月28日 下午 04:11:22
 */
@Data
@ModelName(name = "供应商客户表", tableName = "supplier_customer")
public class SupplierCustomerRb extends Model {  
 
	/**
	 * 主键id
	 */  
	@FieldName(name = "主键id")  
	@PrimaryKey  
	private Long id;  
 
	/**
	 * 供应商Id
	 */  
	@FieldName(name = "供应商Id")  
	private Long supplierId;  
 
	/**
	 * 门店Id
	 */  
	@FieldName(name = "门店Id")  
	private Long storeId;  
 
	/**
	 * 客户名称：店铺名称
	 */  
	@FieldName(name = "客户名称")  
	private String businessName;  
 
	/**
	 * 客户姓名：法人
	 */  
	@FieldName(name = "客户姓名")  
	private String customerName;  
 
	/**
	 * 备注名
	 */  
	@FieldName(name = "备注名")  
	private String remarkName;  
 
	/**
	 * 手机号码
	 */  
	@FieldName(name = "手机号码")  
	private String phoneNumber;  
 
	/**
	 * 分组Id 0:默认分组
	 */  
	@FieldName(name = "分组Id 0")  
	private Long groupId;  
 
	/**
	 * 所在省份
	 */  
	@FieldName(name = "所在省份")  
	private String province;  
 
	/**
	 * 所在城市
	 */  
	@FieldName(name = "所在城市")  
	private String city;  
 
	/**
	 * 客户地址
	 */  
	@FieldName(name = "客户地址")  
	private String businessAddress;  
 
	/**
	 *   状态  0：新客户   1：老客户
	 */  
	@FieldName(name = "  状态  0")  
	private Integer customerType;  
 
	/**
	 *   客户状态0正常，-1删除，1 禁用
	 */  
	@FieldName(name = "  客户状态0正常，-1删除，1 禁用")  
	private Integer status;  
 
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
 
	//请不要手动拓展此类 此类为基础类不允许拓展 如果需要拓展请继承后拓展 
  
 }