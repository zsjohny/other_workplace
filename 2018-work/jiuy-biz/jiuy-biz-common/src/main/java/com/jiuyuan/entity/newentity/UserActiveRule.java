package com.jiuyuan.entity.newentity;

/**
 * 地推条件规则--用户时间阶段规则
 * 
 * 相关bean：UserTimeRule、GroundConditionRule
 * @author 赵兴林
 * @since 2017-11-14
 */
public class UserActiveRule{

	public static final int rule1 = 0;//1（订单中至少有一个商品价格不低于）
	public static final int rule2 = 2;//2（订单优惠金额总计不超过）
	public static final int rule3 = 3;//3（订单实付金额不低于）
	public static final int rule4 = 4;//4（订单中商品SKU数量不低于）
	
    /**
     * 条件名称：1（订单中至少有一个商品价格不低于）、2（订单优惠金额总计不超过）、3（订单实付金额不低于）、4（订单中商品SKU数量不低于）
     */
	private String name;
    /**
     * 条件类型：1（订单中至少有一个商品价格不低于）、2（订单优惠金额总计不超过）、3（订单实付金额不低于）、4（订单中商品SKU数量不低于）
     */
	private int type;
    /**
     * 是否开启，1开启、0关闭
     */
	private int isOpen;
   /**
    * 界限值	可为double
    */
	private double limitValue;
	
	public UserActiveRule(String name,int type,int isOpen,double limitValue){
		this.name = name;
		this.type = type;
		this.isOpen = isOpen;
		this.limitValue = limitValue;
	}
	
	public String getName() {
		return name;
	}
	
	public int getType() {
		return type;
	}
	
	public int getIsOpen() {
		return isOpen;
	}
	
	public double getLimitValue() {
		return limitValue;
	}
	
   
}
