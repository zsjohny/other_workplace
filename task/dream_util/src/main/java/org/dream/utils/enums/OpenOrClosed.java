package org.dream.utils.enums;

public enum OpenOrClosed {
	closed(0,"闭市"),
	open(1,"开市"),
	todaytradeend(2,"当天交易结束"),
	newtradestart(3,"新交易开始"),
	getQuota(4,"提前30秒获得行情");
	public Integer value;
	public String show;
	private OpenOrClosed(Integer value, String show) {
		this.value = value;
		this.show = show;
	}
	

}
