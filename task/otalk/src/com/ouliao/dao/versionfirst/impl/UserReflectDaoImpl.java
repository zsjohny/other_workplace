/**
 * 
 */
package com.ouliao.dao.versionfirst.impl;

import com.ouliao.dao.versionfirst.UserReflectDao;
import com.ouliao.domain.versionfirst.UserReflect;
import com.ouliao.repository.versionfirst.UserReflectCrudRepository;
import com.ouliao.repository.versionfirst.UserReflectPageRepository;
import com.ouliao.repository.versionfirst.UserReflectRepository;
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
import java.util.List;

/**
 * 
 * 
 * @author xiaoluo
 * @version $Id: UserReflectCrudDaoImpl.java, 2016年2月26日 下午1:29:18
 */
@Repository
public class UserReflectDaoImpl implements UserReflectDao {
	@Autowired
	private UserReflectCrudRepository userReflectCrudRepository;
	@Autowired
	private UserReflectRepository userReflectRepository;

	@Autowired
	private UserReflectPageRepository userReflectPageRepository;

	/**
	 *
	 * 
	 * @param
	 * @return
	 */

	@Override
	public void creatUserReflect(UserReflect userReflect) {
		userReflectCrudRepository.save(userReflect);
	}

	/**
	 *
	 * 
	 * @param
	 * @return
	 */

	@Override
	public Long queryReflectCountByAll() {
		return userReflectRepository.queryCountByIsDeleted();
	}

	/**
	 *
	 * 
	 * @param
	 * @return
	 */

	@Override
	public int updateIsDeletedByUserReflectId(List<Integer> ids) {
		return userReflectRepository.updateIsDeletedByUserReflectId(ids);
	}

	/**
	 *
	 * 
	 * @param
	 * @return
	 */

	@Override
	public List<UserReflect> queryUserReflectAllByIsDeleted() {
		return userReflectRepository.queryUserReflectAllByIsDeleted();
	}

	/**
	 *
	 * 
	 * @param
	 * @return
	 */

	@Override
	public Page<UserReflect> queryUserReflectWithDrawByUserId(Integer starPage, Integer pageNum, Integer userId) {
		final Integer id = userId;

		Sort sort = new Sort(Direction.DESC, "createTime");

		Pageable pageable = new PageRequest(starPage, pageNum, sort);

		Specification<UserReflect> specification = new Specification<UserReflect>() {

			@Override
			public Predicate toPredicate(Root<UserReflect> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {

				List<Predicate> list = new ArrayList<>();
				list.add(cb.equal(root.get("isDeleted").as(String.class), "1"));
				list.add(cb.equal(root.get("userId").as(Integer.class), id));

				return cq.where(list.toArray(new Predicate[list.size()])).getRestriction();
			}

		};

		return userReflectPageRepository.findAll(specification, pageable);
	}

	/**
	 *
	 * 
	 * @param
	 * @return
	 */

	@Override
	public List<UserReflect> queryUserReflectWithDrawAllByUserId(Integer userId) {
		return userReflectRepository.queryUserReflectWithDrawAllByUserId(userId);
	}

}
