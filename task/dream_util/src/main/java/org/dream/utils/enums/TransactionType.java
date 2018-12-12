package org.dream.utils.enums;
/**
 * 发送给交易平台的类型，代表各个操作
 * 
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author yehx
 * @date 2016年7月13日 下午7:24:47
 */
public enum TransactionType {
	REQ_OPEN_OR_UNWIND(60001,"开仓/平仓-请求"),
	RES_OPEN_OR_UNWIND(60002,"开仓/平仓-响应"),
	REQ_CLEARANCE(60003,"清仓-请求"),
	RES_CLEARANCE(60004,"清仓-响应");
	
	public Integer value;
	public String  show;
	
	private TransactionType(Integer value,String show){
		this.value=value;
		this.show=show;
	}
}
