package com.yujj.business.service.globalsetting.bean;
/**
 * 数据库yjj_GlobalSetting表中jiucoin_global_setting对应的propertyValue的一个值
 * 暂不确定是什么值
 * @author qyf
 *
 */
public class ShoppingJiuCoinSetting {
	/**
	 * 
	 */
	private String evaluationPercentage;
	/**
	 * 
	 */
	private String returnPercentage;
	public String getEvaluationPercentage() {
		return evaluationPercentage;
	}
	public void setEvaluationPercentage(String evaluationPercentage) {
		this.evaluationPercentage = evaluationPercentage;
	}
	public String getReturnPercentage() {
		return returnPercentage;
	}
	public void setReturnPercentage(String returnPercentage) {
		this.returnPercentage = returnPercentage;
	}
	
}
