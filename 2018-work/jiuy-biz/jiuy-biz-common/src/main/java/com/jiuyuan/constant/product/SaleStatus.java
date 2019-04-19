package com.jiuyuan.constant.product;

public enum SaleStatus {
	START(0, ""),
	ALL(0, ""),
	ON_SALE(1, "AND a.SaleStartTime < UNIX_TIMESTAMP() * 1000 AND (a.SaleEndTime = 0 OR a.SaleEndTime > UNIX_TIMESTAMP() * 1000) AND a.Status >= 0"),
	OFF_SALE(2, "AND ((a.SaleEndTime < UNIX_TIMESTAMP() * 1000 AND a.SaleEndTime <> 0) OR a.Status = -1)"),
	WILL_SALE(3, "AND a.SaleStartTime > UNIX_TIMESTAMP() * 1000 AND a.Status >= 0"),
	END(3, "");
	
	SaleStatus(int intValue, String sql) {
		this.intValue = intValue;
		this.sql = sql;
	}
	
	private int intValue;
	private String sql;
	
	public int getIntValue() {
		return intValue;
	}
	
	public String getSql() {
		return sql;
	}
	
	public static String getSql(int intValue) {
		for(SaleStatus saleStatus : SaleStatus.values()) {
			if(saleStatus.getIntValue() == intValue) {
				return saleStatus.getSql();
			}
		}
		return null;
	}
}
