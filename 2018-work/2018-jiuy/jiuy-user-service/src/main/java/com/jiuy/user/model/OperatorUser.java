package com.jiuy.user.model; 

import com.jiuy.base.annotation.FieldName; 
import com.jiuy.base.annotation.ModelName; 
import com.jiuy.base.annotation.PrimaryKey; 
import com.jiuy.base.model.Model; 
import lombok.Data; 
import java.util.Date;

/**
 * 后台用户
 * <p> 
 * 请不要再映射对象内添加自定义属性 如果需要添加属性请继承后在子类中添加属性
 * 
 * @author Aison
 * @version V1.0  
 * @date 2018年06月11日 上午 11:45:45
 */
@Data
@ModelName(name = "后台用户")
public class OperatorUser extends Model {  
 
	/**
	 * 主键id
	 */  
	@FieldName(name = "主键id")  
	@PrimaryKey  
	private Integer id;  
 
	/**
	 * 头像
	 */  
	@FieldName(name = "头像")  
	private String avatar;  
 
	/**
	 * 账号
	 */  
	@FieldName(name = "账号")  
	private String account;  
 
	/**
	 * 密码
	 */  
	@FieldName(name = "密码")  
	private String password;  
 
	/**
	 * 名字
	 */  
	@FieldName(name = "名字")  
	private String name;  
 
	/**
	 * 生日
	 */  
	@FieldName(name = "生日")  
	private Date birthday;  
 
	/**
	 * 性别（1：男 2：女）
	 */  
	@FieldName(name = "性别（1：男 2：女）")  
	private Integer sex;  
 
	/**
	 * 电子邮件
	 */  
	@FieldName(name = "电子邮件")  
	private String email;  
 
	/**
	 * 电话
	 */  
	@FieldName(name = "电话")  
	private String phone;  
 
	/**
	 * 角色id
	 */  
	@FieldName(name = "角色id")  
	private String roleId;  
 
	/**
	 * 部门id
	 */  
	@FieldName(name = "部门id")  
	private Integer deptId;  
 
	/**
	 * 状态(1：启用  2：冻结  3：删除）
	 */  
	@FieldName(name = "状态(1：启用  2：冻结  3：删除）")  
	private Integer status;  
 
	/**
	 * 创建时间
	 */  
	@FieldName(name = "创建时间")  
	private Date createTime;  
 
	/**
	 * 保留字段
	 */  
	@FieldName(name = "保留字段")  
	private Integer version;  
 
	private Long userId;  
 
	//请不要手动拓展此类 此类为基础类不允许拓展 如果需要拓展请继承后拓展 
  
 }