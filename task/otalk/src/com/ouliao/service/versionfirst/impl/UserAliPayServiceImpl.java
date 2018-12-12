/**
 * 
 */
package com.ouliao.service.versionfirst.impl;

import com.ouliao.dao.versionfirst.UserAliPayDao;
import com.ouliao.domain.versionfirst.UserAliPay;
import com.ouliao.service.versionfirst.UserAliPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 
 * 
 * @author xiaoluo
 * @version $Id: UserAliPayServiceImpl.java, 2016年2月25日 下午9:15:05
 */
@Service
@Transactional
public class UserAliPayServiceImpl implements UserAliPayService {
	@Autowired
	private UserAliPayDao userAliPayDao;

	/**
	 *
	 * 
	 * @param
	 * @return
	 */

	@Override
	public UserAliPay createOrderByUserId(UserAliPay userAliPay) {
		return userAliPayDao.createOrderByUserId(userAliPay);
	}

	/**
	 *
	 * 
	 * @param
	 * @return
	 */

	@Override
	public UserAliPay queryUserAlipayByIdAndPayInfo(Integer payId, String payInfo) {
		return userAliPayDao.queryUserAlipayByIdAndPayInfo(payId, payInfo);
	}

	/**
	 *
	 * 
	 * @param
	 * @return
	 */

	@Override
	public int updateUserAlipayCountByPayId(Double payCount, Integer payId, String payInfo) {
		return userAliPayDao.updateUserAlipayCountByPayId(payCount, payId, payInfo);
	}

	/**
	 *
	 * 
	 * @param
	 * @return
	 */

	@Override
	public int deleteUserAlipayByUserAliPayId(String isDeleted, Long UserAliPayId) {
		return userAliPayDao.deleteUserAlipayByUserAliPayId(isDeleted, UserAliPayId);
	}

	/**
	 *
	 * 
	 * @param
	 * @return
	 */

	@Override
	public int updateUserAlipayIsAuthorByPayId(String isAuthor, String sign, Double payCount, String payInfo,
			String userAliAccount) {
		return userAliPayDao.updateUserAlipayIsAuthorByPayId(isAuthor, sign, payCount, payInfo, userAliAccount);
	}

	/**
	 *
	 * 
	 * @param
	 * @return
	 */

	@Override
	public Page<UserAliPay> queryUserAlipayPayRecordByPayId(Integer starPage, Integer pageNum, Integer payId) {
		return userAliPayDao.queryUserAlipayPayRecordByPayId(starPage, pageNum, payId);
	}

	/**
	 *
	 * 
	 * @param
	 * @return
	 */

	@Override
	public Integer queryUserAliPayRecordCountByPayId(Integer payId) {
		return userAliPayDao.queryUserAliPayRecordCountByPayId(payId);
	}

	/**
	 *
	 * 
	 * @param
	 * @return
	 */

	@Override
	public List<UserAliPay> queryUserAlipayAllByPayId(Integer payId) {
		return userAliPayDao.queryUserAlipayAllByPayId(payId);
	}

	/**
	 *
	 * 
	 * @param
	 * @return
	 */

	@Override
	public void deleteEmptyUserAliPayRecordByPayId(Integer payId) {
		userAliPayDao.deleteEmptyUserAliPayRecordByPayId(payId);
	}

}
