/**
 * 
 */
package com.ouliao.service.versionfirst;

import com.ouliao.domain.versionfirst.UserAliPay;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * 
 * 
 * @author xiaoluo
 * @version $Id: UserAliPayService.java, 2016年2月25日 下午9:14:45
 */

public interface UserAliPayService {
	UserAliPay createOrderByUserId(UserAliPay userAliPay);

	UserAliPay queryUserAlipayByIdAndPayInfo(Integer payId, String payInfo);

	int updateUserAlipayCountByPayId(Double payCount, Integer payId, String payInfo);

	int updateUserAlipayIsAuthorByPayId(String isAuthor, String sign, Double payCount, String payInfo,
										String userAliAccount);

	int deleteUserAlipayByUserAliPayId(String isDeleted, Long UserAliPayId);

	Page<UserAliPay> queryUserAlipayPayRecordByPayId(Integer starPage, Integer pageNum, Integer payId);

	List<UserAliPay> queryUserAlipayAllByPayId(Integer payId);
	Integer queryUserAliPayRecordCountByPayId(Integer payId);

	void deleteEmptyUserAliPayRecordByPayId(Integer payId);
}
