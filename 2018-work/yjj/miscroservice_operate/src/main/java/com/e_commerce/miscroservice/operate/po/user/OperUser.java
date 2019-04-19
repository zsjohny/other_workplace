package com.e_commerce.miscroservice.operate.po.user;

import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Id;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Table;
import com.e_commerce.miscroservice.commons.entity.application.BasePage;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 管理员表
 * </p>
 *
 * @author nijin
 * @since 2017-10-14
 */
@Table("operator_user")
@Data
public class OperUser  extends BasePage implements Serializable{

	@Id
	private Integer id;
	@Column(value = "avatar", commit = "头像", length = 45)
	private String avatar;
	@Column(value = "account", commit = "账号", length = 45)
	private String account;
	@Column(value = "password", commit = "密码", length = 45)
	private String password;
	@Column(value = "salt", commit = "盐", length = 45)
	private String salt;
	@Column(value = "name", commit = "名称", length = 45)
	private String name;
	@Column(value = "birthday", commit = "生日", length = 45)
	private Date birthday;
	@Column(value = "sex", commit = "性别(1:男,2:女)", length = 45)
	private Integer sex;
	@Column(value = "email", commit = "电子邮件", length = 45)
	private String email;
	@Column(value = "phone", commit = "电话", length = 45)
	private String phone;
	@Column(value = "roleid", commit = "角色id", length = 45)
	private String roleid;
	@Column(value = "", commit = "", length = 11)
	private Integer deptid;
	@Column(value = "", commit = "", length = 11)
	private Integer status;
	@Column(value = "", commit = "", length = 11)
	private Date createtime;
	@Column(value = "", commit = "", length = 11)
	private Integer version;
	@Column(value = "", commit = "", length = 11)
	private Integer isOriginalpassword;



}
