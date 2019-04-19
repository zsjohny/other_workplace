package com.jiuyuan.entity.order;

/**
 * 订单状态页面订单状态信息传输对象
 * 
 * @author Administrator
 *
 */
public class OrderStateVO {
	
	//下单成功
	public static String type_XiaDanCengGou = "0";
	//待付款
	public static String type_DaiFuKuan = "1";
	//已付款
	public static String type_YiFuKuan = "2";
	//交易完成
	public static String type_JiaoYiWanCheng = "3";
	//交易关闭
	public static String type_JiaoYiGuanBi = "4";
	//待提货
	public static String type_DaiTiHuo = "5";
	//待发货
	public static String type_DaiFaHuoFuo = "7";
	//已发货
	public static String type_YiFaHuoFuo = "8";
	
//	节点类型：0下单成功、1待付款、2已付款、3交易完成、4交易关闭、5待提货
	private String type = "";
//	节点名称：0下单成功、1待付款、2已付款、3交易完成、4交易关闭、5待提货
	private String name = "";
	//节点时间
	private String time = "";
	//提示信息1
	private String tip1 = "";
	//提示信息2
	private String tip2 = "";
	//提示信息3
	private String tip3 = "";
	//剩余支付时间，单位秒
	private String surplusPayTime  = "0";
	//订单状态
	private int orderStatus ;
	
	public int getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(int orderStatus) {
		this.orderStatus = orderStatus;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getTip1() {
		return tip1;
	}
	public void setTip1(String tip1) {
		this.tip1 = tip1;
	}
	public String getTip2() {
		return tip2;
	}
	public void setTip2(String tip2) {
		this.tip2 = tip2;
	}
	
	public String getTip3() {
		return tip3;
	}
	public void setTip3(String tip3) {
		this.tip3 = tip3;
	}
	public String getSurplusPayTime() {
		return surplusPayTime;
	}
	public void setSurplusPayTime(String surplusPayTime) {
		this.surplusPayTime = surplusPayTime;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	
	
}
