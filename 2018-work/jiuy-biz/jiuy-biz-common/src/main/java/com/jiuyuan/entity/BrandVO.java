/**
 * 
 */
package com.jiuyuan.entity;

import java.util.List;
import java.util.Map;

import com.yujj.entity.Brand;

/**
* @author DongZhong
* @version 创建时间: 2016年12月18日 下午10:33:48
*/
public class BrandVO extends Brand {
	private int productNum;
	/**
	 * 是否有优惠券 0：没有优惠券  1：有优惠券
	 */
	private Integer hasCoupon;
	/**
	 * 是否有客户权限 0:没有权限  1:有权限
	 */
	private Integer hasPermission;
	
	
	/**
	 * 批发限制状态（即混批）（0不混批、1混批）
	 */
	private int wholesaleLimitState;
	
	/**
	 * 批发限制提示文本1, 例子：10件、￥1000元 起订
	 */
	private String wholesaleLimitTip1;
	/**
	 * 批发限制提示文本1, 例子：10件、￥1000元 起订
	 */
	private String wholesaleLimitTip2;

	/**
	 * 供应商id
	 */
	private Long supplierId;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5675115147013917383L;
	List<Map<String, Object>> products;
	public List<Map<String, Object>> getProducts() {
		return products;
	}
	public void setProducts(List<Map<String, Object>> products) {
		this.products = products;
	}
	public int getProductNum() {
		return productNum;
	}
	public void setProductNum(int productNum) {
		this.productNum = productNum;
	}
	public Integer getHasCoupon() {
		return hasCoupon;
	}
	public void setHasCoupon(Integer hasCoupon) {
		this.hasCoupon = hasCoupon;
	}
	public Integer getWholesaleLimitState() {
		return wholesaleLimitState;
	}
	public void setWholesaleLimitState(Integer wholesaleLimitState) {
		this.wholesaleLimitState = wholesaleLimitState;
	}
	public String getWholesaleLimitTip1() {
		return wholesaleLimitTip1;
	}
	public void setWholesaleLimitTip1(String wholesaleLimitTip1) {
		this.wholesaleLimitTip1 = wholesaleLimitTip1;
	}
	public String getWholesaleLimitTip2() {
		return wholesaleLimitTip2;
	}
	public void setWholesaleLimitTip2(String wholesaleLimitTip2) {
		this.wholesaleLimitTip2 = wholesaleLimitTip2;
	}
	public void setWholesaleLimitState(int wholesaleLimitState) {
		this.wholesaleLimitState = wholesaleLimitState;
	}
	public Integer getHasPermission() {
		return hasPermission;
	}
	public void setHasPermission(Integer hasPermission) {
		this.hasPermission = hasPermission;
	}

	public Long getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Long supplierId) {
		this.supplierId = supplierId;
	}
}
