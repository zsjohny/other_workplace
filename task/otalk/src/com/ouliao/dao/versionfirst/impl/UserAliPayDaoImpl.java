/**
 * 
 */
package com.ouliao.dao.versionfirst.impl;

import com.ouliao.dao.versionfirst.UserAliPayDao;
import com.ouliao.domain.versionfirst.UserAliPay;
import com.ouliao.repository.versionfirst.UserAliPayCrudRepository;
import com.ouliao.repository.versionfirst.UserAliPayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 
 * 
 * @author xiaoluo
 * @version $Id: UserAliPayDaoImpl.java, 2016年2月25日 下午9:14:18
 */
@Repository
public class UserAliPayDaoImpl implements UserAliPayDao {
	@Autowired
	private UserAliPayRepository userAliPayRepository;
	@Autowired
	private UserAliPayCrudRepository userAliPayCrudRepository;

	/**
	 *
	 * 
	 * @param
	 * @return
	 */

	@Override
	public UserAliPay createOrderByUserId(UserAliPay userAliPay) {
		return userAliPayCrudRepository.save(userAliPay);
	}

	/**
	 *
	 * 
	 * @param
	 * @return
	 */

	@Override
	public UserAliPay queryUserAlipayByIdAndPayInfo(Integer payId, String payInfo) {
		return userAliPayRepository.queryUserAlipayByIdAndPayInfo(payId, payInfo);
	}

	/**
	 *
	 * 
	 * @param
	 * @return
	 */

	@Override
	public int updateUserAlipayCountByPayId(Double payCount, Integer payId, String payInfo) {
		return userAliPayRepository.updateUserAlipayCountByPayId(payCount, payId, payInfo);
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
		if ("true".equals(isAuthor)) {
			return userAliPayRepository.updateUserAlipayIsAuthorAliPayByPayId(isAuthor, sign, payCount, payInfo,
					userAliAccount);
		}

		return userAliPayRepository.updateUserAlipayIsAuthorByPayId(isAuthor, sign, payCount, payInfo, userAliAccount);
	}

	/**
	 *
	 * 
	 * @param
	 * @return
	 */

	@Override
	public int deleteUserAlipayByUserAliPayId(String isDeleted, Long UserAliPayId) {
		return userAliPayRepository.deleteUserAlipayByUserAliPayId(isDeleted, UserAliPayId);
	}

	/**
	 *
	 * 
	 * @param
	 * @return
	 */

	@Override
	public Page<UserAliPay> queryUserAlipayPayRecordByPayId(Integer starPage, Integer pageNum, Integer payId) {

		final Integer id = payId;

		Sort sort = new Sort(Direction.DESC, "userCreateTime");

		Pageable pageable = new PageRequest(starPage, pageNum, sort);

		Specification<UserAliPay> specification = new Specification<UserAliPay>() {

			@Override
			public Predicate toPredicate(Root<UserAliPay> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {

				List<Predicate> list = new ArrayList<>();
				list.add(cb.equal(root.get("payId").as(Integer.class), id));
				list.add(cb.isNotNull(root.get("userModifyTime").as(Date.class)));

				return cq.where(list.toArray(new Predicate[list.size()])).getRestriction();

			}

		};

		return userAliPayCrudRepository.findAll(specification, pageable);
	}

	/**
	 *
	 * 
	 * @param
	 * @return
	 */

	@Override
	public Integer queryUserAliPayRecordCountByPayId(Integer payId) {
		return userAliPayRepository.queryUserAliPayRecordCountByPayId(payId);
	}

	/**
	 *
	 * 
	 * @param
	 * @return
	 */

	@Override
	public List<UserAliPay> queryUserAlipayAllByPayId(Integer payId) {
		return userAliPayRepository.queryUserAlipayAllByPayId(payId);
	}

	/**
	 *
	 * 
	 * @param
	 * @return
	 */

	@Override
	public void deleteEmptyUserAliPayRecordByPayId(Integer payId) {

		List<UserAliPay> userAliPays = userAliPayRepository.queryEmptyUserAliPayRecordByPayId(payId);

		if (userAliPays == null) {
			return;
		}

		userAliPayCrudRepository.delete(userAliPays);
	}

}
