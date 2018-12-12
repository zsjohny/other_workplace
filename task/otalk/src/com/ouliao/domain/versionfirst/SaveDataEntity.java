/**
 * 
 */
package com.ouliao.domain.versionfirst;

/**
 * 
 * 
 * @author xiaoluo
 * @version $Id: SaveData.java, 2016年3月5日 下午12:46:13
 */
// #ID&注册时间&手机号&密码&支付宝账号&昵称&认证状态&认证名字&通话时长&账户余额&收入金额&消费金额&在线星期&在线时间
public class SaveDataEntity {
	private String userNickName;
	private String userNum;
	private String userPhone;
	private String userPass;
	private String userSign;
	private String userAuth;
	private String userContract;


	private String userCallTime;
	private String userCallTimeWeek;
	private Double userCallCost;
	private Double userCallTotal;
	private String userLabel;

	private Double userMoney;
	private Double userCallEarn;
	private Double userCallConsume;
	private String userCreateTime;

	/**
	 * @return the userNickName
	 */
	public String getUserNickName() {
		return userNickName;
	}

	/**
	 * @return the userNum
	 */
	public String getUserNum() {
		return userNum;
	}

	/**
	 * @return the userPhone
	 */
	public String getUserPhone() {
		return userPhone;
	}

	/**
	 * @return the userPass
	 */
	public String getUserPass() {
		return userPass;
	}

	/**
	 * @return the userSign
	 */
	public String getUserSign() {
		return userSign;
	}

	/**
	 * @return the userAuth
	 */
	public String getUserAuth() {
		return userAuth;
	}

	/**
	 * @return the userContract
	 */
	public String getUserContract() {
		return userContract;
	}

	/**
	 * @return the userCallTime
	 */
	public String getUserCallTime() {
		return userCallTime;
	}

	/**
	 * @return the userCallTimeWeek
	 */
	public String getUserCallTimeWeek() {
		return userCallTimeWeek;
	}

	/**
	 * @return the userCallCost
	 */
	public Double getUserCallCost() {
		return userCallCost;
	}


	/**
	 * @return the userLabel
	 */
	public String getUserLabel() {
		return userLabel;
	}

	/**
	 * @return the userMoney
	 */
	public Double getUserMoney() {
		return userMoney;
	}

	/**
	 * @return the userCallEarn
	 */
	public Double getUserCallEarn() {
		return userCallEarn;
	}

	/**
	 * @return the userCallConsume
	 */
	public Double getUserCallConsume() {
		return userCallConsume;
	}

	/**
	 * @return the userCreateTime
	 */
	public String getUserCreateTime() {
		return userCreateTime;
	}

	/**
	 * @param userNickName
	 *            the userNickName to set
	 */
	public void setUserNickName(String userNickName) {
		this.userNickName = userNickName;
	}

	/**
	 * @param userNum
	 *            the userNum to set
	 */
	public void setUserNum(String userNum) {
		this.userNum = userNum;
	}

	/**
	 * @param userPhone
	 *            the userPhone to set
	 */
	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}

	/**
	 * @param userPass
	 *            the userPass to set
	 */
	public void setUserPass(String userPass) {
		this.userPass = userPass;
	}

	/**
	 * @param userSign
	 *            the userSign to set
	 */
	public void setUserSign(String userSign) {
		this.userSign = userSign;
	}

	/**
	 * @param userAuth
	 *            the userAuth to set
	 */
	public void setUserAuth(String userAuth) {
		this.userAuth = userAuth;
	}

	/**
	 * @param userContract
	 *            the userContract to set
	 */
	public void setUserContract(String userContract) {
		this.userContract = userContract;
	}

	/**
	 * @param userCallTime
	 *            the userCallTime to set
	 */
	public void setUserCallTime(String userCallTime) {
		this.userCallTime = userCallTime;
	}

	/**
	 * @param userCallTimeWeek
	 *            the userCallTimeWeek to set
	 */
	public void setUserCallTimeWeek(String userCallTimeWeek) {
		this.userCallTimeWeek = userCallTimeWeek;
	}

	/**
	 * @param userCallCost
	 *            the userCallCost to set
	 */
	public void setUserCallCost(Double userCallCost) {
		this.userCallCost = userCallCost;
	}

	/**
	 * @return the userCallTotal
	 */
	public Double getUserCallTotal() {
		return userCallTotal;
	}

	/**
	 * @param userCallTotal
	 *            the userCallTotal to set
	 */
	public void setUserCallTotal(Double userCallTotal) {
		this.userCallTotal = userCallTotal;
	}

	/**
	 * @param userLabel
	 *            the userLabel to set
	 */
	public void setUserLabel(String userLabel) {
		this.userLabel = userLabel;
	}

	/**
	 * @param userMoney
	 *            the userMoney to set
	 */
	public void setUserMoney(Double userMoney) {
		this.userMoney = userMoney;
	}

	/**
	 * @param userCallEarn
	 *            the userCallEarn to set
	 */
	public void setUserCallEarn(Double userCallEarn) {
		this.userCallEarn = userCallEarn;
	}

	/**
	 * @param userCallConsume
	 *            the userCallConsume to set
	 */
	public void setUserCallConsume(Double userCallConsume) {
		this.userCallConsume = userCallConsume;
	}

	/**
	 * @param userCreateTime
	 *            the userCreateTime to set
	 */
	public void setUserCreateTime(String userCreateTime) {
		this.userCreateTime = userCreateTime;
	}



}
