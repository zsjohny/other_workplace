/**
 * 
 */
package com.ouliao.dao.versionfirst.impl;

import com.ouliao.dao.versionfirst.UserBlackListDao;
import com.ouliao.domain.versionfirst.UserBlackList;
import com.ouliao.repository.versionfirst.UserBlackListCrudRepository;
import com.ouliao.repository.versionfirst.UserBlackListPageRepository;
import com.ouliao.repository.versionfirst.UserBlackListRepository;
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
 * @version $Id: UserBlackListDaoImpl.java, 2016年2月23日 下午6:27:05
 */
@Repository
public class UserBlackListDaoImpl implements UserBlackListDao {
	@Resource
	private UserBlackListCrudRepository userBlackListCrudRepository;
	@Autowired
	private UserBlackListRepository userBlackListRepository;
	@Autowired
	private UserBlackListPageRepository userBlackListPageRepository;

	/**
	 *
	 * 
	 * @param
	 * @return
	 */

	@Override
	public int updateUserBlackListByUserBlackListId(String isDeleted, Integer userBlackListId) {

		return userBlackListRepository.updateUserBlackListByUserBlackListId(isDeleted, userBlackListId);
	}

	/**
	 *
	 * 
	 * @param
	 * @return
	 */

	@Override
	public UserBlackList createUserBlackList(UserBlackList userBlackList) {
		return userBlackListCrudRepository.save(userBlackList);
	}

	/**
	 *
	 * 
	 * @param
	 * @return
	 */

	@Override
	public UserBlackList queryUserIsBlackListById(Integer userId, Integer userBlackId) {

		return userBlackListRepository.queryUserIsBlackListById(userId, userBlackId);
	}

	/**
	 *
	 * 
	 * @param
	 * @return
	 */

	@Override
	public Page<UserBlackList> queryUserBlackListByUserBlackId(Integer startPage, Integer pageSize, Integer userId) {
		final Integer id = userId;
		Specification<UserBlackList> specification = new Specification<UserBlackList>() {

			@Override
			public Predicate toPredicate(Root<UserBlackList> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {

				List<Predicate> list = new ArrayList<>();
				list.add(cb.equal(root.get("isDeleted").as(String.class), "0"));
				list.add(cb.equal(root.get("userId").as(Integer.class), id));

				return cq.where(list.toArray(new Predicate[list.size()])).getRestriction();
			}
		};
		Sort sort = new Sort(Direction.ASC, "userBlackListId");

		Pageable pageable = new PageRequest(startPage, pageSize, sort);

		return userBlackListPageRepository.findAll(specification, pageable);
	}

	/**
	 *
	 * 
	 * @param
	 * @return
	 */

	@Override
	public Integer queryBlackListCountByUserId(Integer userId) {
		return userBlackListRepository.queryBlackListCountByUserId(userId);
	}

	/**
	 *
	 * 
	 * @param
	 * @return
	 */

	@Override
	public int updateUserBlackListIsDeletedAllByUserBlackByIds(Integer userId, List<Integer> ids) {
		return userBlackListRepository.updateUserBlackListIsDeletedAllByUserBlackByIds(userId, ids);
	}
}
