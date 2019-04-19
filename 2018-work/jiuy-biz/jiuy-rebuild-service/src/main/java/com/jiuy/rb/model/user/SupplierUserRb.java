package com.jiuy.rb.model.user; 

import com.jiuy.base.annotation.FieldName; 
import com.jiuy.base.annotation.ModelName; 
import com.jiuy.base.annotation.PrimaryKey; 
import com.jiuy.base.model.Model; 
import lombok.Data; 
import java.math.BigDecimal;
import java.util.Date;

/**
 * 管理员表
 * <p> 
 * 请不要再映射对象内添加自定义属性 如果需要添加属性请继承后在子类中添加属性
 * 
 * @author Aison
 * @version V1.0  
 * @date 2018年06月29日 上午 11:32:16
 */
@Data
@ModelName(name = "管理员表", tableName = "supplier_user")
public class SupplierUserRb extends Model {  
 
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
	 * md5密码盐
	 */  
	@FieldName(name = "md5密码盐")  
	private String salt;  
 
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
	@FieldName(name = "性别（1")  
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
	private String roleid;  
 
	/**
	 * 部门id
	 */  
	@FieldName(name = "部门id")  
	private Integer deptid;  
 
	/**
	 * 状态(1：启用  2：冻结  3：删除）
	 */  
	@FieldName(name = "状态(1")  
	private Integer status;  
 
	/**
	 * 创建时间
	 */  
	@FieldName(name = "创建时间")  
	private Date createtime;
 
	/**
	 * 保留字段
	 */  
	@FieldName(name = "保留字段")  
	private Integer version;  
 
	/**
	 * 支付宝关联人姓名
	 */  
	@FieldName(name = "支付宝关联人姓名")  
	private String alipayName;  
 
	/**
	 * 商家名称
	 */  
	@FieldName(name = "商家名称")  
	private String businessName;  
 
	/**
	 * 公司名称
	 */  
	@FieldName(name = "公司名称")  
	private String companyName;  
 
	/**
	 * 商家地址
	 */  
	@FieldName(name = "商家地址")  
	private String businessAddress;  
 
	/**
	 * 营业执照号或者统一社会信用代码
	 */  
	@FieldName(name = "营业执照号或者统一社会信用代码")  
	private String licenseNumber;  
 
	/**
	 * 税务登记号
	 */  
	@FieldName(name = "税务登记号")  
	private String taxid;  
 
	/**
	 * 法定代表人
	 */  
	@FieldName(name = "法定代表人")  
	private String legalPerson;  
 
	/**
	 * 身份证号码
	 */  
	@FieldName(name = "身份证号码")  
	private String idCardNumber;  
 
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
	 * 银行卡选择标记 0 未选中， 1选中
	 */  
	@FieldName(name = "银行卡选择标记 0 未选中， 1选中")  
	private Integer bankCardFlag;  
 
	/**
	 * 银行开户名称
	 */  
	@FieldName(name = "银行开户名称")  
	private String bankAccountName;  
 
	/**
	 * 开户银行名称
	 */  
	@FieldName(name = "开户银行名称")  
	private String bankName;  
 
	/**
	 * 银行开户号
	 */  
	@FieldName(name = "银行开户号")  
	private String bankAccountNo;  
 
	/**
	 * 支付宝选择标记 0 未选中， 1选中
	 */  
	@FieldName(name = "支付宝选择标记 0 未选中， 1选中")  
	private Integer alipayFlag;  
 
	/**
	 * 支付宝账号
	 */  
	@FieldName(name = "支付宝账号")  
	private String alipayAccount;  
 
	/**
	 * 品牌仓库Id
	 */  
	@FieldName(name = "品牌仓库Id")  
	private Long lowarehouseId;  
 
	/**
	 * 关联品牌Id
	 */  
	@FieldName(name = "关联品牌Id")  
	private Long brandId;  
 
	/**
	 * 更新时间
	 */  
	@FieldName(name = "更新时间")  
	private Long updatetime;  
 
	/**
	 * 分销门店数

	 */  
	@FieldName(name = "分销门店数")  
	private Integer storeNumber;  
 
	/**
	 * 账户资金
	 */  
	@FieldName(name = "账户资金")  
	private BigDecimal cashIncome;  
 
	/**
	 * 可提现余额
	 */  
	@FieldName(name = "可提现余额")  
	private BigDecimal availableBalance;  
 
	/**
	 * 组织机构号
	 */  
	@FieldName(name = "组织机构号")  
	private String organizationNo;  
 
	/**
	 * 品牌描述
	 */  
	@FieldName(name = "品牌描述")  
	private String brandDescription;  
 
	/**
	 * 品牌LOGO

	 */  
	@FieldName(name = "品牌LOGO")  
	private String brandLogo;  
 
	/**
	 * 品牌展示图片
	 */  
	@FieldName(name = "品牌展示图片")  
	private String brandShowImgs;  
 
	/**
	 * 提成百分比
	 */  
	@FieldName(name = "提成百分比")  
	private BigDecimal commissionPercentage;  
 
	/**
	 * 结算日期
	 */  
	@FieldName(name = "结算日期")  
	private Integer settlementDate;  
 
	/**
	 * 微信选择标记 0 未选中， 1选中
	 */  
	@FieldName(name = "微信选择标记 0 未选中， 1选中")  
	private Integer weixinFlag;  
 
	/**
	 * 微信账号
	 */  
	@FieldName(name = "微信账号")  
	private String weixinAccount;  
 
	/**
	 * 微信关联姓名
	 */  
	@FieldName(name = "微信关联姓名")  
	private String weixinName;  
 
	/**
	 * 提款设置 0 所有费用合并提款， 1不同款项分开取款 
	 */  
	@FieldName(name = "提款设置 0 所有费用合并提款， 1不同款项分开取款 ")  
	private Integer withdrawSetting;  
 
	/**
	 * 物流余额
	 */  
	@FieldName(name = "物流余额")  
	private BigDecimal expressCashIncome;  
 
	/**
	 * 可提现物流余额
	 */  
	@FieldName(name = "可提现物流余额")  
	private BigDecimal availableExpressCash;  
 
	/**
	 * 最低体现额
	 */  
	@FieldName(name = "最低体现额")  
	private BigDecimal minWithdrawal;  
 
	/**
	 * 保证金
	 */  
	@FieldName(name = "保证金")  
	private BigDecimal bond;  
 
	/**
	 * 商家号
	 */  
	@FieldName(name = "商家号")  
	private Long businessNumber;  
 
	/**
	 * 物流银行开户名称
	 */  
	@FieldName(name = "物流银行开户名称")  
	private String expressBankAccountName;  
 
	/**
	 * 物流开户银行名称
	 */  
	@FieldName(name = "物流开户银行名称")  
	private String expressBankName;  
 
	/**
	 * 物流银行账户号
	 */  
	@FieldName(name = "物流银行账户号")  
	private String expressBankAccountNo;  
 
	/**
	 * 创建时间,长整型
	 */  
	@FieldName(name = "创建时间,长整型")  
	private Long addCreatetime;  
 
	/**
	 * 是否使用初始化密码,0：不是，1：是
	 */  
	@FieldName(name = "是否使用初始化密码,0")  
	private Integer isOriginalpassword;  
 
	/**
	 * 供应商收货人
	 */  
	@FieldName(name = "供应商收货人")  
	private String receiver;  
 
	/**
	 * 供应商收货地址
	 */  
	@FieldName(name = "供应商收货地址")  
	private String supplierReceiveAddress;  
 
	/**
	 * 收货人电话
	 */  
	@FieldName(name = "收货人电话")  
	private String receiverPhone;  
 
	/**
	 * 商品权限 0：公开  -1：不公开  大于等于1：分组id
	 */  
	@FieldName(name = "商品权限 0")  
	private String productPermission;  
 
	/**
	 * 批发起定额
	 */  
	@FieldName(name = "批发起定额")  
	private BigDecimal wholesaleCost;  
 
	/**
	 * 批发起定量
	 */  
	@FieldName(name = "批发起定量")  
	private Integer wholesaleCount;  
 
	/**
	 * 商品宣传图
	 */  
	@FieldName(name = "商品宣传图")  
	private String campaignImage;  
 
	//请不要手动拓展此类 此类为基础类不允许拓展 如果需要拓展请继承后拓展 
  
 }