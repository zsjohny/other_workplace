package com.store.entity;

public class ColorVO  {
    
	    private long id;//颜色ID
	    private String propertyValue;//颜色名称
		
		public ColorVO(long colorId, String colorName) {
			this.id = colorId;
			this.propertyValue = colorName;
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
