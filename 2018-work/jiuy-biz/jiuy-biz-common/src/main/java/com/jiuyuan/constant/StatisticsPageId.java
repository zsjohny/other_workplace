package com.jiuyuan.constant;

import com.jiuyuan.util.enumeration.IntEnum;
/**
 * 统计页面ID  0-首页模板统计,-1-分类页模板,-2-玖币商城模板,大于0专场id
 * @author Administrator
 *
 */
public enum StatisticsPageId implements IntEnum {
	/**
	 * 首页
	 */
	HOME_PAGE(0),
	/**
	 * -1-分类页模板
	 */
	CATEGORY_PAGE(-1),
	/**
	 * -2-玖币商城模板
	 */
	mall_PAGE(-2);

	StatisticsPageId(int intValue) {
		this.intValue = intValue;
	}
	
	private int intValue;
	
	@Override
	public int getIntValue() {
		return intValue;
	}
	
	public static StatisticsPageId getByIntValue(int intValue) {
		for (StatisticsPageId statisticsPageId : StatisticsPageId.values()) {
			if (statisticsPageId.getIntValue() == intValue) {
				return statisticsPageId;
			}
		}
		return null;
	}

}
