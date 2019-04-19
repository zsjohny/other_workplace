package com.store.entity;

public class SkuVO  {
    
	   
		private long skuId;//SKUID
	    private long colorId;//颜色ID
	    private String color;//颜色名称
	    private long sizeId;//尺码名称
	    private String size;//尺码名称
	    private int remainCount;//库存量
	    private String propertyIds;//商品SKU属性值聚合，PropertyNameId:PropertyValueId形式，多个以英文,隔开，例：7:453;8:425
	    
	    public SkuVO(long skuId,long colorId, String colorName, long sizeId, String sizeName, int remainCount, String propertyIds) {
	    	this.skuId = skuId;
	    	this.colorId = colorId;
	    	this.color = colorName;
	    	this.sizeId = sizeId;
	    	this.size = sizeName;
	    	this.remainCount = remainCount;
	    	this.propertyIds = propertyIds;
		}
	    
	    
	    
		public String getPropertyIds() {
			return propertyIds;
		}



		public void setPropertyIds(String propertyIds) {
			this.propertyIds = propertyIds;
		}



		public long getSkuId() {
			return skuId;
		}
		public void setSkuId(long skuId) {
			this.skuId = skuId;
		}
		public long getColorId() {
			return colorId;
		}
		public void setColorId(long colorId) {
			this.colorId = colorId;
		}
		public String getColor() {
			return color;
		}
		public void setColor(String color) {
			this.color = color;
		}
		public long getSizeId() {
			return sizeId;
		}
		public void setSizeId(long sizeId) {
			this.sizeId = sizeId;
		}
		public String getSize() {
			return size;
		}
		public void setSize(String size) {
			this.size = size;
		}
		public int getRemainCount() {
			return remainCount;
		}
		public void setRemainCount(int remainCount) {
			this.remainCount = remainCount;
		}
		
}
