package com.jiuyuan.entity.newentity;

import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 代理商用户
 * </p>
 *
 * @author 赵兴林
 * @since 2018-04-07
 */
@TableName("wxaproxy_user")
public class ProxyUser extends Model<ProxyUser> {

    private static final long serialVersionUID = 1L;
    
    public final static int proxy_state_normal = 1;// 代理商状态：0禁用、1启用
    public final static int proxy_state_disable = 0;// 代理商状态：0禁用、1启用
    
	


    /**
     * id
     */
	@TableId(value="id", type= IdType.AUTO)
	private Long id;
    /**
     * 头像
     */
	private String avatar;
    /**
     * 账号
     */
	private String account;
    /**
     * 密码
     */
	private String password;
    /**
     * md5密码盐
     */
	private String salt;
    /**
     * 名字
     */
	private String name;
    /**
     * 生日
     */
	private Date birthday;
    /**
     * 性别（1：男 2：女）
     */
	private Integer sex;
    /**
     * 电子邮件
     */
	private String email;
    /**
     * 电话
     */
	private String phone;
    /**
     * 角色id
     */
	private String roleid;
    /**
     * 部门id
     */
	private Integer deptid;
    /**
     * 状态(1：启用  2：冻结  3：删除）
     */
	private Integer status;
    /**
     * 代理商编号
     */
	@TableField("proxy_user_num")
	private String proxyUserNum;
    /**
     * 代理商名称
     */
	@TableField("proxy_user_name")
	private String proxyUserName;
    /**
     * 代理商姓名
     */
	@TableField("proxy_user_full_name")
	private String proxyUserFullName;
    /**
     * 代理商手机号
     */
	@TableField("proxy_user_phone")
	private String proxyUserPhone;
    /**
     * 所在省份城市县区
     */
	@TableField("province_city_county")
	private String provinceCityCounty;
    /**
     * 所在省份
     */
	private String province;
    /**
     * 所在城市
     */
	private String city;
    /**
     * 所在县区
     */
	private String county;
    /**
     * 身份证号码
     */
	@TableField("id_card_no")
	private String idCardNo;
	   /**
     * 代理产品ID
     */
	@TableField("proxy_product_id")
	private Long proxyProductId;
	
	   /**
     * 代理产品库存量
     */
	@TableField("stock_count")
	private Integer stockCount;
	
	   /**
  * 代理产品销售量
  */
	@TableField("sell_out_count")
	private Integer sellOutCount;
	
/**
  * 历史总进货量
  */
	@TableField("history_total_stock_count")
	private Integer historyTotalStockCount;
	
	
	
	 /**
     * 创建时间
     */
	@TableField("create_time")
	private Long createTime;
	/**
	 * 创建时间
	 */
	@TableField("qrcode_str")
	private String qrcodeStr;
	
	

	public Integer getHistoryTotalStockCount() {
		return historyTotalStockCount;
	}

	public void setHistoryTotalStockCount(Integer historyTotalStockCount) {
		this.historyTotalStockCount = historyTotalStockCount;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getRoleid() {
		return roleid;
	}

	public void setRoleid(String roleid) {
		this.roleid = roleid;
	}

	public Integer getDeptid() {
		return deptid;
	}

	public void setDeptid(Integer deptid) {
		this.deptid = deptid;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	public String getProxyUserNum() {
		return proxyUserNum;
	}

	public void setProxyUserNum(String proxyUserNum) {
		this.proxyUserNum = proxyUserNum;
	}

	public String getProxyUserName() {
		return proxyUserName;
	}

	public void setProxyUserName(String proxyUserName) {
		this.proxyUserName = proxyUserName;
	}

	public String getProxyUserFullName() {
		return proxyUserFullName;
	}

	public void setProxyUserFullName(String proxyUserFullName) {
		this.proxyUserFullName = proxyUserFullName;
	}

	public String getProxyUserPhone() {
		return proxyUserPhone;
	}

	public void setProxyUserPhone(String proxyUserPhone) {
		this.proxyUserPhone = proxyUserPhone;
	}

	public String getProvinceCityCounty() {
		return provinceCityCounty;
	}

	public void setProvinceCityCounty(String provinceCityCounty) {
		this.provinceCityCounty = provinceCityCounty;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCounty() {
		return county;
	}

	public void setCounty(String county) {
		this.county = county;
	}

	public String getIdCardNo() {
		return idCardNo;
	}

	public void setIdCardNo(String idCardNo) {
		this.idCardNo = idCardNo;
	}

	public Long getProxyProductId() {
		return proxyProductId;
	}

	public void setProxyProductId(Long proxyProductId) {
		this.proxyProductId = proxyProductId;
	}

	public Integer getStockCount() {
		return stockCount;
	}

	public void setStockCount(Integer stockCount) {
		this.stockCount = stockCount;
	}

	public Integer getSellOutCount() {
		return sellOutCount;
	}

	public void setSellOutCount(Integer sellOutCount) {
		this.sellOutCount = sellOutCount;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}
	
	public String getQrcodeStr() {
		return qrcodeStr;
	}

	public void setQrcodeStr(String qrcodeStr) {
		this.qrcodeStr = qrcodeStr;
	}

	public String buildProxyStateName() {
		if(this.status == proxy_state_normal){
			return "启用";
		}else{
			return "禁用";
		}
	}

	

	
}
