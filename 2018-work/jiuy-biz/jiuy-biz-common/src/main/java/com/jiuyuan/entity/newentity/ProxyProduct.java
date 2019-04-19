package com.jiuyuan.entity.newentity;

import com.baomidou.mybatisplus.enums.IdType;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 代理产品
 * </p>
 *
 * @author 赵兴林
 * @since 2018-04-07
 */
@TableName("wxaproxy_product")
public class ProxyProduct extends Model<ProxyProduct> {
	public final static long oneDay = 24 * 60 * 60 *1000;
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
	@TableId(value="id", type= IdType.AUTO)
	private Long id;
    /**
     * 产品名称
     */
	private String name;
    /**
     * 市场单价
     */
	private Double price;
//    /**
//     * 可售总库存量
//     */
//	@TableField("total_count")
//	private Integer totalCount;
    /**
     * 单位使用限制天数
     */
	@TableField("single_use_limit_day")
	private Integer singleUseLimitDay;
    /**
     * 续约保护天数
     */
	@TableField("renew_protect_day")
	private Integer renewProtectDay;
    /**
     * 产品备注
     */
	private String note;
    /**
     * 创建时间
     */
	@TableField("create_time")
	private Long createTime;


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

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

//	public Integer getTotalCount() {
//		return totalCount;
//	}
//
//	public void setTotalCount(Integer totalCount) {
//		this.totalCount = totalCount;
//	}

	public Integer getSingleUseLimitDay() {
		return singleUseLimitDay;
	}

	public void setSingleUseLimitDay(Integer singleUseLimitDay) {
		this.singleUseLimitDay = singleUseLimitDay;
	}

	public Integer getRenewProtectDay() {
		return renewProtectDay;
	}

	public void setRenewProtectDay(Integer renewProtectDay) {
		this.renewProtectDay = renewProtectDay;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}
	/**
	 * 总开通天数
	 * @param proxyProductCount
	 * @return
	 */
	public int buildProductTotalOpenDay(int proxyProductCount) {
		int productTotalOpenDay = proxyProductCount * singleUseLimitDay;//总开通天数
		return productTotalOpenDay;
	}
	
	/**
	 * 产品服务使用截止时间（当前时间 + 总开通天数 * 一天毫秒数）
	 * @param proxyProductCount
	 * @return
	 */
	public long buildProductCloseTime(int proxyProductCount ,long wxaCloseTime,long wxaOpenTime) {
		long startTime = buildStartTime(wxaCloseTime,wxaOpenTime);//续约起始时间
		return startTime + (buildProductTotalOpenDay(proxyProductCount) * oneDay); //产品服务使用截止时间
	}
	/**
	 * 产品服务续约保护截止时间(产品服务使用截止时间 + 续约保护天数 * 一天毫秒数)
	 * @param proxyProductId
	 * @param proxyProductCount
	 * @return
	 */
	public long buildProductRenewProtectCloseTime(int proxyProductCount,long wxaCloseTime,long wxaOpenTime){
		return buildProductCloseTime(proxyProductCount, wxaCloseTime, wxaOpenTime) + (this.renewProtectDay  * oneDay);
	}
	
	/**
	 * 续约起始时间
	 * @return
	 */
	public long buildStartTime(long wxaCloseTime,long wxaOpenTime) {
		long now = System.currentTimeMillis();
		if(wxaCloseTime == 0){
			return now;
		}else if(wxaCloseTime >= now){//小程序开通期间内
			return wxaCloseTime;
		}else{
			return now;
		}
	}
	
	/**
	 * 小程序开通时间
	 * @return
	 */
	public long buildWxaOpenTime(long wxaCloseTime,long wxaOpenTime) {
		long now = System.currentTimeMillis();
		if(wxaCloseTime == 0){
			return now;
		}else if(wxaCloseTime >= now){//小程序开通期间内
			return wxaOpenTime;
		}else{
			return now;
		}
	}
}
