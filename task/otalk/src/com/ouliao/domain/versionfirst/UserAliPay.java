/**
 * 
 */
package com.ouliao.domain.versionfirst;

import javax.persistence.*;
import java.util.Date;

/**
 * 
 * 
 * @author xiaoluo
 * @version $Id: UserAliPay.java, 2016年2月25日 下午8:52:59
 */
@Entity
@Table(name = "useralipay")
public class UserAliPay {
	private Long userAliPayId;

	private String payInfo;
	private Integer payId;
	private Double payCount;

	private String isAuthor;
	private String sign;
	private String userAliAccount;
	private String isDeleted;
	private Date userCreateTime;
	private Date userModifyTime;

	@Id
	@GeneratedValue
	/**
	 * @return the userAliPayId
	 */
	public Long getUserAliPayId() {
		return userAliPayId;
	}

	/**
	 * @param userAliPayId
	 *            the userAliPayId to set
	 */
	public void setUserAliPayId(Long userAliPayId) {
		this.userAliPayId = userAliPayId;
	}

	@Column(length = 8)
	/**
	 * @return the isAuthor
	 */
	public String getIsAuthor() {
		return isAuthor;
	}

	@Column(length = 128)
	/**
	 * @return the userAliAccount
	 */
	public String getUserAliAccount() {
		return userAliAccount;
	}

	/**
	 * @param userAliAccount
	 *            the userAliAccount to set
	 */
	public void setUserAliAccount(String userAliAccount) {
		this.userAliAccount = userAliAccount;
	}

	@Column(length = 512)
	/**
	 * @return the sign
	 */
	public String getSign() {
		return sign;
	}

	/**
	 * @param sign
	 *            the sign to set
	 */
	public void setSign(String sign) {
		this.sign = sign;
	}

	/**
	 * @param isAuthor
	 *            the isAuthor to set
	 */
	public void setIsAuthor(String isAuthor) {
		this.isAuthor = isAuthor;
	}

	/**
	 * @return the payId
	 */
	public Integer getPayId() {
		return payId;
	}

	/**
	 * @param payId
	 *            the payId to set
	 */
	public void setPayId(Integer payId) {
		this.payId = payId;
	}

	@Column(length = 512)
	/**
	 * @return the payInfo
	 */
	public String getPayInfo() {
		return payInfo;
	}

	/**
	 * @return the payCount
	 */
	public Double getPayCount() {
		return payCount;
	}

	@Column(length = 8)
	/**
	 * @return the isDeleted
	 */
	public String getIsDeleted() {
		return isDeleted;
	}

	/**
	 * @return the userCreateTime
	 */
	public Date getUserCreateTime() {
		return userCreateTime;
	}

	/**
	 * @return the userModifyTime
	 */
	public Date getUserModifyTime() {
		return userModifyTime;
	}

	/**
	 * @param payInfo
	 *            the payInfo to set
	 */
	public void setPayInfo(String payInfo) {
		this.payInfo = payInfo;
	}

	/**
	 * @param payCount
	 *            the payCount to set
	 */
	public void setPayCount(Double payCount) {
		this.payCount = payCount;
	}

	/**
	 * @param isDeleted
	 *            the isDeleted to set
	 */
	public void setIsDeleted(String isDeleted) {
		this.isDeleted = isDeleted;
	}

	/**
	 * @param userCreateTime
	 *            the userCreateTime to set
	 */
	public void setUserCreateTime(Date userCreateTime) {
		this.userCreateTime = userCreateTime;
	}

	/**
	 * @param userModifyTime
	 *            the userModifyTime to set
	 */
	public void setUserModifyTime(Date userModifyTime) {
		this.userModifyTime = userModifyTime;
	}

}
