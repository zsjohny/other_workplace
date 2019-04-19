package com.jiuyuan.constant;

import com.jiuyuan.util.enumeration.IntEnum;

public enum StatisticsType implements IntEnum {
	/**
	 * 首页
	 */
	HOME_PAGE(0);

	StatisticsType(int intValue) {
		this.intValue = intValue;
	}
	
	private int intValue;
	
	@Override
	public int getIntValue() {
		return intValue;
	}
	
	public static StatisticsType getByIntValue(int intValue) {
		for (StatisticsType statisticsType : StatisticsType.values()) {
			if (statisticsType.getIntValue() == intValue) {
				return statisticsType;
			}
		}
		
		return null;
	}

}
