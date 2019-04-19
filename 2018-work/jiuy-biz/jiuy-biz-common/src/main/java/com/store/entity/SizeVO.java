package com.store.entity;

public class SizeVO  {
    
	    private long id;//颜色ID
	    private String propertyValue;//颜色名称
		
		
		public SizeVO(long sizeId, String sizeName) {
			this.id = sizeId;
			this.propertyValue = sizeName;
		}

		

		public long getId() {
			return id;
		}



		public void setId(long id) {
			this.id = id;
		}



		public String getPropertyValue() {
			return propertyValue;
		}

		public void setPropertyValue(String propertyValue) {
			this.propertyValue = propertyValue;
		}
		
	   
	    
}
