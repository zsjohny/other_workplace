/**
 * 
 */
package com.ouliao.service.versionfirst;

import com.ouliao.domain.versionfirst.UserCallMark;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * 
 * 
 * @author xiaoluo
 * @version $Id: UserCallMarkService.java, 2016年2月27日 上午7:33:24
 */

public interface UserCallMarkService {
	void createUserCallMark(UserCallMark userCallMark);

	Page<UserCallMark> queryUserCallMarkByUserId(Integer starPage, Integer pageNum, Integer userId);

	int updateUserCallMarkIsDeletedByUserCallMarkId(Integer userCallMarkId, Double userCallCost, String userCallTime,
													Double userCallEarn);

	UserCallMark queryUserCallMarkIsDeletedById(Integer userCalledId, Integer userId);

	UserCallMark queryUserCallMarkByUserCallMarkId(Integer userCallMarkId);

	int updateUserCallMarkIsScoreByUserCallMarkId(Integer userCallMarkId);

	void deleteUserCallMarkIsDeletedByCallMarkId(Integer userCallMarkId);

	List<UserCallMark> queryUserCallMarkAllByUserCallMarkId(Integer userCallMarkId);

	List<UserCallMark> queryUserCallMarkIsDeletedAllById(Integer userCalledId, Integer userId);


}