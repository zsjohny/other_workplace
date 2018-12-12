/**
 * 
 */
package com.ouliao.service.versionfirst;

import com.ouliao.domain.versionfirst.UserBlackList;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * 
 * 
 * @author xiaoluo
 * @version $Id: UserBlackListService.java, 2016年2月23日 下午6:28:13
 */

public interface UserBlackListService {
	int updateUserBlackListByUserBlackListId(String isDeleted, Integer userBlackListId);

	UserBlackList createUserBlackList(UserBlackList userBlackList);

	UserBlackList queryUserIsBlackListById(Integer userId, Integer userBlackId);

	Page<UserBlackList> queryUserBlackListByUserBlackId(Integer startPage, Integer pageSize, Integer userBlackId);

	Integer queryBlackListCountByUserId(Integer userId);

	int updateUserBlackListIsDeletedAllByUserBlackByIds(Integer userId, List<Integer> ids);
}
