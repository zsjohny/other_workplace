package com.jiuy.user.model; 

import com.jiuy.base.annotation.FieldName; 
import com.jiuy.base.annotation.ModelName; 
import com.jiuy.base.annotation.PrimaryKey; 
import com.jiuy.base.model.Model; 
import lombok.Data; 
import java.math.BigDecimal;

/**
 * 管理员表
 * <p> 
 * 请不要再映射对象内添加自定义属性 如果需要添加属性请继承后在子类中添加属性
 * 
 * @author Aison
 * @version V1.0  
 * @date 2018年06月08日 下午 06:15:58
 */
@Data
@ModelName(name = "管理员表")
public class SupplierUser extends Model {  
 
	/**
	 * 主键
	 */  
	@FieldName(name = "主键")  
	@PrimaryKey  
	private Long id;  
 
	/**
	 * 用户id
	 */  
	@FieldName(name = "用户id")  
	private Long userId;  
 
	/**
	 * 支付宝关联人姓名
	 */  
	@FieldName(name = "支付宝关联人姓名")  
	private String alipayName;  
 
	/**
	 * 品牌名称
	 */  
	@FieldName(name = "品牌名称")  
	private String businessName;  
 
	/**
	 * 公司名称
	 */  
	@FieldName(name = "公司名称")  
	private String companyName;  
 
	/**
	 * 供应商地址
	 */  
	@FieldName(name = "供应商地址")  
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
	 * 法人
	 */  
	@FieldName(name = "法人")  
	private String legalPerson;  
 
	/**
	 * 法人身份证号
	 */  
	@FieldName(name = "法人身份证号")  
	private String idCardNumber;  
 
	/**
	 * 省
	 */  
	@FieldName(name = "省")  
	private String province;  
 
	/**
	 * 市
	 */  
	@FieldName(name = "市")  
	private String city;  
 
	/**
	 * 银行卡选择标记 0 未选中， 1选中
	 */  
	@FieldName(name = "银行卡选择标记 0 未选中， 1选中")  
	private Integer bankCardFlag;  
 
	/**
	 * 银行开户人名称
	 */  
	@FieldName(name = "银行开户人名称")  
	private String bankAccountName;  
 
	/**
	 * 银行名称
	 */  
	@FieldName(name = "银行名称")  
	private String bankName;  
 
	/**
	 * 银行卡号
	 */  
	@FieldName(name = "银行卡号")  
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
	@FieldName(name = "提款设置 0 所有费用合并提款， 1不同款项分开取款")  
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
	 * 物流银行开户人名称
	 */  
	@FieldName(name = "物流银行开户人名称")  
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
	 * 商品权限 0：公开  -1：不公开
	 */  
	@FieldName(name = "商品权限 0：公开  -1：不公开")  
	private String productPermission;  
 
	/**
	 * 商品宣传图
	 */  
	@FieldName(name = "商品宣传图")  
	private String campaignImage;  
 
	//请不要手动拓展此类 此类为基础类不允许拓展 如果需要拓展请继承后拓展 
  
 }