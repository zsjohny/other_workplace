package com.store.entity;

import java.util.List;

import com.jiuyuan.entity.ProductSKU;
import com.jiuyuan.entity.newentity.CartItemNewVO;




public class BrandCartItemNewVO  {
    
    private long brandId;//品牌ID
    private String brandName;//品牌名称
    private int brandHasCouponCount;//品牌可领取优惠券数量
    private String wholesaleLimitTip;//批发限制提示文本, 例子：10件、￥1000元 起订
    private int hasPermission;//商品是否有权限查看：0无权限，1有权限
    private List<CartProductItemVO> cartProductItemVOList;//购物车商品明细
    
    
   	public int getHasPermission() {
   		return hasPermission;
   	}
   	public void setHasPermission(int hasPermission) {
   		this.hasPermission = hasPermission;
   	}
    
	public String getWholesaleLimitTip() {
		return wholesaleLimitTip;
	}
	public void setWholesaleLimitTip(String wholesaleLimitTip) {
		this.wholesaleLimitTip = wholesaleLimitTip;
	}
	
	
	public long getBrandId() {
		return brandId;
	}
	public void setBrandId(long brandId) {
		this.brandId = brandId;
	}
	public String getBrandName() {
		return brandName;
	}
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	
	public int getBrandHasCouponCount() {
		return brandHasCouponCount;
	}
	public void setBrandHasCouponCount(int brandHasCouponCount) {
		this.brandHasCouponCount = brandHasCouponCount;
	}
	public List<CartProductItemVO> getCartProductItemVOList() {
		return cartProductItemVOList;
	}
	public void setCartProductItemVOList(List<CartProductItemVO> cartProductItemVOList) {
		this.cartProductItemVOList = cartProductItemVOList;
	}
	
    
    
    

}
