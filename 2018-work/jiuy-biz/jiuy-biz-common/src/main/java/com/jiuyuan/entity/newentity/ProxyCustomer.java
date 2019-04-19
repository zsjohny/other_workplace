package com.jiuyuan.entity.newentity;

import com.baomidou.mybatisplus.enums.IdType;
import com.jiuyuan.util.DateUtil;
import com.xiaoleilu.hutool.date.DateUnit;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 代理商客户
 * </p>
 *
 * @author 赵兴林
 * @since 2018-04-07
 */
@TableName("wxaproxy_customer")
public class ProxyCustomer extends Model<ProxyCustomer> {

    private static final long serialVersionUID = 1L;
    
    public static final int STATUS_SIGNED = 0;//已签约
    public static final int STATUS_RENEWAL_RESERVE = 1;//续约保护区

    /**
     * id
     */
	@TableId(value="id", type= IdType.AUTO)
	private Long id;
    /**
     * 客户姓名
     */
	private String name;
    /**
     * 客户手机号
     */
	private String phone;
//    /**
//     * 
//     */
//	private Integer status;
	
	/**
     * 代理商ID
     */
	@TableField("proxy_user_id")
	private Long proxyUserId;
	
    /**
     * 开通服务产品ID
     */
	@TableField("proxy_product_id")
	private Long proxyProductId;
    /**
     * 开通服务产品名称
     */
	@TableField("proxy_product_name")
	private String proxyProductName;
    /**
     * 开通服务数量
     */
	@TableField("proxy_product_count")
	private Integer proxyProductCount;
	
    /**
     * 产品服务开通时间
     */
	@TableField("product_open_time")
	private Long productOpenTime;
	/**
     * 产品服务使用截止时间
     */
	@TableField("product_close_time")
	private Long productCloseTime;
	/**
     * 产品服务续约保护截止时间
     */
	@TableField("product_renew_protect_close_time")
	private Long productRenewProtectCloseTime;
	
	//客户状态：已签约(0)、续约保护期中(1)、已过期(2)
	public int buildStatus() {
		long now = System.currentTimeMillis();
		if(now <= productCloseTime){
			return 0;//已签约
		}else if(now <= productRenewProtectCloseTime){
			return 1;//续约保护期中
		}else{
			return 2;//已过期
		}
	}
	//客户状态：已签约(0)、续约保护期中(1)、已过期(2)
	public String buildStatusName() {
		int  status = buildStatus();
		if(status == 0){
			return "已签约";
		}else if(status == 1){
			return "续约保护期中";
		}else{
			return "已过期";
		}
	}
    /**
     * 开通总天数
     */
	@TableField("product_total_open_day")
	private Integer productTotalOpenDay;
	/**
	 * 门店ID
	 */
	@TableField("store_id")
	private Long storeId;
//    /**
//     * 剩余开通使用天数
//     */
//	private Double productSurpluslOpenDay;


	public Long getProductRenewProtectCloseTime() {
		return productRenewProtectCloseTime;
	}

	public void setProductRenewProtectCloseTime(Long productRenewProtectCloseTime) {
		this.productRenewProtectCloseTime = productRenewProtectCloseTime;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

//	public Integer getStatus() {
//		return status;
//	}
//
//	public void setStatus(Integer status) {
//		this.status = status;
//	}

	public Long getProxyProductId() {
		return proxyProductId;
	}

	public void setProxyProductId(Long proxyProductId) {
		this.proxyProductId = proxyProductId;
	}

	public String getProxyProductName() {
		return proxyProductName;
	}

	public void setProxyProductName(String proxyProductName) {
		this.proxyProductName = proxyProductName;
	}

	public Integer getProxyProductCount() {
		return proxyProductCount;
	}

	public void setProxyProductCount(Integer proxyProductCount) {
		this.proxyProductCount = proxyProductCount;
	}

	public Long getProductOpenTime() {
		return productOpenTime;
	}

	public void setProductOpenTime(Long productOpenTime) {
		this.productOpenTime = productOpenTime;
	}

	public Long getProductCloseTime() {
		return productCloseTime;
	}

	public void setProductCloseTime(Long productCloseTime) {
		this.productCloseTime = productCloseTime;
	}

	public Integer getProductTotalOpenDay() {
		return productTotalOpenDay;
	}

	public void setProductTotalOpenDay(Integer productTotalOpenDay) {
		this.productTotalOpenDay = productTotalOpenDay;
	}

	public Double getProductSurpluslOpenDay() {
		long currentTime = System.currentTimeMillis();
		long productCloseTime = this.productCloseTime;
		if(currentTime > productCloseTime){
			return 0.0;
		}
		long productSurpluslOpenTime = productCloseTime - currentTime;
		BigDecimal bigDecimal = new BigDecimal(productSurpluslOpenTime);
		long oneDay = 24L*60*60*1000;
		bigDecimal = bigDecimal.divide(new BigDecimal(oneDay),1, BigDecimal.ROUND_HALF_DOWN);
		
		return bigDecimal.doubleValue();
	}


	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	public Long getProxyUserId() {
		return proxyUserId;
	}

	public void setProxyUserId(Long proxyUserId) {
		this.proxyUserId = proxyUserId;
	}

	public Long getStoreId() {
		return storeId;
	}

	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}

//	public void setProductSurpluslOpenDay(Double productSurpluslOpenDay) {
//		this.productSurpluslOpenDay = productSurpluslOpenDay;
//	}

	

	

}
