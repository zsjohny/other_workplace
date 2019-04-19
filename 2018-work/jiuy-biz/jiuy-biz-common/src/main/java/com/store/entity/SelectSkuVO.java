package com.store.entity;

public class SelectSkuVO  {
    
	    private long skuId;//skuID
	    private String propertyIds;////商品SKU属性值聚合，PropertyNameId:PropertyValueId形式，多个以英文,隔开，例：7:453;8:425
	    private int buyCount;//购买数量
		public SelectSkuVO(long skuId, int buyCount, String propertyIds) {
			this.skuId = skuId;
			this.propertyIds = propertyIds;
			this.buyCount = buyCount;
		}
		public long getSkuId() {
			return skuId;
		}
		public void setSkuId(long skuId) {
			this.skuId = skuId;
		}
		public String getPropertyIds() {
			return propertyIds;
		}
		public void setPropertyIds(String propertyIds) {
			this.propertyIds = propertyIds;
		}
		public int getBuyCount() {
			return buyCount;
		}
		public void setBugCount(int buyCount) {
			this.buyCount = buyCount;
		}
		
		
		
	    
}
