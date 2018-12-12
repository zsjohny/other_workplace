/**
 *
 */
package com.ouliao.dao.versionfirst.impl;

import com.ouliao.dao.versionfirst.UserConcernDao;
import com.ouliao.domain.versionfirst.User;
import com.ouliao.domain.versionfirst.UserActive;
import com.ouliao.domain.versionfirst.UserConcern;
import com.ouliao.repository.versionfirst.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author xiaoluo
 * @version $Id: UserConcernDaoImpl.java, 2016年2月23日 下午9:56:25
 */
@Repository
public class UserConcernDaoImpl implements UserConcernDao {

    @Autowired
    private UserConcernRepository userConcernRepository;
    @Autowired
    private UserConcernPageRepository userConcernPageRepository;
    @Autowired
    private OuLiaoRepository ouLiaoRepository;
    @Autowired
    private UserActiveRepository userActiveRepository;

    @Autowired
    private UserActiveCrudRepository userActiveCrudRepository;

    /**
     * @param
     * @return
     */

    @Override
    public int updateUserConcernByUserConcernId(String isDeleted, Integer userConcernId, User user) {

        if (StringUtils.isNotEmpty(user.getIsFirstConcern())) {

            List<UserActive> userActives = userActiveRepository.queryUserActiveAllByUserId(user.getFirstConcernId());

            if ("1".equals(isDeleted)) {

                if ("0".equals(user.getIsFirstConcern())) {
                    if (userActives != null & userActives.size() != 0) {
                        Integer count = userActives.get(0).getUserCount();
                        count--;
                        if (count < 0) {
                            count = 0;
                        }
                        userActiveRepository.updateUserCountByUserId(count, user.getFirstConcernId());

                        ouLiaoRepository.updateOwnerFirstConcernByUserId("1", user.getFirstConcernId(),
                                user.getUserId());
                    }
                }
            } else {

                if ("1".equals(user.getIsFirstConcern())) {
                    if (userActives != null & userActives.size() != 0) {
                        Integer count = userActives.get(0).getUserCount();
                        count++;
                        userActiveRepository.updateUserCountByUserId(count, user.getFirstConcernId());

                        ouLiaoRepository.updateOwnerFirstConcernByUserId("0", user.getFirstConcernId(),
                                user.getUserId());
                    }
                }

            }
        }

        // 开线程进行查询

        return userConcernRepository.updateUserConcernByUserConcernId(isDeleted, userConcernId);
    }

    /**
     * @param
     * @return
     */

    @Override
    public UserConcern createUserConcern(UserConcern userConcern, User user, String userSign) {

        if (user == null) {
            return null;
        }

        if (!"service".equals(userSign)) {

            // 新版本的问题兼容
            // customer
            try {
                if (!"customer".equals(userSign)) {

                    JedisPool jedisPool = new JedisPool("localhost", 10088);
                    Jedis jedis = jedisPool.getResource();
                    Integer count = 0;
                    if (jedis.exists(userSign)) {
                        count = Integer.valueOf(jedis.get(userSign));
                    } else {
                        jedis.setex(userSign, 60 * 60 * 24 * 30, "4");
                    }

                    if (count > 5) {
                        return userConcernPageRepository.save(userConcern);
                    }
                    count++;
                    jedis.setex(userSign, 60 * 60 * 24 * 30, String.valueOf(count));
                    jedis.disconnect();
                }

            } catch (Exception e) {

            }

            if (StringUtils.isEmpty(user.getIsFirstConcern())) {
                List<UserActive> userActives = userActiveRepository
                        .queryUserActiveAllByUserId(userConcern.getUserOnfocusId());

                if (userActives != null & userActives.size() != 0) {
                    Integer count = userActives.get(0).getUserCount();
                    count++;
                    userActiveRepository.updateUserCountByUserId(count, userConcern.getUserOnfocusId());

                } else {

                    UserActive userActive = new UserActive();

                    userActive.setUserId(userConcern.getUserOnfocusId());
                    userActive.setUserCount(1);
                    userActive.setCreateTime(new Date());

                    userActiveCrudRepository.save(userActive);
                }
                ouLiaoRepository.updateOwnerFirstConcernByUserId("0", userConcern.getUserOnfocusId(), user.getUserId());
            }

        }

        return userConcernPageRepository.save(userConcern);
    }

    /**
     * @param
     * @return
     */

    @Override
    public UserConcern queryUserIsConcernById(Integer userId, Integer userOnfocusId) {

        return userConcernRepository.queryUserIsConcernById(userId, userOnfocusId);
    }

    /**
     * @param
     * @return
     */

    @Override
    public Page<UserConcern> queryUserConcernByUserConcernId(Integer startPage, Integer pageSize,
                                                             Integer userConcernId) {
        final Integer id = userConcernId;
        Specification<UserConcern> specification = new Specification<UserConcern>() {

            @Override
            public Predicate toPredicate(Root<UserConcern> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {

                List<Predicate> list = new ArrayList<>();
                list.add(cb.equal(root.get("isDeleted").as(String.class), "0"));
                list.add(cb.equal(root.get("userId").as(Integer.class), id));

                return cq.where(list.toArray(new Predicate[list.size()])).getRestriction();
            }
        };
        Sort sort = new Sort(Direction.DESC, "userModifyTime");

        Pageable pageable = new PageRequest(startPage, pageSize, sort);

        return userConcernPageRepository.findAll(specification, pageable);
    }

    /**
     * @param
     * @return
     */

    @Override
    public List<UserConcern> queryUserConcernedByUserConcernId(Integer startPage, Integer pageSize,
                                                               Integer userConcernedId) {
        // final Integer id = userConcernedId;
        // Specification<UserConcern> specification = new
        // Specification<UserConcern>() {
        //
        // @Override
        // public Predicate toPredicate(Root<UserConcern> root, CriteriaQuery<?>
        // cq, CriteriaBuilder cb) {
        //
        // List<Predicate> list = new ArrayList<>();
        // list.add(cb.equal(root.get("isDeleted").as(String.class), "0"));
        // list.add(cb.equal(root.get("userOnfocusId").as(Integer.class), id));
        //
        // return cq.where(list.toArray(new
        // Predicate[list.size()])).getRestriction();
        // }
        // };
        // Sort sort = new Sort(Direction.DESC, "userModifyTime");
        //
        // Pageable pageable = new PageRequest(startPage, pageSize, sort);
        //
        // return userConcernPageRepository.findAll(specification, pageable);
        return userConcernRepository.queryUserConcernedByUserConcernId(userConcernedId, (startPage - 1) * pageSize,
                pageSize);
    }

    /**
     * @param
     * @return
     */

    @Override
    public Integer queryUserConcernByUserId(Integer userId) {
        return userConcernRepository.queryUserConcernByUserId(userId);
    }

    /**
     * @param
     * @return
     */

    @Override
    public Integer queryUserConcernedByUserOnfocusId(Integer userOnfocusId) {
        return userConcernRepository.queryUserConcernedByUserOnfocusId(userOnfocusId);
    }

    /**
     * @param
     * @return
     */

    @Override
    public List<Integer> queryUserConcerndByOrder(Integer startPage, Integer pageSize) {

        // select userId from userconcern where isDeleted='0' group by userId
        // ORDER BY count(*) DESC
        // Specification<UserConcern> specification = new
        // Specification<UserConcern>() {
        //
        // @Override
        // public Predicate toPredicate(Root<UserConcern> root, CriteriaQuery<?>
        // cq, CriteriaBuilder cb) {
        // // cq.multiselect(root.get("userId"));
        // // cb.equal(root.get("isDeleted").as(String.class), "0");
        // // cq.groupBy(root.get("userId"));
        // //
        // // cq.orderBy(cb.desc(root.get("count(*)")));
        // cq.multiselect(root.get("userId"));
        // cb.equal(root.get("isDeleted").as(String.class), "0");
        // cq.groupBy(root.get("userId"));
        // cq.orderBy(cb.desc("count(*)"));
        // return cq.getRestriction();
        // }
        // };
        //
        // Pageable pageable = new PageRequest(startPage, pageSize);
        //
        // return userConcernPageRepository.findAll(specification, pageable);

        return userConcernRepository.queryUserConcerndByOrder(startPage, pageSize);

    }

    /**
     * @param
     * @return
     */

    @Override
    public List<UserConcern> queryUserConcerndAllByUserId(Integer userId) {
        return userConcernRepository.queryUserConcerndAllByUserId(userId);
    }

    /**
     * @param
     * @return
     */

    @Override
    public List<UserConcern> queryUserConcerndedAllByUserId(Integer useredId) {
        return userConcernRepository.queryUserConcerndedAllByUserId(useredId);
    }

    @Override
    public List<UserConcern> queryUserConcerneAlldByUserOnfocusId(Integer userOnfocusId) {
        return userConcernRepository.queryUserConcerneAlldByUserOnfocusId(userOnfocusId);
    }

    @Override
    public List<UserConcern> queryUserConcerneAlldByUserConcernId(Integer userId) {
        return userConcernRepository.queryUserConcerneAlldByUserConcernId(userId);
    }

    @CachePut(value = "mycache", key = "#useId")
    public Integer timelyUpdateCountByUserId(Integer useId) {
        List<UserConcern> userConcerns = userConcernRepository.queryUserConcerneAlldByUserOnfocusId(useId);
        int k = 0;
        if (userConcerns != null && userConcerns.size() != 0) {

            for (UserConcern userConcern : userConcerns) {

                if (userConcern == null) {
                    continue;

                }
                List<UserConcern> userConcers = userConcernRepository
                        .queryUserConcerneAlldByUserConcernId(userConcern.getUserId());

                if (userConcers != null && userConcers.size() > 1) {

                    if (userConcers.get(1).getUserOnfocusId().equals(useId)
                            && "0".equals(userConcers.get(1).getIsDeleted())) {

                        k++;
                    }

                }
            }
        }

        return k;
    }

    @CachePut(value = "mycache", key = "#useId")
    public Integer timeUpdateByUserConcernByUserId(Integer useId) {

        return userConcernRepository.queryUserConcernedByUserOnfocusId(useId);
    }

    @Override
    public void updateUserContractByUserOnfocuseId(String userConract, Integer userOnfocusId) {
        int count = userConcernRepository.updateUserContractByUserOnfocuseId(userConract, userOnfocusId);


    }
}
