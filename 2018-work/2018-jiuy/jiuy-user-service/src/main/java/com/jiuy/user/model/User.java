package com.jiuy.user.model; 

import com.jiuy.base.annotation.FieldName; 
import com.jiuy.base.annotation.ModelName; 
import com.jiuy.base.annotation.PrimaryKey; 
import com.jiuy.base.model.Model; 
import lombok.Data; 
import java.util.Date;

/**
 * 用户表
 * <p> 
 * 请不要再映射对象内添加自定义属性 如果需要添加属性请继承后在子类中添加属性
 * 
 * @author Aison
 * @version V1.0  
 * @date 2018年06月08日 下午 10:44:48
 */
@Data
@ModelName(name = "用户表")
public class User extends Model {  
 
	/**
	 * 主键
	 */  
	@FieldName(name = "主键")  
	@PrimaryKey  
	private Long id;  
 
	/**
	 * 用户姓名
	 */  
	@FieldName(name = "用户姓名")  
	private String userName;  
 
	/**
	 * 登陆名称
	 */  
	@FieldName(name = "登陆名称")  
	private String loginName;  
 
	/**
	 * 手机号码
	 */  
	@FieldName(name = "手机号码")  
	private String phoneNumber;  
 
	/**
	 * 密码密文
	 */  
	@FieldName(name = "密码密文")  
	private String pwd;  
 
	/**
	 * 用户类型
	 */  
	@FieldName(name = "用户类型")  
	private Integer userType;  
 
	/**
	 * 昵称
	 */  
	@FieldName(name = "昵称")  
	private String nikeName;  
 
	/**
	 * 邮箱
	 */  
	@FieldName(name = "邮箱")  
	private String email;  
 
	/**
	 * 创建时间
	 */  
	@FieldName(name = "创建时间")  
	private Date createTime;  
 
	/**
	 * 状态:1启用,0禁用
	 */  
	@FieldName(name = "状态")  
	private Integer status;  
 
	/**
	 * 性别:1：男 2：女
	 */  
	@FieldName(name = "性别")  
	private Integer sex;  
 
	/**
	 * 生日
	 */  
	@FieldName(name = "生日")  
	private String birthday;  
 
	//请不要手动拓展此类 此类为基础类不允许拓展 如果需要拓展请继承后拓展 
  
 }