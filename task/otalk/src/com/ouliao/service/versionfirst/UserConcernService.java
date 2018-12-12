/**
 *
 */
package com.ouliao.service.versionfirst;

import com.ouliao.domain.versionfirst.User;
import com.ouliao.domain.versionfirst.UserConcern;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @author xiaoluo
 * @version $Id: UserConcernService.java, 2016年2月23日 下午9:56:50
 */

public interface UserConcernService {
    int updateUserConcernByUserConcernId(String isDeleted, Integer userConcernId, User user);

    UserConcern createUserConcern(UserConcern userConcern, User user, String userSign);

    UserConcern queryUserIsConcernById(Integer userId, Integer userOnfocusId);

    Page<UserConcern> queryUserConcernByUserConcernId(Integer startPage, Integer pageSize, Integer userConcernId);

    List<UserConcern> queryUserConcernedByUserConcernId(Integer startPage, Integer pageSize, Integer userConcernedId);

    Integer queryUserConcernByUserId(Integer userId);

    Integer queryUserConcernedByUserOnfocusId(Integer userOnfocusId);

    List<Integer> queryUserConcerndByOrder(Integer startPage, Integer pageSize);

    List<UserConcern> queryUserConcerndedAllByUserId(Integer useredId);

    List<UserConcern> queryUserConcerndAllByUserId(Integer userId);

    List<UserConcern> queryUserConcerneAlldByUserOnfocusId(Integer userOnfocusId);

    Integer queryUserConcerneAlldByUserConcernId(Integer useId);

     void updateUserContractByUserOnfocuseId(String userConract, Integer userOnfocusId);
}
