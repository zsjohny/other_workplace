package com.jiuyuan.entity.newentity;

public class CartItemNewVO  {
    
    private long cartId;//购物车ID
    private long productId;//商品ID
    private String productName;//商品名称
    private String productMainImg;//商品主图
  //商品状态： 0（编辑中，未完成编辑）、 1（新建，编辑完成、待提审）、2（待提审）、3（待审核，审核中）、4（审核不通过）、 5（待上架，审核通过、待上架）、 6（上架，审核通过、已上架）、 7（下架，审核通过、已下架）
    private int productState;
//  //平台商品上下架状态（用于显示蒙层）：0已上架、1已下架、2已删除
//    private int platformProductState;
    private long storeId;//门店ID
    private long skuId;//SKUID
//    private int productBuyCount;//商品购买数量
    private int buyCount;//SKU购买数量
    private int isSelected;//是否选择，0未选择，1选择
    private long brandId;//品牌ID
    private String brandName;//品牌名称
    private String ladderPriceJson;//阶梯价
    private String memberLadderPriceJson;//会员阶梯价
	private Integer memberLevel;//会员商品
    private String clothesNumber;//款号
   
    

    

//   
//	public int getProductBuyCount() {
//		return productBuyCount;
//	}
//
//	public void setProductBuyCount(int productBuyCount) {
//		this.productBuyCount = productBuyCount;
//	}
//
//	public int getPlatformProductState() {
//		return platformProductState;
//	}
//	public void setPlatformProductState(int platformProductState) {
//		this.platformProductState = platformProductState;
//	}


	public Integer getMemberLevel() {
		return memberLevel;
	}

	public void setMemberLevel(Integer memberLevel) {
		this.memberLevel = memberLevel;
	}

	public String getMemberLadderPriceJson() {
		return memberLadderPriceJson;
	}

	public void setMemberLadderPriceJson(String memberLadderPriceJson) {
		this.memberLadderPriceJson = memberLadderPriceJson;
	}

	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getProductMainImg() {
		return productMainImg;
	}
	public void setProductMainImg(String productMainImg) {
		this.productMainImg = productMainImg;
	}
	
	public long getCartId() {
		return cartId;
	}
	public void setCartId(long cartId) {
		this.cartId = cartId;
	}
	public long getProductId() {
		return productId;
	}
	public void setProductId(long productId) {
		this.productId = productId;
	}
	public long getStoreId() {
		return storeId;
	}
	public void setStoreId(long storeId) {
		this.storeId = storeId;
	}
	public long getSkuId() {
		return skuId;
	}
	public void setSkuId(long skuId) {
		this.skuId = skuId;
	}
	public int getBuyCount() {
		return buyCount;
	}
	public void setBuyCount(int buyCount) {
		this.buyCount = buyCount;
	}
	public int getIsSelected() {
		return isSelected;
	}
	public void setIsSelected(int isSelected) {
		this.isSelected = isSelected;
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
	public String getLadderPriceJson() {
		return ladderPriceJson;
	}
	public void setLadderPriceJson(String ladderPriceJson) {
		this.ladderPriceJson = ladderPriceJson;
	}
	public String getClothesNumber() {
		return clothesNumber;
	}
	public void setClothesNumber(String clothesNumber) {
		this.clothesNumber = clothesNumber;
	}
	public int getProductState() {
		return productState;
	}
	public void setProductState(int productState) {
		this.productState = productState;
	}
	
	
    
    
    

}
