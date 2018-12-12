/**
 *
 */
package com.ouliao.dao.versionfirst.impl;

import com.ouliao.dao.versionfirst.UserSayDao;
import com.ouliao.domain.versionfirst.UserCommont;
import com.ouliao.domain.versionfirst.UserSayContent;
import com.ouliao.domain.versionfirst.UserSupportSay;
import com.ouliao.repository.versionfirst.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

/**
 *
 *
 * @author xiaoluo
 * @version $Id: UserCommontDaoImpl.java, 2016年2月19日 下午6:58:07
 */
@Repository
public class UserSayDaoImpl implements UserSayDao {
	@Resource
	private UserCommontCrudRepository userCommontCrudRepository;
	@Resource
	private UserSayContentCrudRepository userSayCrudRepository;
	@Resource
	private UserCommontRepository userCommontRepository;
	@Resource
	private UserSayContentRepository userSayContentRepository;
	@Resource
	private UserSupportSayCrudRepository userSupportSayCrudRepository;
	@Resource
	private UserSupportSayRepository userSupportSayRepository;
	@Autowired
	private UserCommontPageRepository userCommontPageRepository;

	/**
	 *
	 *
	 * @param
	 * @return
	 */

	@Override
	public boolean createUserCommontByUserId(UserCommont userCommont) {
		userCommont = userCommontCrudRepository.save(userCommont);

		if (userCommont == null) {
			return false;
		} else {
			return true;
		}

	}

	/**
	 *
	 *
	 * @param
	 * @return
	 */

	@Override
	public boolean createUserSayContentByUserId(UserSayContent userSayContent) {
		userSayContent = userSayCrudRepository.save(userSayContent);

		if (userSayContent == null) {
			return false;
		} else {
			return true;
		}

	}

	/**
	 *
	 *
	 * @param
	 * @return
	 */

	@Override
	public int deleteUserSayContentByUserId(Integer userId, Integer userSayContentId) {

		return userSayContentRepository.deleteUserSayContentByUserId(userId, userSayContentId);

	}

	/**
	 *
	 *
	 * @param
	 * @return
	 */

	@Override
	public List<UserSayContent> querySayContentByUserId(Integer userId) {

		return userSayContentRepository.querySayContentByUserId(userId);
	}

	public Iterable<UserSupportSay> querySayAllSupports() {
		return userSupportSayCrudRepository.findAll();
	}

	/**
	 *
	 *
	 * @param
	 * @return
	 */

	@Override
	public void saveSupportsByUserId(UserSupportSay userSupportSay) {
		userSupportSayCrudRepository.save(userSupportSay);

	}

	/**
	 *
	 *
	 * @param
	 * @return
	 */

	@Override
	public List<UserSupportSay> querySupporIsDeletedByUserId(Integer userSayContentId) {
		return userSupportSayRepository.querySupporIsDeletedByUserId(userSayContentId);

	}

	/**
	 *
	 *
	 * @param
	 * @return
	 */

	@Override
	public void updateSupportSayContentById(String isDeleted, Integer userId, Integer userSayContentId) {

		userSupportSayRepository.updateSupportSayContentById(isDeleted, userId, userSayContentId);

	}

	/**
	 *
	 *
	 * @param
	 * @return
	 */

	@Override
	public UserSupportSay querySupportUniqueById(Integer userId, Integer userSayContentId) {

		return userSupportSayRepository.querySupportUniqueById(userId, userSayContentId);
	}

	/**
	 *
	 *
	 * @param
	 * @return
	 */

	@Override
	public int deleteCommontById(Integer userCommontId, Integer userId, Integer ownerId) {
		return userCommontRepository.deleteCommontById(userCommontId, userId, ownerId);

	}

	/**
	 *
	 *
	 * @param
	 * @return
	 */

	@Override
	public Page<UserCommont> querySayCommontAllByUserSayContentId(Integer startPage, Integer pageCount,
																  Integer userSayContentId) {

		final Integer sayId = userSayContentId;
		Specification<UserCommont> specification = new Specification<UserCommont>() {

			@Override
			public Predicate toPredicate(Root<UserCommont> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {

				List<Predicate> list = new ArrayList<>();
				list.add(cb.equal(root.get("userSayContentId").as(Integer.class), sayId));
				list.add(cb.equal(root.get("isDeleted").as(String.class), "0"));
				return cq.where(list.toArray(new Predicate[list.size()])).getRestriction();
			}
		};
		Sort sort = new Sort(Direction.ASC, "userCreateTime");
		Pageable pageable = new PageRequest(startPage, pageCount, sort);

		return userCommontPageRepository.findAll(specification, pageable);

	}

	/**
	 *
	 *
	 * @param
	 * @return
	 */

	@Override
	public UserSayContent querySayContentByUserSayContentId(Integer userSayContentId) {

		return userSayContentRepository.querySayContentByUserSayContentId(userSayContentId);
	}

	/**
	 *
	 *
	 * @param
	 * @return
	 */

	@Override
	public UserSupportSay querySupportUniqueExpecIsDeletedById(Integer userId, Integer userSayContentId) {

		return userSupportSayRepository.querySupportUniqueExpecIsDeletedById(userId, userSayContentId);
	}

	/**
	 *
	 *
	 * @param
	 * @return
	 */

	@Override
	public UserSayContent querySayContentUniqueById(Integer userId, Integer userSayContentId) {

		return userSayContentRepository.querySayContentUniqueById(userId, userSayContentId);
	}

	/**
	 *
	 *
	 * @param
	 * @return
	 */

	@Override
	public UserCommont querySayCommontOneByUserCommontId(Integer userCommontId) {

		return userCommontRepository.querySayCommontOneByUserCommontId(userCommontId);
	}

	/**
	 *
	 *
	 * @param
	 * @return
	 */

	@Override
	public void deleteCommontAllByUserCommontId(Integer userCommontId) {

		userCommontCrudRepository.delete(userCommontId);
	}

	/**
	 *
	 *
	 * @param
	 * @return
	 */

	@Override
	public List<UserCommont> querySayCommontAllByUserSayContentId(Integer userSayContentId) {

		return userCommontRepository.querySayCommontAllByUserSayContentId(userSayContentId);
	}

	/**
	 *
	 *
	 * @param
	 * @return
	 */

	@Override
	public Integer querySupportCountById(Integer userSayContentId) {

		return userSupportSayRepository.querySupportCountById(userSayContentId);
	}

	/**
	 *
	 *
	 * @param
	 * @return
	 */

	@Override
	public Page<UserSayContent> querySayContentAllIsDeletedByUserId(Integer startPage, Integer pageCount,
																	Integer userId) {
		final Integer id = userId;
		Sort sort = new Sort(Direction.DESC, "userCreateTime");

		Pageable pageable = new PageRequest(startPage, pageCount, sort);

		Specification specification = new Specification<UserSayContent>() {

			@Override
			public Predicate toPredicate(Root<UserSayContent> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
				List<Predicate> list = new ArrayList<>();
				list.add(cb.equal(root.get("userId").as(Integer.class), id));
				list.add(cb.equal(root.get("isDeleted").as(String.class), "0"));

				return cq.where(list.toArray(new Predicate[list.size()])).getRestriction();
			}
		};

		return userSayContentRepository.findAll(specification, pageable);
	}

	/**
	 *
	 *
	 * @param
	 * @return
	 */

	@Override
	public Integer querySayCommontCountCountByUserSayContentId(Integer userSayContentId) {

		return userCommontRepository.querySayCommontCountCountByUserSayContentId(userSayContentId);
	}

}
