package com.jiuy.supplier.core.shiro;

import java.io.Serializable;
import java.util.List;

/**
 * 自定义Authentication对象，使得Subject除了携带用户的登录名外还可以携带更多信息
 *
 * @author fengshuonan
 * @date 2016年12月5日 上午10:26:43
 */
public class ShiroUser implements Serializable {

    private static final long serialVersionUID = 1L;

    public Integer id;          // 主键ID
    public String account;      // 账号
    public String name;         // 姓名
    public Integer deptId;      // 部门id
    public List<Integer> roleList; // 角色集
    public String deptName;        // 部门名称
    public List<String> roleNames; // 角色名称集
    public String businessName;    //商家名称
    public String status;          //账户状态0正常，1 禁用
    public String companyName;     //公司名称
    public String BusinessAddress; //商家地址
    public String LicenseNumber;   //营业执照号或者统一社会信用代码
    public String TaxId;        //税务登记号
    public String LegalPerson;     //法定代表人
    public String IdCardNumber;    //身份证号
    public String Province;     //所在省份
    public String City;         //所在城市
    public int BankCardFlag;    //银行卡选择标记 0 未选中， 1选中
    public String BankAccountName; //银行开户名称
    public String BankName;     //开户银行名称
    public String BankAccountNo;   //银行账户号
    public int AlipayFlag;         //支付宝选择标记 0 未选中， 1选中
	public String AlipayAccount;   //支付宝账号
    public String AlipayName;      //支付宝关联人姓名
    public String PhoneNumber;     //手机号码
    public double availableBalance; //可提现金额
    public double minWithdrawal;   //最低提现额
    public String clothNumberPrefix; //品牌商品款号前缀
    public String brandName;       //品牌名称
    public long brandId;        //品牌Id
    public long lowarehouseId;  //仓库Id
    
    public long getBrandId() {
		return brandId;
	}

	public void setBrandId(long brandId) {
		this.brandId = brandId;
	}

	public long getLowarehouseId() {
		return lowarehouseId;
	}

	public void setLowarehouseId(long lowarehouseId) {
		this.lowarehouseId = lowarehouseId;
	}

	public String getLowarehouseName() {
		return lowarehouseName;
	}

	public void setLowarehouseName(String lowarehouseName) {
		this.lowarehouseName = lowarehouseName;
	}

	public String lowarehouseName; //仓库名称    
    

    public String getClothNumberPrefix() {
		return clothNumberPrefix;
	}

	public void setClothNumberPrefix(String clothNumberPrefix) {
		this.clothNumberPrefix = clothNumberPrefix;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public double getMinWithdrawal() {
		return minWithdrawal;
	}

	public void setMinWithdrawal(double minWithdrawal) {
		this.minWithdrawal = minWithdrawal;
	}

	public double getAvailableBalance() {
		return availableBalance;
	}

	public void setAvailableBalance(double availableBalance) {
		this.availableBalance = availableBalance;
	}

	public String getBusinessName() {
		return businessName;
	}

	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getBusinessAddress() {
		return BusinessAddress;
	}

	public void setBusinessAddress(String businessAddress) {
		BusinessAddress = businessAddress;
	}

	public String getLicenseNumber() {
		return LicenseNumber;
	}

	public void setLicenseNumber(String licenseNumber) {
		LicenseNumber = licenseNumber;
	}

	public String getTaxId() {
		return TaxId;
	}

	public void setTaxId(String taxId) {
		TaxId = taxId;
	}

	public String getLegalPerson() {
		return LegalPerson;
	}

	public void setLegalPerson(String legalPerson) {
		LegalPerson = legalPerson;
	}

	public String getIdCardNumber() {
		return IdCardNumber;
	}

	public void setIdCardNumber(String idCardNumber) {
		IdCardNumber = idCardNumber;
	}

	public String getProvince() {
		return Province;
	}

	public void setProvince(String province) {
		Province = province;
	}

	public String getCity() {
		return City;
	}

	public void setCity(String city) {
		City = city;
	}

	public int getBankCardFlag() {
		return BankCardFlag;
	}

	public void setBankCardFlag(int bankCardFlag) {
		BankCardFlag = bankCardFlag;
	}

	public String getBankAccountName() {
		return BankAccountName;
	}

	public void setBankAccountName(String bankAccountName) {
		BankAccountName = bankAccountName;
	}

	public String getBankName() {
		return BankName;
	}

	public void setBankName(String bankName) {
		BankName = bankName;
	}

	public String getBankAccountNo() {
		return BankAccountNo;
	}

	public void setBankAccountNo(String bankAccountNo) {
		BankAccountNo = bankAccountNo;
	}

	public int getAlipayFlag() {
		return AlipayFlag;
	}

	public void setAlipayFlag(int alipayFlag) {
		AlipayFlag = alipayFlag;
	}

	public String getAlipayAccount() {
		return AlipayAccount;
	}

	public void setAlipayAccount(String alipayAccount) {
		AlipayAccount = alipayAccount;
	}

	public String getAlipayName() {
		return AlipayName;
	}

	public void setAlipayName(String alipayName) {
		AlipayName = alipayName;
	}

	public String getPhoneNumber() {
		return PhoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		PhoneNumber = phoneNumber;
	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getDeptId() {
        return deptId;
    }

    public void setDeptId(Integer deptId) {
        this.deptId = deptId;
    }

    public List<Integer> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<Integer> roleList) {
        this.roleList = roleList;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public List<String> getRoleNames() {
        return roleNames;
    }

    public void setRoleNames(List<String> roleNames) {
        this.roleNames = roleNames;
    }

}
