/**
 * 
 */
package com.ouliao.dao.versionfirst.impl;

import com.ouliao.dao.versionfirst.SysMsgShowDao;
import com.ouliao.domain.versionfirst.SysMsgShow;
import com.ouliao.repository.versionfirst.SysMsgShowPageRepository;
import com.ouliao.repository.versionfirst.SysMsgShowRepository;
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
 * @version $Id: SysMsgShowDaoImpl.java, 2016年3月13日 下午6:58:48
 */
@Repository
public class SysMsgShowDaoImpl implements SysMsgShowDao {
	@Autowired
	private SysMsgShowPageRepository sysMsgShowPageRepository;
	@Autowired
	private SysMsgShowRepository sysMsgShowRepository;

	/**
	 *
	 * 
	 * @param
	 * @return
	 */

	@Override
	public Page<SysMsgShow> querySysMsgShowByUserId(Integer starPage, Integer pageNum, Integer userId) {

		final Integer id = userId;

		Sort sort = new Sort(Direction.ASC, "creatTime");

		Pageable pageable = new PageRequest(starPage, pageNum, sort);

		Specification<SysMsgShow> specification = new Specification<SysMsgShow>() {

			@Override
			public Predicate toPredicate(Root<SysMsgShow> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
				List<Predicate> list = new ArrayList<>();
				list.add(cb.equal(root.get("isDeleted").as(String.class), "0"));
				list.add(cb.equal(root.get("userId").as(Integer.class), id));

				return cq.where(list.toArray(new Predicate[list.size()])).getRestriction();
			}

		};

		return sysMsgShowPageRepository.findAll(specification, pageable);
	}

	/**
	 *
	 * 
	 * @param
	 * @return
	 */

	@Override
	public void createSysMsgShow(List<SysMsgShow> sysMsgShows) {
		sysMsgShowPageRepository.save(sysMsgShows);

	}

	/**
	 *
	 * 
	 * @param
	 * @return
	 */

	@Override
	public void deletAllSysMsgShowByUserIds(Iterable<SysMsgShow> ids) {
		sysMsgShowPageRepository.delete(ids);
	}

	/**
	 *
	 * 
	 * @param
	 * @return
	 */

	@Override
	public Iterable<SysMsgShow> querySysMsgShowAll(Integer userId) {
		final Integer id = userId;

		Specification<SysMsgShow> specification = new Specification<SysMsgShow>() {

			@Override
			public Predicate toPredicate(Root<SysMsgShow> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
				List<Predicate> list = new ArrayList<>();
				list.add(cb.equal(root.get("isDeleted").as(String.class), "0"));
				list.add(cb.equal(root.get("userId").as(Integer.class), id));

				return cq.where(list.toArray(new Predicate[list.size()])).getRestriction();
			}

		};

		return sysMsgShowPageRepository.findAll(specification);

	}

	/**
	 *
	 * 
	 * @param
	 * @return
	 */

	@Override
	public long queryCountSysMsgShowByUserId(Integer UserId) {

		final Integer id = UserId;

		Specification<SysMsgShow> specification = new Specification<SysMsgShow>() {

			@Override
			public Predicate toPredicate(Root<SysMsgShow> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
				List<Predicate> list = new ArrayList<>();
				list.add(cb.equal(root.get("isDeleted").as(String.class), "0"));
				list.add(cb.equal(root.get("userId").as(Integer.class), id));

				return cq.where(list.toArray(new Predicate[list.size()])).getRestriction();
			}

		};
		return sysMsgShowPageRepository.count(specification);
	}

	@Override
	public Long queryCountBySysMsgByUserId(Integer userId) {
		return sysMsgShowRepository.queryCountBySysMsgByUserId(userId);
	}

}
