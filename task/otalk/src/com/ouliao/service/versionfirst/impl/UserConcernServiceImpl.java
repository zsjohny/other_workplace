/**
 *
 */
package com.ouliao.service.versionfirst.impl;

import com.ouliao.dao.versionfirst.UserActiveDao;
import com.ouliao.dao.versionfirst.UserConcernDao;
import com.ouliao.domain.versionfirst.User;
import com.ouliao.domain.versionfirst.UserConcern;
import com.ouliao.service.versionfirst.UserConcernService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author xiaoluo
 * @version $Id: UserConcernServiceImpl.java, 2016年2月23日 下午9:56:59
 */
@Service
@Transactional
public class UserConcernServiceImpl implements UserConcernService {
    @Autowired
    private UserConcernDao userConcernDao;

    @Autowired
    private UserActiveDao userActiveDao;

    /**
     * @param
     * @return
     */

    @Override
    public int updateUserConcernByUserConcernId(String isDeleted, Integer userConcernId, User user) {
        try {
            // 开启查询线程
            ExecutorService executorService = Executors.newFixedThreadPool(20);

            final Integer count = userConcernId;

            Runnable runnable = new Runnable() {
                @Override
                public void run() {

                    userConcernDao.timeUpdateByUserConcernByUserId(count);
                }
            };
            if (count != null) {
                executorService.execute(runnable);
            }
        } catch (Exception e) {

        }
        return userConcernDao.updateUserConcernByUserConcernId(isDeleted, userConcernId, user);
    }

    /**
     * @param
     * @return
     */

    @Override
    public UserConcern createUserConcern(final UserConcern userConcern, User user, String userSign) {

        // final Integer userId = userConcern.getUserOnfocusId();
        //
        // //增加性能测试
        // Thread thread = new Thread() {
        // public void run() {
        //
        // userConcernDao.timelyUpdateCountByUserId(userId);
        //
        // }
        //
        // };
        //
        // thread.start();

        // 开启查询线程
        ExecutorService executorService = Executors.newFixedThreadPool(20);

        final Integer count = userConcern.getUserConcernId();

        Runnable runnable = new Runnable() {
            @Override
            public void run() {

                userConcernDao.timeUpdateByUserConcernByUserId(count);
            }
        };
        if (count != null) {
            executorService.execute(runnable);
        }
        return userConcernDao.createUserConcern(userConcern, user, userSign);
    }

    /**
     * @param
     * @return
     */

    @Override
    public UserConcern queryUserIsConcernById(Integer userId, Integer userOnfocusId) {
        return userConcernDao.queryUserIsConcernById(userId, userOnfocusId);
    }

    /**
     * @param
     * @return
     */

    @Override
    public Page<UserConcern> queryUserConcernByUserConcernId(Integer startPage, Integer pageSize,
                                                             Integer userConcernId) {
        return userConcernDao.queryUserConcernByUserConcernId(startPage, pageSize, userConcernId);
    }

    /**
     * @param
     * @return
     */

    @Override
    public Integer queryUserConcernByUserId(Integer userId) {
        return userConcernDao.queryUserConcernByUserId(userId);
    }

    /**
     * @param
     * @return
     */

    @Override
    public Integer queryUserConcernedByUserOnfocusId(Integer userOnfocusId) {
        return userConcernDao.queryUserConcernedByUserOnfocusId(userOnfocusId);
    }

    /**
     * @param
     * @return
     */

    @Override
    public List<Integer> queryUserConcerndByOrder(Integer startPage, Integer pageSize) {
        return userConcernDao.queryUserConcerndByOrder(startPage, pageSize);
    }

    /**
     * @param
     * @return
     */

    @Override
    public List<UserConcern> queryUserConcerndAllByUserId(Integer userId) {
        return userConcernDao.queryUserConcerndAllByUserId(userId);
    }

    // @Cacheable(value = "mycache", key = "#userOnfocusId")
    @Override
    public List<UserConcern> queryUserConcerneAlldByUserOnfocusId(Integer userOnfocusId) {
        return userConcernDao.queryUserConcerneAlldByUserOnfocusId(userOnfocusId);
    }

    @Cacheable(value = "mycache", key = "#useId")
    @Override
    public Integer queryUserConcerneAlldByUserConcernId(Integer useId) {

        List<UserConcern> userConcerns = userConcernDao.queryUserConcerneAlldByUserOnfocusId(useId);
        int k = 0;
        if (userConcerns != null && userConcerns.size() != 0) {

            for (UserConcern userConcern : userConcerns) {

                if (userConcern == null) {
                    continue;

                }
                List<UserConcern> userConcers = userConcernDao
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

    @Override
    public void updateUserContractByUserOnfocuseId(String userConract, Integer userOnfocusId) {

        userConcernDao.updateUserContractByUserOnfocuseId(userConract, userOnfocusId);
    }

    /**
     * @param
     * @return
     */

    @Override
    public List<UserConcern> queryUserConcernedByUserConcernId(Integer startPage, Integer pageSize,
                                                               Integer userConcernedId) {
        return userConcernDao.queryUserConcernedByUserConcernId(startPage, pageSize, userConcernedId);
    }

    /**
     * @param
     * @return
     */

    @Override
    public List<UserConcern> queryUserConcerndedAllByUserId(Integer useredId) {
        return userConcernDao.queryUserConcerndedAllByUserId(useredId);
    }
}
