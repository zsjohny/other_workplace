package com.store.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jiuyuan.entity.newentity.CartItemNewVO;




public class CartProductItemVO  {
	private static final Logger logger = LoggerFactory.getLogger(CartProductItemVO.class);
	    private long productId;//商品ID
	    private String productName;//商品名称
	    private String productMainImg;//商品主图
	  //商品状态： 0（编辑中，未完成编辑）、 1（新建，编辑完成、待提审）、2（待提审）、3（待审核，审核中）、4（审核不通过）、 5（待上架，审核通过、待上架）、 6（上架，审核通过、已上架）、 7（下架，审核通过、已下架）
//	    private int productState;
	  //平台商品上下架状态（用于显示蒙层）：0已上架、1已下架、2已删除
	    private int platformProductState;
	    private long storeId;//门店ID
	    private int productBuyCount;//商品购买数量
	    private int isSelected;//是否选择，0未选择，1选择
	    private long brandId;//品牌ID
	    private String brandName;//品牌名称
	    private String ladderPriceJson;//阶梯价
	    private String memberLadderPriceJson;//会员阶梯价
	    private Integer memberLevel;//会员商品
	    private String clothesNumber;//款号
	    private String cartIds;//购物车ID集合，英文逗号分隔
	    
	    private List<ColorVO> colorList = new ArrayList<ColorVO>();//颜色集合
	    private List<SizeVO> sizeList = new ArrayList<SizeVO>();//尺码集合
	    private Map<String,SkuVO> skuMap = new HashMap<String,SkuVO>();//sku集合
	    private List<Long> selectColorList = new ArrayList<Long>();//已选择颜色集合
	    private List<Long> selectSizeList = new ArrayList<Long>();//已选择尺码集合
	    private List<SelectSkuVO> selectSkuList = new ArrayList<SelectSkuVO>();//选择sku集合


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

		public String getCartIds() {
			return cartIds;
		}
		public void setCartIds(String cartIds) {
			this.cartIds = cartIds;
		}
		public List<ColorVO> getColorList() {
			return colorList;
		}
		public void setColorList(List<ColorVO> colorList) {
			this.colorList = colorList;
		}
		public List<SizeVO> getSizeList() {
			return sizeList;
		}
		public void setSizeList(List<SizeVO> sizeList) {
			this.sizeList = sizeList;
		}
		public List<Long> getSelectColorList() {
			return selectColorList;
		}
		public void setSelectColorList(List<Long> selectColorList) {
			this.selectColorList = selectColorList;
		}
		public List<Long> getSelectSizeList() {
			return selectSizeList;
		}
		public void setSelectSizeList(List<Long> selectSizeList) {
			this.selectSizeList = selectSizeList;
		}
		
		
		
		
		public List<SelectSkuVO> getSelectSkuList() {
			return selectSkuList;
		}
		public void setSelectSkuList(List<SelectSkuVO> selectSkuList) {
			this.selectSkuList = selectSkuList;
		}
		public Map<String, SkuVO> getSkuMap() {
			return skuMap;
		}
		public void setSkuMap(Map<String, SkuVO> skuMap) {
			this.skuMap = skuMap;
		}
		public long getProductId() {
			return productId;
		}
		public void setProductId(long productId) {
			this.productId = productId;
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
		public int getPlatformProductState() {
			return platformProductState;
		}
		public void setPlatformProductState(int platformProductState) {
			this.platformProductState = platformProductState;
		}
		public long getStoreId() {
			return storeId;
		}
		public void setStoreId(long storeId) {
			this.storeId = storeId;
		}
		public int getProductBuyCount() {
			return productBuyCount;
		}
		public void setProductBuyCount(int productBuyCount) {
			this.productBuyCount = productBuyCount;
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
		
		public CartProductItemVO(CartItemNewVO cartItemNewVO) {
			this.productId = cartItemNewVO.getProductId();//商品ID
			this.productName = cartItemNewVO.getProductName();//商品名称
			this.productMainImg = cartItemNewVO.getProductMainImg();//商品主图
			logger.info("商品状态转换商品上下架状态（用于显示蒙层）cartItemNewVO.getProductState():"+cartItemNewVO.getProductState()+",productId:"+productId);
			this.platformProductState = buildPlatformProductState(cartItemNewVO.getProductState());//平台商品上下架状态（用于显示蒙层）：0已上架、1已下架、2已删除
			this.storeId = cartItemNewVO.getStoreId() ;//门店ID
			this.productBuyCount = cartItemNewVO.getBuyCount();//商品购买数量
			this.isSelected = cartItemNewVO.getIsSelected();//是否选择，0未选择，1选择
			this.brandId = cartItemNewVO.getBrandId();//品牌ID
			this.brandName = cartItemNewVO.getBrandName();//品牌名称
			this.ladderPriceJson = cartItemNewVO.getLadderPriceJson();//阶梯价
			this.memberLadderPriceJson = cartItemNewVO.getMemberLadderPriceJson();//会员阶梯价
			this.memberLevel = cartItemNewVO.getMemberLevel();//会员价格
			this.clothesNumber = cartItemNewVO.getClothesNumber();//款号
		}
    
	    /**
	     * 商品状态转换商品上下架状态（用于显示蒙层）：0已上架、1已下架、2已删除
	     */
	    public int buildPlatformProductState(int productState) {
	    	if(productState == 6){
	    		return 0;
	    	}else{
	    		return 1;
	    	}
		}
		public void addColor(long colorId, String colorName) {
			boolean isHave =  false;
			for(ColorVO colorVO : colorList){
				long cid = colorVO.getId();
				if(cid == colorId){
					isHave = true;
				}
			}
			if(!isHave){
				colorList.add(new ColorVO(colorId,colorName));
			}
			
		}
		
		public void addSize(long sizeId, String sizeName) {
			boolean isHave =  false;
			for(SizeVO sizeVO : sizeList){
				long sid = sizeVO.getId();
				if(sid == sizeId){
					isHave = true;
				}
			}
			if(!isHave){
				sizeList.add(new SizeVO(sizeId,sizeName));
			}
		}
		
		public void addSelectSizeList(long sizeId, String sizeName) {
			boolean isHave =  false;
			for(Long sid : selectSizeList){
				if(sid == sizeId){
					isHave = true;
				}
			}
			if(!isHave){
				selectSizeList.add(sizeId);
			}
			
		}
		public void addSelectColorList(long colorId, String colorName) {
			boolean isHave =  false;
			for(Long cid : selectColorList){
				if(cid == colorId){
					isHave = true;
				}
			}
			if(!isHave){
				selectColorList.add(colorId);
			}
		}
		
		
		public void addSelectSkuList(long skuId, int buyCount, String propertyIds) {
			boolean isHave =  false;
			for(SelectSkuVO selectSkuVO : selectSkuList){
				long selectSkuId = selectSkuVO.getSkuId();
				if(selectSkuId == skuId){
					isHave = true;
				}
			}
			if(!isHave){
				selectSkuList.add(new SelectSkuVO(skuId,buyCount,propertyIds));
			}
		}
		
		public void addSku(String propertyIds, SkuVO skuVO) {
			skuMap.put(propertyIds, skuVO);
		}
		public void addCartId(long cartId) {
			if(StringUtils.isEmpty(cartIds)){
				cartIds = String.valueOf(cartId);
			}else{
				cartIds = cartIds + "," + cartId;
			}
		}
		
		
		

}
