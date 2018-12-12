/**
 *
 */
package com.ouliao.dao.versionfirst.impl;

import com.ouliao.dao.versionfirst.UserCallMarkDao;
import com.ouliao.domain.versionfirst.UserCallMark;
import com.ouliao.repository.versionfirst.UserCallMarkPageRepository;
import com.ouliao.repository.versionfirst.UserCallMarkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

/**
 * @author xiaoluo
 * @version $Id: UserCallMarkDaoImpl.java, 2016年2月27日 上午7:33:01
 */
@Repository
public class UserCallMarkDaoImpl implements UserCallMarkDao {
    @Autowired
    private UserCallMarkPageRepository userCallMarkPageRepository;
    @Autowired
    private UserCallMarkRepository userCallMarkRepository;

    /**
     * @param
     * @return
     */
    @Transactional(propagation = Propagation.MANDATORY)
    @Override
    public void createUserCallMark(UserCallMark userCallMark) {
//        userCallMarkRepository.createUserCallMark(userCallMark.getUserCalledId(), userCallMark.getUserCreateTime(), userCallMark.getUserId());
        userCallMarkPageRepository.saveAndFlush(userCallMark);
    }

    /**
     * @param
     * @return
     */

    @Override
    public Page<UserCallMark> queryUserCallMarkByUserId(Integer starPage, Integer pageNum, Integer userId) {

        final Integer id = userId;

        Sort sort = new Sort(Direction.DESC, "userCreateTime");
        Pageable pageable = new PageRequest(starPage, pageNum, sort);
        Specification<UserCallMark> specification = new Specification<UserCallMark>() {

            @Override
            public Predicate toPredicate(Root<UserCallMark> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                List<Predicate> lists = new ArrayList<>();
                lists.add(cb.equal(root.get("isDeleted").as(String.class), "1"));
                lists.add(cb.or(cb.equal(root.get("userId").as(Integer.class), id),
                        cb.equal(root.get("userCalledId").as(Integer.class), id)));

                return cq.where(lists.toArray(new Predicate[lists.size()])).getRestriction();
            }
        };
        return userCallMarkPageRepository.findAll(specification, pageable);
    }

    /**
     * @param
     * @return
     */

    @Override
    public int updateUserCallMarkIsDeletedByUserCallMarkId(Integer userCallMarkId, Double userCallCost,
                                                           String userCallTime, Double userCallEarn) {
        return userCallMarkRepository.updateUserCallMarkIsDeletedByUserCallMarkId(userCallMarkId, userCallCost,
                userCallTime, userCallEarn);
    }

    /**
     * @param
     * @return
     */

    @Override
    public UserCallMark queryUserCallMarkIsDeletedById(Integer userCalledId, Integer userId) {
        return userCallMarkRepository.queryUserCallMarkIsDeletedById(userCalledId, userId);
    }

    /**
     * @param
     * @return
     */

    @Override
    public UserCallMark queryUserCallMarkByUserCallMarkId(Integer userCallMarkId) {
        return userCallMarkRepository.queryUserCallMarkByUserCallMarkId(userCallMarkId);
    }

    /**
     * @param
     * @return
     */

    @Override
    public int updateUserCallMarkIsScoreByUserCallMarkId(Integer userCallMarkId) {
        return userCallMarkRepository.updateUserCallMarkIsScoreByUserCallMarkId(userCallMarkId);
    }

    /**
     * @param
     * @return
     */

    @Override
    public void deleteUserCallMarkIsDeletedByCallMarkId(Integer userCallMarkId) {
        userCallMarkPageRepository.delete(userCallMarkId);
    }

    /**
     * @param
     * @return
     */

    @Override
    public List<UserCallMark> queryUserCallMarkAllByUserCallMarkId(Integer userCallMarkId) {
        return userCallMarkRepository.queryUserCallMarkAllByUserCallMarkId(userCallMarkId);
    }

    /**
     * @param
     * @return
     */

    @Override
    public List<UserCallMark> queryUserCallMarkIsDeletedAllById(Integer userCalledId, Integer userId) {
        return userCallMarkRepository.queryUserCallMarkIsDeletedAllById(userCalledId, userId);
    }

}
